(ns appulate.form-editor.ui.markets
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn market-view [market owner]
  (reify
    om/IRender
    (render [_]
      (let [{:keys [id name]} market
            chk-id (str "selectedMarket" id)]
         (dom/div nil
              (dom/input #js {:type "checkbox" :id chk-id}
                        (dom/label #js {:htmlFor chk-id} name)))))))

(defn selected-view [markets owner]
  (reify
    om/IRender
    (render [_]
       (apply dom/div nil
              (om/build-all market-view markets)))))
