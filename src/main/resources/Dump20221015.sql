-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: dev_ims_db
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `benefit`
--

DROP TABLE IF EXISTS `benefit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `benefit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `benefit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benefit`
--

LOCK TABLES `benefit` WRITE;
/*!40000 ALTER TABLE `benefit` DISABLE KEYS */;
INSERT INTO `benefit` VALUES (1,'admin','2022-01-01 00:00:00.000000','admin','2022-04-06 00:00:00.000000',_binary '\0','Social and health insurance'),(2,'admin','2021-06-12 00:00:00.000000','admin','2022-01-20 00:00:00.000000',_binary '\0','Comprehensive health plan'),(3,'admin','2021-02-01 00:00:00.000000','admin','2021-12-11 00:00:00.000000',_binary '\0','Ancillary benefits'),(4,'admin','2020-11-19 00:00:00.000000','admin','2022-01-27 00:00:00.000000',_binary '\0','Holidays and days off'),(5,'admin','2021-08-15 00:00:00.000000','admin','2022-04-22 00:00:00.000000',_binary '\0','Professional development'),(6,'admin','2019-12-09 00:00:00.000000','admin','2022-04-10 00:00:00.000000',_binary '\0','Working equipment'),(7,'admin','2022-01-21 00:00:00.000000','admin','2022-08-10 00:00:00.000000',_binary '\0','Travel'),(8,'admin','2021-03-21 00:00:00.000000','admin','2022-01-24 00:00:00.000000',_binary '\0','Vehicle');
/*!40000 ALTER TABLE `benefit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `candidate`
--

DROP TABLE IF EXISTS `candidate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `candidate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `address` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `attach_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `candidate_position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `candidate_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `highest_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `note` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `year_experience` int DEFAULT NULL,
  `recruiter_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdilxfduq7o8ys2pd8gf7dn632` (`recruiter_id`),
  CONSTRAINT `FKdilxfduq7o8ys2pd8gf7dn632` FOREIGN KEY (`recruiter_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidate`
--

LOCK TABLES `candidate` WRITE;
/*!40000 ALTER TABLE `candidate` DISABLE KEYS */;
INSERT INTO `candidate` VALUES (1,'recruiter','2022-10-10 10:49:30.713430','admin','2022-10-15 10:37:42.111961',_binary '\0','Xuan Dinh, Ha Noi, Viet Nam',NULL,'BACKEND_DEVELOPER','WAITING_FOR_APPROVAL','1994-02-24','nvdat666@gmail.com','Nguyễn Văn Đạt','FEMALE','BACHELOR_DEGREE','Graduated in Information Technology\r\nExperience in programming Unreal Engine, C++ \r\nSolid knowledge of Unreal Engine software development/C++','0974737007',2,3),(2,'recruiter','2022-10-10 10:51:56.202316','admin','2022-10-15 09:49:03.131623',_binary '\0','Thanh Oai, Hà Nội',NULL,'BUSINESS_ANALYST','OPEN','2000-10-11','lhc270101@gmail.com','Lê Hải Cảng','MALE','BACHELOR_DEGREE','Có kinh nghiệm lập trình Unreal Engine, C++\r\nCó kiến thức vững chắc về phát triển các ứng dụng/ phần mềm trên Unreal Engine/C++','08492742852',2,3),(3,'recruiter','2022-10-10 10:53:15.275690','admin','2022-10-15 09:55:52.493525',_binary '\0','Roman Plaza',NULL,'PROJECT_MANAGER','OPEN','1995-06-15','phimvabia@gmail.com','Nguyễn Việt Cường','MALE','MASTER_DEGREE','Have experience in manual testing (Web testing, API, Mobile app are preferred). \r\nHave experience in Database testing, be able to test at Function level and Non-Function level. ','0893247231',3,3),(6,'recruiter','2022-10-10 10:57:48.467327','admin','2022-10-15 10:11:45.479786',_binary '\0','Nam Từ Liêm, Hà Nội',NULL,'BUSINESS_ANALYST','OPEN','1998-07-15','thuc03081998@gmail.com','Lê Trí Thực','FEMALE','MASTER_DEGREE','non-IT','085235253',3,3),(7,'recruiter','2022-10-10 10:59:01.330719','admin','2022-10-15 09:50:01.881922',_binary '\0','Hoà Lạc. Hà Nội',NULL,'HR','OPEN','1998-02-18','ducvannguyen998@gmail.com','Nguyễn Đức Văn','FEMALE','MASTER_DEGREE','Có kinh nghiệm làm việc với ngôn ngữ lập trình C#, ASP.NET, MVC;\r\nCó hiểu biết Javascript, HTML (HTML5), CSS (CSS3), jQuery, .NET core (kinh nghiệm về  Angular, Jquery JS\r\nNắm vững kiến thức cơ bản về MSSQL Server (2014+) ','0907542311',3,3),(8,'recruiter','2022-10-10 11:00:05.093036','admin','2022-10-15 09:54:16.014356',_binary '\0','Hà Đông',NULL,'BACKEND_DEVELOPER','OPEN','2000-04-04','dinhthaitn96@gmail.com','Đỗ Đình Thái','MALE','BACHELOR_DEGREE','2 - 4 years of experience in Java, Spring, Oracle, MySQL\r\nExperience with system for B2C/B2B business model is a big plus\r\nExperience with RESTful APIs, Microservices','082357242421',3,3),(9,'recruiter','2022-10-10 11:01:31.309453','admin','2022-10-15 09:53:08.084246',_binary '\0','Nhổn, Hà Nội',NULL,'TESTER','OPEN','1995-06-07','ninhdinhvinh11012001@gmail.com','Ninh Đình Vinh','MALE','MASTER_DEGREE','Có kiến thức CNTT: Cấu trúc phần mềm, phân tích và thiết kế hệ thống, cơ sở dữ liệu.\r\nCó khả năng phân tích, tư duy logic tốt.\r\nCó kiến thức về quy trình phát triển phần mềm (Agile, Waterfall,…)\r\nSử dụng tốt các công cụ: Axure, Figma, Draw.io,….','0975252232',4,3),(10,'recruiter','2022-10-10 11:03:30.710382','admin','2022-10-15 10:11:59.305570',_binary '\0','Ha Dong',NULL,'TESTER','OPEN','1995-06-08','annhi8112013@gmail.com','Nguyễn Mạnh Cường','MALE','BACHELOR_DEGREE','non-IT','099572241',4,3),(11,'admin','2022-10-15 09:43:56.889293','admin','2022-10-15 10:05:14.631396',_binary '\0','Tây Hồ, Hà Nội',NULL,'BACKEND_DEVELOPER','OPEN','1996-10-16','ntp.vuatrochoi@gmail.com','Nguyễn Trí Phúc','MALE','BACHELOR_DEGREE','Năng động, kiến thức tốt, tư duy tốt','0975418524',1,3),(12,'admin','2022-10-15 09:46:48.773142','admin','2022-10-15 09:46:48.773142',_binary '\0','Hoàng Mai, Hà Nội',NULL,'BACKEND_DEVELOPER','OPEN','1993-07-12','minhtu93931@gmail.com','Trần Minh Tú','MALE','BACHELOR_DEGREE','Tư duy tốt, kiến thức tốt\r\nCó kinh nghiệm java >3 năm','0912485712',3,3),(13,'recruiter','2022-10-10 11:03:30.710382','admin','2022-10-15 10:05:01.284491',_binary '\0','Ha Dong',NULL,'TESTER','OPEN','1995-06-08','blueworkblue@gmail.com','Nguyễn Tất Thắng','MALE','BACHELOR_DEGREE','4 years of experience in Java, Spring, Oracle, MySQL','099572241',4,3);
/*!40000 ALTER TABLE `candidate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `candidate_skill`
--

DROP TABLE IF EXISTS `candidate_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `candidate_skill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `candidate_id` bigint DEFAULT NULL,
  `skill_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKijjf42p0sh2c2na28g5aalx2p` (`candidate_id`),
  KEY `FKb7cxhiqhcah7c20a2cdlvr1f8` (`skill_id`),
  CONSTRAINT `FKb7cxhiqhcah7c20a2cdlvr1f8` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`),
  CONSTRAINT `FKijjf42p0sh2c2na28g5aalx2p` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidate_skill`
--

LOCK TABLES `candidate_skill` WRITE;
/*!40000 ALTER TABLE `candidate_skill` DISABLE KEYS */;
INSERT INTO `candidate_skill` VALUES (61,'admin','2022-10-15 09:46:48.775136','admin','2022-10-15 09:46:48.775136',_binary '\0',12,1),(62,'admin','2022-10-15 09:49:03.125640','admin','2022-10-15 09:49:03.125640',_binary '\0',2,2),(63,'admin','2022-10-15 09:49:03.126666','admin','2022-10-15 09:49:03.126666',_binary '\0',2,7),(64,'admin','2022-10-15 09:49:03.127634','admin','2022-10-15 09:49:03.127634',_binary '\0',2,9),(65,'admin','2022-10-15 09:49:03.128632','admin','2022-10-15 09:49:03.128632',_binary '\0',2,5),(66,'admin','2022-10-15 09:49:03.129629','admin','2022-10-15 09:49:03.129629',_binary '\0',2,6),(67,'admin','2022-10-15 09:50:01.878962','admin','2022-10-15 09:50:01.878962',_binary '\0',7,3),(68,'admin','2022-10-15 09:50:01.879926','admin','2022-10-15 09:50:01.879926',_binary '\0',7,1),(76,'admin','2022-10-15 09:51:58.866126','admin','2022-10-15 09:51:58.866126',_binary '\0',1,2),(77,'admin','2022-10-15 09:51:58.867151','admin','2022-10-15 09:51:58.867151',_binary '\0',1,1),(78,'admin','2022-10-15 09:51:58.869118','admin','2022-10-15 09:51:58.869118',_binary '\0',1,3),(79,'admin','2022-10-15 09:53:08.081252','admin','2022-10-15 09:53:08.081252',_binary '\0',9,2),(80,'admin','2022-10-15 09:53:08.082225','admin','2022-10-15 09:53:08.082225',_binary '\0',9,5),(81,'admin','2022-10-15 09:53:08.083250','admin','2022-10-15 09:53:08.083250',_binary '\0',9,4),(82,'admin','2022-10-15 09:54:16.011387','admin','2022-10-15 09:54:16.011387',_binary '\0',8,2),(83,'admin','2022-10-15 09:54:16.012385','admin','2022-10-15 09:54:16.012385',_binary '\0',8,3),(84,'admin','2022-10-15 09:54:16.013360','admin','2022-10-15 09:54:16.013360',_binary '\0',8,5),(87,'admin','2022-10-15 09:55:52.492528','admin','2022-10-15 09:55:52.492528',_binary '\0',3,6),(92,'admin','2022-10-15 10:05:14.627409','admin','2022-10-15 10:05:14.627409',_binary '\0',11,6),(93,'admin','2022-10-15 10:05:14.629402','admin','2022-10-15 10:05:14.629402',_binary '\0',11,2),(94,'admin','2022-10-15 10:05:14.630400','admin','2022-10-15 10:05:14.630400',_binary '\0',11,4),(95,'admin','2022-10-15 10:11:45.478820','admin','2022-10-15 10:11:45.478820',_binary '\0',6,8),(96,'admin','2022-10-15 10:11:45.479786','admin','2022-10-15 10:11:45.479786',_binary '\0',6,9),(97,'admin','2022-10-15 10:11:59.302549','admin','2022-10-15 10:11:59.302549',_binary '\0',10,7),(98,'admin','2022-10-15 10:11:59.303576','admin','2022-10-15 10:11:59.303576',_binary '\0',10,3),(99,'admin','2022-10-15 10:11:59.304544','admin','2022-10-15 10:11:59.304544',_binary '\0',10,2),(100,'admin','2022-10-15 10:11:59.304544','admin','2022-10-15 10:11:59.304544',_binary '\0',10,1);
/*!40000 ALTER TABLE `candidate_skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interview_schedule`
--

DROP TABLE IF EXISTS `interview_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interview_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `interview_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `interview_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `meeting_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `note` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `schedule_time_from` datetime(6) DEFAULT NULL,
  `schedule_time_to` datetime(6) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `candidate_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `interview_position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `job_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK73nhoi0kkr9uyol6qtl05cwyn` (`candidate_id`),
  KEY `FKi3cmk4cr4sbsircs08snjygrq` (`user_id`),
  KEY `FKtmw2tjq1pky2wmdpo6ilcbe9d` (`job_id`),
  CONSTRAINT `FK73nhoi0kkr9uyol6qtl05cwyn` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`id`),
  CONSTRAINT `FKi3cmk4cr4sbsircs08snjygrq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKtmw2tjq1pky2wmdpo6ilcbe9d` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interview_schedule`
--

LOCK TABLES `interview_schedule` WRITE;
/*!40000 ALTER TABLE `interview_schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `interview_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interview_schedule_user`
--

DROP TABLE IF EXISTS `interview_schedule_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interview_schedule_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `interview_schedule_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdyu7y5ohrkpy2rtl4lihpjx7t` (`interview_schedule_id`),
  KEY `FKa4t56pm8yct19msf1v322ubq4` (`user_id`),
  CONSTRAINT `FKa4t56pm8yct19msf1v322ubq4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKdyu7y5ohrkpy2rtl4lihpjx7t` FOREIGN KEY (`interview_schedule_id`) REFERENCES `interview_schedule` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interview_schedule_user`
--

LOCK TABLES `interview_schedule_user` WRITE;
/*!40000 ALTER TABLE `interview_schedule_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `interview_schedule_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `description` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `job_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `job_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `job_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `salary_from` decimal(19,2) DEFAULT NULL,
  `salary_to` decimal(19,2) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `working_address` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,'admin','2022-10-15 09:57:34.386077','admin','2022-10-15 09:57:34.386077',_binary '\0','Read and understand requirement specification\r\nAnalyze and create software specification\r\nCreate System Design with instruction from Team Lead\r\nCreate Detail Design independently\r\nDo coding and unit test \r\nProvide technical guidance, review on source code and design documents to lower level staffs in project ','2022-10-23','Senior','OPEN','Web Developer (Cake PHP, Laravel, Java)',15000000.00,20000000.00,'2022-10-15','Etown 2, 364 Cong Hoa, Tan Binh, Ho Chi Minh'),(2,'admin','2022-10-15 09:58:37.375846','admin','2022-10-15 09:58:37.375846',_binary '\0','We are looking for engineer with broad experience control and evaluate project quality. We expect candidates capable of using Greybox testing, and testing new techniques and technologies (Related to APIs, service servers, and databases. \r\n\r\nYou will sometimes join multiple projects and lead other QCs. It is also possible to work with partner teams in foreign countries such as Japan, and Korea to accomplish the project objective.','2022-10-31','Senior, Leader','OPEN','Senior Manual QA Engineer',20000000.00,30000000.00,'2022-10-25','Viettel Building, 285 Cach Mang Thang Tam Street, Ward 12, District 10, Ho Chi Minh'),(3,'admin','2022-10-15 09:59:34.145538','admin','2022-10-15 09:59:34.145538',_binary '\0','Bachelor Degree in Computer Engineer, Computer Science, IT or other related field\r\nExperienced working with Java or Golang software development\r\nExperienced in banking domain is a BIG plus\r\nExperience working with one or more from the following: web application development, Unix environments, mobile application development, information retrieval, networking, developing large-scale software system, version control system, and/or security software development\r\nGood communication in English','2022-10-31','Fresher, Junior','OPEN','Java / Golang Engineer (All levels)',10000000.00,15000000.00,'2022-10-19','Friendship Tower, 31 Le Duan, Ben Nghe Ward, District 1, Ho Chi Minh');
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_benefit`
--

DROP TABLE IF EXISTS `job_benefit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_benefit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `benefit_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `benefit_id` bigint DEFAULT NULL,
  `job_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrwgx5uvlt0roso4vuukvtbwmk` (`benefit_id`),
  KEY `FKbreqdqw5ps6hd3rsdris4qcs3` (`job_id`),
  CONSTRAINT `FKbreqdqw5ps6hd3rsdris4qcs3` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`),
  CONSTRAINT `FKrwgx5uvlt0roso4vuukvtbwmk` FOREIGN KEY (`benefit_id`) REFERENCES `benefit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_benefit`
--

LOCK TABLES `job_benefit` WRITE;
/*!40000 ALTER TABLE `job_benefit` DISABLE KEYS */;
INSERT INTO `job_benefit` VALUES (1,'admin','2022-10-15 09:57:34.390038','admin','2022-10-15 09:57:34.390038',_binary '\0',NULL,2,1),(2,'admin','2022-10-15 09:57:34.392033','admin','2022-10-15 09:57:34.392033',_binary '\0',NULL,5,1),(3,'admin','2022-10-15 09:58:37.377841','admin','2022-10-15 09:58:37.377841',_binary '\0',NULL,3,2),(4,'admin','2022-10-15 09:58:37.378871','admin','2022-10-15 09:58:37.378871',_binary '\0',NULL,2,2),(5,'admin','2022-10-15 09:58:37.379835','admin','2022-10-15 09:58:37.379835',_binary '\0',NULL,8,2),(6,'admin','2022-10-15 09:58:37.380833','admin','2022-10-15 09:58:37.380833',_binary '\0',NULL,5,2),(7,'admin','2022-10-15 09:58:37.381864','admin','2022-10-15 09:58:37.381864',_binary '\0',NULL,7,2),(8,'admin','2022-10-15 09:59:34.146533','admin','2022-10-15 09:59:34.146533',_binary '\0',NULL,3,3),(9,'admin','2022-10-15 09:59:34.147531','admin','2022-10-15 09:59:34.147531',_binary '\0',NULL,5,3),(10,'admin','2022-10-15 09:59:34.148529','admin','2022-10-15 09:59:34.148529',_binary '\0',NULL,2,3);
/*!40000 ALTER TABLE `job_benefit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_skill`
--

DROP TABLE IF EXISTS `job_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_skill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `job_id` bigint DEFAULT NULL,
  `skill_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9ix4wg520ii2gu2felxdhdup6` (`job_id`),
  KEY `FKj33qbbf3vk1lvhqpcosnh54u1` (`skill_id`),
  CONSTRAINT `FK9ix4wg520ii2gu2felxdhdup6` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`),
  CONSTRAINT `FKj33qbbf3vk1lvhqpcosnh54u1` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_skill`
--

LOCK TABLES `job_skill` WRITE;
/*!40000 ALTER TABLE `job_skill` DISABLE KEYS */;
INSERT INTO `job_skill` VALUES (1,'admin','2022-10-15 09:57:34.397019','admin','2022-10-15 09:57:34.397019',_binary '\0',1,2),(2,'admin','2022-10-15 09:57:34.398041','admin','2022-10-15 09:57:34.398041',_binary '\0',1,4),(3,'admin','2022-10-15 09:57:34.399014','admin','2022-10-15 09:57:34.399014',_binary '\0',1,5),(4,'admin','2022-10-15 09:58:37.382858','admin','2022-10-15 09:58:37.382858',_binary '\0',2,4),(5,'admin','2022-10-15 09:58:37.384822','admin','2022-10-15 09:58:37.384822',_binary '\0',2,1),(6,'admin','2022-10-15 09:58:37.385819','admin','2022-10-15 09:58:37.385819',_binary '\0',2,2),(7,'admin','2022-10-15 09:59:34.149554','admin','2022-10-15 09:59:34.149554',_binary '\0',3,1);
/*!40000 ALTER TABLE `job_skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offer`
--

DROP TABLE IF EXISTS `offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `basic_salary` decimal(19,2) DEFAULT NULL,
  `contract_from` date DEFAULT NULL,
  `contract_to` date DEFAULT NULL,
  `contract_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `note` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `offer_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `offer_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `approved_by_id` bigint DEFAULT NULL,
  `candidate_id` bigint DEFAULT NULL,
  `interview_schedule_id` bigint DEFAULT NULL,
  `recruiter_owner_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK988ba2biepl5jn4gjrstso7fb` (`approved_by_id`),
  KEY `FK9irqkqj4ujilh16d896pwf5e1` (`candidate_id`),
  KEY `FKcq2s859iyqdjsnqnb8d90sr6s` (`interview_schedule_id`),
  KEY `FKec6up2shr3j2419tlkqjll4yv` (`recruiter_owner_id`),
  CONSTRAINT `FK988ba2biepl5jn4gjrstso7fb` FOREIGN KEY (`approved_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9irqkqj4ujilh16d896pwf5e1` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`id`),
  CONSTRAINT `FKcq2s859iyqdjsnqnb8d90sr6s` FOREIGN KEY (`interview_schedule_id`) REFERENCES `interview_schedule` (`id`),
  CONSTRAINT `FKec6up2shr3j2419tlkqjll4yv` FOREIGN KEY (`recruiter_owner_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
/*!40000 ALTER TABLE `offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `expired_time` datetime(6) DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_g0guo4k8krgpwuagos61oc06j` (`token`),
  KEY `FK83nsrttkwkb6ym0anu051mtxn` (`user_id`),
  CONSTRAINT `FK83nsrttkwkb6ym0anu051mtxn` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill`
--

DROP TABLE IF EXISTS `skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `skill_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill`
--

LOCK TABLES `skill` WRITE;
/*!40000 ALTER TABLE `skill` DISABLE KEYS */;
INSERT INTO `skill` VALUES (1,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','Java'),(2,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','C'),(3,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','Python'),(4,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','JavaScript'),(5,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','.net'),(6,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','Angular'),(7,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','NodeJS'),(8,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','Business Analyst'),(9,'system','2022-01-01 00:00:00.000000','system','2022-01-01 00:00:00.000000',_binary '\0','Communication');
/*!40000 ALTER TABLE `skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `dob` date DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'system','2022-09-25 00:00:00.000000','system','2022-09-25 00:00:00.000000',_binary '\0','annhi8112013@gmail.com','$2a$10$AB9H4H705JZfWgNxXKfVPe6l.A.7fxG6GI9eHhzT.SB5SCFMai6yq','ROLE_ADMIN','ACTIVE','admin','IT',NULL,'Admin','MALE',NULL,'0945216215',NULL),(2,'system','2022-09-25 00:00:00.000000','system','2022-09-25 00:00:00.000000',_binary '\0','annhi8112013@gmail.com','$2a$10$22sXKkz5T3sERURmDTV6puuZA35j5Zo6gaw4gkrKLG1mBe/aRhX9W','ROLE_MANAGER','ACTIVE','manager','IT',NULL,'Manager','MALE',NULL,'0963563628',NULL),(3,'system','2022-09-25 00:00:00.000000','system','2022-09-25 00:00:00.000000',_binary '\0','annhi8112013@gmail.com','$2a$10$3TBR08GmhsKCJK24qopE7ew.3eAaTdiXF1QTtodcx5mrjo0jFrbky','ROLE_RECRUITER','ACTIVE','recruiter','IT',NULL,'Recruiter','MALE',NULL,'0948787852',NULL),(4,'system','2022-09-25 00:00:00.000000','system','2022-09-25 00:00:00.000000',_binary '\0','annhi8112013@gmail.com','$2a$10$V31ECnlzE.k7gTuwVChJZ.tOG5YzALkJLnr682cp7J4o08pJd7hjS','ROLE_INTERVIEWER','ACTIVE','interviewer','IT',NULL,'Interviewer','MALE',NULL,'0912485715',NULL),(12,'system','2022-09-25 00:00:00.000000','system','2022-09-25 00:00:00.000000',_binary '\0','annhi8112013@gmail.com','$2a$10$V31ECnlzE.k7gTuwVChJZ.tOG5YzALkJLnr682cp7J4o08pJd7hjS','ROLE_INTERVIEWER','ACTIVE','interviewer1','HR',NULL,'Interviewer1','MALE',NULL,'0963526324',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-15 10:41:08
