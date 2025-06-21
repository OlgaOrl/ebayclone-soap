# eBay Clone SOAP Service

This project implements a SOAP web service for an eBay clone application, providing functionality for user management, product listings, auctions, and order processing.

## Features

- User registration and authentication
- Product catalog management
- Auction system with bidding
- Order processing
- SOAP-based API with WSDL definitions

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for containerized deployment)

## Project Structure

```
/ebay-clone-soap
├── src/
│   ├── main/
│   │   ├── java/com/ebayclone/soap/
│   │   │   ├── service/       # Service interfaces
│   │   │   ├── service/impl/  # Service implementations
│   │   │   ├── types/         # Data types
│   │   │   ├── handlers/      # SOAP handlers
│   │   │   ├── client/        # Client examples
│   │   │   └── config/        # Configuration
│   │   └── resources/
│   │       └── wsdl/          # WSDL definitions
│   └── test/                  # Unit tests
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── run.sh                     # Startup script
```

## Building and Running

### Using Maven

```bash
# Build the project
mvn clean package

# Run the server
java -cp target/ebay-clone-soap-1.0-SNAPSHOT.jar com.ebayclone.soap.SoapServer
```

### Using Docker

```bash
# Build and run with Docker
./run.sh
```

## Available Services

Once the server is running, the following SOAP services are available:

- User Service: http://localhost:8080/soap/user?wsdl
- Product Service: http://localhost:8080/soap/product?wsdl
- Auction Service: http://localhost:8080/soap/auction?wsdl
- Order Service: http://localhost:8080/soap/order?wsdl

## Testing the Services

You can use the provided client examples to test the services:

```bash
# Run the user service client
java -cp target/ebay-clone-soap-1.0-SNAPSHOT.jar com.ebayclone.soap.client.UserServiceClient

# Run the auction service client
java -cp target/ebay-clone-soap-1.0-SNAPSHOT.jar com.ebayclone.soap.client.AuctionServiceClient
```

## Authentication

The services use SOAP headers for authentication. Include an `AuthToken` header in your requests:

```xml
<soapenv:Header>
  <auth:AuthToken xmlns:auth="http://ebay.clone.soap/auth">your-token-here</auth:AuthToken>
</soapenv:Header>
```

## Development

To extend the services:

1. Define new operations in the service interfaces
2. Implement the operations in the service implementation classes
3. Add corresponding data types
4. Update the WSDL if necessary

## Running Tests

```bash
mvn test
```
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.