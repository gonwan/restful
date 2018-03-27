-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 192.168.11.196    Database: restful_api
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_api`
--

DROP TABLE IF EXISTS `t_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_api` (
  `api_id` bigint(21) NOT NULL AUTO_INCREMENT,
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `api_name` varchar(100) DEFAULT NULL,
  `api_groups` varchar(50) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `datasource_id` bigint(21) NOT NULL,
  `datasource_database_id` bigint(21) NOT NULL,
  `logic_sql` text,
  `mandatory_condition` text,
  `where_available` tinyint(1) DEFAULT '1',
  `date_column` varchar(200) DEFAULT NULL COMMENT 'format: col_name:col_type:col_format',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `comments` varchar(1000) DEFAULT NULL,
  `extra1` varchar(200) DEFAULT NULL,
  `extra2` varchar(200) DEFAULT NULL,
  `extra3` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_api_group`
--

DROP TABLE IF EXISTS `t_api_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_api_group` (
  `group_id` bigint(21) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) DEFAULT NULL,
  `comments` varchar(1000) DEFAULT NULL,
  `extra1` varchar(200) DEFAULT NULL,
  `extra2` varchar(200) DEFAULT NULL,
  `extra3` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_authority`
--

DROP TABLE IF EXISTS `t_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_authority` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(21) NOT NULL,
  `read_api_group_ids` varchar(200) DEFAULT NULL,
  `write_api_group_ids` varchar(200) DEFAULT NULL,
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `comments` varchar(1000) DEFAULT NULL,
  `extra1` varchar(200) DEFAULT NULL,
  `extra2` varchar(200) DEFAULT NULL,
  `extra3` varchar(200) DEFAULT NULL,
  `max_row_once` bigint(21) DEFAULT '5000',
  `max_row_daily` bigint(21) DEFAULT '500000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_data_source`
--

DROP TABLE IF EXISTS `t_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_data_source` (
  `id` bigint(21) NOT NULL,
  `database_id` bigint(21) NOT NULL,
  `database_name` varchar(128) DEFAULT NULL,
  `connection_string` varchar(256) DEFAULT NULL,
  `dialect` tinyint(2) NOT NULL COMMENT '1 - oracle, 2 - sqlserver, 3 - mysql.',
  `read_username` varchar(32) DEFAULT NULL,
  `read_password` varchar(64) DEFAULT NULL,
  `write_username` varchar(32) DEFAULT NULL,
  `write_password` varchar(64) DEFAULT NULL,
  `comments` varchar(1000) DEFAULT NULL,
  `extra1` varchar(200) DEFAULT NULL,
  `extra2` varchar(200) DEFAULT NULL,
  `extra3` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`,`database_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `username` varchar(32) NOT NULL,
  `password` varchar(64) DEFAULT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `comments` varchar(1000) DEFAULT NULL,
  `extra1` varchar(200) DEFAULT NULL,
  `extra2` varchar(200) DEFAULT NULL,
  `extra3` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_index` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_date_count`
--

DROP TABLE IF EXISTS `t_user_date_count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_date_count` (
  `username` varchar(32) NOT NULL,
  `row_date` date NOT NULL,
  `row_count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`row_date`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-23 18:13:41
