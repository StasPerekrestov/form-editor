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

(defmethod ua-control :yesno [{:keys [value]}]
  (reify
    om/IRender
    (render [_]
      (dom/input #js {:type "checkbox" :checked value}))))

(defmethod ua-control :default [{type :type}]
  (throw
    (str "There is no appropriate ui-control for quesiton type" type)))