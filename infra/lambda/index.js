const { DynamoDBClient } = require("@aws-sdk/client-dynamodb");
const { DynamoDBDocumentClient, PutCommand } = require("@aws-sdk/lib-dynamodb");
require("dotenv").config();

const client = new DynamoDBClient({});
const ddb = DynamoDBDocumentClient.from(client);

const TABLE_NAME = process.env.SUSPECT_TABLE_NAME;

exports.handler = async(event) => {
    const batchItemFailures = [];

    for (const rec of event.Records ?? []) {
        try {
            const body = JSON.parse(rec.body);

            const item = {
                cpf: body.cpf,
                date: body.date,
                id: Number(body.id),
                value: Number(body.value),
                lat: Number(body.lat),
                lon: Number(body.lon)
            };

            console.log("Saving item:", item);

            await ddb.send(new PutCommand({
                TableName: TABLE_NAME,
                Item: item
            }));

        } catch (error) {
            console.error("Error", rec.messageId, error);
            batchItemFailures.push({ itemIdentifier: rec.messageId });
        }
    }

    return { batchItemFailures };
}