# AWS Signature Authentication

Restonomer supports AWS Signature Version-4 which authenticates a request based on a signature formulated from the access keys (access key ID, secret access key)

In order to use AWS Signature authentication in checkpoint, user need to provide below parameters:

*   AWS Access Key
*   AWS Secret Key

User can configure AWS Signature authentication in checkpoint file in below manner:

```hocon
authentication = {
  type = "AwsSignatureAuthentication"
  access-key = "test_access_key"
  secret-key = "test_secret_key"
}
```
