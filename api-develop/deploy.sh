#!/bin/bash
PASS=n@ta3zbXWBN
./gradlew build
if [ $? -eq 0 ]
then
    echo $(cp build/libs/api-1.2.1-SNAPSHOT.jar /opt/mr/api/api.jar)
    systemctl daemon-reload
    systemctl restart mr-api
fi




