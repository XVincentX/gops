(ns gops.datomic (:require [datomic.client.api :as d]))

(def client (d/client {:server-type :dev-local
                       :system "dev"
                       :storage-dir "/Users/vncz/dev/gops/src/gops/data/"}))

(d/create-database client {:db-name "hello"})

(def conn (d/connect client {:db-name "hello"}))

(def movie-schema [{:db/ident :movie/title
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The title of the movie"}

                   {:db/ident :movie/genre
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The genre of the movie"}

                   {:db/ident :movie/release-year
                    :db/valueType :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc "The year the movie was released in theaters"}])

(d/transact conn {:tx-data movie-schema})

(def first-movies [{:movie/title "The Goonies"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Commando"
                    :movie/genre "thriller/action"
                    :movie/release-year 1985}
                   {:movie/title "Repo Man"
                    :movie/genre "punk dystopia"
                    :movie/release-year 1984}])

(d/transact conn {:tx-data first-movies})

(def db (d/db conn))

(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

(d/q all-titles-q db)

(d/transact conn {:tx-data [{:movie/title "Cacca"
                             :movie/genre "Piedi"
                             :movie/release-year 2001}]})

(d/q '[:find ?sku
       :where [?e :inv/sku "SKU-42"]
       [?e :inv/color ?color]
       [?e2 :inv/color ?color]
       [?e2 :inv/sku ?sku]]
     db)
