#!/bin/bash

# *************
# This file is only for the installation
# *************

sudo apt-get install nodejs -y
sudo apt-get install apache2 -y
sudo apt-get install mysql-server -y

# add a new user to mysql

sudo apt-get install maven -y

# change document root to point the webapp folder
# /etc/apache2/sites-enabled/000-default
# change directory section with the new webapp folder
# change AllowOverride to All
# delete Indexes
# /etc/apache2/apache2.conf

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

# if you want to connect from a remote computer you should change bind-address to * in /etc/mysql/mysql.conf.d/mysqld.cnf
