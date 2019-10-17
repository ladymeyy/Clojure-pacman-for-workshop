(ns simple-pacman.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))

(declare simple-pacman-game main-screen)

;todo :
;      1. Read about pure functions in the added file
;      2. Edit the code so the functions would be PURE functions. e.g. It returns the same result if given the same arguments
;      3. Edit the code so the player would be able to move to the LEFT


(def window-height 506)
(def window-width 900)
(def pac-size 60)
(def speed 15)

;|-------------------- handle input --------------------------|
(defn- get-direction []
  (cond
    (key-pressed? :dpad-right) :right))

;|--------------- handle player position -----------------|
(defn- get-new-position [direction entity ]
  (case direction
    :right (+ (:x entity) speed)))


(defn- update-player-position [{:keys [player?] :as entity}]
  (if player?
    (let [direction (get-direction)
          x (get-new-position direction entity)]
      (assoc entity :x x  :direction direction))
    entity))


;----------------------- main screen ------------------------ |
(defscreen main-screen

           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage))


             (let [background (texture "background.png")
                   player (assoc (texture "pac.png")
                            :player? true :x 40 :y 40 :width pac-size  :height pac-size :angle 0  :direction :right)]
               [background player ]))

           :on-render
           (fn [screen entities]
             (clear!)
             (render! screen entities))

           :on-key-down
           (fn [screen entities]
             (cond
               (key-pressed? :r) (app! :post-runnable #(set-screen! simple-pacman-game main-screen))
               (get-direction) (map update-player-position entities))))

(defgame simple-pacman-game
         :on-create
         (fn [this]
           (set-screen! this main-screen)))