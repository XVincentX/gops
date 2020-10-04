(ns gops.cc)

(def operations [{:type :add :name "Tom" :cc 4111111111111111 :value 1000}
                 {:type :add :name "Lisa" :cc 5454545454545454 :value 3000}
                 {:type :charge :name "Tom" :value 500}
                 {:type :charge :name "Tom" :value 800}
                 {:type :charge :name "Lisa" :value 7}
                 {:type :credit :name "Lisa" :value 100}])

(defmulti process-transaction :type)

(defmethod process-transaction :add [transaction ledger]
  (-> ledger
      (assoc-in [(-> transaction :name keyword) :current] (:value transaction))
      (assoc-in [(-> transaction :name keyword) :limit] (:value transaction))))

(defmethod process-transaction :charge [transaction ledger]
  (if
   (> (get-in ledger [(-> transaction :name keyword) :current]) (:value transaction))
    (update-in ledger [(-> transaction :name keyword) :current] - (:value transaction))
    ledger))

(defmethod process-transaction :credit [transaction ledger]
  (update-in ledger [(-> transaction :name keyword) :current] + (:value transaction)))

(defn swap-ledger-values [ledger-row]
  (if (< (:limit ledger-row) (:current ledger-row))
    (update ledger-row :current #(- (:limit ledger-row) %))
    ledger-row))
