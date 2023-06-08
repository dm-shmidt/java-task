create table subjects
(
    id        bigint not null,
    name      varchar(128),
    last_name varchar(128),
    primary key (id)
);

create table bank_accounts
(
    id      bigint         not null,
    prefix  varchar(128)   not null,
    suffix  varchar(128)   not null,
    account_number varchar(128) not null,
    balance numeric(18, 2) not null,
    subject bigint,
    apply_for_loan boolean,
    primary key (id),
    foreign key (subject) references subjects (id)
);


create table transactions
(
    id      bigint         not null,
    amount  numeric(18, 2) not null,
    account bigint         not null,
    primary key (id),
    foreign key (account) references bank_accounts (id)
);

create sequence hibernate_sequence start with 500;
create sequence account_sequence start with 1;
create sequence transactions_sequence start with 1;
