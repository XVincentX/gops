(ns gops.core
  (:gen-class))

(def initialState {:firstPlayer {:deck #{1 2 3 4 5 6 7 8 9 10 11} :score 0}
                   :secondPlayer {:deck #{1 2 3 4 5 6 7 8 9 10 11} :score 0}
                   :bountyDeck #{1 2 3 4 5 6 7 8 9 10 11}})

(defn random-card-strategy [deck] (rand-nth (seq deck)))
(defn highest-card-strategy [deck] (apply max (seq deck)))

(defn gameStep [currentState] (let [bountyDeck (:bountyDeck currentState)
                                    first-player-deck (get-in currentState [:firstPlayer :deck])
                                    second-player-deck (get-in currentState [:secondPlayer :deck])
                                    first-player-score (get-in currentState [:firstPlayer :score])
                                    second-player-score (get-in currentState [:secondPlayer :score])]
                                (if (= 0 (count bountyDeck))
                                  currentState
                                  (let [drawn-card (rand-nth (seq bountyDeck))
                                        first-player-card (random-card-strategy first-player-deck)
                                        second-player-card (highest-card-strategy second-player-deck)
                                        new-state {:bountyDeck (disj bountyDeck drawn-card)
                                                   :firstPlayer {:deck (disj first-player-deck first-player-card) :score first-player-score}
                                                   :secondPlayer {:deck (disj second-player-deck second-player-card) :score second-player-score}}
                                        matchWinner (if (> first-player-card second-player-card) :firstPlayer :secondPlayer)]
                                    (gameStep (update-in new-state [matchWinner :score] inc))))))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (let [state (gameStep initialState)]
    (println state)
    (if (> (get-in state [:firstPlayer :score]) (get-in state [:secondPlayer :score])) (println "player 1 won") (println "player 2 won"))))
