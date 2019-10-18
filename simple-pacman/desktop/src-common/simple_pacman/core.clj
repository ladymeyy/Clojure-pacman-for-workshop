(ns simple-pacman.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))

(declare simple-pacman-game main-screen)

;TODO add additional movement directions: up & down.
; TODO add boundaries to the screen.  (The player won't be able to go outside the screen boundaries & disappear)


;|-----------------  dots  ------------------|

(defn- generate-dot [ x y dot-size]
  "generate collectable dot"
  (assoc (texture "dot.png") :x x :y y :width dot-size :height dot-size :dot? true))


(defn- gen-dots [ dot-size ]
  "This function generates entities of dots"
  (for [x (range 100 800 40) y (range 80 450 80)] (generate-dot x y dot-size)))


;|-------------------- handle input --------------------------|
(defn- get-direction []
  (cond
    (key-pressed? :dpad-left) :left
    (key-pressed? :dpad-right) :right))

;|--------------- handle player position -----------------|
(defn- get-new-position [direction entity ]
  (case direction
    :right (+ (:x entity) (:speed entity))
    :left (- (:x entity) (:speed entity))))

(defn- get-angle [direction]
  (case direction
    :right 0
    :left 180))

(defn- update-player-position [{:keys [player?] :as entity}]
  (if player?
    (let [direction (get-direction)
          x (get-new-position direction entity)
          angle (get-angle direction)]
      (assoc entity :x x :angle angle :direction direction))
    entity))


;----------------------- main screen ------------------------ |
(defscreen main-screen

           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage))


             (let [background (texture "background.png")
                   player-size 60
                   dot-size (/ player-size 3)
                   player (assoc (texture "pac.png")
                            :player? true :x 40 :y 40 :width player-size  :height player-size :angle 0  :direction :right :speed 15)
                   dots (gen-dots dot-size)]
               [background player dots]))

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