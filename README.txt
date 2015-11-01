/* My Note is a simple and convenient online notepad */

Instructions how to deploy the application

Add this to the context.xml

1. Crate a user in MySQL v5.5 server
"CREATE USER '<USERNAME>'@'<MYNOTE_SERVER_HOSTNAME>' IDENTIFIED BY '<PASSWORD>';"

2. Create a database on MySQL server by the command
"CREATE DATABASE mynote CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

3. Grant privileges to the user by the commands
"GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,INDEX on mynote.* TO '<USERNAME>'@'<MYNOTE_SERVER_HOSTNAME>' IDENTIFIED BY '<PASSWORD>';"
"flush privileges;"

4. You can confirm the permissions by the command 
"SHOW GRANTS FOR <USERNAME>@<MYNOTE_SERVER_HOSTNAME>;"

For development create dev configuration
1. Create separate Tomcat base
2. Add to context.xml next definition

   <Resource  name="jdbc/mynote_datasource"
          auth="Container"
          type="javax.sql.DataSource"
          username="mynote"
          password="mynote"
          driverClassName="com.mysql.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/mynote_dev?characterEncoding=UTF-8"
          maxActive="15" maxIdle="3"/>

3. Create mynote_test database and grant privileges to mynote user