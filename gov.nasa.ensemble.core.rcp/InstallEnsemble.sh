#! /bin/bash

## MOST CURRENT VERSION OF INSTALL SCRIPT USE THIS ONE TO EDIT STUFF
# UPDATED: Guy Pyrzak 2/12/2007

# Install script to collect up settings for Ensemble and all servers, then 
# appropriately edit the rcp properties file and the startup file for the servers.

# if they say no then output their previous default settings for those values that i won't be filling in.
#  DONE - however not done in the most efficient way, sorry to whomever has to deal with it

# We may want to add a -v flag to let the verbose stuff into
# We may want a way to delete or ignore the user defaults file
# we may want a way to get help on all this sort of 

# 

##########################################################################################################################################
##########################################################################################################################################

# These are the strings that we will grep for when doing a untaring and directories. This should be edited per build as appropriate.

EUROPA_TARDIR_ID="europa"
CLIENT_TARDIR_ID="PSI"
#SUPPORT_TARDIR_ID="Support"
DEFAULT_SETTINGS_FILENAME="default_settings"

##########################################################################################################################################
##########################################################################################################################################

# Before we do anything lets make sure they are root
checkForRoot=`whoami`   
IsRootCheck="n"
if [ "$checkForRoot" != "root" ]; then
	#echo -e "\n\n!! ERROR !! \n   You must be root to run this script. Please type in sudo ./install.sh to run this script.\n\n"
	#exit
	IsRootCheck="n"
else
	IsRootCheck="y"
fi
#make sure we're in the right directory
#cd `echo "$0" | grep -o "\(.*/\)*"`
#echo "current Directory is "`pwd` 

###  MAKE IT EASY FOR CMS
###




	CM_GOOD_VALUE=0
	while [ $CM_GOOD_VALUE = 0 ]; do
		echo -n "Would you like to accept all the default settings? (y/n) : " 
		read -e CM_ANSWER
		if [ "$CM_ANSWER" = "y" ] || [ "$CM_ANSWER" = "n" ]; then
			let CM_GOOD_VALUE=1
		else
			echo "!!!   Please answer either y or n..."
		fi
	done


#################################################
if [ ! -d ./setup_scripts ]; then 
	mkdir ./setup_scripts
	
else
# check for multiple default settings.
	string_of_setting_loc=`ls setup_scripts | grep "default_settings" | sed -n "s/\default_settings_\(.*\)/\1/p"`
	list_of_setting_loc=($string_of_setting_loc)
    counter=1
    echo -e " \n"
    
    
	for i in ${list_of_setting_loc[@]}; do
		echo "       $counter). $i"
		let "counter += 1"
	done
	echo -e "\n\n"
	echo -n "Please enter the number next to the location you are in : "
	read -e LOC_ANSWER
	let "LOC_ANSWER += 0"
	if [ $LOC_ANSWER -lt $counter -a $LOC_ANSWER -gt 0 ]; then
		echo "good answer"	
		
		let "LOC_ANSWER -= 1"
		DEFAULT_SETTINGS_FILENAME="default_settings_${list_of_setting_loc[$LOC_ANSWER]}"
		echo "file is $DEFAULT_SETTINGS_FILENAME"
	else
		echo "bad answer >$LOC_ANSWER "
	fi

	
fi

echo "SELECT VERSION();" > setup_scripts/noop.sql

# exit

DB_TEST=0
DB_ASKED=0
XMLRPC_TEST=0


## CHECKING FOR TAR FILES, right now checking for all eventually just want to check for the needed ones
europaTar=`ls *.tgz 2>&1| grep "$EUROPA_TARDIR_ID"`
clientTar=`ls *.tgz 2>&1| grep "$CLIENT_TARDIR_ID"`
#supportTar=`ls *.tgz 2>&1| grep "$SUPPORT_TARDIR_ID"`

if [ "$europaTar" = "" -o "$clientTar" = "" ]; then
	echo -e "\n\n\n      !! ERROR CANNOT RUN INSTALL SCRIPT !!"
	echo "     Can't find the correct tar files, looking for files containing:"
	echo -e "     -- \"$EUROPA_TARDIR_ID\"\n     -- \"$CLIENT_TARDIR_ID\""
#	exit
fi 


# READ IN DEFAULTS
DEFAULT_PARAMS=`sed -n "s/\$\(.*\)=\(.*\);/\1\\=\2/p" setup_scripts/$DEFAULT_SETTINGS_FILENAME`
c=($DEFAULT_PARAMS)

for i in ${c[@]}; do
	eval $i
	#echo $i
done

# READ IN USER SETTINGS
if [ -f setup_scripts/user_settings ]; then

	USER_PARAMS=`sed -n "s/\$\(.*\)=\(.*\);/\1\\=\2/p" setup_scripts/user_settings`
	d=($USER_PARAMS)

	for j in ${d[@]}; do
		eval $j
	#	echo $j
	done
fi

function menu_select {
	GOOD_VALUE=0
	
	if [ "$CM_ANSWER" = "n" ]; then
	
	while [ $GOOD_VALUE = 0 ]; do
		echo -n "$1 [$3]: " 
		read -e ANSWER
		if [ "$ANSWER" = "y" ] || [ "$ANSWER" = "n" ]; then
			let GOOD_VALUE=1
		elif [ "$ANSWER" = "" ]; then
			let GOOD_VALUE=1
			ANSWER=$3
			#echo "default selected"
		else
			echo "!!!   Please answer either y or n..."
		fi
	done
	eval $2=\$ANSWER
	else
	
		eval $2=\$3
	
	fi
}


function basic_prompt {
	#echo  "$1"
	if [ "$CM_ANSWER" = "n" ]; then
	read -e -p "$1 [$3]: " ANSWER
	DEFAULT=$3
	if [ "$ANSWER" = "" ]; then
		ANSWER=$3
	fi
	eval $2=\$ANSWER
	if [ "$ANSWER" != "" ]; then
		echo "\$$2=\"$ANSWER\";" 1>>setup_scripts/user_settings
	fi
	else
		eval $2=\$3
	fi
	
}

echo -e "\n\n\n\n\n\n"
echo ""
echo "***************************************"
echo "         PSI INSTALL SCRIPT"
echo "***************************************"
echo ""
echo -e "This is the install script for Europa, PSI Database and PSI Client. \nIf you do not wish to proceed press control C at any time\n\n\n"
echo ""
echo "--------------------------------------"
echo "         PSI INSTALL SETTINGS ..."
echo "--------------------------------------"
echo ""




menu_select "Would you like to install the Europa Server? (y/n)" INSTALL_EUROPA $INSTALL_EUROPA
menu_select "Would you like to update the PSI Database? (y/n)" UPDATE_DB $UPDATE_DB
menu_select "Would you like to install the Active MQ Server? (y/n)" INSTALL_ACTIVEMQ $INSTALL_ACTIVEMQ
#we are no longer supporting the installing the APCore Server.
#menu_select "Would you like to install the APCore Server? (y/n)" INSTALL_PAPS $INSTALL_PAPS
INSTALL_PAPS="n"
menu_select "Would you like to install the PSI Client? (y/n)" INSTALL_CLIENT $INSTALL_CLIENT
#menu_select "Would you like to install the MIPL Data Products? (y/n)" INSTALL_MIPL $INSTALL_MIPL
INSTALL_MIPL="n"
menu_select "Would you like to install the Message Reactor and Cataloger? (y/n)" INSTALL_MSGRX $INSTALL_MSGRX

