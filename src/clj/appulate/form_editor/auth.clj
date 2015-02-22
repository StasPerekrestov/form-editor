(ns appulate.form-editor.auth
  (:require
    [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
    [buddy.auth.backends.session :refer [session-backend]]
    [compojure.response :refer [render]]
    [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
    [ring.util.response :refer [redirect header]]
    [buddy.auth :refer [authenticated? throw-unauthorized]]
    [clojure.java.io :as io]))

(def authdata {:admin "123123"
               :foo "dragon"})
(defn home
  [req]
  (if-not (authenticated? req)
    (->
      (render (slurp (io/resource "public/login.html")) req)
      ;(header "x-forgery-token" *anti-forgery-token*)
      )
    (render (slurp (io/resource "public/index.html")) req)))

(defn login
  [request]
  (render
    (slurp
      (io/resource "public/login.html")) request))

(defn login-authenticate
  "Authenticate Handler
Respons to post requests in same url as login and is responsible to
identify the incoming credentials and set the appropiate authenticated
user into session. `authdata` will be used as source of valid users."
  [request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])
        session (:session request)]
    (if-let [found-password (get authdata (keyword username))]
      (if (= found-password password)
        (let [nexturl (get-in request [:query-params :next] "/")
              session (assoc session :identity (keyword username))]
          (-> (redirect nexturl)
              (assoc :session session)))
        (render (slurp (io/resource "public/index.html")) request))
      (render (slurp (io/resource "public/login.html")) request))))


(defn logout
  "Logout handler
It is responsible of cleaing session."
  [request]
  (-> (redirect "/login")
      (assoc :session {})))


(defn unauthorized-handler
  "Self defined unauthorized handler
This function is responsible of handling unauthorized requests.
(When unauthorized exception is raised by some handler)"
  [request metadata]
  (cond
    ;; If request is authenticated, raise 403 instead
    ;; of 401 (because user is authenticated but permission
    ;; denied is raised).
    (authenticated? request)
    (-> (render (slurp (io/resource "error.html")) request)
        (assoc :status 403))

    ;; In other cases, redirect it user to login.
    :else
    (let [current-url (:uri request)]
      (redirect (format "/login?next=%s" current-url)))))


(def auth-backend
  (session-backend {:unauthorized-handler unauthorized-handler}))


(defn wrap-auth [routes]
  (-> routes
      (wrap-authorization auth-backend)
      (wrap-authentication auth-backend)))