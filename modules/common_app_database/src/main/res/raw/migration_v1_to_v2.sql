create table if not exists favorite_schedules(
    _id         integer primary key autoincrement,
    grp_num     text not null,
    description text not null,
    ord         integer not null
);