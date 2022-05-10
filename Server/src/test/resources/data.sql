-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: smart_ad_platform
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.21-MariaDB-1:10.4.21+maria~focal

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
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'All',NULL),(2,'Clothing',1),(3,'Sunglasses',2),(4,'Hats',2),(5,'Clothing tops',2),(6,'Clothing bottoms',2),(7,'Shoes',2),(8,'Arts',1),(9,'String music instruments',8),(10,'Electronics',1),(11,'Computers',10),(12,'Mobile electronics',10),(13,'Household items',1),(14,'Office items',13),(15,'Furniture',13),(16,'Outdoors',1),(17,'Sports',1),(18,'Automotive',1),(19,'Cars',18),(20,'Motorbikes',18),(21,'Automotive parts',18);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
INSERT INTO `images` VALUES (3,'Clothing bottoms','pants.jpg',2,1),(5,'Clothing','suit.jpg',4,2),(7,'Cars','176246975_787647935478198_8870609468318387752_n.jpg',5,1),(16,'Clothing tops','black-tshirt.jpg',3,1);
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (2,'+37061313123','Good pants','Brown pants',20.00,6,1),(3,'+370123123','Good tshirt','Black tshirt',15.00,5,1),(4,'+370123123','Flashy','An orange suit',100.00,2,2),(5,'+3706655646','Eurobeat intensifies','Subaru Impreza',3500.00,19,1);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_USER'),(2,'ROLE_ADMIN'),(8,'ROLE_MODERATOR');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1),(1,2),(2,1),(2,8),(18,1);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'luckyrads@gmail.com','$2a$10$cnjdaMVu89TSM6zE6jYfu.mat1wc/jj1U.sSKukIK6bzrbm4qJYfC','LuckyRads'),(2,'matas.malickas@stud.vilniustech.lt','$2a$10$WyYBGdQVOpaSe/M8H0wl..QKyVGtQiXez4Cy6oINSmLlvV9CnkYLS','MatasM'),(18,'fakeuser@vgtu.lt','$2a$10$VQm27AjYmC1ODoxhOOLnuu9kSq5oBFfqWcZ63WxoCEdfc4RP2H9dG','FakeUser123');
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

-- Dump completed on 2022-05-07 14:12:30
