(ns appulate.form-editor.ui.notes
  (:require  [om.core :as om :include-macros true]
             [om.dom :as dom :include-macros true]))

(defn notepad
  "TextArea to edit notes"
  [_ owner]
  (reify
    om/IRender
    (render [_]
      (dom/textarea #js {:value "This is a text"}))))