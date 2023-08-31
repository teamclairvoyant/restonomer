# Simple Client Application

* Import the `RestonomerContext` class:

  ```scala
  import com.clairvoyant.restonomer.app.RestonomerContext
  ```

* Create `RestonomerContext` instance:

  User can create the restonomer context instance by passing the restonomer context directory path to the constructor 
  of RestonomerContext class.

  Currently, user can provide the local file system path or GCS path for the restonomer context directory.

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
