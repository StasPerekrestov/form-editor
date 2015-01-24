(ns appulate.form-editor.ui.marketing
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [appulate.form-editor.ui.markets :as markets]
   [appulate.form-editor.ui.qa :as qa]))

(defn insured [{:keys [name]} owner]
  (reify
    om/IRender
    (render [_]
            (dom/div nil name))))

(defn section [data owner]
  (reify
    om/IRender
    (render [_]
            (dom/div nil
                     (dom/div #js {:className "row"}
                              (dom/div #js {:className "small-12 large-12 columns"}
                                       (om/build insured (:insured data))))
                     (dom/div #js {:className "row"} (om/build markets/selected-view (:markets data)))

                     (dom/div #js {:className "row"}
                              (dom/div #js {:className "small-12 large-12 columns" }
                                       (dom/ul #js {:className "tabs" :data-tab nil}
                                               (dom/li #js {:className "tab-title active"}
                                                       (dom/a #js {:href "#questionarie-tab"} "Questionarie"))
                                               (dom/li #js {:className "tab-title"}
                                                       (dom/a #js {:href "#forms-tab"} "Forms"))
                                               (dom/li #js {:className "tab-title"}
                                                       (dom/a #js {:href "#lossRuns-tab"} "LOSS RUNS"))
                                               (dom/li #js {:className "tab-title"}
                                                       (dom/a #js {:href "#other-docs-tab"} "OTHER DOCS"))
                                               (dom/li #js {:className "tab-title"}
                                                       (dom/a #js {:href "#notes-tab"} "NOTES")))
                                       (dom/div #js {:className "tabs-content"}
                                                (dom/div #js {:className "content active" :id "questionarie-tab"}
                                                         (om/build qa/qa-view (:application data)))
                                                (dom/div #js {:className "content" :id "forms-tab"})
                                                (dom/div #js {:className "content" :id "lossRuns-tab"})
                                                (dom/div #js {:className "content" :id "other-docs-tab"})
                                                (dom/div #js {:className "content" :id "notes-tab"}))))))))
