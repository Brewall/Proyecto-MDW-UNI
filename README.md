# Sistema de Apuestas Deportivas
Aplicación web de casa de apuestas desarrollada con Spring Boot 3.5.4 y Java 21. El sistema permite a los usuarios realizar apuestas en eventos deportivos, gestionar su saldo mediante depósitos y retiros, y enviar reclamos al equipo de soporte.

## Descripción del Proyecto

Este proyecto es una plataforma completa de apuestas deportivas que incluye:

- **Sistema de usuarios con roles**: Usuarios regulares (USER) que pueden apostar y personal de soporte (SUPPORT) con panel administrativo
- **Gestión de eventos deportivos**: Creación y administración de eventos con múltiples cuotas de apuesta
- **Sistema de apuestas**: Los usuarios pueden realizar, cancelar y consultar sus apuestas
- **Gestión financiera**: Depósitos, retiros y seguimiento completo de transacciones
- **Sistema de reclamos**: Canal de comunicación entre usuarios y equipo de soporte
- **Panel administrativo**: Herramientas para el equipo de soporte para gestionar usuarios y responder reclamos
- **Seguridad robusta**: Autenticación con Spring Security, encriptación BCrypt y control de acceso basado en roles
- **Internacionalización**: Soporte para español e inglés

## Requisitos del Sistema

- Java 21 o superior
- PostgreSQL 17 o superior
- Gradle 8 o superior
- IDE compatible con Java (IntelliJ IDEA, Eclipse, VS Code)

## Configuración Inicial

### 1. Clonar el repositorio

```bash
git clone https://github.com/Brewall/Proyecto-MDW-UNI.git
cd Proyecto-MDW-UNI
```

### 2. Configurar Base de Datos

#### Opción A: PostgreSQL Local

1. Instalar PostgreSQL 17
2. Crear una base de datos:

```sql
CREATE DATABASE bd_apuestas;
```

#### Opción B: PostgreSQL en la Nube

Utilizar un proveedor como Aiven, Neon, o Supabase y obtener las credenciales de conexión.

### 3. Configurar Variables de Entorno

El proyecto utiliza variables de entorno para las credenciales de la base de datos.

#### Paso 1: Crear archivo .env

En la raíz del proyecto, copiar el archivo de ejemplo:

```bash
cp .env.example .env
```

#### Paso 2: Editar .env con tus credenciales

Abrir el archivo `.env` y configurar los valores:

```properties
# PostgreSQL LOCAL:
DB_URL=jdbc:postgresql://localhost:5432/bd_apuestas
DB_USERNAME=postgres
DB_PASSWORD=tu_contraseña_local

# PostgreSQL en la NUBE (ejemplo):
# DB_URL=jdbc:postgresql://tu-host.aivencloud.com:12345/defaultdb?ssl=require
# DB_USERNAME=avnadmin
# DB_PASSWORD=tu_contraseña_cloud

# Puerto del servidor (opcional, por defecto 8080)
SERVER_PORT=8080
```


#### Paso 3: Cargar variables de entorno

**Windows (PowerShell):**

```powershell
# Cargar variables desde .env
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.*)$') {
        [Environment]::SetEnvironmentVariable($matches[1], $matches[2], "Process")
    }
}
```

**Linux/Mac (Bash):**

```bash
export $(grep -v '^#' .env | xargs)
```

**Desde IDE (IntelliJ/Eclipse):**

Configurar las variables en Run Configuration → Environment Variables

### 4. Ejecutar la Aplicación

