Setup
* download postgres from docker hub: `docker pull postgres`

Startup
* to start the db docker container run: `docker run -p 127.0.0.1:5432:5432 -e POSTGRES_PASSWORD=pw --name dis postgres`