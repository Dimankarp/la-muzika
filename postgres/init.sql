\connect studs

create type public.musicgenre as enum ('ROCK', 'BLUES', 'MATH_ROCK', 'PUNK_ROCK');
alter type public.musicgenre owner to muzika;


create type public.roles as enum ('USER', 'ADMIN');
alter type public.roles owner to muzika;



create table public.studio
(
    id      bigint generated always as identity
        constraint studio_pk
            primary key,
    name    varchar(255),
    address text
);
alter table public.studio
    owner to muzika;


create table public.album
(
    id     bigint generated always as identity
        constraint album_pk
            primary key,
    name   varchar(255) not null
        constraint non_empty
            check (length((name)::text) > 0),
    tracks integer
        constraint positive_tracks
            check (tracks > 0)
);
alter table public.album
    owner to muzika;


create table public.coordinates
(
    id bigint generated always as identity
        primary key,
    x  double precision not null
        constraint x_bound
            check (x <= (540)::double precision),
    y  double precision not null
        constraint y_bound
            check (y <= (844)::double precision)
);
alter table public.coordinates
    owner to muzika;


create table public."user"
(
    id       bigint generated always as identity
        constraint id
            primary key,
    username varchar(255) not null
        constraint unique_name
            unique
        constraint username_not_empty
            check (length((username)::text) > 0),
    hash     varchar(48)  not null
        constraint hash_full_size
            check (length((hash)::text) = 48)
);

comment on column public."user".hash is 'SHA384 password hash';

alter table public."user"
    owner to muzika;



create table public.role_member
(
    member_id bigint not null
        constraint member_fk
            references public."user",
    role      roles  not null,
    constraint role_member_pk
        primary key (member_id, role)
);

alter table public.role_member
    owner to muzika;



create table public.music_band
(
    id                     bigint generated always as identity primary key,
    name                   varchar(255)                                       not null
        constraint name_non_empty
            check (length((name)::text) > 0),
    coordinates_id         bigint                                             not null
        constraint coords_fk
            references public.coordinates
            on update cascade on delete set null,
    creation_date          timestamp with time zone default CURRENT_TIMESTAMP not null,
    genre                  musicgenre,
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
            on update cascade on delete set null
);
alter table public.music_band
    owner to muzika;


create table public.band_owner
(
    owner_id bigint not null
        constraint user_fk
            references public."user",
    band_id  bigint not null
        constraint band_fk
            references public.music_band,
    constraint band_owner_pk
        primary key (band_id, owner_id)
);

alter table public.band_owner
    owner to muzika;




