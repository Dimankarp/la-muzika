\connect studs


create table public.account
(
    id       bigint generated always as identity
        constraint id
            primary key,
    username varchar(255) not null
        constraint unique_name
            unique
        constraint username_not_empty
            check (length((username)::text) > 0),
    hash     varchar(96)  not null
        constraint hash_full_size
            check (length((hash)::text) = 96)
);

comment
    on column public.account.hash is 'SHA384 password hash in hex';


alter table public.account
    owner to muzika;



create table public.studio
(
    id       bigint generated always as identity
        constraint studio_pk
            primary key,
    name     varchar(255),
    address  text,
    owner_id bigint references public.account
        on update cascade on delete cascade not null
);
alter table public.studio
    owner to muzika;


create table public.album
(
    id       bigint generated always as identity
        constraint album_pk
            primary key,
    name     varchar(255)                   not null
        constraint non_empty
            check (length((name)::text) > 0),
    tracks   integer
        constraint positive_tracks
            check (tracks > 0),
    owner_id bigint references public.account
        on update cascade on delete cascade not null
);
alter table public.album
    owner to muzika;


create table public.role_member
(
    id        bigint generated always as identity primary key,
    member_id bigint      not null
        constraint member_fk
            references public.account
            on update cascade on delete cascade,
    role      varchar(50) not null,
    unique (member_id, role)
);

alter table public.role_member
    owner to muzika;


create table public.music_band
(
    id                     bigint generated always as identity primary key,
    name                   varchar(255)                                       not null
        constraint name_non_empty
            check (length((name)::text) > 0),
    x                      double precision                                   not null
        constraint x_bound
            check (x <= (540):: double precision
                ),
    y                      double precision                                   not null
        constraint y_bound
            check (y <= (844)::double precision),
    creation_date          timestamp with time zone default CURRENT_TIMESTAMP not null,
    genre                  VARCHAR(50)                                        not null
        constraint non_empty_genre
            check (length((genre)::text) > 0),
    number_of_participants integer
        constraint positive_participants
            check (number_of_participants > 0),
    singles_count          integer
        constraint positive_singles
            check (singles_count > 0),
    description            text,
    best_album_id          bigint
        constraint album_fk
            references public.album
            on update cascade on delete set null,
    albums_count           bigint                                             not null
        constraint positive_albums
            check (albums_count > 0),
    establishment_date     timestamp with time zone                           not null,
    studio_id              bigint
        constraint studio_fk
            references public.studio
            on update cascade on delete set null,
    admin_open             bool                     default false             not null,
    owner_id               bigint references public.account
        on update cascade on delete cascade                                   not null
);
alter table public.music_band
    owner to muzika;


create table public.audit
(
    id            bigint generated always as identity primary key,
    creation_date timestamp with time zone default CURRENT_TIMESTAMP not null,
    creator_id    bigint                                             references public.account
                                                                         on update cascade on delete set null,
    action_type   varchar(255)                                       not null
        constraint action_type_non_empty
            check (length((action_type)::text) > 0),
    target_id     bigint references public.music_band
        on update cascade on delete cascade                          not null
);

alter table public.audit
    owner to muzika;


create table public.admin_requests
(
    id            bigint generated always as identity primary key,
    creation_date timestamp with time zone default CURRENT_TIMESTAMP not null,
    user_id       bigint                                             references public.account
                                                                         on update cascade on delete set null
);

alter table public.audit
    owner to muzika;






