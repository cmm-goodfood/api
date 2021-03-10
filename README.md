# Good food api

## Development

### Database

For local usage, you can use docker

1. Start docker engine
2. Run docker compose file using :
``` docker-compose up -d ```

You can go to [localhost:8081](localhost:8081)

With these logins (from docker-compose file) :
> PGADMIN_DEFAULT_EMAIL: pgadmin4@pgadmin.org \
> PGADMIN_DEFAULT_PASSWORD: admin

And set up connection with these logins (from docker-compose file) :
> container_name: goodfood_postgres \
> POSTGRES_USER: user \
> POSTGRES_PASSWORD: user