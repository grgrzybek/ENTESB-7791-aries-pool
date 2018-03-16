
== Testing

$ cd $FUSE_HOME
$ echo <number> > data/test # where <number> is the number of threads to perform JPA query with XA transaction
$ echo <number> > data/btest # (blueprint version with jpa:context and tx:transaction)


== Logging

Be sure to add:

    log4j.logger.org.apache.geronimo.connector=TRACE

to etc/org.ops4j.pax.logging.cfg. It'll give you (for every transaction):

2018-03-15 16:47:35,133 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.SinglePoolConnectionInterceptor] (SinglePoolConnectionInterceptor.java:105) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | Supplying pooled connection  MCI: ManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2] from pool: org.apache.geronimo.connector.outbound.SinglePoolConnectionInterceptor@4a9b8ede
2018-03-15 16:47:35,150 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.TransactionEnlistingInterceptor] (TransactionEnlistingInterceptor.java:58) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | Enlisting connection handle: nullManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2] with XAResource org.apache.geronimo.transaction.manager.WrapperNamedXAResource@1de88840 in transaction: org.apache.geronimo.transaction.manager.TransactionImpl@6a2a5261
2018-03-15 16:47:35,153 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.TransactionCachingInterceptor] (TransactionCachingInterceptor.java:104) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | supplying connection from pool null for managed connection org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2 to tx caching interceptor org.apache.geronimo.connector.outbound.TransactionCachingInterceptor@69d78a70
2018-03-15 16:47:35,156 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.GeronimoConnectionEventListener] (GeronimoConnectionEventListener.java:65) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | connectionClosed called with org.tranql.connector.jdbc.ConnectionHandle@41b16d66 for MCI: ManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2] and MC: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2
2018-03-15 16:47:35,159 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.TransactionCachingInterceptor] (TransactionCachingInterceptor.java:128) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | tx active, not returning connection  for tx caching interceptor org.apache.geronimo.connector.outbound.TransactionCachingInterceptor@69d78a70 handle: org.tranql.connector.jdbc.ConnectionHandle@41b16d66ManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2]
2018-03-15 16:47:35,161 | INFO  | {EMF-01-thread} [grgr.customer.test.Activator$Task] (Activator.java:167) | 319 - grgr.customer.test - 0.1.0.BUILD-SNAPSHOT |  - 1: Grzegorz
2018-03-15 16:47:35,164 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.TransactionCachingInterceptor] (TransactionCachingInterceptor.java:172) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | Transaction completed, attempting to return shared connection MCI: for tx caching interceptor org.apache.geronimo.connector.outbound.TransactionCachingInterceptor@69d78a70 ManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2]
2018-03-15 16:47:35,166 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.TransactionEnlistingInterceptor] (TransactionEnlistingInterceptor.java:101) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | not delisting connection handle: nullManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2] with XAResource org.apache.geronimo.transaction.manager.WrapperNamedXAResource@1de88840 no transaction
2018-03-15 16:47:35,168 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.AbstractSinglePoolConnectionInterceptor] (AbstractSinglePoolConnectionInterceptor.java:106) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | returning connection null for MCI ManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2] and MC org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2 to pool org.apache.geronimo.connector.outbound.SinglePoolConnectionInterceptor@4a9b8ede
2018-03-15 16:47:35,170 | TRACE | {EMF-01-thread} [org.apache.geronimo.connector.outbound.TransactionCachingInterceptor] (TransactionCachingInterceptor.java:159) | 301 - org.apache.geronimo.components.geronimo-connector - 3.1.1 | completed return of connection through tx cache for tx caching interceptor org.apache.geronimo.connector.outbound.TransactionCachingInterceptor@69d78a70 handle: nullManagedConnectionInfo: org.apache.geronimo.connector.outbound.ManagedConnectionInfo@65a17f7e. mc: org.apache.aries.transaction.jdbc.internal.XADataSourceMCFFactory$XADataSourceMCF$1@588c03f2]


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
