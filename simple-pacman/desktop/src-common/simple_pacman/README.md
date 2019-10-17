## **Pure functions:**

####Definition
A function is a *pure function* if: 

    - It returns the same result if given the same arguments 
    - It does not cause any observable side effects

####Return the same result & observable side effects:

* Reading Files: If our function reads external files, it’s not a pure function — the file’s contents can change.

* Random number generation: Any function that relies on a random number generator cannot be pure.

* modifying a global object or a parameter passed by reference.

####Benefits of pure functions:
 
	- They’re easier to reason about
	- They’re easier to combine
	- They’re easier to test
	- They’re easier to debug

 The key point is that because a pure function has no side effects or hidden I/O you can:
  - get a terrific idea of what it does just by looking at its signature.
  - easily isolate and test EACH FUNCTION in your code separately - which is highly important when you have complicated business logic  . 
 
Another important point is that when your code is constructed of pure functions, 
it basically turns your code into blocks that are easy to combine and re-use in many different contexts. 
