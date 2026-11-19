-- ============================================================
-- SELLPAL / INVERCAJAS — SCRIPT MAESTRO (TODO EN UNO)
-- ============================================================
-- Qué hace:
--   1. Borra tablas previas (desarrollo)
--   2. Crea el MER completo + columnas de la interfaz
--   3. Inserta datos de prueba alineados con la UI (Mondelez)
--
-- Cómo usarlo:
--   1. CREATE DATABASE sellpal_db;   (si no existe)
--   2. Conéctate a sellpal_db
--   3. Ejecuta ESTE archivo completo de arriba a abajo
--   4. Arranca Spring Boot (SellpalApplication)
--
-- Admin web (lo crea Spring al arrancar):
--   admin@sellpal.co  /  Admin2026!
-- ============================================================

BEGIN;

-- ---------- LIMPIEZA ----------
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS entrega CASCADE;
DROP TABLE IF EXISTS detalle_pedido CASCADE;
DROP TABLE IF EXISTS pedido CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS categoria_producto CASCADE;
DROP TABLE IF EXISTS estado_pedido CASCADE;
DROP TABLE IF EXISTS proveedor CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS empleado CASCADE;
DROP TABLE IF EXISTS persona CASCADE;
DROP TABLE IF EXISTS cargo CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- ---------- CARGO ----------
CREATE TABLE cargo (
    id_cargo        SERIAL PRIMARY KEY,
    nombre_cargo    VARCHAR(100) NOT NULL UNIQUE,
    descripcion     TEXT,
    estado          VARCHAR(20) DEFAULT 'Activo'
);

-- ---------- PROVEEDOR ----------
CREATE TABLE proveedor (
    id_proveedor    SERIAL PRIMARY KEY,
    nombre_empresa  VARCHAR(120) NOT NULL,
    contacto        VARCHAR(120)
);

-- ---------- ESTADO_PEDIDO ----------
CREATE TABLE estado_pedido (
    id_estado       SERIAL PRIMARY KEY,
    nombre_estado   VARCHAR(50) NOT NULL UNIQUE
);

-- ---------- CATEGORIA_PRODUCTO ----------
CREATE TABLE categoria_producto (
    id_categoria    SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    tipo            VARCHAR(100) NOT NULL,
    codigo          VARCHAR(40)
);

-- ---------- PERSONA / EMPLEADO / CLIENTE ----------
CREATE TABLE persona (
    documento_unico     BIGINT PRIMARY KEY,
    nombre              VARCHAR(120) NOT NULL,
    direccion           VARCHAR(200),
    telefono            VARCHAR(30),
    email               VARCHAR(150),
    fecha_nacimiento    DATE
);

CREATE TABLE empleado (
    documento_unico     BIGINT PRIMARY KEY REFERENCES persona(documento_unico),
    sueldo              NUMERIC(12,2) NOT NULL,
    id_cargo            INT NOT NULL REFERENCES cargo(id_cargo)
);

CREATE TABLE cliente (
    documento_unico     BIGINT PRIMARY KEY REFERENCES persona(documento_unico),
    tipo_cliente        VARCHAR(50),
    estado_cuenta       VARCHAR(50)
);

-- ---------- PRODUCTO (MER + campos UI) ----------
CREATE TABLE producto (
    id_producto     SERIAL PRIMARY KEY,
    nombre          VARCHAR(150) NOT NULL,
    descripcion     TEXT,
    precio          NUMERIC(12,2) NOT NULL,
    stock           INT NOT NULL DEFAULT 0,
    estado          VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    id_categoria    INT NOT NULL REFERENCES categoria_producto(id_categoria),
    codigo          VARCHAR(40),
    marca           VARCHAR(80),
    stock_minimo    INT DEFAULT 0,
    unidad_medida   VARCHAR(40),
    imagen          VARCHAR(255),
    destacado       BOOLEAN DEFAULT FALSE
);

CREATE UNIQUE INDEX ux_producto_codigo ON producto (codigo) WHERE codigo IS NOT NULL;

-- ---------- PEDIDO (empleado opcional para pedidos web) ----------
CREATE TABLE pedido (
    id_pedido               SERIAL PRIMARY KEY,
    fecha                   TIMESTAMP NOT NULL DEFAULT NOW(),
    total                   NUMERIC(12,2) NOT NULL DEFAULT 0,
    id_cliente              BIGINT NOT NULL REFERENCES cliente(documento_unico),
    id_empleado_registro    BIGINT REFERENCES empleado(documento_unico),
    id_proveedor            INT REFERENCES proveedor(id_proveedor),
    id_estado               INT NOT NULL REFERENCES estado_pedido(id_estado)
);

