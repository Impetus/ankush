pswd=$1
cd /tmp && rm -rf *master*
cd /tmp && wget https://github.com/nathanmarz/jzmq/archive/master.zip --no-check-certificate && unzip master && cd jzmq-master/ && ./autogen.sh && ./configure && touch src/classdist_noinst.stamp && cd src/ && CLASSPATH=.:./.:$CLASSPATH javac -d . org/zeromq/ZMQ.java org/zeromq/App.java org/zeromq/ZMQForwarder.java org/zeromq/EmbeddedLibraryTools.java org/zeromq/ZMQQueue.java org/zeromq/ZMQStreamer.java org/zeromq/ZMQException.java && cd .. && make && echo "$pswd" | sudo -S make install
cd /tmp && rm -rf jzmq* *master* 