# Online Course Management System (OCMS)

The Online Course Management System (OCMS) is a Spring Boot application designed to manage online courses, providing secure user authentication, course management, review features, and role-based access control to support a robust online learning platform.

## Table of Contents

- [Getting Started](#getting-started)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Contributing](#contributing)

For comprehensive information on features, API endpoints, user roles and permissions, database setup, technologies, and other details, please refer to the documentation in the `./Documents` directory.

## Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.8 or later
- (Optional) Docker for containerized setup

## Installation

Clone the repository:

```bash
git clone https://github.com/yourusername/ocms.git
cd ocms
```

## Running the Application

Start the application using:

```bash
mvn spring-boot:run
```

The application will be accessible at http://localhost:8080.

## Testing

This project uses JUnit and Mockito for unit and integration testing. To run the test suite:

```bash
mvn test
```

## Contributing

Contributions are welcome! Follow these steps to contribute:

**Fork the repository.**

**Create a feature branch:**

```bash
git checkout -b feature/YourFeature
```

**Commit your changes:**

```bash
git commit -am 'Add new feature'
```

**Push to the branch:**

```bash
git push origin feature/YourFeature
```

**Open a pull request**
