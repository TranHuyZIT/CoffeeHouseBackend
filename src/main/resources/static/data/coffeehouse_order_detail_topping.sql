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
-- Table structure for table `order_detail_topping`
--

DROP TABLE IF EXISTS `order_detail_topping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail_topping` (
  `od_id` bigint NOT NULL,
  `topping_id` bigint NOT NULL,
  PRIMARY KEY (`od_id`,`topping_id`),
  KEY `FKkg20wx3fo1b4ripgg0qkb5toh` (`topping_id`),
  CONSTRAINT `FKhf4d8kwbsx0gs8ponam2vyv9j` FOREIGN KEY (`od_id`) REFERENCES `order_detail` (`id`),
  CONSTRAINT `FKkg20wx3fo1b4ripgg0qkb5toh` FOREIGN KEY (`topping_id`) REFERENCES `topping` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail_topping`
--

LOCK TABLES `order_detail_topping` WRITE;
/*!40000 ALTER TABLE `order_detail_topping` DISABLE KEYS */;
INSERT INTO `order_detail_topping` VALUES (32,1),(34,1),(36,1),(39,1),(40,1),(41,1),(44,1),(47,1),(52,1),(53,1),(60,1),(76,1),(39,2),(19,3),(33,3),(35,3),(38,3),(39,3),(43,3),(46,3),(19,5),(33,5),(35,5),(38,5),(43,5),(46,5),(49,5),(50,5),(52,5),(53,5),(60,5),(63,5),(64,5),(19,7),(33,7),(35,7),(46,7),(49,7),(50,7),(72,7);
/*!40000 ALTER TABLE `order_detail_topping` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-05 11:36:49