##### GENERAL QUESTIONS
USER_SETTINGS_FILE=setup_scripts/user_settings
echo "\$INSTALL_EUROPA=\"$INSTALL_EUROPA\";" 1> $USER_SETTINGS_FILE
echo "\$UPDATE_DB=\"$UPDATE_DB\";" 1>> $USER_SETTINGS_FILE
echo "\$INSTALL_ACTIVEMQ=\"$INSTALL_ACTIVEMQ\";" 1>> $USER_SETTINGS_FILE
echo "\$INSTALL_PAPS=\"$INSTALL_PAPS\";" 1>> $USER_SETTINGS_FILE
echo "\$INSTALL_CLIENT=\"$INSTALL_CLIENT\";" 1>> $USER_SETTINGS_FILE
echo "\$INSTALL_MIPL=\"$INSTALL_MIPL\";"  1>> $USER_SETTINGS_FILE
echo "\$INSTALL_MSGRX=\"$INSTALL_MSGRX\";" 1>> $USER_SETTINGS_FILE

#### Some defaults we are not asking for yet
EUROPA_MODEL="phx"
EUROPA_MAXSTEPS="1000"
EUROPA_STRICT="false"
ADVISOR_DEFAULT="Europa"

# General settings that we will need
if [ "$INSTALL_EUROPA" = "y" ]; then
	basic_prompt "What is the path to the Europa Root?" EUROPA_ROOT $EUROPA_ROOT
