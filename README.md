# Sklep internetowy
Projekt: Zaawansowane technologie usług sieciowych

# Working with the H2 database

1. Run application
2. Go to http://localhost:8080/console
3. In the form set driver as `org.h2.Driver` and JDBC url as `jdbc:h2:path_to_db_files\database;MODE=MySQL`,
where path_to_db_files is path to your `database.mv.db` file
4. Connect and modify database

Tip: H2 syntax is very rigorous (in contrast to mySql), so any inaccurate entry in your code may cause error
