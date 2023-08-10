---
sidebar_position: 3
---

# Run all checkpoints

If you want to run all checkpoints kept under `checkpoints` directory, then you can use `runAllCheckpoints()` method 
provided by `restonomerContext` instance.

The `runAllCheckpoints()` method runs all checkpoints kept under `checkpoints` directory irrespective of whether a 
checkpoint is at the root level or inside any subdirectory.

You can trigger all checkpoints in below manner:

```scala
import com.clairvoyant.restonomer.core.app.RestonomerContext

private val restonomerContextDirectoryPath = "./restonomer_context"
private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)

restonomerContext.runAllCheckpoints()
```
