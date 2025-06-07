CREATE DATABASE InventarioDB;
GO

USE InventarioDB;
GO

CREATE TABLE productos (
    id INT PRIMARY KEY IDENTITY(1,1),
    nombre NVARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    cantidad INT NOT NULL
);
