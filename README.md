# Java Producer-Consumer with Spring Boot

Producer Consumer Problem is a typical multi-process synchronization problem where both
processes are sharing the shared fixed-size buffer also referred to as the queue.

This is a Producer-Consumer implementation in Spring boot where one or more Producers is generating a series of tasks and one or more Consumers
are consuming the generated tasks for processing. 

Both Consumers & Producers have a shared place (Queue) where the Producer adds tasks and the Consumer removes and processes the tasks.

## General info

This is a Spring Boot project using Java 11, the implementation of the producer consumer is done in pure Java and no external libraries where used.
The application is using the Spring IoC containers which are responsible for instantiating, configuring, and assembling beans.

## Terminology

**Producer** -> A producer can be any class that implements the Producer interface. The Producer interface contains only one method, createTasks() which returns a List of tasks. 
When you add/register a producer to the Foreman, the foreman will  wrap the producer in a ProducerRunner class and then run the ProducerRunner in a separate thread in order for the Producer's tasks to be written in the shared queue.
 
**Consumer** -> A consumer instance sole responsibility is to execute a task and update the engine accordingly.
 
**Task** -> A Task can be any class that implement the Task interface. The task interface has two methods `getTaskType()` & `execute()`. There can be 3 types of tasks, <i>CPU_INTENSIVE, IO_INTENSIVE or MEMORY_INTENSIVE</i>.

**Engine** -> Holds the state of the producer-consumer application, it holds the common queue & various monitor statistics based on which the foreman can make decisions.
 
**Foreman** -> Responsible to instantiate/spawn the ProducerRunner and Consumer threads that will write and read from the common queue. 
The foreman is always alive, in case there are no tasks to produce or consume the foreman goes into wait mode.
 
**TaskDistributor** -> Responsible to distribute the tasks from the shared queue to their associated queue by type, i.e an io task will be moved from the shared queue to the io queue
 
 ## How to extend the service
 
 In order to extend the service you need to create your own Producer & Tasks and then create a Controller class that will add/register the producer to the Foreman.<br>
 An example is already provided under the example package. <br>
 Once that is done you can monitor the task processing of your producer by hitting the `/monitor` endpoint.
 
 ## How to run with Maven
 Once you are in the project's home directory then run below command. <br>
 Using the spring boot plugin: `mvn spring-boot:run` 
 
 ## How to run with docker
 
 `docker run geom4rios/java-producer-consumer:master`
 
 ## How to run the example
 
 Once the application is up and running then using a rest client, i.e postman or insomnia, hit the endpoint `{{host}}/engine/example`.<br>
 You can monitor the application by hitting the endpoint `{{host}}/monitor`.
 
 The default host used is http://localhost:8080.
 
 ## API Documentation
 
 A small API documentation exists, you can view the API by going into this url -> `https://editor.swagger.io/`
 and copy pasting the contents of the `producer-consumer-swagger.yml` file inside the editor.
 
 
 
 