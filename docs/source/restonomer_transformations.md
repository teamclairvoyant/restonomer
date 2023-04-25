# What is restonomer transformation ?

Once the api response has been received by the application and transformed into spark dataframe,
one can apply the sequence of pre-defined transformations on the response dataframe.

This gives user the ability to modify the data in the desired format before persisting it in the target system.

The restonomer transformation is represented by the sealed trait `RestonomerTransformation`.

You can add a configuration for the restonomer transformation in the checkpoint conf file in the 
below manner:

```hocon
name = "checkpoint_add_column_transformation"

data = {
  data-request = {
    url = "http://localhost:8080/add-column-transformation"
  }

  data-response = {
    body = {
      type = "JSON"
    }

    transformations = [
      {
        type = "AddLiteralColumn"
        column-name = "col_D"
        column-value = "val_D"
        column-data-type = "string"
      },
      {
        type = "AddLiteralColumn"
        column-name = "col_E"
        column-value = "val_E"
        column-data-type = "string"
      }
    ]
  }
}
```

# Types of restonomer transformations

## SelectColumns

It lets the user select a list of columns from the dataframe.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                |
|:----------------|:---------:|:-------------:|:-------------------------------------------|
| column-names    |    Yes    |       -       | It is the list of columns required by user |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3.4678
}
```

Now, suppose the requirement is to select 2 columns from dataframe :

```text

  "col_B": 4,
  "col_C": 3.4678

```

Then, user can configure the `SelectColumns` transformation in the below manner:

```hocon
{
  type = "select-columns"
  column-names= ["col_B", "col_C"]
  }
```

The transformed response will select the desired column from dataframe as shown below.

```json
{
  "col_B": 4,
  "col_C": 3.4678
}
```

## AddColumn

It lets the user add a new column with a literal value of the desired data type or with a valid SQL expression.

This transformation needs below inputs from the user:

| Input Arguments  | Mandatory | Default Value | Description                                                |
|:-----------------|:---------:|:-------------:|:-----------------------------------------------------------|
| column-name      |    Yes    |       -       | Name of the new column to be added                         |
| column-value     |    Yes    |       -       | Literal value of the new column                            |
| value-type       |    yes    |       -       | "literal" or "expression"                                  |
| column-data-type |    No     |    string     | The spark sql data type that new column needs to be casted |

User can configure the `AddColumn` transformation in the below manner:

Example-1 : Where user wants to add a literal column

```hocon
{
  type = "AddColumn"
  column-name = "col_D"
  column-value = "val_D"
  value-type = "literal"
  column-data-type = "string"
}
```

Example-2 : Where user wants to add a derived column using SQL expression.

```hocon
{
  type = "AddColumn"
  column-name = "col_D"
  column-value = "((col_A+122)*100)"
  value-type = "expression"
  column-data-type = "string"
}
```

## DeleteColumns

It lets the user delete specific columns from the restonomer response dataframe.

This transformation needs below inputs from the user:

| Input Arguments  | Mandatory | Default Value | Description                        |
|:-----------------|:---------:|:-------------:|:-----------------------------------|
| column-names     |    Yes    |       -       | List of column names to be deleted |

User can configure the `DeleteColumns` transformation in the below manner:

```hocon
{
  type = "DeleteColumns"
  column-names = ["col_A", "col_B"]
}
```

## ExplodeColumn

It lets the user explode specific column values into multiple rows with the same column name from the restonomer 
response dataframe.

This transformation needs below inputs from the user:

| Input Arguments | Mandatory | Default Value | Description                       |
|:----------------|:---------:|:-------------:|:----------------------------------|
| column-name     |    Yes    |       -       | The name of column to be exploded |

User can configure the `ExplodeColumn` transformation in the below manner:

```hocon
{
  type = "ExplodeColumn"
  column-name = "col_A"
}
```

## CastColumns

It lets the user cast the data type of multiple columns to the desired different types at once.

This transformation expects user to provide below inputs:

| Input Arguments         | Mandatory | Default Value | Description                                               |
|:------------------------|:---------:|:-------------:|:----------------------------------------------------------|
| column-data-type-mapper |    Yes    |       -       | It defines the mapping of column to its desired data type |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3.4678
}
```

Now, suppose the requirement is to cast above columns into below data types:

```text
col_A -> string
col_B -> double
col_C -> decimal type with precision 19 and scale 2
```

Then, user can configure the `CastColumns` transformation in the below manner:

```hocon
{
  type = "CastColumns"
  column-data-type-mapper = {
    "col_A" = "string"
    "col_B" = "double"
    "col_C" = "decimal(19,2)"
  }
}
```

The transformed response will now have the columns with the desired data types:

```json
{
  "col_A": "5",
  "col_B": 4.0,
  "col_C": 3.47
}
```

## CastNestedColumn

