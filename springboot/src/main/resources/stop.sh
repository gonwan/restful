#!/bin/bash

start_dir=`pwd`
cd `dirname "$0"`

curr_dir=`pwd`
for pid in `pgrep java`; do
    one_dir=`readlink -f /proc/$pid/cwd | gawk '{ print $1 }'`
    if [ "$one_dir" != "" ] && [ "$one_dir" == "$curr_dir" ]; then
        echo -n 'killing' $pid '.'
        kill -9 $pid
        while ps --pid $pid &> /dev/null; do
            sleep 1
            echo -n '.'
        done
        echo 'done.'
    fi
done

cd $start_dir
