# Chat Together

This repository contains a simple terminal based chat application. It is fully build with
java, communication between clients provided through native java sockets API, aka client/server communication


## Get Started
 ### 1. Set a property file
In order to run this application you need to set the following property variables
~~~
    url.db = <your url>
    username.db = <your username>
    password.db = <your password
    pool.size = 10 // feel free to make a change
~~~

### 2. Start a MySQL database
Make sure to have a database running on your local machine, with properties 
which you specified in a property file
### 3. Run the Application
Once you've made all previous steps, now you can start the server, so client can send messages to each other


## Built with
* [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) - Oracle Java 21 JDK
* [MySQL](https://www.mysql.com/) - MySQL Databas
* JDBC
* [HikariCP](https://github.com/brettwooldridge/HikariCP)
* [password4j](https://password4j.com/)* 


## TODO LIST
- Add logging
- Refactor code
- Add connection to non-localhost machine

## Contributing
If you want to contribute to this project you can pull request, your contributions are always very welcomed.

## Versioning
This project does not have versioning and made with learning purposes.