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
     
Despues debes moverte al directorio raiz del proyecto, para hacerlo puedes usar el siguiente comando:
  
    `cd global-logic-challenge`

2. Construir el Proyecto

Para construir el proyecto con Gradle, ejecuta el siguiente comando en la raíz del proyecto:

   Unix  
    `./gradlew build`
   
   Windows  
    `gradlew.bat build` 

Esto descargará las dependencias necesarias y compilará el proyecto.

3. Ejecutar el Proyecto 
 
Para ejecutar el microservicio de manera local, usa el siguiente comando:
    
  Unix   
   `./gradlew bootRun`
   
  Windows  
   `gradlew.bat bootRun`  
   
El servidor Spring Boot se iniciará y estará disponible en `http://localhost:8099`, si prefieres cambiar el puerto puedes cambiarlo en el archivo de configuracion `application.yaml` (server.port)

4. Configuración del Proyecto

El archivo application.yaml contiene las configuraciones necesarias para la conexión con la base de datos y otros parámetros.

La base de datos en memoria (H2), ya está preconfigurada de la siguiente manera:

* Base de datos url = jdbc:h2:mem:userdb  
* Username = admin  
* Password = 1234  

NOTA: con estas mismas credenciales podrás acceder a la consola web de h2 (una vez que el proyecto este siendo ejecutado localmente), entrando a: 

`http://localhost:8099/h2-console/`  

Una vez en la consola de H2 puedes ejecutar los siguientes comandos para revisar los registros insertados en la base de datos (después de haber ejecutado el primer request POST para crear un user):

`SELECT * FROM user;`

NOTA: el password enviado en el body del POST es encriptado al guardarse en la base y luego desencriptado cuando se ejecuta el GET, para quien consume los endpoints este proceso no queda a simple vista pero con esta consulta sobre la tabla `user` podrás ver el password encriptado  

Si el usuario creado tiene uno o más teléfonos asociados puedes revisar esos registros también ejecutando:  

`SELECT * FROM phone;`

5. Probar los Endpoints

Una vez que el servidor esté en ejecución, podrás interactuar con los siguientes endpoints:

POST `http://localhost:8099/users/sign-up` : Crear un nuevo usuario. Este endpoint no cuenta con proceso de autenticación

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

GET `http://localhost:8099/users/login` : obtener los detalles de un usuario por su uuid, para ser consumido debemos tomar el atributo `token` del response del endpoint anterior y colocarlo en el `Authorization` header del request con el prefijo `Bearer `, o si estamos usando un cliente como `Postman` podemos ir a la pestaña de "Auth" -> "Auth Type" -> "Bearer Token". Luego en el apartado "Token" colocar el valor del token, en este caso no hace falta el prefijo "Bearer " ya que Postman lo hace por nosotros. 

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

NOTA: En la carpeta `diagrams` dentro de la raíz del proyecto se encuentran los diagramas de secuencia y de componentes del proyecto