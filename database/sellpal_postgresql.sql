-- ============================================================
-- SELLPAL / INVERCAJAS - SCHEMA DEFINITIVO (PostgreSQL)
-- MER aprobado por la instructora - reconstrucción total
-- ============================================================
-- Este script BORRA todo lo anterior y crea la estructura nueva
-- desde cero. Ejecutar completo, de arriba hacia abajo.
-- ============================================================

-- 1. LIMPIEZA TOTAL (borra todo lo que exista con estos nombres)
DROP TABLE IF EXISTS PAGO CASCADE;
DROP TABLE IF EXISTS ENTREGA CASCADE;
DROP TABLE IF EXISTS DETALLE_PEDIDO CASCADE;
DROP TABLE IF EXISTS PEDIDO CASCADE;
DROP TABLE IF EXISTS PRODUCTO CASCADE;
DROP TABLE IF EXISTS CATEGORIA_PRODUCTO CASCADE;
DROP TABLE IF EXISTS ESTADO_PEDIDO CASCADE;
DROP TABLE IF EXISTS PROVEEDOR CASCADE;
DROP TABLE IF EXISTS CLIENTE CASCADE;
DROP TABLE IF EXISTS EMPLEADO CASCADE;
DROP TABLE IF EXISTS PERSONA CASCADE;
DROP TABLE IF EXISTS CARGO CASCADE;

-- ============================================================
-- 2. TABLAS CATÁLOGO / INDEPENDIENTES
-- ============================================================

CREATE TABLE CARGO (
    id_cargo        SERIAL PRIMARY KEY,
    nombre_cargo    VARCHAR(80)  NOT NULL,
    descripcion     VARCHAR(255)
);

CREATE TABLE PROVEEDOR (
    id_proveedor    SERIAL PRIMARY KEY,
    nombre_empresa  VARCHAR(120) NOT NULL,
    contacto        VARCHAR(120)
);

CREATE TABLE ESTADO_PEDIDO (
    id_estado       SERIAL PRIMARY KEY,
    nombre_estado   VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE CATEGORIA_PRODUCTO (
    id_categoria    SERIAL PRIMARY KEY,
    nombre          VARCHAR(80) NOT NULL,
    tipo            VARCHAR(80)
);

-- ============================================================
-- 3. PERSONA (base) Y HERENCIA 1:1 (EMPLEADO / CLIENTE)
-- ============================================================

CREATE TABLE PERSONA (
    documento_unico     BIGINT PRIMARY KEY,
    nombre              VARCHAR(120) NOT NULL,
    direccion           VARCHAR(200),
    telefono            VARCHAR(30),
    email               VARCHAR(120),
    fecha_nacimiento    DATE
);

CREATE TABLE EMPLEADO (
    documento_unico     BIGINT PRIMARY KEY REFERENCES PERSONA(documento_unico),
    sueldo              DECIMAL(12,2) NOT NULL,
    id_cargo            INT NOT NULL REFERENCES CARGO(id_cargo)
);

CREATE TABLE CLIENTE (
    documento_unico     BIGINT PRIMARY KEY REFERENCES PERSONA(documento_unico),
    tipo_cliente        VARCHAR(50),
    estado_cuenta       VARCHAR(50)
);

-- ============================================================
-- 4. PRODUCTO
-- ============================================================

CREATE TABLE PRODUCTO (
    id_producto     SERIAL PRIMARY KEY,
    nombre          VARCHAR(120) NOT NULL,
    descripcion     TEXT,
    precio          DECIMAL(12,2) NOT NULL,
    stock           INT NOT NULL DEFAULT 0,
    estado          VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    id_categoria    INT NOT NULL REFERENCES CATEGORIA_PRODUCTO(id_categoria)
);

-- ============================================================
-- 5. PEDIDO
-- ============================================================

CREATE TABLE PEDIDO (
    id_pedido           SERIAL PRIMARY KEY,
    fecha               TIMESTAMP NOT NULL DEFAULT NOW(),
    id_cliente          BIGINT NOT NULL REFERENCES CLIENTE(documento_unico),
    id_empleado_registro BIGINT REFERENCES EMPLEADO(documento_unico),
    id_proveedor        INT REFERENCES PROVEEDOR(id_proveedor),
    id_estado           INT NOT NULL REFERENCES ESTADO_PEDIDO(id_estado)
);

-- ============================================================
-- 6. DETALLE_PEDIDO
-- ============================================================

CREATE TABLE DETALLE_PEDIDO (
    id_detalle      SERIAL PRIMARY KEY,
    id_pedido       INT NOT NULL REFERENCES PEDIDO(id_pedido),
    id_producto     INT NOT NULL REFERENCES PRODUCTO(id_producto),
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    total           DECIMAL(14,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED
);

-- ============================================================
-- 7. ENTREGA (una entrega se genera por renglón de detalle)
-- ============================================================

CREATE TABLE ENTREGA (
    id_entrega              SERIAL PRIMARY KEY,
    id_detalle              INT NOT NULL REFERENCES DETALLE_PEDIDO(id_detalle),
    fecha_entrega           TIMESTAMP,
    estado                  VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    observaciones           VARCHAR(255),
    id_empleado_repartidor  BIGINT NOT NULL REFERENCES EMPLEADO(documento_unico)
);

-- ============================================================
-- 8. PAGO
-- ============================================================

CREATE TABLE PAGO (
    id_pago         SERIAL PRIMARY KEY,
    id_entrega      INT NOT NULL REFERENCES ENTREGA(id_entrega),
    metodo_pago     VARCHAR(50) NOT NULL,
    monto           DECIMAL(12,2) NOT NULL,
    fecha_pago      TIMESTAMP NOT NULL DEFAULT NOW(),
    estado_pago     VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE'
);


-- ============================================================
-- 9. USUARIOS (login web SELLPAL)
-- ============================================================
-- La entidad JPA Usuario usa la tabla "usuarios".
-- Con spring.jpa.hibernate.ddl-auto=update, Hibernate la crea al arrancar.
-- No es obligatorio crearla aquí; si quieres crearla a mano:

CREATE TABLE IF NOT EXISTS usuarios (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    rol         VARCHAR(30)  NOT NULL DEFAULT 'CLIENTE',
    telefono    VARCHAR(40),
    direccion   VARCHAR(200),
    ciudad      VARCHAR(80),
    documento   VARCHAR(40)  NOT NULL UNIQUE,
    activo      BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ============================================================
-- FIN DEL SCHEMA
-- ============================================================

