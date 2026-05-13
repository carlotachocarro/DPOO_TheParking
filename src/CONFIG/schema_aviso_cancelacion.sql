-- Ejecutar una vez en la base de datos del parking (MySQL/MariaDB).
CREATE TABLE IF NOT EXISTS aviso_cancelacion_admin (
  id_aviso INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  cod_plaza_cancel VARCHAR(32) NOT NULL,
  planta_cancel INT NOT NULL,
  matricula VARCHAR(32) NOT NULL,
  tipo_vehiculo VARCHAR(64) NOT NULL,
  cod_plaza_nueva VARCHAR(32) DEFAULT NULL,
  planta_nueva INT DEFAULT NULL,
  PRIMARY KEY (id_aviso),
  KEY idx_usuario (id_usuario)
);
