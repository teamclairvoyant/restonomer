# ChangeColumnCase

It lets the user change the case of the column names.

This transformation expects user to provide below inputs:

| Input Arguments  | Mandatory | Default Value | Description                                                 |
|:-----------------|:---------:|:-------------:|:------------------------------------------------------------|
| target-case-type |    Yes    |       -       | Supported case types (lower,upper,snake,kebab,camel,pascal) |
| source-case-type |    No     |     lower     | Supported case types (snake,kebab,camel,pascal)             |

Below is a quick helper chart to understand commonly used case types and their general usage

| Input Arguments |     Example      | Usage                                                                 |
|:----------------|:----------------:|:----------------------------------------------------------------------|
| Camel           |  myVariableName  | Used for variables, methods, and parameter names                      |
| Pascal          |   MyClassName    | Used for class names and type names                                   |
| Snake           | my_variable_name | Used for variable names in some scripting languages                   |
| Kebab           | my-variable-name | Used for configurations, file names, URLs, and command-line arguments |

For example, consider we have below restonomer response in json:

 ```json
 {
   "col_a": "1",
   "COL_B": "2"
 }
 ```

Now, as we know all these columns are in snake-case, suppose the requirement is to transform case of all columns to camel case.

Then, user can configure the `ChangeColumnCase` transformation in the below manner:
 ```hocon
 {
   type = "ChangeColumnCase"
   target-case-type = "camel"
   source-case-type = "snake"
 }
 ```
 
 The transformed response will now have the columns with the desired case type:
 
 ```json
 {
   "colA": "1",
   "colB": "2"
 }
 ```

Remember that, for converting the case to "lower" or "upper" user need not give source-case-type as it will not matter for the conversion.
However, for any other target case type user needs to provide an appropriate source-case-type. Otherwise, Restonomer will error out.
