variable "region" {
  default = "Defines the AWS region to create resources in."
  type    = string
}

variable "lambda_timeout_seconds" {
  description = "Defines the maximum time that a lambda will be executed."
  type        = number
  default     = 3
}
