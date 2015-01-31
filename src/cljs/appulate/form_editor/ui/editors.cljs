(ns appulate.form-editor.ui.editors
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defmulti ua-control (fn [question] (:type question)))

(defmethod ua-control :text [question]
  (reify
    om/IRender
    (render [_]
      (let [{:keys [value]} question]
        (dom/input #js {:type "text"
                        :value value
                        :placeholder "Type value here"
                        :onChange #(om/update! question [:value] (->>
                                                                   %1
                                                                   (.-target)
                                                                   (.-value)))})))))

(defn label-for-radio [title]
  (dom/span #js {:style #js {:margin "10px"}} title))

(defmethod ua-control :yesno [question]
  (reify
    om/IRender
    (render [_]
      (let [{:keys [id value]} question
            radio-name (str "radio" id)]
        (dom/div nil
          (dom/label #js {:className "left"}
            (dom/input #js {:type "radio"
                            :name radio-name
                            :checked (true? value)
                            :onChange #(om/update! question [:value] true)}) (label-for-radio "Yes"))
          (dom/label nil
            (dom/input #js {:type "radio"
                            :name radio-name
                            :checked (false? value)
                            :onChange #(om/update! question [:value] false)}) (label-for-radio "No")))))))

(defmethod ua-control :default [{type :type}]
  (throw
    (str "There is no appropriate ui-control for quesiton type" type)))