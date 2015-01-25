(ns appulate.form-editor.core
    (:require [goog.events :as events]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [appulate.form-editor.utils :refer [guid]]
              [appulate.form-editor.event-bus.bus :as finance]
              [appulate.form-editor.data :as data]
              [appulate.form-editor.ui.marketing :as marketing]
              [figwheel.client :as fw]))

;; Lets you do (prn "stuff") to the console
(enable-console-print!)

(defonce app-state
  (atom (data/init)))

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
            (dom/div nil
                (om/build fe-stub app)))))

;see http://blog.michielborkent.nl/blog/2014/09/25/figwheel-keep-Om-turning/
(defn main []
   (om/root fe-app app-state {:target (.getElementById js/document "app")}))

(defonce initial-call-to-main (main))

(fw/watch-and-reload
 :jsload-callback (fn []
                    (main)
                    ;; (stop-and-start-my app)
                    ))
