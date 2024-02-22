
DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
    user_id int AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    password varchar(50) NOT NULL,
    PRIMARY KEY (user_id)
);

DROP TABLE IF EXISTS Chats;

CREATE TABLE Chats (
    chat_id int AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    user_id int,
    PRIMARY KEY(chat_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

