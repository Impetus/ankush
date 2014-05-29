# setting ankush home.
BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
ANKUSH_DIR=$HOME/.ankush
ANKUSH_LOG_DIR=$ANKUSH_DIR/server/log
LOGFILE=$ANKUSH_LOG_DIR/tomcat.log
DB_FILE="$BASEDIR/conf/jdbc.properties"
ANKUSH_DB_DIR=$ANKUSH_DIR/server/db
ANKUSH_DB_FILE=$ANKUSH_DB_DIR/jdbc.properties

export ANKUSH_HOME=$BASEDIR


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

# Set java options
JAVA_OPTS="-Xmx2g -Xms512m -XX:PermSize=512m -XX:MaxPermSize=1g -Dderby.system.home=$ANKUSH_DB_DIR"

# running the executable jar.
java $JAVA_OPTS -jar $BASEDIR/lib/ankush.jar </dev/null >>$LOGFILE 2>&1 &

# writing the pid of server /tmp/ankush.pid file.
echo $! > /tmp/ankush.pid

