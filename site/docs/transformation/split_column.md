# SplitColumn

This transformation allows user to create new columns using the value of another column that is a delimiter separated 
string.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                                                                         |
|:----------------|:---------:|:-------------:|:--------------------------------------------------------------------------------------------------------------------|
| from-column     |    Yes    |       -       | Name of the source column having delimiter separated string as a value from which new columns need to be created    |
| delimiter       |    Yes    |       -       | The delimiter by which a string is separated                                                                        |
| to-columns      |    Yes    |       -       | It is a map of new column name against the position of the value that is needed from the delimiter separated string |


For example, consider we have below restonomer response in json:

```json
{
  "address": "Apt-123,XYZ Building,Pune,Maharashtra"
}
```

Now, suppose the requirement is to create new columns `apt_number`, `society_name`, `city` and `state` from the `address` 
column as shown below:

```json
{
 "apt_number": "Apt-123",
 "society_name": "XYZ Building",
 "city": "Pune",
 "state": "Maharashtra"
}
```

Then, user can configure the `SplitColumn` transformation in the below manner:

```hocon
{
  type = "SplitColumn"
  from-column = "address"
  delimiter = ","
  to-columns = {
    "apt_number" = 0
    "society_name" = 1
    "city" = 2
    "state" = 3
  }
}
```

The transformed response will have filtered records as desired:

```json
{
  "address": "Apt-123,XYZ Building,Pune,Maharashtra",
  "apt_number": "Apt-123",
  "society_name": "XYZ Building",
  "city": "Pune",
  "state": "Maharashtra"
}
```
