#!/bin/bash

CURAPPID=dataStorage

JAVACLASSPATH=../config/:$(echo ../lib/*.jar|sed 's/ /:/g')

APPNAME=com.instant.datastorage.StorageMain
APPPARA=
JAVABIN=java
curDate=`date '+%G%m%d'`
curTime=`date '+%H%M%S'`
LOGFILE=$curDate\_$curTime\_$CURAPPID.log
echo $JAVACLASSPATH
function  displayAppStatus()
{
   echo '  '   
   echo 'Current SmsApp['$CURAPPID'] Status: '
   echo ' '

   ps -ef |grep GID=$GLOBALPID|grep "SMSAPPID=$CURAPPID "

   echo ''
}

function stopApp()
{
  kill -15 `ps -ef|grep SMSAPPID="$CURAPPID "|grep -v console|grep -v grep|awk '{print $2}'`

}
 
function startApp()
{
$JAVABIN  -DGID=$GLOBALPID -DSMSAPPID=$CURAPPID -classpath $JAVACLASSPATH $APPJAVA_ADDPRO  $APPNAME $APPPARA > ../logs/$LOGFILE 2>&1 &

}

function startAppWithoutLog()
{
$JAVABIN  -DGID=$GLOBALPID -DSMSAPPID=$CURAPPID -classpath $JAVACLASSPATH $APPJAVA_ADDPRO  $APPNAME $APPPARA &

}


if [ $# -eq 0 ]; then
    displayHelp
    exit
fi

# Shell Control
if [ $1 = "-start" ]; then

    stopApp 
    startApp
    sleep 3
    displayAppStatus
    exit
fi

# Shell Control
if [ $1 = "-start_nolog" ]; then

    stopApp
    startAppWithoutLog
    sleep 3
    displayAppStatus
    exit
fi

if [ $1 = "-stop" ]; then
    stopApp
    sleep 5
    displayAppStatus
    exit
fi


if [ $1 = "-status" ]; then
    displayAppStatus
    exit
fi

