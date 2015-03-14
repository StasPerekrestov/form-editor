(ns appulate.security.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [<! put! chan]]
    [cljs-http.client :as http]))

(defn login [{:keys [login password]}]
  (let [c (chan)]
    (go
      (let [{status :status} (<! (http/post "/login" {:form-params {:username login :password password}
                                                      :content-type :transit+json
                                                      :transit-opts {:handlers {}}}))]
        (when (= status 200)
          (put! c 1))))
    c))

(defn logout []
  (let [c (chan)]
    (go
      (let [{status :status} (<! (http/post "/logout" {}))]
        (when (or (= status 200) (= status 302))
          (put! c 1))))
    c))
