resource "aws_dynamodb_table" "suspect_transaction_table" {
  name         = var.dynamodb_name
  billing_mode = "PAY_PER_REQUEST"

  hash_key  = "cpf"
  range_key = "date"

  attribute {
    name = "cpf"
    type = "S"
  }

  attribute {
    name = "date"
    type = "S"
  }

  attribute {
    name = "id"
    type = "N"
  }

  global_secondary_index {
    name            = "IdIndex"
    hash_key        = "id"
    projection_type = "ALL"
  }
}
