# CodeQuest – Programming Learning Game (VS Code)

A Maven WAR project for **JDK 17 + Apache Tomcat 10.1 + MySQL 8**. It uses JSP, Jakarta Servlets, JDBC, HTML and CSS. JSP pages use scriptlets (no JSTL and no JSP Expression Language), matching the assignment.

## Features
- Student registration, login, logout and session-protected dashboard
- Separate administrator login and dashboard
- Admin question creation and question management
- Java / SQL / DSA missions with Easy / Medium / Hard levels
- One attempt per mission; correct answers award XP once
- XP-based levels and global leaderboard
- PreparedStatement, foreign keys, database transaction for answer + XP update
- PBKDF2 password hashing; no plaintext passwords
- Common dark gaming UI

## Requirements
- JDK 17
- Apache Maven 3.9+
- Apache Tomcat 10.1 (Jakarta Servlet 6)
- MySQL 8.x

## 1. Create the database
Open MySQL Workbench or the MySQL command line and run `database/schema.sql`.
Then update database credentials in:
`src/main/java/com/codequest/util/DBConnection.java`

Default connection expects:
- database: `codequest_db`
- username: `root`
- password: `root` (change this to your local MySQL password)

## 2. Create the first admin
From the project folder run:
```bash
mvn clean package
```
Then deploy `target/CodeQuest.war` to Tomcat 10.1. Start Tomcat and open:
`http://localhost:8080/CodeQuest/`

Create an admin account once using the one-time setup URL:
`http://localhost:8080/CodeQuest/SetupAdminServlet?secret=CHANGE_THIS_SETUP_SECRET&username=admin&email=admin@codequest.local&password=Admin@123`
Change `SETUP_SECRET` in `SetupAdminServlet.java` before deployment. The setup servlet refuses to create another admin if one already exists. After successful setup, remove or disable `SetupAdminServlet.java` and rebuild for a real deployment.

## 3. Run from VS Code
1. Extract the ZIP and open the `CodeQuest-VSCode` folder in VS Code.
2. Install **Extension Pack for Java** and **Maven for Java**.
3. Set `DBConnection.java` credentials.
4. Run `mvn clean package` in the VS Code terminal.
5. Copy `target/CodeQuest.war` to `<TOMCAT_HOME>/webapps/`.
6. Start Tomcat:
   - Windows: `%TOMCAT_HOME%\bin\startup.bat`
   - Linux/macOS: `$TOMCAT_HOME/bin/startup.sh`
7. Visit `http://localhost:8080/CodeQuest/`.

> VS Code is the editor. Tomcat runs the JSP/Servlet application; the browser displays it. Do not use Python's Live Server for JSP.

## Default level rule
Level = `1 + floor(total_xp / 100)`. The next-level progress bar uses the remainder out of 100 XP.

## Notes
- The assignment requires a one-attempt-per-mission rule. A submission row is inserted for each first attempt; a correct first attempt awards XP.
- This is a learning project, not a production deployment. Add HTTPS, rate limiting, CSRF protection, secure secrets management, and account recovery before public hosting.
- If port 8080 is occupied, change Tomcat's HTTP connector port in `conf/server.xml`.
