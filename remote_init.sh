#!/bin/bash

sudo apt-get install nodejs -y
sudo apt-get install apache2 -y
sudo apt-get install mysql-server -y

# add a new user to mysql

sudo apt-get install maven -y
# /etc/apache2/sites-enabled/000-default cambia document root per puntare alla cartella webapp
# /etc/apache2/apache2.conf # cambia la sezione directory /var/www con la tua cartella webapp
# 		sistema AllowOverride ad All
#		elimina Indexes o Index
sudo service apache2 restart
sudo apt-get install openjdk-8-jre -y
sudo apt-get install openjdk-8-jdk -y

# add java_path
#/usr/lib/jvm/java-8-openjdk-amd64
echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64" >> ~/.bashrc
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

# git clone repository
mvn clean install
mvn spring-boot:run -Dmaven.test.skip=true

# cambia bind-address a *
/etc/mysql/mysql.conf.d/mysqld.cnf
