package de.dplatz;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlCorsOptions;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.amazon.awscdk.services.s3.ObjectOwnership;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

public class CDKStack extends Stack {

    public CDKStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
       
        Function function = createFunction("aws-lambda-playground-function", "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest", Map.of("FRONTEND_CORS_DOMAIN", "https://s3-eu-central-1.amazonaws.com"), 512, 10);
        CfnOutput.Builder.create(this, "FunctionARN").value(function.getFunctionArn()).build();

        

        var frontendBucket = Bucket.Builder.create(this, "FrontendBucket")
                .bucketName("aws-lambda-playground-frontend")
                .publicReadAccess(true)
                .removalPolicy(RemovalPolicy.DESTROY)
                .autoDeleteObjects(true)
                .blockPublicAccess(BlockPublicAccess.Builder.create()
                        .blockPublicPolicy(false)
                        .build())
                .build();

        CfnOutput.Builder.create(this, "BucketSiteUrl").value(String.format("https://%s/index.html", frontendBucket.getBucketRegionalDomainName())).build();

        var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.NONE)
                .cors(FunctionUrlCorsOptions.builder()
                        .allowedOrigins(List.of("https://" + frontendBucket.getBucketRegionalDomainName()))
                        .build())
                .build());
        CfnOutput.Builder.create(this, "FunctionUrl").value(functionUrl.getUrl()).build();


        BucketDeployment.Builder.create(this, "FrontendBucketDeployment")
                .sources(List.of(Source.asset("../../app/src/main/resources/META-INF/resources"), Source.data("env.js", String.format("const BACKEND_URL='%s';", removeTrailingSlash(functionUrl.getUrl())))))
                .destinationBucket(frontendBucket)
                .build();
    }

    Function createFunction(String functionName,String functionHandler, Map<String,String> configuration, int memory, int timeout) {
        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_21)
                .architecture(Architecture.ARM_64)
                .code(Code.fromAsset("../../app/build/function.zip"))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .tracing(Tracing.ACTIVE)
                .build();
    }

    static String removeTrailingSlash(String url) {
        if (url.lastIndexOf("/") == url.length() - 1) return url.substring(0, url.length() - 1);
        return url;
    }
}
