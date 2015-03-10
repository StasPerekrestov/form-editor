(ns appulate.form-editor.views.layout
  (:require
    [hiccup.element :refer :all]
    [hiccup.page :refer [html5 include-css include-js]]))

(defn application [title & content]
  (html5 {:lang "en"}
         [:head
          [:title title]
          (include-css "/css/main.css")
          (include-css "js/lib/foundation/css/normalize.css")
          (include-css "js/lib/foundation/css/foundation.css")
          (include-js "http://fb.me/react-0.12.2.js")

          [:body
           [:div {:class "container"} content]
           (include-js "js/out/goog/base.js")
           (include-js "js/fe.js")
           [:script  "goog.require('appulate.form_editor.core');"]
           (include-js "js/lib/jquery/dist/jquery.min.js")
           (include-js "js/lib/foundation/js/foundation.min.js")
           (include-js "js/lib/foundation/js/foundation.min.js")
           [:script  "$(document).foundation();$(document).foundation('tab', 'reflow');"]
           ]]))
