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

(defn navigate-to! [url]
  (set! js/window.location.href url))

(defn perform-auth [{:keys [login password]}]
  (go
    (let [{status :status} (<! (http/post "/login" {:form-params {:username login :password password}
                                                    :content-type :transit+json
                                                    :transit-opts {:handlers {}}}))]
      (when (= status 200)
        (navigate-to! "/")))))

(defn logout []
  (go
    (let [{status :status} (<! (http/post "/logout" {}))]
      (when (or (= status 200) (= status 302))
        (navigate-to! "/login")))))


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