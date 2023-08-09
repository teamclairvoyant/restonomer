---
sidebar_position: 3
---

# Application Configurations

Application configurations are high level configs that application requires in order to behave in a specific way.

Application configurations are represented by a case class `ApplicationConfig`.

Application configurations are provided in a file `application.conf` that is kept under restonomer context directory.

This conf file contains all application related configurations such as spark configurations.

Below is the sample `application.conf` file:

```hocon
spark-configs = {
  "spark.master" = "local[*]"
}
```
