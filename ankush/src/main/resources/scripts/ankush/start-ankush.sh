# setting ankush home.
BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
ANKUSH_DIR=$HOME/.ankush
ANKUSH_LOG_DIR=$ANKUSH_DIR/server/log
LOGFILE=$ANKUSH_LOG_DIR/tomcat.log
DB_FILE="$BASEDIR/conf/jdbc.properties"
ANKUSH_DB_DIR=$ANKUSH_DIR/server/db
ANKUSH_DB_FILE=$ANKUSH_DB_DIR/jdbc.properties
ANKUSH_REPO_DIR=$ANKUSH_DIR/server/repo


# export ankush home directory.
export ANKUSH_HOME=$BASEDIR

# setting and exporting server port.
export ANKUSH_SERVER_PORT=8080

# Check dtabase configutaion file
if [ ! -f $ANKUSH_DB_FILE ];
then
  # Create directorie
  mkdir -p $ANKUSH_DB_DIR

  # Copy sample database configuration file
  cp $DB_FILE $ANKUSH_DB_FILE
fi

# creating logs directory.
mkdir -p $ANKUSH_LOG_DIR

# creating repo dir
mkdir -p $ANKUSH_REPO_DIR

#remove existing links
find ~/.ankush/server/repo -maxdepth 1 -type l -exec rm -f {} \;

#create links for bundlled repo file
ln -s "$BASEDIR/repo/"* $ANKUSH_REPO_DIR

echo Starting Ankush ...

# check if port is already in use

netstat -nl | grep $ANKUSH_SERVER_PORT > /dev/null

if [ $? -eq 0 ];
then
  echo "Port $ANKUSH_SERVER_PORT is already in use"
  exit 1
fi

cd "$BASEDIR"

# Set java options
JAVA_OPTS="-Xmx2g -Xms512m -XX:PermSize=512m -XX:MaxPermSize=1g -Dderby.system.home=$ANKUSH_DB_DIR"

# running the executable jar.
java $JAVA_OPTS -jar "$BASEDIR/lib/ankush.jar" -httpPort $ANKUSH_SERVER_PORT </dev/null >>$LOGFILE 2>&1 &

# writing the pid of server /tmp/ankush.pid file.
echo $! > /tmp/ankush.pid

echo Ankush started 
echo "Ankush Access URL : http://<AnkushServerIP>:$ANKUSH_SERVER_PORT/ankush/"