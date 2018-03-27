#!/bin/bash

start_dir=`pwd`
cd `dirname "$0"`

./stop.sh
# define environment variable like SPRING_PROFILES_ACTIVE=prd for deployment environment.
JAVA_OPT="-server -Xms2G -Xmx5G -XX:+PrintGCDetails -Xloggc:./gc.log -Djava.net.preferIPv4Stack=true"
#JAVA_OPT="$JAVA_OPT -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=6060 -Djava.rmi.server.hostname=192.168.11.196"
nohup java $JAVA_OPT -cp ../conf:../lib/* com.gonwan.restful.springboot.Application &> nohup.out &

curr_dir=`pwd`
fails=0
while [ $fails -le 3 ]; do
    for pid in `pgrep java`; do
        one_dir=`readlink -e /proc/$pid/cwd`
        if [ "$one_dir" != "" ] && [ "$one_dir" == "$curr_dir" ]; then
            echo $pid':' $one_dir
            exit 0
        fi
    done
    sleep 1
    fails=$(($fails + 1))
done
echo 'start error...'
tail -n 15 nohup.out

cd $start_dir
