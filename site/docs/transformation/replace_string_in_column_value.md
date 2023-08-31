# ReplaceStringInColumnValue

It lets the user replace the pattern in the column specified by user.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                          |
|:----------------|:---------:|:-------------:|:-------------------------------------|
| column-name     |    Yes    |       -       | It is the column name                |
| pattern         |    Yes    |       -       | The values that needs to be replaced |
| replacement     |    Yes    |       -       | The value that replaces the pattern  | 



For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3.4678
}
```

Now, suppose the requirement is to replace the col_A column values :

```text
col_A -> "abc"
col_B -> 4
col_C -> 3.4678
```

Then, user can configure the `ReplaceStringInColumnValue` transformation in the below manner:

```hocon
{
  type = "ReplaceStringInColumnValue"
  column-name = "col_A"
  pattern = 5
  replacement = "abc"
  }
```

The transformed response will have the replaced value or pattern in the desired column as shown below.

```json
{
  "col_A": "abc",
  "col_B": 4,
  "col_C": 3.4678
}
```
