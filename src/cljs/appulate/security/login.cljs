(ns appulate.security.login
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [<!]]
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]
    [appulate.security.ui :refer [login-panel]]
    [appulate.security.api :as api]
    [appulate.form-editor.utils :refer [navigate-to!]]))

;; Lets you do (prn "stuff") to the console
(enable-console-print!)

(defonce app-state
         (atom nil))

(defn authenticate [credentials]
  (go
    (<! (api/login credentials))
    (navigate-to! "/marketing")))


(defn login-section [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:className "row"}
               (dom/div #js {:className "small-6 small-centered large-8 columns"}
                        (om/build login-panel {:onSignIn
                                               (fn [credentials]
                                                 (authenticate credentials))}))))))

(defn main []
  (om/root login-section
           app-state
           {:target (.getElementById js/document "app")}))

(defonce initial-call-to-main (main))