```bash
cd app

# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### 5. Acceder a la Aplicación

Abrir el navegador en: **http://localhost:8080**

**Credenciales por defecto:**

- **Usuario de soporte**: soporte@casino.com / soporte123
- **Usuarios regulares**: Crear una cuenta desde la página de registro

## Estructura del Proyecto

```
Proyecto-MDW-UNI/
├── .env.example              # Plantilla de variables de entorno
├── .gitignore                # Archivos ignorados por Git
├── README.md                 # Este archivo
├── run.bat                   # Script de ejecución para Windows
├── run.sh                    # Script de ejecución para Linux/Mac
└── app/
    ├── build.gradle          # Configuración de dependencias
    ├── gradlew / gradlew.bat # Gradle Wrapper
    ├── src/
    │   ├── main/
    │   │   ├── java/com/example/app/
    │   │   │   ├── AppApplication.java      # Clase principal
    │   │   │   ├── config/                  # Configuración (Security, DataInitializer)
    │   │   │   ├── controller/              # Controladores web
    │   │   │   │   └── web/                 # Controladores de vistas
    │   │   │   ├── model/                   # Entidades JPA (Usuario, Apuesta, Evento, etc.)
    │   │   │   ├── repository/              # Repositorios Spring Data JPA
    │   │   │   ├── service/                 # Lógica de negocio
    │   │   │   │   ├── interfaces/          # Interfaces de servicios
    │   │   │   │   └── impl/                # Implementaciones
    │   │   │   └── util/                    # Utilidades (EventoGenerator)
    │   │   └── resources/
    │   │       ├── application.properties   # Configuración de Spring Boot
    │   │       ├── messages.properties      # Textos en español
    │   │       ├── messages_en.properties   # Textos en inglés
    │   │       └── templates/               # Vistas Thymeleaf
    │   │           ├── apuestas.html
    │   │           ├── dashboard.html
    │   │           ├── login.html
    │   │           ├── perfil.html
    │   │           ├── reclamos.html
    │   │           ├── registro.html
    │   │           ├── transacciones.html
    │   │           └── support/             # Vistas del panel de soporte
    │   └── test/                            # Tests unitarios
    └── build/                               # Archivos compilados
