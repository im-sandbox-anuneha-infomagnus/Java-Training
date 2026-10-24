# CampusLedger – Student Record Manager

A console-based Java application for managing student records using object-oriented programming, collections, streams, and exception handling.

## Features
- Add student records
- View all students sorted by ID
- Search students by ID
- Remove student records
- Detect duplicate student IDs
- Validate student details and marks
- Handle invalid input and missing records

## Technologies
- Java 21
- Maven
- Java Collections and Streams
- Custom Exceptions

## Prerequisites
- JDK 21 or later
- Maven

## How to Run

Compile the project:

```powershell
mvn clean compile
```

Run the application:

```powershell
mvn org.codehaus.mojo:exec-maven-plugin:3.5.1:java "-Dexec.mainClass=com.anuneha.campusledger.Main"
```

**Note:** Records are stored in memory and are cleared when the application restarts.
