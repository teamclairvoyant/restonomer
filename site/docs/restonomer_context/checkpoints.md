---
sidebar_position: 2
---

# Checkpoints

A checkpoint is the main entry point for any request trigger via restonomer.
A checkpoint is a configuration that you need to provide in the HOCON format to the restonomer application.

A checkpoint configuration is basically represented using the case class `CheckpointConfig`.

User would need to write a checkpoint configuration in a file and keep it in `checkpoints` directory under restonomer context directory.

Below is the sample checkpoint configuration file:

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
      password = "token[$.secret]"
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
