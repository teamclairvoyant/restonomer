# Replace Nulls With Default Value

This transformation lets users replace nulls in the dataset with some default value.

This transformation needs below inputs from the user:

| Input Arguments | Mandatory | Default Value | Description                                                                               |
|:----------------|:---------:|:-------------:|:------------------------------------------------------------------------------------------|
| value-map       |    Yes    |       -       | The key of the map is the column name, and the value of the map is the replacement value. |

**Currently, this transformation is supported only for string columns.**

For example, consider we have below restonomer response in json:

```json
[
  {
    "col_A": null,
    "col_B": "val_B1",
    "col_C": "val_C1"
  },
  {
    "col_A": "val_A2",
    "col_B": "val_B2",
    "col_C": null
  }
]
```

Now, if the requirement is to replace nulls in `col_A` with value `Default_A` and nulls in `col_C` with `Default_C`,
then user can configure the `ReplaceNullsWithDefaultValue` transformation in the below manner:

```hocon
transformations = [
  {
    type = "ReplaceNullsWithDefaultValue"
    value-map = {
      "col_A" = "Default_A"
      "col_C" = "Default_C"
    }
  }
]
```

The transformed response will look like:

```json
[
  {
    "col_A": "Default_A",
    "col_B": "val_B1",
    "col_C": "val_C1"
  },
  {
    "col_A": "val_A2",
    "col_B": "val_B2",
    "col_C": "Default_C"
  }
]
```
