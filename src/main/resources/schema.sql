-- Creación de la base de datos
CREATE DATABASE ProyectoRutas;
USE ProyectoRutas;

-- Tabla usuario
CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    correo VARCHAR(255) NOT NULL,
    usuario VARCHAR(255) NOT NULL,
    contrasenia VARCHAR(255) NOT NULL
);

INSERT INTO usuario (correo, usuario, contrasenia)
VALUES ('juan.perez@email.com', 'juanp', '1234seguro'),
       ('maria.garcia@email.com', 'mariag', 'passw0rd'),
       ('carlos.lopez@email.com', 'carlosl', 'securePass123');

-- Tabla puntoInteres
CREATE TABLE puntoInteres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombreOrganismo VARCHAR(255) NOT NULL,
    calle VARCHAR(255) NOT NULL,
    localidad VARCHAR(255) NOT NULL,
    latitud DECIMAL(20, 15) NOT NULL,
    longitud DECIMAL(20, 15) NOT NULL,
    urlImagen VARCHAR(255) NOT NULL,
    tipo VARCHAR(255) NOT NULL
);

-- Tabla favorito
CREATE TABLE favorito (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idUsuario INT NOT NULL,
    idPuntoInteres INT NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    FOREIGN KEY (idPuntoInteres) REFERENCES puntoInteres(id)
);

-- Tabla ruta
CREATE TABLE ruta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    puntosInteres TEXT NOT NULL,
    fechaCreacion DATE NOT NULL,
    duracion INT NOT NULL, -- duración en minutos
    distanciaTotal INT NOT NULL, -- distancia en m
    tipo ENUM('usuario', 'predeterminada') NOT NULL
);

-- Tabla rutaPredeterminada
CREATE TABLE rutaPredeterminada (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idRuta INT NOT NULL,
    provincia VARCHAR(255) NOT NULL,
    municipio VARCHAR(255) NOT NULL,
    FOREIGN KEY (idRuta) REFERENCES ruta(id)
);

-- Tabla rutaUsuario
CREATE TABLE rutaUsuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idRuta INT NOT NULL,
    idUsuario INT NOT NULL,
    FOREIGN KEY (idRuta) REFERENCES ruta(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);

-- Tabla rutaRealizada
CREATE TABLE rutaRealizada (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idRuta INT NOT NULL,
    idUsuario INT NOT NULL,
    fechaRealizada DATE NOT NULL,
    duracion INT NOT NULL,
    valoracion INT,
    numVecesRealizada INT NOT NULL,
    FOREIGN KEY (idRuta) REFERENCES ruta(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);

-- Tabla rutaParaRealizar
CREATE TABLE rutaParaRealizar (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idRuta INT NOT NULL,
    idUsuario INT NOT NULL,
    fechaAniadida DATE NOT NULL,
    horaAniadida TIME NOT NULL,
    FOREIGN KEY (idRuta) REFERENCES ruta(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);

-- Tabla audio
CREATE TABLE audio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idPuntoInteres INT NOT NULL,
    duracion INT NOT NULL, -- segundos
    formato VARCHAR(255), 
    FOREIGN KEY (idPuntoInteres) REFERENCES puntoInteres(id)
);
