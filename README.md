# 🎰 Sistema de Apuestas Deportivas

Aplicación web de casa de apuestas desarrollada con Spring Boot 3.5.4 y Java 21.

## 📋 Requisitos

- Java 21+
- PostgreSQL 17+
- Gradle 8+

## 🚀 Configuración Inicial

### 1. Clonar el repositorio

```bash
git clone https://github.com/Brewall/Proyecto-MDW-UNI.git
cd Proyecto-MDW-UNI
```

### 2. Configurar variables de entorno

Copia el archivo de ejemplo y configura tus credenciales:

```bash
cp .env.example .env
```

Edita el archivo `.env` con tus credenciales reales de PostgreSQL:

```properties
DB_URL=jdbc:postgresql://your-host:port/database_name?ssl=require
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
SERVER_PORT=8080
```

**⚠️ IMPORTANTE:** El archivo `.env` contiene credenciales sensibles y **NO debe subirse a Git** (ya está en `.gitignore`).

### 3. Ejecutar la aplicación

#### Opción 1: Con Gradle

```bash
cd app
./gradlew bootRun   # Linux/Mac
.\gradlew.bat bootRun   # Windows
```

#### Opción 2: Desde IDE (Eclipse/IntelliJ)

1. Importar como proyecto Gradle
2. Configurar JAVA_HOME apuntando a JDK 21
3. Ejecutar `AppApplication.java`

### 4. Acceder a la aplicación

Abre tu navegador en: **http://localhost:8080**

## 📁 Estructura del Proyecto

```
app/
├── src/main/java/com/example/app/
│   ├── controller/       # Controladores web
│   ├── model/           # Entidades JPA
│   ├── repository/      # Repositorios
│   ├── service/         # Lógica de negocio
│   └── util/            # Utilidades
├── src/main/resources/
│   ├── application.properties
│   ├── messages*.properties
│   └── templates/       # Vistas Thymeleaf
└── build.gradle
```

## 🛠️ Tecnologías

- **Backend:** Spring Boot 3.5.4 (Spring Data JPA, Spring Web)
- **Frontend:** Thymeleaf + Bootstrap 5
- **Base de Datos:** PostgreSQL 17
- **Build Tool:** Gradle
- **Java:** 21

## 🔐 Seguridad

- ✅ Credenciales externalizadas (variables de entorno)
- ⚠️ Contraseñas sin encriptar (pendiente Spring Security)
- ⚠️ Sin protección CSRF (pendiente)

## 📝 Características

- ✅ Autenticación de usuarios
- ✅ Sistema de apuestas deportivas
- ✅ Gestión de transacciones (depósitos/retiros)
- ✅ Sistema de reclamos
- ✅ Internacionalización (ES/EN)
- ✅ Dashboard con estadísticas

## 🤝 Contribuir

1. Crear una rama: `git checkout -b feature/nueva-funcionalidad`
2. Commit cambios: `git commit -m 'Agregar nueva funcionalidad'`
3. Push: `git push origin feature/nueva-funcionalidad`
4. Crear Pull Request

## 📄 Licencia

Este proyecto es de uso académico.
