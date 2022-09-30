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

* or you can have subdirectories under `checkpoints` directory and can keep checkpoint files in that subdirectory

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

# Application Configurations

Application configurations are high level configs that application requires in order to behave in a specific way.

Application configurations are represented by a case class `ApplicationConfig` :

```scala
case class ApplicationConfig(
    sparkConfigs: Option[Map[String, String]]
)
```

Application configurations are provided in a file `application.conf` that is kept under restonomer context directory.

This conf file contains all application related configurations such as spark configurations.

Below is the sample `application.conf` file:

```hocon
spark-configs = {
  "spark.master" = "local[*]"
}
```

# Config Variables

While providing the checkpoint configuration, there may come a situation where you would not want to provide a static 
value for any particular field but rather want to provide some dynamic value which is passed on to the application 
at runtime. Here comes config variables to the rescue.

The config variables are denoted like `${<config_variable>}`

Example: `${BASIC_AUTH_TOKEN}`

Below is the sample checkpoint configuration using config variables:

```hocon
name = "sample_postman_checkpoint"

request = {
  url = "https://postman-echo.com/basic-auth"
  
  authentication = {
    type = "basic-authentication"
    basic-token = ${BASIC_AUTH_TOKEN}
  }
}

response = {
  body = {
    format = "JSON"
  }
}
```

There are two ways of substituting the config variables by their actual values at runtime:

* You can provide config variable and its value in `config_variables.conf` file kept in `uncommitted` folder under 
restonomer context directory.

    Below is the sample `config_variables.conf` file content:

    ```hocon
    BASIC_AUTH_TOKEN = "token1234"
    BEARER_AUTH_TOKEN = "token5678"
    ```
  
* You can provide config variable and its value by setting it in environment variable before running the application.

The restonomer application first looks for the substitute value in environment variables. If the value is not 
present over there, then it starts looking for the value in the `config_variables.conf` file.

So, environment variables get priority over `config_variables.conf` file.

In case the value is not present in both, then the value is not substituted and the application reads the checkpoint 
configuration the same way it is provided.