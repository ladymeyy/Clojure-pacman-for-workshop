(ns simple-pacman.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))

(declare simple-pacman-game main-screen)

(def window-height 506)
(def window-width 900)
(def pac-size 60)
(def speed 15)


;|-------------------- handle input --------------------------|

(defn- get-direction []
  (cond
    (key-pressed? :dpad-up) :up
    (key-pressed? :dpad-down) :down
    (key-pressed? :dpad-left) :left
    (key-pressed? :dpad-right) :right))

;|--------------- handle player position -----------------|

(defn- get-angle [direction]
  (case direction
    :right 0
    :up 90
    :left 180
    :down 270))

(defn- get-new-x [direction entity ]
  (let [new-x (case direction
                :right (+ (:x entity) speed)
                :left (- (:x entity) speed)
                (:x entity))]
    (if (or (< new-x 0) (<= (- window-width pac-size) new-x)) ;apply screen x boundaries
      (:x entity)
      new-x)))

(defn- get-new-y [direction entity ]
  (let [new-y (case direction
                :up (+ (:y entity) speed)
                :down (- (:y entity) speed)
                (:y entity))]
    (if (or (< new-y 0) (<= (- window-height pac-size) new-y)) ;apply screen y boundaries
      (:y entity)
      new-y)))


(defn- update-player-position [{:keys [player?] :as entity}]
  (if player?
    (let [direction (get-direction)
          x (get-new-x direction entity)
          y (get-new-y direction entity)
          angle (get-angle direction)]
      (assoc entity :x x :y y :angle angle :direction direction))
    entity))


;----------------------- main screen ------------------------ |
(defscreen main-screen

           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage))


             (let [background (texture "background.png")
                   player (assoc (texture "pac.png")
                            :player? true :x 40 :y 40 :width pac-size :height pac-size :angle 0  :direction :right)]
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