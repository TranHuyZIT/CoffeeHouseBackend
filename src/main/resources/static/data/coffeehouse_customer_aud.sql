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
-- Table structure for table `customer_aud`
--

DROP TABLE IF EXISTS `customer_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_aud` (
  `id` bigint NOT NULL,
  `rev` int NOT NULL,
  `revtype` tinyint DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`rev`,`id`),
  CONSTRAINT `FKgfwtmwfqmkddg7g0pg36luq54` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_aud`
--

LOCK TABLES `customer_aud` WRITE;
/*!40000 ALTER TABLE `customer_aud` DISABLE KEYS */;
INSERT INTO `customer_aud` VALUES (3,2,1,'B2-10, Đường số 3','2023-02-21 17:29:01.453000','STFC-240222-SpaceGalaxyStars-GettyImages-1035676256.jpg','2023-04-11 09:13:50.413000'),(3,6,1,'B2-10, Đường số 3','2023-02-21 17:29:01.453000','1669737919_hi-tea-dao_2a001e9cc33442ebbb4e7829d3c2bba0.webp','2023-04-17 09:16:30.046000'),(10,21,0,'','2023-04-26 10:36:08.979000','user.png','2023-04-26 10:36:08.979000'),(11,22,0,'','2023-04-26 10:55:41.170000','user.png','2023-04-26 10:55:41.170000'),(11,23,1,'B123 - Đường số 4','2023-04-26 10:55:41.170000','user.png','2023-04-26 11:06:46.287000'),(11,24,1,'B123 - Đường số 4','2023-04-26 10:55:41.170000','1675355354_bg-tch-sua-da-no_7d616f5963074582a841dac3e3c6d89e_large.webp','2023-04-26 11:07:27.155000'),(3,28,1,'B2-10, Đường số 3','2023-02-21 17:29:01.453000','6461d9b12463d8671d356875','2023-05-15 14:43:59.717000'),(3,29,1,'B2-10, Đường số 3','2023-02-21 17:29:01.453000','6461d9b12463d8671d356875','2023-05-15 14:45:10.180000');
/*!40000 ALTER TABLE `customer_aud` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-05 11:36:58