```

## Arquitectura y Tecnologías

### Arquitectura

El proyecto sigue el patrón **MVC (Model-View-Controller)** con arquitectura en capas:

- **Capa de Presentación**: Thymeleaf templates + Bootstrap 5
- **Capa de Controlador**: Spring MVC Controllers
- **Capa de Servicio**: Lógica de negocio (interfaces + implementaciones)
- **Capa de Persistencia**: Spring Data JPA + Repositorios
- **Capa de Modelo**: Entidades JPA

### Stack Tecnológico

| Componente          | Tecnología             | Versión |
|---------------------|------------------------|---------|
| Lenguaje            | Java                   | 21      |
| Framework Backend   | Spring Boot            | 3.5.4   |
| Framework Web       | Spring MVC             | -       |
| Motor de Templates  | Thymeleaf              | -       |
| Frontend            | Bootstrap              | 5       |
| Seguridad           | Spring Security        | 6.x     |
| ORM                 | Hibernate (JPA)        | -       |
| Base de Datos       | PostgreSQL             | 17+     |
| Herramienta de Build| Gradle                 | 8+      |
| Variables de Entorno| spring-dotenv          | 4.0.0   |

## Modelo de Datos

El sistema utiliza 6 entidades principales:

### Usuario
- Almacena información de usuarios (USER y SUPPORT)
- Estados: ACTIVO, INHABILITADO
- Roles: USER (puede apostar), SUPPORT (administración)

### Evento
- Eventos deportivos disponibles para apuestas
- Estados: PROGRAMADO, EN_CURSO, FINALIZADO
- Relación OneToMany con Cuota

### Cuota
- Diferentes opciones de apuesta para cada evento
- Cada cuota tiene una descripción y valor (multiplicador)
- Estados: DISPONIBLE, NO_DISPONIBLE

### Apuesta
- Registro de apuestas realizadas por usuarios
- Estados: PENDIENTE, GANADA, PERDIDA, CANCELADA
- Calcula ganancia potencial automáticamente

### Transaccion
- Historial completo de movimientos financieros
- Tipos: DEPOSITO, RETIRO, APUESTA, GANANCIA
- Permite trazabilidad de todas las operaciones

### Reclamo
- Sistema de tickets de soporte
- Estados: PENDIENTE, EN_REVISION, RESUELTO
- Categorías: PROBLEMA_TECNICO, PROBLEMA_PAGO, CONSULTA_GENERAL, SUGERENCIA, RECLAMO_APUESTA

## Funcionalidades

### Para Usuarios (Rol USER)

1. **Autenticación y Registro**
   - Registro de nuevos usuarios
   - Login con email y contraseña
   - Encriptación de contraseñas con BCrypt

2. **Dashboard**
   - Visualización de eventos deportivos disponibles
   - Consulta de apuestas recientes y pendientes
   - Acceso rápido a todas las funcionalidades

3. **Gestión de Apuestas**
   - Realizar apuestas en eventos programados
   - Ver historial completo de apuestas
   - Filtrar apuestas por estado y deporte
   - Cancelar apuestas pendientes (con devolución de saldo)
   - Visualización de ganancias potenciales

4. **Gestión Financiera**
   - Depositar saldo en la cuenta
   - Retirar saldo disponible
   - Ver historial de transacciones
   - Filtrar transacciones por tipo y fechas
   - Visualización de balance y estadísticas

5. **Sistema de Reclamos**
   - Crear reclamos por diferentes categorías
   - Seguimiento de estado de reclamos
   - Ver respuestas del equipo de soporte
   - Editar reclamos pendientes

6. **Perfil de Usuario**
   - Ver información personal
   - Estadísticas de apuestas (ganadas/perdidas)
   - Gestión de saldo

### Para Soporte (Rol SUPPORT)

1. **Dashboard Administrativo**
   - Estadísticas generales del sistema
   - Resumen de usuarios y reclamos
   - Acceso rápido a reclamos pendientes

2. **Gestión de Usuarios**
   - Listar todos los usuarios
   - Buscar usuarios por nombre o email
   - Filtrar por estado (ACTIVO/INHABILITADO)
   - Cambiar estado de usuarios
   - Ver detalles completos de cada usuario

3. **Gestión de Reclamos**
   - Ver todos los reclamos del sistema
   - Filtrar por estado y categoría
   - Cambiar estado de reclamos
   - Responder reclamos
   - Marcar reclamos como resueltos

## Seguridad

### Autenticación y Autorización

- **Spring Security** para gestión de sesiones y autenticación
- **BCrypt** para hash seguro de contraseñas
- Control de acceso basado en roles (RBAC)
- Protección CSRF habilitada
- Sesiones seguras con cookies HTTP-only

### Control de Acceso por Rutas

```
Rutas Públicas:
  - /login, /registro
  - /css/**, /js/**, /images/**

Solo Rol USER:
  - /apuestas/**
  - /perfil/depositar, /perfil/retirar

Solo Rol SUPPORT:
  - /support/**

Rutas Autenticadas (ambos roles):
  - /dashboard, /perfil
  - /transacciones, /reclamos
```

### Validaciones de Seguridad

- Usuarios INHABILITADOS pueden hacer login pero no apostar
- Validación de saldo antes de realizar apuestas
- Verificación de permisos en operaciones sensibles
- Validación de estados antes de cambios críticos

## Configuración de la Base de Datos

La aplicación utiliza las siguientes configuraciones en `application.properties`:

```properties
# Conexión a PostgreSQL (usa variables de entorno)
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Hibernate
spring.jpa.hibernate.ddl-auto=update  # Crea/actualiza tablas automáticamente
spring.jpa.show-sql=false             # No mostrar SQL en consola
spring.jpa.open-in-view=false         # Prevenir lazy loading fuera de transacciones

# Connection Pool (optimizado para BD en la nube)
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
```

## Internacionalización

El sistema soporta múltiples idiomas:

- **Español** (por defecto): `messages.properties`
- **Inglés**: `messages_en.properties`

Configuración en `application.properties`:

```properties
spring.messages.basename=messages
spring.messages.encoding=UTF-8
spring.web.locale=es
```

## Solución de Problemas

### Error de conexión a la base de datos

1. Verificar que PostgreSQL esté ejecutándose
2. Verificar que las variables de entorno estén cargadas:

   ```powershell
   echo $env:DB_URL
   echo $env:DB_USERNAME
   ```
>>>>>>> 1adbe27 (Refactorizacion del proyecto y documentacion)

3. Para bases de datos en la nube, asegurar que la URL incluya `?ssl=require`

### Puerto 8080 ya está en uso

Cambiar el puerto en el archivo `.env`:

```properties
SERVER_PORT=8081
```

### Errores al compilar

Limpiar y reconstruir el proyecto:

```bash
cd app
.\gradlew.bat clean build
```

### Usuario de soporte no existe

El usuario de soporte se crea automáticamente al iniciar la aplicación. Si no existe, verificar los logs de la aplicación.

## Desarrollo

### Ejecutar en modo desarrollo

```bash
cd app
.\gradlew.bat bootRun
```

### Compilar el proyecto

```bash
cd app
.\gradlew.bat build
```

### Ejecutar tests

```bash
cd app
.\gradlew.bat test
```

### Generar JAR ejecutable

```bash
cd app
.\gradlew.bat bootJar
```

El archivo JAR se generará en `app/build/libs/`

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## Licencia

Este proyecto es parte de un trabajo académico para el curso de Marcos de Desarrollo Web.

## Autores

Proyecto desarrollado como parte del curso de Marcos de Desarrollo Web - UNI

## Contacto

Para consultas o soporte, abrir un issue en el repositorio de GitHub.
