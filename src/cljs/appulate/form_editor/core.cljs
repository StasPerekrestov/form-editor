(ns appulate.form-editor.core
    (:require-macros [cljs.core.async.macros :refer [go alt!]])
    (:require [goog.events :as events]
              [cljs.core.async :refer [put! <! >! chan]]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [appulate.form-editor.utils :refer [guid]]
              [appulate.form-editor.event-bus.bus :as ebus]
              [appulate.form-editor.data :as data]
              [appulate.form-editor.ui.marketing :as marketing]
              [appulate.form-editor.ui.off-canvas :as ofc]))

;; Lets you do (prn "stuff") to the console
(enable-console-print!)

(defonce app-state
  (atom (data/init)))

(go
  (loop [msg (<! ebus/notif-ch)]
    (swap! app-state (fn [e] (->> e
                                  (:notifications)
                                  (cons {:message msg})
                                  (assoc-in e [:notifications]))))
      (recur (<! ebus/notif-ch))))

(defn fe-stub [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build marketing/section data)))))

(defn fe-app [app owner]
  (reify
    om/IRender
    (render [_]
      (om/build ofc/off-canvas
                [(get-in app [:application :sections 0 :questions 0 :value] "New Insured")
                 (->>
                   (om/build ofc/label "Left Panel")
                   (conj (om/build-all ofc/link-item ["LItem1" "LItem2" "LItem3"])))
                 (om/build fe-stub app)
                 (conj (om/build-all ofc/link-item (->>
                                                     (:notifications app)
                                                     (map (fn [{{:keys [message from]} :message}] 
                                                            (str (name from) ": " message)))))
                       (om/build ofc/msg-editor {:ch ebus/publish-ch})
                       (om/build ofc/label "Messages")
                       )]))))

;see http://blog.michielborkent.nl/blog/2014/09/25/figwheel-keep-Om-turning/
(defn main []
   (om/root fe-app app-state {:target (.getElementById js/document "app")}))

(defonce initial-call-to-main (main))
