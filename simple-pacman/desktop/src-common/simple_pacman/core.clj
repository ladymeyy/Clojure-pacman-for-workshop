(ns simple-pacman.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))



(declare simple-pacman-game main-screen)

;|------------------assets ----------------------------|
(def background-img "background.png")
(def pacman-img "pac.png")
(def dot-img "dot.png")

;|----------------global size & speed ------------- |
(def window-height 506)
(def window-width 900)
(def pac-size 60)
(def dot-size 20)                                   ;  collectable dot size : height & width
(def speed 15)


;|----------------- yellow dots rows ------------------|

(defn- generate-dot [ x y ]
  "generate collectable dot"
  (assoc (texture (str dot-img)) :x x :y y :width dot-size :height dot-size :collectable? true))


(defn- generate-dots-row [y]
  "generates dots in row y"
  (map generate-dot (range 100 800 40) (repeat y)))


(defn- generate-collectables-rows []
  "generates rows of collectables dots. "
  (map generate-dots-row (range 80 450 80)))

;|-------------------- handle player input --------------------------|


(defn- get-direction []
  (cond
    (key-pressed? :dpad-up) :up
    (key-pressed? :dpad-down) :down
    (key-pressed? :dpad-left) :left
    (key-pressed? :dpad-right) :right))

;|--------------- handle player position -----------------|

(defn- get-angle [direction]
  (let [new-angle (case direction
                    :right 0
                    :up 90
                    :left 180
                    :down 270)]
    new-angle))

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



;|----------------------------- handle collectable dots ------------------------------|
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



;|---------------- move & collect ------------------ |

(defn- move-and-collect [entities]
  (->> entities
       (map (fn [entity]
              (->> entity
                   (update-player-position)
                   (update-collected-list))))
       (remove-collected-obj)))


;----------------------- main screen ------------------------ |
(defscreen main-screen

           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage))


             (let [background (texture background-img)
                   player (assoc (texture pacman-img)
                            :x 40 :y 40 :width pac-size  :height pac-size :angle 0  :player? true :direction :right)
                   collectables (generate-collectables-rows)]
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



