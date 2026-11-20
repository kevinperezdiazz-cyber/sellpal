# 🛒 SELLPAL — Sistema de Gestión Comercial y Logística

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon_Cloud-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Render_Deploy-2496ED?style=for-the-badge&logo=docker&logoColor=white)

> **Plataforma web integral B2B y B2C** desarrollada para **INVERCAJAS** (distribuidora de productos Mondelez) como proyecto productivo del programa **Tecnólogo en Análisis y Desarrollo de Software (ADSO) — SENA**.

---

## 📋 Descripción del Proyecto

**SELLPAL** digitaliza y centraliza el ciclo operativo de distribución comercial: desde la exploración del catálogo y la generación de pedidos en línea por parte de clientes mayoristas y minoristas, hasta el control de inventario, asignación de repartidores, trazabilidad de entregas y conciliación de pagos en el panel administrativo.

### ✨ Características Principales
* **Portal Comercial (Cliente):** Catálogo interactivo filtrado por categorías, validación de stock en tiempo real, carrito de compras persistente y seguimiento del estado de pedidos.
* **Panel Administrativo:** Gestión CRUD completa de productos, categorías, cargos, empleados, clientes, proveedores, pedidos, entregas y pagos.
* **Control de Accesos y Roles:** Autenticación segura diferenciada entre perfiles `ADMIN` y `CLIENTE`.
* **Arquitectura Cloud-Ready:** Desplegado mediante contenedores Docker en **Render** conectado a **PostgreSQL Serverless en Neon**.

---

## 📂 Estructura del Repositorio

```text
sellpal/
├── src/                        # Código fuente Backend (Spring Boot) y Frontend (HTML/CSS/JS)
├── database/                   # Scripts SQL (MER, DDL, DML y script maestro TODO_EN_UNO.sql)
├── postman/                    # Colecciones de pruebas de API REST (CRUD y flujo completo)
├── docs/                       # Documentación técnica, MER.pdf, Modelo Relacional.pdf y manuales
├── Dockerfile                  # Configuración de compilación Maven (Java 17) y ejecución ligera
├── pom.xml                     # Dependencias y configuración de construcción del proyecto
└── DESPLIEGUE_RENDER_NEON.md   # Guía paso a paso de despliegue en la nube
