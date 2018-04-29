#! /bin/sh

git pull origin master
mvn -Pproduction clean package
docker build -t tio:$1 .