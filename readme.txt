
== Fuse

features:install jndi connector
osgi:install -s mvn:mysql/mysql-connector-java/5.1.46
osgi:install -s mvn:grgr.customer/database/0.1.0.BUILD-SNAPSHOT

features:install jpa hibernate
osgi:install -s mvn:grgr.customer/jpa/0.1.0.BUILD-SNAPSHOT

osgi:install -s mvn:grgr.customer/test/0.1.0.BUILD-SNAPSHOT

== Docker

$ docker run -d --name fuse-mysql-server -e MYSQL_ROOT_PASSWORD=fuse -p 3306:3306 mysql:5.7.21
root@5168c3985593:~# mysql -p
Enter password: fuse
...
mysql> create database fuse character set 'utf8';
Query OK, 1 row affected (0.00 sec)
...
mysql> create user 'fuse'@'%' identified with mysql_native_password by 'fuse';
Query OK, 0 rows affected (0.00 sec)

mysql> select Host, User, authentication_string, plugin from mysql.user;
+-----------+---------------+-------------------------------------------+-----------------------+
| Host      | User          | authentication_string                     | plugin                |
+-----------+---------------+-------------------------------------------+-----------------------+
| localhost | root          | *66366D5297921E017C7C9378931FD111B3951D84 | mysql_native_password |
| localhost | mysql.session | *THISISNOTAVALIDPASSWORDTHATCANBEUSEDHERE | mysql_native_password |
| localhost | mysql.sys     | *THISISNOTAVALIDPASSWORDTHATCANBEUSEDHERE | mysql_native_password |
| %         | root          | *66366D5297921E017C7C9378931FD111B3951D84 | mysql_native_password |
| %         | fuse          | *66366D5297921E017C7C9378931FD111B3951D84 | mysql_native_password |
+-----------+---------------+-------------------------------------------+-----------------------+
5 rows in set (0.00 sec)

mysql> grant all on fuse.* to 'fuse'@'%';
Query OK, 0 rows affected (0.00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.01 sec)

mysql> use fuse
Database changed
mysql> create table users ( id integer not null auto_increment, name varchar(32), primary key (id) );
Query OK, 0 rows affected (0.04 sec)

mysql> desc users;
+-------+-------------+------+-----+---------+----------------+
| Field | Type        | Null | Key | Default | Extra          |
+-------+-------------+------+-----+---------+----------------+
| id    | int(11)     | NO   | PRI | NULL    | auto_increment |
| name  | varchar(32) | YES  |     | NULL    |                |
+-------+-------------+------+-----+---------+----------------+
