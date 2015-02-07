(ns appulate.form-editor.event-bus.bus
    (:require-macros [cljs.core.async.macros :refer [go alt!]])
    (:require [cljs.core.async :refer [put! <! >! chan timeout]]
              [cljs-http.client :as http]
              [taoensso.sente  :as sente :refer (cb-success?)]))


(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/ws" ; Note the same path as before
       {:type :auto ; e/o #{:auto :ajax :ws}
       })]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

(def publish-ch (chan))

(def notif-ch (chan))


(go
  (loop [msg (<! publish-ch)]
    (chsk-send! [:fe/broadcast msg])
    (recur (<! publish-ch))))

(defmulti message-dispatcher (fn [{:keys [event]}]
                               (let [[code body] event]
                                 (if (= :chsk/recv code)
                                   (let [[cd _] body]
                                     cd)
                                   :ignore)
                                 )))

(defmethod message-dispatcher :default [msg])

(defmethod message-dispatcher :fe/mesg [{[_ msg] :?data}]
  (put! notif-ch msg))

(go
  (loop [msg (<! ch-chsk)]
    (message-dispatcher msg)
    (recur (<! ch-chsk))))