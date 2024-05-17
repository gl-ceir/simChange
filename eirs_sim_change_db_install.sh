#!/opt/homebrew/bin/bash
conffile=/Users/dexter/eirsapp/configuration/configuration.properties
typeset -A config # init array

while read line
do
    if echo $line | grep -F = &>/dev/null
    then
        varname=$(echo "$line" | cut -d '=' -f 1)
        config[$varname]=$(echo "$line" | cut -d '=' -f 2-)
    fi
done < $conffile
conn1="mysql -h${config[ip]} -P${config[dbPort]} -u${config[dbUsername]} -p${config[dbPassword]}"
conn="mysql -h${config[ip]} -P${config[dbPort]} -u${config[dbUsername]} -p${config[dbPassword]} ${config[appdbName]}"

echo "creating app database."
${conn1} -e "CREATE DATABASE IF NOT EXISTS apptest1;"
echo "app database successfully created!"

`${conn} <<EOFMYSQL

CREATE TABLE if not exists black_list (
  id int NOT NULL AUTO_INCREMENT,
  complain_type varchar(50) DEFAULT NULL,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  expiry_date timestamp NULL DEFAULT NULL,
  imei varchar(50) DEFAULT NULL,
  mode_type varchar(50) DEFAULT NULL,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  request_type varchar(50) DEFAULT NULL,
  txn_id varchar(50) DEFAULT NULL,
  user_id varchar(50) DEFAULT NULL,
  user_type varchar(50) DEFAULT NULL,
  operator_id varchar(50) DEFAULT NULL,
  operator_name varchar(50) DEFAULT NULL,
  actual_imei varchar(50) DEFAULT NULL,
  tac varchar(50) DEFAULT NULL,
  remarks varchar(250) DEFAULT NULL,
  msisdn varchar(15) DEFAULT NULL,
  imsi varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  INDEX index_name (created_on, modified_on)
);


CREATE TABLE if not exists grey_list (
  id int NOT NULL AUTO_INCREMENT,
  complain_type varchar(50) DEFAULT NULL,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  expiry_date timestamp NULL DEFAULT NULL,
  imei varchar(50) DEFAULT NULL,
  mode_type varchar(50) DEFAULT NULL,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  request_type varchar(50) DEFAULT NULL,
  txn_id varchar(50) DEFAULT NULL,
  user_id varchar(50) DEFAULT NULL,
  user_type varchar(50) DEFAULT NULL,
  operator_id varchar(50) DEFAULT NULL,
  operator_name varchar(50) DEFAULT NULL,
  actual_imei varchar(50) DEFAULT NULL,
  tac varchar(50) DEFAULT NULL,
  remarks varchar(250) DEFAULT NULL,
  msisdn varchar(15) DEFAULT NULL,
  imsi varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  INDEX index_name (created_on, modified_on)
);



CREATE TABLE if not exists exception_list (
  id int NOT NULL AUTO_INCREMENT,
  complain_type varchar(50) DEFAULT NULL,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  expiry_date timestamp NULL DEFAULT NULL,
  imei varchar(50) DEFAULT NULL,
  mode_type varchar(50) DEFAULT NULL,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  request_type varchar(50) DEFAULT NULL,
  txn_id varchar(50) DEFAULT NULL,
  user_id varchar(50) DEFAULT NULL,
  user_type varchar(50) DEFAULT NULL,
  operator_id varchar(50) DEFAULT NULL,
  operator_name varchar(50) DEFAULT NULL,
  actual_imei varchar(50) DEFAULT NULL,
  tac varchar(50) DEFAULT NULL,
  remarks varchar(250) DEFAULT NULL,
  msisdn varchar(15) DEFAULT NULL,
  imsi varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  INDEX index_name (created_on, modified_on)
);




CREATE TABLE if not exists black_list_his (
  id int NOT NULL AUTO_INCREMENT,
  complain_type varchar(50) DEFAULT NULL,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  expiry_date timestamp NULL DEFAULT NULL,
  imei varchar(50) DEFAULT NULL,
  mode_type varchar(50) DEFAULT NULL,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  request_type varchar(50) DEFAULT NULL,
  txn_id varchar(50) DEFAULT NULL,
  user_id varchar(50) DEFAULT NULL,
  user_type varchar(50) DEFAULT NULL,
  operator_id varchar(50) DEFAULT NULL,
  operator_name varchar(50) DEFAULT NULL,
  actual_imei varchar(50) DEFAULT NULL,
  tac varchar(50) DEFAULT NULL,
  remarks varchar(250) DEFAULT NULL,
  msisdn varchar(15) DEFAULT NULL,
  imsi varchar(50) DEFAULT NULL,
  operation int,
  PRIMARY KEY (id),
  INDEX index_name (created_on, modified_on)
);


CREATE TABLE if not exists grey_list_his (
  id int NOT NULL AUTO_INCREMENT,
  complain_type varchar(50) DEFAULT NULL,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  expiry_date timestamp NULL DEFAULT NULL,
  imei varchar(50) DEFAULT NULL,
  mode_type varchar(50) DEFAULT NULL,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  request_type varchar(50) DEFAULT NULL,
  txn_id varchar(50) DEFAULT NULL,
  user_id varchar(50) DEFAULT NULL,
  user_type varchar(50) DEFAULT NULL,
  operator_id varchar(50) DEFAULT NULL,
  operator_name varchar(50) DEFAULT NULL,
  actual_imei varchar(50) DEFAULT NULL,
  tac varchar(50) DEFAULT NULL,
  remarks varchar(250) DEFAULT NULL,
  msisdn varchar(15) DEFAULT NULL,
  imsi varchar(50) DEFAULT NULL,
  operation int,
  PRIMARY KEY (id),
  INDEX index_name (created_on, modified_on)
);


CREATE TABLE if not exists exception_list_his (
  id int NOT NULL AUTO_INCREMENT,
  complain_type varchar(50) DEFAULT NULL,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  expiry_date timestamp NULL DEFAULT NULL,
  imei varchar(50) DEFAULT NULL,
  mode_type varchar(50) DEFAULT NULL,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  request_type varchar(50) DEFAULT NULL,
  txn_id varchar(50) DEFAULT NULL,
  user_id varchar(50) DEFAULT NULL,
  user_type varchar(50) DEFAULT NULL,
  operator_id varchar(50) DEFAULT NULL,
  operator_name varchar(50) DEFAULT NULL,
  actual_imei varchar(50) DEFAULT NULL,
  tac varchar(50) DEFAULT NULL,
  remarks varchar(250) DEFAULT NULL,
  msisdn varchar(15) DEFAULT NULL,
  imsi varchar(50) DEFAULT NULL,
  operation int,
  PRIMARY KEY (id),
  INDEX index_name (created_on, modified_on)
);

CREATE TABLE if not exists sys_param (
  id int NOT NULL AUTO_INCREMENT,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  description varchar(255) DEFAULT '',
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  tag varchar(255) DEFAULT NULL,
  type int DEFAULT '0',
  value text,
  active int DEFAULT '0',
  feature_name varchar(255) DEFAULT '',
  remark varchar(255) DEFAULT '',
  user_type varchar(255) DEFAULT '',
  modified_by varchar(255) DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE KEY tag (tag)
);

CREATE TABLE if not exists sys_generated_alert (
  id int NOT NULL AUTO_INCREMENT,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  alert_id varchar(20) DEFAULT '',
  description varchar(250) DEFAULT '',
  status int DEFAULT '0',
  user_id int DEFAULT '0',
  username varchar(50) DEFAULT '',
  PRIMARY KEY (id)
);

CREATE TABLE if not exists cfg_feature_alert (
  id int NOT NULL AUTO_INCREMENT,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  alert_id varchar(20) DEFAULT '',
  description varchar(250) DEFAULT '',
  feature varchar(250) DEFAULT '',
  PRIMARY KEY (id)
);

insert into sys_param (description, tag, value, feature_name) SELECT 'The msisdn prefixes used to validate the dump files received from operators. The values can be comma-separated in case of multiple prefixes.', 'msisdnPrefix', '855', 'List Management' FROM dual WHERE NOT EXISTS ( SELECT * FROM sys_param WHERE tag = 'msisdnPrefix');
insert into sys_param (description, tag, value, feature_name) SELECT 'The imsi prefixes used to validate the dump files received from operators. The values can be comma-separated in case of multiple prefixes.', 'imsiPrefix', '456', 'List Management' FROM dual WHERE NOT EXISTS ( SELECT * FROM sys_param WHERE tag = 'imsiPrefix');

insert into cfg_feature_alert (alert_id, description, feature) values ("alert5400", "The Sim Change file not found at path <e> for operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5401", "The Sim Change file <e> does not contain new IMSI header for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5402", "The Sim Change file <e> does not contain old IMSI header for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5403", "The Sim Change file <e> does not contain MSISDN header for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5404", "The Sim Change file <e> does not contain sim_change_date header for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5405", "The Sim Change file <e> failed for new IMSI prefix validation for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5406", "The Sim Change file <e> failed for old IMSI prefix validation for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5407", "The Sim Change file <e> failed for msisdn prefix validation for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5408", "Null/Non-Numeric values exists in Sim Change file <e> for operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5409", "The Sim Change file <e> failed for uniqueness for new IMSI values for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5410", "The Sim Change file <e> failed for uniqueness for old IMSI values for the operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) values ("alert5411", "The process failed for Sim Change with an exception <e> for operator <process_name>", "Sim_Change_Dump");
insert into cfg_feature_alert (alert_id, description, feature) SELECT "alert5001", "The values for either IMSI Prefix or MSISDN prefix is missing in database.", "Sim_Change_Dump" FROM dual WHERE NOT EXISTS ( SELECT * FROM cfg_feature_alert WHERE alert_id = 'alert5001');


EOFMYSQL`

echo "creating aud database."
${conn1} -e "CREATE DATABASE IF NOT EXISTS aud;"
echo "aud database successfully created!"

conn2="mysql -h${config[ip]} -P${config[dbPort]} -u${config[dbUsername]} -p${config[dbPassword]} ${config[auddbName]}"

`${conn2} << EOFMYSQL


CREATE TABLE if not exists modules_audit_trail (
  id int NOT NULL AUTO_INCREMENT,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  execution_time int DEFAULT '0',
  status_code int DEFAULT '0',
  status varchar(100) DEFAULT NULL,
  error_message varchar(255) DEFAULT NULL,
  module_name varchar(50) DEFAULT '',
  feature_name varchar(50) DEFAULT '',
  action varchar(20) DEFAULT '',
  count int DEFAULT '0',
  info varchar(255) DEFAULT '',
  server_name varchar(30) DEFAULT '',
  count2 int DEFAULT '0',
  failure_count int DEFAULT '0',
  PRIMARY KEY (id)
);
alter table modules_audit_trail modify error_message varchar(1000);
EOFMYSQL`
echo "tables creation completed."
echo "                                             *
						  ***
						 *****
						  ***
						   *                           "
echo "********************Thank You DB Process is completed now for HLR Deactivation Module*****************"
