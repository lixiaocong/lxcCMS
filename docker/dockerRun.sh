#!/bin/bash
echo starting shadowsocks...
ssserver -c/config/ss.config -d start 
echo starting aria2c...
while true
do
	echo alive
	sleep 5
done
