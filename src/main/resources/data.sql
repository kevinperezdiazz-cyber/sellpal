-- ============================================================
-- SELLPAL / INVERCAJAS - DATOS DE PRUEBA
-- Ejecutar DESPUÉS de schema.sql, en este mismo orden.
-- ============================================================

-- CARGO (3)
INSERT INTO CARGO (nombre_cargo, descripcion) VALUES
                                                  ('Gerente',    'Administra la operación general de la tienda'),
                                                  ('Vendedor',   'Atiende y registra los pedidos de los clientes'),
                                                  ('Repartidor', 'Encargado de realizar las entregas a domicilio');

-- PROVEEDOR (2)
INSERT INTO PROVEEDOR (nombre_empresa, contacto) VALUES
                                                     ('Dulces Colombia S.A.S',   '3001234567'),
                                                     ('Galletas Boyacá Ltda',    '3109876543');

-- ESTADO_PEDIDO (4)
INSERT INTO ESTADO_PEDIDO (nombre_estado) VALUES
                                              ('Pendiente'),
                                              ('Aprobado'),
                                              ('Despachado'),
                                              ('Entregado');

-- CATEGORIA_PRODUCTO (4)
INSERT INTO CATEGORIA_PRODUCTO (nombre, tipo) VALUES
                                                  ('Confitería',    'Dulce'),
                                                  ('Galletería',    'Horneado'),
                                                  ('Chocolatería',  'Dulce'),
                                                  ('Snacks',        'Salado');

-- PERSONA (6: 3 futuros empleados + 3 futuros clientes)
INSERT INTO PERSONA (documento_unico, nombre, direccion, telefono, email, fecha_nacimiento) VALUES
                                                                                                (1001, 'Juan Pérez',       'Cra 5 #10-20, Tunja',   '3111111111', 'juan.perez@sellpal.com',      '1990-05-10'),
                                                                                                (1002, 'Laura Gómez',      'Calle 20 #8-15, Tunja', '3122222222', 'laura.gomez@sellpal.com',     '1988-03-22'),
                                                                                                (1003, 'Andrés Ruiz',      'Av. Central #3-40, Tunja','3133333333','andres.ruiz@sellpal.com',    '1995-07-14'),
                                                                                                (2001, 'Carlos Rodríguez', 'Calle 33 #12-10, Tunja','3144444444', 'carlos.rodriguez@mail.com',   '1985-11-02'),
                                                                                                (2002, 'María Torres',     'Cra 9 #4-22, Tunja',    '3155555555', 'maria.torres@mail.com',       '1992-01-18'),
                                                                                                (2003, 'Sofía Martínez',   'Calle 15 #7-33, Tunja', '3166666666', 'sofia.martinez@mail.com',     '1998-09-09');

-- EMPLEADO (3)
INSERT INTO EMPLEADO (documento_unico, sueldo, id_cargo) VALUES
                                                             (1001, 2500000, 1),
                                                             (1002, 1800000, 2),
                                                             (1003, 1600000, 3);

-- CLIENTE (3)
INSERT INTO CLIENTE (documento_unico, tipo_cliente, estado_cuenta) VALUES
                                                                       (2001, 'Natural', 'Activa'),
                                                                       (2002, 'Natural', 'Activa'),
                                                                       (2003, 'Empresa',  'Activa');

-- PRODUCTO (6)
INSERT INTO PRODUCTO (nombre, descripcion, precio, stock, estado, id_categoria) VALUES
                                                                                    ('Chocoramo',      'Ponqué cubierto de chocolate',       3500, 100, 'ACTIVO', 3),
                                                                                    ('Galletas Chokis','Galleta con chispas de chocolate',   5000, 80,  'ACTIVO', 2),
                                                                                    ('Bombones Jet',   'Bombón de chocolate relleno',        2000, 150, 'ACTIVO', 1),
                                                                                    ('Papas Margarita','Snack de papa criolla',              3000, 60,  'ACTIVO', 4),
                                                                                    ('Oreo Original',  'Galleta rellena de crema',           4500, 90,  'ACTIVO', 2),
                                                                                    ('Mentas Glacial', 'Confite de menta',                   1500, 200, 'ACTIVO', 1);

-- PEDIDO (5)
INSERT INTO PEDIDO (fecha, id_cliente, id_empleado_registro, id_proveedor, id_estado) VALUES
                                                                                          (NOW(), 2001, 1002, NULL, 2),
                                                                                          (NOW(), 2002, 1002, NULL, 1),
                                                                                          (NOW(), 2003, 1001, 1,    3),
                                                                                          (NOW(), 2001, 1002, NULL, 4),
                                                                                          (NOW(), 2002, 1001, 2,    2);

-- DETALLE_PEDIDO (8) -- 'total' se calcula solo (columna generada)
INSERT INTO DETALLE_PEDIDO (id_pedido, id_producto, cantidad, precio_unitario) VALUES
                                                                                   (1, 1, 3,  3500),
                                                                                   (1, 3, 5,  2000),
                                                                                   (2, 2, 2,  5000),
                                                                                   (3, 4, 4,  3000),
                                                                                   (3, 5, 3,  4500),
                                                                                   (4, 6, 10, 1500),
                                                                                   (5, 1, 6,  3500),
                                                                                   (5, 2, 4,  5000);

-- ENTREGA (5) -- una entrega por renglón de detalle (no todos los renglones tienen entrega aún)
INSERT INTO ENTREGA (id_detalle, fecha_entrega, estado, observaciones, id_empleado_repartidor) VALUES
                                                                                                   (1, NOW() + INTERVAL '1 day', 'Entregado',   'Entrega sin novedad', 1003),
                                                                                                   (3, NULL,                     'Pendiente',   NULL,                  1003),
                                                                                                   (4, NOW() + INTERVAL '2 day', 'Despachado',  'En tránsito',         1003),
                                                                                                   (6, NOW() + INTERVAL '1 day', 'Entregado',   'Entrega sin novedad', 1003),
                                                                                                   (7, NULL,                     'Pendiente',   NULL,                  1003);

-- PAGO (5) -- monto = total del renglón asociado a cada entrega
INSERT INTO PAGO (id_entrega, metodo_pago, monto, fecha_pago, estado_pago) VALUES
                                                                               (1, 'Efectivo',      10500, NOW(), 'Confirmado'),
                                                                               (2, 'Transferencia', 10000, NOW(), 'Pendiente'),
                                                                               (3, 'Tarjeta',        12000, NOW(), 'Confirmado'),
                                                                               (4, 'Efectivo',       15000, NOW(), 'Confirmado'),
                                                                               (5, 'Transferencia',  21000, NOW(), 'Pendiente');

-- ============================================================
-- TOTAL DE REGISTROS INSERTADOS: 54 (supera el mínimo de 30)
-- ============================================================
