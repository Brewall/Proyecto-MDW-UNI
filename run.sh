#!/bin/bash
# Script para ejecutar la aplicación con variables de entorno

# Configurar Java (ajustar según tu instalación)
export JAVA_HOME="/usr/lib/jvm/java-21-openjdk"  # Linux
# export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home"  # macOS

# Cargar variables de entorno desde archivo .env
if [ -f .env ]; then
    echo "Cargando variables desde .env..."
    export $(cat .env | grep -v '^#' | xargs)
else
    echo "ADVERTENCIA: Archivo .env no encontrado. Usando valores por defecto."
    export DB_URL="jdbc:postgresql://localhost:5432/bd_apuestas"
    export DB_USERNAME="postgres"
    export DB_PASSWORD="password"
    export SERVER_PORT="8080"
fi

# Ejecutar aplicación
cd app
./gradlew bootRun
