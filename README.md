# EP21F1citasalud-back
Proyecto Fabrica Escuela 2025-1 Citas Salud Feature 1. Desarrollando solución web de auto agendamiento médico que permita a los usuarios de EPS gestionar sus citas de forma autónoma, eficiente y segura, mejorando los procesos administrativos de la IPS y cumpliendo con la normativa del sistema de salud colombiano.

## Configuración de Variables de Entorno

Este proyecto utiliza variables de entorno para gestionar la configuración sensible. Para configurar el proyecto:

1. Crea un archivo `.env` en la raíz del proyecto siguiendo el formato del archivo `.env.example`
2. Configura las siguientes variables (todas son **obligatorias**)
3. La aplicación validará que estas variables estén configuradas al iniciar

### Variables Requeridas

| Variable          | Descripción                                  | Ejemplo                                        |
|-------------------|----------------------------------------------|------------------------------------------------|
| DB_URL            | URL de conexión a la base de datos           | jdbc:postgresql://localhost:5432/bd_citassalud |
| DB_USERNAME       | Usuario de la base de datos                  | postgres                                       |
| DB_PASSWORD       | Contraseña de la base de datos               | tu_contraseña                                  |
| JWT_SECRET        | Clave secreta para firmar tokens JWT         | clave_secreta_larga_y_segura                   |
| JWT_EXPIRATION    | Tiempo de expiración del token en ms         | 86400000 (24 horas)                            |
| SERVER_PORT       | Puerto del servidor                          | 8080                                           |
| CONTEXT_PATH      | Ruta base para los endpoints de la API       | /api                                           |
| SWAGGER_SERVER_URL| URL base para la documentación Swagger       | http://localhost:8080/api                       |
