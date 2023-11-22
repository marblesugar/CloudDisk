-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: disk
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `tbl_file`
--

DROP TABLE IF EXISTS `tbl_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_file` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名称',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `cur_dir` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前文件（夹）所在的父目录',
  `file_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件位置',
  `file_extention` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件后缀',
  `is_dir` int NOT NULL DEFAULT '0' COMMENT '是否是目录',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否被删除',
  `expired_time` datetime DEFAULT NULL COMMENT '过期时间',
  `creator_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_file`
--

LOCK TABLES `tbl_file` WRITE;
/*!40000 ALTER TABLE `tbl_file` DISABLE KEYS */;
INSERT INTO `tbl_file` VALUES (3,'视频.mp4','视屏',1018523,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/ab6cdb01f2fc47919acad0dd4a08eee5.mp4','.mp4',0,1,'2023-11-26 22:48:09',1),(6,'blue.jpg','图片',61137,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/4c30ce5fa7af491c97510e729308bb97.jpg','.jpg',0,1,'2023-11-26 22:37:37',1),(7,'national_img13.jpg','图片',91878,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/7f7a349a135c4295aec68ff8025f93a8.jpg','.jpg',0,1,'2023-11-26 22:37:37',1),(8,'public-icon1.png','图片',2464,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/6c57fe0bef654abf909151d0d724818f.png','.png',0,1,'2023-11-26 22:37:37',1),(9,'s60dbfa49b65dc.png','图片',55477,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/3f898363c4d84e7faef47944f8631d93.png','.png',0,1,'2023-11-26 22:37:31',1),(10,'s60dbfa9b1f6ac.png','图片',59639,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/775ca2da4b334fe3b5881a152e5e4a2b.png','.png',0,1,'2023-11-26 22:37:37',1),(11,'s61f3aa849862f.png','图片',24189,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/b2edb06217b240b0a34de2760bc2b68a.png','.png',0,1,'2023-11-26 22:37:37',1),(12,'s61ef692cd6b2c.jpg','图片',19081,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/12a13ff139494821a3a2fde38f0541af.jpg','.jpg',0,1,'2023-11-26 22:37:43',1),(13,'test.docx','文本',10022,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/6aea4b96fff148f19f3325f428ee0d0e.docx','.docx',0,1,'2023-11-26 22:37:43',1),(14,'s58bcfc33c893d.png','图片',60625,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/a6c6573cb3a143e69aa41deb154c224c.png','.png',0,1,'2023-11-26 22:43:38',1),(15,'s5f805daf10ee8.png','图片',6956,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/de7869c273a1432aaab3510b8bc8daf5.png','.png',0,1,'2023-11-26 22:47:44',1),(16,'national_img13.jpg','图片',91878,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/8e4ac484c87649989e8f4a859dd32e43.jpg','.jpg',0,1,'2023-11-26 22:48:09',1),(17,'national_img14.jpg','图片',90783,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/bbbdc753942444eaaea88bbe91d50f1b.jpg','.jpg',0,1,'2023-11-26 22:48:09',1),(18,'national_img14.jpg','图片',90783,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/3920e4473bd745b2927f684a1ed79a49.jpg','.jpg',0,0,NULL,1),(19,'test1',NULL,NULL,'D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/test1',NULL,1,0,NULL,1);
/*!40000 ALTER TABLE `tbl_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_share`
--

DROP TABLE IF EXISTS `tbl_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_share` (
  `shareId` int NOT NULL AUTO_INCREMENT,
  `shareUrl` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `shareUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` tinyint DEFAULT '1' COMMENT '1公开 2加密 -1已取消',
  `command` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提取码',
  PRIMARY KEY (`shareId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_share`
--

LOCK TABLES `tbl_share` WRITE;
/*!40000 ALTER TABLE `tbl_share` DISABLE KEYS */;
INSERT INTO `tbl_share` VALUES (1,'52ff1a6f','\\2','ft',1,NULL),(2,'d0c4d03a','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/996bac5491b74ee3a4ab2cd1df56052f.jpg','ft',1,NULL),(3,'acd789b6','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/996bac5491b74ee3a4ab2cd1df56052f.jpg','ft',1,NULL),(4,'090687af','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/996bac5491b74ee3a4ab2cd1df56052f.jpg','ft',1,NULL),(5,'ab020f30','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/996bac5491b74ee3a4ab2cd1df56052f.jpg','ft',1,NULL),(6,'77cd5461','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/996bac5491b74ee3a4ab2cd1df56052f.jpg','ft',1,NULL),(7,'d8fa7cc6','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/996bac5491b74ee3a4ab2cd1df56052f.jpg','ft',1,NULL),(8,'39f7b9ea','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/a6c6573cb3a143e69aa41deb154c224c.png','ft',1,NULL),(9,'03a20ce0','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/de7869c273a1432aaab3510b8bc8daf5.png','ft',1,NULL),(10,'ac8bd98d','D:\\1Asenior_up\\cloud-disk-master1\\cloud-disk-master\\springboot/src/main/resources/upload/1/3920e4473bd745b2927f684a1ed79a49.jpg','ft',1,NULL);
/*!40000 ALTER TABLE `tbl_share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user`
--

DROP TABLE IF EXISTS `tbl_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（md5加密）',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `role` int NOT NULL DEFAULT '0' COMMENT '角色（0用户，1管理员）',
  `salt` varchar(255) NOT NULL COMMENT 'md5加盐',
  `activation_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '确认码',
  `activation_time` datetime NOT NULL COMMENT '激活失效时间',
  `is_vaild` int NOT NULL DEFAULT '0' COMMENT '是否可用',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (1,'ft','5EAD15A8D02CA7A882C810E776728163','2998213038@qq.com',0,'pq6otl','77700979-bee8-49e1-b27c-048967d8b920','2023-11-14 10:54:19',1,0),(2,'z123','8F53C69D8512358BF51CE3BE9843BA33','2459452965@qq.com',0,'wx5goj','a8772c07-56d6-4739-9ddd-cd0bec5cf913','2023-11-19 22:46:49',0,0),(3,'dsf','8835DB8DB2AB9A59216D377EBA742639','2459452963@qq.com',0,'ludkpg','309ce397-276e-40bd-9f51-ff8ff404c29b','2023-11-19 22:49:47',0,0),(4,'dsf','2D4FDE6C44259A7372CA49286B45FF38','2459452365@qq.com',0,'oo3vla','eee53895-64f7-4874-9584-172be50e72be','2023-11-19 22:50:57',0,0);
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user_info`
--

DROP TABLE IF EXISTS `tbl_user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_user_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `creator_id` int NOT NULL,
  `sex` int DEFAULT NULL,
  `birth_day` date DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `nation` varchar(255) DEFAULT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `work_place` varchar(255) DEFAULT NULL,
  `home_place` varchar(255) DEFAULT NULL,
  `is_deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user_info`
--

LOCK TABLES `tbl_user_info` WRITE;
/*!40000 ALTER TABLE `tbl_user_info` DISABLE KEYS */;
INSERT INTO `tbl_user_info` VALUES (1,'ft','2998213038@qq.com','https://fuss10.elemecdn.com/2/11/6535bcfb26e4c79b48ddde44f4b6fjpeg.jpeg',1,1,'2023-11-03','中国','汉族','学生','济南','济南',0);
/*!40000 ALTER TABLE `tbl_user_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-19 23:29:09
