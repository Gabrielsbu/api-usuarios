create table IF NOT EXISTS solidarity_roles (
                        role_id serial primary key,
                        role_label varchar(120) not null,
                        role_label_pt varchar(120),
                        role_authorities text not null
);

