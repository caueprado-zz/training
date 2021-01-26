FROM maven:3.6.3-jdk-11

RUN mkdir /training && \
    groupadd app && \
    useradd app -u 1001 -g app -d /training -s /bin/bash

COPY /backend/target/schedule-backend-0.0.1-SNAPSHOT.jar /training/

RUN chown 1001:app /resiliencia -R

WORKDIR /resiliencia

CMD java -jar -Dspring.profiles.active=local /training/schedule-backend-0.0.1-SNAPSHOT.jar -Dcom.sun.management.jmxremote \
 -Dcom.sun.management.jmxremote.port=9010 \
 -Dcom.sun.management.jmxremote.local.only=false \
 -Dcom.sun.management.jmxremote.authenticate=false \
 -Dcom.sun.management.jmxremote.ssl=false

EXPOSE 9010
EXPOSE 8091