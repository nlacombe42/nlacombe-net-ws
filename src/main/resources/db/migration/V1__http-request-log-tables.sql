create table `http_request_logs` (
    http_request_id bigint unsigned not null auto_increment,
    created_at_timestamp timestamp not null,
    method varchar(255) not null,
    uri varchar(255) not null,
    response_status int null,
    primary key (http_request_id)
) char set utf8mb4 collate utf8mb4_unicode_ci;

create table `http_request_log_headers` (
    http_request_header_id bigint unsigned not null auto_increment,
    http_request_id bigint unsigned not null,
    header_name varchar(255) not null,
    header_value varchar(255) not null,
    primary key (http_request_header_id),
    foreign key (http_request_id) references http_request_logs(http_request_id)
) char set utf8mb4 collate utf8mb4_unicode_ci;
