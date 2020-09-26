(ns gops.core
  (:gen-class))

(def initial-state "Initial game state" {:firstPlayer {:deck #{1 2 3 4 5 6 7 8 9 10 11 12 13} :score 0}
                                         :secondPlayer {:deck #{1 2 3 4 5 6 7 8 9 10 11 12 13} :score 0}
                                         :bountyDeck #{1 2 3 4 5 6 7 8 9 10 11 12 13}})

(defn random-card-strategy [deck] (rand-nth (seq deck)))
(defn highest-card-strategy [deck] (apply max (seq deck)))
(def draw-card random-card-strategy)

(defn game-step [current-state] (let [bountyDeck (:bountyDeck current-state)
                                      first-player-deck (-> current-state :firstPlayer :deck)
                                      second-player-deck (-> current-state :secondPlayer :deck)]
                                  (if (seq bountyDeck)
                                    (let [drawn-card (draw-card bountyDeck)
                                          first-player-card (random-card-strategy first-player-deck)
                                          second-player-card (highest-card-strategy second-player-deck)
                                          match-winner (if (> first-player-card second-player-card) :firstPlayer :secondPlayer)]
                                      (game-step (-> current-state
                                                     (update-in [match-winner :score] inc)
                                                     (update-in [:bountyDeck] disj drawn-card)
                                                     (update-in [:firstPlayer :deck] disj first-player-card)
                                                     (update-in [:secondPlayer :deck] disj second-player-card))))
                                    current-state)))

(defn -main []
  (let [final-state (game-step initial-state)]
    (println final-state)
    (if (> (get-in final-state [:firstPlayer :score]) (get-in final-state [:secondPlayer :score])) (println "player 1 won") (println "player 2 won"))))
