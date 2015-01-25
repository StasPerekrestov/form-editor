(ns appulate.form-editor.ui.editors
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defmulti ua-control (fn [question] (:type question)))

(defmethod ua-control :text [{:keys [value]}]
  (reify
    om/IRender
    (render [_]
      (dom/input #js {:type "text" :value value :placeholder "Type value here"}))))

(defn label-for-radio [title]
  (dom/span #js {:style #js {:margin "10px"}} title))

(defmethod ua-control :yesno [{:keys [id value]}]
  (reify
    om/IRender
    (render [_]
      (let [radio-name (str "radio" id)]
        (dom/div nil
          (dom/label #js {:className "left"}
            (dom/input #js {:type "radio" :name radio-name}) (label-for-radio "Yes"))
          (dom/label nil
            (dom/input #js {:type "radio" :name radio-name}) (label-for-radio "No")))))))

(defmethod ua-control :default [{type :type}]
  (throw
    (str "There is no appropriate ui-control for quesiton type" type)))