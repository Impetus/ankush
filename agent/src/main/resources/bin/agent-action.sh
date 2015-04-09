# setting agent library directory
AGENT_LIB=$AGENT_INSTALL_DIR/.ankush/agent/libs/
# running agent action command
java -cp $AGENT_LIB/*:$AGENT_LIB/**.jar  -Dagent.install.dir=$AGENT_INSTALL_DIR com.impetus.ankush.agent.action.ActionHandler $*
