##Immutability 

When data is immutable, its state cannot change after it’s created.
 If you want to change an immutable object, you can’t. Instead, you create a new object with the new value.
 


##Keywords in clojure : 

  #####map with keywords instead of multiple objects:
  So how did we replace the player object we usually have in games developed in oop? 
  lets take a look at the code generating the player entity: 
  ```
   (assoc (texture "pac.png") :player? true :x 40 :y 40 :width 60  :height 60 :angle 0  :direction :right :speed 15) 
  ```
  
  The assoc function as described in https://clojuredocs.org/clojure.core/assoc :                              
" assoc[iate]. When applied to a map, returns a new map of the
  same (hashed/sorted) type, that contains the mapping of key(s) to
  val(s)."
  
  The ```(texture "pac.png") ``` function returns a game entity - which is a *map*.
  
  So throughout the code of this game we will use ```assoc``` to generate a NEW PLAYER ENTITY  
  with the new values needed. 
  This is how we keep all of the functions - pure! and the entities of the game - immutable! 
   
   

    
 