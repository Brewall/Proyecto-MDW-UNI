# Sistema de Apuestas Deportivas

Aplicación web de casa de apuestas desarrollada con Spring Boot 3.5.4 y Java 21.

## Requisitos

- Java 21+
- PostgreSQL 17+
- Gradle 8+

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

#### Opción B: PostgreSQL en la Nube (Aiven, Neon, etc.)

Obtener las credenciales de conexión desde el panel de tu proveedor cloud.

### 3. Configurar Variables de Entorno

El proyecto usa variables de entorno para las credenciales de la base de datos. **Nunca subas credenciales al repositorio.**

#### Paso 1: Crear archivo `.env`

En la raíz del proyecto, copia el archivo de ejemplo:

```bash
cp .env.example .env
```

#### Paso 2: Editar `.env` con tus credenciales

Abre el archivo `.env` y configura tus valores:

```properties
# Para PostgreSQL LOCAL:
DB_URL=jdbc:postgresql://localhost:5432/bd_apuestas
DB_USERNAME=postgres
DB_PASSWORD=tu_contraseña_local

# Para PostgreSQL en la NUBE (ejemplo Aiven):
# DB_URL=jdbc:postgresql://tu-host.aivencloud.com:12345/defaultdb?ssl=require
# DB_USERNAME=avnadmin
# DB_PASSWORD=tu_contraseña_cloud

# Puerto del servidor (opcional, por defecto 8080)
SERVER_PORT=8080
```

**Importante:** El archivo `.env` está en `.gitignore` y NO se sube al repositorio.

#### Paso 3: Cargar variables de entorno

**Windows (PowerShell):**
```powershell
# Opción 1: Cargar manualmente antes de ejecutar
$env:DB_URL="jdbc:postgresql://localhost:5432/bd_apuestas"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="tu_contraseña"

# Opción 2: Usar script para cargar desde .env
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.*)$') {
        [Environment]::SetEnvironmentVariable($matches[1], $matches[2], "Process")
    }
}
```

**Linux/Mac (Bash):**
```bash
# Cargar variables desde .env
export $(grep -v '^#' .env | xargs)
```

**Desde IDE (IntelliJ/Eclipse):**
- Configurar las variables en Run Configuration > Environment Variables

### 4. Ejecutar la Aplicación

```bash
cd app

# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### 5. Acceder a la Aplicación

Abre tu navegador en: **http://localhost:8080**

**Usuarios de prueba:**
- Usuario regular: Regístrate desde la página de registro
- Panel de soporte: `/support/dashboard` (requiere rol SOPORTE en BD)

## Estructura del Proyecto

```
Proyecto-MDW-UNI/
├── .env.example          # Plantilla de variables de entorno
├── .gitignore
├── README.md
└── app/
    ├── build.gradle
    ├── src/main/java/com/example/app/
    │   ├── config/           # Configuración (Security, etc.)
    │   ├── controller/       # Controladores web y API
    │   ├── dto/              # Objetos de transferencia
    │   ├── model/            # Entidades JPA
    │   ├── repository/       # Repositorios Spring Data
    │   ├── service/          # Lógica de negocio
    │   └── util/             # Utilidades
    └── src/main/resources/
        ├── application.properties
        ├── messages.properties      # Español
        ├── messages_en.properties   # Inglés
        └── templates/               # Vistas Thymeleaf
```

## Tecnologías

| Componente | Tecnología |
|------------|------------|
| Backend | Spring Boot 3.5.4 |
| Frontend | Thymeleaf + Bootstrap 5 |
| Base de Datos | PostgreSQL 17 |
| Seguridad | Spring Security + BCrypt |
| Build Tool | Gradle |
| Java | 21 |

## Funcionalidades

- Autenticación y registro de usuarios
- Sistema de apuestas deportivas
- Gestión de transacciones (depósitos/retiros)
- Sistema de reclamos y soporte
- Panel de administración (soporte)
- Internacionalización (Español/Inglés)
- Dashboard con estadísticas

## Seguridad

- Contraseñas encriptadas con BCrypt
- Credenciales externalizadas en variables de entorno
- Protección de rutas con Spring Security
- Validación de sesiones

## Solución de Problemas

### Error de conexión a la base de datos

1. Verificar que PostgreSQL esté corriendo
2. Verificar las variables de entorno estén cargadas:
   ```powershell
   echo $env:DB_URL
   ```
3. Para bases de datos en la nube, verificar que `?ssl=require` esté en la URL

