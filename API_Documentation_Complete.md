# DOCUMENTACIÓN DE CONTRATOS API - SISTEMA CITAS SALUD

## 📋 ESTRUCTURA DE DATOS (DTOs)

### 🔐 **AuthRequest** - Login Request
```json
{
  "email": "string (requerido, email válido)",
  "password": "string (requerido, min 6 caracteres)"
}
```

### 🔐 **TwoFactorRequest** - Verificación 2FA
```json
{
  "email": "string (requerido, email válido)",
  "code": "string (requerido, 6 dígitos)"
}
```

### 🔐 **AuthResponse** - Login Response
```json
{
  "token": "string (JWT token)",
  "type": "Bearer",
  "id": "number (ID del usuario)",
  "nombre": "string",
  "apellido": "string", 
  "email": "string",
  "rolNombre": "string (ADMINISTRADOR|MEDICO|PACIENTE)",
  "permisos": ["string array con permisos del usuario"]
}
```

### 👤 **UsuarioDTO** - Usuario Request/Response
```json
{
  "usuarioId": "number (solo en response)",
  "nombre": "string (requerido, max 80 chars)",
  "apellido": "string (requerido, max 50 chars)",
  "email": "string (requerido, único, max 100 chars)",
  "documento": "string (requerido, max 15 chars)",
  "tipoDocumento": "number (ID del tipo documento: 1=Cédula, 2=Pasaporte, 3=Registro Civil, 4=Tarjeta)",
  "password": "string (requerido en creación, max 80 chars)",
  "telefono": "string (opcional, max 20 chars)",
  "fechaRegistro": "date (yyyy-mm-dd, auto-generado)",
  "ultimoAcceso": "date (yyyy-mm-dd, auto-actualizado)",
  "estado": "number (ID del estado: 1=Activo, 2=Inactivo, 3=Suspendido)",
  "rolId": "number (ID del rol: 1=ADMINISTRADOR, 2=MEDICO, 3=PACIENTE)"
}
```

### 🎭 **RolDTO** - Rol Response
```json
{
  "rolId": "number",
  "nombreRol": "string (ADMINISTRADOR|MEDICO|PACIENTE)",
  "descripcion": "string",
  "permisos": [
    {
      "permisoId": "number",
      "nombrePermiso": "string",
      "descripcion": "string"
    }
  ]
}
```

### 📊 **ActividadUsuarioDTO** - Actividad Response
```json
{
  "actividadId": "number",
  "usuarioId": "number",
  "usuarioEmail": "string",
  "tipoActividad": "string (CREAR_USUARIO|ACTUALIZAR_USUARIO|DESBLOQUEO_USUARIO)",
  "descripcion": "string",
  "fechaHora": "datetime (yyyy-mm-ddThh:mm:ss)",
  "detalleAdiccionales": "string"
}
```

### 🔒 **RegistroAccesoDTO** - Registro Acceso Response
```json
{
  "accesoId": "number",
  "usuarioId": "number",
  "usuarioEmail": "string",
  "fechaHora": "datetime (yyyy-mm-ddThh:mm:ss)",
  "direccionIp": "string",
  "estado": "string (OK|2FA_ENVIADO|LOGIN_OK|CREDENCIALES_INVALIDAS|2FA_INVALIDO|USUARIO_NO_ENCONTRADO)",
  "codigoError": "string (opcional)"
}
```

## 🚦 **CÓDIGOS DE ESTADO HTTP**

| Código | Significado | Descripción |
|--------|-------------|-------------|
| 200 | OK | Operación exitosa |
| 201 | Created | Recurso creado exitosamente |
| 400 | Bad Request | Datos inválidos o faltantes |
| 401 | Unauthorized | No autenticado o credenciales inválidas |
| 403 | Forbidden | Sin permisos para realizar la operación |
| 404 | Not Found | Recurso no encontrado |
| 500 | Internal Server Error | Error interno del servidor |

## 🔑 **SISTEMA DE PERMISOS**

### **ADMINISTRADOR** - Permisos Completos
- CREAR_USUARIO
- MODIFICAR_USUARIO  
- ELIMINAR_USUARIO
- VISUALIZAR_TODOS_USUARIOS
- ASIGNAR_ROLES
- GESTIONAR_CITAS
- GESTIONAR_MEDICOS
- GESTIONAR_ESPECIALIDADES
- GESTIONAR_HORARIOS
- VER_REPORTES

