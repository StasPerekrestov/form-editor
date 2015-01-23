(ns appulate.form-editor.data)

(defn init[]  {
               :notifications [{:message "Hello Info" :type :info}
                               {:message "Hello Warning" :type :warning}
                               {:message "Hello Alert" :type :alert}]

               :markets [{:id 1 :name "Zuric"}
                         {:id 2 :name "Atlas"}
                         {:id 3 :name "FirstComp"}
                         {:id 4 :name "Afbi"}
                         {:id 5 :name "Nif Group"}]
               :application {
                             :selected-section-id nil
                             :selectedForms []
                             ;Maybe map :id question is more appropriate
                             :sections [{:id 1234
                                         :name "Common"
                                         :questions [{:id 123 :name "Insured" :type "text"}
                                                    {:id 345 :name "FEIN" :type "text"}]}
                                        {:id 1235
                                         :name "Rating"                                         }
                                        {:id 12346
                                         :name "Underwriting"}
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
