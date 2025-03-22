# guidelines

## Overview
the system built on a Java, Maven, SpringBoot, MyBatis, MySQL, and Redis backend while using HTML, TailwindCSS, and FontAwesome for the frontend. These rules are established to help ensure high code quality, maintainability, and security, while also assisting developers in following best practices.

## General Guidelines
- All code should be error-free, adhering to best practices (DRY, clear readability, modularity). 
- There should be no TODOs or placeholders. Every function, component, and module must be fully implemented and pass unit tests.
- Early returns are encouraged to enhance readability.
- Use descriptive variable/function names; event handlers must start with a ‘handle’ prefix (e.g., handleClick, handleKeyDown).
- Adhere to proper security practices. For example, when handling sensitive credentials (passwords), ensure they are processed using proper salting and AES encryption.
- Logging should incorporate context using proper log levels (INFO, WARN, ERROR) and avoid using System.out.println. Use appropriate logging frameworks.

## Naming Conventions & File Structure
- Database table and field names: lowercase, underscore separated. Tables use the prefix "tbl_".
- All primary keys should be UUID strings ensuring uniqueness across environments.
- Domain, Application, Facade, Infrastructure, and Main modules should have their own folders.
- For frontend, each prototype page should be a separate HTML file with TailwindCSS for styling and FontAwesome for icons. Use real UI images from approved sources (Unsplash, Pexels, etc.).

### Code File Organization
- Backend Java Modules: Separate packages for app, domain, facade, infrastructure, and main. For example:
  * app: Application services
  * domain: Core domain models and business logic
  * facade: Exposing APIs for external interactions
  * infrastructure: Data access and integration modules
  * main: Application bootstrap

- Frontend Files: All HTML pages in a dedicated directory. Use descriptive names (e.g., DataSourceSelectionPage.html, MetadataExtractionPage.html).

## Styling Guidelines for Frontend
- Use TailwindCSS classes for styling. Do not use inline CSS or external CSS frameworks.
- Always include accessibility attributes such as tabindex="0", aria-label, and implement on:keydown handlers as necessary.
- When applying class conditionals, use the "class:" syntax instead of ternary operators for class names.

## Component and Code Specific Guidelines
- **JavaScript/TypeScript:**
  * Use const whenever possible. 
  * Break down functions into atomic, testable units.
  * Include all necessary imports; ensure modules are self-contained.
  * Apply proper error handling, using try-catch blocks with proper logging.

- **React/NextJS Components:**
  * Define clear PropTypes or TypeScript types for components.
  * Use early returns in functional components.
  * Ensure components are accessible (include aria-labels, keyboard handlers).
  * Write modular, reusable components. Avoid duplicated code.

## Error Handling and Logging
- Use a logging framework (such as SLF4J with Logback) rather than System.out.println.
- Log exceptions with detailed context and stack trace.
- Implement retry logic for critical operations (like API calls to LLM) with exponential backoff.

## Testing and Documentation
- Write unit tests for each critical function. Ensure every module has proper test coverage.
- Integration tests must simulate all critical user flows.
- Ensure documentation (especially for APIs) is comprehensive and maintained in a markdown file within the /docs directory.

## Testing Strategy
- **Unit Tests:** Use JUnit and Mockito for Java modules; coverage for each service function, especially encryption, metadata extraction, and CSV generation.
- **Integration Tests:** Verify end-to-end flows, including datasource configuration to metadata extraction, API versioning endpoints, and LowCodeConfig retrieval.
- **End-to-End Tests:** Utilize Selenium or Cypress to simulate user journeys (metadata manager, data query analyst, and low-code integration developer flows).
- **Performance Tests:** Ensure critical API endpoints respond within acceptable thresholds (< 3 seconds for page loads and < 30 seconds for query execution) and CSV file generation does not exceed limits.
- **Security Tests:** Include tests for encryption/decryption, password hashing, and injection prevention.

## Security Practices
- Store sensitive information (e.g., data source credentials) securely after salting and AES encryption.
- Do not expose decrypted credentials on the UI.
- Ensure endpoints have proper access control (even though this module is handled in a separate system, implement safety checks if applicable).

## Performance Considerations
- Favor readability and maintainability; be mindful of redundant processing.
- Optimize data queries using proper indexing strategies.
- Use caching (Redis) where applicable, with well-defined invalidation rules.
- Lazy-load large modules if necessary.

## Final Code Review Checklist
- All modules are well-tested (unit, integration, E2E).
- Code formatting adheres to a consistent style (indentation, spacing).
- Comments and documentation are clear and maintainable.
- All files follow the proper folder structure as indicated by the project’s domain-driven design (DDD) architecture.

