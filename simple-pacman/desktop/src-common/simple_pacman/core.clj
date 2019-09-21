(ns simple-pacman.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))

(def background-img "background.png")
(def pacman-img "pac.png")
(def dot-img "dot.png")


(def window-height 506)
(def window-width 900)
(def pac-size 60)
(def c-size 20)                                   ;  collectable dots size : height & width

(declare simple-pacman-game main-screen)
(def speed 15)


(defn- get-collectable-y [line-num]
  (+ pac-size (* line-num 180)))

(defn- get-collectable-x [collectables-num]
  (let [spaces (* pac-size (+ collectables-num 1))]
    (+ spaces (* c-size collectables-num))))

(defn- generate-collectable [ line-num collectables-num ]
  "scatter collectables around the screen"
  (let [x (get-collectable-x collectables-num)
        y (get-collectable-y line-num)]
    (assoc (texture (str dot-img)) :x x :y y :width c-size :height c-size :collectable? true)))


(defn- generate-collectables-line [line-number  ]
  (map #(generate-collectable line-number % ) (range 0 10)))

(defn- generate-collectables-lines []
  (map #(generate-collectables-line % ) (range 0 5)))


;|-------------------- player input --------------------------|


(defn- get-new-angle [direction]
  (let [new-angle (case direction
                    :right 0
                    :up 90
                    :left 180
                    :down 270)]
    new-angle))

(defn- get-direction []
  (cond
    (key-pressed? :dpad-up) :up
    (key-pressed? :dpad-down) :down
    (key-pressed? :dpad-left) :left
    (key-pressed? :dpad-right) :right))

;|--------------- player position & collecting -----------------|

(defn- get-new-x [direction entity ]
  (let [new-x (case direction
                :right (+ (:x entity) speed)
                :left (- (:x entity) speed)
                (:x entity))]
    (if (or (< new-x 0) (<= (- window-width pac-size) new-x))
      (:x entity)
      new-x)))

(defn- get-new-y [direction entity ]
  (let [new-y (case direction
                :up (+ (:y entity) speed)
                :down (- (:y entity) speed)
                (:y entity))]
    (if (or (< new-y 0) (<= (- window-height pac-size) new-y))
      (:y entity)
      new-y)))

(defn- update-player-position [{:keys [player?] :as entity}]
  (if player?
    (let [direction (get-direction)
          x (get-new-x direction entity)
          y (get-new-y direction entity)
          new-angle (get-new-angle direction)]
      (assoc entity :x x :y y :angle new-angle :direction direction))
    entity))

(defn- update-collected-list [{:keys [player? collectable?] :as entity}]
  (if (or player? collectable?)
    (assoc entity :hit-box (rectangle (:x entity) (:y entity) (:width entity) (:height entity)))
    entity))

(defn- remove-collected-obj [entities]
  (if-let [collectables (filter #(contains? % :collectable?) entities)]
    (let [player  (some #(when (:player? %) %) entities)  ;some returns the first logical true!
          touched-collectables (filter #(rectangle! (:hit-box player) :overlaps (:hit-box %)) collectables)]     ;use rectangle! because we already have a rectangle and we want to call a function on it
      (remove (set touched-collectables) entities))
    entities))

(defn- move-and-collect [entities]
  (->> entities
       (map (fn [entity]
              (->> entity
                   (update-player-position)
                   (update-collected-list))))
       (remove-collected-obj)))

(defscreen main-screen

           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage))


             (let [background (texture background-img)
                   player (assoc (texture pacman-img)
                            :x 40 :y 40 :width pac-size  :height pac-size :angle 0  :player? true :direction :right)
                   collectables (generate-collectables-lines)
                   ]
               [background player collectables]))

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



