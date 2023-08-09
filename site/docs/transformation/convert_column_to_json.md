# ConvertColumnToJson

It lets the user convert MapType or Struct type to JSON string.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                      |
|:----------------|:---------:|:-------------:|:-------------------------------------------------|
| column-name     |    Yes    |       -       | Name of the column which needs to be transformed |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": "1",
  "col_B": [
    {
      "Zipcode": 704,
      "ZipCodeType": "STANDARD"
    }
  ]
}
```

Now, suppose the requirement is to transform col_B to json string:

Then, user can configure the `ConvertColumnToJson` transformation in the below manner:

```hocon
{
  type = "ConvertColumnToJson"
  column-name = "col_B"
}
```

The transformed response will now have the columns with the desired data types:

```json
{
  "col_A": 1,
  "col_B": "[{'ZipCodeType':'STANDARD','Zipcode':704}]"
}
```
