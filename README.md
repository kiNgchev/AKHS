# AKHS 
AKHS — is a collection of services for automation tasks, written on Kotlin with Spring Framework\
All services linked with each other with the help of [Apache Kafka](https://kafka.apache.org/)

# Environment variables
In the following, env variables will be described in format: `VARIBALE_NAME:default_value — value_description`

---
## akhs-configurations
- `CONFIG_SERVER_USER:config` — the username that will be set as value to get access to config server
- `CONFIG_SERVER_PASSWORD:config-password` — the password that will be set as value to get access to config server 
---
## akhs-discord, akhs-telegram, akhs-twitch, akhs-youtube
### Config server
- `CONFIG_SERVER_USER:config` — the username that needed to get access to config server
- `CONFIG_SERVER_PASSWORD:config-password` — the password that needed to get access to config server
### Database (Postgres)
- `POSTGRES_USER:postgres` — the username that needed to connect to database server
- `POSTGRES_PASSWORD:secret` — the password that needed to connect to database server
- `POSTGRES_DATABASE:akhs` — the database name to which needs to connect
- `POSTGRES_HOST:localhost` — the host on which located database server
- `POSTGRES_PORT:5432` — the port through which connect to database server
### Apache Kafka
- `KAFKA_BOOTSTRAP_SERVERS:localhost:9092` — the bootstrap servers for connecting to Apache Kafka. Bootstrap servers will be storage in format: `host1:9092,host2:9093,host3:29092`
### Redis
- `REDIS_HOST:localhost` — the host on which located Redis server
- `REDIS_PORT:6379` — the port through which connect to Redis server

---
# Build
To build JAR files, you should run:
```bash
  gradle clean build
```

---
To build docker images of services, you should run: 
```Bash
  docker build --build-arg JAR_FILE=<service jar file> -t akhs/<service-name>:<service-version> .
```
for example:
```Bash
  docker build --build-arg JAR_FILE=akhs-configurations/build/libs/akhs-configurations-0.0.1.jar -t akhs/akhs-configurations:0.0.1 .
```

---
# Run

## Required services
For run AKHS, you should deploy the following services:
- [PostgreSQL](https://www.postgresql.org/)
- [Apache Kafka](https://kafka.apache.org/)
- [Redis](https://redis.io/)

---
## Without Docker-compose
> [!IMPORTANT]
> Your SHOULD deploy [required services](#before-run)

For run AKHS, you may run JAR file or run: 
```Bash
  gradle <service>:run
```
If you use docker-compose, you should run:
```bash
  docker-compose build
```

---
## Partial with Docker-compose
> [!NOTE]
> In this solution [required services](#before-run) deployed on Docker-compose

Before run AKHS, you should run:
```Bash
  docker-compose up
```

For run AKHS, you may run JAR file or run:
```Bash
  gradle <service>:run
```

---
## With Docker-compose
> [!IMPORTANT]
> Before run AKHS in docker-compose you should [build JAR files](#build)

For run AKHS, you should run:
```Bash
  docker-compose up
```

---
# Profiles
- `|` - separate exchangeable profiles
- `,` - separate required profiles

The expression `cloud | local, dev | prod` equals the next expression: `(cloud or local) and (dev or prod)`

| Microservice name   | Profiles                    |
|---------------------|-----------------------------|
| akhs-configurations | git \| native               |
| akhs-discord        | cloud \| local, dev \| prod |
| akhs-telegram       | cloud \| local, dev \| prod |
| akhs-twitch         | cloud \| local, dev \| prod |
| akhs-youtube        | cloud \| local, dev \| prod |

---
