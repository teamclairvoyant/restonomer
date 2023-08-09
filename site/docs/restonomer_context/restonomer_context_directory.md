---
sidebar_position: 1
---

# Restonomer Context Directory

The restonomer context directory is a base location for keeping all checkpoints.

User would need to provide the restonomer context directory path in order to instantiate the `RestonomerContext` object.

```scala
import com.clairvoyant.restonomer.core.app.RestonomerContext

private val restonomerContextDirectoryPath = "./restonomer_context"
private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)
```

The structure of restonomer context directory looks like below:

```text
restonomer_context
  - checkpoints/
  - uncommitted/
  - .gitignore
  - application.conf
```
