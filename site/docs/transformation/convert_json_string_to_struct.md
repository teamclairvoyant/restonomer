# ConvertJSONStringToStruct

It lets the user convert a column of JSON string type to struct type.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                      |
| :-------------- | :-------: | :-----------: | :----------------------------------------------- |
| column-name     |    Yes    |       -       | Name of the column which needs to be transformed |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": "{\"col_B\":\"val_B1\",\"col_C\":\"val_C1\"}"
}
```

Now, suppose the requirement is to transform `col_A` to struct type.

Then, user can configure the `ConvertJSONStringToStruct` transformation in the below manner:

```hocon
{
  type = "ConvertJSONStringToStruct"
  column-name = "col_A"
}
```

The transformed response will now have the `col_A` of Struct type:

```json
{
  "col_A": {
    "col_B": "val_B1",
    "col_C": "val_C1"
  }
}
```
