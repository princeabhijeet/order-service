FROM eclipse-temurin:17-jdk-jammy

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} order-service.jar

ENTRYPOINT [ "java", "-jar", "/order-service.jar" ]

EXPOSE 9002

#
# Build docker image:
# docker build -t springboot-microservices/order-service:0.0.1 .
# -t : tag info will be provided in command
# last . : find Dockerfile in current root folder to build docker image 

#
# See all images:
# docker images

#
# Run docker image:
# docker run -d -p9002:9002 --name order-service springboot-microservices/order-service:0.0.1
# docker run -d -p9002:9002 --net msnet --name order-service springboot-microservices/order-service:0.0.1
# -d : detatched mode
# -p : port information
# 8761:8761 : hostport:containerport
# --name : name of container
# last name/imageid : at last should be image name or image id

#
# see running images/containers
# docker ps -a
#

