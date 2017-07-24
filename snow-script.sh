#!/bin/bash
echo "----snow-push apps---"
cf create-org snow
cf target -o snow
cf create-space test
cf target -o snow -s test
cf a

#i=$(ls -p | grep "/")

#i=$(ls -l | grep ^d) 



cd UAATokenGenerator
mvn clean install  -Dmaven.test.skip=true
cf push

cd ../ListUsers
mvn clean install  -Dmaven.test.skip=true
cf push

 
