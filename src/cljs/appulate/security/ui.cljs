(ns appulate.security.ui
  (:require
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]))

(defn- sing-in [data login password]
  (let [{sign-in :onSignIn} data]
    (when (fn? sign-in)
      (sign-in {:login login :password password}))))

(defn login-panel [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:login nil :password nil})
    om/IRenderState
    (render-state [_ {:keys [login password]}]
      (dom/fieldset nil
                    (dom/legend nil "Login")
                    (dom/div #js {:className "row"})
                    (dom/div #js {:className "large-12 columns"}
                             (dom/label nil "Login"
                                        (dom/input #js {:type "text"
                                                        :placeholder "Login"
                                                        :value login
                                                        :onChange #(om/set-state! owner :login (->> %1
                                                                                                      (.-target)
                                                                                                      (.-value)))}))

                             (dom/label nil "Password"
                                        (dom/input #js {:type "password"
                                                        :placeholder
                                                        "Password"
                                                        :value password
                                                        :onKeyDown (fn [e]
                                                                      (when (= 13 (.-keyCode e))
                                                                        (sing-in data login password)))
                                                        :onChange #(om/set-state! owner :password (->> %1
                                                                                                      (.-target)
                                                                                                      (.-value)))}))
                             (dom/input #js {:type "button"
                                             :value "Sign in"
                                             :className "button tiny"
                                             :onClick (fn [_] (sing-in data login password))}))))))
