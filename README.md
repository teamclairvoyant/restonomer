# Restonomer

Restonomer has been designed with a clear focus on solving the various data retrieval issues while fetching huge datasets from REST APIs that include:

* implementing an API-specific custom authentication mechanism

* implementing a solution for over-fetching and under-fetching of data

* implementing an API-specific pagination mechanism to retrieve complete data

* implementing an efficient auto retry mechanism in case of request failure

* implementing a fault-tolerant and highly performant solution that is scalable

* implementing an optimised solution that works in a concurrent and distributed fashion


Restonomer also provides users with a toolkit for transformation and persistance of the data that include:

* dealing with a variety of response data types (String / Bytes / Zipped)

* dealing with a variety of response data formats (JSON / CSV / XML)

* transforming the datasets into the required structure

* persisting the huge datasets to a specific storage system



Restonomer, at a high level, aims at providing users with a fault-tolerant and scalable solution to retrieve huge datasets from REST APIs, transform the datasets, and persist the datasets in a concurrent and distributed fashion.


## Getting Started

### Add SBT Dependency

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

libraryDependencies += "com.clairvoyant.restonomer" %% "restonomer-core" % "2.0"
```

`<github_token>` is the Personal Access Token with the permission to read packages.

### Simple Restonomer Client Application

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

## Restonomer Usage Guide Document

You can find the documentation on how to use Restonomer and all of its features along with well described 
examples here:

[Restonomer Usage Guide](https://teamclairvoyant.github.io/restonomer/)