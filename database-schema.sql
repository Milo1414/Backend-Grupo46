CREATE TABLE CLIENTE (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    tipo_doc VARCHAR(50),
    nro_doc INTEGER,
    telefono INTEGER,
    mail VARCHAR(255)
);

CREATE TABLE CONTENEDOR (
    id_contenedor SERIAL PRIMARY KEY,
    peso DECIMAL(10, 2),
    volumen DECIMAL(10, 2),
    id_estado_contenedor INTEGER,
    cliente_asociado VARCHAR(255),
    fecha_creacion TIMESTAMP,
    id_cliente INTEGER NOT NULL,
    CONSTRAINT fk_contenedor_cliente FOREIGN KEY (id_cliente) 
        REFERENCES CLIENTE(id_cliente) ON DELETE CASCADE
);

CREATE TABLE CAMION (
    dominio_camion VARCHAR(20) PRIMARY KEY,
    capacidad_peso DECIMAL(10, 2),
    capacidad_volumen DECIMAL(10, 2),
    disponibilidad BOOLEAN DEFAULT TRUE,
    nombre_transportista VARCHAR(255),
    telefono_transportista INTEGER,
    costo_base_km DECIMAL(10, 2),
    consumo_l_km DECIMAL(10, 2)
);

CREATE TABLE DEPOSITO (
    id_deposito SERIAL PRIMARY KEY,
    latitud_deposito DECIMAL(10, 6),
    longitud_deposito DECIMAL(10, 6),
    calle_deposito VARCHAR(255),
    nro_deposito INTEGER,
    costo_dia_transportista DECIMAL(10, 2)
);

CREATE TABLE TARIFA (
    id_tarifa SERIAL PRIMARY KEY,
    descripcion VARCHAR(500),
    valor INTEGER,
    costo_km_base DECIMAL(10, 2),
    valor_litro_combustible INTEGER,
    consumo_prom_1_km INTEGER
);

CREATE TABLE TARIFA_RANGO (
    id_tarifa_rango SERIAL PRIMARY KEY,
    min_peso_kg DECIMAL(10, 2),
    max_peso_kg DECIMAL(10, 2),
    min_volumen_m3 DECIMAL(10, 2),
    max_volumen_m3 DECIMAL(10, 2),
    factor_camion DECIMAL(10, 2),
    id_tarifa INTEGER NOT NULL,
    CONSTRAINT fk_tarifa_rango_tarifa FOREIGN KEY (id_tarifa) 
        REFERENCES TARIFA(id_tarifa) ON DELETE CASCADE
);

CREATE TABLE ESTADO (
    id_estado SERIAL PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    entidad_tipo VARCHAR(50) NOT NULL
);

CREATE TABLE SOLICITUD (
    id_solicitud SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    contenedor_id BIGINT NOT NULL,
    costo_estimado DECIMAL(10, 2),
    costo_final DECIMAL(10, 2),
    tiempo_estimado_horas DOUBLE PRECISION,
    tiempo_real_horas DOUBLE PRECISION,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_solicitud_cliente FOREIGN KEY (cliente_id) 
        REFERENCES CLIENTE(id_cliente) ON DELETE RESTRICT,
    CONSTRAINT fk_solicitud_contenedor FOREIGN KEY (contenedor_id) 
        REFERENCES CONTENEDOR(id_contenedor) ON DELETE RESTRICT
);

CREATE TABLE RUTA (
    id_ruta SERIAL PRIMARY KEY,
    cantidad_tramos INTEGER,
    cantidad_depositos INTEGER,
    solicitud_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_ruta_solicitud FOREIGN KEY (solicitud_id) 
        REFERENCES SOLICITUD(id_solicitud) ON DELETE CASCADE
);

CREATE TABLE TRAMO (
    id_tramo SERIAL PRIMARY KEY,
    origen VARCHAR(500) NOT NULL,
    destino VARCHAR(500) NOT NULL,
    tipo VARCHAR(100) NOT NULL CHECK (tipo IN ('origen-destino', 'origen-deposito', 'deposito-destino', 'deposito-deposito')),
    costo_aproximado DECIMAL(10, 2),
    costo_real DECIMAL(10, 2),
    distancia_km DOUBLE PRECISION,
    tiempo_estimado_horas DOUBLE PRECISION,
    fecha_hora_inicio_real TIMESTAMP,
    fecha_hora_fin_real TIMESTAMP,
    camion_id BIGINT,
    ruta_id BIGINT NOT NULL,
    CONSTRAINT fk_tramo_ruta FOREIGN KEY (ruta_id) 
        REFERENCES RUTA(id_ruta) ON DELETE CASCADE
);

CREATE TABLE CAMBIO_ESTADO (
    id_cambio_estado SERIAL PRIMARY KEY,
    id_estado BIGINT NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    solicitud_id BIGINT,
    tramo_id BIGINT,
    contenedor_id BIGINT,
    CONSTRAINT fk_cambio_estado_estado FOREIGN KEY (id_estado)
        REFERENCES ESTADO(id_estado) ON DELETE RESTRICT,
    CONSTRAINT fk_cambio_estado_solicitud FOREIGN KEY (solicitud_id) 
        REFERENCES SOLICITUD(id_solicitud) ON DELETE CASCADE,
    CONSTRAINT fk_cambio_estado_tramo FOREIGN KEY (tramo_id) 
        REFERENCES TRAMO(id_tramo) ON DELETE CASCADE,
    CONSTRAINT fk_cambio_estado_contenedor FOREIGN KEY (contenedor_id) 
        REFERENCES CONTENEDOR(id_contenedor) ON DELETE CASCADE
);

