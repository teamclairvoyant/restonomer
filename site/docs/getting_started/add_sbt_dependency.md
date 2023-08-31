# Add SBT Dependency

To use Restonomer in an existing SBT project with Scala 2.12 or a later version, 
add the following dependency to your `build.sbt`

```sbt
resolvers += "Restonomer Github Repo" at "https://maven.pkg.github.com/teamclairvoyant/restonomer/"

credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "<github_user_name>",
  "<github_token>"
)

libraryDependencies += "com.clairvoyant" %% "restonomer" % "3.0.0"
```

`<github_token>` is the Personal Access Token with the permission to read packages.
