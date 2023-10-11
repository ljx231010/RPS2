# RPS

**RPS** is a method where the goal is to recommend expressed host organism for a user-specified pathway and then recommend foreign enzymes for foreign reaction.

**RPS** consists of four main steps:

- **First**,  obtain the pathway and weight parameter information input by the user from the web page.
- **Second**, the scores of pathway under 70 candidate host organism are calculated based on the proportion of endogenous reactions, the number of dead end metabolites and score of competing endogenous reactions, and then generate top-ranked candidate host organism.
- **Third**, for each host organism, RPS can display the reaction information of the pathway, the enzyme, and determines whether each reaction is an endogenous reaction.
- **Finally**, recommend potential foreign enzymes for foreign reaction, provides three ways to do this: by phylogenetic distance, by Km value, or by combination of phylogenetic distance and Km value.

# Software environment used

- Java: JDK1.8.0_352
- &IDE: IDEA2020.3
- Backend: SpringMVC5.1.9 Spring5.1.9 MyBatis3.5.2
- Database: mysql8.0.22
- Web Server: Tomcat 9
- Build Tool: Maven
- Other: Druid(database connection pool) JUnit Log4j FastJson

# Dataset Preparation

#### Database data

> Assume that in MySQL, user stores data in database ‘rea’, the user name is “root”, password is “123456”,  the followings are the command and operation for adding data to MySQL Database.

1. Download `fre.sql`.
2. Enter mysql with command line `mysql -u -root -p` and input the password "123456"(或自定义的用户及密码)
3. Create database "fre" with command line `create database fre;`
4. Enter database "fre" with command line `use fre;`
5. Import the data "fre.sql" with command line `source D:\\fre.sql;`

#### Local files

1.Download the `data.rar` file and unzip it to the d:/data directory

# Use

1.Install tomcat.

2.Import the project into IDEA or Eclipse, configure tomcat, and use it.

​	Or directly deploy to tomcat for use.

3.The specific functions can be read in the `Help` section of the website after successful deployment
