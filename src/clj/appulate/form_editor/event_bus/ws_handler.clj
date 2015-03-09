(ns appulate.form-editor.event-bus.ws-handler
  (:require
    [clojure.core.async :refer [put! <! >! chan timeout go close!]]
    [taoensso.sente :as sente]
    [taoensso.sente.server-adapters.http-kit :as sente-adapters]
    [cheshire.core :refer [parse-string]]))

(let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
              connected-uids]}
      (sente/make-channel-socket! sente-adapters/http-kit-adapter {
                                   ; we need this stuff to be able to distinguish
                                   ; only newly joined clients
                                   :user-id-fn (fn [ring-request]
                                                 (let [{:keys [params]} ring-request]
                                                  ;fake :user-id = :client-id
                                                   (:client-id params)))
                                   })]
  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids) ; Watchable, read-only atom
  )

(defmulti message-dispatcher (fn [msg] (:id msg)))
(defmethod message-dispatcher :default [msg])

(defmethod message-dispatcher :fe/broadcast [{:keys [id ?data connected-uids ring-req]}]
  (let [
        client-id (get-in ring-req [:params :client-id])
        uids (filter #(not= client-id %1) (:any @connected-uids))
        ]
    (println "ClientId:" client-id)
    (doseq [uid uids]
      (println "Sending: " ?data "to" uid)
      (chsk-send! uid [:fe/mesg ?data]))))

(go
  (loop [msg (<! ch-chsk)]
    (message-dispatcher msg)
    (recur (<! ch-chsk))))
