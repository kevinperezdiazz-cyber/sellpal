# Despliegue de SELLPAL en Render + Neon (gratis)

## 1. Base de datos en Neon
1. Crear proyecto en neon.com (PostgreSQL).
2. Connection Details: **apagar "Connection pooling"** y copiar host, base, usuario y contraseña.
3. SQL Editor: pegar y ejecutar COMPLETO `database/SELLPAL_TODO_EN_UNO.sql` (no hace falta CREATE DATABASE).
4. URL para Java: `jdbc:postgresql://HOST/BASE?sslmode=require` (sin usuario ni clave dentro, y sin parametros extra).

## 2. GitHub
```
git add .
git commit -m "SELLPAL listo para despliegue"
git push origin main
```
La carpeta raiz del repo debe contener `Dockerfile`, `pom.xml` y `src/`.

## 3. Render
New + > Web Service > repositorio > Language **Docker** > Instance **Free**.
Variables de entorno:

| Variable | Valor |
|---|---|
| SPRING_PROFILES_ACTIVE | postgresql,render |
| DB_URL | jdbc:postgresql://HOST/BASE?sslmode=require |
| DB_USER | usuario de Neon |
| DB_PASSWORD | clave de Neon |

Create Web Service. Cuando los Logs digan "Started SellpalApplication" y el estado sea Live, abrir la URL.

## 4. Pruebas
- Admin: admin@sellpal.co / admin123 (lo crea Spring al arrancar)
- Cliente: compras@elahorro.co / 123456
- Postman: reemplazar `http://localhost:8080` por la URL de Render (Ctrl+Shift+F).

## Notas del plan gratuito
- Render duerme el servicio tras 15 min sin trafico; despertar tarda cerca de 1 min.
- Neon se suspende tras unos minutos; la primera consulta puede tardar unos segundos.
- Las sesiones de login viven en memoria: se pierden cuando el servicio se duerme o se redespliega.
