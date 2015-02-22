(ns appulate.security.login
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [<!]]
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]
    [appulate.security.auth :refer [login-panel]]
    [cljs-http.client :as http]
    [figwheel.client :as fw]))

;; Lets you do (prn "stuff") to the console
(enable-console-print!)

(defonce app-state
         (atom nil))

(defn perform-auth [{:keys [login password]}]
  (go
    (let [resp (<! (http/post "/login" {:form-params {:username login :password password}}))])

    )
  )

(defn login-section [data owner]
  (reify
    om/IRender
    (render [_]
      (om/build login-panel {:onSignIn
                             (fn [credentials]
                               (perform-auth credentials))}))))

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
