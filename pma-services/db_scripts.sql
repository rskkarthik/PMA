drop table if exists tasks;
drop table if exists projects;
drop table if exists users;

create table users (
	id int not null auto_increment,
	first_name varchar(255) not null,
	last_name varchar(255) not null,
	emp_id int not null unique,
	primary key (id)
);

create table projects (
	id int not null auto_increment,
	name varchar(255) not null,
	start_date date,
	end_date date,
	priority int not null,
	manager_id int not null,
	active boolean not null default 1,
	primary key (id),
	foreign key (manager_id) references users(id)
);

create table tasks (
	id int not null auto_increment,
	name varchar(255) not null,
	project_id int not null,
	parent_task_id int,
	priority int not null,
	start_date date not null,
	end_date date not null,
	user_id int not null,
	active boolean not null default 1,
	primary key (id),
	foreign key (project_id) references projects(id),
	foreign key (parent_task_id) references tasks(id),
	foreign key (user_id) references users(id)
);
