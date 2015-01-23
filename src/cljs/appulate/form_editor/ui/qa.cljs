(ns appulate.form-editor.ui.qa
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require
   [cljs.core.async :refer [put! chan <!]]
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [appulate.form-editor.ui.editors :as editors]))

(defn section-selector [section-data owner]
  "Renders a view that allows to select a particular section"
  (reify
    om/IRender
    (render [_]
            (let [{:keys [ch selected section]} section-data]
              (let [{:keys [name id]} section]
                (dom/dd #js {:className (if (true? selected) "active")} (dom/a #js {:href "#" :onClick (fn[e]
                                                                                                 (put! ch id)
                                                                                                 (->>
                                                                                                  e
                                                                                                  (.preventDefault)))} name)))))))

(defn qs-editor [qs owner]
  "Renders questions an answers section"
  (reify
    om/IRender
    (render [_]
            (let [{:keys [id name type]} qs]
            (dom/div nil
                     (dom/label nil name)
                     (om/build editors/text-box "put text here"))))))

(defn qa-view [application owner]
  (reify
    om/IInitState
    (init-state [_]
                {:selection-ch (chan)})
    om/IWillMount
    (will-mount [_]
                (let [selection-ch (om/get-state owner :selection-ch)]
                  (go (loop [section-id (<! selection-ch)]
                        (om/update! application [:selected-section-id] section-id)
                        (recur (<! selection-ch))))))
    om/IRender
    (render [_]
            (apply dom/div nil
                     (let [{:keys [sections selected-section-id]} application
                           selection-ch (om/get-state owner :selection-ch)
                           selected-section (first (filter #(= (:id %1) selected-section-id) sections))
                           ]
                       [(apply dom/dl #js {:className "sub-nav"}
                              (map (fn [s]
                                     (om/build section-selector {:selected (= s selected-section)
                                                                 :section s
                                                                 :ch selection-ch} ))
                                   sections))

                       (if-not (nil? selected-section)
                           (apply dom/div #js {:className "panel"}
                                  (om/build-all qs-editor (:questions selected-section))))]
                       )))))
