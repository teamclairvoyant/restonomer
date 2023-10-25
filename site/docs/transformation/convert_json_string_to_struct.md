# Convert JSON String To Struct

It lets the user convert a column of JSON string type to struct type.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                          |
|:----------------|:---------:| :-----------: |:-----------------------------------------------------|
| column-name     |    Yes    |       -       | Name of the column which needs to be transformed     |
| schema-ddl      |    No     |       -       | The Data Definition Language (DDL) for the column    |

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

Let's take another example, consider we have below restonomer response in json:

```json
{
  "col_A": "val_A",
  "col_B": "{\"col_C\": \"val_C\",\"col_D\": 5}"
}
```

Now, suppose the requirement is to transform `col_B` to struct type using specific schema.

Then, user can configure the `ConvertJSONStringToStruct` transformation in the below manner:

```hocon
{
  type = "ConvertColumnToJson"
  column-name = "col_B"
  schema-ddl = "col_C STRING, col_D STRING"
}
```

The transformed response will now have the `col_B` of Struct type:

```json
{
  "col_A": "val_A",
  "col_B": {
    "col_C": "val_C",
    "col_D": "5"
  }
}
```
