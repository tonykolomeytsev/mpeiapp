create table notes(
    _id         integer primary key autoincrement,
    content     text not null,
    datetime    text not null,
    cls_name    text not null,
    grp_name    text not null
);