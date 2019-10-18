#Functions as first-class entities
 The idea of functions as first-class entities is that
  functions are also treated as values and used as data.

 In Clojure it’s common to use ```defn```  to define functions, but this is just syntactic sugar for :
 ```
 (def foo (fn ...))
 ```
 ``` fn ``` returns the function itself. 
 ``` defn ``` returns a var which points to a function object.
 
 ######Functions as first-class entities can:
 1. Refer to a function from constants and variables
 2. Pass functions as a parameter to other functions
 3. Return functions as result from other functions
 
 The idea is to treat functions as values and pass functions like data. 
 This way we can combine different functions to create new functions with new behavior.
