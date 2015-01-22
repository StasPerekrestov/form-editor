(ns appulate.form-editor.ui.editors
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))


(defn text-box [value]
  (reify
    om/IRender
    (render [_]
          (dom/input #js {:type "text"} value))))
