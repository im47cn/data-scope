# Data Scope

Data Scope is an open-source data quality platform designed to help organizations monitor, validate, and improve their data quality across various data sources.

## Overview

Data Scope provides a comprehensive solution for data quality management with features including:

- Data quality monitoring and validation
- Customizable data quality rules
- Support for multiple data sources
- Anomaly detection and alerting
- User-friendly dashboard for data quality visualization
- Natural language query interface
- Low-code rule configuration

## Architecture

Data Scope follows a modular architecture with the following key components:

- **Web UI**: User interface for configuring rules, viewing results, and managing data quality
- **API Layer**: RESTful APIs for interacting with the platform
- **Core Engine**: Handles rule execution, validation, and data processing
- **Data Source Connectors**: Interfaces with various data sources
- **Storage Layer**: Manages metadata and validation results

For more details, see [Architecture Documentation](docs/architecture.md).

## Getting Started

### Prerequisites

- Java 8 or higher
- MySQL 5.7 or higher
- Redis (for caching)
- Maven 3.6 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/im47cn/data-scope.git
   cd Data Scope
   ```

2. Configure the database:
    - Create a database named `datainsight`
    - Update database configuration in `src/main/resources/application.yml`

3. Build the project:
   ```bash
   mvn clean package
   ```

4. Run the application:
   ```bash
   java -jar target/datainsight.jar
   ```

5. Access the web interface at `http://localhost:8080`

## Features

### Data Quality Rules

Data Scope supports various types of data quality rules:

- Completeness checks
- Uniqueness validation
- Referential integrity
- Pattern matching
- Statistical validations
- Custom SQL-based rules

### Data Source Support

- MySQL
- PostgreSQL
- Oracle
- SQL Server
- Hive
- Spark
- More connectors coming soon

### Alerting and Notifications

Configure alerts based on rule violations with notification channels:

- Email
- Slack
- Webhook
- Custom integrations

## Project Roadmap

Based on our sprint planning documents:

- **Sprint 1**: Core platform setup, basic UI, and initial data source connectors
- **Sprint 2**: Enhanced rule engine, additional data sources, and improved dashboard
- **Sprint 3**: Advanced analytics, natural language query interface, and API enhancements

See [Project Plan](docs/project-plan.md) for more details.

## Contributing

We welcome contributions to Data Scope! Please see our [Contributing Guidelines](CONTRIBUTING.md) for more information.

## License

This project is licensed under the [Apache License 2.0](LICENSE).

## Documentation

For more detailed documentation, please refer to:

- [Architecture Design](docs/architecture.md)
- [Project Specification](docs/specification.md)
- [Architecture Decision Records](docs/ADR.md)
- [API Documentation](http://localhost:8080/swagger-ui.html) (when running locally)

## Contact

For questions or support, please open an issue on GitHub or contact the project maintainers.