-- ---------- DETALLE_PEDIDO ----------
CREATE TABLE detalle_pedido (
    id_detalle      SERIAL PRIMARY KEY,
    id_pedido       INT NOT NULL REFERENCES pedido(id_pedido),
    id_producto     INT NOT NULL REFERENCES producto(id_producto),
    cantidad        INT NOT NULL,
    precio_unitario NUMERIC(12,2) NOT NULL,
    total           NUMERIC(14,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED
);

-- ---------- ENTREGA ----------
CREATE TABLE entrega (
    id_entrega              SERIAL PRIMARY KEY,
    id_detalle              INT NOT NULL REFERENCES detalle_pedido(id_detalle),
    fecha_entrega           TIMESTAMP,
    estado                  VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    observaciones           VARCHAR(255),
    id_empleado_repartidor  BIGINT NOT NULL REFERENCES empleado(documento_unico)
);

-- ---------- PAGO ----------
CREATE TABLE pago (
    id_pago         SERIAL PRIMARY KEY,
    id_entrega      INT NOT NULL REFERENCES entrega(id_entrega),
    metodo_pago     VARCHAR(50) NOT NULL,
    monto           NUMERIC(12,2) NOT NULL,
    fecha_pago      TIMESTAMP NOT NULL DEFAULT NOW(),
    estado_pago     VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE'
);

-- ---------- USUARIOS (login web) ----------
CREATE TABLE usuarios (
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
-- DATOS DE PRUEBA (alineados con la interfaz)
-- ============================================================

INSERT INTO estado_pedido (nombre_estado) VALUES
  ('Pendiente'), ('Aprobado'), ('Procesando'), ('Enviado'),
  ('Despachado'), ('Entregado'), ('Cancelado');

INSERT INTO cargo (nombre_cargo, descripcion, estado) VALUES
  ('Administrador General', 'Gestión total del sistema, catálogos y accesos', 'Activo'),
  ('Coordinador de Ventas', 'Atención a clientes y validación de pedidos mayoristas', 'Activo'),
  ('Jefe de Bodega y Logística', 'Control de inventario, stock y despachos', 'Activo'),
  ('Repartidor / Conductor', 'Entrega de mercancía en rutas y cobranza contra entrega', 'Activo'),
  ('Cajero / Facturador', 'Recepción y conciliación de pagos electrónicos y efectivo', 'Activo');

INSERT INTO proveedor (nombre_empresa, contacto) VALUES
  ('Mondelez Colombia S.A.S.', '3001112233'),
  ('Distribuidora Andina Snacks', '3109876543');

INSERT INTO categoria_producto (nombre, tipo, codigo) VALUES
  ('Galletas & Wafers', 'Horneado', 'CAT-GAL'),
  ('Confitería & Caramelos', 'Dulce', 'CAT-CONF'),
  ('Chocolatería', 'Dulce', 'CAT-CHOC'),
  ('Gomas de Mascar & Menta', 'Confitería', 'CAT-GOMS'),
  ('Snacks & Pasabocas', 'Salado', 'CAT-SNK');

INSERT INTO persona (documento_unico, nombre, direccion, telefono, email, fecha_nacimiento) VALUES
  (1023456789, 'Carlos Andrés Morales', 'Calle 100 # 19-20, Bogotá', '3104567890', 'carlos.morales@invercajas.com', '1985-04-12'),
  (1019876543, 'Diana Patricia Ramos', 'Cra 7 # 45-10, Bogotá', '3159876543', 'diana.ramos@invercajas.com', '1990-08-22'),
  (1034567891, 'Julián David Peña', 'Av. El Dorado # 68-70, Bogotá', '3201234567', 'julian.pena@invercajas.com', '1992-01-15'),
  (1045678912, 'Mateo Gómez Herrera', 'Calle 26 # 50-15, Bogotá', '3008765432', 'mateo.gomez@invercajas.com', '1995-11-03'),
  (1056789123, 'Valentina Restrepo', 'Cra 15 # 93-40, Bogotá', '3123456789', 'valentina.restrepo@invercajas.com', '1993-06-28'),
  (9005432101, 'Supertiendas El Ahorro S.A.S.', 'Cra 15 # 45 - 20, Bogotá', '3114567890', 'compras@elahorro.co', NULL),
  (9012345672, 'Minimercado San Jorge', 'Calle 72 # 11 - 84, Bogotá', '3187654321', 'contacto@sanjorge.com', NULL),
  (8001234563, 'Tienda La Economía', 'Cra 50 # 20-15, Medellín', '3001122334', 'laeconomia@mail.com', NULL);

INSERT INTO empleado (documento_unico, sueldo, id_cargo)
SELECT e.doc, e.sueldo, c.id_cargo
FROM (VALUES
  (1023456789::bigint, 4500000.00, 'Administrador General'),
  (1019876543::bigint, 3200000.00, 'Coordinador de Ventas'),
  (1034567891::bigint, 3000000.00, 'Jefe de Bodega y Logística'),
  (1045678912::bigint, 1800000.00, 'Repartidor / Conductor'),
  (1056789123::bigint, 1900000.00, 'Cajero / Facturador')
) AS e(doc, sueldo, cargo_nombre)
JOIN cargo c ON c.nombre_cargo = e.cargo_nombre;

INSERT INTO cliente (documento_unico, tipo_cliente, estado_cuenta) VALUES
  (9005432101, 'Mayorista', 'Activo'),
  (9012345672, 'Minorista', 'Activo'),
  (8001234563, 'Minorista', 'Activo');

INSERT INTO producto (nombre, descripcion, precio, stock, estado, id_categoria, codigo, marca, stock_minimo, unidad_medida, imagen, destacado)
SELECT v.nombre, v.descripcion, v.precio, v.stock, v.estado, c.id_categoria, v.codigo, v.marca, v.stock_minimo, v.unidad_medida, v.imagen, v.destacado
FROM (VALUES
  ('Galletas Oreo Original (Caja x 24 paquetes de 4 galletas)',
   'La clásica galleta sándwich de chocolate con cremoso relleno de vainilla.',
   36000.00, 145, 'ACTIVO', 'Galletas & Wafers', 'PROD-ORE-01', 'Mondelez / Oreo', 20, 'Caja x 24', 'assets/img/prod_oreo.jpg', true),
  ('Galletas Club Social Original (Caja Display x 36 tacos)',
   'Galleta salada crocante e inconfundible.',
   34500.00, 8, 'ACTIVO', 'Galletas & Wafers', 'PROD-CS-02', 'Mondelez / Club Social', 15, 'Display x 36', 'assets/img/prod_clubsocial.jpg', true),
  ('Chicles Trident Menta Fresca (Caja x 18 paquetes)',
   'Goma de mascar sin azúcar con sabor menta prolongado.',
   29900.00, 82, 'ACTIVO', 'Gomas de Mascar & Menta', 'PROD-TRI-03', 'Mondelez / Trident', 15, 'Caja x 18', 'assets/img/prod_trident.jpg', true),
  ('Pastillas Halls Extra Strong Menta (Caja Display x 12 paquetes)',
   'Pastillas refrescantes de menta intensa.',
   22000.00, 210, 'ACTIVO', 'Confitería & Caramelos', 'PROD-HAL-04', 'Mondelez / Halls', 30, 'Display x 12', 'assets/img/prod_halls.jpg', false),
  ('Galletas Chips Ahoy! Original (Caja x 12 paquetes)',
   'Galletas con chispas de chocolate auténticas.',
   28000.00, 55, 'ACTIVO', 'Galletas & Wafers', 'PROD-CHA-05', 'Mondelez / Chips Ahoy', 12, 'Caja x 12', 'assets/img/prod_chipsahoy.jpg', true),
  ('Caramelos Sparkies Frutas (Bolsa x 50 unidades)',
   'Caramelos masticables con explosión de sabor a frutas.',
   16500.00, 0, 'INACTIVO', 'Confitería & Caramelos', 'PROD-SPA-06', 'Mondelez / Sparkies', 25, 'Bolsa x 50', 'assets/img/prod_sparkies.jpg', false),
  ('Chocolate Milka Alpine Milk Tableta 90g (Display x 12 tabletas)',
   'Auténtico chocolate con leche de los Alpes.',
   58000.00, 40, 'ACTIVO', 'Chocolatería', 'PROD-MIL-07', 'Mondelez / Milka', 10, 'Display x 12', 'assets/img/prod_milka.jpg', true),
  ('Galletas BelVita Desayuno Honey & Oats (Caja x 20 galletas)',
   'Galletas horneadas con granos enteros, avena y miel.',
   31000.00, 6, 'ACTIVO', 'Galletas & Wafers', 'PROD-BEL-08', 'Mondelez / BelVita', 12, 'Caja x 20', 'assets/img/prod_belvita.jpg', false)
) AS v(nombre, descripcion, precio, stock, estado, cat_nombre, codigo, marca, stock_minimo, unidad_medida, imagen, destacado)
JOIN categoria_producto c ON c.nombre = v.cat_nombre;

COMMIT;

-- Verificación rápida
SELECT 'cargos' AS tabla, COUNT(*) AS filas FROM cargo
UNION ALL SELECT 'categorias', COUNT(*) FROM categoria_producto
UNION ALL SELECT 'productos', COUNT(*) FROM producto
UNION ALL SELECT 'empleados', COUNT(*) FROM empleado
UNION ALL SELECT 'clientes', COUNT(*) FROM cliente
UNION ALL SELECT 'estados', COUNT(*) FROM estado_pedido;
