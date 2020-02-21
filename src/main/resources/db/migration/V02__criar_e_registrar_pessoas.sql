create table pessoa (
    id bigint(20) primary key auto_increment,
    nome varchar(30) not null,
    ativo boolean not null,

    logradouro varchar(30),
    numero varchar(30),
    complemento varchar(30),
    bairro varchar(30),
    cep varchar(30),
    cidade varchar(30),
    estado varchar(30)
);

insert  into pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values ('Ricardo', true, 'rua abc', '00', 'qd 5 lt 15', 'Estrela', '75250000', 'Goiania', 'GO');
insert  into pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values ('Rodrigo', true, 'rua abc', '00', 'qd 5 lt 15', 'Estrela', '75250000', 'Goiania', 'GO');
insert  into pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values ('Rubia', true, 'rua abc', '00', 'qd 5 lt 15', 'Estrela', '75250000', 'Goiania', 'GO');
