resource "aws_sqs_queue" "sqs_queue" {
  name                       = "fraud-guard-sqs"
  visibility_timeout_seconds = ceil((var.lambda_timeout_seconds * var.lambda_batch_size) * 1.25)
}
