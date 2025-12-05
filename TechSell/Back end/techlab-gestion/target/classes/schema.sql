CREATE TABLE IF NOT EXISTS producto (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  precio DOUBLE NOT NULL,
  stock INT NOT NULL
);

CREATE TABLE IF NOT EXISTS pedido (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  estado VARCHAR(32) NOT NULL DEFAULT 'CREADO',
  total DOUBLE NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS linea_pedido (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  producto_id BIGINT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DOUBLE NOT NULL,
  subtotal DOUBLE NOT NULL,
  CONSTRAINT fk_linea_producto FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Relación pedido - lineas se maneja embebida en la entidad; si necesitas FK, añade pedido_id
ALTER TABLE linea_pedido ADD COLUMN IF NOT EXISTS pedido_id BIGINT;
ALTER TABLE linea_pedido ADD CONSTRAINT fk_linea_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id);
