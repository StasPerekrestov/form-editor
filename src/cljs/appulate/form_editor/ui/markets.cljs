(ns appulate.form-editor.ui.markets
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [goog.string :as gstring]))

(defn market-view [market owner]
  (reify
    om/IRender
    (render [_]
            (apply dom/div #js {:className "row"}
                   (let [{:keys [id name]} market
                         chk-id (str "selectedMarket" id)]
                     [(dom/div #js {:className "small-2 large-2 columns"}
                               (dom/input #js {:type "checkbox" :id chk-id}
                                          (dom/label #js {:htmlFor chk-id} name)))
                      (dom/div #js {:className "small-2 large-2 columns"}
                               (dom/span #js {:className "round label"} "$3800"))
                      (dom/div #js {:className "small-2 large-2 columns"}
                               (dom/span #js {:className "round success label"} "not submitted yet"))
                      (dom/div #js {:className "small-2 large-2 columns"}
                               (dom/a #js {:href "#" :style #js {:text-decoration "Underline"}} "Select"))
                      (dom/div #js {:className "small-4 large-4 columns"} "")])))))

(defn selected-view [markets owner]
  (reify
    om/IRender
    (render [_]
            (apply dom/div nil
                   (flatten [(dom/div #js {:className "row"}
                                      (dom/div #js {:className "small-2 large-2 columns"} "MARKETS")
                                      (dom/div #js {:className "small-2 large-2 columns"} "EST. PREMIUM")
                                      (dom/div #js {:className "small-2 large-2 columns"} "STATUS")
                                      (dom/div #js {:className "small-4 large-4 columns"} "ACTION")
                                      (dom/div #js {:className "small-2 large-2 columns"} "QUOTE"))
                            (apply dom/div #js {:className "scrollable"}
                              (om/build-all market-view markets))])))))
