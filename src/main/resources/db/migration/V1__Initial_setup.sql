CREATE TABLE autor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    sexo VARCHAR(10),
    ano_nascimento INT NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL
);

CREATE TABLE livro (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    data_publicacao DATE NOT NULL
);

CREATE TABLE locatario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    sexo VARCHAR(10),
    telefone VARCHAR(15) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL
);

CREATE TABLE aluguel (
    id SERIAL PRIMARY KEY,
    locatario_id BIGINT NOT NULL REFERENCES locatario(id),
    data_retirada DATE NOT NULL,
    data_devolucao DATE
);

CREATE TABLE livro_autor (
    livro_id BIGINT REFERENCES livro(id),
    autor_id BIGINT REFERENCES autor(id),
    PRIMARY KEY (livro_id, autor_id)
);

CREATE TABLE aluguel_livro (
    aluguel_id BIGINT REFERENCES aluguel(id),
    livro_id BIGINT REFERENCES livro(id),
    PRIMARY KEY (aluguel_id, livro_id)
);
