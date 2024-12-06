CREATE TABLE autor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    sexo ENUM('MASCULINO', 'FEMININO', 'OUTRO'),
    ano_nascimento INT NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE
);

CREATE TABLE livro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL UNIQUE,
    data_publicacao DATE NOT NULL
);

CREATE TABLE locatario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    sexo ENUM('MASCULINO', 'FEMININO', 'OUTRO'),
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE
);

CREATE TABLE aluguel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_retirada DATE NOT NULL,
    data_devolucao DATE,
    locatario_id BIGINT NOT NULL,
    FOREIGN KEY (locatario_id) REFERENCES locatario(id)
);

CREATE TABLE autor_livro (
    autor_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,
    PRIMARY KEY (autor_id, livro_id),
    FOREIGN KEY (autor_id) REFERENCES autor(id),
    FOREIGN KEY (livro_id) REFERENCES livro(id)
);

CREATE TABLE aluguel_livro (
    aluguel_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,
    PRIMARY KEY (aluguel_id, livro_id),
    FOREIGN KEY (aluguel_id) REFERENCES aluguel(id),
    FOREIGN KEY (livro_id) REFERENCES livro(id)
);
