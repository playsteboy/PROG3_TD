create database "mini_football_db";
create user  "mini_football_db_manager" with password "password";
\c "mini_football_db";
grant create on schema public to  "mini_football_db_manager";
grant usage on schema public to  "mini_football_db_manager";

