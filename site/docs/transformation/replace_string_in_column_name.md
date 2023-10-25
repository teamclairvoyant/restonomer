# Replace String In Column Name

It lets the user replace the pattern in the column name specified by user with the other text.

This transformation expects user to provide below inputs:

| Input Arguments     | Mandatory | Default Value | Description                                                                                |
|:--------------------|:---------:|:-------------:|:-------------------------------------------------------------------------------------------|
| column-name         |    Yes    |       -       | It is the column name                                                                      |
| pattern             |    Yes    |       -       | The values that needs to be replaced                                                       |
| replacement         |    Yes    |       -       | The value that replaces the pattern                                                        |
| replace-recursively |    No     |     False     | Flag to determine if operation needs to be performed at root level only or at nested level |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_D": {
    "col_B": 6
  },
  "col_F": [
    {
      "col_B": 4.356343
    }
  ]
}
```

Now, suppose the requirement is to replace the `_B` in column name `col_B` with `_B_test` at root and nested level.

Then, user can configure the `ReplaceStringInColumnName` transformation in the below manner:

```hocon
{
  type = "ReplaceStringInColumnName"
  column-name = "col_B"
  pattern = "_B"
  replacement = "_B_test"
  replace-recursively = true
}
```

The transformed response will have the replaced value or pattern in the desired column as shown below.

```json
{
  "col_A": 5,
  "col_B_test": 4,
  "col_D": {
    "col_B_test": 6
  },
  "col_F": [
    {
      "col_B_test": 4.356343
    }
  ]
}
```
