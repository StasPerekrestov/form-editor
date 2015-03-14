(ns appulate.form-editor.serve
  (:use [org.httpkit.server :only [run-server]])
  (:require [appulate.form-editor.core :as core]
            [compojure.core :refer [routes]]
            [ring.middleware.reload :as reload]
            [appulate.form-editor.auth :as auth]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

(defn in-dev? [& _] true) ;; TODO read a config variable from command line, env, or file?

(def app-routes
  (let [ring-defaults-config
        (assoc-in site-defaults [:security :anti-forgery]
          {:read-token (fn [req] (-> req :params :csrf-token))})]

    ;; NB: Sente requires the Ring `wrap-params` + `wrap-keyword-params`
    ;; middleware to work. These are included with
    ;; `ring.middleware.defaults/wrap-defaults` - but you'll need to ensure
    ;; that they're included yourself if you're not using `wrap-defaults`.
    ;;
    ;;todo: add anti-forgery check
    (-> core/main-routes
          (auth/wrap-auth)
          (wrap-keyword-params)
          (wrap-params ring-defaults-config)
          (wrap-session)
         )))

(def all-routes
  (routes app-routes core/other-routes))

(defonce server (atom nil))

(defn start [args]
  (let [handler (if (in-dev? args)
                  (reload/wrap-reload
                    (routes #'all-routes)) ;; only reload when dev
                  (routes all-routes))
        port 8080]
    (println (str "Starting server on port: " port "..."))
    (reset! server (run-server handler {:port port}))))

(defn stop []
  (let [srv @server]
    (when-not (nil? srv)
      (srv :timeout 100)
      (reset! server nil))))

(defn -main [& args] ;; entry point, lein run will pick up and start from here
  (start args))
