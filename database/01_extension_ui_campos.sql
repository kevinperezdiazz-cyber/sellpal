-- ============================================================
-- SELLPAL - Extensión de columnas para alinear con la interfaz
-- Ejecutar UNA vez sobre sellpal_db (después del schema base)
-- ============================================================

-- Producto: campos que usa el catálogo / admin de la interfaz
ALTER TABLE producto
  ADD COLUMN IF NOT EXISTS codigo         VARCHAR(40),
  ADD COLUMN IF NOT EXISTS marca          VARCHAR(80),
  ADD COLUMN IF NOT EXISTS stock_minimo   INT DEFAULT 0,
  ADD COLUMN IF NOT EXISTS unidad_medida  VARCHAR(40),
  ADD COLUMN IF NOT EXISTS imagen         VARCHAR(255),
  ADD COLUMN IF NOT EXISTS destacado      BOOLEAN DEFAULT FALSE;

-- Categoría: código amigable de la UI
ALTER TABLE categoria_producto
  ADD COLUMN IF NOT EXISTS codigo VARCHAR(40);

-- Cargo: estado visual de la UI
ALTER TABLE cargo
  ADD COLUMN IF NOT EXISTS estado VARCHAR(20) DEFAULT 'Activo';

-- Pedido: total (la entidad JPA ya lo espera; por si no existe aún)
ALTER TABLE pedido
  ADD COLUMN IF NOT EXISTS total NUMERIC(12,2) DEFAULT 0;

-- Índices útiles
CREATE UNIQUE INDEX IF NOT EXISTS ux_producto_codigo ON producto (codigo) WHERE codigo IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS ux_categoria_codigo ON categoria_producto (codigo) WHERE codigo IS NOT NULL;
