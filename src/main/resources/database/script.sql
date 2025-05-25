PRAGMA foreign_keys = ON;

CREATE TABLE User (
    UserID INTEGER PRIMARY KEY AUTOINCREMENT ,
    Name TEXT,
    Password TEXT
);

CREATE TABLE Admin (
    AdminID INTEGER PRIMARY KEY AUTOINCREMENT ,
    username TEXT,
    password TEXT
);

CREATE TABLE Category (
    CategoryID INTEGER PRIMARY KEY AUTOINCREMENT ,
    title TEXT
);

CREATE TABLE Items (
    ItemsID INTEGER PRIMARY KEY AUTOINCREMENT ,
    CategoryID INTEGER,
    AdminID TEXT,
    title TEXT,
    price INTEGER,
    stock INTEGER,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID),
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

CREATE TABLE OrderProfile (
    OrderID INTEGER PRIMARY KEY AUTOINCREMENT ,
    UserID INTEGER,
    OrderStatus TEXT, -- ENUM simulated as TEXT
    OrderDate DATE,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE OrderItems (
    OrderID INTEGER,
    ItemsID INTEGER,
    items_count INTEGER,
    PRIMARY KEY (OrderID, ItemsID),
    FOREIGN KEY (OrderID) REFERENCES OrderProfile(OrderID),
    FOREIGN KEY (ItemsID) REFERENCES Items(ItemsID)
);