CREATE INDEX idx_contenedor_cliente ON CONTENEDOR(id_cliente);
CREATE INDEX idx_contenedor_estado ON CONTENEDOR(id_estado_contenedor);
CREATE INDEX idx_cliente_nro_doc ON CLIENTE(nro_doc);
CREATE INDEX idx_camion_disponibilidad ON CAMION(disponibilidad);
CREATE INDEX idx_tarifa_rango_tarifa ON TARIFA_RANGO(id_tarifa);
CREATE INDEX idx_solicitud_cliente ON SOLICITUD(cliente_id);
CREATE INDEX idx_solicitud_contenedor ON SOLICITUD(contenedor_id);
CREATE INDEX idx_solicitud_fecha ON SOLICITUD(fecha_creacion);
CREATE INDEX idx_ruta_solicitud ON RUTA(solicitud_id);
CREATE INDEX idx_tramo_ruta ON TRAMO(ruta_id);
CREATE INDEX idx_tramo_camion ON TRAMO(camion_id);
CREATE INDEX idx_cambio_estado_solicitud ON CAMBIO_ESTADO(solicitud_id);
CREATE INDEX idx_cambio_estado_tramo ON CAMBIO_ESTADO(tramo_id);
CREATE INDEX idx_cambio_estado_contenedor ON CAMBIO_ESTADO(contenedor_id);
CREATE INDEX idx_cambio_estado_estado ON CAMBIO_ESTADO(id_estado);
CREATE INDEX idx_estado_entidad_tipo ON ESTADO(entidad_tipo);
CREATE INDEX idx_estado_codigo ON ESTADO(codigo);

-- Datos semilla para la tabla ESTADO
-- Estados para SOLICITUD
INSERT INTO ESTADO (descripcion, codigo, entidad_tipo) VALUES
('En Borrador', 'BORRADOR', 'SOLICITUD'),
('Programada', 'PROGRAMADA', 'SOLICITUD'),
('En Tránsito', 'EN_TRANSITO', 'SOLICITUD'),
('Entregada', 'ENTREGADA', 'SOLICITUD'),
('Cancelada', 'CANCELADA', 'SOLICITUD');

-- Estados para TRAMO
INSERT INTO ESTADO (descripcion, codigo, entidad_tipo) VALUES
('Estimado', 'ESTIMADO', 'TRAMO'),
('Asignado', 'ASIGNADO', 'TRAMO'),
('Iniciado', 'INICIADO', 'TRAMO'),
('Finalizado', 'FINALIZADO', 'TRAMO');

-- Estados para CONTENEDOR
INSERT INTO ESTADO (descripcion, codigo, entidad_tipo) VALUES
('Ocupado', 'OCUPADO', 'CONTENEDOR'),
('Vacío', 'VACIO', 'CONTENEDOR'),
('En Mantenimiento', 'EN_MANTENIMIENTO', 'CONTENEDOR'),
('En Tránsito', 'EN_TRANSITO', 'CONTENEDOR');

INSERT INTO CLIENTE (nombre, apellido, tipo_doc, nro_doc, telefono, mail) VALUES
('Juan', 'Pérez', 'DNI', 12345678, 1123456789, 'juan.perez@email.com'),
('María', 'González', 'DNI', 23456789, 1134567890, 'maria.gonzalez@email.com'),
('Carlos', 'Rodríguez', 'DNI', 34567890, 1145678901, 'carlos.rodriguez@email.com');

INSERT INTO CAMION (dominio_camion, capacidad_peso, capacidad_volumen, disponibilidad, nombre_transportista, telefono_transportista, costo_base_km, consumo_l_km) VALUES
('ABC123', 10000.00, 50.00, true, 'Pedro Transportista', 1156789012, 5.50, 0.35),
('DEF456', 15000.00, 75.00, true, 'Luis Conductor', 1167890123, 6.00, 0.40),
('GHI789', 8000.00, 40.00, true, 'Ana Drivers', 1178901234, 5.00, 0.30);

INSERT INTO DEPOSITO (latitud_deposito, longitud_deposito, calle_deposito, nro_deposito, costo_dia_transportista) VALUES
(-34.603722, -58.381592, 'Av. Corrientes', 1234, 500.00),
(-34.615851, -58.433238, 'Av. Rivadavia', 5678, 450.00),
(-34.598567, -58.370356, 'Av. Santa Fe', 9012, 550.00);

INSERT INTO TARIFA (descripcion, valor, costo_km_base, valor_litro_combustible, consumo_prom_1_km) VALUES
('Tarifa Estándar 2025', 100, 8.50, 150, 30);

INSERT INTO TARIFA_RANGO (min_peso_kg, max_peso_kg, min_volumen_m3, max_volumen_m3, factor_camion, id_tarifa) VALUES
(0.00, 1000.00, 0.00, 10.00, 1.00, 1),
(1000.01, 5000.00, 10.01, 30.00, 1.25, 1),
(5000.01, 10000.00, 30.01, 50.00, 1.50, 1),
(10000.01, 20000.00, 50.01, 100.00, 2.00, 1);
