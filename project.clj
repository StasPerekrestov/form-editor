(defproject forms "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0-beta3"]
                 [org.clojure/tools.reader "0.9.2"]
                 ;; CLJ
                 [ring/ring-core "1.3.2"]
                 [compojure "1.3.4"]
                 [hiccup "1.0.5"]
                 [cheshire "5.5.0"]
                 [ring/ring-devel "1.3.2"]
                 [ring/ring-defaults "0.1.5"]
                 [javax.servlet/servlet-api "2.5"]
                 [buddy/buddy-auth "0.6.0"]
                 ;; CLJS
                 [org.clojure/clojurescript "0.0-3308"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-http "0.1.35"]
                 [secretary "1.2.3"]
                 [org.omcljs/om "0.9.0"]
                 [figwheel "0.3.7"]
                 [http-kit "2.1.19"]
                 [com.taoensso/sente "1.5.0"]]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-ring "0.9.6"]
            [lein-pdo "0.1.1"]
            [lein-figwheel "0.3.7"]
            [lein-ancient "0.6.7"]
            [lein-bower "0.5.1"]]

  :aliases {"dev" ["pdo" "cljsbuild" "auto" "dev," "ring" "server-headless"]}

  :ring {:handler appulate.form-editor.core/app
         :init    appulate.form-editor.core/init}

  :main appulate.form-editor.serve

  :source-paths ["src/clj"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        ; put client config options in :figwheel
                        :figwheel { :websocket-host "localhost"
                                   :on-jsload "appulate.form-editor.core/main"}

                        :compiler {
                                   :output-to "resources/public/js/fe.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true
                                   :source-map-timestamp true
                                   :cache-analysis true
                                   :externs ["react/externs/react.js"]}}
                       {:id "login"
                        :figwheel { :websocket-host "localhost"
                                   :on-jsload "appulate.security.login/main"}

                        :source-paths ["src/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/login.js"
                                   :output-dir "resources/public/js/outlogin"
                                   :optimizations :none
                                   :source-map true
                                   :source-map-timestamp true
                                   :cache-analysis true
                                   :externs ["react/externs/react.js"]
                                   }}
                       {:id "release"
                        :source-paths ["src/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/fe.js"
                                   :output-dir "resources/public/js/outprod"
                                   :source-map "resources/public/js/fe.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :output-wrapper false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]
                                   :closure-warnings
                                   {:non-standard-jsdoc :off}}}]}

  :figwheel {:http-server-root "public" ;; default and assumes "resources"
             :server-port 3449 ;; default
             :css-dirs ["resources/public/css"] ;; watch and update CSS
             :repl true
             :ring-handler appulate.form-editor.serve/all-routes
             }

  :bower-dependencies [[foundation "5.5.2"]]
  :bower {:directory "resources/public/js/lib"})