It lets the user cast the data type of any nested or struct type column from one type to another.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                           |
|:----------------|:---------:|:-------------:|:------------------------------------------------------|
| column-name     |    Yes    |       -       | The name of the nested or struct column               |
| ddl             |    Yes    |       -       | The new Data Definition Language (DDL) for the column |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": "val_A",
  "col_B": {
     "col_C": "val_C",
     "col_D": 5
  }
}
```

Now, there is no direct way to cast the data type of `col_D` from `Long` to `String`. But it can be easily done using the 
`CastNestedColumn` transformation.

User can configure the `CastNestedColumn` transformation in the below manner:

```hocon
{
  type = "CastNestedColumn"
  column-name = "col_B"
  ddl = "col_C STRING, col_D STRING"
}
```

The transformed response will now have the data type of `col_D` as `String`:

```json
{
  "col_A": "val_A",
  "col_B": {
     "col_C": "val_C",
     "col_D": "5"
  }
}
```

## CastColumnsBasedOnPrefix

It lets the user cast the data type of multiple columns to the desired different types at once based on the prefix of the columns.

This transformation expects user to provide below inputs:

| Input Arguments    | Mandatory | Default Value | Description                                                                                                       |
|:-------------------|:---------:|:-------------:|:------------------------------------------------------------------------------------------------------------------|
| prefix-list        |    Yes    |       -       | It defines the list of prefixes based on which given columns to be selected to cast them to the desired data type |
| data-type-to-cast  |    Yes    |       -       | It defines the desired data type to which the columns have to be casted                                           |

For example, consider we have below restonomer response in json:

```json
{
  "name": "abc",
  "price_in_india": 200,
  "price_in_uk": 12,
  "percentage_difference": 3.4678
}
```

Now, suppose the requirement is to cast the columns containing "price" and "percent" prefix into decimal(19,2) data type.
Then, user can configure the `CastColumnsBasedOnPrefix` transformation in the below manner:

```hocon
{
 type = "CastColumnsBasedOnPrefix"
 prefix-list = ["price", "percent"]
 data-type-to-cast = "decimal(19,2)"
}
```

The transformed response will now have the desired columns with the desired data types:

```json
{
  "name": "abc",
  "price_in_india": 200.00,
  "price_in_uk": 12.00,
  "percentage_difference": 3.47
}
```

## CastColumnsBasedOnSuffix

It lets the user cast the data type of multiple columns to the desired different types at once based on the suffix of the columns.

This transformation expects user to provide below inputs:

| Input Arguments   | Mandatory | Default Value | Description                                                                                                       |
|:------------------|:---------:|:-------------:|:------------------------------------------------------------------------------------------------------------------|
| suffix-list       |    Yes    |       -       | It defines the list of suffixes based on which given columns to be selected to cast them to the desired data type |
| data-type-to-cast |    Yes    |       -       | It defines the desired data type to which the columns have to be casted                                           |

For example, consider we have below restonomer response in json:

```json
{
  "name": "abc",
  "india_price": 200,
  "US_price": 12,
  "percentage_difference": 3.4678
}
```

Now, suppose the requirement is to cast the columns containing "price" and "percent" suffix into decimal(19,2) data type.
Then, user can configure the `CastColumnsBasedOnSuffix` transformation in the below manner:

```hocon
{
 type = "CastColumnsBasedOnSuffix"
 suffix-list = ["price", "percent"]
 data-type-to-cast = "decimal(19,2)"
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

## CastColumnsBasedOnSubstring

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

## FlattenSchema

It lets the user flatten the schema of the restonomer response. If any of the column is of StructType or is nested, 
this transformation removes the nested structure and represent each nested attribute at a root level.

This transformation expects 0 inputs from the user.

For example, consider we have below restonomer response in json:

```json
{
  "rewardApprovedMonthPeriod": {
    "from": "2021-09",
    "to": "2021-10"
  }
}
```

Now, the agenda is to get rid of nested schema and bring the `from` and `to` columns at the root level.

User can configure the `FlattenSchema` transformation in the below manner:

```hocon
{
  type = "FlattenSchema"
}
```

The transformed response will now have the flat schema as below:

```json
{
  "rewardApprovedMonthPeriod_from": "2021-09",
  "rewardApprovedMonthPeriod_to": "2021-10"
}
```

## ConvertColumnToJson

It lets the user convert MapType or Struct type to JSON string.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                      |
|:----------------|:---------:|:-------------:|:-------------------------------------------------|
| column-name     |    Yes    |       -       | Name of the column which needs to be transformed |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": "1",
  "col_B": [
    {
      "Zipcode": 704,
      "ZipCodeType": "STANDARD"
    }
  ]
}
```

Now, suppose the requirement is to transform col_B to json string:

Then, user can configure the `ConvertColumnToJson` transformation in the below manner:

```hocon
{
  type = "ConvertColumnToJson"
  column-name = "col_B"
}
```

The transformed response will now have the columns with the desired data types:

```json
{
  "col_A": 1,
  "col_B": "[{'ZipCodeType':'STANDARD','Zipcode':704}]"
}
```

## ChangeColumnCase

It lets the user change the case of column names.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                        |
|:----------------|:---------:|:-------------:|:-----------------------------------|
| case-type       |    Yes    |       -       | Supported case types (lower,upper) |

For example, consider we have below restonomer response in json:

 ```json
 {
   "col_a": "1",
   "COL_B": "2"
 }
 ```

Now, suppose the requirement is to transform case of all columns to lowercase:

Then, user can configure the `ChangeColumnCase` transformation in the below manner:

 ```hocon
 {
   type = "ChangeColumnCase"
   case-type = "lower"
 }
 ```

The transformed response will now have the columns with the desired case type:

 ```json
 {
   "col_a": "1",
   "col_b": "2"
 }
 ```


## ReplaceStringInColumnValue

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

## RenameColumns

It lets the user rename one or multiple column(s) at once.

This transformation expects user to provide below inputs:

| Input Arguments      | Mandatory | Default Value | Description                                                    |
|:---------------------|:---------:|:-------------:|:---------------------------------------------------------------|
| rename-column-mapper |    Yes    |       -       | It defines the mapping of the existing and desired column name |

Now, suppose the requirement is to rename above columns like below:

```text
col_A -> test_col_A
col_B -> COL_b
col_C -> my_column
```

Then, user can configure the `RenameColumns` transformation in the below manner:

```hocon
{
  type = "RenameColumns"
  rename-column-mapper = {
    "col_A" = "test_col_A"
    "col_B" = "COL_b"
    "col_C" = "my_column"
  }
}
```

The transformed response will now have the columns with the desired name:

```json
{
  "test_col_A": 5,
  "COL_b": 4,
  "my_column": 3.4678
}
```

## AddPrefixToColumnNames

It lets the user add a desired prefix to select/all column names.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                            |
|:----------------|:---------:|:-------------:|:-----------------------------------------------------------------------|
| prefix          |    Yes    |       -       | It defines the desired prefix that will be added to the column name    |
| column-names    |    No     |      all      | It defines the list of column names to which the prefix will get added |


For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3
}
```

