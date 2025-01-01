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
- Docker for containerized setup

## Installation

Clone the image:

```bash
docker pull lukanemsitsveridze/ocms:1
```

## Running the Application

Start the application container:

```bash
docker run -d --name ocms -p 8080:8080 lukanemsitsveridze/ocms:1
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
