##Higher-order functions
When we talk about higher-order functions, 
we mean a function that either: takes one or more functions as arguments,
or returns a function as its result.


Three of the most commonly used HoFs, 
originating in functional programming but so useful they’ve been copied in many imperative langauges,
are map, filter and reduce (or fold - that concept goes by several different names).

######examples from our code: 

```
 (map (fn [entity] "do something" ) entities)
 ```
 
 ```
(filter #(contains? % :dot?) entities)
 ```



###Functions as parameters
The paradigm is called functional programming largely because
functions can return functions, and functions can consume functions.
Add to that the idea of chaining functions together allowing function composition,
and things are looking pretty darn functional.

Since you can use them when you need to customize the behavior of a function - this increases re-usability of your code! 