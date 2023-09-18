create table user
(
    id     binary(16) not null,
    active bit not null,
    primary key (id)
) engine = InnoDB;

create table wallet
(
    id            binary(16) not null,
    balance       decimal(19, 2) not null,
    balance_limit decimal(19, 2) not null,
    user_id       binary(16) not null,
    primary key (id)
) engine = InnoDB;

create table wire_transfer_history
(
    id     binary(16) not null,
    amount decimal(19, 2) not null,
    sender binary(16) not null,
    receiver binary(16) not null,
    primary key (id)
) engine = InnoDB;

insert into user (id, active)
values (x'3b52824434f7406bbb7e690912f66b10', true);
insert into user (id, active)
values (x'c5ee925c3dbb4941b825021446f24446', true);
insert into user (id, active)
values (x'625c6fc4145d408f8dd533c16ba26064', false);

insert into wallet(id, balance, balance_limit, user_id)
values (x'4721ee722ff3417fade3acd0a804605b', 150000, 200000, x'3b52824434f7406bbb7e690912f66b10');
insert into wallet(id, balance, balance_limit, user_id)
values (x'0ac16db71b024a87b9c1e7d8f226c48d', 200000, 1000000, x'c5ee925c3dbb4941b825021446f24446');
insert into wallet(id, balance, balance_limit, user_id)
values (x'7de4b8affa0f4391aaa9c61ea9b40f83', 10000, 20000, x'625c6fc4145d408f8dd533c16ba26064');

insert into wire_transfer_history(id, amount, sender, receiver)
values (x'8d71004329b6420e8452233f5a035520', 20000, x'3b52824434f7406bbb7e690912f66b10', x'c5ee925c3dbb4941b825021446f24446');