# ConvertArrayOfStructToArrayOfJSONString

It lets the user convert columns of array of struct type to array of json string type.

For example, consider we have below restonomer response in json:

```json
{
  "col_A": [
    {
      "col_B": "val_B1",
      "col_C": "val_C1"
    },
    {
      "col_B": "val_B2",
      "col_C": "val_C2"
    }
  ]
}
```

Now, suppose the requirement is to transform `col_A` to array of json string.

Then, user can configure the `ConvertArrayOfStructToArrayOfJSONString` transformation in the below manner:

```hocon
{
  type = "ConvertArrayOfStructToArrayOfJSONString"
}
```

The transformed response will now have the `col_A` with the desired array of string data type:

```json
{
  "col_A": [
    "{\"col_B\":\"val_B1\",\"col_C\":\"val_C1\"}",
    "{\"col_B\":\"val_B2\",\"col_C\":\"val_C2\"}"
  ]
}
```
