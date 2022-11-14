# Run single checkpoint

Once you have defined a checkpoint configuration file, then the next step is to trigger the checkpoint.

The restonomer framework provides you the way to execute just a single checkpoint.

This can be achieved using the method `runCheckpoint(checkpointFilePath)` provided by `restonomerContext` instance.

The only thing needed from you is the path of the configuration file relative to the `checkpoints` directory.

For example, suppose you have below restonomer context directory structure:

```text
restonomer_context\
    -   checkpoints\
            -   checkpoint_1.conf
            -   checkpoint_2.conf
            -   sub_dir_1\
                    -   checkpoint_3.conf
```

Then you can trigger the `checkpoint_1.conf` in the below manner:

```scala
import com.clairvoyant.restonomer.core.app.RestonomerContext

private val restonomerContextDirectoryPath = "./restonomer_context"
private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)

restonomerContext.runCheckpoint(checkpointFilePath = "checkpoint_1.conf")
```

And, if you want to trigger the checkpoint `checkpoint_3.conf` that is kept under `sub_dir_1` directory inside `checkpoints` folder, 
then you can trigger the same in below manner:

```scala
import com.clairvoyant.restonomer.core.app.RestonomerContext

private val restonomerContextDirectoryPath = "./restonomer_context"
private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)

restonomerContext.runCheckpoint(checkpointFilePath = "sub_dir_1/checkpoint_3.conf")
```

# Run all checkpoints under specific directory

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
