data "archive_file" "lambda_handler" {
  type        = "zip"
  source_file = "${path.module}/lambda/index.js"
  output_path = "${path.module}/build/lambda_handler.zip"
}

resource "aws_lambda_event_source_mapping" "lambda_sqs_trigger" {
  function_name           = aws_lambda_function.lambda.function_name
  event_source_arn        = aws_sqs_queue.sqs_queue.arn
  function_response_types = ["ReportBatchItemFailures"]

  batch_size = var.lambda_batch_size

  scaling_config {
    maximum_concurrency = 100
  }
}

resource "aws_lambda_function" "lambda" {
  function_name = var.lambda_name
  role          = aws_iam_role.lambda_role.arn

  filename         = data.archive_file.lambda_handler.output_path
  source_code_hash = data.archive_file.lambda_handler.output_base64sha256

  handler = "index.handler"
  runtime = "nodejs22.x"

  timeout = var.lambda_timeout_seconds
}
