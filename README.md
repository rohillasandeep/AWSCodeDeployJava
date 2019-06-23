# AWS CodeDeploy Java Application
Command Line Java Project : Automate deployments through AWS CodeDeploy

This project aims at automating deployments through pipeline executions [such as Jenkins] on systems where, either AWS CLI is not available or cannot be deployed.

I have prepared a Java Command Line wrapper which uses AWS Java SDK to upload the local build (.zip format) to S3 bucket and instructs AWS CodeDeploy to pick up recently uploaded build from S3 and deploy to designated stack of servers. 

Once a runnable jar is prepared it refers to 2 configuration files:
1. config-linux.properties: This file details all the configurations required by the Application to deploy the build. Sample file is uploaded to the repository. Please modify accordingly.
2. credentials.txt: This files maintains the AWS API Keys required for push to S3 and execute deployments through AWS Code Deploy.

Assumptions:
1. AWS Code Deploy Application and Deployments Groups are already setup in AWS
2. All relevant Roles and Policies created in AWS and applied to appropriate resources and users.
3. Pipelines are properly setup to prepare the buid as per Code Deploy requirements
  https://docs.aws.amazon.com/codedeploy/latest/userguide/reference-appspec-file.html#appspec-reference-server
  https://docs.aws.amazon.com/codedeploy/latest/userguide/deployment-steps.html#deployment-steps-server
  https://docs.aws.amazon.com/codedeploy/latest/userguide/deployments-create-prerequisites.html

Hope this helps!

-Sandeep
