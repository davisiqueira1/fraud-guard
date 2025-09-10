<h1 align="center"> 
    Fraud Guard
</h1>
<p align="center">
    Spring Boot Fraud Detection API with AWS Deployment
</p>

This project was an opportunity to learn **Spring Boot**, **AWS**, and **Terraform** by building a complete fraud detection system.

## Built with

![Spring]
![Java]
![PostgreSQL]
![AWS]
![DynamoDB]
![SQS]
![Lambda]
![Terraform]

## Prerequisites

- [Java](https://www.oracle.com/java/technologies/downloads/) (21+)
- [Maven](https://maven.apache.org/download.cgi)

## Setup environment (Local Development)

1. **Clone the repository**:

```bash
git clone https://github.com/davisiqueira1/fraud-guard.git fraud-guard
```

2. **Configure the Backend**:
   - Navigate to the backend directory
   - Update `application.properties`

```properties
spring.datasource.url=url
spring.datasource.username=username
spring.datasource.password=password
spring.security.secret=secret
spring.security.issuer=issuer
aws.access_key=access_key
aws.secret_key=secret_key
aws.region_name=us-east-1
aws.sqs_url=sqs_url
```

3. **Run the Backend**:

```bash
cd backend
mvn spring-boot:run
```

4. Access the application locally:

```
Backend API: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html
```

## AWS Deployment

Below is a summary of what was implemented in AWS:

### Architecture

- **Backend**:
  - Integrated with **SQS** for async processing
  - Fraud alerts stored in **DynamoDB**
- **Event Processing**:
  - **Lambda** consuming messages from **SQS**
- **Infrastructure**:
  - Fully provisioned with **Terraform**

**Diagram:**

```
[Client] -> [API] -> [PostgreSQL]
               |
               -> [SQS] -> [Lambda] -> [DynamoDB]
```

### Setup Infrastructure (Terraform)

1. **Go to the infra folder**

```bash
cd infra
```

2. **Create `terraform.tfvars` with your variables**

```hcl
region                 = "us-east-1"
lambda_timeout_seconds = 3
lambda_name            = "fraud-guard-lambda"
lambda_batch_size      = 10
dynamodb_name          = "fraud-guard-suspect-table"
```

3. **Initialize, plan and apply**

```bash
terraform init
terraform plan
terraform apply
```

> After `apply`, check the outputs for the SQS URL needed in the backend configuration.

### **Deployment Flow**

1. **Infrastructure**:
   - Provisioned entirely with **Terraform** (`infra/` folder)
2. **Backend**:
   - Packaged with Maven (`mvn package`)
3. **Event Processing**:
   - SQS queue triggers Lambda
   - Alerts stored in DynamoDB

[Spring]: https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[AWS]: https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazonwebservices&logoColor=white
[Java]: https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java&logoColor=white
[PostgreSQL]: https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[DynamoDB]: https://img.shields.io/badge/DynamoDB-4053D6?style=for-the-badge&logo=amazondynamodb&logoColor=white
[SQS]: https://img.shields.io/badge/AWS-SQS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white
[Lambda]: https://img.shields.io/badge/AWS-Lambda-FF9900?style=for-the-badge&logo=awslambda&logoColor=white
[Terraform]: https://img.shields.io/badge/Terraform-IaC-7B42BC?style=for-the-badge&logo=terraform&logoColor=white