## Infrastructure and CI/CD Requirements
- **Environment Variables:** All sensitive credentials and environment values should be placed in environment-specific .env files with placeholders (e.g., DB_PASSWORD=changeme).
- **Build Pipeline:** Set up a Maven build pipeline for Java modules and a separate script for frontend builds. Enforce linting and testing stages.
- **Database Migrations:** Use versioned migration tools (Flyway or Liquibase) for database changes. All database migrations should be scripted and versioned.
- **Deployment:** Create stages for dev, staging, and production. Implement rollback procedures in the pipeline. Dockerize services and deploy on separate environments. Ensure rollback strategies are defined in case of deployment failures.

## Documentation Standards
- All API endpoints must be documented in a central OpenAPI/Swagger document.
- Each module (frontend and backend) should include README files outlining setup, configuration, and running tests.
- Maintain a /docs directory for user stories, configuration guidelines, and developer notes.

## Data Schema and Caching
- **Entity Models:** Define domain models for Data Source, Metadata, Query Conditions, and LowCodeConfig. Ensure fields are defined with types (e.g., String for ID, Date for timestamps) and maintain relationships (one-to-many relationships for data sources and tables).
- **Indexing:** Use unique indexes for key fields (u_idx prefix) and additional indexes where necessary (idx prefix) to optimize search and retrieval.
- **Caching:** Implement caching (using Redis) for frequently accessed metadata and configuration data. Set expiration times and invalidation rules.

## Testing Requirements
- **Unit Tests:** Every core function should be unit tested using JUnit for Java and Jest for JavaScript/TypeScript.
- **Integration Tests:** Simulate end-to-end flows for data source configuration, metadata extraction, and query execution.
- **End-to-End Tests:** Use tools like Selenium or Cypress to simulate user journeys (e.g., metadata manager, data query analyst, and low-code integration developer flows).
- **Performance Benchmarks:** Ensure key API endpoints (like metadata extraction and data query execution) respond within defined thresholds (e.g., < 3s for page loads, < 30s for SQL queries).
- **Security Tests:** Include tests for encryption, password handling, and vulnerability scans (e.g., SQL injection prevention).

## Frontend Component Architecture
- **Component Hierarchy:** Define a clear hierarchy: layout components (pages) at the top, container components, and presentational components.
- **State Management:** Use React hooks and context to manage state. Components should pass down data via props and context.
- **Responsive Design:** Use TailwindCSS responsive breakpoints to adapt UI to different screen sizes.
- **Accessibility:** Ensure components include ARIA roles and appropriate keyboard navigation handlers.
- **Form Validation:** Implement validation rules on input components; show error states using Tailwind error classes.

## API Contracts and Communication
- **API Specifications:** Document each backend endpoint with request/response schemas (preferably in OpenAPI format). Specify parameter types, versioning fields, and error response formats.
- **Inter-Module Communication:** Modules (app, domain, facade, infrastructure) should communicate via clearly defined interfaces. Use MapStruct or similar libraries for DTO mapping in Java.
- **Real-Time Communication:** For metadata extraction status and query progress, consider long polling or WebSocket integration (if required). Otherwise, implement periodic polling endpoints.

## Error Handling Protocol
- **Error Types:** Categorize errors into validation errors, system errors, and external API errors.
- **Logging:** Use a consistent logging format with timestamps, module name, error code, and stack trace. For Java, use SLF4J with Logback.
- **Retry Policies:** Implement retry with exponential backoff for network calls (especially for LLM API integrations).
- **Standardized Responses:** All API errors should follow a standardized JSON response format: { "errorCode": "XYZ", "message": "Detailed error message", "details": {} }.
- Standardize all API error responses in JSON with code, message, and details. 
- Use SLF4J with Logback in the backend. Log errors with context (timestamp, module, error code).
- Implement retry logic with exponential backoff for external API calls (e.g., LLM API).

## Performance Optimization
- **Caching:** Utilize Redis for caching frequently accessed data like metadata and LowCode configuration.
- **Lazy Loading:** Implement lazy loading in the frontend for non-critical components to reduce initial page load times.
- **Query Optimization:** Use indexed fields and optimized SQL queries for backend operations.
- **Asset Optimization:** Use code splitting and bundling strategies. Compress assets using gzip or Brotli.
- Identify potential bottlenecks (e.g., heavy metadata extraction, complex SQL queries) and apply caching (via Redis) where feasible.
- Implement lazy loading for non-critical frontend components and use code splitting.
- Optimize SQL queries with indexing strategies as per the database design guidelines.


## CI/CD Pipeline
- **Build Pipeline:** Integrate Maven for Java builds with stages for compilation, testing, and packaging. Use ESLint and Prettier for frontend.
- **Validation Gates:** Set up automated tests (unit, integration, E2E) to run on each commit. Fail builds on test failures.
- **Deployment:** Use Docker containers for local testing and deployment. Each environment (dev, staging, production) must have its own configuration file.
- **Rollback Procedures:** Implement automated rollback if new deployments fail health checks.

---

This plan translates user stories into concrete development tasks with clear technical specifications, ensuring that this project is developed according to best practices in code quality, security, and performance.
