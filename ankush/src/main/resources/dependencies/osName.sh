OS=`uname -s`
if [ "${OS}" = "SunOS" ] ; then
	OSNAME=Solaris
elif [ "${OS}" = "AIX" ] ; then
	OSNAME="${OS}"
elif [ "${OS}" = "Linux" ] ; then
	if [ -f /etc/redhat-release ] ; then
		OSNAME=`cat /etc/redhat-release | tr "\n" ' '`
	elif [ -f /etc/SuSE-release ] ; then
		OSNAME=`cat /etc/SuSE-release | tr "\n" ' '`
	elif [ -f /etc/mandrake-release ] ; then
		OSNAME=`cat /etc/mandrake-release | tr "\n" ' '`
	elif [ -f /etc/debian_version ] ; then
		OSNAME=`cat /etc/lsb-release | tr "\n" ' '`
	fi
	if [ -f /etc/UnitedLinux-release ] ; then
		OSNAME="${OS}[`cat /etc/UnitedLinux-release | tr "\n" ' '`]"
	fi
fi
echo ${OSNAME}