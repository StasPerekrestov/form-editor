(ns appulate.form-editor.ui.qa
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require
   [cljs.core.async :refer [put! chan <!]]
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]
   [appulate.form-editor.ui.editors :as editors]
   [clojure.string :as string]))

(defn- section-selector [section-data owner]
  "Renders a view that allows to select a particular section"
  (reify
    om/IRender
    (render [_]
            (let [{:keys [ch selected section]} section-data]
              (let [{:keys [name id]} section
                    questions (:questions section)
                    questions-count (count questions)
                    disabled (= 0 questions-count)
                    label (str name "(" questions-count ")")]
                (dom/dd #js {:className (if (true? selected) "active")}
                        (dom/a #js {:href "#" :onClick (fn[e]

                                                         (if (false? disabled)
                                                           (put! ch id))
                                                         (->>
                                                          e
                                                          (.preventDefault)))} label)))))))
(defn- question-search [search-data owner]
  (reify
    om/IRender
    (render [_]
            (let [searchId "qSearch"
                  {:keys [ch pattern]} search-data]
              (dom/input #js {:placeholder "Search:"
                              :id searchId
                              :type "text"
                              :className "right"
                              :value pattern
                              :onChange #(put! ch (->> %1 (.-target) (.-value)))})))))


(defn qs-editor [qs owner]
  "Renders questions an answers section"
  (reify
    om/IRender
    (render [_]
            (let [{:keys [id name type]} qs]
              (dom/div nil
                       (dom/label nil name)
                       (om/build editors/text-box "put text here"))))))

(defn qa-view [application owner]
  (reify
    om/IInitState
    (init-state [_]
                {:selection-ch (chan)
                 :search-ch (chan)
                 :search-pattern ""})
    om/IWillMount
    (will-mount [_]
                (let [selection-ch (om/get-state owner :selection-ch)
                      search-ch (om/get-state owner :search-ch)]
                  (go (loop [section-id (<! selection-ch)]
                        (om/update! application [:selected-section-id] section-id)
                        (recur (<! selection-ch))))
                  (go (loop [pattern (<! search-ch)]
                        (om/set-state! owner :search-pattern pattern)
                        (recur (<! search-ch))))))
    om/IRender
    (render [_]
            (apply dom/div nil (let [{:keys [sections selected-section-id]} application
                                     selection-ch (om/get-state owner :selection-ch)
                                     search-ch (om/get-state owner :search-ch)
                                     pattern (om/get-state owner :search-pattern)
                                     selected-section (first (filter #(= (:id %1) selected-section-id) sections))
                                     ]
                                 [(dom/div #js {:className "row"}
                                           (dom/div #js {:className "small-9 large-9 columns"}
                                                    (apply dom/dl #js {:className "sub-nav"}
                                                           (map (fn [s]
                                                                  (om/build section-selector {:selected (= s selected-section)
                                                                                              :section s
                                                                                              :ch selection-ch} ))
                                                                sections)))

                                           (dom/div #js {:className "small-3 large-3 columns"}
                                                    (om/build question-search {:ch search-ch :pattern pattern})))
                                  (if-not (nil? selected-section)
                                    (apply dom/div #js {:className "panel"}
                                           (om/build-all qs-editor
                                                         (filter #(or (> (.indexOf (:name %1) pattern) -1) (= 0 (count pattern))) (:questions selected-section)))))])))))
