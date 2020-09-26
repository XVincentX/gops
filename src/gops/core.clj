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
                                                  :secondPlayer {:deck (disj secondPlayerDeck secondPlayerCard) :score secondPlayerScore}}
                                        matchWinner (if (> firstPlayerCard secondPlayerCard) :firstPlayer :secondPlayer)]
                                    (gameStep (update-in newState [matchWinner :score] inc))))))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (let [state (gameStep initialState)]
    (println state)
    (if (> (get-in state [:firstPlayer :score]) (get-in state [:secondPlayer :score])) (println "player 1 won") (println "player 2 won"))))
