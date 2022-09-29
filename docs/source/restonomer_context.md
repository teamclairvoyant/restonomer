# Restonomer Context Directory

The restonomer context directory is a base location for keeping all checkpoints.

You would need to provide the restonomer context directory path in order to instantiate the `RestonomerContext` object.

```scala
import com.clairvoyant.restonomer.core.app.RestonomerContext

private val restonomerContextDirectoryPath = "./restonomer_context"
private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)
```

The restonomer context directory contains below directories and files:
* `checkpoints/`
* `uncommitted/`
* `.gitignore`
* `application.conf`

The structure of restonomer context directory looks like below:

```scala
restonomer_context
  - checkpoints/
  - uncommitted/
  - .gitignore
  - application.conf
```

# Checkpoints

# Application Config

# Config Variables