USE sic_stj_db;

DROP TABLE IF EXISTS detalles_solicitud;

CREATE TABLE detalles_solicitud (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud INT NOT NULL,
    id_bien INT NOT NULL,
    cantidad INT NOT NULL,
    precio_estimado DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_detalles_solicitud
        FOREIGN KEY (id_solicitud)
        REFERENCES solicitudes_compra(id_solicitud)
        ON DELETE CASCADE,
    CONSTRAINT fk_detalle_bien
        FOREIGN KEY (id_bien)
        REFERENCES bienes_catalogo(id_bien)
);
