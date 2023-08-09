# CastColumnsBasedOnSubstring

It lets the user cast the data type of multiple columns to the desired different types at once based on the substring of the columns.

This transformation expects user to provide below inputs:

| Input Arguments   | Mandatory | Default Value | Description                                                                                                         |
|:------------------|:---------:|:-------------:|:--------------------------------------------------------------------------------------------------------------------|
| substring-list    |    Yes    |       -       | It defines the list of substrings based on which given columns to be selected to cast them to the desired data type |
| data-type-to-cast |    Yes    |       -       | It defines the desired data type to which the columns have to be casted                                             |

For example, consider we have below restonomer response in json:

```json
{
  "name": "abc",
  "product_india_price": 200,
  "product_US_price": 12,
  "percentage_difference": 3.4678
}
```

Now, suppose the requirement is to cast the columns containing "price" and "percent" into decimal(19,2) data type.
Then, user can configure the `CastColumnsBasedOnSubstring` transformation in the below manner:

```hocon
{
 type = "CastColumnsBasedOnSubstring"
 substring-list = ["price", "percent"]
 data-type-to-cast = "decimal(19,2)"
}
```

The transformed response will now have the desired columns with the desired data types.

```json
{
  "name": "abc",
  "product_india_price": 200.00,
  "product_US_price": 12.00,
  "percentage_difference": 3.47
}
```
