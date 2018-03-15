
features:install jndi connector
osgi:install -s mvn:mysql/mysql-connector-java/5.1.46
osgi:install -s mvn:grgr.customer/database/0.1.0.BUILD-SNAPSHOT

features:install jpa hibernate
