# aws-serverless-demo
This project explains how can to work with API Gateway, Lambda, S3, CloudFormation, DynamoDB using serverless framework

### The Serverless Framework

> The Serverless Framework helps you build serverless apps with radically less overhead and cost. It provides a powerful, unified experience to develop, deploy, test, secure and monitor your serverless applications.

Refer this link: https://serverless.com/


### Installation 

###### **How to install the serverless framework**

**Pre-requesties:**

* node and npm should be installed on your machine.

**Install serverless framework**

```
npm install -g serverless

```

### aws serverless demo using serverless framework

##### aws-serverless-demo architecture

![aws-serverless-architecture](aws-serverless-architecture.png)


- **Dynamodb**  : For store and retrive the records using key value pairs.
- **Lambda**    :  Run code for virtually any type of application or backend service - all with zero administration.
- **Api Gateway** : Creating, publishing, maintaining, monitoring, and securing REST APIs at any scale.
- **S3 Bucket** : S3 buckets is known to be promising, stable and highly scalable online storage solution.
- **CloudWatch** : For monitoring the logs.
- **CloudFormation**: It helps you model and setup your aws resources with less amount of time

This project has a simple application which store and retrive record from Dynamodb database.

The setup has been made through **serverless framework**

Let see how can we use serverless framework to develop this simple application.

As discussed earlier, we need to install serverless before start the application.

After that, we need to configure your AWS credentials to serverless like this

```
serverless config credentials --provider aws --key <your aws key> --secret <your aws secret key>

```

Lets start to create a new serverless framework project.

I am creating this project in java. We can also create project in nodejs, python etc.

**_Step: 1_**

**Create a folder called account-api**

```
cd account-api

serverless create --template aws-java-maven --name account-api

```
![sls_createproject.png](sls_createproject.png)

**_Step: 2_**

Open your project in your favourite IDE.

**_Step: 3_**

We need to do slight changes in pom.xml

Change from 

```
  <groupId>com.serverless</groupId>
  <artifactId>hello</artifactId>
  <packaging>jar</packaging>
  <version>dev</version>
  <name>hello</name>

```
to 

```
 <groupId>com.serverless</groupId>
  <artifactId>accounts-api</artifactId>
  <packaging>jar</packaging>
  <version>dev</version>
  <name>accounts-api</name>

```

And we are going to use Dynamodb so we need to add below dependencies

```
  <dependency>
          <groupId>com.amazonaws</groupId>
          <artifactId>aws-java-sdk-dynamodb</artifactId>
          <version>1.11.632</version>
  </dependency>

```

**_Step: 4_**

##### _serverless.yml_

The heart of serverless framework is **_serverless.yml_** file

By using this yml file, you can alot of configuration which specific aws. There are alot. But I am going to use few things in this demo.
For example, I am going to configure dynamodb table, iam role to access dynamodb from lambda, lambda function and API Gateway.

Basically once we run the application, whatever we configured serverless.yml file will be executed with the help of cloudformation. Stack will be created in cloudformation and it will be executing all steps one by one.

Lets see one by one

