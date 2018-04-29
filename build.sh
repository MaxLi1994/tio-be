#! /bin/sh

git pull origin master
mvn -Pproduction clean package
docker build -t tio:$1 .
docker stop $2
docker run -d -p 8080:8080 tio:$1