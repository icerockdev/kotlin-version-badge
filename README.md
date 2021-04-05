# kotlin-version-badge

`kotless` lambda for AWS Lambda for getting badge like ![kotlin-version](https://img.shields.io/badge/kotlin-1.4.31-orange) but with automatically fetch version from maven package by group and name.

# Usage
`https://kotlin-version.aws.icerock.dev/kotlin-version?group=<your groupId>&name=<your artifactId>`

for example:

`https://kotlin-version.aws.icerock.dev/kotlin-version?group=dev.icerock.moko&name=mvvm`

# Used resources
- https://github.com/JetBrains/kotless
- https://hadihariri.com/2020/05/12/from-zero-to-lamda-with-kotless/ - very helpful guide
- https://site.kotless.io/pages/introduction
- https://medium.datadriveninvestor.com/configurate-route-53-and-adding-ssl-certificate-145d8a317d91 - helpful for Route53 setup process
