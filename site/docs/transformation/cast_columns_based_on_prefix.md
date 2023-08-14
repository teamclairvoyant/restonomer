# CastColumnsBasedOnPrefix

It lets the user cast the data type of multiple columns to the desired different types at once based on the prefix of the columns.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                                                             |
|:----------------|:---------:|:-------------:|:--------------------------------------------------------------------------------------------------------|
| prefix          |    Yes    |       -       | It defines the prefix based on which given columns to be selected to cast them to the desired data type |
| data-type       |    Yes    |       -       | It defines the desired data type to which the columns have to be casted                                 |

For example, consider we have below restonomer response in json:

```json
{
  "name": "abc",
  "price_in_india": 200,
  "price_in_uk": 12,
  "percentage_difference": 3.4678
}
```

Now, suppose the requirement is to cast the columns containing `price` prefix into `decimal(19,2)` data type.
Then, user can configure the `CastColumnsBasedOnPrefix` transformation in the below manner:

```hocon
{
 type = "CastColumnsBasedOnPrefix"
 prefix = "price"
 data-type = "decimal(19,2)"
}
```

The transformed response will now have the desired columns with the desired data types:

```json
{
  "name": "abc",
  "price_in_india": 200.00,
  "price_in_uk": 12.00,
  "percentage_difference": 3.4678
}
```
