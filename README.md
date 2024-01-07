# aws-playground-app

This project showcases how to deploy a Quarkus-based AWS Lambda function via the AWS CDK.

## Installation

1. Install [AWS CDK CLI](https://docs.aws.amazon.com/cdk/latest/guide/getting_started.html)
2. [`cdk boostrap`](https://docs.aws.amazon.com/cdk/latest/guide/bootstrapping.html)

## Deploying the Lambda to AWS

You can deploy this example to AWS via:
```shell script
./deploy.sh all
```

> **_NOTE:_**  This only works if you have configured AWS credentials on your system.

### Other useful CDK commands

 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation