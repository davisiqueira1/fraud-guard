variable "region" {
  default = "Defines the AWS region to create resources in."
  type    = string
}

variable "lambda_timeout_seconds" {
  description = "Defines the maximum time a lambda function can spend processing one message."
  type        = number
  default     = 3
}

variable "lambda_name" {
  description = "Defines the lambda function name."
  type        = string
}

variable "lambda_batch_size" {
  description = "Defines the lambda batch size when polling from SQS queue."
  type        = number
  default     = 10
}

variable "dynamodb_name" {
  description = "Defines the dynamodb table name."
  type        = string
}