Now, suppose the requirement is to add prefix to the columns like below:

```text
col_A -> test_col_A
col_B -> test_col_B
```

Then, user can configure the `AddPrefixToColumnNames` transformation in the below manner:

```hocon
{
  type = "AddPrefixToColumnNames"
  suffix = "test"
  column-names = ["col_A", "col_B"]
}
```

The transformed response will now have the columns with the desired prefix like below.
Note that, underscore character (_) will get added automatically, separating prefix and column name part.

```json
{
  "test_col_A": 5,
  "test_col_B": 4,
  "col_C": 3
 }
```

## AddSuffixToColumnNames

It lets the user add a desired suffix to select/all column names.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                            |
|:----------------|:---------:|:-------------:|:-----------------------------------------------------------------------|
| suffix          |    Yes    |       -       | It defines the desired suffix that will be added to the column name    |
| column-names    |    no     |      all      | It defines the list of column names to which the suffix will get added |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3
}
```

Now, suppose the requirement is to add suffix to the few of the columns like below:

```text
col_A -> col_A_old
col_B -> col_B_old
```

Then, user can configure the `AddSuffixToColumnNames` transformation in the below manner:

```hocon
{
  type = "AddSuffixToColumnNames"
  suffix = "old"
  column-names = ["col_A", "col_B"]
}
```

The transformed response will now have the columns with the desired suffix like below.
Note that, underscore character ('_') will get added automatically, separating suffix and the column name.

```json
{
  "col_A_old": 5,
  "col_B_old": 4,
  "col_C": 3
}
```

## FilterRecords

It lets the user filter records from the response based on a provided filter condition.

This transformation expects user to provide below inputs:

| Input Arguments  | Mandatory | Default Value | Description                                                                    |
|:-----------------|:---------:|:-------------:|:-------------------------------------------------------------------------------|
| filter-condition |    Yes    |       -       | It is a condition expression which will be used for filtering required records |

For example, consider we have below restonomer response in json:

```json
[
  {
    "student_name": "John",
    "marks": 10
  },
  {
    "student_name": "Bob",
    "marks": 30
  }
]
```

Now, suppose the requirement is to filter student records having marks more than 20 from the data.
Then, user can configure the `FilterRecords` transformation in the below manner:

```hocon
{
  type = "FilterRecords"
  filter-condition = "marks > 20"
 }
```

The transformed response will have filtered records as desired:

```json
[
  {
    "student_name": "Bob",
    "marks": 30
  }
]
```

## SplitColumn

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