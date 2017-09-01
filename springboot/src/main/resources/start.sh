#!/bin/bash

start_dir=`pwd`
cd `dirname "$0"`

./stop.sh
JAVA_OPT="-server -d64 -Xms2G -Xmx5G -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./gc.log"
nohup java $JAVA_OPT -Dlogback.configurationFile=file:../conf/logback.xml -cp ../conf:../lib/* com.gonwan.restful.springboot.Application &> nohup.out &

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
