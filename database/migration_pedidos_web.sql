-- Ejecutar una sola vez en una base existente de SELLPAL.
-- Los pedidos creados desde la tienda no tienen empleado interno al momento de compra.
ALTER TABLE pedido ALTER COLUMN id_empleado_registro DROP NOT NULL;
