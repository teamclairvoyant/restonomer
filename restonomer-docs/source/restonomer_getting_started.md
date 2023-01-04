# Add SBT Dependency

To use Restonomer in an existing SBT project with Scala 2.12 or a later version, 
add the following dependency to your `build.sbt`

```sbt
libraryDependencies += "com.clairvoyant.restonomer" %% "restonomer-core" % "1.0"
```

# Simple Restonomer Client Application

* Import the `RestonomerContext` class:

  ```scala
  import com.clairvoyant.restonomer.core.app.RestonomerContext
  ```

* Create `RestonomerContext` instance:

  User can create the restonomer context instance by passing the restonomer context directory path to the constructor 
  of RestonomerContext class.

  ```scala
  private val restonomerContextDirectoryPath = "<restonomer_context_directory_path>"
  private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)
  ```

* Run Checkpoints:

  Once the restonomer context instance is created, user can use various methods provided by the instance to run specific 
  checkpoints or all checkpoints as desired.

  ```scala
  restonomerContext.runAllCheckpoints()
  ```