# remove the trailing slash if it exists just to make things consistant, other option is to put it in
	if [ `echo ${EUROPA_ROOT:${#EUROPA_ROOT}-1}` = "/" ]; then
		EUROPA_ROOT=${EUROPA_ROOT:0:${#EUROPA_ROOT}-1}
		#echo "$EUROPA_ROOT"
	fi
	
	GOOD_VALUE=0
		if [ "$CM_ANSWER" = "n" ]; then
	while [ $GOOD_VALUE = 0 ]; do
		read -e -p  "What is the Root Shell Environment (bash/tsch)? [$ROOT_SHELL_ENV]: " ROOT_SHELL_ENV_IN 
		if [ "$ROOT_SHELL_ENV_IN" = "bash" ] || [ "$ROOT_SHELL_ENV_IN" = "tsch" ]; then
			let GOOD_VALUE=1
		elif [ "$ROOT_SHELL_ENV_IN" = "" ]; then
			let GOOD_VALUE=1
			ROOT_SHELL_ENV_IN=$ROOT_SHELL_ENV	
		else
			echo "You must type in either bash or tsch"
		fi
	done
	ROOT_SHELL_ENV=$ROOT_SHELL_ENV_IN
		
	fi
	
	echo "\$ROOT_SHELL_ENV=\"$ROOT_SHELL_ENV\";" 1>> $USER_SETTINGS_FILE
	
	basic_prompt "What is the Europa Logfile File Path?" EUROPA_LOGFILE_PATH $EUROPA_LOGFILE_PATH
	basic_prompt "What is the Europa Log Directory Path?" EUROPA_LOGDIRECTORY_PATH $EUROPA_LOGDIRECTORY_PATH
	basic_prompt "What is the Initial State File Path?" INITIAL_STATE_PATH $INITIAL_STATE_PATH
	basic_prompt "What is the Europa Server Host Name?" APP_SERVER_HOSTNAME $APP_SERVER_HOSTNAME
	basic_prompt "What is the Europa Server Port Number?" EUROPA_PORT $EUROPA_PORT	
	basic_prompt "What is the Europa Server Child Port Number?" EUROPA_CHILD_PORT $EUROPA_CHILD_PORT	
	basic_prompt "What is the Europa Server User Name?" EUROPA_USER $EUROPA_USER
	basic_prompt "What User should have Owner Permissions for the Europa Server Files?" EUROPA_PERMISSION_USER $EUROPA_PERMISSION_USER
	basic_prompt "What Group should have Group Permissions for the Europa Server Files?" EUROPA_PERMISSION_GROUP $EUROPA_PERMISSION_GROUP
	
	
	XMLRPC_TEST=1;
	
else 
	# write the values to the file anyway
	echo "\$EUROPA_ROOT=\"$EUROPA_ROOT\";"  1>> $USER_SETTINGS_FILE
	echo "\$ROOT_SHELL_ENV=\"$ROOT_SHELL_ENV\";"  1>> $USER_SETTINGS_FILE
	echo "\$EUROPA_LOGFILE_PATH=\"$EUROPA_LOGFILE_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$EUROPA_LOGDIRECTORY_PATH=\"$EUROPA_LOGDIRECTORY_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$INITIAL_STATE_PATH=\"$INITIAL_STATE_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$APP_SERVER_HOSTNAME=\"$APP_SERVER_HOSTNAME\";"  1>> $USER_SETTINGS_FILE
	echo "\$EUROPA_PORT=\"$EUROPA_PORT\";"  1>> $USER_SETTINGS_FILE
	echo "\$EUROPA_CHILD_PORT=\"$EUROPA_CHILD_PORT\";"  1>> $USER_SETTINGS_FILE
	echo "\$EUROPA_USER=\"$EUROPA_USER\";"  1>> $USER_SETTINGS_FILE
	echo "\$EUROPA_PERMISSION_USER=\"$EUROPA_PERMISSION_USER\";" 1>> $USER_SETTINGS_FILE
	echo "\$EUROPA_PERMISSION_GROUP=\"$EUROPA_PERMISSION_GROUP\";" 1>> $USER_SETTINGS_FILE
fi

# MYSQL Settings to ask for

if [ "$UPDATE_DB" = "y" ]; then

	mySqlCheck=0
	while [ $mySqlCheck = 0 ]; do
		basic_prompt "What is the Database Host Name?" DB_HOST $DB_HOST
		basic_prompt "What is the Database Port Number?" DB_PORT $DB_PORT
		basic_prompt "What is the Database ROOT User Name ** User must exist ** ?" DB_USER $DB_USER
		basic_prompt "What is the Database ROOT Password? " DB_PASSWORD $DB_PASSWORD
		MYSQL_CHECK=`mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD < setup_scripts/noop.sql 2>&1`
		MYSQL_Checker=`expr "$MYSQL_CHECK" : '\(.*[(][)]\)'`
		#echo $MYSQL_Checker
		if [ "$MYSQL_Checker" = "VERSION()" ]; then
			mySqlCheck=1;
		else
			echo "          !!!     Unable to connect to Database Server. "
			echo "          !!!     Please check your database settings again."
			echo "          !!!     Check that the ROOT User has enough permissions."
			#echo "          !!!     Please check your database settings again."
		fi
		
	done
	
	basic_prompt "What is the Database Admin User Name" DB_ADMIN $DB_ADMIN
	basic_prompt "What is the Database Admin Password" DB_ADMIN_PASSWORD $DB_ADMIN_PASSWORD
	basic_prompt "What is the Database Client User Name" DB_CLIENT $DB_CLIENT
	basic_prompt "What is the Database Client Password" DB_CLIENT_PASSWORD $DB_CLIENT_PASSWORD
	basic_prompt "What is the Planning Database Name?" DB_NAME $DB_NAME
	basic_prompt "What is the Usage Database Name?" USAGE_DB_NAME $USAGE_DB_NAME
	# better thing to do is check if it exists and then ask
	basic_prompt "What is the Planning Database Dump File Name (for backing up the current DB)?" MAIN_DB_DUMPFILE $MAIN_DB_DUMPFILE
	basic_prompt "What is the Usage Database Dump File Name (for backing up the current DB)?" USAGE_DB_DUMP_FILE $USAGE_DB_DUMP_FILE
	DB_TEST=1;
	DB_ASKED=1;
else
	echo "\$DB_HOST=\"$DB_HOST\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_PORT=\"$DB_PORT\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_USER=\"$DB_USER\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_PASSWORD=\"$DB_PASSWORD\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_ADMIN=\"$DB_ADMIN\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_ADMIN_PASSWORD=\"$DB_ADMIN_PASSWORD\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_CLIENT=\"$DB_CLIENT\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_CLIENT_PASSWORD=\"$DB_CLIENT_PASSWORD\";"  1>> $USER_SETTINGS_FILE
	echo "\$DB_NAME=\"$DB_NAME\";"  1>> $USER_SETTINGS_FILE
	echo "\$USAGE_DB_NAME=\"$USAGE_DB_NAME\";"  1>> $USER_SETTINGS_FILE
	echo "\$USAGE_DB_DUMP_FILE=\"$USAGE_DB_DUMP_FILE\";"  1>> $USER_SETTINGS_FILE
fi

# APCORE SETTINGS to ask for
if [ "$INSTALL_PAPS" = "z" ]; then
	
	basic_prompt "What is the path to APCore?" APCORE_PATH $APCORE_PATH
	if [ `echo ${APCORE_PATH:${#APCORE_PATH}-1}` = "/" ]; then
		APCORE_PATH=${APCORE_PATH:0:${#APCORE_PATH}-1}
		#echo "$APCORE_PATH"
	fi
	basic_prompt "What is the path to the APCore log file?" APCORE_LOG $APCORE_LOG
	basic_prompt "What is the APCore username?" APCORE_USR $APCORE_USR
else
	echo "\$APCORE_PATH=\"$APCORE_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$APCORE_LOG=\"$APCORE_LOG\";"  1>> $USER_SETTINGS_FILE
	echo "\$APCORE_USR=\"$APCORE_USR\";"  1>> $USER_SETTINGS_FILE
fi


# Active MQ Settings to ask for
if [ "$INSTALL_ACTIVEMQ" = y ]; then
	if [ "$INSTALL_EUROPA" = "n" ] && [ "$INSTALL_PAPS" = "n" ]; then
		basic_prompt "What is the Application Server Host Name?" APP_SERVER_HOSTNAME $APP_SERVER_HOSTNAME
	fi
	basic_prompt "What directory should the JMS Server(ActiveMQ) be installed in?" JMS_INSTALLPATH $JMS_INSTALLPATH
	if [ `echo ${JMS_INSTALLPATH:${#JMS_INSTALLPATH}-1}` = "/" ]; then
		JMS_INSTALLPATH=${JMS_INSTALLPATH:0:${#JMS_INSTALLPATH}-1}
	fi
	
	basic_prompt "What directory should the JMS Server(ActiveMQ) logs be written to (must be writable)?" JMS_LOGPATH $JMS_LOGPATH
	basic_prompt "What user should the JMS Server(ActiveMQ) be run as?" JMS_USR $JMS_USR
else
	echo "\$JMS_INSTALLPATH=\"$JMS_INSTALLPATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$JMS_LOGPATH=\"$JMS_LOGPATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$JMS_USR=\"$JMS_USR\";"  1>> $USER_SETTINGS_FILE
	
fi


# CLIENT SETTINGS to ask for

if [ "$INSTALL_CLIENT" = "y" ]; then
	if [ "$INSTALL_EUROPA" = "n" ] && [ "$INSTALL_PAPS" = "n" ]; then
		basic_prompt "What is the Europa Server Host Name?" APP_SERVER_HOSTNAME $APP_SERVER_HOSTNAME
		basic_prompt "What is the Europa Server Port Number?" EUROPA_PORT $EUROPA_PORT	
		#basic_prompt "What is the Europa Server User Name?" EUROPA_USER $EUROPA_USER
        	
		XMLRPC_TEST=1;
	fi
	if [ "$UPDATE_DB" = "n" ]; then
		basic_prompt "What is the Database Host Name?" DB_HOST $DB_HOST
		basic_prompt "What is the Database Port Number?" DB_PORT $DB_PORT
		basic_prompt "What is the Database Client User Name" DB_CLIENT $DB_CLIENT
		basic_prompt "What is the Database Client Password" DB_CLIENT_PASSWORD $DB_CLIENT_PASSWORD
		DB_USER=$DB_CLIENT
		DB_PASSWORD=$DB_CLIENT_PASSWORD
		basic_prompt "What is the Planning Database Name?" DB_NAME $DB_NAME
		basic_prompt "What is the Usage Database Name?" USAGE_DB_NAME $USAGE_DB_NAME
		#DB_TEST=1;
		DB_ASKED=1;
	fi
	basic_prompt "What is the APCore Server Port Number?" APCORE_PORT $APCORE_PORT
	basic_prompt "What is the Path to the Latest PAD?" PAD_LATEST_PATH $PAD_LATEST_PATH
	basic_prompt "What is the Path to the APGEN user-defined libraries (libudef.so)?" APGEN_LIBS_PATH $APGEN_LIBS_PATH
	basic_prompt "What is the Path to the PAPS Init Script?" APGEN_INITSCRIPT_PATH $APGEN_INITSCRIPT_PATH
	basic_prompt "What is the Client Path?" CLIENT_PATH $CLIENT_PATH
	basic_prompt "What is the Local Cache Directory?" CLIENT_LOCAL_CACHE_DIRECTORY $CLIENT_LOCAL_CACHE_DIRECTORY
	basic_prompt "What port is JMS(ActiveMQ) running on?" CLIENT_JMS_PORT $CLIENT_JMS_PORT
	
	
	basic_prompt "What User should have Owner Permissions for PSI?" PSI_PERMISSION_USER $PSI_PERMISSION_USER
	basic_prompt "What Group should have Group Permissions for PSI?" PSI_PERMISSION_GROUP $PSI_PERMISSION_GROUP

	if [ `echo ${CLIENT_PATH:${#CLIENT_PATH}-1}` = "/" ]; then
		CLIENT_PATH=${CLIENT_PATH:0:${#CLIENT_PATH}-1}
		#echo "$CLIENT_PATH"
	fi
else
	echo "\$PAD_LATEST_PATH=\"$PAD_LATEST_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$APGEN_LIBS_PATH=\"$APGEN_LIBS_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$APGEN_INITSCRIPT_PATH=\"$APGEN_INITSCRIPT_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$CLIENT_PATH=\"$CLIENT_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$PSI_PERMISSION_USER=\"$PSI_PERMISSION_USER\";" 1>> $USER_SETTINGS_FILE
	echo "\$PSI_PERMISSION_GROUP=\"$PSI_PERMISSION_GROUP\";" 1>> $USER_SETTINGS_FILE
	echo "\$CLIENT_LOCAL_CACHE_DIRECTORY=\"$CLIENT_LOCAL_CACHE_DIRECTORY\";" 1>> $USER_SETTINGS_FILE	
	if [ `echo ${CLIENT_PATH:${#CLIENT_PATH}-1}` = "/" ]; then
		CLIENT_PATH=${CLIENT_PATH:0:${#CLIENT_PATH}-1}
		#echo "$CLIENT_PATH"
	fi
fi

if [ "$INSTALL_MSGRX" = "y" ]; then
	if [ "$DB_ASKED" = "0"  ]; then
		basic_prompt "What is the Database Host Name?" DB_HOST $DB_HOST
		basic_prompt "What is the Database Port Number?" DB_PORT $DB_PORT
		basic_prompt "What is the Database Client User Name" DB_CLIENT $DB_CLIENT
		basic_prompt "What is the Database Client Password" DB_CLIENT_PASSWORD $DB_CLIENT_PASSWORD
		DB_USER=$DB_CLIENT
		DB_PASSWORD=$DB_CLIENT_PASSWORD
		basic_prompt "What is the Planning Database Name?" DB_NAME $DB_NAME
		basic_prompt "What is the Usage Database Name?" USAGE_DB_NAME $USAGE_DB_NAME
		#DB_TEST=1;
	fi
	basic_prompt "What is the path to the message reactor?" MSGRX_PATH $MSGRX_PATH
	basic_prompt "What is the name of the J2EE server?" MSGRX_J2EE_SERVER $MSGRX_J2EE_SERVER
	basic_prompt "What is the pipeline server?" PIPE_SERVER $PIPE_SERVER
	#remove the trailing slash
	if [ `echo ${MSGRX_PATH:${#MSGRX_PATH}-1}` = "/" ]; then
		MSGRX_PATH=${MSGRX_PATH:0:${#MSGRX_PATH}-1}
		#echo "$MSGRX_PATH"
	fi
	
	basic_prompt "Who should recieve email if there is an error in the Message Reactor?" MSGRX_EMAIL $MSGRX_EMAIL
	basic_prompt "What mail server should be used to send the emails from the Mesage Reactor?" MSGRX_EMAIL_SERVER $MSGRX_EMAIL_SERVER
else
	echo "\$MSGRX_PATH=\"$MSGRX_PATH\";"  1>> $USER_SETTINGS_FILE
	echo "\$MSGRX_EMAIL=\"$MSGRX_EMAIL\";"  1>> $USER_SETTINGS_FILE
	echo "\$MSGRX_EMAIL_SERVER=\"$MSGRX_EMAIL_SERVER\";"  1>> $USER_SETTINGS_FILE
fi
echo ""
echo "--------------------------------------"
echo "         TESTING CONFIG....."
echo "--------------------------------------"
echo ""

if [ "$XMLRPC_TEST" = "1" ]; then
# ADD QUESTION ABOUT WHICH PERL TO USE
	RPC_PRECHECK=`perl -MRPC::XML -e "print "`
	if [ "$RPC_PRECHECK" != "" ]; then
		echo "Missing the RPC::XML Perl Module. Please Install it before proceeding with the install"
		exit 1
	else
		echo "          PERL RPC::XML Module ... SUCCESS!"
	fi

fi

if [ "$DB_TEST" = "1" ]; then 
	
	MYSQL_CHECK=`mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD < setup_scripts/noop.sql 2>&1`
	MYSQL_Checker=`expr "$MYSQL_CHECK" : '\(.*[(][)]\)'`
	#echo $MYSQL_Checker
	if [ "$MYSQL_Checker" != "VERSION()" ]; then
		echo "          DATABASE CONNECTION ... FAILED :( "
		echo "          !!!     Unable to connect to Database Server. "
		echo "          !!!     Please check your database settings again."
		echo "          !!!     Make sure the User, database and permissions are set correctly."
		exit 1
	else
		echo "          DATABASE CONNECTION ... SUCCESS!"
	fi
fi

if [ "$INSTALL_CLIENT" = "y" ]; then
	echo -n "          CHECKING JAVA VERSION ... "
	testingJavaVersion=`java -fullversion 2>&1 | sed -n "s/.*\"\([0-9][.][0-9]\).*/\1/p"`
	minJavaVersion=1.5
	javaTestResult=`expr $minJavaVersion \> $testingJavaVersion`
	if [ $javaTestResult -eq 1 ]; then
		echo "FAILED"
		echo "          Java version $testingJavaVersion, Java $minJavaVersion is required"
		exit
	else
		echo "COMPLETE"
	fi
fi



#what other things do I need to check for?




	echo ""
	echo "--------------------------------------"
	echo "         UNTARing FILES....."
	echo "--------------------------------------"
	echo ""
# extract the tar files
tarfile=`ls *.tgz ` 
tarList=($tarfile)



for aFile in ${tarList[@]}; do
	echo -n "       * UNTARING $aFile ... "
	tar -xzf "$aFile"
	echo "COMPLETED"
	
done

# determine which directory is which

baseDir=`pwd`
europaDir=`ls -d */| grep "$EUROPA_TARDIR_ID"`
clientDir=`ls -d */| grep "$CLIENT_TARDIR_ID"`
supportDir=""



function replace_config_file_setting {
#  NOTE: SInce file names use the / in the path i've decided to use the ^ 
#		however it can be replace by any other single character if needed
#$1 is the file path
#$2 is the variable we're replacing
#$3 is the value that we're replacing it with
	configchanged=`sed "s^$2.*^$2$3^" "$1"`
	
	#echo "$configchanged"
	echo "$configchanged" 1> "$1"
}



#START EUROPA STUFF
if [ "$INSTALL_EUROPA" = "y" ]; then
	echo ""
	echo "--------------------------------------"
	echo "         INSTALLING EUROPA....."
	echo "--------------------------------------"
	echo ""


	if [ "$ROOT_SHELL_ENV" = "bash" ]; then
		whichbash=`which bash`
		#echo "Europa Dir is: $europaDir"
		echo -e "#!$whichbash\nexport EUROPA_ROOT=$EUROPA_ROOT\nexport LD_LIBRARY_PATH=\"\$EUROPA_ROOT\"/DynamicEuropa/lib:\"\$EUROPA_ROOT\"/PLASMA/lib:/:\"\$LD_LIBRARY_PATH\"" > "$baseDir"/"$europaDir"plasma-environment-runtime
	else
		echo -e "#!/usr/local/bin/tcsh\nsetenv EUROPA_ROOT \"$EUROPA_ROOT\"\nsetenv LD_LIBRARY_PATH \"\$EUROPA_ROOT\"/DynamicEuropa/lib:\"\$EUROPA_ROOT\"/PLASMA/lib:/:\"\$LD_LIBRARY_PATH\"" > "$baseDir"/"$europaDir"plasma-environment-runtime
	fi
	chmod a+x "$baseDir"/"$europaDir"plasma-environment-runtime
	whichbash=`which bash`
	if [ `echo ${EUROPA_LOGDIRECTORY_PATH:${#EUROPA_LOGDIRECTORY_PATH}-1}` = "/" ]; then
		EUROPA_LOGDIRECTORY_PATH=${EUROPA_LOGDIRECTORY_PATH:0:${#EUROPA_LOGDIRECTORY_PATH}-1}
		#echo "$CLIENT_PATH"
	fi
	echo -e "port $EUROPA_PORT\n@config phx\nphx Phoenix/xmlrpcconfig.txt\ndefault phx\nlogdir $EUROPA_LOGDIRECTORY_PATH\ndontdie 0" > "$baseDir"/"$europaDir"DynamicEuropa/serverconfig.txt

	# replace the log stuff
	xmlrpcconfigFilePath="$baseDir"/"$europaDir"DynamicEuropa/Phoenix/xmlrpcconfig.txt

	replace_config_file_setting $xmlrpcconfigFilePath "log " $EUROPA_LOGFILE_PATH
	replace_config_file_setting $xmlrpcconfigFilePath "port " $EUROPA_PORT
	replace_config_file_setting $xmlrpcconfigFilePath "initial_state " $INITIAL_STATE_PATH
	replace_config_file_setting $xmlrpcconfigFilePath "child_host " $APP_SERVER_HOSTNAME
	replace_config_file_setting $xmlrpcconfigFilePath "child_port " $EUROPA_CHILD_PORT

	echo -e "MAILTO=\"\"\n0,5,10,15,20,25,30,35,40,45,50,55 * * * * ${EUROPA_ROOT}CheckStatus.cron  >> ${EUROPA_LOGDIRECTORY_PATH}/Cron.log 2>&1\n#* 2 * * 0 /msop/PSI/EuropaServer/CleanUp.cron  >> /msop/PSI/EuropaServer/Cron.log 2>&1" > "$europaDir"/EuropaServer.crontab

	
	child_command="./Phoenix_DynamicEuropa_o_rt"
	replace_config_file_setting $xmlrpcconfigFilePath "child_command " $child_command
	child_absolute_path="$EUROPA_ROOT"/DynamicEuropa/Phoenix
	replace_config_file_setting $xmlrpcconfigFilePath "child_absolute_path " $child_absolute_path

	
	#We should move it but for now we'll run it where it sits
	#this should test it
	#echo `."$child_command $EUROPA_PORT"`
	
	echo -n "      * CHECKING for $EUROPA_ROOT ... "
	if [ -d "$EUROPA_ROOT" ]; then
		echo "EXISTS"
	else
		mkdir -p "$EUROPA_ROOT"
		echo "CREATED"
	fi
	
	echo -n "      * COPYING CLIENT TO GIVEN EUROPA PATH ... "
	cp -pR "$europaDir"* "$EUROPA_ROOT"
	
	echo "COMPLETED"
	

	Europa_Logs_Dir=${EUROPA_LOGDIRECTORY_PATH}/
	echo -n "      * CHECKING for logs directory ... "
	if [ -d $Europa_Logs_Dir ]; then
		echo "EXISTS"
	else 
		mkdir $Europa_Logs_Dir
		echo "CREATED"
	fi
	`chmod g+rw $Europa_Logs_Dir`
	# make sure to copy stuff to the new directory but that should be easy with a cp command
	
	echo -n "      * CREATING startEuropa.sh ... "
	`echo -e "#!$whichbash\n\ncd $EUROPA_ROOT\n\n source plasma-environment-runtime\n\ncd DynamicEuropa\n\n nohup perl server_manager &" > $EUROPA_ROOT/startEuropa.sh`
	EUROPA_StartEuropa_check=`chmod a+x $EUROPA_ROOT/startEuropa.sh`	
	echo "CREATED"
	
	if [ "$EUROPA_PERMISSION_USER" = "" ]; then
		EUROPA_PERMISSION_USER=`whoami`
	fi
	#if [ "$EUROPA_PERMISSION_GROUP" = "" ]; then
		#chown -R $EUROPA_PERMISSION_USER $EUROPA_ROOT 
	#else
		#chown -R $EUROPA_PERMISSION_USER:$EUROPA_PERMISSION_GROUP $EUROPA_ROOT
	#fi
	chmod -R g+rwx $EUROPA_ROOT
	
	
	if [ $IsRootCheck = "y" ]; then
	
		echo -n "      * CHECKING for init.d ... "
		if [ -d /etc/init.d ]; then
			echo "EXISTS"
		else 
			mkdir /etc/init.d
			echo "CREATED"
		fi
		#find the europa.for.init.d file
		cp -f "$baseDir"/"$europaDir"europa.for.init.d.bak "$baseDir"/"$europaDir"europa.for.init.d
		europaforinitd="$baseDir"/"$europaDir"europa.for.init.d
		replace_config_file_setting $europaforinitd "EUROPA_DIR=" "$EUROPA_ROOT/DynamicEuropa/Phoenix"
		replace_config_file_setting $europaforinitd "EUROPA_USR=" "$EUROPA_USER"
		replace_config_file_setting $europaforinitd "EUROPA_PRE=" "$EUROPA_ROOT/plasma-environment-runtime"
	
		
		cp "$europaDir"/europa.for.init.d /etc/init.d/europa
		echo "      * SETTING PERMISSIONS FOR /etc/init.d/europa"
		chown root /etc/init.d/europa
		chgrp root /etc/init.d/europa
		chmod 755 /etc/init.d/europa
		
		# find the activemq.for.init.d file
		#activemqforinitd="$baseDir"/activemq.for.init.d

		
	fi	
	
	# might want to add a start, status, stop check here
fi


if [ "$UPDATE_DB" = "y" ]; then
	echo ""
	echo "--------------------------------------"
	echo "         UPDATING THE DATABASE"
	echo "--------------------------------------"
	echo ""


	echo "      * BACKING UP EXISTING DATABASES"
	`mysqldump --user=$DB_USER --host=$DB_HOST --port=$DB_PORT --password=$DB_PASSWORD --result-file=$MAIN_DB_DUMPFILE $DB_NAME`
	`mysqldump --user=$DB_USER --host=$DB_HOST --port=$DB_PORT --password=$DB_PASSWORD --result-file=$USAGE_DB_DUMP_FILE $USAGE_DB_NAME`
	echo "      * UPDATING DATABASES"
	# create the make database files along with other stuff
	echo -e "DROP DATABASE IF EXISTS $DB_NAME;\nDROP DATABASE IF EXISTS $USAGE_DB_NAME;" > setup_scripts/createDB.sql
	echo -e "create database $DB_NAME;\ncreate database $USAGE_DB_NAME;" >> setup_scripts/createDB.sql
	echo -e "GRANT ALL ON $DB_NAME.* TO $DB_ADMIN@'%' IDENTIFIED BY \"$DB_ADMIN_PASSWORD\";\nGRANT ALL ON $USAGE_DB_NAME.* TO $DB_ADMIN@'%' IDENTIFIED BY \"$DB_ADMIN_PASSWORD\";" >> setup_scripts/createDB.sql
	clientRights="DELETE, INSERT, SELECT, UPDATE"
	echo -e "GRANT $clientRights ON $DB_NAME.* TO $DB_CLIENT@'%' IDENTIFIED BY \"$DB_CLIENT_PASSWORD\";\nGRANT $clientRights ON $USAGE_DB_NAME.* TO $DB_CLIENT@'%' IDENTIFIED BY \"$DB_CLIENT_PASSWORD\";\n flush privileges;" >> setup_scripts/createDB.sql
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD < setup_scripts/createDB.sql 2>&1`
	planningDBsql=`ls sql | grep ".*PHOENIX-DUMP.*"`
	usageDBsql=`ls sql | grep ".*PHOENIX-USAGE-DUMP.*"`
	#echo "mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < $planningDBsql"
	#echo "mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $USAGE_DB_NAME < $usageDBsql"
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < sql/$planningDBsql`
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $USAGE_DB_NAME < sql/$usageDBsql`
	
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < sql/GDS411_STEP-1_NamedLocationGroup_SCHEMA.sql`
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < sql/GDS411_STEP-2_TargetSCHEMA_minus_PP.sql`
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < sql/GDS411_STEP-3_NamedLocationGroup_INIT.sql`
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < sql/GDS411_STEP-4_DataProduct_SCHEMA.sql`
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < sql/GDS411_STEP-5_Frame_SCHEMA_PLUS_DATA.sql`
	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < sql/GDS411_STEP-6_PlanningPosition_SCHEMA_PLUS_DATA.sql`
	
fi


if [ "$INSTALL_PAPS" = "y" ]; then
	echo ""
	echo "--------------------------------------"
	echo "         INSTALLING PAPS (not really) "
	echo "--------------------------------------"
	echo ""
#	echo "installing PAPS"
	# I'm not sure what the steps are supposed to be but i'll press on
#	PAPSTar=`ls $supportDir | grep "APcoreServer.*"`
#	cd "$supportDir"
#	tar -xvf "$PAPSTar"
#
#	
#	#check for the APCore directory, if it doesn't exist then make it
#	if [ -d "$APCORE_PATH" ]; then
#		echo "$APCORE_PATH EXISTS"
#	else
#		mkdir -p "$APCORE_PATH"
#	fi
#	
#	# change the APcoreServer/plugins/gov.nasa.ensemble.core.apcore.server_1.0.0/apcore.properties
#	apcoreServerDir=`ls -d */| grep "APcore"`
#	apcorePluginDir=`ls $apcoreServerDir/APcoreServer/plugins/ | grep "core.apcore.server"
#	# now that we know what's what we can edit the config file itself
#	apcorePropertiesFilePath="$apcoreServerDir/APcoreServer/plugins/$apcorePluginDir/apcore.properties"
#	replace_config_file_setting $apcorePropertiesFilePath "apcore.session.exec=" "$APCORE_PATH/bin/linux-3.4.5/APcore"
#	apcoreForInitDFilePath="$apcoreServerDir/apcore.for.init.d"
#	replace_config_file_setting $apcoreForInitDFilePath "APCORE_DIR=" $APCORE_PATH
#	replace_config_file_setting $apcoreForInitDFilePath "APCORE_LOG=" $APCORE_LOG
#	replace_config_file_setting $apcoreForInitDFilePath "APCORE_USR=" $APCORE_USR
#	if [ -d /etc/init.d ]; then
#		echo "init.d exists"
#	else 
#		mkdir /etc/init.d
#	fi
#	cp $apcoreForInitDFilePath /etc/init.d/apcore
#	chown root /etc/init.d/apcore
#	chgrp root /etc/init.d/apcore
#	chmod 755 /etc/init.d/apcore
#	/sbin/chkconfig --add apcore
#	checkconfigOutput=`/sbin/chkconfig --list | grep apcore | sed  "s/\(\S*\)\s*/\1 /g"`
#	if [ "$checkconfigOutput" = "apcore 0:off 1:off 2:off 3:off 4:off 5:on 6:off " ]; then
#		echo "APcore config set correctly"
#	else
#		echo "APcore NOT SET UP CORRECTLY it is: >$checkconfigOutput<"
#	fi
#	
#	cp -pR "$europaDir"EuropaServer/* "$APCORE_PATH"
#	
#	
#	echo `/etc/init.d/apcore start`
#	echo `/etc/init.d/apcore stop`
#	echo `/etc/init.d/apcore status`
#	cd ..
fi



if [ "$INSTALL_ACTIVEMQ" = "y" ]; then
	# copy over the files
	
	echo ""
	echo "--------------------------------------"
	echo "         INSTALLING ACTIVE MQ "
	echo "--------------------------------------"
	echo ""	
	
		echo -n "      * REMOVING THE OLD INSTALL OF ACTIVE MQ from /msop/activemq/* ..."
		rm -rf /msop/activemq/*
		cd /msop/activemq
		# untar the download
		
		rm -rf /msop/pes_rw/activemq
		mkdir -p /msop/pes_rw/activemq
		
		
		echo -n "      * EDITING ACTIVE MQ FOR INIT D in $baseDir ... "
		cp -f "$baseDir"/activemq.for.init.d.bak "$baseDir"/activemq.for.init.d
		activemqforinitd="$baseDir"/activemq.for.init.d

		#replace_config_file_setting $activemqforinitd "ACTIVE_MQ_LOG=" "$JMS_LOGPATH"
		#replace_config_file_setting $activemqforinitd "ACTIVE_MQ_DIR=" "$JMS_INSTALLPATH/bin"
		#replace_config_file_setting $activemqforinitd "ACTIVE_MQ_USR=" "$JMS_USR"
	
	
		mkdir -p "$JMS_LOGPATH"
		if [ $IsRootCheck = "y" ]; then
			cp activemqforinitd /etc/init.d/activemq
			echo "      * SETTING PERMISSIONS FOR /etc/init.d/activemq"
			chown root /etc/init.d/activemq
			chgrp root /etc/init.d/activemq
			chmod 755 /etc/init.d/activemq
		fi
		
		echo -n "      * COPYING ACTIVE MQ to $JMS_INSTALLPATH ... "
#		cd "$JMS_INSTALLPATH" 		
#		tar -xvzf "$baseDir"/activemq.tgz

		cd "$baseDir"		
		cp -R activemq/* "$JMS_INSTALLPATH"
		echo "COMPLETE"
fi



if [ "$INSTALL_CATALOGUE" = "y" ]; then
	# copy over the files
	
	echo ""
	echo "--------------------------------------"
	echo "         INSTALLING CATALOGUE "
	echo "--------------------------------------"
	echo ""	
	
		echo -n "      * EDITING ACTIVE MQ FOR INIT D in $baseDir ... "
		cp -f "$baseDir"/activemq.for.init.d.bak "$baseDir"/activemq.for.init.d
		activemqforinitd="$baseDir"/activemq.for.init.d
		#replace_config_file_setting $activemqforinitd "ACTIVE_MQ_LOG=" "$JMS_LOGPATH"
		#replace_config_file_setting $activemqforinitd "ACTIVE_MQ_DIR=" "$JMS_INSTALLPATH/bin"
		#replace_config_file_setting $activemqforinitd "ACTIVE_MQ_USR=" "$JMS_USR"
	
	
		mkdir -p "$JMS_LOGPATH"
		if [ $IsRootCheck = "y" ]; then
			cp activemqforinitd /etc/init.d/activemq
			echo "      * SETTING PERMISSIONS FOR /etc/init.d/activemq"
			chown root /etc/init.d/activemq
			chgrp root /etc/init.d/activemq
			chmod 755 /etc/init.d/activemq
		fi
		
		echo -n "      * COPYING ACTIVE MQ to $JMS_INSTALLPATH ... "
#		cd "$JMS_INSTALLPATH" 		
#		tar -xvzf "$baseDir"/activemq.tgz

#		cd "$baseDir"		
		cp -R activemq/* "$JMS_INSTALLPATH"
		echo "COMPLETE"
fi



if [ "$INSTALL_CLIENT" = "y" ]; then
	echo ""
	echo "--------------------------------------"
	echo "         INSTALLING PSI"
	echo "--------------------------------------"
	echo ""
	#echo `pwd`
	###########################################
	# BELOW IS WHERE ALL THE CONFIG FILES FOR ENSEMBLE CAN BE FOUND!
	############################################
	ensemblePropertiesDirectory=`ls "$clientDir"/plugins/ | grep "gov\.nasa\.ensemble\.phoenix\.rcp_.*"`
	ensemblePropertiesFilePath="$clientDir"/plugins/"$ensemblePropertiesDirectory/ensemble.properties"	
	activemqPropertiesDirectory=`ls "$clientDir"/plugins/ | grep "gov\.nasa\.ensemble\.core\.jms*"`
	activemqPropertiesFilePath="$clientDir"plugins/"$activemqPropertiesDirectory/activemq_PSI.properties"
	
	echo "      * EDITING CLIENT CONFIG FILE"
	#echo $ensemblePropertiesFilePath
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.type.planning=" "mysql"
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.name.planning=" $DB_NAME
	DB_HOST_AND_PORT="$DB_HOST:$DB_PORT"
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.host.planning=" $DB_HOST_AND_PORT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.user.planning=" $DB_CLIENT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.pass.planning=" $DB_CLIENT_PASSWORD 

	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.type.dataproducts=" "mysql"
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.name.dataproducts=" $DB_NAME
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.host.dataproducts=" $DB_HOST_AND_PORT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.user.dataproducts=" $DB_CLIENT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.pass.dataproducts=" $DB_CLIENT_PASSWORD

	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.type.frame=" "mysql"
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.name.frame=" $DB_NAME
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.host.frame=" $DB_HOST_AND_PORT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.user.frame=" $DB_CLIENT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.pass.frame=" $DB_CLIENT_PASSWORD

	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.type.usage=" "mysql"
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.name.usage=" $USAGE_DB_NAME
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.host.usage=" $DB_HOST_AND_PORT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.user.usage=" $DB_CLIENT
	replace_config_file_setting $ensemblePropertiesFilePath "hibernate.database.pass.usage=" $DB_CLIENT_PASSWORD
	
	replace_config_file_setting $ensemblePropertiesFilePath "apcore.server.host=" $APP_SERVER_HOSTNAME 
	replace_config_file_setting $ensemblePropertiesFilePath "apcore.server.port=" $APCORE_PORT
	replace_config_file_setting $ensemblePropertiesFilePath "apcore.default.aaf=" $PAD_LATEST_PATH
	replace_config_file_setting $ensemblePropertiesFilePath "apcore.default.libudef=" $APGEN_LIBS_PATH
	replace_config_file_setting $ensemblePropertiesFilePath "apcore.default.init.script=" $APGEN_INITSCRIPT_PATH
	
	replace_config_file_setting $ensemblePropertiesFilePath "europa.host=" $APP_SERVER_HOSTNAME
	replace_config_file_setting $ensemblePropertiesFilePath "europa.port=" $EUROPA_PORT
	replace_config_file_setting $ensemblePropertiesFilePath "europa.modelname=" $EUROPA_MODEL
	replace_config_file_setting $ensemblePropertiesFilePath "europa.maxsteps=" $EUROPA_MAXSTEPS
	replace_config_file_setting $ensemblePropertiesFilePath "europa.stricttypes=" $EUROPA_STRICT

	replace_config_file_setting $ensemblePropertiesFilePath "advisor.default=" $ADVISOR_DEFAULT
	replace_config_file_setting $ensemblePropertiesFilePath "local.cache.directory.default=" $CLIENT_LOCAL_CACHE_DIRECTORY
	if [ ! -d "$CLIENT_LOCAL_CACHE_DIRECTORY" ]; then
		mkdir -p $CLIENT_LOCAL_CACHE_DIRECTORY
	fi
	
	ACTIVE_MQ_HOST_AND_PORT="tcp://${APP_SERVER_HOSTNAME}:${CLIENT_JMS_PORT}"
#	echo "      * Attempting to replace the file in $activemqPropertiesFilePath with $ACTIVE_MQ_HOST_AND_PORT"
	replace_config_file_setting $ensemblePropertiesFilePath "java.naming.provider.url=" $ACTIVE_MQ_HOST_AND_PORT
	
	#add unzip of MIPL data and cp it to the correct location= $CLIENT_LOCAL_CACHE_DIRECTORY

	#echo -n "      * Creating symlink of ensemble.properties file ... "
	#mv $ensemblePropertiesFilePath ${ensemblePropertiesFilePath}.movetopes_rw_slash_psi
	#ln -s /msop/pes_rw/psi/ensemble.properties ${clientDir}/plugins/${ensemblePropertiesDirectory}/ensemble.properties
	
	
	
	#copy the PSI install to wherever
	
	echo -n "      * CHECKING CLIENT PATH ... "
	
	if [ -d "$CLIENT_PATH" ]; then
		
		timestamp=`date +'%Y%m%e%H%M%S'`
		mv $CLIENT_PATH ${CLIENT_PATH}_old_${timestamp}
		mkdir -p "$CLIENT_PATH"
		echo "EXISTS"
	else
		mkdir -p "$CLIENT_PATH"
		echo "CREATED"
	fi
	
	echo -n "      * COPYING CLIENT TO GIVEN CLIENT PATH ... "
	cp -pR "$clientDir"/* "$CLIENT_PATH"
	
	if [ "$PSI_PERMISSION_USER" = "" ]; then
		PSI_PERMISSION_USER=`whoami`
	fi
	
	#if [ "$PSI_PERMISSION_GROUP" != "" ]; then
	 
	#	chown -R $PSI_PERMISSION_USER:$PSI_PERMISSION_GROUP $CLIENT_PATH
	#else 
	#	chown -R $PSI_PERMISSION_USER $CLIENT_PATH 
	#fi
	chmod -R g+rwx $CLIENT_PATH

	
	echo "COMPLETED"	
	
fi


if [ "$INSTALL_MIPL" = "y" ]; then

	#unzip the mipl stuff to the correct directory
	#run the mysql on the database
	# copy the mipl data over#
#	MIPLTAR=`ls | grep "MIPL"`
#	MIPLSQL=`ls | grep "DataProductCatalog"`
#	cd "$CLIENT_LOCAL_CACHE_DIRECTORY"
#	tar -xzf "$baseDir"/$MIPLTAR
	cd "$baseDir"
#	echo `mysql -u$DB_USER -h$DB_HOST -P$DB_PORT -p$DB_PASSWORD $DB_NAME < $MIPLSQL`
	
fi



if [ "$INSTALL_MSGRX" = "y" ]; then

	echo ""
	echo "--------------------------------------"
	echo "         INSTALLING MESSAGE REACTOR"
	echo "--------------------------------------"
	echo ""
	
msgrxbase="${baseDir}/psi_messagereactor"
confpl="${msgrxbase}/conf/config.pl"

msgxml="${msgrxbase}/conf/PSI_MessageReactor.xml"
msginitd="${msgrxbase}/psi_messagereactor.for.init.d"
pipelineconfig="${msgrxbase}/tools/do-pipeline.config"
catpropfilepath=`ls "${msgrxbase}"/tools/catalogPopulator/plugins/ | grep "gov\.nasa\.jpl\.maestro\.phoenix\.catalog\.config_.*"`
echo "$catpropfilepath"
catpropfile="${msgrxbase}/tools/catalogPopulator/plugins/${catpropfilepath}/ensemble.properties"
echo "$catpropfile"
builderpropfilepath=`ls "${msgrxbase}"/tools/phxMosaicBuilder/plugins/ | grep "gov\.nasa\.jpl\.orchestrator\.phoenix_.*"`
builderpropfile="${msgrxbase}/tools/phxMosaicBuilder/plugins/${builderpropfilepath}/ensemble.properties"
builderexec="${MSGRX_PATH}/tools/phxMosaicBuilder/mosaicker"

serverpropfilepath=`ls "${msgrxbase}"/tools/phxMosaicServer/plugins/ | grep "gov\.nasa\.jpl\.orchestrator\.phoenix_.*"`
serverpropfile="${msgrxbase}/tools/phxMosaicServer/plugins/${serverpropfilepath}/ensemble.properties"



# remove old msg reactor
rm -rf /msop/psi_messagereactor/*


replace_config_file_setting $pipelineconfig "\$PSI_dataRootDir" " = \"$CLIENT_LOCAL_CACHE_DIRECTORY\";"
replace_config_file_setting $confpl "my \$basedir" " = \"$MSGRX_PATH\";"
replace_config_file_setting $confpl "\$fromEmail" " = \"$MSGRX_EMAIL\";"
replace_config_file_setting $confpl "\$toEmail" " = \"$MSGRX_EMAIL\";"
replace_config_file_setting $confpl "\$administrator" " = \"$MSGRX_EMAIL\";"
replace_config_file_setting $confpl "\$mailServer" " = \"$MSGRX_EMAIL_SERVER\";"
replace_config_file_setting $confpl "\$j2eeServer" " = \"$MSGRX_J2EE_SERVER\";"

#replace_config_file_setting $msgxml "        <Command> " "/tps/bin/perl $MSGRX_PATH/catalog/catalog.pl -a {ACTION} -f {FILE_NAME} -d {dir_path} </Command>"
replace_config_file_setting $msginitd "MSGREACTOR_DIR=" "$MSGRX_PATH"

replace_config_file_setting $catpropfile "hibernate.database.type.planning=" "mysql"
replace_config_file_setting $catpropfile "hibernate.database.name.planning=" $DB_NAME
DB_HOST_AND_PORT="$DB_HOST:$DB_PORT"
replace_config_file_setting $catpropfile "hibernate.database.host.planning=" $DB_HOST_AND_PORT
replace_config_file_setting $catpropfile "hibernate.database.user.planning=" $DB_CLIENT
replace_config_file_setting $catpropfile "hibernate.database.pass.planning=" $DB_CLIENT_PASSWORD 

replace_config_file_setting $catpropfile "hibernate.database.type.dataproducts=" "mysql"
replace_config_file_setting $catpropfile "hibernate.database.name.dataproducts=" $DB_NAME
replace_config_file_setting $catpropfile "hibernate.database.host.dataproducts=" $DB_HOST_AND_PORT
replace_config_file_setting $catpropfile "hibernate.database.user.dataproducts=" $DB_CLIENT
replace_config_file_setting $catpropfile "hibernate.database.pass.dataproducts=" $DB_CLIENT_PASSWORD

replace_config_file_setting $catpropfile "hibernate.database.type.frame=" "mysql"
replace_config_file_setting $catpropfile "hibernate.database.name.frame=" $DB_NAME
replace_config_file_setting $catpropfile "hibernate.database.host.frame=" $DB_HOST_AND_PORT
replace_config_file_setting $catpropfile "hibernate.database.user.frame=" $DB_CLIENT
replace_config_file_setting $catpropfile "hibernate.database.pass.frame=" $DB_CLIENT_PASSWORD

replace_config_file_setting $catpropfile "hibernate.database.type.usage=" "mysql"
replace_config_file_setting $catpropfile "hibernate.database.name.usage=" $USAGE_DB_NAME
replace_config_file_setting $catpropfile "hibernate.database.host.usage=" $DB_HOST_AND_PORT
replace_config_file_setting $catpropfile "hibernate.database.user.usage=" $DB_CLIENT
replace_config_file_setting $catpropfile "hibernate.database.pass.usage=" $DB_CLIENT_PASSWORD


replace_config_file_setting $builderpropfile "hibernate.database.type.planning=" "mysql"
replace_config_file_setting $builderpropfile "hibernate.database.name.planning=" $DB_NAME
DB_HOST_AND_PORT="$DB_HOST:$DB_PORT"
replace_config_file_setting $builderpropfile "hibernate.database.host.planning=" $DB_HOST_AND_PORT
replace_config_file_setting $builderpropfile "hibernate.database.user.planning=" $DB_CLIENT
replace_config_file_setting $builderpropfile "hibernate.database.pass.planning=" $DB_CLIENT_PASSWORD 

replace_config_file_setting $builderpropfile "hibernate.database.type.dataproducts=" "mysql"
replace_config_file_setting $builderpropfile "hibernate.database.name.dataproducts=" $DB_NAME
replace_config_file_setting $builderpropfile "hibernate.database.host.dataproducts=" $DB_HOST_AND_PORT
replace_config_file_setting $builderpropfile "hibernate.database.user.dataproducts=" $DB_CLIENT
replace_config_file_setting $builderpropfile "hibernate.database.pass.dataproducts=" $DB_CLIENT_PASSWORD

replace_config_file_setting $builderpropfile "hibernate.database.type.frame=" "mysql"
replace_config_file_setting $builderpropfile "hibernate.database.name.frame=" $DB_NAME
replace_config_file_setting $builderpropfile "hibernate.database.host.frame=" $DB_HOST_AND_PORT
replace_config_file_setting $builderpropfile "hibernate.database.user.frame=" $DB_CLIENT
replace_config_file_setting $builderpropfile "hibernate.database.pass.frame=" $DB_CLIENT_PASSWORD

replace_config_file_setting $builderpropfile "hibernate.database.type.usage=" "mysql"
replace_config_file_setting $builderpropfile "hibernate.database.name.usage=" $USAGE_DB_NAME
replace_config_file_setting $builderpropfile "hibernate.database.host.usage=" $DB_HOST_AND_PORT
replace_config_file_setting $builderpropfile "hibernate.database.user.usage=" $DB_CLIENT
replace_config_file_setting $builderpropfile "hibernate.database.pass.usage=" $DB_CLIENT_PASSWORD
replace_config_file_setting $builderpropfile "local.cache.directory.default=" $CLIENT_LOCAL_CACHE_DIRECTORY

replace_config_file_setting $serverpropfile "hibernate.database.type.planning=" "mysql"
replace_config_file_setting $serverpropfile "hibernate.database.name.planning=" $DB_NAME
DB_HOST_AND_PORT="$DB_HOST:$DB_PORT"
replace_config_file_setting $serverpropfile "hibernate.database.host.planning=" $DB_HOST_AND_PORT
replace_config_file_setting $serverpropfile "hibernate.database.user.planning=" $DB_CLIENT
replace_config_file_setting $serverpropfile "hibernate.database.pass.planning=" $DB_CLIENT_PASSWORD 

replace_config_file_setting $serverpropfile "hibernate.database.type.dataproducts=" "mysql"
replace_config_file_setting $serverpropfile "hibernate.database.name.dataproducts=" $DB_NAME
replace_config_file_setting $serverpropfile "hibernate.database.host.dataproducts=" $DB_HOST_AND_PORT
replace_config_file_setting $serverpropfile "hibernate.database.user.dataproducts=" $DB_CLIENT
replace_config_file_setting $serverpropfile "hibernate.database.pass.dataproducts=" $DB_CLIENT_PASSWORD

replace_config_file_setting $serverpropfile "hibernate.database.type.frame=" "mysql"
replace_config_file_setting $serverpropfile "hibernate.database.name.frame=" $DB_NAME
replace_config_file_setting $serverpropfile "hibernate.database.host.frame=" $DB_HOST_AND_PORT
replace_config_file_setting $serverpropfile "hibernate.database.user.frame=" $DB_CLIENT
replace_config_file_setting $serverpropfile "hibernate.database.pass.frame=" $DB_CLIENT_PASSWORD

replace_config_file_setting $serverpropfile "hibernate.database.type.usage=" "mysql"
replace_config_file_setting $serverpropfile "hibernate.database.name.usage=" $USAGE_DB_NAME
replace_config_file_setting $serverpropfile "hibernate.database.host.usage=" $DB_HOST_AND_PORT
replace_config_file_setting $serverpropfile "hibernate.database.user.usage=" $DB_CLIENT
replace_config_file_setting $serverpropfile "hibernate.database.pass.usage=" $DB_CLIENT_PASSWORD


replace_config_file_setting $serverpropfile "mosaic.executable=" $builderexec
replace_config_file_setting $serverpropfile "local.cache.directory.default=" $CLIENT_LOCAL_CACHE_DIRECTORY


#copy the PSI install to wherever
	
	echo -n "      * CHECKING CATALOG PATH ... "
	
	if [ -d "$MSGRX_PATH" ]; then
		
		timestamp=`date +'%Y%m%e%H%M%S'`
		mv $MSGRX_PATH ${MSGRX_PATH}_old_${timestamp}
		mkdir -p "$MSGRX_PATH"
		echo "EXISTS"
	else
		mkdir -p "$MSGRX_PATH"
		echo "CREATED"
	fi
	
	echo -n "      * COPYING Catalog and Messge Reactor TO GIVEN PATH $MSGRX_PATH ... "
	cp -pR "$msgrxbase"/* "$MSGRX_PATH"
	echo "COMPLETED"	
	
	fi



	echo -e "\n\n\n"
if [ "$INSTALL_CLIENT" = "y" ]; then

	echo "--------------------------------------"
	echo "        YOU'RE NOT DONE YET!!!"
	echo "--------------------------------------"
	echo ""
	echo " To finish installing PSI switch to the PESMGR account and run the commands below (just copy and paste it into the command line): "
	echo " bash makeSymlinks_pesmgr.sh"
	echo " THEN switch back to CM and run:"
	echo " bash makeSymlinks_cm.sh"
	echo ""
elif [ "$INSTALL_MSGRX" = "y" ]; then
	echo "--------------------------------------"
	echo "        YOU'RE NOT DONE YET!!!"
	echo "--------------------------------------"
	echo ""
	echo " To finish installing PSI switch to the PESMGR account and run the commands below (just copy and paste it into the command line): "
	echo " bash makeSymlinks_pesmgr.sh"
	echo " THEN switch back to CM and run:"
	echo " bash makeSymlinks_cm.sh"
	echo ""
else
	echo "--------------------------------------"
	echo "        INSTALLER FINISHED "
	echo "--------------------------------------"
	echo " Please refer to install directions for further information"
	echo "--------------------------------------"
fi
