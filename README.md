# Compiler

A **simple to use** compiler based on LALR(1) parser generator.  
It was written for my compiler course in Shahid Beheshti University.

## Getting Started

This project uses Java, hence to run it, you machine should be Java enabled. Project written and compiled with JDK version 8.

### Compilation

In order to compile project, you need some extra libraries for parser, AOP (Aspect oriented programming) and etc.

Required libraries are included in folder `lib` in root of project. 
This folder contain below folders:   
`ASM` : It contain library for generating bytecode. It must be included in java classpath.  
`AspectJ` : It contain library for compiling AOP files. It must be included in java classpath.
`Java-Cup`: It contain library for compiling cup parser. It must be included in java classpath.

Note: Enabling AOP for Intellij is not necessary however `AspectJ` libraries are required. IF you enable it, you can see
output like below when you have runtime error:
```
Runtime error:
    at: println(x)
    at: foreach (x in a) begin ... end
    at: function void main(string[] args)
Print a non-primitive value is not possible: int[]
```
If you need, you must use `aspectjtools` library and enable `Ajc` compiler in Intellij.

### Documentation

In most of class, you find java doc for methods to describe it's functionality. 
You can extend this project on your own needs.

## Built With

* [Cup-Parser](http://www2.cs.tum.edu/projects/cup/) - The API for create LALR(1) parser