Microservicio para la Creación y Consulta de Usuarios

El presente proyecto es un microservicio desarrollado con Spring Boot (versión 2.5.14) y Gradle (versión 7.4) para la gestión de usuarios. Permite la creación y consulta de usuarios, además de la gestion de los teléfonos asociados a cada usuario.

Requisitos 
* Java 11 
* Gradle 7.4
* Spring Boot 2.5.14

Descripción
Este microservicio proporciona endpoints para crear y consultar usuarios. Además, cada usuario puede tener múltiples teléfonos asociados. Las entidades principales son:

* UserEntity: representa al usuario con atributos como userUuid, name, email, password, createdAt, lastLogin, y isActive.
* PhoneEntity: representa los teléfonos asociados a los usuarios, donde cada teléfono tiene una relación con un UserEntity a través de una llave foránea.

Instrucciones para Construcción y Ejecución Local
1. Clonar el repositorio
Primero, clona el repositorio desde GitHub (o el repositorio público correspondiente):

## bash
git clone https://github.com/usuario/repositorio.git
cd repositorio
2. Construir el Proyecto
Asegúrate de tener Gradle instalado en tu máquina. Si no lo tienes, puedes descargarlo desde aquí.

Para construir el proyecto con Gradle, ejecuta el siguiente comando en la raíz del proyecto:

bash
Copy code
./gradlew build
Esto descargará las dependencias necesarias y compilará el proyecto.

3. Configuración del Proyecto
El archivo application.properties contiene las configuraciones necesarias para la conexión con la base de datos y otros parámetros.

Si deseas usar una base de datos en memoria (H2), ya está preconfigurado de la siguiente manera:

properties
Copy code
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
Si prefieres usar una base de datos externa, deberás modificar estos valores según sea necesario.

4. Ejecutar el Proyecto
Para ejecutar el microservicio de manera local, usa el siguiente comando:

bash
Copy code
./gradlew bootRun
El servidor Spring Boot se iniciará y estará disponible en http://localhost:8080.

5. Probar los Endpoints
Una vez que el servidor esté en ejecución, podrás interactuar con los siguientes endpoints:

POST /usuarios: Crear un nuevo usuario.
Body de ejemplo:

json
{
  "userUuid": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Juan Pérez",
  "email": "juan.perez@ejemplo.com",
  "password": "password123",
  "createdAt": "2025-01-07T12:00:00",
  "lastLogin": "2025-01-07T12:00:00",
  "isActive": true
}
GET /api/usuarios/{id}: Obtener los detalles de un usuario por su id.

GET /api/usuarios: Obtener la lista de todos los usuarios.

6. Base de Datos y H2 Console
Si estás utilizando H2, puedes acceder a la consola web para ver el contenido de la base de datos en la siguiente URL:

bash
Copy code
http://localhost:8080/h2-console
Asegúrate de usar la URL jdbc:h2:mem:testdb para conectarte.