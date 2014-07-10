#! /bin/bash
JAVA_HOME=/lyt/jre1.6.0_30
WORK_DIR=/lyt/service
LIBRARY_DIR=$WORK_DIR/lib
CLASSPATH=service.jar:$LIBRARY_DIR/httpclient-4.1.1.jar:$LIBRARY_DIR/httpclient-cache-4.1.1.jar:$LIBRARY_DIR/httpcore-4.1.jar:$LIBRARY_DIR/httpmime-4.1.1.jar:$LIBRARY_DIR/commons-codec-1.5.jar:$LIBRARY_DIR/ccws.jar:$LIBRARY_DIR/axis.jar:$LIBRARY_DIR/commons-discovery.jar:$LIBRARY_DIR/commons-logging.jar:$LIBRARY_DIR/FSSServerLib.jar:$LIBRARY_DIR/commons-net-1.4.1.jar:$LIBRARY_DIR/eji.jar:$LIBRARY_DIR/jaxrpc.jar:$LIBRARY_DIR/log4j.jar:$LIBRARY_DIR/ojdbc14.jar:$LIBRARY_DIR/wsdl4j-1.5.1.jar:$LIBRARY_DIR/wss4j-1.5.3.jar:$LIBRARY_DIR/xercesImpl.jar:$LIBRARY_DIR/xmlsec-1.4.1.jar:$LIBRARY_DIR/common-thread.jar:$LIBRARY_DIR/commons-pool.jar:$CLASSPATH
APP_PATH=com.crm.thread.ServiceManager
pid_file=service.pid

RUNNING=0
if [ -f $pid_file ]; then
	pid=`cat $pid_file`
	if [ "x$pid" != "x" ] && kill -0 $pid 2>/dev/null; then
		RUNNING=1
	fi
fi

RESULT=0

if [ "$1" = "start" ] ; then
  cd $WORK_DIR
  if [ $RUNNING -eq 0 ]; then
    $JAVA_HOME/bin/java -cp $CLASSPATH -Xms512m -Xmx2048m $APP_PATH &
    echo $! > $pid_file 
    sleep 2
    pid=`cat $pid_file`
    if [ "x$pid" != "x" ] && kill -0 $pid 2>/dev/null; then
      RESULT=0
    else
      RESULT=1
    fi
  fi
elif [ "$1" = "stop" ] ; then
  if [ $RUNNING -eq 1 ]; then
    kill -9 $pid
    sleep 2
    pid=`cat $pid_file` 
    if [ "x$pid" != "x" ] && kill -0 $pid 2>/dev/null; then
      RESULT=1
    else
      RESULT=0
    fi
  fi
elif [ "$1" = "check" ] ; then
  if [ $RUNNING -eq 0 ]; then
    RESULT=1
  fi
else
  echo "Usage: $0 {  start | stop | check }"
  RESULT=1
fi

exit $RESULT

