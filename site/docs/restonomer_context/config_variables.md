---
sidebar_position: 4
---

# Config Variables

While providing the checkpoint configuration, there may come a situation where you would not want to provide a static 
value for any particular field but rather want to provide some dynamic value which is passed on to the application 
at runtime. Here comes config variables to the rescue.

The config variables are denoted like `${<config_variable>}`

Example: `${BASIC_AUTH_TOKEN}`

Below is the sample checkpoint configuration using config variables:

```hocon
name = "sample_postman_checkpoint"

token = {
  token-request = {
    url = "http://localhost:8080/token-response-body"

    authentication = {
      type = "BearerAuthentication"
      bearer-token = "test_token_123"
    }
  }

  token-response-placeholder = "ResponseBody"
}

data = {
  data-request = {
    url = "https://postman-echo.com/basic-auth"

    authentication = {
      type = "BasicAuthentication"
      user-name = "postman"
      password = ${BASIC_AUTH_TOKEN}
    }
  }

  data-response = {
    body = {
      type = "Text"
      text-format = {
        type = "JSONTextFormat"
      }
    }

    persistence = {
      type = "LocalFileSystem"
      file-format = {
        type = "ParquetFileFormat"
      }
      file-path = "./rest-output/"
    }
  }
}
```

There are three ways of substituting the config variables by their actual values at runtime:

* **Config variables from file:**

  You can provide config variable and its value in `config_variables.conf` file kept in `uncommitted` folder under 
  restonomer context directory.

    Below is the sample `config_variables.conf` file content:

    ```hocon
    BASIC_AUTH_TOKEN = "token1234"
    BEARER_AUTH_TOKEN = "token5678"
    ```

* **Config variables from application arguments:**

  You can provide a list of key-value pairs as a map to the restonomer context instance:

  ```scala
  import com.clairvoyant.restonomer.core.app.RestonomerContext
  
  object RestonomerApp extends App {
    val configVariables = Map(
      "BASIC_AUTH_TOKEN" -> "token1234",
      "BEARER_AUTH_TOKEN" -> "token5678"
    )
  
    private val restonomerContext = RestonomerContext(
      configVariablesFromApplicationArgs = configVariables
    )
  
    restonomerContext.runAllCheckpoints()
  }
  ```

* **Environment Variables:**
  
  You can provide config variable and its value by setting it in environment variable before running the application.

**CONFIG VARIABLES SUBSTITUTION PRIORITY**

The restonomer application first looks for the substitute value in the config variables from file `config_variables.conf`.
If the value is not present over there, then it starts looking for the value in the config variables from application arguments. 
If the value is still not present, then the application looks at the environment variables.

```text
Config variables from file > Config variables from application args > Environment variables
```

In case the value is not present in any of the above three options, then the value is not substituted and the application reads 
the checkpoint configuration the same way it is provided without any substitution.
