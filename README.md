# Student Registration System

A Java-based desktop application for managing student registrations, courses, and reports using a MySQL database and a Swing GUI.

## Features

- Add, update, and delete students and courses
- Register students to courses
- View registration reports
- Export student data to text files
- MVC structure using DAO and Service layers
- Custom logging and configuration handling

## Technologies Used

- Java
- Swing (GUI)
- MySQL
- JDBC (via MySQL Connector)
- Object-Oriented Programming principles

## Project Structure

```
OOP project/
├── src/                  # Source code
│   ├── abstractclasses/  # Abstract classes
│   ├── app/              # Testing Main
│   ├── DAO/              # Database access
│   ├── exceptions/       # Custom Exceptions
│   ├── gui/              # Swing GUI components
│   ├── interfaces/       # Interface methods
│   ├── model/            # Data models
│   ├── service/          # Business logic
│   ├── util/             # Helpers, config, logger
|   ├── MAIN.java         # This the Actual running point
├── libs/                 # External libraries (MySQL Connector)
├── exports/              # Exported student files
├── config.properties     # Configuration settings
├── app.log               # Application logs
```

## Getting Started

### Prerequisites

- Java JDK 11 or above
- **XAMPP** (to run MySQL locally)
- An IDE (e.g., IntelliJ IDEA or Eclipse, Vs-Code)

### Setup Instructions

1. Clone the repository.
2. Start Apache and MySQL via **XAMPP Control Panel** [Download XAMMP](https://nodejs.org](https://sourceforge.net/projects/xampp/files/XAMPP%20Windows/8.2.12/xampp-windows-x64-8.2.12-0-VS16-installer.exe))
3. Add the MySQL Connector JAR from `libs/` to your project libraries (already included in the Repo).
4. Configure your database credentials in `config.properties` but it will work as is.
 
5. Run the Javac As:

    ```bash
    for /R "src" %F in (*.java) do javac -cp "libs\mysql-connector-j-9.4.0.jar;out" -d out "%F"

    ```

6.Start the app from MAIN.

    java -cp "libs/mysql-connector-j-9.4.0.jar;out" MAIN

## Database Configuration

Edit the `config.properties` file:

```
db.url=jdbc:mysql://localhost:3306/studentdb
db.username=your_mysql_username
db.password=your_mysql_password
```

Once again the config will also work as is 


