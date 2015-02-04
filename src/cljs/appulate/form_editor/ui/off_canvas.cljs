(ns appulate.form-editor.ui.off-canvas
  (:require [goog.events :as events]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))


(defn ^:private left-panel-top [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/section #js {:className "left-small"}
                   (dom/a #js {:className "left-off-canvas-toggle menu-icon"
                               :href "#"
                               :aria-expanded "false"}
                          (dom/span nil))))))

(defn ^:private right-panel-top [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/section #js {:className "right-small"}
                   (dom/a #js {:className "right-off-canvas-toggle menu-icon"
                               :href "#"
                               :aria-expanded "false"}
                          (dom/span nil))))))

(defn ^:private middle-panel-top [title owner]
  (reify
    om/IRender
    (render [_]
      (dom/section #js {:className "middle tab-bar-section"}
         (dom/h1 #js {:className "title"} title)))))

(defn left-panel [items owner]
  (reify
    om/IRender
    (render [_]
      (println "Items type" (type items))
      (dom/aside #js {:className "left-off-canvas-menu"}
         (apply dom/ul #js {:className "off-canvas-list"} items)))))

(defn right-panel [items owner]
  (reify
    om/IRender
    (render [_]
      (dom/aside #js {:className "right-off-canvas-menu"}
                 (apply dom/ul #js {:className "off-canvas-list"}
                         items)))))

(defn middle-panel [panel owner]
  (reify
    om/IRender
    (render [_]
      (dom/aside #js {:className "main-section"}
                 (dom/div nil
                          panel)))))
(defn label [title]
  (reify
    om/IRender
    (render [_]
      (dom/li nil (dom/label nil title)))))

(defn link-item [title]
  (reify
    om/IRender
    (render [_]
      (dom/li nil (dom/a #js {:href "#"} title)))))

(defn off-canvas [[header left-items middle right-items] owner]
  "Takes a title, left panel content, middle panel content and right panel content
  and renders an off-canvas control"
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:data-offcanvas "" :className "off-canvas-wrap"}
               (dom/div #js {:className "inner-wrap"}
                        (dom/nav #js {:className "tab-bar"}
                                 (om/build left-panel-top nil)
                                 (om/build middle-panel-top header)
                                 (om/build right-panel-top nil))

                                 (om/build left-panel left-items)
                                 (om/build middle-panel middle)
                                 (om/build right-panel right-items))))))