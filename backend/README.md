# gP2S REST Service

1. Configuring gP2S REST Service on your local box
    - requires JDK 8 to be installed, you can get it here (Java SE Development Kit 8 Downloads): http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
    - Java 8 has to be enabled in your OS so the following command "java -version" should return "1.8.xx"
    - For IntelliJ IDEA and Eclipse users - please install Lombok Plugin as it's being widely used in this application
    - to build and run the application locally maven 3.2+ is required https://maven.apache.org/install.html
    
2. Running external dependencies — using Docker
    1. Install [Docker](https://www.docker.com/community-edition) on your machine.
    2. To start:
    ```
    docker swarm init
    docker stack deploy -c docker-compose.yml gp2s_dependencies
    docker service ls
    ```
    3. To stop:
    ```
    docker stack rm gp2s_dependencies
    docker swarm leave --force
    ```
    2. ```docker swarm init``` - start docker manager so we can deploy composed stack
    3. ```docker stack deploy -c docker-compose.yml gp2s_dependencies``` - deploy stack with development services 
    for gP2S
    ```
    Creating network gp2s_dependencies_gp2snet
    Creating service gp2s_dependencies_apacheds
    Creating service gp2s_dependencies_mysql
    Creating service gp2s_dependencies_mongodb
    ```
    4. ```docker service ls``` - list available services so you know when they are started: ```REPLICAS 1/1```; it might take 
    sometime before all services will start, final output should look as follows:
    ```bash
    ID                  NAME                         MODE                REPLICAS            IMAGE                            PORTS
    s0qesv6a0q7a        gp2s_dependencies_apacheds   replicated          1/1                 openmicroscopy/apacheds:latest   *:10389->10389/tcp
    jz721u92hzp6        gp2s_dependencies_mongodb    replicated          1/1                 mongo:latest                     *:27017->27017/tcp
    u2xkaniq0o5s        gp2s_dependencies_mysql      replicated          1/1                 mysql/mysql-server:5.7           *:3306->3306/tcp
    ```
    5. ```docker stack rm gp2s_dependencies``` - after you are done with development and want to free some resources 
    you can remove the deployed stack
    6. ```docker swarm leave --force``` - at the end you can shut down docker manager

4. Running gP2S REST Service on your local box
    - before the first run, you should prepare your local profile
        -- go to backend/src/main/resources
        -- clone file example-application-local.yml as application-local.yml
        -- open new file application-local.yml and adjust the configuration
    - to run application execute the following command `mvn spring-boot:run`
    - to run application without docker execute the following command `mvn spring-bot:run -Dspring.profiles.active=hsqldb`
    - when the app is loaded, you can access it according to the info displayed in logs.

5. To build a war file, simply run:
    ```bash
    $ mvn clean package -Dspring.profiles.active=[dev,prd]
    ```

### Running application tests

To run application unit tests:
```
$ mvn test
```

To run application integration tests:

```
$ mvn integration-test
or 
$ mvn verify
```

### Windows 7 localhost port forwarding with VirtualBox

1. Go to VirtualBox application
2. Select the docker default VM
3. Open Preferences
4. Select Network tab
5. Find and select the network adapter tab that uses NAT
6. Open Advanced settings> Port forwarding
7. Add following definitions
```
NAME | Host IP | Host Port | Guest IP | Guest Port
mysql | 127.0.0.1 | 3306 | <Empty> | 3306
mongodb | 127.0.0.1 | 27017 | <Empty> | 27017
```

