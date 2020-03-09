# Producer CSV Schema
| CSV Header| Farm |Location | Crop type | Year | Yield | 
| ---      | ---- | ------  | --------- | ---- | ----- |
| Data type | String | String | String | Integer | Double |
| Explanation | The name of the farm or field. | The location of the farm. | The crop grown. | The year the yield was recorded. | The yield. |
| Example Row | "Gerard Cornelis Farms" | "Ailsa Craig, ON" | "Corn" | 2020 | 2000 |
- See the sample file in `src/main/resources/producer_lite.csv` for additional clarification. There is essentially no error-checking, so ensure you adhere to the schema.

# StatsCan CSV Schema
| CSV Header| Type of crop | Year 1 | Year 2 | ... |
| --- | --- | --- | --- | --- |
| Data type | String | Integer | Integer | Integer |
| Explanation | The crop grown. | The **yield** in the particular year specified in the header. |... | ...
- See the sample file in `src/main/resources/statscan_lite.csv` for additional clarification. Again, there is essentially no error-checking, so ensure you adhere to the schema.

- Standard CSV rules apply to these files - if you have a string value that contains a comma it MUST be wrapped in double quotes.