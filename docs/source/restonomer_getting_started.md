# Add SBT Dependency

To use Restonomer in an existing SBT project with Scala 2.12 or a later version, 
add the following dependency to your `build.sbt`

```sbt
resolvers += "Restonomer Github Repo" at "https://maven.pkg.github.com/teamclairvoyant/restonomer/"

credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "<github_user_name>",
  "<github_token>"
)

libraryDependencies += "com.clairvoyant.restonomer" %% "restonomer-core" % "2.2.0"
```

`<github_token>` is the Personal Access Token with the permission to read packages.

# Simple Restonomer Client Application

* Import the `RestonomerContext` class:

  ```scala
  import com.clairvoyant.restonomer.core.app.RestonomerContext
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