-- ============================================================
-- SELLPAL / INVERCAJAS - DATOS DE PRUEBA (PostgreSQL)
-- Ejecutar DESPUÉS de sellpal_postgresql.sql, en este orden.
-- ============================================================
-- Nota: tabla "usuarios" la crea Hibernate (ddl-auto=update).
-- Crear cuentas desde Registro web o POST /api/auth/registro (BCrypt).
-- ============================================================

INSERT INTO CARGO (nombre_cargo, descripcion) VALUES
('Gerente',    'Administra la operación general de la tienda'),
('Vendedor',   'Atiende y registra los pedidos de los clientes'),
('Repartidor', 'Encargado de realizar las entregas a domicilio');

INSERT INTO PROVEEDOR (nombre_empresa, contacto) VALUES
('Dulces Colombia S.A.S',   '3001234567'),
('Galletas Boyacá Ltda',    '3109876543');

INSERT INTO ESTADO_PEDIDO (nombre_estado) VALUES
('Pendiente'),
('Aprobado'),
('Despachado'),
('Entregado');

INSERT INTO CATEGORIA_PRODUCTO (nombre, tipo) VALUES
('Galletas & Wafers',      'Horneado'),
('Confitería & Caramelos', 'Dulce'),
('Chocolatería',           'Dulce'),
('Snacks & Pasabocas',     'Salado');

INSERT INTO PERSONA (documento_unico, nombre, direccion, telefono, email, fecha_nacimiento) VALUES
(1001, 'Juan Pérez',       'Cra 5 #10-20, Tunja',   '3111111111', 'juan.perez@sellpal.com',      '1990-05-10'),
(1002, 'Laura Gómez',      'Calle 20 #8-15, Tunja', '3122222222', 'laura.gomez@sellpal.com',     '1988-03-22'),
(1003, 'Andrés Ruiz',      'Av. Central #3-40, Tunja','3133333333','andres.ruiz@sellpal.com',    '1995-07-14'),
(2001, 'Carlos Rodríguez', 'Calle 33 #12-10, Tunja','3144444444', 'carlos.rodriguez@mail.com',   '1985-11-02'),
(2002, 'María Torres',     'Cra 9 #4-22, Tunja',    '3155555555', 'maria.torres@mail.com',       '1992-01-18'),
(2003, 'Sofía Martínez',   'Calle 15 #7-33, Tunja', '3166666666', 'sofia.martinez@mail.com',     '1998-09-09');

INSERT INTO EMPLEADO (documento_unico, sueldo, id_cargo) VALUES
(1001, 2500000, 1),
(1002, 1800000, 2),
(1003, 1600000, 3);

INSERT INTO CLIENTE (documento_unico, tipo_cliente, estado_cuenta) VALUES
(2001, 'Natural', 'Activa'),
(2002, 'Natural', 'Activa'),
(2003, 'Empresa',  'Activa');

INSERT INTO PRODUCTO (nombre, descripcion, precio, stock, estado, id_categoria) VALUES
('Oreo Original',        'Galleta sándwich de chocolate con crema de vainilla. Caja mayorista.', 36000, 145, 'ACTIVO', 1),
('Galletas Club Social', 'Galleta salada crocante, display mayorista.',                          34500,  80, 'ACTIVO', 1),
('Chicles Trident Menta','Goma de mascar sin azúcar, caja x 18.',                                29900,  82, 'ACTIVO', 2),
('Pastillas Halls Menta','Pastillas refrescantes, display x 12.',                                22000, 210, 'ACTIVO', 2),
('Galletas Chips Ahoy!', 'Galleta con chispas de chocolate, caja mayorista.',                    38500,  60, 'ACTIVO', 1),
('Chocolate Milka',      'Tableta de chocolate con leche, presentación mayorista.',              58000,  45, 'ACTIVO', 3),
('Chocoramo',            'Ponqué cubierto de chocolate.',                                         3500, 100, 'ACTIVO', 3),
('Bombones Jet',         'Bombón de chocolate relleno.',                                          2000, 150, 'ACTIVO', 2),
('Papas Margarita',      'Snack de papa criolla.',                                                3000,  60, 'ACTIVO', 4),
('Mentas Glacial',       'Confite de menta.',                                                     1500, 200, 'ACTIVO', 2);

INSERT INTO PEDIDO (fecha, id_cliente, id_empleado_registro, id_proveedor, id_estado) VALUES
(NOW(), 2001, 1002, NULL, 2),
(NOW(), 2002, 1002, NULL, 1),
(NOW(), 2003, 1001, 1,    3),
(NOW(), 2001, 1002, NULL, 4),
(NOW(), 2002, 1001, 2,    2);

INSERT INTO DETALLE_PEDIDO (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(1, 1, 3,  36000),
(1, 3, 5,  29900),
(2, 2, 2,  34500),
(3, 4, 4,  22000),
(3, 5, 3,  38500),
(4, 6, 2,  58000),
(5, 1, 6,  36000),
(5, 2, 4,  34500);

INSERT INTO ENTREGA (id_detalle, fecha_entrega, estado, observaciones, id_empleado_repartidor) VALUES
(1, NOW() + INTERVAL '1 day', 'Entregado',   'Entrega sin novedad', 1003),
(3, NULL,                     'Pendiente',   NULL,                  1003),
(4, NOW() + INTERVAL '2 day', 'Despachado',  'En tránsito',         1003),
(6, NOW() + INTERVAL '1 day', 'Entregado',   'Entrega sin novedad', 1003),
(7, NULL,                     'Pendiente',   NULL,                  1003);

INSERT INTO PAGO (id_entrega, metodo_pago, monto, fecha_pago, estado_pago) VALUES
(1, 'Efectivo',      108000, NOW(), 'Confirmado'),
(2, 'Transferencia',  69000, NOW(), 'Pendiente'),
(3, 'Tarjeta',        88000, NOW(), 'Confirmado'),
(4, 'Efectivo',      116000, NOW(), 'Confirmado'),
(5, 'Transferencia', 216000, NOW(), 'Pendiente');

-- Para admin: tras registrarte en la web, ejecuta:
-- UPDATE usuarios SET rol = 'ADMIN' WHERE email = 'tu-correo@empresa.com';
