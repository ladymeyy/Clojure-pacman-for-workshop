(ns simple-pacman.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))

(declare simple-pacman-game main-screen)

;TODO: let's talk about the higher order functions hiding in this code ..
;TODO let's talk about macros
;
;Bonus task: add 2 more directions: up & down.
; Add more boundaries , not only screen boundaries 

;|-----------------  dots  ------------------|

(defn- generate-dot [ x y dot-size]
  "generate collectable dot"
  (assoc (texture "dot.png") :x x :y y :width dot-size :height dot-size :dot? true))

(defn- gen-dots [ dot-size]
  (for [x (range 100 800 40) y (range 80 450 80)] (generate-dot x y dot-size)))

;|-------------------- handle input --------------------------|

(defn- get-direction []
  (cond
    (key-pressed? :dpad-left) :left
    (key-pressed? :dpad-right) :right))

;|--------------- handle player position -----------------|

(defn- get-angle [direction]
  (case direction
    :right 0
    :left 180))

(defn- get-new-x [direction entity ]
  (let [speed (:speed entity)
        new-x (case direction
                :right (+ (:x entity) speed)
                :left (- (:x entity) speed)
                (:x entity))]
    (if (or (< new-x 0) (<= (- 506 (:width entity)) new-x)) ;apply screen x boundaries
      (:x entity)
      new-x)))



(defn- update-player-position [{:keys [player?] :as entity}]
  (if player?
    (let [direction (get-direction)
          x (get-new-x direction entity)
          angle (get-angle direction)]
      (assoc entity :x x :y y :angle angle :direction direction))
    entity))


;|----------------------------- handle collectable dots ------------------------------|

(defn- update-collected-list [{:keys [player? dot?] :as entity}]
  (if (or player? dot?)
    (assoc entity :hit-box (rectangle (:x entity) (:y entity) (:width entity) (:height entity))) ;use rectangle because it will be easy to use for collision detection
    entity))

(defn- remove-collected-dots [entities]
  (if-let [dots (filter #(contains? % :dot?) entities)] ;get only dot entities
    (let  [player  (some #(when (:player? %) %) entities)  ;some returns the first logical true!
          touched-dots (filter #(rectangle! (:hit-box player) :overlaps (:hit-box %)) dots)]     ;use rectangle! because we already have a rectangle and we want to call a function on it
      (remove (set touched-dots) entities))
    entities))


;|---------------- move & collect ------------------ |

(defn- move-and-collect [entities]
  (->> entities
       (map (fn [entity]
              (->> entity
                   (update-player-position)
                   (update-collected-list))))
       (remove-collected-dots)))


;----------------------- main screen ------------------------ |
(defscreen main-screen

           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage))


             (let [background (texture "background.png")
                   player-size 60
                   dot-size (/ player-size 3)
                   player (assoc (texture "pac.png")
                            :x 40 :y 40 :width player-size  :height player-size :angle 0  :player? true :direction :right :speed 15)
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
               (get-direction) (move-and-collect entities))))

(defgame simple-pacman-game
         :on-create
         (fn [this]
           (set-screen! this main-screen)))