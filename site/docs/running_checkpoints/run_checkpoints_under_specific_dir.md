---
sidebar_position: 2
---

# Run checkpoints under specific directory

There may be a situation where you would be interested in triggering all checkpoints kept under specific directory 
in checkpoints folder.

The restonomer framework provides you with an option to run all checkpoints kept under specific directory using the method 
`runCheckpointsUnderDirectory(checkpointsDirectoryPath)`

The only input required from user is the path of the directory relative to the `checkpoints` folder.

Suppose, if you want to run all checkpoints kept under `sub_dir_1` directory, then it can be done in below manner:

```scala
import com.clairvoyant.restonomer.core.app.RestonomerContext

private val restonomerContextDirectoryPath = "./restonomer_context"
private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)

restonomerContext.runCheckpoint(checkpointsDirectoryPath = "sub_dir_1")
```
