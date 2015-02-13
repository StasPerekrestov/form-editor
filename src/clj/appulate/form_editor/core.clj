(ns appulate.form-editor.core
    (:require
              [compojure.route :as route]
              [compojure.core :refer [GET POST defroutes routes]]
              [cheshire.core :as json]
              [appulate.form-editor.auth :as auth]
              [appulate.form-editor.event-bus.ws-handler :as ws]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes all-routes
  (GET "/" [] auth/home)

  (GET "/login" [req] (auth/login req))
  (POST "/login" [] auth/login-authenticate)
  (GET "/logout" [] auth/logout)

  (GET "/test" [] (json-response
                   {:message "You made it!"}))
  (POST "/test" req (json-response
                     {:message "Doing something something important..."}))
  (GET  "/ws" req (ws/ring-ajax-get-or-ws-handshake req))
  (POST "/ws" req (ws/ring-ajax-post              req))
  (route/resources "/")
  (route/not-found "404 - Page not found."))

;(def all-routes
;  (routes app-routes ws/ws-routes))
