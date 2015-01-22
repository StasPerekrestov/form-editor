(ns appulate.form-editor.core
    (:require-macros [cljs.core.async.macros :refer [go alt!]])
    (:require [goog.events :as events]
              [cljs.core.async :refer [put! <! >! chan timeout]]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [cljs-http.client :as http]
              [appulate.form-editor.utils :refer [guid]]
              [appulate.form-editor.event-bus.bus :as finance]
              [appulate.form-editor.data :as data]
              [appulate.form-editor.ui.marketing :as marketing]
              [figwheel.client :as fw]))

;; Lets you do (prn "stuff") to the console
(enable-console-print!)

(def app-state
  (atom (data/init)))

(defonce
  re-render-ch (chan))


(defn fe-stub [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (go (loop []
            (when (<! re-render-ch)
              ;(println "refreshing")
              (om/refresh! owner)
              (recur)))))
    om/IRender
    (render [_]
      (dom/div nil
               (om/build marketing/section data)))))

(defn fe-app [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/div nil
                (om/build fe-stub app)))))

(om/root fe-app app-state {:target (.getElementById js/document "app")})

(fw/watch-and-reload
 :jsload-callback (fn []
                    (put! re-render-ch true)
                    ;; (stop-and-start-my app)
                    ))
