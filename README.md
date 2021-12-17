# Static Code Analysis

This tool performs a static code analysis of compiled java code. 
For all classes within a given base package, all invocations will be collected. Then, an invocation matrix is printed 
for all classes matching a given pattern.

The tool is intended to analyze legacy code with many dependencies between the classes (e.g., a service being used by 20 services and using 20 other services).
Using the invocation matrix, one could detect that different sets of methods are used by different classes. 
Thus, the regarded class could be split up into multiple smaller, less complex classes. 
Or, as a first step, you could introduce interfaces for the different calling classes.
With that, the analysis could help to reduce the dependency mesh, improving the readability and maintainability of the code.

**Disclaimer**: This tool will and can only perform a technical analysis. 
You have to interpret the result regarding the business aspects of that class, 
and you have to decide whether splitting the class makes sense in the first place.



## Usage
### Preconditions
Java 17 is required.
### Command Line Arguments

`java -cp <additional-classpath>:static-code-analysis-0.1.jar de.andrena.tools.staticcodeanalysis.application.Main <package prefix> <class name pattern>`

* `<additional-classpath` specifies the classes that should be analyzed.
* `<package prefix>` specifies the base package. All classes that reside in that package or in a subpackage will be analyzed.
* `<class name pattern>` specifies the pattern of the classes for that the invocation matrix should be printed. E.g., `.*Service` will print the matrix for all classes with the suffix `Service`.

### Example
```java -cp build/test-classes:static-code-analysis-0.2.jar de.andrena.tools.staticcodeanalysis.application.Main de.andrena .*Service```

This will analyze the calls below package `de.andrena` and show the invocation matrix for classes ending with `Service`.

Output:
```
 Analyzing classes with root package de.andrena, showing calls to classes matching pattern '.*Service'
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

# License

This code is licensed under [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license](http://creativecommons.org/licenses/by-nc-sa/4.0/).

&copy; andrena objects ag