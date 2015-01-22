(ns appulate.form-editor.ui.marketing
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [appulate.form-editor.ui.markets :as markets]
   [appulate.form-editor.ui.qa :as qa]))

(defn insured [insured owner]
  (reify
    om/IRender
    (render [_]
        (let [{:keys [name]} insured]
            (dom/div nil name)))))

(defn section [data owner]
  (reify
    om/IRender
    (render [_]
            (dom/div nil
              (dom/div #js {:className "row"}
                (dom/div #js {:className "small-12 large-12 columns"}
                         (om/build insured (:insured data))))
              (dom/div #js {:className "row"} (om/build markets/selected-view (:markets data)))
              (dom/div #js {:className "row"} (om/build qa/qa-view (:application data)))))))
