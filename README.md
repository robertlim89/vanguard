# Trade reporting engine

Persists trade information into an H2 database and allows for users to perform queries on this information.

## Requirements

- Java 21
- Gradle

## Running

- `./gradlew build` - To build the application
- `./gradlew bootRun` - To run. The trade information in the XML files will be automatically loaded on startup.
- `./gradlew test` - To run the tests.

## Endpoints

- `/v1/setup` (Deprecated) - Loads the XML into the DB. This is already done at startup so running this endpoint will cause the same information to be loaded again, creating duplicates.
- `/v1/query` - Allows for queries against the information stored in the DB. If `/v1/setup` is not run, then no information will be returned. 
Also has the following query parameters:
- `type` (Optional) - the type of query to execute
  - If absent (or null), returns all entries in the DB. Ignores custom queries.
  - If set to `basic` then the default query of 
  `((seller_party == "EMU_BANK") && (premium_currency == "AUD")) || ((seller_party == "BISON_BANK") && (premium_currency == "USD"))` will be executed. Ignores custom queries.
  - If set to `custom` then it will read a custom JSON query from the body. Example is:
  ```json
  {
      "logicalOperation": "OR",
      "queries": [{
          "property": "buyerParty",
          "value": "EMU_BANK",
          "comparison": "EQUALS",
          "queries": []
      },
      {
          "property": "premiumAmount",
          "value": 199.0,
          "comparison": "GREATER_THAN",
          "queries": []
      }
      ]
  }
  ```
    - `logicalOperation`: Either `OR` or `AND`. Joins the child queries using the respective logical operator. Only required if `queries` is non-empty.
    - `property`: The name of the property in the event to check. Valid values are `buyerParty`, `sellerParty`, `premiumCurrency` and `premiumAmount`. Not required for joining queries
    - `value`: The value to check against the event's value. For `buyerParty`, `sellerParty`, `premiumCurrency` this must be a string, and `premiumAmount` must be a number. Not required for joining queries
    - `comparison`: Either `EQUALS`, `GREATER_THAN` or `LESS_THAN`. The comparison to use. Relative operators are based on the event value vs the query's value (e.g. `event.premiumAmount` should be greater than 199.0 in the example above). For string parameters, relative operators compare lexicographically. Not required for joining queries
    - `queries` (Required): An array of child queries that will be joined using the `logicalOperation`. Set to an empty array for leaf queries.

## Assumptions/Trade-offs

- (A) Each event is assumed to have a value in all 4 properties at the correct path point.
- (T) The amount of data to be stored/retrieved can be stored in-memory is limited. Filtering is done in the application. 
This was done to allow for flexibility in the types of queries available, but might cause the application to lag if there are many events.
If there is an expectation that a number of results will be returned, then pagination could be used.
- (T) Field names are hardcoded, this simplifies query construction, but makes it less easy to change when the XML changes
- (A) The event files are available in sequence (e.g. event1, event2, event3... eventN) when loading data
- (T) To reduce development time, only "AND" and "OR" was implemented for logical operators
- (T) To reduce development time, there is no de-duplication, update or post-initialise insertion of events in the DB.
- (T) If one of the events cannot be stored in the DB, then the entire transaction is rolled back. This was done on the assumption that either
all events are stored, or none.
- (T) If one of the events cannot be retrieved or compared, then no results will be displayed and the transaction will fail.
- (T) None of the endpoints are secured, rate-limited or cached. All of these were not done due to time constraints.