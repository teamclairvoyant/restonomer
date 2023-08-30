# SelectColumnsWithExpressions

It lets the user select and transform columns in a dataframe using SQL expressions. 

This transformation expects user to provide below inputs:

| Input Arguments    | Mandatory | Default Value | Description                                         |
| :----------------- | :-------: | :-----------: | :-------------------------------------------------- |
| column-expressions |    Yes    |       -       | List of expressions for the columns to be selected. |

For example, consider we have below restonomer response in json:

```json
{
  "id": 1,
  "name": "Alice",
  "age": 25
}
```

Now, suppose the requirement is to:
* Rename column `name` as `full_name`
* Select new column `age_next_year` with value of `age` + 1

Then, user can configure the `SelectColumnsWithExpressions` transformation in the below manner:

```hocon
{
  type = "SelectColumnsWithExpressions"
  column-expressions = [
      "id",
      "name as full_name",
      "age + 1 as age_next_year"
  ]
}
```

The transformed response will select the desired columns from dataframe as shown below:

```json
{
  "id": 1,
  "full_name": "Alice",
  "age_next_year": 26
}
```
