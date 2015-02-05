(ns appulate.form-editor.data)

(defn fake-markets []
  (map #(hash-map :id %1 :name (str "Market-" (+ 1000 %1)))
       (range 1 100)))

(defn nil?? [x d]
  "Returns a value if it's not null or a default value in the other case"
  (if (nil? x) d x))

(defn sort-markets [markets]
    (->> markets
         (sort (fn [{selected1 :selected name1 :name}
                    {selected2 :selected name2 :name}]
                 (compare [(nil?? selected2 false) name1]
                          [(nil?? selected1 false) name2])))
         (vec)))

(defn init[]  {
               :notifications [{:message "Hello Info" :type :info}
                               {:message "Hello Warning" :type :warning}
                               {:message "Hello Alert" :type :alert}]

               :markets (vec (sort-markets (concat [{:id 1 :name "Zuric"}
                         {:id 2 :name "Atlas"}
                         {:id 3 :name "FirstComp"}
                         {:id 4 :name "Afbi"}
                         {:id 5 :name "Nif Group"}]
                                (fake-markets))))
               :application {
                             :selected-section-id 1234
                             :selectedForms []
                             ;Maybe map :id question is more appropriate
                             :sections [{:id 1234
                                         :name "Common"
                                         :questions [{:id 123 :name "Insured" :type :text}
                                                     {:id 345 :name "FEIN" :type :text}
                                                     {:id 342 :name "Additional FEIN?" :type :yesno}
                                                     {:id 343 :name "Additional Legal Entities?" :type :yesno}]}
                                        {:id 1235
                                         :name "Rating"}

                                        {:id 12346
                                         :name "Underwriting"
                                         :questions [{:id 523 :name "Is the insured engaged in any other type of business?" :type :yesno}
                                                     {:id 622 :name "Any work over water?" :type :yesno}
                                                     {:id 745 :name "Are athletic teams sponsored?" :type :yesno}]}

                                        {:id 123457
                                         :name "Coverage History"}

                                        {:id 123458
                                         :name "Policy"
                                         :questions [{:id 452 :name "Credit Bureau ID Number" :type :text}
                                                     {:id 453 :name "NCCI ID Number" :type :text}
                                                     {:id 454 :name "Special Endorsements?" :type :yesno}]
                                         }
                                        ]
                             }
               :policy {
                        :effective-date: "2014/12/23"
                        :policyType :WC
                        }
               :insured {:name "John Doe"}})
