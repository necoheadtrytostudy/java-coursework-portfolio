# JDBC and MySQL Grade Database Demo

A Java console application demonstrating secure database access and common SQL operations using JDBC and MySQL.

## Features

- Create a grade table when it does not exist
- Insert student grade records
- Update existing scores
- Query Java grades in descending order
- Delete failing grade records
- Use parameterized SQL with `PreparedStatement`
- Read database credentials from environment variables

## Concepts Practiced

- JDBC database connections
- MySQL CRUD operations
- `PreparedStatement`
- `ResultSet`
- Try-with-resources
- SQL sorting and filtering
- Secure credential handling

## Requirements

- JDK 17 or newer
- MySQL Server
- MySQL Connector/J

```sql
CREATE DATABASE student;
```

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="your-password"
$env:DB_URL="jdbc:mysql://localhost:3306/student?serverTimezone=UTC"
```

```powershell
javac -cp ".;mysql-connector-j.jar" src\GradeDatabaseDemo.java
java -cp ".;src;mysql-connector-j.jar" GradeDatabaseDemo
```

```text
2026001 | Amy             | 92.00
2026002 | Ben             | 88.00
```

## Security

Database passwords are not stored in the source code. Credentials are supplied through environment variables.
