(ns appulate.form-editor.core
    (:require
              [compojure.route :as route]
              [compojure.core :refer [GET POST defroutes routes]]
              [cheshire.core :as json]
              [appulate.form-editor.auth :as auth]
              [appulate.form-editor.event-bus.ws-handler :as ws]
              [appulate.form-editor.views.layout :as layout]
              [appulate.form-editor.Views.marketing :as marketing]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes auth-routes
           (GET "/login"  [] auth/login)
           (POST "/login"  [] auth/login-authenticate-ajax)
           (POST "/logout" []  auth/logout))

(defroutes chat-routes
           (GET  "/ws" req (ws/ring-ajax-get-or-ws-handshake req))
           (POST "/ws" req (ws/ring-ajax-post              req)))


(defroutes other-routes
  (GET "/marketing" [] (layout/application "Marketing" (marketing/app)))
  (GET "/" [] auth/root)
  (GET "/test" [] (json-response
                   {:message "You made it!"}))
  (POST "/test" req (json-response
                     {:message "Doing something something important..."}))
  (route/resources "/")
  (route/not-found "404 - Page not found."))

(def all-routes
  (routes auth-routes chat-routes other-routes))
