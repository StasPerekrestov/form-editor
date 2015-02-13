(ns appulate.form-editor.serve
  (:use [org.httpkit.server :only [run-server]])
  (:require [appulate.form-editor.core :as core]
            [compojure.core :refer [routes]]
            [ring.middleware.reload :as reload]
            [appulate.form-editor.auth :as auth]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.params :refer [wrap-params]]))

(defn in-dev? [& _] true) ;; TODO read a config variable from command line, env, or file?

(def my-ring-handler
  (let [ring-defaults-config
        (assoc-in site-defaults [:security :anti-forgery]
          {:read-token (fn [req] (-> req :params :csrf-token))})]

    ;; NB: Sente requires the Ring `wrap-params` + `wrap-keyword-params`
    ;; middleware to work. These are included with
    ;; `ring.middleware.defaults/wrap-defaults` - but you'll need to ensure
    ;; that they're included yourself if you're not using `wrap-defaults`.
    ;;
    ;(wrap-defaults
    ;  (->> core/all-routes
    ;      (auth/wrap-auth))
    ;      ring-defaults-config)
    ;instead of wrap-params use wrap default and add csrf-token
    (->> core/all-routes
         (wrap-params)
         (auth/wrap-auth))
    ))

(defonce server (atom nil))

(defn start [args]
  (let [handler (if (in-dev? args)
                  (reload/wrap-reload
                    (routes #'my-ring-handler)) ;; only reload when dev
                  (routes my-ring-handler))
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
