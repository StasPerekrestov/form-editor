(ns appulate.form-editor.ui.markets
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require
   [cljs.core.async :refer [put! chan <!]]
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [appulate.form-editor.data :refer [sort-markets]]
   [goog.string :as gstring]))

(defn market-view [market owner {:keys [selection-ch]}]
  (reify
    om/IRender
    (render [_]
          (let [{:keys [id name selected]} market
                chk-id (str "selectedMarket" id)
                invert-selection #(put! selection-ch market)
                ]
            (apply dom/div #js {:className "row" :key name}
                     [(dom/div #js {:className "small-2 large-2 columns"}
                               (dom/input #js {:type "checkbox"
                                               :id chk-id
                                               :checked selected
                                               :onChange invert-selection}
                                          (dom/label #js {:htmlFor chk-id} name)))
                      (dom/div #js {:className "small-2 large-2 columns"}
                               (dom/span #js {:className "round label"} "$3800"))
                      (dom/div #js {:className "small-2 large-2 columns"}
                               (dom/span #js {:className "round success label"} "not submitted yet"))
                      (dom/div #js {:className "small-2 large-2 columns"}
                               (dom/a #js {:href "#" :style #js {:textDecoration "Underline"}
                                           :onClick (fn [e]
                                                          (.stopPropagation e)
                                                          (invert-selection))} "Select"))
                      (dom/div #js {:className "small-4 large-4 columns"} "")])))))

(defn selected-view [markets owner]
  (reify
    om/IInitState
    (init-state [_]
      {:selection-ch (chan)})
    om/IWillMount
    (will-mount [_]
      (let [{:keys [selection-ch]} (om/get-state owner)]
        (go
          (loop [market (<! selection-ch)]
            (om/transact! markets [] (fn [ms]
                                         (let [indices (fn [pred coll]
                                                 (keep-indexed #(when (pred %2) %1) coll))
                                               idx (first (indices #(= market %1) ms))
                                               selected (:selected market)
                                               mn (assoc market :selected (not selected))
                                               ]
                                           (sort-markets (assoc-in ms [idx] mn))))
                                       )
            (recur (<! selection-ch)))))
      )
    om/IRenderState
    (render-state [_ {:keys [selection-ch]}]
        (let [ m-cnt (count markets)
               m-cnt-selected (count (filter (fn [{selected :selected}]
                                                (true? selected)) markets))]
            (apply dom/div nil
                   (flatten [(dom/div #js {:className "row"}
                                      (dom/div #js {:className "small-2 large-2 columns market-headers"}
                                               (dom/strong nil "MARKETS")
                                                (dom/div #js {:className "badge"}
                                                  (dom/span #js {:className "round secondary label"}
                                                    (str m-cnt-selected "/" m-cnt))))
                                      (dom/div #js {:className "small-2 large-2 columns"}
                                               (dom/strong nil "EST. PREMIUM"))
                                      (dom/div #js {:className "small-2 large-2 columns"}
                                               (dom/strong nil "STATUS"))
                                      (dom/div #js {:className "small-4 large-4 columns"}
                                               (dom/strong nil "ACTION"))
                                      (dom/div #js {:className "small-2 large-2 columns"}
                                               (dom/strong nil "QUOTE")))
                            (apply dom/div #js {:className "scrollable"}
                                      (om/build-all market-view markets {:opts {:selection-ch selection-ch}}))
                             ]))))))
