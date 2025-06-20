# FarmersBay

## Overview

This is a Java Swing application built using the MVC (Model-View-Controller) pattern.
The project uses SQLite as the database and incorporates JDBC for database connectivity..

---

## Technologies and Tools

- **JDK 23** (Java Development Kit) for the development environment
- **Java 17** as the compiler target version
- **JavaFX** for building the graphical user interface (GUI)
- **SQLite** as the embedded database
- **JDBC** for connecting and interacting with SQLite
- **Maven** for project management and build automation

---

## Prerequisites

- JDK 23 installed and properly configured in your environment
- Maven installed (for build and dependency management)
- SQLite database file included in the project (or instructions to create one)

---

## Setup and Build Instructions

1. Clone the repository:

   ```bash
   git clone https://https://github.com/sleepymor/farmersbay
   cd farmersbay
   ```
2. Build the project using maven

   ```bash
   mvn clean compile
   ```
3. Run the project

   ```bash
   mvn exec:java -Dexec.mainClass="your.package.Main"
   ```
