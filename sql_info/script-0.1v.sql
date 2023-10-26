create table category_type
(
    type_id int auto_increment
        constraint `PRIMARY`
        primary key,
    type    varchar(30) not null
);

create table derivative_type
(
    type_id int auto_increment
        constraint `PRIMARY`
        primary key,
    type    varchar(30) not null
);

create table fair_value_level
(
    level varchar(3) not null
        constraint `PRIMARY`
        primary key
);

create table form_type
(
    type_id int auto_increment
        constraint `PRIMARY`
        primary key,
    type    varchar(20) not null
);

create table managed_type
(
    type_id int auto_increment
        constraint `PRIMARY`
        primary key,
    type    varchar(25) not null
);

create table investment_company
(
    cik             int                 not null,
    name            varchar(100)        not null,
    year            year default '0000' not null,
    managed_type_id int  default 2      not null,
    constraint `PRIMARY`
        primary key (cik, year),
    constraint investment_company_managed_type_type_id_fk
        foreign key (managed_type_id) references managed_type (type_id)
            on update cascade on delete cascade
);

create table closed
(
    cik int not null
        constraint `PRIMARY`
        primary key,
    constraint closed_investment_company_cik_fk
        foreign key (cik) references investment_company (cik)
            on update cascade on delete cascade,
    constraint closed_investment_company_cik_fk2
        foreign key (cik) references investment_company (cik)
            on update cascade on delete cascade
);

create table filing
(
    accession_number varchar(20) not null
        constraint `PRIMARY`
        primary key,
    form_type_id     int         not null,
    cik              int         not null,
    constraint filing_form_type_type_id_fk
        foreign key (form_type_id) references form_type (type_id)
            on update cascade on delete cascade,
    constraint filing_investment_company_cik_fk
        foreign key (cik) references investment_company (cik)
            on update cascade on delete cascade
);

create table month_type
(
    type_id int auto_increment
        constraint `PRIMARY`
        primary key,
    type    int not null
);

create table net_gains
(
    net_gains_id        int auto_increment
        constraint `PRIMARY`
        primary key,
    net_realized_gain   double not null,
    net_unrealized_appr double not null
);

create table nport_p
(
    accession_number varchar(20) not null
        constraint `PRIMARY`
        primary key,
    constraint nport_p_filing_accession_number_fk
        foreign key (accession_number) references filing (accession_number)
            on update cascade on delete cascade
);

create table fund_info
(
    accession_number varchar(20) not null
        constraint `PRIMARY`
        primary key,
    total_assets     double      not null,
    net_assets       double      not null,
    constraint fund_info_nport_p_accession_number_fk
        foreign key (accession_number) references nport_p (accession_number)
            on update cascade on delete cascade
);

create table gen_info
(
    accession_number varchar(20)  not null
        constraint `PRIMARY`
        primary key,
    series_name      varchar(100) null,
    series_id        varchar(100) null,
    fiscal_pd_end    date         null,
    reported         date         null,
    is_final_filing  tinyint(1)   null,
    constraint gen_info_nport_p_accession_number_fk
        foreign key (accession_number) references nport_p (accession_number)
            on update cascade on delete cascade
);

create table months_info
(
    accession_number varchar(20) not null,
    month_type_id    int         not null,
    net_gains_id     int         not null,
    redemption       double      not null,
    reinvestment     double      not null,
    sales            double      not null,
    constraint `PRIMARY`
        primary key (accession_number, month_type_id),
    constraint months_info_fund_info_accession_number_fk
        foreign key (accession_number) references fund_info (accession_number)
            on update cascade on delete cascade,
    constraint months_info_month_type_type_id_fk
        foreign key (month_type_id) references month_type (type_id)
            on update cascade,
    constraint months_info_net_gains_net_gains_id_fk
        foreign key (net_gains_id) references net_gains (net_gains_id)
            on update cascade on delete cascade
);

create table category_info
(
    accession_number varchar(20) not null,
    month_type_id    int         not null,
    category_type_id int         not null,
    net_gains_id     int         not null,
    constraint `PRIMARY`
        primary key (accession_number, month_type_id, category_type_id),
    constraint category_info_category_type_type_id_fk
        foreign key (category_type_id) references category_type (type_id)
            on update cascade on delete cascade,
    constraint category_info_months_info_accession_number_month_type_id_fk
        foreign key (accession_number, month_type_id) references months_info (accession_number, month_type_id)
            on update cascade on delete cascade,
    constraint category_info_net_gains_net_gains_id_fk
        foreign key (net_gains_id) references net_gains (net_gains_id)
            on update cascade on delete cascade
);

create table class_info
(
    accession_number varchar(20) not null,
    month_type_id    int         not null,
    class_id         int         not null,
    percent_return   double      not null,
    constraint `PRIMARY`
        primary key (accession_number, month_type_id, class_id),
    constraint class_info_months_info_accession_number_month_type_id_fk
        foreign key (accession_number, month_type_id) references months_info (accession_number, month_type_id)
            on update cascade on delete cascade
);

create table derivative_info
(
    accession_number   varchar(20) not null,
    month_type_id      int         not null,
    category_type_id   int         not null,
    derivative_type_id int         not null,
    net_gains_id       int         not null,
    constraint `PRIMARY`
        primary key (accession_number, month_type_id, category_type_id, derivative_type_id),
    constraint derivative_info_category_info_accession_number_month_type_id__fk
        foreign key (accession_number, month_type_id, category_type_id) references category_info (accession_number, month_type_id, category_type_id)
            on update cascade on delete cascade,
    constraint derivative_info_derivative_type_type_id_fk
        foreign key (derivative_type_id) references derivative_type (type_id)
            on update cascade,
    constraint derivative_info_net_gains_net_gains_id_fk
        foreign key (net_gains_id) references net_gains (net_gains_id)
            on update cascade on delete cascade
);

create table open
(
    cik      int           not null
        constraint `PRIMARY`
        primary key,
    org_type int default 0 not null,
    constraint open_investment_company_cik_fk
        foreign key (cik) references investment_company (cik)
            on update cascade on delete cascade
);

create table trade_type
(
    type varchar(5) not null
        constraint `PRIMARY`
        primary key
);

create table portfolio_info
(
    accession_number    varchar(20) not null
        constraint `PRIMARY`
        primary key,
    cusip               varchar(9)  not null,
    fair_value_level_id varchar(3)  not null,
    issuer_name         varchar(50) not null,
    issue_name          varchar(50) not null,
    value_usd           double      not null,
    percent_of_nav      double      not null,
    trade_type_id       varchar(5)  not null,
    asset_category      int         null,
    issuer_category     int         not null,
    lei                 varchar(20) null,
    constraint portfolio_info_fair_value_level_level_fk
        foreign key (fair_value_level_id) references fair_value_level (level)
            on update cascade,
    constraint portfolio_info_nport_p_accession_number_fk
        foreign key (accession_number) references nport_p (accession_number)
            on update cascade on delete cascade,
    constraint portfolio_info_trade_type_type_fk
        foreign key (trade_type_id) references trade_type (type)
            on update cascade
);

create index portfolio_info_category_type_type_id_fk
    on portfolio_info (asset_category);


