(ns appulate.security.login
  (:require
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]
    [appulate.security.auth :refer [login-panel]]
    [figwheel.client :as fw]))

;; Lets you do (prn "stuff") to the console
(enable-console-print!)

(defonce app-state
         (atom nil))

(defn login-section [data owner]
  (reify
    om/IRender
    (render [_]
      (om/build login-panel {:onSignIn
                             (fn [credentials]
                               (println credentials))}))))

(defn main []
  (om/root login-section
           app-state
           {:target (.getElementById js/document "app")}))

(defonce initial-call-to-main (main))

(fw/start {
           :load-warninged-code true
           :on-jsload (fn []
                        (main)
                        ;; (stop-and-start-my app)
                        )})
