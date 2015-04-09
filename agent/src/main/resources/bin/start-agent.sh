# setting agent library directory
AGENT_LIB=$AGENT_INSTALL_DIR/.ankush/agent/libs
# setting agent config directory
AGENT_CONF=$AGENT_INSTALL_DIR/.ankush/agent/conf
# running agent start command
(jps | grep AnkushAgent || java -Xmx256m -Dagent.install.dir=$AGENT_INSTALL_DIR -Dlog4j.configuration=file:$AGENT_CONF/log4j.properties -cp $AGENT_LIB/*:$AGENT_LIB/**.jar com.impetus.ankush.agent.daemon.AnkushAgent) > /dev/null &