# Restonomer Context Directory

The restonomer context directory is a base location for keeping all checkpoints.

You would need to provide the restonomer context directory path in order to instantiate the `RestonomerContext` object.

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

# Checkpoints

A checkpoint is the main entry point for any request trigger via restonomer.
A checkpoint is a configuration that you need to provide in the HOCON format to the restonomer application.

A checkpoint configuration is basically represented using the case class `CheckpointConfig`:

```scala
case class CheckpointConfig(
    name: String,
    request: RequestConfig,
    response: ResponseConfig,
    httpBackendType: Option[String]
)
```

You would need to write a checkpoint configuration in a file and keep it in `checkpoints` directory under restonomer context directory.
Below is the sample checkpoint configuration file:

```hocon
name = "sample_postman_checkpoint"

request = {
  url = "https://postman-echo.com/basic-auth"
  
  authentication = {
    type = "basic-authentication"
    user-name = "postman"
    password = "password"
  }
}

response = {
  body = {
    format = "JSON"
  }
}
```

You can place a checkpoint file either:

* directly under `checkpoints` directory

OR

* you can have subdirectories under `checkpoints` directory and can keep checkpoint files in that subdirectory

For example, `checkpoints` directory can have below structure:

```text
restonomer_context\
    -   checkpoints\
            -   checkpoint_1.conf
            -   checkpoint_2.conf
            -   sub_dir_1\
                    -   checkpoint_3.conf
                    -   checkpoint_4.conf
                    -   sub_dir_2\
                            -   checkpoint_5.conf
            -   sub_dir_3\
                    -   checkpoint_6.conf
```

This kind of hierarchical structure helps user to categorise their checkpoints by keeping set of files in different subdirectories.

# Application Config

# Config Variables