-- ============================================================
-- SELLPAL - Datos de prueba alineados con la interfaz (Mondelez)
-- Ejecutar DESPUÉS de sellpal_postgresql.sql y 01_extension_ui_campos.sql
-- Es seguro re-ejecutar parcialmente: usa ON CONFLICT / NOT EXISTS
-- ============================================================

-- ---------- ESTADOS DE PEDIDO ----------
INSERT INTO estado_pedido (nombre_estado)
SELECT v FROM (VALUES
  ('Pendiente'), ('Aprobado'), ('Procesando'), ('Enviado'),
  ('Despachado'), ('Entregado'), ('Cancelado')
) AS t(v)
WHERE NOT EXISTS (SELECT 1 FROM estado_pedido e WHERE e.nombre_estado = t.v);

-- ---------- CARGOS (como en data.js de la interfaz) ----------
INSERT INTO cargo (nombre_cargo, descripcion, estado)
SELECT * FROM (VALUES
  ('Administrador General', 'Gestión total del sistema, catálogos y accesos', 'Activo'),
  ('Coordinador de Ventas', 'Atención a clientes y validación de pedidos mayoristas', 'Activo'),
  ('Jefe de Bodega y Logística', 'Control de inventario, stock y despachos', 'Activo'),
  ('Repartidor / Conductor', 'Entrega de mercancía en rutas y cobranza contra entrega', 'Activo'),
  ('Cajero / Facturador', 'Recepción y conciliación de pagos electrónicos y efectivo', 'Activo')
) AS v(nombre_cargo, descripcion, estado)
WHERE NOT EXISTS (SELECT 1 FROM cargo c WHERE c.nombre_cargo = v.nombre_cargo);

-- ---------- PROVEEDOR ----------
INSERT INTO proveedor (nombre_empresa, contacto)
SELECT * FROM (VALUES
  ('Mondelez Colombia S.A.S.', '3001112233'),
  ('Distribuidora Andina Snacks', '3109876543')
) AS v(nombre_empresa, contacto)
WHERE NOT EXISTS (SELECT 1 FROM proveedor p WHERE p.nombre_empresa = v.nombre_empresa);

-- ---------- CATEGORÍAS ----------
INSERT INTO categoria_producto (nombre, tipo, codigo)
SELECT * FROM (VALUES
  ('Galletas & Wafers', 'Horneado', 'CAT-GAL'),
  ('Confitería & Caramelos', 'Dulce', 'CAT-CONF'),
  ('Chocolatería', 'Dulce', 'CAT-CHOC'),
  ('Gomas de Mascar & Menta', 'Confitería', 'CAT-GOMS'),
  ('Snacks & Pasabocas', 'Salado', 'CAT-SNK')
) AS v(nombre, tipo, codigo)
WHERE NOT EXISTS (SELECT 1 FROM categoria_producto c WHERE c.nombre = v.nombre);

-- ---------- PERSONAS + EMPLEADOS ----------
INSERT INTO persona (documento_unico, nombre, direccion, telefono, email, fecha_nacimiento)
SELECT * FROM (VALUES
  (1023456789::bigint, 'Carlos Andrés Morales', 'Calle 100 # 19-20, Bogotá', '3104567890', 'carlos.morales@invercajas.com', '1985-04-12'::date),
  (1019876543::bigint, 'Diana Patricia Ramos', 'Cra 7 # 45-10, Bogotá', '3159876543', 'diana.ramos@invercajas.com', '1990-08-22'::date),
  (1034567891::bigint, 'Julián David Peña', 'Av. El Dorado # 68-70, Bogotá', '3201234567', 'julian.pena@invercajas.com', '1992-01-15'::date),
  (1045678912::bigint, 'Mateo Gómez Herrera', 'Calle 26 # 50-15, Bogotá', '3008765432', 'mateo.gomez@invercajas.com', '1995-11-03'::date),
  (1056789123::bigint, 'Valentina Restrepo', 'Cra 15 # 93-40, Bogotá', '3123456789', 'valentina.restrepo@invercajas.com', '1993-06-28'::date)
) AS v(documento_unico, nombre, direccion, telefono, email, fecha_nacimiento)
WHERE NOT EXISTS (SELECT 1 FROM persona p WHERE p.documento_unico = v.documento_unico);

