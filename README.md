# Configuration nécessaire pour lancer l'application

- [JDK Java version 21](insérer lien)
- [Quarkus](https://quarkus.io/guides/cli-tooling)
- [Docker](insérer lien)

## Comment lancer l'application :

1. Ouvrez un terminal.
2. Accédez au dossier `src\main\docker\dockerBDD`.
3. Exécutez la commande `docker compose up`.

Ensuite, dans un nouveau terminal, lancez la commande `./gradlew quarkusDev`.

Les routes de l'API nécessitent une authentification. Pour cela, utilisez d'abord la route `/register`, puis `/authentication`, qui vous fournira un token permettant l'accès aux autres routes.

## Accéder à la documentation Swagger :

Après avoir lancer l'application, rendez-vous sur cette [URL](http://localhost:8080/q/swagger-ui/#/).
