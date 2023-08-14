# CastColumnsBasedOnSuffix

It lets the user cast the data type of multiple columns to the desired different types at once based on the suffix of the columns.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                                                                     |
|:----------------|:---------:|:-------------:|:----------------------------------------------------------------------------------------------------------------|
| suffix          |    Yes    |       -       | It defines the suffix based on which given columns to be selected to cast them to the desired data type         |
| data-type       |    Yes    |       -       | It defines the desired data type to which the columns have to be casted                                         |

For example, consider we have below restonomer response in json:

```json
{
  "name": "abc",
  "india_price": 200,
  "US_price": 12,
  "percentage_difference": 3.4678
}
```

Now, suppose the requirement is to cast the columns containing `price` as suffix into `decimal(19,2)` data type.
Then, user can configure the `CastColumnsBasedOnSuffix` transformation in the below manner:

```hocon
{
 type = "CastColumnsBasedOnSuffix"
 suffix = "price"
 data-type = "decimal(19,2)"
}
```

The transformed response will now have the desired columns with the desired data types.

```json
{
  "name": "abc",
  "india_price": 200.00,
  "US_price": 12.00,
  "percentage_difference": 3.4678
}
```
