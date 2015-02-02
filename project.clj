(defproject forms "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.reader "0.8.13"]
                 ;; CLJ
                 [ring/ring-core "1.3.2"]
                 [compojure "1.3.1"]
                 [cheshire "5.4.0"]
                 [ring/ring-devel "1.3.2"]
                 [ring/ring-defaults "0.1.3"]
                 [javax.servlet/servlet-api "2.5"]
                 ;; CLJS
                 [org.clojure/clojurescript "0.0-2755"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-http "0.1.25"]
                 [secretary "1.2.1"]
                 [org.omcljs/om "0.8.8"]
                 [figwheel "0.2.2-SNAPSHOT"]
                 [http-kit "2.1.19"]
                 [com.taoensso/sente "1.3.0"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-ring "0.9.1"]
            [lein-pdo "0.1.1"]
            [lein-figwheel "0.2.2-SNAPSHOT"]
            [lein-ancient "0.6.1"]
            [lein-bower "0.5.1"]]

  :aliases {"dev" ["pdo" "cljsbuild" "auto" "dev," "ring" "server-headless"]}

  :ring {:handler appulate.form-editor.core/app
         :init    appulate.form-editor.core/init}

  :main appulate.form-editor.serve

  :source-paths ["src/clj"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/fe.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true
                                   :source-map-timestamp true
                                   :cache-analysis true
                                   :externs ["react/externs/react.js"]}}
                       {:id "release"
                        :source-paths ["src/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/fe.js"
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
             :repl false
             :ring-handler appulate.form-editor.serve/dev-handler
             }

  :bower-dependencies [[foundation "5.5.0"]]
  :bower {:directory "resources/public/js/lib"})
