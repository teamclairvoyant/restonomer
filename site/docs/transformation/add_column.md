# AddColumn

It lets the user add a new column with a literal value of the desired data type or with a valid SQL expression.

This transformation needs below inputs from the user:

| Input Arguments   | Mandatory | Default Value | Description                                                |
|:------------------|:---------:|:-------------:|:-----------------------------------------------------------|
| column-name       |    Yes    |       -       | Name of the new column to be added                         |
| column-value-type |    Yes    |       -       | "literal" or "expression"                                  |
| column-value      |    Yes    |       -       | Literal value of the new column                            |
| column-data-type  |    No     |    string     | The spark sql data type that new column needs to be casted |

User can configure the `AddColumn` transformation in the below manner:

Example-1 : Where user wants to add a literal column

```hocon
{
  type = "AddColumn"
  column-name = "col_D"
  column-value-type = "literal"
  column-value = "val_D"
  column-data-type = "string"
}
```

Example-2 : Where user wants to add a derived column using SQL expression.

```hocon
{
  type = "AddColumn"
  column-name = "col_D"
  column-value-type = "expression"
  column-value = "((col_A + 122) * 100)"
  column-data-type = "string"
}
```
