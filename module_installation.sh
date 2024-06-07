#!/bin/bash

# script for first time installation of sim change module.



#!/bin/bash
#set -x
#set -e
pause(){
  read -p "Press [Enter] key to continue..." fackEnterKey
}
one()
{
appfolder=/u01/eirsapp
appfolderdir=/u01/eirsapp/simChange
datafolder=/u02/eirsdata/logs/simChange

mkdir -p ${appfolderdir}
mkdir -p ${datafolder}

echo `tar -xzvf '' >> simChange_Module_untar_$(date +%Y%m%d)_log.txt`
echo `mv simChange/* ${appfolderdir}`

echo "++++++++++++Module Installation completed+++++++++++"
pause
}

two()
{
	./eirs_sim_change_db_install.sh
pause
}


show_menus() {
        clear
echo -e ""
echo -e ""
echo -e "         #############################################################"
echo -e "         ##          EIRS_EIR FCM MODULE INSTALLATION               ##"
echo -e "         ##~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~##"
echo -e "         ##                    M A I N - M E N U                    ##"
echo -e "         ##~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~##"
echo -e "         ##                                                         ##"
echo -e "         ##            \033[1m*Please Chose Anyone Action*\033[0m   ##"          #//Don't edit this line
echo -e "         ##                                                         ##"
echo -e "         ##             1.    Install Module                        ##"
echo -e "         ##             2.    Run Db Script                         ##"
echo -e "         ##             3.    Exit                                  ##"
echo -e "         #############################################################"
echo -e ""
             }

read_options(){
        local choice
        read -p "Enter choice [ 1 - 4] " choice
        case $choice in
                1) one ;;
                2) two ;;
                3) exit 0;;
                *) echo -e "${RED}Error...${STD}" && sleep 2
        esac
}

# ----------------------------------------------
# Step #3: Trap CTRL+C, CTRL+Z and quit singles
# ----------------------------------------------
trap '' SIGINT SIGQUIT SIGTSTP

# -----------------------------------
# Step #4: Main logic - infinite loop
# ------------------------------------
while true
do

        show_menus
        read_options
done

