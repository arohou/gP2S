FROM  openjdk:8-jre-alpine

VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
ADD $JAR_FILE gp2s-service.jar

ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm"
ENV SPRING_PROFILES_ACTIVE="docker"
ENV MYSQL_HOST="mysql"
ENV MYSQL_PORT="3306"
ENV MYSQL_USERNAME="gp2s_user"
ENV MYSQL_PASSWORD="password"
ENV MYSQL_SCHEMA="gp2sdb"
ENV MONGO_HOST="mongodb"
ENV MONGO_PORT="27017"
ENV MONGO_DATABASE="gp2sdb"
ENV LDAP_URL="ldap://apacheds:10389"
ENV LDAP_MANAGER_DN="uid=admin,ou=system"
ENV LDAP_MANAGER_PASSWORD="secret"
ENV LDAP_USER_SEARCH_QUERY="uid={0}"

CMD java \
    -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS \
    -jar gp2s-service.jar \
    --spring.profiles.active=$SPRING_PROFILES_ACTIVE \
    --gp2s.mysql.host=$MYSQL_HOST \
    --gp2s.mysql.port=$MYSQL_PORT \
    --gp2s.mysql.username=$MYSQL_USERNAME \
    --gp2s.mysql.password=$MYSQL_PASSWORD \
    --gp2s.mysql.schema=$MYSQL_SCHEMA \
    --gp2s.mongo.host=$MONGO_HOST \
    --gp2s.mongo.port=$MONGO_PORT \
    --gp2s.mongo.database=$MONGO_DATABASE \
    --gp2s.auth.ldap.url=$LDAP_URL \
    --gp2s.auth.ldap.manager-dn=$LDAP_MANAGER_DN \
    --gp2s.auth.ldap.manager-password=$LDAP_MANAGER_PASSWORD \
    --gp2s.auth.ldap.user-search-query=$LDAP_USER_SEARCH_QUERY