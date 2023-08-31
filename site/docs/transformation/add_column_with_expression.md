# AddColumnWithExpression

It lets the user add a new column with an expression value of the desired data type.

This transformation needs below inputs from the user:

| Input Arguments   | Mandatory | Default Value | Description                                                                                                                                                                        |
|:------------------|:---------:|:-------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| column-name       |    Yes    |       -       | Name of the new column to be added                                                                                                                                                 |
| column-expression |    Yes    |       -       | Expression for the value of the new column                                                                                                                                         |
| column-data-type  |    No     |       -       | The spark sql data type that new column needs to be casted                                                                                                                         |
| replace-existing  |    No     |     false     | If set to true, if a column already exists with the same name as `column-name`, it will get replaced with the new value. <br/>If set to false, then it returns original dataframe. |

User can configure the `AddColumnWithExpression` transformation in the below manner:

For example, consider we have below restonomer response in json:

```json
{
  "col_A": "val_A",
  "col_B": "val_B",
  "col_C": 10.2
}
```

Now, if the requirement is to add a new column `col_D` with the value twice as the value of `col_C`, then user can
configure the
`AddColumnWithExpression` transformation in the below manner:

```hocon
{
  type = "AddColumnWithExpression"
  column-name = "col_D"
  column-expression = "col_C * 2"
  replace-existing = true
}
```

The transformed response will now have the new column `col_D` added:

```json
{
  "col_A": "val_A",
  "col_B": "val_B",
  "col_C": 10.2,
  "col_D": 20.4
}
```