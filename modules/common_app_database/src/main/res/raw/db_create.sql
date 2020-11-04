create table notes(
    _id         integer primary key autoincrement,
    content     text not null,
    datetime    text not null,
    cls_name    text not null,
    grp_name    text not null
);
create table favorite_schedules(
    _id         integer primary key autoincrement,
    grp_num     text not null,
    description text not null,
    ord         integer not null
);