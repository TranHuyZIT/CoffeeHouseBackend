CREATE DATABASE  IF NOT EXISTS `coffeehouse` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `coffeehouse`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: coffeehouse
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `message` varchar(10000) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'2023-05-25 16:19:14.674000','','This is a test message','Hello from Gia Huy'),(2,'2023-05-25 16:21:02.823000','','This is a test message','Hello from Gia Huy'),(3,'2023-05-25 16:21:11.115000','','This is a test message','Hello from Gia Huy'),(4,'2023-05-25 18:16:30.511000','','This is a test message','Hello from Gia Huy'),(5,'2023-05-25 21:25:56.695000','','This is a test message','Hello from Gia Huy'),(6,'2023-05-25 22:08:25.975000','','This is a test message','Hello from Gia Huy'),(7,'2023-05-26 20:51:37.999000','','This is a test message','Hello from Gia Huy'),(8,'2023-05-26 20:51:45.157000','','This is a test message','Hello from Gia Huy'),(9,'2023-05-26 20:52:42.272000','','This is a test message','Hello from Gia Huy'),(10,'2023-05-26 21:02:34.025000','','This is a test message','Hello from Gia Huy'),(20,'2023-05-27 17:26:07.466000','','This is a test message','Hello from Gia Huy'),(21,'2023-05-27 17:26:40.083000','','This is a test message','Hello from Gia Huy'),(22,'2023-05-27 17:27:59.325000','','This is a test message','Hello from Gia Huy'),(23,'2023-05-27 17:30:21.436000','','This is a test message','Hello from Gia Huy'),(24,'2023-05-28 00:32:49.984000','','This is a test message','Hello from Gia Huy'),(25,'2023-05-28 00:33:16.556000','','This is a test message','Hello from Gia Huy'),(26,'2023-05-28 00:47:18.723000','','This is a test message','Hello from Gia Huy'),(27,'2023-05-28 00:47:45.534000','','This is a test message','Hello from Gia Huy'),(28,'2023-05-28 00:49:36.212000','','This is a test message','Hello from Gia Huy'),(29,'2023-05-28 00:54:59.464000','','This is a test private message','Hello from Gia Huy'),(31,'2023-05-28 01:50:35.049000','','Đơn hàng của bạn đang xử lý, giao tại B2-10, Đường số 3.','Đơn hàng của bạn tại The Coffee House');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-05 11:36:53
