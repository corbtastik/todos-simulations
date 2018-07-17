## Todo(s) Simulations

Howdy and welcome.  This repository contains Gatling Simulations to run against various parts of the [Todo(s) EcoSystem](https://github.com/corbtastik/todos-ecosystem) of Microservices.

### Running Simulations

Set ``TODOS_API_ENDPOINT`` to run all Simulations against a given Microservice and use ``TODOS_API_USERS`` to drive concurrency.

The Microservice being called must support Todo CRUD ops.

```bash
./mvnw gatling:integration-test \
  -DTODOS_API_ENDPOINT=http://localhost:8009 \
  -DTODOS_API_USERS=100
```