INSERT INTO empleado (documento_unico, sueldo, id_cargo)
SELECT e.documento_unico, e.sueldo, c.id_cargo
FROM (VALUES
  (1023456789::bigint, 4500000.00, 'Administrador General'),
  (1019876543::bigint, 3200000.00, 'Coordinador de Ventas'),
  (1034567891::bigint, 3000000.00, 'Jefe de Bodega y Logística'),
  (1045678912::bigint, 1800000.00, 'Repartidor / Conductor'),
  (1056789123::bigint, 1900000.00, 'Cajero / Facturador')
) AS e(documento_unico, sueldo, cargo_nombre)
JOIN cargo c ON c.nombre_cargo = e.cargo_nombre
WHERE NOT EXISTS (SELECT 1 FROM empleado x WHERE x.documento_unico = e.documento_unico);

-- ---------- PERSONAS + CLIENTES ----------
INSERT INTO persona (documento_unico, nombre, direccion, telefono, email, fecha_nacimiento)
SELECT * FROM (VALUES
  (9005432101::bigint, 'Supertiendas El Ahorro S.A.S.', 'Cra 15 # 45 - 20, Bogotá', '3114567890', 'compras@elahorro.co', NULL::date),
  (9012345672::bigint, 'Minimercado San Jorge', 'Calle 72 # 11 - 84, Bogotá', '3187654321', 'contacto@sanjorge.com', NULL::date),
  (8001234563::bigint, 'Tienda La Economía', 'Cra 50 # 20-15, Medellín', '3001122334', 'laeconomia@mail.com', NULL::date)
) AS v(documento_unico, nombre, direccion, telefono, email, fecha_nacimiento)
WHERE NOT EXISTS (SELECT 1 FROM persona p WHERE p.documento_unico = v.documento_unico);

INSERT INTO cliente (documento_unico, tipo_cliente, estado_cuenta)
SELECT * FROM (VALUES
  (9005432101::bigint, 'Mayorista', 'Activo'),
  (9012345672::bigint, 'Minorista', 'Activo'),
  (8001234563::bigint, 'Minorista', 'Activo')
) AS v(documento_unico, tipo_cliente, estado_cuenta)
WHERE NOT EXISTS (SELECT 1 FROM cliente c WHERE c.documento_unico = v.documento_unico);

-- ---------- PRODUCTOS (catálogo interfaz Mondelez) ----------
INSERT INTO producto (nombre, descripcion, precio, stock, estado, id_categoria, codigo, marca, stock_minimo, unidad_medida, imagen, destacado)
SELECT v.nombre, v.descripcion, v.precio, v.stock, v.estado, c.id_categoria, v.codigo, v.marca, v.stock_minimo, v.unidad_medida, v.imagen, v.destacado
FROM (VALUES
  ('Galletas Oreo Original (Caja x 24 paquetes de 4 galletas)',
   'La clásica galleta sándwich de chocolate con cremoso relleno de vainilla.',
   36000.00, 145, 'ACTIVO', 'Galletas & Wafers', 'PROD-ORE-01', 'Mondelez / Oreo', 20, 'Caja x 24', 'assets/img/prod_oreo.jpg', true),
  ('Galletas Club Social Original (Caja Display x 36 tacos)',
   'Galleta salada crocante e inconfundible. Esencial en todo punto de venta.',
   34500.00, 8, 'ACTIVO', 'Galletas & Wafers', 'PROD-CS-02', 'Mondelez / Club Social', 15, 'Display x 36', 'assets/img/prod_clubsocial.jpg', true),
  ('Chicles Trident Menta Fresca (Caja x 18 paquetes)',
   'Goma de mascar sin azúcar con microcápsulas de sabor menta prolongado.',
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
   'Galletas horneadas con granos enteros, avena y miel natural.',
   31000.00, 6, 'ACTIVO', 'Galletas & Wafers', 'PROD-BEL-08', 'Mondelez / BelVita', 12, 'Caja x 20', 'assets/img/prod_belvita.jpg', false)
) AS v(nombre, descripcion, precio, stock, estado, cat_nombre, codigo, marca, stock_minimo, unidad_medida, imagen, destacado)
JOIN categoria_producto c ON c.nombre = v.cat_nombre
WHERE NOT EXISTS (SELECT 1 FROM producto p WHERE p.codigo = v.codigo);

-- Nota: el usuario admin web (admin@sellpal.co / Admin2026!)
-- lo crea automáticamente Spring Boot al arrancar (SellpalApplication seed).
