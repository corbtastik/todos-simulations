## Todo(s) Simulations

Howdy and welcome.  This repository contains Gatling Simulations to run against various parts of the [Todo(s) EcoSystem](https://github.com/corbtastik/todos-ecosystem) of Microservices.

The Microservice being called must support the [Todo CRUD api](https://github.com/corbtastik/todos-ecosystem)

### Running Simulations

Set ``TODOS_API_ENDPOINT`` to run all Simulations against a given Microservice and use ``TODOS_API_USERS`` to drive concurrency.

```bash
./mvnw gatling:integration-test \
  -DTODOS_API_ENDPOINT=http://localhost:8009 \
  -DTODOS_API_USERS=100
```

Where ``localhost:8009`` is an endpoint that exposes [Todo CRUD api](https://github.com/corbtastik/todos-ecosystem) and the number of users to ramp up over 30 seconds.

``TodosSimulation`` Ramps up a number of Users who each run a sequence of requests against the Todo API.  The sequence is:

1. Requests.createTodo - create a new Todo and save the id
1. Requests.retrieveTodo - with id pull back the new Todo
1. Requests.updateTodo - update the Todo
1. Requests.retrieveUpdatedTodo - pull back and make sure it has changes
1. Requests.deleteTodo - now delete
1. Requests.retrieveTodo404 - make sure its gone

### References

1. [Gatling](https://gatling.io/)
