# COSEVIF - Backend

Backend del sistema **COSEVIF** para el control y seguimiento de visitas a un fraccionamiento. Desarrollado en **Spring Boot** con autenticación JWT y base de datos **MongoDB**.

## 🛠 Tecnologías

- Java 17 en adelante
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- MongoDB
- Maven

## 📦 Estructura General

```
src/
 └── main/
     ├── java/
     │    └── utez.edu.mx.cosevif/
     │         ├── controller/
     │         ├── service/
     │         ├── repository/
     │         ├── model/
     │         └── security/
     └── resources/
          ├── application.properties
```

## 🚀 Cómo ejecutar

1. Clona el repositorio:

```bash
git clone https://github.com/Jassiel75/cosevif.git
cd cosevif
```

2. Abre el proyecto con IntelliJ o tu IDE preferido.
3. Asegúrate de tener una base de datos MongoDB corriendo localmente.
4. Ejecuta la clase `CosevifApplication.java` para iniciar el servidor.

## 🔐 Roles y Autenticación

- `ADMIN` – solo accede por la plataforma web.
- `RESIDENT` – puede registrar visitas y trabajadores (vía app móvil).
- `GUARD` – funcionalidad futura para escaneo de QR y validación (móvil).

## 📌 Estado actual

- Autenticación con JWT funcional
- CRUD de Visitas
- CRUD de Visitas de Trabajadores (con imagen)
- Subida de imágenes como base64
- Endpoints protegidos por rol

> 🔧 El backend está en desarrollo activo.