#!/bin/bash
echo "-------script for snow app push--"

 cf login --skip-ssl-validation -a https://api.sys.eu.cfdev.canopy-cloud.com -u admin -p JioovG*jCadGect37mkyuur9kyat$

cf create-org snow
cf target -o snow
cf create-space test
cf target -o snow -s test

i=$(ls -d */) 
arra=()
for word in $i 

do #arra+=($word);
	cd $word
	mvn clean install  -Dmaven.test.skip=true
	cf push
	cd ../
done 

cf a
