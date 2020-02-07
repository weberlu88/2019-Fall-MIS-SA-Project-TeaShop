-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sa_group10
-- ------------------------------------------------------
-- Server version	8.0.17

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
-- Table structure for table `tbladministrant`
--

DROP TABLE IF EXISTS `tbladministrant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbladministrant` (
  `idtblAdministrant` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Email` varchar(60) DEFAULT NULL,
  `Password` varchar(45) DEFAULT NULL,
  `FullName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idtblAdministrant`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbladministrant`
--

LOCK TABLES `tbladministrant` WRITE;
/*!40000 ALTER TABLE `tbladministrant` DISABLE KEYS */;
INSERT INTO `tbladministrant` VALUES (1,'bestsite@prxnhub.com','123456','ShopKeeper');
/*!40000 ALTER TABLE `tbladministrant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblmember`
--

DROP TABLE IF EXISTS `tblmember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblmember` (
  `idtblMember` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Email` varchar(60) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `FullName` varchar(45) NOT NULL,
  `Telephone` varchar(15) NOT NULL,
  `CreateDateTime` datetime DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`idtblMember`),
  UNIQUE KEY `Email_UNIQUE` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblmember`
--

LOCK TABLES `tblmember` WRITE;
/*!40000 ALTER TABLE `tblmember` DISABLE KEYS */;
INSERT INTO `tblmember` VALUES (1,'masako@gmail.com','123456','雅子','0987654321','2019-12-01 00:00:00',0),(2,'fork@gmail.com','123456','叉子','0918000000','2019-12-01 00:00:00',0),(3,'plate@prxnhub.com','123456','盤子','0912888777','2019-12-01 00:00:00',0),(5,'QWERTY@g.com','QWERTYUIOP','123456','0987609876','2019-12-16 15:47:18',0),(6,'test@mail.com','123456','testese','0900111222','2019-12-28 05:56:36',0),(7,'test@mail.h','123456','yyuutre','0800777666','2019-12-29 05:57:15',1),(8,'813@my.best','123456','世界上最危險的東西就是希望','0813813813','2020-12-12 00:00:00',1),(9,'123@gmail.com','456456','frank','0939549730','2020-01-03 08:54:07',0),(10,'aaaa@g.m','123456','aaaaaaa','0000000','2020-01-03 09:00:16',1);
/*!40000 ALTER TABLE `tblmember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblorder`
--

DROP TABLE IF EXISTS `tblorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblorder` (
  `idtblOrder` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `MemberId` int(10) unsigned NOT NULL,
  `TotalPrice` int(11) NOT NULL,
  `OrderStatus` tinyint(4) NOT NULL DEFAULT '0',
  `PaymentMethod` varchar(20) NOT NULL,
  `CreditCardNum` varchar(25) DEFAULT NULL,
  `Address` varchar(60) DEFAULT NULL,
  `OrderDateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`idtblOrder`),
  KEY `fk_tblOrder_tblMember_idx` (`MemberId`),
  CONSTRAINT `fk_tblOrder_tblMember` FOREIGN KEY (`MemberId`) REFERENCES `tblmember` (`idtblMember`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblorder`
--

LOCK TABLES `tblorder` WRITE;
/*!40000 ALTER TABLE `tblorder` DISABLE KEYS */;
INSERT INTO `tblorder` VALUES (15,1,970,1,'cash','到店支付','泱泱路69號','2019-12-17 09:46:36'),(20,1,160,1,'cash','到店支付','屏東縣鹽埔鄉','2019-12-18 15:29:39'),(38,10,90,1,'cash','到店支付','address','2020-01-03 09:04:21'),(39,9,60,0,'cash','到店支付','456','2020-01-03 13:46:32');
/*!40000 ALTER TABLE `tblorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblorderdetail`
--

DROP TABLE IF EXISTS `tblorderdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblorderdetail` (
  `idtblOrderDetail` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `OrderId` int(10) unsigned NOT NULL,
  `ProductId` int(10) unsigned NOT NULL,
  `Amount` smallint(6) NOT NULL,
  `Size` varchar(10) NOT NULL,
  `Sugar` varchar(10) DEFAULT '全糖',
  `Ice` varchar(10) DEFAULT '全冰',
  `Price` smallint(5) unsigned DEFAULT NULL,
  PRIMARY KEY (`idtblOrderDetail`),
  KEY `fk_tblOrderDetail_tblOrder1_idx` (`OrderId`),
  KEY `fk_tblOrderDetail_tblProduct1_idx` (`ProductId`),
  CONSTRAINT `fk_tblOrderDetail_tblOrder1` FOREIGN KEY (`OrderId`) REFERENCES `tblorder` (`idtblOrder`),
  CONSTRAINT `fk_tblOrderDetail_tblProduct1` FOREIGN KEY (`ProductId`) REFERENCES `tblproduct` (`idtblProduct`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblorderdetail`
--

LOCK TABLES `tblorderdetail` WRITE;
/*!40000 ALTER TABLE `tblorderdetail` DISABLE KEYS */;
INSERT INTO `tblorderdetail` VALUES (4,15,11,5,'large','半糖','微冰',325),(5,15,19,8,'large','半糖','去冰',400),(6,15,3,7,'middle','半糖','微冰',245),(17,20,3,4,'large','正常','正常',160),(35,38,16,2,'large','半糖','微冰',90),(36,39,15,1,'large','正常','正常',60);
/*!40000 ALTER TABLE `tblorderdetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblproduct`
--

DROP TABLE IF EXISTS `tblproduct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblproduct` (
  `idtblProduct` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ProductName` varchar(20) NOT NULL,
  `Category` varchar(20) DEFAULT NULL,
  `LargePrice` smallint(5) unsigned DEFAULT NULL,
  `SmallPrice` smallint(5) unsigned DEFAULT NULL,
  `Discription` varchar(40) DEFAULT NULL,
  `IsOffShelf` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`idtblProduct`),
  UNIQUE KEY `ProductName_UNIQUE` (`ProductName`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblproduct`
--

LOCK TABLES `tblproduct` WRITE;
/*!40000 ALTER TABLE `tblproduct` DISABLE KEYS */;
INSERT INTO `tblproduct` VALUES (1,'大正紅茶','想喝茶',35,15,'這是一杯紅茶',0),(2,'初露青茶','想喝茶',40,35,'這是一杯青茶',0),(3,'青採翠玉','想喝茶',40,35,'這看起來是綠茶',0),(4,'決明大麥','想喝茶',40,35,'這是一杯麥茶',0),(5,'茉香綠茶','想喝茶',30,25,'這才是綠茶?',0),(6,'高峰烏龍綠','想喝茶',35,30,'這是烏龍綠',0),(7,'伯爵紅茶','想喝茶',40,30,'喝起來有伯爵感的紅茶',0),(8,'伯爵紅茶拿鐵','找鮮乳',60,50,'伯爵紅茶+牛乳',0),(9,'大正紅茶拿鐵','找鮮乳',60,50,'大正紅茶+牛乳',0),(10,'布丁紅茶拿鐵','找鮮乳',65,55,'紅茶+布丁+牛乳\n',0),(11,'仙草凍茶拿鐵','找鮮乳',65,55,'仙草凍+紅茶+牛乳',0),(12,'紅豆紅茶拿鐵','找鮮乳',65,55,'紅豆+紅茶+牛乳',0),(13,'麥茶拿鐵','找鮮乳',50,45,'麥茶+牛乳',0),(14,'烏龍拿鐵','找鮮乳',50,45,'烏龍茶+牛乳',0),(15,'綠茶拿鐵','找鮮乳',60,50,'綠茶+牛乳',0),(16,'冬瓜檸檬','手作特調',45,35,'冬瓜+檸檬',0),(17,'冬瓜青茶','手作特調',45,35,'冬瓜+青茶',0),(18,'蜂農花蜜茶','手作特調',50,40,'蜂蜜花茶',0),(19,'冰糖洛神梅','手作特調',50,40,'洛神梅子+冰糖',0),(20,'甘蔗青茶','手作特調',40,30,'甘蔗+青茶',0),(21,'青檸香茶','手作特調',50,40,'檸檬+青茶',0),(22,'柳丁綠茶','手作特調',50,40,'柳丁+綠茶',0),(23,'冰萃檸檬','手作特調',55,45,'檸檬汁',0),(28,'測試用綠茶','想喝茶',50,45,'我只是測試用ㄉ飲料',0),(29,'好好喝茶','想喝茶',60,12,'真D好喝',0);
/*!40000 ALTER TABLE `tblproduct` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-06  4:33:56
