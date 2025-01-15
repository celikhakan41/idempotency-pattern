# Idempotency Pattern Example

This project demonstrates how to implement the Idempotency Pattern in a Spring Boot application, which helps in preventing duplicate requests when the same operation is triggered multiple times with the same request identifier (Idempotency Key). It simulates a payment processing system where a client can make a payment request, and the server ensures that repeated requests with the same Idempotency Key are handled only once.

## Features

- **Idempotency Pattern**: Prevents duplicate payment requests based on an Idempotency Key.
- **In-memory Cache**: Uses a `ConcurrentHashMap` to store transaction details, avoiding the use of a database.
- **Spring Boot**: Leverages Spring Boot for building RESTful APIs.
- **Postman Tests**: Includes instructions for testing the endpoints with Postman.
- **JUnit Tests**: Test coverage for the payment processing functionality and idempotency logic.

## Technologies Used

- **Spring Boot**: For building the backend and RESTful API.
- **Mockito**: For mocking service dependencies in unit tests.
- **JUnit**: For testing the controllers and services.
- **Postman**: For manual testing of the API endpoints.

## Getting Started

To get a local copy up and running, follow these steps.

### Prerequisites

Make sure you have the following tools installed:

- Java 17 or higher
- Maven (for building the project)
- Postman (for API testing)

### Installing

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/idempotency-pattern.git
    ```

2. Navigate to the project directory:

    ```bash
    cd idempotency-pattern
    ```

3. Build the project with Maven:

    ```bash
    mvn clean install
    ```

4. Run the application:

    ```bash
    mvn spring-boot:run
    ```

The application will start on `http://localhost:8080`.

## API Endpoints

### POST /payment

**Description**: Processes a payment request with an Idempotency Key to ensure the same payment request is not processed more than once.

- **Request Header**: `Idempotency-Key` (string)
- **Request Body**: JSON containing payment details (amount, currency, card number)

#### Example Request:

```http
POST http://localhost:8080/payment
```
#### Headers:
```
Idempotency-Key: 123456
```

#### Body (raw JSON):
```JSON
{
  "amount": 100.0,
  "currency": "USD",
  "cardNumber": "4111111111111111"
}
```

#### Example Response:

- **200 OK:** Payment processed successfully
- **409 Conflict:** Duplicate request, Payment already processed with the given Idempotency Key


### Running Tests:
1. To run the tests, execute the following command:

```bash
mvn test
```
2. Test Coverage: The tests cover the functionality of the PaymentController and IdempotencyService, ensuring that the idempotency logic works as expected.


#### Example Test Results
- **Test Successful Payment Processing:** Verifies that a payment request is successfully processed when the Idempotency Key is unique.

- **Test Duplicate Payment Request:** Verifies that a payment request is rejected if the same Idempotency Key is sent again.

### Mocking with Mockito
The service layer is mocked using Mockito in unit tests to isolate the logic and test the behavior of the controller and the `IdempotencyService`.

### Conclusion
This project demonstrates how to implement and test the Idempotency Pattern using Spring Boot. It ensures that duplicate payment requests with the same Idempotency Key are processed only once, which is crucial for ensuring that operations like payments are not unintentionally duplicated.
