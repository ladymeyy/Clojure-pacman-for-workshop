#Declarative and Imperative Programming


##Declarative programming 
A programming paradigm that expresses the logic of a computation without describing its control flow.

##Imperative programming 
 A programming paradigm that uses statements that change a program’s state.
 
 
 ####Imperative Example:
 
 ```
   int* range(){
        int results [700];
       
        for(int i=100; i <800; i+=40){
            results[i] = i;
        }
        return results;
    }
``` 

 1. This code describes HOW to do something. In each line of code, we’re either explicitly iterating over an array
    or explicitly laying out steps for how to implement the functionality we want (i.e inserting ```i``` into a specific index in the array ) .
 2. Another thing is that we’re mutating some piece of state :
    (If you’re unfamiliar with the term state, it’s basically information about something held in memory — which should sound a lot like variables).
    we create a variable called results, and then we continually modify it.
 3. This is annoying to read (in my opinion lol.. )


 ####Declarative Example: 
 
 ```
 (range 100 800 40)
```
 1.  In this tiny piece of Clojure code we’re describing WHAT we want to happen rather than HOW.
     (we don’t know HOW range is implemented, we also probably don’t care). 
 2. We’re not mutating any state. All of the mutations are abstracted inside of range. 
 3. It’s also more readable (once you get used to this type of syntax).
