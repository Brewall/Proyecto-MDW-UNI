@echo off
REM Script para ejecutar la aplicación con variables de entorno

REM Configurar Java
set JAVA_HOME=C:\Program Files\Java\jdk-21

REM Cargar variables de entorno desde archivo .env (si existe)
if exist .env (
    echo Cargando variables desde .env...
    for /f "tokens=1,2 delims==" %%a in (.env) do (
        REM Ignorar líneas de comentarios
        echo %%a | findstr /r "^#" >nul
        if errorlevel 1 (
            set %%a=%%b
        )
    )
) else (
    echo ADVERTENCIA: Archivo .env no encontrado. Usando valores por defecto.
    set DB_URL=jdbc:postgresql://localhost:5432/bd_apuestas
    set DB_USERNAME=postgres
    set DB_PASSWORD=password
    set SERVER_PORT=8080
)

REM Ejecutar aplicación
cd app
call gradlew.bat bootRun

pause
