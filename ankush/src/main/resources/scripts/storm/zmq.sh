pswd=$1
cd /tmp && rm -rf zeromq*
cd /tmp && wget http://download.zeromq.org/zeromq-2.1.7.tar.gz && tar -xzf zeromq-2.1.7.tar.gz && cd zeromq-2.1.7 && ./configure && make && echo "$pswd" | sudo -S make install
cd /tmp && rm -rf zeromq* zmq.sh
 
