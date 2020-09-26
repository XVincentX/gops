(ns gops.core
  (:gen-class))

(def initial-state "Initial game state"
  {:first-player {:deck #{1 2 3 4 5 6 7 8 9 10 11 12 13} :score 0}
   :second-player {:deck #{1 2 3 4 5 6 7 8 9 10 11 12 13} :score 0}
   :bounty-deck #{1 2 3 4 5 6 7 8 9 10 11 12 13}})

(defn random-card-strategy [deck] (rand-nth (seq deck)))
(defn highest-card-strategy [deck] (apply max (seq deck)))
(def draw-card random-card-strategy)

(defn game-step [current-state]
  (let [bounty-deck (:bounty-deck current-state)
        first-player-deck (-> current-state :first-player :deck)
        second-player-deck (-> current-state :second-player :deck)]
    (if (seq bounty-deck)
      (let [drawn-card (draw-card bounty-deck)
            first-player-card (random-card-strategy first-player-deck)
            second-player-card (highest-card-strategy second-player-deck)
            match-winner (if (> first-player-card second-player-card) :first-player :second-player)]
        (game-step (-> current-state
                       (update-in [match-winner :score] inc)
                       (update-in [:bounty-deck] disj drawn-card)
                       (update-in [:first-player :deck] disj first-player-card)
                       (update-in [:second-player :deck] disj second-player-card))))
      current-state)))

(defn -main []
  (let [final-state (game-step initial-state)]
    (println final-state)
    (if (> (get-in final-state [:first-player :score]) (get-in final-state [:second-player :score]))
      (println "player 1 won")
      (println "player 2 won"))))
