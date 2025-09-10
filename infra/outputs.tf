output "sqs_queue_url" {
  description = "The URL of the Fraud Guard SQS Queue"
  value       = aws_sqs_queue.sqs_queue.url
}