### **MEDICO** - Permisos Médicos
- VER_CITAS_ASIGNADAS
- ACTUALIZAR_ESTADO_CITA
- REGISTRAR_OBSERVACIONES
- VER_HISTORIAL_PACIENTE
- GESTIONAR_HORARIO_PROPIO

### **PACIENTE** - Permisos Básicos
- SOLICITAR_CITA
- CANCELAR_CITA_PROPIA
- VER_CITAS_PROPIAS
- ACTUALIZAR_DATOS_PROPIOS

## 🔐 **AUTENTICACIÓN JWT**

### **Headers Requeridos:**
```
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

### **Flujo de Autenticación:**
1. **POST /api/auth/login** - Envía credenciales → Recibe código 2FA
2. **POST /api/auth/verify-2fa** - Envía código → Recibe JWT token
3. **Usar JWT** en header Authorization para todas las demás peticiones

## 📝 **VALIDACIONES DE CAMPOS**

### **Campos Obligatorios:**
- **Usuario**: nombre, apellido, email, documento, tipoDocumento, password, estado, rolId
- **Auth**: email, password
- **2FA**: email, code

### **Formatos Específicos:**
- **Email**: Debe ser un email válido
- **Password**: Mínimo 6 caracteres
- **Documento**: Máximo 15 caracteres
- **Teléfono**: Máximo 20 caracteres
- **Código 2FA**: Exactamente 6 dígitos

## 🌐 **URLs BASE**

### **Desarrollo:**
- Base URL: `http://localhost:8080/api`
- Swagger: `http://localhost:8080/api/doc/swagger-ui.html`

### **Producción (Codespaces):**
- Base URL: `https://tu-codespace.app.github.dev/api`
- Swagger: `https://tu-codespace.app.github.dev/api/doc/swagger-ui.html`

## 📄 **RESPUESTAS PAGINADAS**

### **Estructura de Respuesta Paginada:**
```json
{
  "content": [/* array de objetos */],
  "currentPage": 0,
  "totalItems": 100,
  "totalPages": 5
}
```

### **Parámetros de Paginación:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 20)

## ⚠️ **ERRORES COMUNES**

### **Error 401 - No Autorizado:**
```json
{
  "path": "/api/endpoint",
  "details": "Full authentication is required to access this resource",
  "error": "Unauthorized", 
  "message": "No está autorizado para acceder a este recurso. Por favor inicie sesión.",
  "status": 401,
  "timestamp": "2025-06-15T10:30:00"
}
```

### **Error 403 - Sin Permisos:**
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Acceso denegado: No tiene los permisos necesarios para acceder a este recurso",
  "path": "/api/endpoint",
  "timestamp": "2025-06-15T10:30:00"
}
```

## 🔄 **CASOS DE USO TÍPICOS**

### **1. Login Completo:**
```bash
# Paso 1: Login inicial
POST /api/auth/login
{"email": "admin@citasalud.com", "password": "admin123"}
# Response: "Código de verificación enviado"

# Paso 2: Verificar 2FA (código se muestra en consola)
POST /api/auth/verify-2fa  
{"email": "admin@citasalud.com", "code": "123456"}
# Response: JWT token y datos del usuario
```

### **2. Crear Usuario (como Admin):**
```bash
POST /api/usuarios
Headers: Authorization: Bearer {jwt_token}
{
  "nombre": "Dr. Juan",
  "apellido": "Pérez", 
  "email": "dr.juan@hospital.com",
  "documento": "98765432",
  "tipoDocumento": 1,
  "password": "medico123",
  "telefono": "3009876543",
  "estado": 1,
  "rolId": 2
}
```

### **3. Ver Mis Actividades (como Usuario):**
```bash
GET /api/actividad-usuario/usuario/{mi_id}
Headers: Authorization: Bearer {jwt_token}
```

---

**📌 Nota:** Este sistema usa autenticación de dos factores (2FA) obligatoria. El código se muestra en la consola del servidor para propósitos de desarrollo.
