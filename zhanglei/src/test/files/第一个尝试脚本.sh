#!/bin/bash

unzip vip.zip
/usr/bin/mv -f vip /data/wwwroot/xini-v2-agent/WEB-INF/classes/
rm -rf vip.zip
for i in `ps -ef | grep tomcat |grep -v grep | awk -F '[ ;]+' '{print $2}'`
do
    kill -9 $i
done
/usr/local/tomcat/bin/startup.sh
