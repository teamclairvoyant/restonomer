# XML

Restonomer can parse the api response of text type in XML format. User need to configure the checkpoint in below format:

```hocon
name = "checkpoint_xml_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/xml-response-converter"
  }

  data-response = {
    body = {
      type = "Text"
      text-format = {
        type = "XMLTextFormat"
        row-tag = "ROW"
      }
    }

    persistence = {
      type = "LocalFileSystem"
      file-format = {
        type = "ParquetFileFormat"
      }
      file-path = "/tmp/response_body"
    }
  }
}
```

## Compression

In case the xml text that is returned by the api is compressed, user can configure the checkpoint in below format:

```hocon
name = "checkpoint_xml_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/xml-response-converter"
  }

  data-response = {
    body = {
      type = "Text"
      compression = "GZIP"
      text-format = {
        type = "XMLTextFormat"
        row-tag = "ROW"
      }
    }

    persistence = {
      type = "LocalFileSystem"
      file-format = {
        type = "ParquetFileFormat"
      }
      file-path = "/tmp/response_body"
    }
  }
}
```

As of now, restonomer supports only `GZIP` compression format.

## XML Text Format Configurations

Just like `row-tag`, user can configure below other properties for XML text format that will help restonomer for
parsing:

| Parameter Name                |    Default Value    | Description                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|:------------------------------|:-------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| attribute-prefix              |          _          | The prefix for attributes so that we can differentiate attributes and elements.                                                                                                                                                                                                                                                                                                                                                               |
| charset                       |        UTF-8        | Defaults to 'UTF-8' but can be set to other valid charset names.                                                                                                                                                                                                                                                                                                                                                                              |
| column-name-of-corrupt-record |   _corrupt_record   | Allows renaming the new field having malformed string created by PERMISSIVE mode. This overrides spark.sql.columnNameOfCorruptRecord.                                                                                                                                                                                                                                                                                                         |
| date-format                   |     yyyy-MM-dd      | Sets the string that indicates a date format.                                                                                                                                                                                                                                                                                                                                                                                                 |
| exclude-attribute             |        false        | Whether you want to exclude attributes in elements or not.                                                                                                                                                                                                                                                                                                                                                                                    |
| ignore-surrounding-spaces     |        false        | Defines whether or not surrounding whitespaces from values being read should be skipped.                                                                                                                                                                                                                                                                                                                                                      |
| ignore-namespace              |        false        | If true, namespaces prefixes on XML elements and attributes are ignored. <br/>Note that, at the moment, namespaces cannot be ignored on the rowTag element, only its children. <br/>Note that XML parsing is in general not namespace-aware even if false.                                                                                                                                                                                    |
| infer-schema                  |        true         | Infers the input schema automatically from data.                                                                                                                                                                                                                                                                                                                                                                                              |
| mode                          |      FAILFAST       | Allows a mode for dealing with corrupt records during parsing. Allowed values are PERMISSIVE, DROPMALFORMED and FAILFAST.                                                                                                                                                                                                                                                                                                                     |
| null-value                    |        null         | The value to read as a null value.                                                                                                                                                                                                                                                                                                                                                                                                            |
| row-tag                       |         row         | The row tag of your XML files to treat as a row.                                                                                                                                                                                                                                                                                                                                                                                              |
| sampling-ratio                |         1.0         | Defines fraction of rows used for schema inferring.                                                                                                                                                                                                                                                                                                                                                                                           |
| timestamp-format              | yyyy-MM-dd HH:mm:ss | Sets the string that indicates a timestamp format.                                                                                                                                                                                                                                                                                                                                                                                            |
| value-tag                     |       _VALUE        | The tag used for the value when there are attributes in the element having no child.                                                                                                                                                                                                                                                                                                                                                          |
| wildcard-col-name             |       xs_any        | Name of a column existing in the provided schema which is interpreted as a 'wildcard'. It must have type string or array of strings. <br/>It will match any XML child element that is not otherwise matched by the schema. The XML of the child becomes the string value of the column. <br/>If an array, then all unmatched elements will be returned as an array of strings. As its name implies, it is meant to emulate XSD's xs:any type. |
