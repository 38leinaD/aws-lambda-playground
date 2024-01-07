package de.dplatz;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

public class CDKApp {
    
    public static void main(final String[] args) {
            var app = new App();
            var appName = "aws-lambda-playground";
            Tags.of(app).add("project", "dplatz.de");
            Tags.of(app).add("environment", "dev");
            Tags.of(app).add("application", appName);

            var stackProps = StackProps.builder()
                    .env(Environment.builder()
                            .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                            .region(System.getenv("CDK_DEFAULT_REGION"))
                            .build())
                    .build();
        new CDKStack(app, appName, stackProps);
        app.synth();
    }
}
