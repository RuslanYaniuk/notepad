/* My Note is a simple and convenient online notepad */

Instructions to deploy the application

1. Crate a user in MySQL v5.5 server
"CREATE USER '<USERNAME>'@'<MYNOTE_SERVER_HOSTNAME>' IDENTIFIED BY '<PASSWORD>';"

2. Create a database on MySQL server by the command
"CREATE DATABASE mynote CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

3. Grant privilages to the user by the commands
"GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,INDEX on mynote.* TO '<USERNAME>'@'<MYNOTE_SERVER_HOSTNAME>' IDENTIFIED BY '<PASSWORD>';"
"flush privileges;"

4. You can confirm the permissions by the command 
"SHOW GRANTS FOR <USERNAME>@<MYNOTE_SERVER_HOSTNAME>;"
