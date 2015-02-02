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
      (dom/div #js {:data-offcanvas "" :className "off-canvas-wrap"}
          (dom/div #js {:className "inner-wrap"}
            (dom/nav #js {:className "tab-bar"}
               (dom/section #js {:className "left-small"}
                            (dom/a #js {:className "left-off-canvas-toggle menu-icon"
                                        :href "#"
                                        :aria-expanded "false"}
                                   (dom/span nil)))
               (dom/section #js {:className "middle tab-bar-section"}
                 (dom/h1 #js {:className "title"} "Marketing"))
               (dom/section #js {:className "right-small"}
                            (dom/a #js {:className "right-off-canvas-toggle menu-icon"
                                        :href "#"
                                        :aria-expanded "false"}
                                   (dom/span nil))))
            (dom/aside #js {:className "left-off-canvas-menu"}
                       (dom/ul #js {:className "off-canvas-list"}
                          (dom/li nil (dom/label nil "Label"))
                          (dom/li nil (dom/a #js {:href "#"} "Item 1"))
                          (dom/li nil (dom/a #js {:href "#"} "Item 2"))
                          (dom/li nil (dom/a #js {:href "#"} "Item 3"))))
            (dom/aside #js {:className "right-off-canvas-menu"}
                       (dom/ul #js {:className "off-canvas-list"}
                               (dom/li nil (dom/label nil "Label"))
                               (dom/li nil (dom/a #js {:href "#"} "Item 1"))
                               (dom/li nil (dom/a #js {:href "#"} "Item 2"))
                               (dom/li nil (dom/a #js {:href "#"} "Item 3"))))
            (dom/aside #js {:cla "main-section"}
                       (dom/div nil
                                (om/build fe-stub app))))))))

;see http://blog.michielborkent.nl/blog/2014/09/25/figwheel-keep-Om-turning/
(defn main []
   (om/root fe-app app-state {:target (.getElementById js/document "app")}))

(defonce initial-call-to-main (main))

(fw/watch-and-reload
 :jsload-callback (fn []
                    (main)
                    ;; (stop-and-start-my app)
                    ))
