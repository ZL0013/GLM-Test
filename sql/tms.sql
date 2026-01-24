--
-- PostgreSQL database dump
--

\restrict LTDVl7nOi2p7x8aXGZ2xsoeUw0ZKLzFTpBlpLV7bRN7LzM5oaTrW0Xp1sRG7WCQ

-- Dumped from database version 16.10
-- Dumped by pg_dump version 16.10

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: xie_tm; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA xie_tm;


ALTER SCHEMA xie_tm OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: sys_config; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_config (
    config_id integer NOT NULL,
    config_name character varying(100) DEFAULT ''::character varying,
    config_key character varying(100) DEFAULT ''::character varying,
    config_value character varying(500) DEFAULT ''::character varying,
    config_type character(1) DEFAULT 'N'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE xie_tm.sys_config OWNER TO postgres;

--
-- Name: TABLE sys_config; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_config IS '参数配置表';


--
-- Name: COLUMN sys_config.config_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.config_id IS '参数主键';


--
-- Name: COLUMN sys_config.config_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.config_name IS '参数名称';


--
-- Name: COLUMN sys_config.config_key; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.config_key IS '参数键名';


--
-- Name: COLUMN sys_config.config_value; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.config_value IS '参数键值';


--
-- Name: COLUMN sys_config.config_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.config_type IS '系统内置（Y是 N否）';


--
-- Name: COLUMN sys_config.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.create_by IS '创建者';


--
-- Name: COLUMN sys_config.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.create_time IS '创建时间';


--
-- Name: COLUMN sys_config.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.update_by IS '更新者';


--
-- Name: COLUMN sys_config.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.update_time IS '更新时间';


--
-- Name: COLUMN sys_config.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_config.remark IS '备注';


--
-- Name: sys_config_config_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_config_config_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_config_config_id_seq OWNER TO postgres;

--
-- Name: sys_config_config_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_config_config_id_seq OWNED BY xie_tm.sys_config.config_id;


--
-- Name: sys_dept; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_dept (
    dept_id bigint NOT NULL,
    parent_id bigint DEFAULT 0,
    ancestors character varying(50) DEFAULT ''::character varying,
    dept_name character varying(30) DEFAULT ''::character varying,
    order_num integer DEFAULT 0,
    leader character varying(20),
    phone character varying(11),
    email character varying(50),
    status character(1) DEFAULT '0'::bpchar,
    del_flag character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone
);


ALTER TABLE xie_tm.sys_dept OWNER TO postgres;

--
-- Name: TABLE sys_dept; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_dept IS '部门表';


--
-- Name: COLUMN sys_dept.dept_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.dept_id IS '部门id';


--
-- Name: COLUMN sys_dept.parent_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.parent_id IS '父部门id';


--
-- Name: COLUMN sys_dept.ancestors; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.ancestors IS '祖级列表';


--
-- Name: COLUMN sys_dept.dept_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.dept_name IS '部门名称';


--
-- Name: COLUMN sys_dept.order_num; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.order_num IS '显示顺序';


--
-- Name: COLUMN sys_dept.leader; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.leader IS '负责人';


--
-- Name: COLUMN sys_dept.phone; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.phone IS '联系电话';


--
-- Name: COLUMN sys_dept.email; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.email IS '邮箱';


--
-- Name: COLUMN sys_dept.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.status IS '部门状态（0正常 1停用）';


--
-- Name: COLUMN sys_dept.del_flag; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.del_flag IS '删除标志（0代表存在 2代表删除）';


--
-- Name: COLUMN sys_dept.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.create_by IS '创建者';


--
-- Name: COLUMN sys_dept.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.create_time IS '创建时间';


--
-- Name: COLUMN sys_dept.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.update_by IS '更新者';


--
-- Name: COLUMN sys_dept.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dept.update_time IS '更新时间';


--
-- Name: sys_dept_dept_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_dept_dept_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_dept_dept_id_seq OWNER TO postgres;

--
-- Name: sys_dept_dept_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_dept_dept_id_seq OWNED BY xie_tm.sys_dept.dept_id;


--
-- Name: sys_dict_data; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_dict_data (
    dict_code bigint NOT NULL,
    dict_sort integer DEFAULT 0,
    dict_label character varying(100) DEFAULT ''::character varying,
    dict_value character varying(100) DEFAULT ''::character varying,
    dict_type character varying(100) DEFAULT ''::character varying,
    css_class character varying(100) DEFAULT NULL::character varying,
    list_class character varying(100) DEFAULT NULL::character varying,
    is_default character(1) DEFAULT 'N'::bpchar,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE xie_tm.sys_dict_data OWNER TO postgres;

--
-- Name: TABLE sys_dict_data; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_dict_data IS '字典数据表';


--
-- Name: COLUMN sys_dict_data.dict_code; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.dict_code IS '字典编码';


--
-- Name: COLUMN sys_dict_data.dict_sort; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.dict_sort IS '字典排序';


--
-- Name: COLUMN sys_dict_data.dict_label; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.dict_label IS '字典标签';


--
-- Name: COLUMN sys_dict_data.dict_value; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.dict_value IS '字典键值';


--
-- Name: COLUMN sys_dict_data.dict_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.dict_type IS '字典类型';


--
-- Name: COLUMN sys_dict_data.css_class; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.css_class IS '样式属性（其他样式扩展）';


--
-- Name: COLUMN sys_dict_data.list_class; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.list_class IS '表格回显样式';


--
-- Name: COLUMN sys_dict_data.is_default; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.is_default IS '是否默认（Y是 N否）';


--
-- Name: COLUMN sys_dict_data.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.status IS '状态（0正常 1停用）';


--
-- Name: COLUMN sys_dict_data.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.create_by IS '创建者';


--
-- Name: COLUMN sys_dict_data.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.create_time IS '创建时间';


--
-- Name: COLUMN sys_dict_data.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.update_by IS '更新者';


--
-- Name: COLUMN sys_dict_data.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.update_time IS '更新时间';


--
-- Name: COLUMN sys_dict_data.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_data.remark IS '备注';


--
-- Name: sys_dict_data_dict_code_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

ALTER TABLE xie_tm.sys_dict_data ALTER COLUMN dict_code ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME xie_tm.sys_dict_data_dict_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: sys_dict_type; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_dict_type (
    dict_id bigint NOT NULL,
    dict_name character varying(100) DEFAULT ''::character varying,
    dict_type character varying(100) DEFAULT ''::character varying,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE xie_tm.sys_dict_type OWNER TO postgres;

--
-- Name: TABLE sys_dict_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_dict_type IS '字典类型表';


--
-- Name: COLUMN sys_dict_type.dict_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.dict_id IS '字典主键';


--
-- Name: COLUMN sys_dict_type.dict_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.dict_name IS '字典名称';


--
-- Name: COLUMN sys_dict_type.dict_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.dict_type IS '字典类型';


--
-- Name: COLUMN sys_dict_type.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.status IS '状态（0正常 1停用）';


--
-- Name: COLUMN sys_dict_type.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.create_by IS '创建者';


--
-- Name: COLUMN sys_dict_type.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.create_time IS '创建时间';


--
-- Name: COLUMN sys_dict_type.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.update_by IS '更新者';


--
-- Name: COLUMN sys_dict_type.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.update_time IS '更新时间';


--
-- Name: COLUMN sys_dict_type.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_dict_type.remark IS '备注';


--
-- Name: sys_dict_type_dict_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

ALTER TABLE xie_tm.sys_dict_type ALTER COLUMN dict_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME xie_tm.sys_dict_type_dict_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: sys_job; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_job (
    job_id bigint NOT NULL,
    job_name character varying(64) DEFAULT ''::character varying,
    job_group character varying(64) DEFAULT 'DEFAULT'::character varying,
    invoke_target character varying(500) NOT NULL,
    cron_expression character varying(255) DEFAULT ''::character varying,
    misfire_policy character varying(20) DEFAULT '3'::character varying,
    concurrent character(1) DEFAULT '1'::bpchar,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT ''::character varying
);


ALTER TABLE xie_tm.sys_job OWNER TO postgres;

--
-- Name: TABLE sys_job; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_job IS '定时任务调度表';


--
-- Name: COLUMN sys_job.job_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.job_id IS '任务ID';


--
-- Name: COLUMN sys_job.job_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.job_name IS '任务名称';


--
-- Name: COLUMN sys_job.job_group; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.job_group IS '任务组名';


--
-- Name: COLUMN sys_job.invoke_target; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.invoke_target IS '调用目标字符串';


--
-- Name: COLUMN sys_job.cron_expression; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.cron_expression IS 'cron执行表达式';


--
-- Name: COLUMN sys_job.misfire_policy; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.misfire_policy IS '计划执行错误策略';


--
-- Name: COLUMN sys_job.concurrent; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.concurrent IS '是否并发执行';


--
-- Name: COLUMN sys_job.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.status IS '状态（0正常 1暂停）';


--
-- Name: COLUMN sys_job.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.create_by IS '创建者';


--
-- Name: COLUMN sys_job.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.create_time IS '创建时间';


--
-- Name: COLUMN sys_job.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.update_by IS '更新者';


--
-- Name: COLUMN sys_job.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.update_time IS '更新时间';


--
-- Name: COLUMN sys_job.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job.remark IS '备注信息';


--
-- Name: sys_job_job_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_job_job_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_job_job_id_seq OWNER TO postgres;

--
-- Name: sys_job_job_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_job_job_id_seq OWNED BY xie_tm.sys_job.job_id;


--
-- Name: sys_job_log; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_job_log (
    job_log_id bigint NOT NULL,
    job_name character varying(64) NOT NULL,
    job_group character varying(64) NOT NULL,
    invoke_target character varying(500) NOT NULL,
    job_message character varying(500),
    status character(1) DEFAULT '0'::bpchar,
    exception_info character varying(2000) DEFAULT ''::character varying,
    create_time timestamp without time zone
);


ALTER TABLE xie_tm.sys_job_log OWNER TO postgres;

--
-- Name: TABLE sys_job_log; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_job_log IS '定时任务调度日志表';


--
-- Name: COLUMN sys_job_log.job_log_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.job_log_id IS '任务日志ID';


--
-- Name: COLUMN sys_job_log.job_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.job_name IS '任务名称';


--
-- Name: COLUMN sys_job_log.job_group; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.job_group IS '任务组名';


--
-- Name: COLUMN sys_job_log.invoke_target; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.invoke_target IS '调用目标字符串';


--
-- Name: COLUMN sys_job_log.job_message; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.job_message IS '日志信息';


--
-- Name: COLUMN sys_job_log.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.status IS '执行状态（0正常 1失败）';


--
-- Name: COLUMN sys_job_log.exception_info; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.exception_info IS '异常信息';


--
-- Name: COLUMN sys_job_log.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_job_log.create_time IS '创建时间';


--
-- Name: sys_job_log_job_log_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_job_log_job_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_job_log_job_log_id_seq OWNER TO postgres;

--
-- Name: sys_job_log_job_log_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_job_log_job_log_id_seq OWNED BY xie_tm.sys_job_log.job_log_id;


--
-- Name: sys_login_info; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_login_info (
    info_id bigint NOT NULL,
    user_name character varying(50) DEFAULT ''::character varying,
    ipaddr character varying(128) DEFAULT ''::character varying,
    login_location character varying(255) DEFAULT ''::character varying,
    browser character varying(50) DEFAULT ''::character varying,
    os character varying(50) DEFAULT ''::character varying,
    status character(1) DEFAULT '0'::bpchar,
    msg character varying(255) DEFAULT ''::character varying,
    login_time timestamp without time zone
);


ALTER TABLE xie_tm.sys_login_info OWNER TO postgres;

--
-- Name: TABLE sys_login_info; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_login_info IS '系统访问记录';


--
-- Name: COLUMN sys_login_info.info_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.info_id IS '访问ID';


--
-- Name: COLUMN sys_login_info.user_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.user_name IS '用户账号';


--
-- Name: COLUMN sys_login_info.ipaddr; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.ipaddr IS '登录IP地址';


--
-- Name: COLUMN sys_login_info.login_location; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.login_location IS '登录地点';


--
-- Name: COLUMN sys_login_info.browser; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.browser IS '浏览器类型';


--
-- Name: COLUMN sys_login_info.os; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.os IS '操作系统';


--
-- Name: COLUMN sys_login_info.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.status IS '登录状态（0成功 1失败）';


--
-- Name: COLUMN sys_login_info.msg; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.msg IS '提示消息';


--
-- Name: COLUMN sys_login_info.login_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_login_info.login_time IS '访问时间';


--
-- Name: sys_login_info_info_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_login_info_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_login_info_info_id_seq OWNER TO postgres;

--
-- Name: sys_login_info_info_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_login_info_info_id_seq OWNED BY xie_tm.sys_login_info.info_id;


--
-- Name: sys_menu; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_menu (
    menu_id bigint NOT NULL,
    menu_name character varying(50) NOT NULL,
    parent_id bigint DEFAULT 0,
    order_num integer DEFAULT 0,
    path character varying(200) DEFAULT ''::character varying,
    component character varying(255),
    query character varying(255),
    route_name character varying(50) DEFAULT ''::character varying,
    is_frame smallint DEFAULT 1,
    is_cache smallint DEFAULT 0,
    menu_type character(1) DEFAULT ''::bpchar,
    visible character(1) DEFAULT '0'::bpchar,
    status character(1) DEFAULT '0'::bpchar,
    perms character varying(100),
    icon character varying(100) DEFAULT '#'::character varying,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT ''::character varying
);


ALTER TABLE xie_tm.sys_menu OWNER TO postgres;

--
-- Name: TABLE sys_menu; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_menu IS '菜单权限表';


--
-- Name: COLUMN sys_menu.menu_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.menu_id IS '菜单ID';


--
-- Name: COLUMN sys_menu.menu_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.menu_name IS '菜单名称';


--
-- Name: COLUMN sys_menu.parent_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.parent_id IS '父菜单ID';


--
-- Name: COLUMN sys_menu.order_num; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.order_num IS '显示顺序';


--
-- Name: COLUMN sys_menu.path; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.path IS '路由地址';


--
-- Name: COLUMN sys_menu.component; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.component IS '组件路径';


--
-- Name: COLUMN sys_menu.query; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.query IS '路由参数';


--
-- Name: COLUMN sys_menu.route_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.route_name IS '路由名称';


--
-- Name: COLUMN sys_menu.is_frame; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.is_frame IS '是否为外链（0是 1否）';


--
-- Name: COLUMN sys_menu.is_cache; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.is_cache IS '是否缓存（0缓存 1不缓存）';


--
-- Name: COLUMN sys_menu.menu_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.menu_type IS '菜单类型（M目录 C菜单 F按钮）';


--
-- Name: COLUMN sys_menu.visible; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.visible IS '菜单状态（0显示 1隐藏）';


--
-- Name: COLUMN sys_menu.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.status IS '菜单状态（0正常 1停用）';


--
-- Name: COLUMN sys_menu.perms; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.perms IS '权限标识';


--
-- Name: COLUMN sys_menu.icon; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.icon IS '菜单图标';


--
-- Name: COLUMN sys_menu.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.create_by IS '创建者';


--
-- Name: COLUMN sys_menu.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.create_time IS '创建时间';


--
-- Name: COLUMN sys_menu.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.update_by IS '更新者';


--
-- Name: COLUMN sys_menu.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.update_time IS '更新时间';


--
-- Name: COLUMN sys_menu.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_menu.remark IS '备注';


--
-- Name: sys_menu_menu_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_menu_menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_menu_menu_id_seq OWNER TO postgres;

--
-- Name: sys_menu_menu_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_menu_menu_id_seq OWNED BY xie_tm.sys_menu.menu_id;


--
-- Name: sys_notice; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_notice (
    notice_id integer NOT NULL,
    notice_title character varying(50) NOT NULL,
    notice_type character(1) NOT NULL,
    notice_content bytea,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(255) DEFAULT NULL::character varying
);


ALTER TABLE xie_tm.sys_notice OWNER TO postgres;

--
-- Name: TABLE sys_notice; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_notice IS '通知公告表';


--
-- Name: COLUMN sys_notice.notice_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.notice_id IS '公告ID';


--
-- Name: COLUMN sys_notice.notice_title; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.notice_title IS '公告标题';


--
-- Name: COLUMN sys_notice.notice_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.notice_type IS '公告类型（1通知 2公告）';


--
-- Name: COLUMN sys_notice.notice_content; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.notice_content IS '公告内容';


--
-- Name: COLUMN sys_notice.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.status IS '公告状态（0正常 1关闭）';


--
-- Name: COLUMN sys_notice.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.create_by IS '创建者';


--
-- Name: COLUMN sys_notice.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.create_time IS '创建时间';


--
-- Name: COLUMN sys_notice.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.update_by IS '更新者';


--
-- Name: COLUMN sys_notice.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.update_time IS '更新时间';


--
-- Name: COLUMN sys_notice.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_notice.remark IS '备注';


--
-- Name: sys_notice_notice_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_notice_notice_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_notice_notice_id_seq OWNER TO postgres;

--
-- Name: sys_notice_notice_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_notice_notice_id_seq OWNED BY xie_tm.sys_notice.notice_id;


--
-- Name: sys_oper_log; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_oper_log (
    oper_id bigint NOT NULL,
    title character varying(50) DEFAULT ''::character varying,
    business_type integer DEFAULT 0,
    method character varying(200) DEFAULT ''::character varying,
    request_method character varying(10) DEFAULT ''::character varying,
    operator_type integer DEFAULT 0,
    oper_name character varying(50) DEFAULT ''::character varying,
    dept_name character varying(50) DEFAULT ''::character varying,
    oper_url character varying(255) DEFAULT ''::character varying,
    oper_ip character varying(128) DEFAULT ''::character varying,
    oper_location character varying(255) DEFAULT ''::character varying,
    oper_param character varying(2000) DEFAULT ''::character varying,
    json_result character varying(2000) DEFAULT ''::character varying,
    status integer DEFAULT 0,
    error_msg character varying(2000) DEFAULT ''::character varying,
    oper_time timestamp without time zone,
    cost_time bigint DEFAULT 0
);


ALTER TABLE xie_tm.sys_oper_log OWNER TO postgres;

--
-- Name: TABLE sys_oper_log; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_oper_log IS '操作日志记录';


--
-- Name: COLUMN sys_oper_log.oper_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.oper_id IS '日志主键';


--
-- Name: COLUMN sys_oper_log.title; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.title IS '模块标题';


--
-- Name: COLUMN sys_oper_log.business_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.business_type IS '业务类型（0其它 1新增 2修改 3删除）';


--
-- Name: COLUMN sys_oper_log.method; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.method IS '方法名称';


--
-- Name: COLUMN sys_oper_log.request_method; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.request_method IS '请求方式';


--
-- Name: COLUMN sys_oper_log.operator_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.operator_type IS '操作类别（0其它 1后台用户 2手机端用户）';


--
-- Name: COLUMN sys_oper_log.oper_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.oper_name IS '操作人员';


--
-- Name: COLUMN sys_oper_log.dept_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.dept_name IS '部门名称';


--
-- Name: COLUMN sys_oper_log.oper_url; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.oper_url IS '请求URL';


--
-- Name: COLUMN sys_oper_log.oper_ip; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.oper_ip IS '主机地址';


--
-- Name: COLUMN sys_oper_log.oper_location; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.oper_location IS '操作地点';


--
-- Name: COLUMN sys_oper_log.oper_param; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.oper_param IS '请求参数';


--
-- Name: COLUMN sys_oper_log.json_result; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.json_result IS '返回参数';


--
-- Name: COLUMN sys_oper_log.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.status IS '操作状态（0正常 1异常）';


--
-- Name: COLUMN sys_oper_log.error_msg; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.error_msg IS '错误消息';


--
-- Name: COLUMN sys_oper_log.oper_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.oper_time IS '操作时间';


--
-- Name: COLUMN sys_oper_log.cost_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_oper_log.cost_time IS '消耗时间';


--
-- Name: sys_oper_log_oper_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

ALTER TABLE xie_tm.sys_oper_log ALTER COLUMN oper_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME xie_tm.sys_oper_log_oper_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: sys_post; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_post (
    post_id bigint NOT NULL,
    post_code character varying(64) NOT NULL,
    post_name character varying(50) NOT NULL,
    post_sort integer NOT NULL,
    status character(1) NOT NULL,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE xie_tm.sys_post OWNER TO postgres;

--
-- Name: TABLE sys_post; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_post IS '岗位信息表';


--
-- Name: COLUMN sys_post.post_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.post_id IS '岗位ID';


--
-- Name: COLUMN sys_post.post_code; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.post_code IS '岗位编码';


--
-- Name: COLUMN sys_post.post_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.post_name IS '岗位名称';


--
-- Name: COLUMN sys_post.post_sort; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.post_sort IS '显示顺序';


--
-- Name: COLUMN sys_post.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.status IS '状态（0正常 1停用）';


--
-- Name: COLUMN sys_post.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.create_by IS '创建者';


--
-- Name: COLUMN sys_post.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.create_time IS '创建时间';


--
-- Name: COLUMN sys_post.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.update_by IS '更新者';


--
-- Name: COLUMN sys_post.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.update_time IS '更新时间';


--
-- Name: COLUMN sys_post.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_post.remark IS '备注';


--
-- Name: sys_post_post_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_post_post_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_post_post_id_seq OWNER TO postgres;

--
-- Name: sys_post_post_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_post_post_id_seq OWNED BY xie_tm.sys_post.post_id;


--
-- Name: sys_role; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_role (
    role_id bigint NOT NULL,
    role_name character varying(30) NOT NULL,
    role_key character varying(100) NOT NULL,
    role_sort integer NOT NULL,
    data_scope character(1) DEFAULT '1'::bpchar,
    menu_check_strictly boolean DEFAULT true,
    dept_check_strictly boolean DEFAULT true,
    status character(1) NOT NULL,
    del_flag character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE xie_tm.sys_role OWNER TO postgres;

--
-- Name: TABLE sys_role; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_role IS '角色信息表';


--
-- Name: COLUMN sys_role.role_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.role_id IS '角色ID';


--
-- Name: COLUMN sys_role.role_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.role_name IS '角色名称';


--
-- Name: COLUMN sys_role.role_key; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.role_key IS '角色权限字符串';


--
-- Name: COLUMN sys_role.role_sort; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.role_sort IS '显示顺序';


--
-- Name: COLUMN sys_role.data_scope; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.data_scope IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';


--
-- Name: COLUMN sys_role.menu_check_strictly; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.menu_check_strictly IS '菜单树选择项是否关联显示';


--
-- Name: COLUMN sys_role.dept_check_strictly; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.dept_check_strictly IS '部门树选择项是否关联显示';


--
-- Name: COLUMN sys_role.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.status IS '角色状态（0正常 1停用）';


--
-- Name: COLUMN sys_role.del_flag; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.del_flag IS '删除标志（0代表存在 2代表删除）';


--
-- Name: COLUMN sys_role.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.create_by IS '创建者';


--
-- Name: COLUMN sys_role.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.create_time IS '创建时间';


--
-- Name: COLUMN sys_role.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.update_by IS '更新者';


--
-- Name: COLUMN sys_role.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.update_time IS '更新时间';


--
-- Name: COLUMN sys_role.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role.remark IS '备注';


--
-- Name: sys_role_dept; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_role_dept (
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL
);


ALTER TABLE xie_tm.sys_role_dept OWNER TO postgres;

--
-- Name: TABLE sys_role_dept; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_role_dept IS '角色和部门关联表';


--
-- Name: COLUMN sys_role_dept.role_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role_dept.role_id IS '角色ID';


--
-- Name: COLUMN sys_role_dept.dept_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role_dept.dept_id IS '部门ID';


--
-- Name: sys_role_menu; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_role_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE xie_tm.sys_role_menu OWNER TO postgres;

--
-- Name: TABLE sys_role_menu; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_role_menu IS '角色和菜单关联表';


--
-- Name: COLUMN sys_role_menu.role_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role_menu.role_id IS '角色ID';


--
-- Name: COLUMN sys_role_menu.menu_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_role_menu.menu_id IS '菜单ID';


--
-- Name: sys_role_role_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_role_role_id_seq OWNER TO postgres;

--
-- Name: sys_role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_role_role_id_seq OWNED BY xie_tm.sys_role.role_id;


--
-- Name: sys_user; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_user (
    user_id bigint NOT NULL,
    dept_id bigint,
    user_name character varying(30) NOT NULL,
    nick_name character varying(30) NOT NULL,
    user_type character varying(2) DEFAULT '00'::character varying,
    email character varying(50) DEFAULT ''::character varying,
    phonenumber character varying(11) DEFAULT ''::character varying,
    sex character(1) DEFAULT '0'::bpchar,
    avatar character varying(100) DEFAULT ''::character varying,
    password character varying(100) DEFAULT ''::character varying,
    status character(1) DEFAULT '0'::bpchar,
    del_flag character(1) DEFAULT '0'::bpchar,
    login_ip character varying(128) DEFAULT ''::character varying,
    login_date timestamp without time zone,
    pwd_update_date timestamp without time zone,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying,
    password_changed boolean DEFAULT false NOT NULL,
    default_password boolean DEFAULT false NOT NULL,
    last_password_change_time timestamp with time zone
);


ALTER TABLE xie_tm.sys_user OWNER TO postgres;

--
-- Name: TABLE sys_user; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_user IS '用户信息表';


--
-- Name: COLUMN sys_user.user_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.user_id IS '用户ID';


--
-- Name: COLUMN sys_user.dept_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.dept_id IS '部门ID';


--
-- Name: COLUMN sys_user.user_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.user_name IS '用户账号';


--
-- Name: COLUMN sys_user.nick_name; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.nick_name IS '用户昵称';


--
-- Name: COLUMN sys_user.user_type; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.user_type IS '用户类型（00系统用户）';


--
-- Name: COLUMN sys_user.email; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.email IS '用户邮箱';


--
-- Name: COLUMN sys_user.phonenumber; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.phonenumber IS '手机号码';


--
-- Name: COLUMN sys_user.sex; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.sex IS '用户性别（0男 1女 2未知）';


--
-- Name: COLUMN sys_user.avatar; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.avatar IS '头像地址';


--
-- Name: COLUMN sys_user.password; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.password IS '密码';


--
-- Name: COLUMN sys_user.status; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.status IS '账号状态（0正常 1停用）';


--
-- Name: COLUMN sys_user.del_flag; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.del_flag IS '删除标志（0代表存在 2代表删除）';


--
-- Name: COLUMN sys_user.login_ip; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.login_ip IS '最后登录IP';


--
-- Name: COLUMN sys_user.login_date; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.login_date IS '最后登录时间';


--
-- Name: COLUMN sys_user.pwd_update_date; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.pwd_update_date IS '密码最后更新时间';


--
-- Name: COLUMN sys_user.create_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.create_by IS '创建者';


--
-- Name: COLUMN sys_user.create_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.create_time IS '创建时间';


--
-- Name: COLUMN sys_user.update_by; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.update_by IS '更新者';


--
-- Name: COLUMN sys_user.update_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.update_time IS '更新时间';


--
-- Name: COLUMN sys_user.remark; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.remark IS '备注';


--
-- Name: COLUMN sys_user.password_changed; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.password_changed IS '是否修改过默认密码';


--
-- Name: COLUMN sys_user.default_password; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.default_password IS '是否为默认密码';


--
-- Name: COLUMN sys_user.last_password_change_time; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user.last_password_change_time IS '最后修改密码时间';


--
-- Name: sys_user_post; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_user_post (
    user_id bigint NOT NULL,
    post_id bigint NOT NULL
);


ALTER TABLE xie_tm.sys_user_post OWNER TO postgres;

--
-- Name: TABLE sys_user_post; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_user_post IS '用户与岗位关联表';


--
-- Name: COLUMN sys_user_post.user_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user_post.user_id IS '用户ID';


--
-- Name: COLUMN sys_user_post.post_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user_post.post_id IS '岗位ID';


--
-- Name: sys_user_role; Type: TABLE; Schema: xie_tm; Owner: postgres
--

CREATE TABLE xie_tm.sys_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE xie_tm.sys_user_role OWNER TO postgres;

--
-- Name: TABLE sys_user_role; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON TABLE xie_tm.sys_user_role IS '用户和角色关联表';


--
-- Name: COLUMN sys_user_role.user_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user_role.user_id IS '用户ID';


--
-- Name: COLUMN sys_user_role.role_id; Type: COMMENT; Schema: xie_tm; Owner: postgres
--

COMMENT ON COLUMN xie_tm.sys_user_role.role_id IS '角色ID';


--
-- Name: sys_user_user_id_seq; Type: SEQUENCE; Schema: xie_tm; Owner: postgres
--

CREATE SEQUENCE xie_tm.sys_user_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE xie_tm.sys_user_user_id_seq OWNER TO postgres;

--
-- Name: sys_user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: xie_tm; Owner: postgres
--

ALTER SEQUENCE xie_tm.sys_user_user_id_seq OWNED BY xie_tm.sys_user.user_id;


--
-- Name: sys_config config_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_config ALTER COLUMN config_id SET DEFAULT nextval('xie_tm.sys_config_config_id_seq'::regclass);


--
-- Name: sys_dept dept_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_dept ALTER COLUMN dept_id SET DEFAULT nextval('xie_tm.sys_dept_dept_id_seq'::regclass);


--
-- Name: sys_job job_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_job ALTER COLUMN job_id SET DEFAULT nextval('xie_tm.sys_job_job_id_seq'::regclass);


--
-- Name: sys_job_log job_log_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_job_log ALTER COLUMN job_log_id SET DEFAULT nextval('xie_tm.sys_job_log_job_log_id_seq'::regclass);


--
-- Name: sys_login_info info_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_login_info ALTER COLUMN info_id SET DEFAULT nextval('xie_tm.sys_login_info_info_id_seq'::regclass);


--
-- Name: sys_menu menu_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_menu ALTER COLUMN menu_id SET DEFAULT nextval('xie_tm.sys_menu_menu_id_seq'::regclass);


--
-- Name: sys_notice notice_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_notice ALTER COLUMN notice_id SET DEFAULT nextval('xie_tm.sys_notice_notice_id_seq'::regclass);


--
-- Name: sys_post post_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_post ALTER COLUMN post_id SET DEFAULT nextval('xie_tm.sys_post_post_id_seq'::regclass);


--
-- Name: sys_role role_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_role ALTER COLUMN role_id SET DEFAULT nextval('xie_tm.sys_role_role_id_seq'::regclass);


--
-- Name: sys_user user_id; Type: DEFAULT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_user ALTER COLUMN user_id SET DEFAULT nextval('xie_tm.sys_user_user_id_seq'::regclass);


--
-- Data for Name: sys_config; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_config (config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	主框架页-默认皮肤样式名称	sys.index.skinName	skin-blue	Y	admin	2025-11-30 02:41:30.327423		\N	蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow
2	用户管理-账号初始密码	sys.user.initPassword	123456	Y	admin	2025-11-30 02:41:30.327423		\N	初始化密码 123456
3	主框架页-侧边栏主题	sys.index.sideTheme	theme-dark	Y	admin	2025-11-30 02:41:30.327423		\N	深色主题theme-dark，浅色主题theme-light
4	账号自助-验证码开关	sys.account.captchaEnabled	true	Y	admin	2025-11-30 02:41:30.327423		\N	是否开启验证码功能（true开启，false关闭）
5	账号自助-是否开启用户注册功能	sys.account.registerUser	false	Y	admin	2025-11-30 02:41:30.327423		\N	是否开启注册用户功能（true开启，false关闭）
6	用户登录-黑名单列表	sys.login.blackIPList		Y	admin	2025-11-30 02:41:30.327423		\N	设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）
7	用户管理-初始密码修改策略	sys.account.initPasswordModify	1	Y	admin	2025-11-30 02:41:30.327423		\N	0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框
8	用户管理-账号密码更新周期	sys.account.passwordValidateDays	0	Y	admin	2025-11-30 02:41:30.327423		\N	密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框
\.


--
-- Data for Name: sys_dept; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time, update_by, update_time) FROM stdin;
100	0	0	若依科技	0	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.414027		\N
101	100	0,100	深圳总公司	1	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.440391		\N
102	100	0,100	长沙分公司	2	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.468523		\N
103	101	0,100,101	研发部门	1	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.500939		\N
104	101	0,100,101	市场部门	2	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.518412		\N
105	101	0,100,101	测试部门	3	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.537626		\N
106	101	0,100,101	财务部门	4	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.555823		\N
107	101	0,100,101	运维部门	5	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.566946		\N
108	102	0,100,102	市场部门	1	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.580958		\N
109	102	0,100,102	财务部门	2	若依	15888888888	ry@qq.com	0	0	admin	2025-11-29 17:35:30.594531		\N
\.


--
-- Data for Name: sys_dict_data; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	1	男	0	sys_user_sex			Y	0	admin	2025-11-30 02:40:47.72196		\N	性别男
2	2	女	1	sys_user_sex			N	0	admin	2025-11-30 02:40:47.744321		\N	性别女
3	3	未知	2	sys_user_sex			N	0	admin	2025-11-30 02:40:47.758168		\N	性别未知
4	1	显示	0	sys_show_hide		primary	Y	0	admin	2025-11-30 02:40:47.772452		\N	显示菜单
5	2	隐藏	1	sys_show_hide		danger	N	0	admin	2025-11-30 02:40:47.786697		\N	隐藏菜单
6	6	正常	0	sys_normal_disable		primary	Y	0	admin	2025-11-30 02:40:47.803487		\N	正常状态
7	7	停用	1	sys_normal_disable		danger	N	0	admin	2025-11-30 02:40:47.82209		\N	停用状态
8	8	正常	0	sys_job_status		primary	Y	0	admin	2025-11-30 02:40:47.835731		\N	正常状态
9	9	暂停	1	sys_job_status		danger	N	0	admin	2025-11-30 02:40:47.850086		\N	停用状态
10	10	默认	DEFAULT	sys_job_group			Y	0	admin	2025-11-30 02:40:47.862568		\N	默认分组
11	11	系统	SYSTEM	sys_job_group			N	0	admin	2025-11-30 02:40:47.876692		\N	系统分组
12	12	是	Y	sys_yes_no		primary	Y	0	admin	2025-11-30 02:40:47.891062		\N	系统默认是
13	13	否	N	sys_yes_no		danger	N	0	admin	2025-11-30 02:40:47.9066		\N	系统默认否
14	14	通知	1	sys_notice_type		warning	Y	0	admin	2025-11-30 02:40:47.917706		\N	通知
15	15	公告	2	sys_notice_type		success	N	0	admin	2025-11-30 02:40:47.926019		\N	公告
16	16	正常	0	sys_notice_status		primary	Y	0	admin	2025-11-30 02:40:47.935391		\N	正常状态
17	17	关闭	1	sys_notice_status		danger	N	0	admin	2025-11-30 02:40:47.946772		\N	关闭状态
18	18	其他	0	sys_oper_type		info	N	0	admin	2025-11-30 02:40:47.95705		\N	其他操作
19	19	新增	1	sys_oper_type		info	N	0	admin	2025-11-30 02:40:47.964437		\N	新增操作
20	20	修改	2	sys_oper_type		info	N	0	admin	2025-11-30 02:40:47.975987		\N	修改操作
21	21	删除	3	sys_oper_type		danger	N	0	admin	2025-11-30 02:40:47.986343		\N	删除操作
22	22	授权	4	sys_oper_type		primary	N	0	admin	2025-11-30 02:40:48.003842		\N	授权操作
23	23	导出	5	sys_oper_type		warning	N	0	admin	2025-11-30 02:40:48.019573		\N	导出操作
24	24	导入	6	sys_oper_type		warning	N	0	admin	2025-11-30 02:40:48.031389		\N	导入操作
25	25	强退	7	sys_oper_type		danger	N	0	admin	2025-11-30 02:40:48.040354		\N	强退操作
26	26	生成代码	8	sys_oper_type		warning	N	0	admin	2025-11-30 02:40:48.051229		\N	生成操作
27	27	清空数据	9	sys_oper_type		danger	N	0	admin	2025-11-30 02:40:48.060279		\N	清空操作
28	28	成功	0	sys_common_status		primary	N	0	admin	2025-11-30 02:40:48.068901		\N	正常状态
29	29	失败	1	sys_common_status		danger	N	0	admin	2025-11-30 02:40:48.077651		\N	停用状态
\.


--
-- Data for Name: sys_dict_type; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	用户性别	sys_user_sex	0	admin	2025-11-30 02:37:33.488976		\N	用户性别列表
2	菜单状态	sys_show_hide	0	admin	2025-11-30 02:37:33.507451		\N	菜单状态列表
3	系统开关	sys_normal_disable	0	admin	2025-11-30 02:37:33.52645		\N	系统开关列表
4	任务状态	sys_job_status	0	admin	2025-11-30 02:37:33.541914		\N	任务状态列表
5	任务分组	sys_job_group	0	admin	2025-11-30 02:37:33.555276		\N	任务分组列表
6	系统是否	sys_yes_no	0	admin	2025-11-30 02:37:33.565666		\N	系统是否列表
7	通知类型	sys_notice_type	0	admin	2025-11-30 02:37:33.577777		\N	通知类型列表
8	通知状态	sys_notice_status	0	admin	2025-11-30 02:37:33.587292		\N	通知状态列表
9	操作类型	sys_oper_type	0	admin	2025-11-30 02:37:33.596015		\N	操作类型列表
10	系统状态	sys_common_status	0	admin	2025-11-30 02:37:33.605104		\N	登录状态列表
\.


--
-- Data for Name: sys_job; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	系统默认（无参）	DEFAULT	ryTask.ryNoParams	0/10 * * * * ?	3	1	1	admin	2025-11-30 02:48:42.849172		\N	
2	系统默认（有参）	DEFAULT	ryTask.ryParams('ry')	0/15 * * * * ?	3	1	1	admin	2025-11-30 02:48:42.849172		\N	
3	系统默认（多参）	DEFAULT	ryTask.ryMultipleParams('ry', true, 2000L, 316.50D, 100)	0/20 * * * * ?	3	1	1	admin	2025-11-30 02:48:42.849172		\N	
\.


--
-- Data for Name: sys_job_log; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_job_log (job_log_id, job_name, job_group, invoke_target, job_message, status, exception_info, create_time) FROM stdin;
\.


--
-- Data for Name: sys_login_info; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_login_info (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) FROM stdin;
\.


--
-- Data for Name: sys_menu; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	系统管理	0	1	system	\N			1	0	M	0	0		system	admin	2025-11-29 17:51:14.629606		\N	系统管理目录
2	系统监控	0	2	monitor	\N			1	0	M	0	0		monitor	admin	2025-11-29 17:51:14.646296		\N	系统监控目录
3	系统工具	0	3	tool	\N			1	0	M	0	0		tool	admin	2025-11-29 17:51:14.658233		\N	系统工具目录
4	若依官网	0	4	http://ruoyi.vip	\N			0	0	M	0	0		guide	admin	2025-11-29 17:51:14.674816		\N	若依官网地址
100	用户管理	1	1	user	system/user/index			1	0	C	0	0	system:user:list	user	admin	2025-11-29 17:52:20.944919		\N	用户管理菜单
101	角色管理	1	2	role	system/role/index			1	0	C	0	0	system:role:list	peoples	admin	2025-11-29 17:52:20.965504		\N	角色管理菜单
102	菜单管理	1	3	menu	system/menu/index			1	0	C	0	0	system:menu:list	tree-table	admin	2025-11-29 17:52:20.988352		\N	菜单管理菜单
103	部门管理	1	4	dept	system/dept/index			1	0	C	0	0	system:dept:list	tree	admin	2025-11-29 17:52:21.023114		\N	部门管理菜单
104	岗位管理	1	5	post	system/post/index			1	0	C	0	0	system:post:list	post	admin	2025-11-29 17:52:21.036445		\N	岗位管理菜单
105	字典管理	1	6	dict	system/dict/index			1	0	C	0	0	system:dict:list	dict	admin	2025-11-29 17:52:21.049389		\N	字典管理菜单
106	参数设置	1	7	config	system/config/index			1	0	C	0	0	system:config:list	edit	admin	2025-11-29 17:52:21.060754		\N	参数设置菜单
107	通知公告	1	8	notice	system/notice/index			1	0	C	0	0	system:notice:list	message	admin	2025-11-29 17:52:21.071664		\N	通知公告菜单
108	日志管理	1	9	log				1	0	M	0	0		log	admin	2025-11-29 17:52:21.08478		\N	日志管理菜单
109	在线用户	2	1	online	monitor/online/index			1	0	C	0	0	monitor:online:list	online	admin	2025-11-29 17:52:21.09455		\N	在线用户菜单
110	定时任务	2	2	job	monitor/job/index			1	0	C	0	0	monitor:job:list	job	admin	2025-11-29 17:52:21.104993		\N	定时任务菜单
111	数据监控	2	3	druid	monitor/druid/index			1	0	C	0	0	monitor:druid:list	druid	admin	2025-11-29 17:52:21.117038		\N	数据监控菜单
112	服务监控	2	4	server	monitor/server/index			1	0	C	0	0	monitor:server:list	server	admin	2025-11-29 17:52:21.129978		\N	服务监控菜单
113	缓存监控	2	5	cache	monitor/cache/index			1	0	C	0	0	monitor:cache:list	redis	admin	2025-11-29 17:52:21.142228		\N	缓存监控菜单
114	缓存列表	2	6	cacheList	monitor/cache/list			1	0	C	0	0	monitor:cache:list	redis-list	admin	2025-11-29 17:52:21.153003		\N	缓存列表菜单
115	表单构建	3	1	build	tool/build/index			1	0	C	0	0	tool:build:list	build	admin	2025-11-29 17:52:21.161534		\N	表单构建菜单
116	代码生成	3	2	gen	tool/gen/index			1	0	C	0	0	tool:gen:list	code	admin	2025-11-29 17:52:21.17031		\N	代码生成菜单
117	系统接口	3	3	swagger	tool/swagger/index			1	0	C	0	0	tool:swagger:list	swagger	admin	2025-11-29 17:52:21.177978		\N	系统接口菜单
500	操作日志	108	1	operlog	monitor/operlog/index			1	0	C	0	0	monitor:operlog:list	form	admin	2025-11-29 17:52:55.679928		\N	操作日志菜单
501	登录日志	108	2	logininfor	monitor/logininfor/index			1	0	C	0	0	monitor:logininfor:list	logininfor	admin	2025-11-29 17:52:55.700821		\N	登录日志菜单
1000	用户查询	100	1					1	0	F	0	0	system:user:query	#	admin	2025-11-29 17:53:41.177025		\N	
1001	用户新增	100	2					1	0	F	0	0	system:user:add	#	admin	2025-11-29 17:53:41.202574		\N	
1002	用户修改	100	3					1	0	F	0	0	system:user:edit	#	admin	2025-11-29 17:53:41.221531		\N	
1003	用户删除	100	4					1	0	F	0	0	system:user:remove	#	admin	2025-11-29 17:53:41.23663		\N	
1004	用户导出	100	5					1	0	F	0	0	system:user:export	#	admin	2025-11-29 17:53:41.249994		\N	
1005	用户导入	100	6					1	0	F	0	0	system:user:import	#	admin	2025-11-29 17:53:41.262591		\N	
1006	重置密码	100	7					1	0	F	0	0	system:user:resetPwd	#	admin	2025-11-29 17:53:41.2761		\N	
1007	角色查询	101	1					1	0	F	0	0	system:role:query	#	admin	2025-11-29 17:56:26.865095		\N	
1008	角色新增	101	2					1	0	F	0	0	system:role:add	#	admin	2025-11-29 17:56:26.890619		\N	
1009	角色修改	101	3					1	0	F	0	0	system:role:edit	#	admin	2025-11-29 17:56:26.904974		\N	
1010	角色删除	101	4					1	0	F	0	0	system:role:remove	#	admin	2025-11-29 17:56:26.918894		\N	
1011	角色导出	101	5					1	0	F	0	0	system:role:export	#	admin	2025-11-29 17:56:26.935386		\N	
1012	菜单查询	102	1					1	0	F	0	0	system:menu:query	#	admin	2025-11-29 17:57:44.732743		\N	
1013	菜单新增	102	2					1	0	F	0	0	system:menu:add	#	admin	2025-11-29 17:57:44.757641		\N	
1014	菜单修改	102	3					1	0	F	0	0	system:menu:edit	#	admin	2025-11-29 17:57:44.768532		\N	
1015	菜单删除	102	4					1	0	F	0	0	system:menu:remove	#	admin	2025-11-29 17:57:44.775732		\N	
1016	部门查询	103	1					1	0	F	0	0	system:dept:query	#	admin	2025-11-29 18:00:08.603336		\N	
1017	部门新增	103	2					1	0	F	0	0	system:dept:add	#	admin	2025-11-29 18:00:08.626322		\N	
1018	部门修改	103	3					1	0	F	0	0	system:dept:edit	#	admin	2025-11-29 18:00:08.647168		\N	
1019	部门删除	103	4					1	0	F	0	0	system:dept:remove	#	admin	2025-11-29 18:00:08.660257		\N	
1020	岗位查询	104	1					1	0	F	0	0	system:post:query	#	admin	2025-11-29 18:01:03.415973		\N	
1021	岗位新增	104	2					1	0	F	0	0	system:post:add	#	admin	2025-11-29 18:01:03.432647		\N	
1022	岗位修改	104	3					1	0	F	0	0	system:post:edit	#	admin	2025-11-29 18:01:03.450889		\N	
1023	岗位删除	104	4					1	0	F	0	0	system:post:remove	#	admin	2025-11-29 18:01:03.462599		\N	
1024	岗位导出	104	5					1	0	F	0	0	system:post:export	#	admin	2025-11-29 18:01:03.475577		\N	
1025	字典查询	105	1	#				1	0	F	0	0	system:dict:query	#	admin	2025-11-29 18:01:03.487278		\N	
1026	字典新增	105	2	#				1	0	F	0	0	system:dict:add	#	admin	2025-11-29 18:01:03.500401		\N	
1027	字典修改	105	3	#				1	0	F	0	0	system:dict:edit	#	admin	2025-11-29 18:01:03.51299		\N	
1028	字典删除	105	4	#				1	0	F	0	0	system:dict:remove	#	admin	2025-11-29 18:01:03.527613		\N	
1029	字典导出	105	5	#				1	0	F	0	0	system:dict:export	#	admin	2025-11-29 18:01:03.543075		\N	
1030	参数查询	106	1	#				1	0	F	0	0	system:config:query	#	admin	2025-11-29 18:02:40.960616		\N	
1031	参数新增	106	2	#				1	0	F	0	0	system:config:add	#	admin	2025-11-29 18:02:40.985701		\N	
1032	参数修改	106	3	#				1	0	F	0	0	system:config:edit	#	admin	2025-11-29 18:02:41.000566		\N	
1033	参数删除	106	4	#				1	0	F	0	0	system:config:remove	#	admin	2025-11-29 18:02:41.019727		\N	
1034	参数导出	106	5	#				1	0	F	0	0	system:config:export	#	admin	2025-11-29 18:02:41.029311		\N	
1035	公告查询	107	1	#				1	0	F	0	0	system:notice:query	#	admin	2025-11-29 18:02:41.041563		\N	
1036	公告新增	107	2	#				1	0	F	0	0	system:notice:add	#	admin	2025-11-29 18:02:41.056505		\N	
1037	公告修改	107	3	#				1	0	F	0	0	system:notice:edit	#	admin	2025-11-29 18:02:41.069024		\N	
1038	公告删除	107	4	#				1	0	F	0	0	system:notice:remove	#	admin	2025-11-29 18:02:41.077431		\N	
1039	操作查询	500	1	#				1	0	F	0	0	monitor:operlog:query	#	admin	2025-11-29 18:02:41.092533		\N	
1040	操作删除	500	2	#				1	0	F	0	0	monitor:operlog:remove	#	admin	2025-11-29 18:02:41.104861		\N	
1041	日志导出	500	3	#				1	0	F	0	0	monitor:operlog:export	#	admin	2025-11-29 18:02:41.118095		\N	
1042	登录查询	501	1	#				1	0	F	0	0	monitor:logininfor:query	#	admin	2025-11-29 18:04:03.579386		\N	
1043	登录删除	501	2	#				1	0	F	0	0	monitor:logininfor:remove	#	admin	2025-11-29 18:04:03.610249		\N	
1044	日志导出	501	3	#				1	0	F	0	0	monitor:logininfor:export	#	admin	2025-11-29 18:04:03.628096		\N	
1045	账户解锁	501	4	#				1	0	F	0	0	monitor:logininfor:unlock	#	admin	2025-11-29 18:04:03.644631		\N	
1046	在线查询	109	1	#				1	0	F	0	0	monitor:online:query	#	admin	2025-11-29 18:04:03.658101		\N	
1047	批量强退	109	2	#				1	0	F	0	0	monitor:online:batchLogout	#	admin	2025-11-29 18:04:03.67259		\N	
1048	单条强退	109	3	#				1	0	F	0	0	monitor:online:forceLogout	#	admin	2025-11-29 18:04:03.686496		\N	
1049	任务查询	110	1	#				1	0	F	0	0	monitor:job:query	#	admin	2025-11-29 18:04:03.698936		\N	
1050	任务新增	110	2	#				1	0	F	0	0	monitor:job:add	#	admin	2025-11-29 18:04:03.712568		\N	
1051	任务修改	110	3	#				1	0	F	0	0	monitor:job:edit	#	admin	2025-11-29 18:04:03.72582		\N	
1052	任务删除	110	4	#				1	0	F	0	0	monitor:job:remove	#	admin	2025-11-29 18:04:03.741533		\N	
1053	状态修改	110	5	#				1	0	F	0	0	monitor:job:changeStatus	#	admin	2025-11-29 18:04:57.909553		\N	
1054	任务导出	110	6	#				1	0	F	0	0	monitor:job:export	#	admin	2025-11-29 18:04:57.927902		\N	
1055	生成查询	116	1	#				1	0	F	0	0	tool:gen:query	#	admin	2025-11-29 18:04:57.953908		\N	
1056	生成修改	116	2	#				1	0	F	0	0	tool:gen:edit	#	admin	2025-11-29 18:04:57.970664		\N	
1057	生成删除	116	3	#				1	0	F	0	0	tool:gen:remove	#	admin	2025-11-29 18:04:57.99373		\N	
1058	导入代码	116	4	#				1	0	F	0	0	tool:gen:import	#	admin	2025-11-29 18:04:58.013451		\N	
1059	预览代码	116	5	#				1	0	F	0	0	tool:gen:preview	#	admin	2025-11-29 18:04:58.031345		\N	
1060	生成代码	116	6	#				1	0	F	0	0	tool:gen:code	#	admin	2025-11-29 18:04:58.045704		\N	
\.


--
-- Data for Name: sys_notice; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_notice (notice_id, notice_title, notice_type, notice_content, status, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	温馨提醒：2018-07-01 若依新版本发布啦	2	\\xe696b0e78988e69cace58685e5aeb9	0	admin	2025-11-30 02:50:06.386079		\N	管理员
2	维护通知：2018-07-01 若依系统凌晨维护	1	\\xe7bbb4e68aa4e58685e5aeb9	0	admin	2025-11-30 02:50:06.386079		\N	管理员
\.


--
-- Data for Name: sys_oper_log; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_oper_log (oper_id, title, business_type, method, request_method, operator_type, oper_name, dept_name, oper_url, oper_ip, oper_location, oper_param, json_result, status, error_msg, oper_time, cost_time) FROM stdin;
\.


--
-- Data for Name: sys_post; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_post (post_id, post_code, post_name, post_sort, status, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	ceo	董事长	1	0	admin	2025-11-29 17:40:31.31441		\N	
2	se	项目经理	2	0	admin	2025-11-29 17:40:31.328258		\N	
3	hr	人力资源	3	0	admin	2025-11-29 17:40:31.348261		\N	
4	user	普通员工	4	0	admin	2025-11-29 17:40:31.369786		\N	
\.


--
-- Data for Name: sys_role; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark) FROM stdin;
1	超级管理员	admin	1	1	t	t	0	0	admin	2025-11-29 17:44:42.640675		\N	超级管理员
2	普通角色	common	2	2	t	t	0	0	admin	2025-11-29 17:44:42.660253		\N	普通角色
\.


--
-- Data for Name: sys_role_dept; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_role_dept (role_id, dept_id) FROM stdin;
2	100
2	101
2	105
\.


--
-- Data for Name: sys_role_menu; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_role_menu (role_id, menu_id) FROM stdin;
2	1
2	2
2	3
2	4
2	100
2	101
2	102
2	103
2	104
2	105
2	106
2	107
2	108
2	109
2	110
2	111
2	112
2	113
2	114
2	115
2	116
2	117
2	500
2	501
2	1000
2	1001
2	1002
2	1003
2	1004
2	1005
2	1006
2	1007
2	1008
2	1009
2	1010
2	1011
2	1012
2	1013
2	1014
2	1015
2	1016
2	1017
2	1018
2	1019
2	1020
2	1021
2	1022
2	1023
2	1024
2	1025
2	1026
2	1027
2	1028
2	1029
2	1030
2	1031
2	1032
2	1033
2	1034
2	1035
2	1036
2	1037
2	1038
2	1039
2	1040
2	1041
2	1042
2	1043
2	1044
2	1045
2	1046
2	1047
2	1048
2	1049
2	1050
2	1051
2	1052
2	1053
2	1054
2	1055
2	1056
2	1057
2	1058
2	1059
2	1060
\.


--
-- Data for Name: sys_user; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time, update_by, update_time, remark, password_changed, default_password, last_password_change_time) FROM stdin;
2	105	ry	若依	00	ry@qq.com	15666666666	1		$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	0	0	127.0.0.1	2025-11-29 17:37:53.31359	2025-11-29 17:37:53.31359	admin	2025-11-29 17:37:53.31359		\N	测试员	f	f	\N
1	103	admin	若依	00	ry@163.com	15888888888	1		$2a$10$E6lxuh2OhygqrXQfaImDiuoU75ZGPoR8XEsEjnodcYXtgitL/CZda	0	0	127.0.0.1	2025-11-29 17:37:53.28308	2025-11-29 17:37:53.28308	admin	2025-11-29 17:37:53.28308		\N	管理员	f	f	\N
\.


--
-- Data for Name: sys_user_post; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_user_post (user_id, post_id) FROM stdin;
1	1
2	2
\.


--
-- Data for Name: sys_user_role; Type: TABLE DATA; Schema: xie_tm; Owner: postgres
--

COPY xie_tm.sys_user_role (user_id, role_id) FROM stdin;
1	1
2	2
\.


--
-- Name: sys_config_config_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_config_config_id_seq', 8, true);


--
-- Name: sys_dept_dept_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_dept_dept_id_seq', 200, false);


--
-- Name: sys_dict_data_dict_code_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_dict_data_dict_code_seq', 29, true);


--
-- Name: sys_dict_type_dict_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_dict_type_dict_id_seq', 10, true);


--
-- Name: sys_job_job_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_job_job_id_seq', 3, true);


--
-- Name: sys_job_log_job_log_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_job_log_job_log_id_seq', 1, false);


--
-- Name: sys_login_info_info_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_login_info_info_id_seq', 1, false);


--
-- Name: sys_menu_menu_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_menu_menu_id_seq', 2000, false);


--
-- Name: sys_notice_notice_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_notice_notice_id_seq', 2, true);


--
-- Name: sys_oper_log_oper_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_oper_log_oper_id_seq', 1, false);


--
-- Name: sys_post_post_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_post_post_id_seq', 4, true);


--
-- Name: sys_role_role_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_role_role_id_seq', 101, true);


--
-- Name: sys_user_user_id_seq; Type: SEQUENCE SET; Schema: xie_tm; Owner: postgres
--

SELECT pg_catalog.setval('xie_tm.sys_user_user_id_seq', 101, true);


--
-- Name: sys_config sys_config_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_config
    ADD CONSTRAINT sys_config_pkey PRIMARY KEY (config_id);


--
-- Name: sys_dept sys_dept_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_dept
    ADD CONSTRAINT sys_dept_pkey PRIMARY KEY (dept_id);


--
-- Name: sys_dict_data sys_dict_data_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_dict_data
    ADD CONSTRAINT sys_dict_data_pkey PRIMARY KEY (dict_code);


--
-- Name: sys_dict_type sys_dict_type_dict_type_key; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_dict_type
    ADD CONSTRAINT sys_dict_type_dict_type_key UNIQUE (dict_type);


--
-- Name: sys_dict_type sys_dict_type_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_dict_type
    ADD CONSTRAINT sys_dict_type_pkey PRIMARY KEY (dict_id);


--
-- Name: sys_job sys_job_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_job
    ADD CONSTRAINT sys_job_pkey PRIMARY KEY (job_id);


--
-- Name: sys_login_info sys_login_info_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_login_info
    ADD CONSTRAINT sys_login_info_pkey PRIMARY KEY (info_id);


--
-- Name: sys_menu sys_menu_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_menu
    ADD CONSTRAINT sys_menu_pkey PRIMARY KEY (menu_id);


--
-- Name: sys_oper_log sys_oper_log_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_oper_log
    ADD CONSTRAINT sys_oper_log_pkey PRIMARY KEY (oper_id);


--
-- Name: sys_post sys_post_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_post
    ADD CONSTRAINT sys_post_pkey PRIMARY KEY (post_id);


--
-- Name: sys_role_dept sys_role_dept_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_role_dept
    ADD CONSTRAINT sys_role_dept_pkey PRIMARY KEY (role_id, dept_id);


--
-- Name: sys_role_menu sys_role_menu_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_role_menu
    ADD CONSTRAINT sys_role_menu_pkey PRIMARY KEY (role_id, menu_id);


--
-- Name: sys_role sys_role_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_role
    ADD CONSTRAINT sys_role_pkey PRIMARY KEY (role_id);


--
-- Name: sys_user sys_user_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_user
    ADD CONSTRAINT sys_user_pkey PRIMARY KEY (user_id);


--
-- Name: sys_user_post sys_user_post_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_user_post
    ADD CONSTRAINT sys_user_post_pkey PRIMARY KEY (user_id, post_id);


--
-- Name: sys_user_role sys_user_role_pkey; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_user_role
    ADD CONSTRAINT sys_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: sys_config unique_config_key; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_config
    ADD CONSTRAINT unique_config_key UNIQUE (config_key);


--
-- Name: sys_job unique_job; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_job
    ADD CONSTRAINT unique_job UNIQUE (job_id, job_name, job_group);


--
-- Name: sys_job_log unique_job_log_id; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_job_log
    ADD CONSTRAINT unique_job_log_id PRIMARY KEY (job_log_id);


--
-- Name: sys_notice unique_notice_id; Type: CONSTRAINT; Schema: xie_tm; Owner: postgres
--

ALTER TABLE ONLY xie_tm.sys_notice
    ADD CONSTRAINT unique_notice_id PRIMARY KEY (notice_id);


--
-- Name: idx_sys_login_info_lt; Type: INDEX; Schema: xie_tm; Owner: postgres
--

CREATE INDEX idx_sys_login_info_lt ON xie_tm.sys_login_info USING btree (login_time);


--
-- Name: idx_sys_login_info_s; Type: INDEX; Schema: xie_tm; Owner: postgres
--

CREATE INDEX idx_sys_login_info_s ON xie_tm.sys_login_info USING btree (status);


--
-- Name: idx_sys_oper_log_bt; Type: INDEX; Schema: xie_tm; Owner: postgres
--

CREATE INDEX idx_sys_oper_log_bt ON xie_tm.sys_oper_log USING btree (business_type);


--
-- Name: idx_sys_oper_log_ot; Type: INDEX; Schema: xie_tm; Owner: postgres
--

CREATE INDEX idx_sys_oper_log_ot ON xie_tm.sys_oper_log USING btree (oper_time);


--
-- Name: idx_sys_oper_log_s; Type: INDEX; Schema: xie_tm; Owner: postgres
--

CREATE INDEX idx_sys_oper_log_s ON xie_tm.sys_oper_log USING btree (status);


-- ================================================================
-- Token 注册表
-- ================================================================

CREATE TABLE xie_tm.sys_token_registry (
                                           id BIGSERIAL PRIMARY KEY,
                                           access_token_id VARCHAR(36) NOT NULL,
                                           refresh_token_hash VARCHAR(64) NOT NULL,
                                           "user_id" BIGINT NOT NULL,                      -- 加双引号，user 是保留字
                                           username VARCHAR(50) NOT NULL,
                                           device_fingerprint VARCHAR(64),
                                           access_expr_at TIMESTAMP NOT NULL,
                                           refresh_expr_at TIMESTAMP NOT NULL,
                                           is_revoked BOOLEAN DEFAULT FALSE,
                                           revoked_at TIMESTAMP,
                                           revoke_reason VARCHAR(50),
                                           version BIGINT DEFAULT 0,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                           CONSTRAINT uk_token_access_id UNIQUE (access_token_id),
                                           CONSTRAINT uk_token_refresh_hash UNIQUE (refresh_token_hash)
);

-- 索引（user_id 也要加双引号）
CREATE INDEX idx_token_user ON xie_tm.sys_token_registry("user_id", is_revoked);
CREATE INDEX idx_token_access_expr ON xie_tm.sys_token_registry(access_expr_at);
CREATE INDEX idx_token_refresh_expr ON xie_tm.sys_token_registry(refresh_expr_at);
CREATE INDEX idx_token_device ON xie_tm.sys_token_registry(device_fingerprint);

-- 注释
COMMENT ON TABLE xie_tm.sys_token_registry IS 'Token 注册表：维护 Access Token 与 Refresh Token
  的映射关系';
COMMENT ON COLUMN xie_tm.sys_token_registry.id IS '主键';
COMMENT ON COLUMN xie_tm.sys_token_registry.access_token_id IS 'Access Token 唯一标识（UUID），嵌入在
   JWT payload 的 tokenId 字段中';
COMMENT ON COLUMN xie_tm.sys_token_registry.refresh_token_hash IS 'Refresh Token 的 SHA-256 哈希值';
COMMENT ON COLUMN xie_tm.sys_token_registry."user_id" IS '用户ID';
COMMENT ON COLUMN xie_tm.sys_token_registry.username IS '用户名';
COMMENT ON COLUMN xie_tm.sys_token_registry.device_fingerprint IS '设备指纹';
COMMENT ON COLUMN xie_tm.sys_token_registry.access_expr_at IS 'Access Token 过期时间（2小时）';
COMMENT ON COLUMN xie_tm.sys_token_registry.refresh_expr_at IS 'Refresh Token 过期时间（7天）';
COMMENT ON COLUMN xie_tm.sys_token_registry.is_revoked IS '是否已撤销';
COMMENT ON COLUMN xie_tm.sys_token_registry.revoked_at IS '撤销时间';
COMMENT ON COLUMN xie_tm.sys_token_registry.revoke_reason IS '撤销原因: KICK_OUT, PWD_CHANGE,
  PERM_CHANGE, ADMIN_REVOKE';
COMMENT ON COLUMN xie_tm.sys_token_registry.version IS '乐观锁版本号';
COMMENT ON COLUMN xie_tm.sys_token_registry.created_at IS '创建时间';
COMMENT ON COLUMN xie_tm.sys_token_registry.updated_at IS '更新时间';

--
-- PostgreSQL database dump complete
--

\unrestrict LTDVl7nOi2p7x8aXGZ2xsoeUw0ZKLzFTpBlpLV7bRN7LzM5oaTrW0Xp1sRG7WCQ

