(ns appulate.security.login
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [<!]]
    [om.core :as om :include-macros true]
    [appulate.security.ui :refer [login-panel]]
    [appulate.security.api :as api]
    [figwheel.client :as fw]))

;; Lets you do (prn "stuff") to the console
(enable-console-print!)

(defonce app-state
         (atom nil))

(defn navigate-to! [url]
  (set! js/window.location.href url))

(defn authenticate [credentials]
  (go
    (<! (api/login credentials)))
    (navigate-to! "/marketing"))


(defn login-section [data owner]
  (reify
    om/IRender
    (render [_]
      (om/build login-panel {:onSignIn
                             (fn [credentials]
                               (authenticate credentials))}))))

(defn main []
  (om/root login-section
           app-state
           {:target (.getElementById js/document "app")}))

(defonce initial-call-to-main (main))

(comment fw/start {
           :load-warninged-code true
           :on-jsload (fn []
                        (main)
                        ;; (stop-and-start-my app)
                        )})