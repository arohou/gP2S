## Introduction ##
The gP2S web application is composed of backend and frontend. Backend is based on [spring-boot 1.3](https://spring.io/projects/spring-boot) with embedded Tomcat 8 and [Java 8](https://openjdk.java.net/) while frontend on [Vue.js 2.4.2](https://vuejs.org/v2/guide/). The application
uses [mongoDB](https://www.mongodb.com/) and mySQL (https://www.mysql.com/) for storage and [LDAP](https://ldap.com/) for authentication. By default, both of them are shipped and deployed as one application.

In order to build the application all you need is Java 8 and optionally [docker](https://www.docker.com/). More information about building the application is given below. 

In order to run the application, you need Java 8, mysql, mongodb, optionally ldap server and optionally docker. More information on running the application is given in further sections.     

## Building application ##
Building the project is based on [Maven](https://maven.apache.org/). In a codebase we provide maven binary for Windows and Linux-based systems, so you don't need to download and configure maven
on your own, however you can still build the application using maven you already have if it is 3.3+ release.

By default the result of the build is JAR package and docker image. If you don't want to use docker and build only JAR package, find the below lines in `backend\pom.xml` and comment them out:

```
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>dockerfile-maven-plugin</artifactId>
    (...)
</plugin>
```    

The gP2S project leverages [Spring Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html). You can specify as many profiles as you want, with various configurations for e.g different environments or data storage. The application is built always with all the profiles and on the 
bootstrap of the application you specify which profiles are active. Profile configuration is placed in `backend\src\main\resourcesa\application-profilName.yml` files. More information on configuring profiles you will find in below sections. 

Maven `pom.xml` is configured the way it builds frontend first, than it copies the frontend resources to backend sources, and then builds it as final application. So that you don't have to build each part separately and merging them. What is more you don't need `npm` tool installed to build frontend, since maven download it and run it for you during frontned build. 

As it was said, to run the application you need access to mysql and mongdb. We propose four different approaches to gain it:
1. Build the application with embedded [HSQLDB](http://hsqldb.org/) and [embedded mongodb](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)
2. Run the dependencies as docker containers and build the application with related configuration.
3. Build the application to use external mysql and mongodb.     
4. Run only backend using maven without building the sources

In all the above ways, excluding docker approach, successful run of the application shows in the end of the logs the following information:

```
    ----------------------------------------------------------
        Local:          http://127.0.0.1:8080/gp2s
        External:       http://10.0.2.10:8080/gp2s
        Swagger:        http://127.0.0.1:8080/gp2s/swagger-ui.html
    ----------------------------------------------------------
```

In the following sections you get information about each of the above approach. By default authentication is turned off, go to `Authentication` section to get to know how to manage authentication.

### Building and running with embedded databases ###
By default we provide configuration that uses embedded HSQLDB and embedded mongodb, what is a fine solution to start developing fast and easy. The databases live
as long as the application does and by default data are erased on application shutdown. 
To build the application run this command in root directory of the project
```
$ mvnw clean package -DembeddedMongodb=true
```
As a result you get docker image (you have this option enabled) in your local docker repository and JAR package in `backend\target` which is ready to run with authentication turned off.

In order to run it use the following command

```
$ java -jar backend\targer\gp2s-service-1.1.43.jar
```

### Building and running with dependencies run as docker containers ###
When you have docker onboard, this approach is recommended, since it requires a minimum effort to run databases and LDAP. 
In order to build the application run this command in root directory of the project

With authentication turned on:
```
$ mvnw clean package -DfrontendEnv=prod
```

With authentication turned off:
```
$ mvnw clean package 
```

 
As a result you get docker image (you have this option enabled) in your local docker repository and JAR package in `backend\target`. 

In order to run the application and dependencies as docker containers, go to `README-DOCKER` file, where you get the comprehensive guideline about dockerized gP2S. 
When you want to run only the dependencies go to `backend\docker-compose.yml` file for
further instructions. 

When you want to run the application from maven or as jar file with dockerized dependencies already running use:
 
```
$ mvn spring-boot:run -Dspring.profiles.active=docker-dependencies
``` 
to run from maven with embedded tomcat. If you haven't built the application before, it will run only the backend without the frontend. 

Use 
```
$ java - jar --spring.profiles.active=docker-dependencies
```
to run jar file with embedded tomcat


### Building and running with external databases ###
External database can be a database run as on your localhost or any other database accessible from your localhost. The minimum configuration for each of it is the following:
1. Mysql:
      - contains database named `gp2sdb`
      - there is a `gp2s` user with grant all privileges on `mysqldb.gp2sdb` 
2. Mongo
      - contains `gp2sdb` database
Having that, you need to configure spring-boot backend. In order to do that, clone the file `example-application-local.yml` as `application-local.yml` and edit the following entries:
1. For Mysql edit `YOUR_MYSQL_ADDRESS`, `YOUR_MYSQL_PORT` and `YOUR_GP2S_USER_PASSWORD`:
```
datasource:
    (...)
    url: jdbc:mysql://YOUR_MYSQL_ADDRESS:YOUR_MYSQL_PORT/gp2sdb?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
    username: gp2s_user
    password: YOUR_GP2S_USER_PASSWORD
    (...)
``` 

2. For MongoDB edit `YOUR_MONGO_ADDRESS`, `YOUR_MONGO_PORT`,
```
data:
    mongodb:
      uri: mongodb://YOUR_MONGO_ADDRESS:YOUR_MONGO_PORT/gp2sdb
```
In order to build the application run this command in root directory of the project:
```
$ mvnw clean package
``` 
 As a result you get docker image (you have this option enabled) in your local docker repository and JAR package in `backend\target` 

In order to run the application:
```
$ mvn spring-boot:run -Dspring.profiles.active=local
```
to run from maven with embedded tomcat. If you haven't built the application before, it will run only the backend without the frontend. 

Use
```
$ java - jar --spring.profiles.active=docker-local
``` 
to run jar file with embedded tomcat

### Running frontend separately ###
The detailed instructions on how to run frontend separately and connect it to running backend are described in `frontend\README.MD` file


## Authentication ##
By default the authentication is turned off. 
In order to turn it on you have to take these two steps:
1. *Frontend*: go to `frontend\config` directory and edit one of *.env.js file with the profile you would like to use. When you build jar file, by default `dev.env.js` is chosen. Turn on authentication with:
```
SECURITY_ENABLED: true
```

2. *Backend*: in `application-${profile}.yml`, where `${profile}` is a spring profile you want to use, uncomment properties with prefix `gp2s.auth`. By default they are configured
to use dockerized LDAP. If you want to use your own LDAP, you have to change the configuration. The properties are the following:  
  
| Parameter | Docker Value | Description |
| --------- | ------------- | ----------- |
| `gp2s.auth.ldap.url` | `ldap://localhost:10389` | URL to the LDAP server. Usually in form of ldap://server:port |
| `gp2s.auth.ldap.userSearchQuery` | `uid={0}` | An [LDAP query](https://ldapwiki.com/wiki/LDAP%20Query%20Examples) for uniquely binding to a user. The `{0}` is a template token which will be replaced with the username provided on the  login form.|
| `gp2s.auth.ldap.managerDn` | `uid=admin,ou=system` | *Optional*. If the LDAP server has authentication enabled, this is the user DN used in the [LDAP simple authentication schema](https://ldapwiki.com/wiki/Simple%20Authentication). |
| `gp2s.auth.ldap.managerPassword` | `uid=admin,ou=system` | *Optional*. If the LDAP server has authentication enabled, this is the password used in the [LDAP simple authentication schema](https://ldapwiki.com/wiki/Simple%20Authentication). |

Go to `SecurityConfig.java` class to see how these properties are used for LDAP connection.

### Default credentials:
The default credentials when using the 
[bundled Apache Dictionary in the dockerized dependencies](https://hub.docker.com/r/openmicroscopy/apacheds/). 

 * username: admin
 * password: secret



## Deployment ##

**NOTE: The detailed deployment procedure is out of the scope of this document and needs to be developed by the team adopting gP2S source code for their own purposes. This part is providing only guidelines on this topic.**

### Hosting ##

A Spring Boot application is deployed to servers as a single, self-hosted, executable JAR file. This means, that 
when executed with Java, the JAR file begins to act as a small HTTP server handling all the web traffic, i.e. 
serving API responses or static frontend files depending on the URL path provided. Please follow [Spring Boot 
documentation](https://docs.spring.io/spring-boot/docs/1.3.0.RELEASE/reference/html/). 

#### Monitoring & runtime configuration ####

Monitoring and management of the application are out of the scope of this document. We believe that standard [Spring Boot 
documentation about Monitoring and Management](https://docs.spring.io/spring-boot/docs/1.3.0.RELEASE/reference/html/production-ready-monitoring.html)
is sufficient to manage gP2S app.

### Version update ###

Script `set-version.sh` located in the main directory should be used for upgrade version numbers (both backend and 
frontend part of the app).
```$bash
$ ./set-version.sh 1.1.4-SNAPSHOT
```
  
## Integration ##

Genentech deployment of gP2S is integrated with the corporate system landscape. This integration was abstracted out from 
open source version of the app. However, it is possible to implement custom integration with three types of systems:
- Project management
- Ligand registration
- Protein registration 

### Project management system ###
It is possible to integrate with an external system providing additional information about structure solving projects
that are referenced by gP2S to organize lab work. This way all details about the project would be fetched (read-only
access) from that system after providing its ID. This would be available in the settings section.

### Ligand registration system ###
It is possible to integrate with external system identifying ligands - ligand registration system. Based on that 
integration additional features can be enabled in gP2S: 
- Validation of ligand identifiers:
  - Concept ID
  - Batch ID 
- Fetching and displaying of molecular weight
- Visual (structural) representation of the ligand - displaying image representing ligand structure

### Protein registration system ###
It is possible to integrate with external system identifying proteins and protein purification - protein registration 
system. Based on that integration additional features can be enabled in gP2S: 
- Validation of purification ID
- Fetching of molecular weight and based on its value recalculating sample component final concentration between μM and 
  mg/ml
- Displaying other valuable information, like buffer 

### Configuration of external systems ###
Configuring external systems can be done in `frontend\config\*.env.js` files by setting the following flags ((by default there are no external systems integrated) :
  - `PEOPLE_DATA_SYSTEM_EXISTS` - [Boolean] if set to "true", additional information about user who created the entity is displayed for each entity
  - `SECURITY_ENABLED` - [Boolean] security needs to be configured
  - `PROJECT_MANAGEMENT_SYSTEM_EXISTS` - [Boolean] if set to "true", additional information about projects
  - `PROJECT_MANAGEMENT_SYSTEM_NAME` - [String] name of project management system (used in labels and error messages)
  - `LIGAND_REGISTRATION_SYSTEM_EXISTS` - [Boolean] if set to "true", additional information about ligands is displayed in ligand related views
  - `LIGAND_REGISTRATION_SYSTEM_NAME` - [String] name of ligand registration system (used in labels and error messages)
  - `PROTEIN_REGISTRATION_SYSTEM_EXISTS` - [Boolean] if set to "true", additional information about proteins is displayed in ligand related views
  - `PROTEIN_REGISTRATION_PUR_LABEL_SHORT` - [String] short name of ligand registration system
  - `PROTEIN_REGISTRATION_SYSTEM_NAME` - [String] name of protein registration system (used in labels and error messages)

Setting these flags enables certain sections of frontend to show additional information. It is up to the project 
integrator to implement proper backend implementation of REST services that are consumed by the frontend.


## Licensing ##
The project is licensed under [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0).

## Issues ##
Any issues can be reported using the GitHub standard issue management mechanism.
