(ns gops.core
  (:gen-class))

(def initialState {:firstPlayer {:deck #{1 2 3 4 5 6 7 8 9 10 11} :score 0}
                   :secondPlayer {:deck #{1 2 3 4 5 6 7 8 9 10 11} :score 0}
                   :bountyDeck #{1 2 3 4 5 6 7 8 9 10 11}})

(defn random-card-strategy [deck] (rand-nth (seq deck)))

(defn gameStep [currentState] (let [bountyDeck (:bountyDeck currentState)
                                    firstPlayerDeck (get-in currentState [:firstPlayer :deck])
                                    secondPlayerDeck (get-in currentState [:secondPlayer :deck])
                                    firstPlayerScore (get-in currentState [:firstPlayer :score])
                                    secondPlayerScore (get-in currentState [:secondPlayer :score])]
                                (if (= 0 (count bountyDeck))
                                  currentState
                                  (let [drawnCard (rand-nth (seq bountyDeck))
                                        firstPlayerCard (random-card-strategy firstPlayerDeck)
                                        secondPlayerCard (random-card-strategy secondPlayerDeck)
                                        newState {:bountyDeck (disj bountyDeck drawnCard)
                                                  :firstPlayer {:deck (disj firstPlayerDeck firstPlayerCard) :score firstPlayerScore}
                                                  :secondPlayer {:deck (disj secondPlayerDeck secondPlayerCard) :score secondPlayerScore}}]
                                    (if (> firstPlayerCard secondPlayerCard)
                                      (gameStep (assoc-in newState [:firstPlayer :score] (inc firstPlayerScore)))
                                      (gameStep (assoc-in newState [:secondPlayer :score] (inc secondPlayerScore))))))))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (let [state (gameStep initialState)]
    (println state)))
