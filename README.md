# Ejercicio Sucursales

Ejercicio de busqueda de sucursales por distancia con kotlin y spring boot

### comandos para buildear

Nota: No agregue un archivo de variables de entorno para setear el puerto de la base de datos. Asi que se tiene que usar el puerto por defecto para MongoDB: 27017

#### Con Docker y base de datos corriendo en local.

Desde el inicio de la ruta del proyecto (a la altura del archivo Dockerfile) ejecutar:

1. gradlew clean build
2. docker build -t <nombre-de-la-imagen> .
3. docker run -e "SPRING_PROFILES_ACTIVE=docker" -p 8080:8080 <nombre-de-la-imagen>

#### Con Docker Compose, creando tanto el servicio como la base de datos en contenedores.

Desde el inicio de la ruta del proyecto (a la altura del archivo Dockerfile) ejecutar:

1. gradlew clean build
2- docker network create main-network
3. docker-compose up -d (-d para correr la aplicacion en segundo plano, quitar para ver los logs)

Nota: La aplicacion se conecta por la red creada por docker a la base de datos usando el puerto 27017, pero si se quiere observar la base de datos
usando Mongo Compass por fuera del Docker, se puede hacer desde el puerto 27016

### Tecnologias utilizadas

- Lenguaje: Kotlin
- Framework: Spring Boot
- Base de datos: MongoDB
- Build: Gradle
- Deploy: Docker/ docker-compose
- Arquitectura: Arquitectura de 3 capas con Domain Driven Development(ver carpeta diagramas)

##### Dependencias

- Spring boot starter data MongoDB
- Spring boot starter web
- Spring boot starter actuator
- Spring boot starter test
- Kotlin reflect
- Kotlin stdlib
- Spring boot gradle plugin
- Springdoc openapi ui
- flapdoodle embed mongo (para testear en una base embebida)


### Endpoints

La aplicacion corre en el puerto 8080.

##### Swagger
- /swagger-ui.html -GET

##### Health Check base de datos
- /actuator/health/mongo -GET

##### Creacion de sucursal
Creacion de sucursal, todos los campos del body son obligatorios.
###### Request
- /api/sucursal -POST
- Body: {
    direccion: String,
    latitud: Int/Double,
    longitud: Int/Double
  }
- Ejemplo de Body: {
  "direccion": "Florida 1432",
  "latitud": "1432.23,
  "longitud": -1000
}
###### Response
- Body: {
  id: "dso1093ds9128ifw9si", (id generado en base a timestamp de MongoDB)
  direccion: "Florida 1432",
  "latitud": 1432.23,
  "longitud": -1000
}

##### Buscar sucursal por Id
###### Request
- /api/sucursal/{id} -GET
###### Response
- Body: {
  id: "dso1093ds9128ifw9si",
  direccion: "Florida 1432",
  "latitud": 1432.23,
  "longitud": -1000
}

##### Buscar todas las sucursales.
Agregue este endpoint para facilitar la busqueda por id, que es medio engorrosa con los ids de MongoDB.
###### Request
- /api/sucursal -GET
###### Response
- Body: [{
  id: "dso1093ds9128ifw9si",
  direccion: "Florida 1432",
  "latitud": 1432.23,
  "longitud": -1000
}]

##### Buscar sucursal por Id
Los parametros de url son ambos obligatorios.
###### Request
- /api/sucursal/buscarMasCercana?latitud=500&longitud=200 -GET
###### Response
- Body: {
  id: "dso1093ds9128ifw9si",
  direccion: "Florida 1432",
  "latitud": 600
  "longitud": 200,
  "distancia: 100
}



