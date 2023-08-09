# DataResponseBodyConfig

In restonomer framework, the details about the http response body are captured via `DataResponseBodyConfig` class.

User need to provide below configs for Data Response Body Configuration:

| Config Name | Mandatory | Default Value | Description                                                                                                                               |
|:------------|:---------:|:-------------:|:------------------------------------------------------------------------------------------------------------------------------------------|
| type        |    Yes    |       -       | The response body format returned by the rest api. It can hold values like `json`, `csv` and `xml`.                                       |
| data-column |    No     |       -       | This is used in case the response is of json format and the dataset is not present at the root level but in some other attribute of json. |

The response body configurations are provided in the checkpoint file in the below manner:

```hocon
body = {
  type = "JSON"
  data-column = "data.items"
}
```
