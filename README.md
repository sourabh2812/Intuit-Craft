# ServiceScheduler Application

## Introduction
ServiceScheduler is a Spring Boot application designed to manage customer service scheduling in a service center environment. It's inspired by systems like the Apple Genius Bar or Xfinity store services, offering efficient and fair handling of service requests with support for different customer types, including VIP and Regular customers.

## System Design
The application consists of several components working together to implement the service scheduling functionality:

- **Model**: Represents the data structure, including `Customer` and `CustomerType`.
- **Service**: Contains the business logic in the `ServiceScheduler` class.
- **Controller**: Handles HTTP requests in `ServiceSchedulerController`.
- **Exception Handling**: Custom exceptions and global exception handling.
- **Swagger Documentation**: Integrated API documentation using SpringDoc OpenAPI.

### Customer Flow
1. A customer checks in and is given a ticket with a sequential service number.
2. The service number is called by the staff in the order determined by the scheduler.
3. VIP customers are given higher priority over regular customers.

### API Endpoints
- `POST /serviceScheduler/checkIn`: Check in a new customer.
- `GET /serviceScheduler/nextCustomer`: Get the next customer to be served.
- `GET /serviceScheduler/findCustomer/{phoneNumber}`: Find a customer by phone number.

## Technologies Used
- Spring Boot
- Spring MVC
- Swagger/OpenAPI for documentation

## Getting Started

### Prerequisites
- Java JDK 11 or later
- Maven or Gradle (based on your project setup)

### Running the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/sourabh2812/Intuit-Craft.git