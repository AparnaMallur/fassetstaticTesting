-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 22, 2017 at 05:40 PM
-- Server version: 5.5.52-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `fassets`
--

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` tinyblob,
  `created_date` date DEFAULT NULL,
  `from_mobile` bit(1) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `updated_by` tinyblob,
  `updated_date` date DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`role_id`, `created_by`, `created_date`, `from_mobile`, `role_name`, `status`, `updated_by`, `updated_date`) VALUES
(1, NULL, NULL, NULL, 'Auditor', b'1', NULL, NULL),
(2, NULL, NULL, NULL, 'Administrator', b'1', NULL, NULL),
(3, NULL, NULL, NULL, 'Executive', b'1', NULL, NULL),
(4, NULL, NULL, NULL, 'Manager', b'1', NULL, NULL),
(5, NULL, NULL, NULL, 'Client Superuser', b'1', NULL, NULL),
(6, NULL, NULL, NULL, 'Employee', b'1', NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;