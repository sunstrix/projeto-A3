-- V1: create tables for simple domain

CREATE TABLE IF NOT EXISTS usuarios (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS projetos (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255),
  descricao TEXT
);

CREATE TABLE IF NOT EXISTS equipes (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255)
);
