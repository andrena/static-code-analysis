# Static Code Analysis

This tool performs a static code analysis of compiled java code. 
Currently, the following analyses are implemented:
* **invocations**: per class, show a matrix that shows which public methods are invoked by which other services
* **fieldAccesses**: per class, show a matrix which fields are used by which public methods

With those analyses, legacy code can be analyzed to get some ideas how to split up complex classes.
This could help to reduce the complexity of the code, improving its readability and maintainability.

**Disclaimer**: This tool will and can only perform a technical analysis.
You have to interpret the result regarding the business aspects of that class,
and you have to decide whether a refactoring of that class makes sense in the first place.

## Invocations

For all classes within a given base package, all invocations will be collected. Then, an invocation matrix is printed 
for all classes matching a given pattern.

The tool is intended to analyze legacy code with many dependencies between the classes (e.g., a service being used by 20 services and using 20 other services).
Using the invocation matrix, one could detect that different sets of methods are used by different classes. 
Thus, the regarded class could be split up into multiple smaller, less complex classes. 
Or, as a first step, you could introduce interfaces for the different calling classes.

## Field Accesses

All classes within a given base package that match the given pattern will be analysed in turn.
For each class, all accesses of methods to fields are collected. Then, a field access matrix is printed,
showing for each public method of that class which fields are accessed (also transitively).

With the result, you could detect a low cohesion in a class. 
If, e.g., one half of the methods uses only one half of the fields, while the other half
of the methods uses only the other half of the fields, this class could be split up in two.

## Usage
### Preconditions
Java 17 is required.
### General Command Line Arguments

`java -cp <additional-classpath>:static-code-analysis-0.3.jar de.andrena.tools.staticcodeanalysis.application.Main <analysis> <further parameters...>`

### Invocations
#### Command Line Arguments
`java -cp <additional-classpath>:static-code-analysis-0.3.jar de.andrena.tools.staticcodeanalysis.application.Main invocations <package prefix> <class name pattern>`

* `<additional-classpath` specifies the classes that should be analyzed.
* `<package prefix>` specifies the base package. All classes that reside in that package or in a subpackage will be analyzed.
* `<class name pattern>` specifies for which classes the invocation matrix should be printed. E.g., `.*Service` will print the matrix for all classes with the suffix `Service`. The fully quantified class name is regarded.

##### Example
```java -cp build/test-classes:static-code-analysis-0.3.jar de.andrena.tools.staticcodeanalysis.application.Main invocations de.andrena .*Service```

This will analyze the method invocations below package `de.andrena` and show the invocation matrix for classes ending with `Service`.

Output:
```
 Analyzing classes with root package de.andrena, showing invocations of classes matching pattern '.*Service'
 Found 9 classes
 Found 4 invoked classes matching pattern .*Service
 <...>
 Dependencies for de.andrena.tools.staticcodeanalysis.sample.invocations.SampleService

   1 OneController
   2 OtherController

                             1  2
              void <init>()  X  X
 String build(long,boolean)     X
  String build(int,boolean)     X
            String map(int)  X  
           String map(long)  X  
```

With that, you could see directly that OneController only uses the map-methods, 
while OtherController only uses the build-Methods. Thus, SampleService might be split up 
into two classes, or at least it would be possible to introduce one interface per controller. 
Note that `<init>` denotes the constructor of `SampleService`.

### Field Accesses
#### Command Line Arguments
`java -cp <additional-classpath>:static-code-analysis-0.3.jar de.andrena.tools.staticcodeanalysis.application.Main fieldAccesses <package prefix> <class name pattern>`

* `<additional-classpath` specifies the classes that should be analyzed.
* `<package prefix>` specifies the base package. All classes that reside in that package or in a subpackage will be considered.
* `<class name pattern>` specifies for which classes the field access matrix should be printed. E.g., `.*Service` will print the matrix for all classes with the suffix `Service`. The fully quantified class name is regarded.

##### Example
```java -cp build/test-classes:static-code-analysis-0.3.jar de.andrena.tools.staticcodeanalysis.application.Main fieldAccesses de.andrena SimpleUsages```

This will analyze the field accesses for classes below package `de.andrena` with the name `SimpleUsages`.

Output:
```
Analyzing classes with root package de.andrena.tools.staticcodeanalysis.sample.fieldAccesses, showing field accesses of classes matching pattern 'SimpleUsages'
Found 1 classes
Field accesses of public methods for de.andrena.tools.staticcodeanalysis.sample.fieldAccesses.SimpleUsages
                
  1 int usesA()
  2 long usesAB()
  3 int usesC()
                
   1  2  3
a  X  X   
b     X   
c        X
```

With that, you could see directly that field `c` is only used in method `usesC()`, 
and `usesC()` only uses field `c`. Thus, from a technical point of view, this class
could be split up into two classes.

# License

This code is licensed under [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license](http://creativecommons.org/licenses/by-nc-sa/4.0/).

&copy; andrena objects ag