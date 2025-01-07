###Microservicio para la Creación y Consulta de Usuarios

El presente proyecto es un microservicio desarrollado con Spring Boot (versión 2.5.14) y Gradle (versión 7.4) para la gestión de usuarios. Permite la creación y consulta de usuarios, además de la gestion de los teléfonos asociados a cada usuario.

###Requisitos   
* Java 11 
* Gradle 7.4
* Spring Boot 2.5.14

###Descripción  
Este microservicio proporciona endpoints para crear y consultar usuarios. Además, cada usuario puede tener múltiples teléfonos asociados. Las entidades principales son:

* UserEntity: representa al usuario con atributos como userUuid, name, email, password, createdAt, lastLogin, y isActive.
* PhoneEntity: representa los teléfonos asociados a los usuarios, donde cada teléfono tiene una relación con un UserEntity a través de una llave foránea.

Instrucciones para construcción y ejecución local

1. Clonar el repositorio

Primero, clona el repositorio desde GitHub con el siguiente comando:

     `git clone https://github.com/JuanPabloGuirado/global-logic-challenge.git`

2. Construir el Proyecto

Para construir el proyecto con Gradle, ejecuta el siguiente comando en la raíz del proyecto:

    `./gradlew build`

Esto descargará las dependencias necesarias y compilará el proyecto.

3. Configuración del Proyecto

El archivo application.yaml contiene las configuraciones necesarias para la conexión con la base de datos y otros parámetros.

La base de datos en memoria (H2), ya está preconfigurada de la siguiente manera:

* Base de datos url = jdbc:h2:mem:userdb  
* Username = admin  
* Password = 1234  

NOTA: con estas mismas credenciales podras acceder a la consola web de h2 (una vez que el proyecto este siendo ejecutado localmente), entrando a: 

`http://localhost:8080/h2-console/`

4. Ejecutar el Proyecto  
Para ejecutar el microservicio de manera local, usa el siguiente comando:

    `./gradlew bootRun`
    
El servidor Spring Boot se iniciará y estará disponible en `http://localhost:8090`, si prefieres cambiar el puerto puedes cambiarlo en el archivo de configuracion `application.yaml` (server.port)

5. Probar los Endpoints

Una vez que el servidor esté en ejecución, podrás interactuar con los siguientes endpoints:

POST `http://localhost:8090/users/sign-up` : Crear un nuevo usuario. Este endpoint no cuenta con proceso de autenticacion

Body de ejemplo (json): 

Request: 
```
{
    "name": "Juan Perez",
    "email": "juan@correo.com",
    "password": "a3asfGfdfdf4",
    "phones": [
        {
            "number": 778779234,
            "cityCode": 99,
            "countryCode": "01"
        }
    ]
}
```

Response:
```
{
    "id": "88dfe109-90e7-436b-a10b-5679f6b1a800",
    "createdAt": "2025-01-07T18:51:35.8606564",
    "lastLogin": "2025-01-07T18:51:35.8606564",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "isActive": true
}
```

GET `http://localhost:8090/users/login` : obtener los detalles de un usuario por su uuid, para ser consumido debemos tomar el atributo `token` del response del endpoint anterior y colocarlo en el `Authorization` header del request con el prefijo `Bearer ` 

Response:
```
{
    "id": "88dfe109-90e7-436b-a10b-5679f6b1a800",
    "createdAt": "2025-01-07T18:51:35.860656",
    "lastLogin": "2025-01-07T18:51:35.860656",
    "token": "eyJhbGciOiJz9KB3TJSitEo...",
    "isActive": true,
    "name": "Juan Perez",
    "email": "juan@correo.com",
    "password": "a3asfGfdfdf4",
    "phones": [
        {
            "number": 778779234,
            "cityCode": 99,
            "countryCode": "01"
        }
    ]
}
```

NOTA: En la carpeta `diagrams` dentro del folder raiz del proyecto se encuentran los diagramas de secuencia y de componentes del proyecto