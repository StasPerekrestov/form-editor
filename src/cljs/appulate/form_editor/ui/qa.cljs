(ns appulate.form-editor.ui.qa
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require
   [cljs.core.async :refer [put! chan <!]]
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn section-selector [section-data owner]
  (reify
    om/IRender
    (render [_]
            (let [{:keys [ch selected-id section]} section-data]
              (let [{:keys [name id]} section]
                (dom/dd #js {:className (if (= selected-id id) "active")} (dom/a #js {:href "#" :onClick (fn[e]
                                                                (put! ch id)
                                                                (->>
                                                                   e
                                                                   (.preventDefault)))} name)))))))


(defn qa-view [application owner]
  (reify
    om/IInitState
    (init-state [_]
                {:selection-ch (chan)})
    om/IWillMount
    (will-mount [_]
                (let [selection-ch (om/get-state owner :selection-ch)]
                  (go (loop [section-id (<! selection-ch)]
                        (om/update! application [:selected-section] section-id)
                        (recur (<! selection-ch))))))
    om/IRender
    (render [_]
            (dom/div nil
                    (let [{:keys [sections selected-section]} application
                          selection-ch (om/get-state owner :selection-ch)]
                     (apply dom/dl #js {:className "sub-nav"}
                            (map (fn [s]
                                   (om/build section-selector {:selected-id selected-section
                                                               :section s
                                                               :ch selection-ch} ))
                                 sections)))))))
