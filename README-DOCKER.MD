# What is gP2S #
gP2S (for [Genentech’s](http://www.gene.com) Protein to Structure) is a user-friendly web-based laboratory information management system
([LIMS](https://en.wikipedia.org/wiki/Laboratory_information_management_system))
that facilitates accurate record-keeping for cryo-EM labs and multi-user, multi-project facilities. 

The following entities, their relationships and associated metadata are tracked: projects, equipment, consumables,
protocols, samples, grids, microscopy sessions, image processing sessions, maps, and atomic models. Users can also
add free-text comments, optionally including file attachments, allowing for rich annotation of any entity registered
in gP2S.

The front-end has been designed to facilitate use with touchscreen devices and tested extensively on 12.9” iPad Pros,
making it possible to use gP2S at the lab bench while preparing samples and grids, as well as at the computer when
operating the microscope, processing images or depositing models.

The back-end features a number of REST API endpoints, making it possible to integrate gP2S into existing workflows
and scripts.

In the initial release of gP2S, users enter data manually but with as many parameters as possible preset to sensible
default values. We hope that future versions will minimize manual input by interacting directly with hardware
(e.g. microscopes), software (e.g. SerialEM, EPU, Latitude), and files (e.g. star files). We are also planning to
support semi-automated (XML-based) structure deposition to EMDB. 

gP2S is still under development at Genentech. We welcome feedback, bug reports and feature requests.


# How to use this image #
Running gP2S image depends on access to [mysql database](https://www.mysql.com/), [mongodb](https://www.mongodb.com/) 
and [ldap server](https://directory.apache.org/apacheds/). 
We recommend to run all these dependencies as  multi-container Docker applications along with gP2S application. To make it easier we provide a `docker-compose` file that includes all needed configurations of the final environment, so that gP2S and its environment will be ready to use after running it. We use the following docker images as dependencies: [mysql](https://hub.docker.com/_/mysql), [mongodb](https://hub.docker.com/_/mongo) and [apacheds](https://hub.docker.com/r/openmicroscopy/apacheds).

You can also start gP2S image alone and connect it to dependencies in your network taking advantage on environment variables. 
You may read more about this in section `How to connect gP2S application to my databases or LDAP server`
           
## How to run gP2S application with the dependencies ##
First you have to install [docker](www.docker.com) on your host. On Windows 7 you need to configure additional properties described in `Windows 7 docker configuration` section. 
Then copy the content of `docker-compose` file, save it to your working directory as `docker-compose.yml` and follow the next steps.

```
version: "3"
services:
  mysql:
    container_name: gp2s_mysql
    image: mysql:5.7
    ports:
      - 3306:3306
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_USER: gp2s_user
      MYSQL_PASSWORD: password
      MYSQL_DATABASE: gp2sdb
    networks:
      - gp2snet
    #Uncomment below two lines to have mysql data persisent
    #volumes:
    #  - mysql_volume:/var/lib/mysql
  mongodb:
    container_name: gp2s_mongodb
    image: mongo:4.0.6
    environment:
      MONGO_DATA_DIR: /data/db
    ports:
      - 27017:27017
    command: mongod --smallfiles # --quiet
    networks:
      - gp2snet
    #Uncomment below two lines to have mysql data persisent
    #volumes:
    #  -  mongo_volume:/data/db
  apacheds:
    container_name: gp2s_apacheds
    image: openmicroscopy/apacheds:0.7.0
    ports:
      - 10389:10389
    networks:
      - gp2snet
  gp2s-service:
    container_name: gp2s-service
    image: arohou/gp2s
    environment:
      JAVA_OPTS: -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm
      MYSQL_HOST: mysql
      MYSQL_PORT: 3306
      MYSQL_USERNAME: gp2s_user
      MYSQL_PASSWORD: password
      MYSQL_SCHEMA: gp2sdb
      MONGO_HOST: mongodb
      MONGO_PORT: 27017
      MONGO_DATABASE: gp2sdb
      LDAP_URL: ldap://apacheds:10389
      LDAP_MANAGER_DN: uid=admin,ou=system
      LDAP_MANAGER_PASSWORD: secret
      LDAP_USER_SEARCH_QUERY: uid={0}
    ports:
      - 8080:8080
    networks:
      - gp2snet
    depends_on:
      - mysql
      - mongodb
      - apacheds

networks:
  gp2snet:

#Uncomment below lines to have mysql and mongodb data persisent
#volumes:
  #mongo_volume:
  #mysql_volume:

```

In directory with `docker-compose.yml` file, run the following commands:

1. `$ docker swarm init` - start docker manager so you can deploy composed stack.   

2. `$ docker stack deploy -c docker-compose.yml gp2s_stack` - deploy stack services for gP2S. On Windows 7 It's likely that you need to run it with additional switch `--advertise-addr string`

```
   Creating network gp2s_stack_gp2snet
   Creating service gp2s_stack_mongodb
   Creating service gp2s_stack_apacheds
   Creating service gp2s_stack_gp2s-service
   Creating service gp2s_stack_mysql
```

Running this command setups the environment and starts all the services with given configurations. After at most a few minutes all the services should be up and running, so that the setup process is done. The next steps show various ways how to verify this. 
      
3. `$ docker service ls` - list available services so you know when they are started having`REPLICAS 1/1`; it might take sometime before all services will start, final output should look as follows:
    
   ```
    bash
    ID                  NAME                      MODE                REPLICAS            IMAGE                            PORTS
    ziae4sbdjj4f        gp2s_stack_apacheds       replicated          1/1                 openmicroscopy/apacheds:latest   *:10389->10389/tcp
    m4bs6mkv6nlu        gp2s_stack_gp2s-service   replicated          1/1                 arohou/gp2s:latest              *:8080->8080/tcp
    ixvg5odbfr4j        gp2s_stack_mongodb        replicated          1/1                 mongo:latest                     *:27017->27017/tcp
    ww8cokobcjn4        gp2s_stack_mysql          replicated          1/1                 mysql/mysql-server:5.7           *:3306->3306/tcp
   ```
   
4. To be sure that gp2s-service started successfully you can read the service logs. In order to do that:
  - run `docker ps` to find the ID of gP2S-service container (wich is randomly generated by docker):
```
   CONTAINER ID        IMAGE                            COMMAND                  CREATED              STATUS                    PORTS                                                                           NAMES
   e27b7cf83b8a        arohou/gp2s:latest              "/bin/sh -c 'java   …"   23 seconds ago       Up 20 seconds             8080/tcp                                                                        gp2s_stack_gp2s-service.1.l5uljeykb36sc2vd6qwplkz8i
   15bf16cddc90        mysql/mysql-server:5.7           "/entrypoint.sh mysq…"   48 seconds ago       Up 46 seconds (healthy)   3306/tcp, 33060/tcp                                                             gp2s_stack_mysql.1.mv6hluu1povjawiz8sy6cbcom
   a2823ec16f2d        openmicroscopy/apacheds:latest   "/run.sh"                57 seconds ago       Up 56 seconds             8080/tcp, 8443/tcp, 10389/tcp, 10636/tcp, 60088/tcp, 60464/tcp                  gp2s_stack_apacheds.1.n3tdjem3aauqy96pruyvvns79
   1770e2d3504e        mongo:latest                     "docker-entrypoint.s…"   About a minute ago   Up About a minute         27017/tcp                                                                       gp2s_stack_mongodb.1.sfx14wsbp87slp9d483dtvmby
```
  - run `docker container logs e27b7cf83b8a` where `e27b7cf83b8a` is a container id. The logs should end with:
  ```
    ----------------------------------------------------------
        Local:          http://127.0.0.1:8080/gp2s
        External:       http://10.0.2.10:8080/gp2s
        Swagger:        http://127.0.0.1:8080/gp2s/swagger-ui.html
    ----------------------------------------------------------
   ```
When `docker-compose` stack is deployed, all the services run in background. You can stop any docker container that 
belongs to the service and run it again whenever you want using [docker container](https://docs.docker.com/engine/reference/commandline/container/) commands.   
   
   
## How to access the gP2S application ##
By default the application listens on port `8080` on your localhost and is available on `http://127.0.0.1:8080/gp2s`.  
Authentication is turned on by default with a single user `admin` and password `secret`. We recommend adding a tech user to LDAP (see LDAP section below) and using these credentials so as to not use LDAP admin in this context. See the sections on LDAP and gP2S configuration below.

## How to access databases ##
By default mysqldb listens on port `3306` while mongodb on port `27017`, both on your localhost. You can connect to databases
using your favorite clients and credentials from `docker-compose.yml` described in `Configuration`.

## How to access LDAP ##
By default LDAP server listens on port `10389` on your localhost. You can connect to this server using any LDAP client with 
the same credentials as for  application. Having admin rights you can manage a list of users. 
More on LDAP management you may read [here](https://directory.apache.org/apacheds/)


### How to add a user to dockerized LDAP ###
In order to add as a user to LDAP, you need an LDAP client. We recommend [Apache Studio](https://directory.apache.org/studio/).
When you run it, go to [this tutorial](https://access.redhat.com/documentation/en-US/Fuse_MQ_Enterprise/7.1/html/Security_Guide/files/LDAP-AddUserEntries.html) and follow next steps:
1. Create connection using host and port from `docker-compose.yml` file. For the Authentication use `uid=admin,ou=system` and default password `secret`
2. After successful connection, choose the subtree on the right window, for example `ou=system,ou=users`, right click and choose `New entry`
from submenu
3. Create entry using template option, choose `Use existing entry as template` and type `uid=admin,ou=system` so that all the 
attributes will be copied from the admin user. 
4. Provide user details, click `Next` 
5. Double click on `userPassword` attribute, type password, confirm and and then finish the template.

When you're done, you should be able to login to gP2S application with the user you created.


## How to update gP2S service ##
 Every time the newest version of gP2S is available and you want to run it, you need to update `gp2s-service`, using the command:
 ```
 $ docker service update gp2s_stack_gp2s-service
 ```   

## How to turn off deployed stack ##
When you want to erase the deployed gP2S stack use the following two commands. *Remember that this action erases all your data in databases!*
To persist your data even after the gP2S stack is removed, read `Data persistence` section.
  
1. `$ docker stack rm gp2s_stack` - removed deployed stack. 

2. `$ docker swarm leave --force` - shut downs docker manager


## Data persistence ##  
By default any data stored in mysql and mongodb are available as long as `gp2s_stack` is deployed and the host is in `swarm mode`. This means
that even after rebooting the host, the data won't be lost. However if `gp2s_stack` is removed or the command `docker swarm leave --force` is run, then
all the data stored in both databases is lost.  This setup is good for trying out the application and not worrying about erasing the data after all.
For production deployments we recommend setting up docker volumes for the databases so that you have full control over your data. 
This includes making it immune to Docker image crashes, accidental swarm removals, etc.
In order to enable volumes go to `docker-compose.yml` file and uncomment the following lines:

```
 volumes:
   - mysql_volume:/var/lib/mysql
```     
 
```
 volumes:
   - mongo_volume:/data/db
```
 
```
volumes:
  mongo_volume:
  mysql_volume:
```     

After you uncomment above lines you have to redeploy  `gp2s_stack` once again, by removing the old one and deploying the new one following instructions from the sections above. 
Having this setup data will be stored on your host as long as docker is installed.

## How to connect gP2S application to your own databases or LDAP server ## 
To connect gP2S web application to your own mysql server, mongodb or LDAP server, just remove from `docker-compose.yml` file the service that 
you want to replace with your own. Then configure environment variables in `gp2s-service` section of this file accordingly (see the `Configuration` section). 
When you have all dependencies running on your own and you would like to run only gp2s web application in a single docker container, you should create a file called `gp2s-service.envs` with all the environment variables from `Configuration` section related to `gp2s-service` written in separated lines, each line the VAR=VAL format. Then run the following command:
```
$ docker run -it -p 8080:8080 --env-file ./gp2s-service.envs arohou/gp2s:latest
```  
If you want to have gP2S running in background, in non interactive mode, use this command:
```
$ docker run -d  -p 8080:8080 --env-file ./gp2s-service.envs arohou/gp2s:latest
```  

When container starts successfully you will see: 
```
    ----------------------------------------------------------
        Local:          http://127.0.0.1:8080/gp2s
        External:       http://10.0.2.10:8080/gp2s
        Swagger:        http://127.0.0.1:8080/gp2s/swagger-ui.html
    ----------------------------------------------------------
``` 


## Configuration ##
There are the following environment variables configurable in `docker-compose.yml` file for the gP2S stack services:

1. mysql:
    - `MYSQL_ROOT_PASSWORD`: password for mysql server root
    - `MYSQL_USER`: user that will be used by gp2s-service
    - `MYSQL_PASSWORD` : password for the above user
    - `MYSQL_DATABASE`: database schema that will be used by gp2s-service 

2. mongodb:
    - `MONGO_DATA_DIR`: directory in mongodb container where data is stored

3. gp2s-service:
    - `JAVA_OPTS`: java runtime options, it is recommended to leave the defaults  
    -  `MYSQL_HOST`: mysql server address
    -  `MYSQL_PORT`: mysql port number
    -  `MYSQL_USERNAME`: user used to connect to mysql server
    -  `MYSQL_PASSWORD`: password for this user
    -  `MYSQL_SCHEMA`: name of the schema used by gp2s-service
    -  `MONGO_HOST`: mongodb server address
    -  `MONGO_PORT`: mongodb port number
    -  `MONGO_DATABASE`: name of mongodb database used by gp2s-service
    -  `LDAP_URL`: LDAP url followed by port number
    -  `LDAP_MANAGER_DN`: user that is used to connect to LDAP configured as LDAP query
    -  `LDAP_MANAGER_PASSWORD`: password of this user
    -  `LDAP_USER_SEARCH_QUERY`: LDAP query used to search for the users in LDAP server, needed to authenticate users in gp2s-service. When you connect to  LDAP server in your organization, ask
    your LDAP administrator what is the lookup query configured in this server. 
 

## Windows 7 docker configuration ##
When you run docker on Windows 7 you need to make some additional configuration changes to run gP2S docker container.

1. Go to VirtualBox application
2. Select the docker default VM
3. Open Preferences
4. Select Network tab
5. Find and select the network adapter tab that uses NAT
6. Open Advanced settings> Port forwarding
7. Add following definitions'

```
NAME | Host IP | Host Port | Guest IP | Guest Port
mysql | 127.0.0.1 | 3306 | <Empty> | 3306
mongodb | 127.0.0.1 | 27017 | <Empty> | 27017
ldap | 127.0.0.1 | 10389 | <Empty> | 10389
gp2s-service | 127.0.0.1 | 8080 | <Empty> | 8080
```

## Getting started with gP2S ##
Once you have gP2S up and running, it is like a blank slate: it doesn’t know anything about your projects, what microscopes and other equipment your facility has, etc. Before you can start using it to track actual experiments, you will need to spend a few minutes telling gP2S about all these things. So the first thing you should do is navigate to the “Settings” section and work through all the sections to enter information about your lab’s setup. 

At a minimum, you will need to define a project. This will enable you to create proteins, ligands and samples in gP2S.

To enable the creation of grids, you will also need to tell gP2S about Protocols (surface treatment, negative stain, vitrification), Equipment (glow-discharge or plasma cleaners, called “surface treatment machines”, vitrification machines, cryo storage devices), Consumables (grid types, blotting paper). 

For each type of entity (e.g. “Vitrification Machine”, “Grid Type”), navigate to the relevant page by using the left-hand-side menu, click the “Create New” button in the top left of the page, and fill in the form that appears. Fields with red asterisks are required; others are optional. Then click the “Save” button at the top of the page when you are done.

To enable microscopy sessions, you will need to tell gP2S about your microscope(s), which in turn entails creating electron detector(s) and sample holder(s). When creating the microscope object, you have the opportunity to enter the list of magnifications for this microscope. You only need to enter those magnifications you want users to be able to use during data collection. 

