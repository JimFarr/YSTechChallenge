# Architecture Overview

- Layered Architecture
- API Documentation using Swagger
- Dependency Injection
- Declarative Validation
- Unit Tests using JUnit and Mockito
- Inter-Layer Mapping using MapStruct
- Daemon Scheduler using Quartz
- Error Handling using Service Results

The solution employs a layered architecture separating web, service and data layers with a shared core layer using modules. As standard, dependency injection is used to manage the flow of control between these layers, facilitating mocking and unit testing.

The APIs have been documented with Swagger (via http://localhost:9999/swagger-ui/index.html), and each DTO is associated with validation procedures that return a BAD_REQUEST response via a global exception handler. In terms of srror handling, the code was refactored to reduce the need for throwing exceptions as this is a fairly expensive operation; instead service methods return a ServiceResult object which indicates success status and informs the controller layer on how to best handle failure conditions.

#### Some additional notes

- The Daemon service was written as a separate module in order to isolate it from the rest of the API - this provides benefits such as being able to host the daemon on a separate VM so as not to draw resources away from the admin or public APIs, and to retain functionality in the event of a denial of service. Since the data layer has been written as a simple in-memory database, running this service alongside the APIs means they run on separate processes and therefore do not share the same data. Tests have been written to demonstrate that the service that expires accreditation requests works correctly, and logs demonstrate that the Daemon runs and schedules tasks effectively.

- It was originally my intention to have the public API completely isolated and separate from the admin API - this is fairly common practice as it creates a permission boundary between the two endpoints and also means the APIs can be hosted, documented and scaled separately as traffic expectations are likely to be different. Since we are working with an in-memory database, it makes sense to keep these together for now as they could then share the same data and port as per requirements.

- It is generally good practice to version the public API (v1/users/...) for better backwards compatibility, however this seemed beyond the scope of the requirements.

# Audit Logs

The data layer currently supports this behaviour as statuses are never deleted - when a status is updated a new status is created with a more recent dateOfAssignment. Business logic then determines the most recent status for a given accreditation request; since there can only be a small number of statuses per accreditation and the data is held in memory, this logic is not in danger of being a bottleneck (though it stands to be optimized further).

In order to retrieve this audit log we will need a new route, something like GET /statusLog/:accreditationId if we wish to retrieve the log of statuses for a specific accreditation. We then call a service method that calls the AccreditationStatusRepository, which already implements a method findByAccreditationId. We can simply map this list into a service model at the service layer, convert it into a DTO at the controller layer and send it as a response to the client.

# Scaling Up

If we were to assume that data is actually being persisted, there are a number of ways that the application can scale efficiently:

- Better Isolation of Admin and Public APIs

Ideally the admin and public API controllers are implemented in separate modules and deployed as separate processes (preferably on separate VMs). This means that denial of service on the public API does not impact the admin API, and traffic from the admin API does not draw resources away from the public API. This also means that the public API stack can be scaled separately from the admin API, if the traffic expectations are different.

- Isolation and Redundancy Scaling of Daemons

This approach has been implemented in the codebase - by isolating the data sync service (the expiration daemon) we can deploy the service in its own VM or multiple VMs - since  it is idempotent and can be rerun without generating unwanted artifacts, it can be scaled redundantly. This means that if the API is being hammered, the daemon will resume its functionality normally. Additionally if a server goes offline, redundant instances can be run without needing to share context.

- Implement a caching layer in front of an SQL database

If we can reasonably assume that users who have recently submitted an accreditation request are likely to hit the public api, we can build a smart caching strategy that holds the accreditation statuses of the most recent x users to lighten the load on the database.

- Horizontal scaling of the stack with a Gateway Proxy, Load Balancer, and a NoSql database like Redis, ElasticSearch etc

Since our Web-Service-Data stack is fairly lightweight, we can scale these horizontally using something similar to Azure App Services. In the end this would only make a difference if our database layer scaled proportionally using an eventually consistent cluster-based database (or vertical scaling of an SQL database) as the current business logic is not expected to be the main performance bottleneck. It would also be possible to add a gateway proxy with rate limiting capabilities to avoid denial of service during peak traffic.

- Polling with Correlation IDs using a message queue

As a last resort (this wouldn't typically be expected with such a simple web service) the system can be written to expose an api that loads user requests onto a message queue and returns a correlation ID to the client. The client can then poll when that request has been serviced using that ID, as consumers pick up the work asynchronously. This may not provide the best user experience, and would likely require additional caching layers to track active correlation ids. This approach is commonly used in instances where processing is expected to take several seconds, such as Azure's virus scanner integrated in Blob Storage.