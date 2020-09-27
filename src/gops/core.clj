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
  (let [{:keys [bounty-deck first-player second-player]} current-state
        drawn-card (draw-card bounty-deck)
        first-player-card (random-card-strategy (:deck first-player))
        second-player-card (highest-card-strategy (:deck second-player))
        match-winner (if (> first-player-card second-player-card) :first-player :second-player)]
    (-> current-state
        (update-in [match-winner :score] inc)
        (update-in [:bounty-deck] disj drawn-card)
        (update-in [:first-player :deck] disj first-player-card)
        (update-in [:second-player :deck] disj second-player-card))))

(defn -main []
  (loop [next-state (game-step initial-state)]
    (if-not (seq (:bounty-deck next-state))
      (if (> (-> next-state :first-player :score) (-> next-state :second-player :score))
        (println "Player 1 won")
        (println "Player 2 won"))
      (recur (game-step next-state)))))
