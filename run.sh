#!/bin/bash

# Change to the directory where the script is located
cd "$(dirname "$0")"

# setting paths to the daemon (the expiration service) and the web service
JAR_FILE_MODULE_1="DataSyncDaemon/target/datasyncdaemon-1.0-SNAPSHOT.jar"
WAR_FILE_MODULE_2="Web/target/web-1.0-SNAPSHOT.war"

JVM_OPTS="-Xms512m -Xmx1024m"

# Run the daemon
java $JVM_OPTS -jar $JAR_FILE_MODULE_1 &
PID1=$!

# deploying web service - this is using tomcat (the application.properties will automatically run these on port 9999)
CATALINA_HOME="/path/to/tomcat"
cp $WAR_FILE_MODULE_2 $CATALINA_HOME/webapps/

# Start the tomcat server
$CATALINA_HOME/bin/startup.sh
PID2=$!

wait $PID1 $PID2
