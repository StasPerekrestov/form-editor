(ns appulate.form-editor.data)

(defn fake-markets []
  (map #(hash-map :id %1 :name (str "Market-" (+ 1000 %1)))
       (range 1 100)))

(defn init[]  {
               :notifications [{:message "Hello Info" :type :info}
                               {:message "Hello Warning" :type :warning}
                               {:message "Hello Alert" :type :alert}]

               :markets (concat [{:id 1 :name "Zuric"}
                         {:id 2 :name "Atlas"}
                         {:id 3 :name "FirstComp"}
                         {:id 4 :name "Afbi"}
                         {:id 5 :name "Nif Group"}]
                                (fake-markets))
               :application {
                             :selected-section-id 1234
                             :selectedForms []
                             ;Maybe map :id question is more appropriate
                             :sections [{:id 1234
                                         :name "Common"
                                         :questions [{:id 123 :name "Insured" :type :text}
                                                     {:id 345 :name "FEIN" :type :text}
                                                     {:id 342 :name "test" :type :text}]}
                                        {:id 1235
                                         :name "Rating"}

                                        {:id 12346
                                         :name "Underwriting"
                                         :questions [{:id 123 :name "Is the insured engaged in any other type of business?" :type :yesno}
                                                     {:id 122 :name "Any work over water?" :type :yesno}
                                                     {:id 345 :name "Are athletic teams sponsored?" :type :yesno}]}

                                        {:id 123457
                                         :name "Coverage History"}
                                        {:id 123458
                                         :name "Policy"}]
                             }
               :policy {
                        :effective-date: "2014/12/23"
                        :policyType :WC
                        }
               :insured {:name "John Doe"}})
