(ns simple-pacman.core.desktop-launcher
  (:require [simple-pacman.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. simple-pacman-game "simple-pacman" 900 506)
  (Keyboard/enableRepeatEvents true))
