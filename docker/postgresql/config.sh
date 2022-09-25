
# You can CHANGE DB password here
docker exec -it local_pg psql -U postgres -c "CREATE ROLE my_user WITH ENCRYPTED PASSWORD 'my_secret' SUPERUSER LOGIN;"
docker exec -it local_pg psql -U postgres -c "CREATE DATABASE my_db encoding='UTF8';"
docker exec -it local_pg psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE my_db TO my_user;"