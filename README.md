# aws-playground-app

This project showcases how to deploy a Quarkus-based application in a cost-efficient manner to AWS Lambda and S3 via the AWS CDK.
The application is a "standard" application containing a Web-based frontend and a Quarkus-based backend that exposes a Restful API for the frontend.
The front is deployed to an S3 bucket whereas the backend is deployed as AWS lambda thanks to the great/abstraction provided in Quarkus.
The backend is developed as a standard JAX-RS endpoint that can be tested locally and then run on AWS lambda without any AWS lambda specific code.

The idea for this approach is to host applications which are only used infrequently e.g. for personal projects and thus should be as cost-efficient as possible.

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