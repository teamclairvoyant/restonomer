# Redshift

User can use Redshift persistence to write/persist spark dataframe to aws redshift table.

User can configure the Redshift persistence in the below manner:

```hocon
persistence = {
  type = "Redshift"
  host-name = "my-hostname"
  port = 5439
  database-name = "dev"
  table-name = "my_redshift_table"
  user-name = "test_user"
  password = "test_password"
  temp-dir-path = "s3a://my-tmp-redshift-bucket/redshift-tmp-dir/"
  writer-options = {
    temp-dir-region = "ca-central-1"
    iam-role-arn = "arn:aws:iam::283220348991:role/service-role/AmazonRedshift-CommandsAccessRole-20231115T135908"
  }
}
```

The `BigQuery` persistence needs below arguments from the user:

| Argument Name  | Mandatory |                               Default Value                               | Description                                                     |
|:---------------|:---------:|:-------------------------------------------------------------------------:|:----------------------------------------------------------------|
| host-name      |    Yes    |                                     -                                     | Redshift host name.                                             |
| port           |    No     |                                   5439                                    | Redshift port.                                                  |
| database-name  |    No     |                                    dev                                    | Redshift database name.                                         |
| table-name     |    Yes    |                                     -                                     | Redshift table name.                                            |
| user-name      |    Yes    |                                     -                                     | Redshift user name.                                             |
| password       |    Yes    |                                     -                                     | Redshift password.                                              |
| temp-dir-path  |    Yes    |                                     -                                     | S3 path to store temporary files.                               |
| writer-options |    No     | Default instance of <br/>`RedshiftWriterOptions` <br/>with default values | Redshift writer options represented by `RedshiftWriterOptions`. |
| save-mode      |    No     |                                 overwrite                                 | Save mode to use while writing to redshift table.               |

User can pass below options to the `RedshiftWriterOptions` instance:

<table>
  <tr>
    <th>Parameter Name</th>
    <th>Mandatory</th>
    <th>Default value</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>temp-dir-region</td>
    <td>No</td>
    <td>-</td>
    <td>
      <p>AWS region where tempdir is located. Setting this option will improve connector performance for interactions with tempdir as well as automatically supply this value as part of COPY and UNLOAD operations during connector writes and reads. If the region is not specified, the connector will attempt to use the <a href="https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html">Default Credential Provider Chain</a> for resolving where the tempdir region is located. In some cases, such as when the connector is being used outside of an AWS environment, this resolution will fail. Therefore, this setting is highly recommended in the following situations:</p>
      <ol>
        <li>When the connector is running outside of AWS as automatic region discovery will fail and negatively affect connector performance.</li>
        <li>When tempdir is in a different region than the Redshift cluster as using this setting alleviates the need to supply the region manually using the extracopyoptions and extraunloadoptions parameters.</li>
        <li>When the connector is running in a different region than tempdir as it improves the connector's access performance of tempdir.</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td>iam-role-arn</td>
    <td>Only if using IAM roles to authorize Redshift COPY/UNLOAD operations</td>
    <td>-</td>
    <td>Fully specified ARN of the <a href="http://docs.aws.amazon.com/redshift/latest/mgmt/copy-unload-iam-role.html">IAM Role</a> attached to the Redshift cluster, ex: arn:aws:iam::123456789000:role/redshift_iam_role</td>
  </tr>
  <tr>
    <td>forward-spark-s3-credentials</td>
    <td>No</td>
    <td>false</td>
    <td>
      If true then this library will automatically discover the credentials that Spark is using to connect to S3 and will forward those credentials to Redshift over JDBC. These credentials are sent as part of the JDBC query, so it is strongly recommended to enable SSL encryption of the JDBC connection when using this option.
    </td>
  </tr>
  <tr>
    <td>jdbc-driver</td>
    <td>No</td>
    <td>Determined by the JDBC URL's subprotocol</td>
    <td>The class name of the JDBC driver to use. This class must be on the classpath. In most cases, it should not be necessary to specify this option, as the appropriate driver classname should automatically be determined by the JDBC URL's subprotocol.</td>
  </tr>
  <tr>
    <td>dist-style</td>
    <td>No</td>
    <td>EVEN</td>
    <td>The Redshift <a href="http://docs.aws.amazon.com/redshift/latest/dg/c_choosing_dist_sort.html">Distribution Style</a> to be used when creating a table. Can be one of EVEN, KEY or ALL (see Redshift docs). When using KEY, you must also set a distribution key with the distkey option.</td>
  </tr>
  <tr>
    <td>dist-key</td>
    <td>No, unless using DISTSTYLE KEY</td>
    <td>-</td>
    <td>The name of a column in the table to use as the distribution key when creating a table.</td>
  </tr>
  <tr>
    <td>sort-key-spec</td>
    <td>No</td>
    <td>-</td>
    <td>
      <p>A full Redshift <a href="http://docs.aws.amazon.com/redshift/latest/dg/t_Sorting_data.html">Sort Key</a> definition.</p>
      <p>Examples include:</p>
      <ul>
        <li>SORTKEY(my_sort_column)</li>
        <li>COMPOUND SORTKEY(sort_col_1, sort_col_2)</li>
        <li>INTERLEAVED SORTKEY(sort_col_1, sort_col_2)</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>include-column-list</td>
    <td>No</td>
    <td>false</td>
    <td>
      If true then this library will automatically extract the columns from the schema and add them to the COPY command according to the <a href="http://docs.aws.amazon.com/redshift/latest/dg/copy-parameters-column-mapping.html">Column List docs</a>. (e.g. `COPY "PUBLIC"."tablename" ("column1" [,"column2", ...])`).
    </td>
  </tr>
  <tr>
    <td>description</td>
    <td>No</td>
    <td>-</td>
    <td>
      <p>A description for the table. Will be set using the SQL COMMENT command, and should show up in most query tools. See also the description metadata to set descriptions on individual columns.</p>
    </td>
  </tr>
  <tr>
    <td>pre-actions</td>
    <td>No</td>
    <td>-</td>
    <td>
      <p>This can be a list of SQL commands to be executed before loading COPY command. It may be useful to have some DELETE commands or similar run here before loading new data. If the command contains %s, the table name will be formatted in before execution (in case you're using a staging table).</p>
      <p>Be warned that if this commands fail, it is treated as an error, and you'll get an exception. If using a staging table, the changes will be reverted and the backup table restored if pre actions fail.</p>
    </td>
  </tr>
  <tr>
    <td>post-actions</td>
    <td>No</td>
    <td>No default</td>
    <td>
      <p>This can be a list of SQL commands to be executed after a successful COPY when loading data. It may be useful to have some GRANT commands or similar run here when loading new data. If the command contains %s, the table name will be formatted in before execution (in case you're using a staging table).</p>
      <p>Be warned that if this commands fail, it is treated as an error and you'll get an exception. If using a staging table, the changes will be reverted and the backup table restored if post actions fail.</p>
    </td>
  </tr>
  <tr>
    <td>extra-copy-options</td>
    <td>No</td>
    <td>No default</td>
    <td>
      <p>A list extra options to append to the Redshift COPY command when loading data, e.g. TRUNCATECOLUMNS or MAXERROR n (see the <a href="http://docs.aws.amazon.com/redshift/latest/dg/r_COPY.html#r_COPY-syntax-overview-optional-parameters">Redshift docs</a> for other options).</p>
      <p>Note that since these options are appended to the end of the COPY command, only options that make sense at the end of the command can be used, but that should cover most possible use cases.</p>
    </td>
  </tr>
  <tr>
    <td>temp-format</td>
    <td>No</td>
    <td>AVRO</td>
    <td>
      <p>The format in which to save temporary files in S3 when writing to Redshift. Defaults to "AVRO"; the other allowed values are "CSV", "CSV GZIP", and "PARQUET" for CSV, gzipped CSV, and parquet, respectively.</p>
      <p>Redshift is significantly faster when loading CSV than when loading Avro files, so using that tempformat may provide a large performance boost when writing to Redshift.</p>
      <p>Parquet should not be used as the tempformat when using an S3 bucket (tempdir) in a region that is different from the region where the redshift cluster you are writing to resides. This is because cross-region copies are not supported in redshift when using parquet as the format.</p>
    </td>
  </tr>
  <tr>
    <td>csv-null-string</td>
    <td>No</td>
    <td>@NULL@</td>
    <td>
      <p>The String value to write for nulls when using the CSV tempformat. This should be a value which does not appear in your actual data.</p>
    </td>
  </tr>
  <tr>
    <td>auto-push-down</td>
    <td>No</td>
    <td>True</td>
    <td>
      <p>Apply predicate and query pushdown by capturing and analyzing the Spark logical plans for SQL operations. The operations are translated into a SQL query and then executed in Redshift to improve performance.</p>
      <p>Once autopushdown is enabled, it is enabled for all the Redshift tables in the same Spark session.</p>
    </td>
  </tr>
  <tr>
    <td>auto-push-down-s3-result-cache</td>
    <td>No</td>
    <td>False</td>
    <td>Cache the query SQL to unload data S3 path mapping in memory so that the same query don't need to execute again in the same Spark session.</td>
  </tr>
  <tr>
    <td>copy-retry-count</td>
    <td>No</td>
    <td>2</td>
    <td>Number of times to retry a copy operation including dropping and creating any required table before failing.</td>
  </tr>
  <tr>
    <td>jdbc-options</td>
    <td>No</td>
    <td>-</td>
    <td>
      Additional map of parameters to pass to the underlying JDBC driver where the wildcard is the name of the JDBC parameter (e.g., jdbc.ssl). Note that the jdbc prefix will be stripped off before passing to the JDBC driver.
      A complete list of possible options for the Redshift JDBC driver may be seen in the <a href="https://docs.aws.amazon.com/redshift/latest/mgmt/jdbc20-configuration-options.html">Redshift docs</a>.
    </td>
  </tr>
</table>

Now, in order to make this work, user need to first authenticate against AWS account.
User need to set below two environment variables in their execution environment:

*   `AWS_ACCESS_KEY` or `AWS_ACCESS_KEY_ID`
*   `AWS_SECRET_KEY` or `AWS_SECRET_ACCESS_KEY`

Users should know beforehand the values of above credentials for their AWS account.