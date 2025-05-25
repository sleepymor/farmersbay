PRAGMA foreign_keys = ON;

CREATE TABLE User (
    UserID TEXT PRIMARY KEY,
    Name TEXT,
    Password TEXT
);

CREATE TABLE Admin (
    AdminID TEXT PRIMARY KEY,
    username TEXT,
    password TEXT
);

CREATE TABLE Category (
    CategoryID TEXT PRIMARY KEY,
    title TEXT
);

CREATE TABLE Items (
    ItemsID TEXT PRIMARY KEY,
    CategoryID TEXT,
    AdminID TEXT,
    title TEXT,
    price INTEGER,
    stock INTEGER,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID),
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

CREATE TABLE OrderProfile (
    OrderID TEXT PRIMARY KEY,
    UserID TEXT,
    OrderStatus TEXT, -- ENUM simulated as TEXT
    OrderDate DATE,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE OrderItems (
    OrderID TEXT,
    ItemsID TEXT,
    items_count INTEGER,
    PRIMARY KEY (OrderID, ItemsID),
    FOREIGN KEY (OrderID) REFERENCES OrderProfile(OrderID),
    FOREIGN KEY (ItemsID) REFERENCES Items(ItemsID)
);
