-- Database Manager 4.2.5 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `abuse_post`;
CREATE TABLE `abuse_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `postID` int(11) NOT NULL,
  `reason` text NOT NULL,
  `userName` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `pet_id` int(10) NOT NULL,
  `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `steps` varchar(100) NOT NULL,
  `totalsteps` varchar(100) NOT NULL,
  `deviceid` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `addressfill`;
CREATE TABLE `addressfill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  `address` longtext NOT NULL,
  `zip` text NOT NULL,
  `landMark` longtext NOT NULL,
  `country` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(25) NOT NULL,
  `password` varchar(100) NOT NULL,
  `status` int(2) NOT NULL COMMENT '1. active 0. inactive',
  `created_on` varchar(50) NOT NULL,
  `updated_on` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `admin` (`id`, `name`, `email`, `password`, `status`, `created_on`, `updated_on`) VALUES
(1,	'Admin',	'admin@gmail.com',	'123456',	1,	'1519209394',	'1519209394');

DROP TABLE IF EXISTS `admin_notification`;
CREATE TABLE `admin_notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `timing` varchar(50) NOT NULL,
  `user_id` int(10) NOT NULL,
  `category` varchar(50) NOT NULL,
  `date_string` varchar(50) NOT NULL,
  `appointment_date` date NOT NULL,
  `appointment_timestamp` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL COMMENT '0=active, 3=complete, 4=delete',
  `repeat` varchar(50) NOT NULL,
  `advance` varchar(50) NOT NULL,
  `remark` varchar(50) NOT NULL,
  `pet_id` varchar(10) NOT NULL,
  `timezone` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `appointment` (`id`, `timing`, `user_id`, `category`, `date_string`, `appointment_date`, `appointment_timestamp`, `status`, `repeat`, `advance`, `remark`, `pet_id`, `timezone`) VALUES
(1,	'08:21 AM',	0,	'',	'',	'2019-11-28',	'0',	'0',	'',	'',	'',	'',	'');

DROP TABLE IF EXISTS `attribute`;
CREATE TABLE `attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attribute` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `auth_key`;
CREATE TABLE `auth_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `auth_key` (`id`, `auth_key`) VALUES
(1,	'205521Ay0uGpRMiR5da996d7');

DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `b_image` varchar(255) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `banner` (`id`, `b_image`, `status`) VALUES
(61,	'assets/images/category/1566736172.jpg',	1),
(65,	'assets/images/category/1574241748.jpg',	1),
(66,	'assets/images/category/1574845953.jpg',	1),
(67,	'assets/images/category/1574845963.jpg',	1);

DROP TABLE IF EXISTS `breed`;
CREATE TABLE `breed` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `breed_name` varchar(50) NOT NULL,
  `remark` varchar(50) NOT NULL,
  `active_status` varchar(50) NOT NULL,
  `origin` varchar(50) NOT NULL,
  `life_span` varchar(50) NOT NULL,
  `weight_male` varchar(50) NOT NULL,
  `weight_female` varchar(50) NOT NULL,
  `height_male` varchar(50) NOT NULL,
  `height_female` varchar(50) NOT NULL,
  `temperament` varchar(50) NOT NULL,
  `image_path` varchar(50) NOT NULL,
  `target` varchar(50) NOT NULL,
  `manual_activity` varchar(50) NOT NULL,
  `breed_cat` varchar(50) NOT NULL,
  `pet_type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `breed` (`id`, `breed_name`, `remark`, `active_status`, `origin`, `life_span`, `weight_male`, `weight_female`, `height_male`, `height_female`, `temperament`, `image_path`, `target`, `manual_activity`, `breed_cat`, `pet_type`) VALUES
(1,	'Golden Retriever',	'All done',	'True',	'Scotland, England, U.K.',	'10–12 Years',	'25–40 KG',	'20–35 KG',	'56–61 CM',	'51–56 CM',	'Reliable, Friendly, Kind, Confident, Trustworthy, ',	'assets/images/breeds/1560798309.jpg',	'9504',	'120',	'large',	1),
(2,	'Pomeranian',	'All done',	'True',	'Poland',	'08–12 Years',	'1.9–3.5 KG',	'1.9–3.5 KG',	'20–20 CM',	'20–20 CM',	'Playful, Friendly, Extroverted, Active, Intelligen',	'assets/images/breeds/1564333863.jpg',	'2375',	'30',	'small',	1),
(3,	'Afghan Hound',	'All done',	'True',	'Afghanistan, Iran, Pakistan',	'12–14 Years',	'26–34 KG',	'26–34 KG',	'68–74 CM',	'60–69 CM',	'Aloof, Clownish, Happy, Dignified, Independent',	'assets/images/breeds/1557727983.jpg',	'9500',	'120',	'large',	1),
(5,	'Airedale Terrier',	'All done',	'True',	'England',	'10–12 Years',	'23–29 KG',	'18–20 KG',	'58–61 CM',	'56–59 CM',	'Outgoing, Alert, Friendly, Confident, Courageous, ',	'assets/images/breeds/1557728102.jpg',	'4750',	'60',	'medium',	1),
(6,	'Akita',	'All done',	'True',	'Japan',	'15– Years',	'45–59 KG',	'32–45 KG',	'66–71 CM',	'61–66 CM',	'Alert, Docile, Friendly, Responsive, Courageous, D',	'assets/images/breeds/1557728139.jpg',	'9500',	'120',	'large',	1),
(7,	'Anatolian Shepherd Dog',	'All done',	'True',	'Turkey',	'13–15 Years',	'50–65 KG',	'40–55 KG',	'74–81 CM',	'71–79 CM',	'Bold, Steady, Confident, Proud, Intelligent, Indep',	'assets/images/breeds/1557728227.jpeg',	'9500',	'120',	'large',	1),
(8,	'Australian Cattle Dog',	'All done',	'True',	'Australia',	'13–15 Years',	'14–16 KG',	'15–16 KG',	'43–48 CM',	'46–51 CM',	'Protective, Cautious, Obedient, Loyal, Energetic, ',	'assets/images/breeds/1557728314.jpeg',	'9500',	'120',	'medium',	1),
(9,	'Australian Shepherd',	'All done',	'True',	'United States',	'13–15 Years',	'25–32 KG',	'16–25 KG',	'51–58 CM',	'46–54 CM',	'Protective, Good-natured, Active, Intelligent, Aff',	'assets/images/breeds/1557728338.jpeg',	'9500',	'120',	'medium',	1),
(10,	'Australian Silky Terrier',	'All done',	'True',	'Australia',	'12–15 Years',	'3–5 KG',	'3–5 KG',	'23–26 CM',	'23–26 CM',	'Inquisitive, Alert, Quick, Friendly, Responsive, J',	'assets/images/breeds/1557728422.jpeg',	'2375',	'30',	'small',	1),
(11,	'Australian Terrier',	'All done',	'True',	'Australia',	'12–15 Years',	'6.4–7.3 KG',	'5.4–6.4 KG',	'23–28 CM',	'23–28 CM',	'Spirited, Even Tempered, Alert, Loyal, Companionab',	'assets/images/breeds/1557728476.jpeg',	'4750',	'60',	'small',	1);

DROP TABLE IF EXISTS `breed_info`;
CREATE TABLE `breed_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `breed_name` varchar(50) NOT NULL,
  `b_description` text NOT NULL,
  `image` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `breed_info` (`id`, `breed_name`, `b_description`, `image`) VALUES
(1,	'Bichon Frise',	'The Bichon Frise (pronounced BEE-shawn FREE-say; the plural is Bichons Frises) is a cheerful, small dog breed with a love of mischief and a lot of love to give. With his black eyes and fluffy white coat, the Bichon looks almost like a child’s toy. And it doesn’t take long to realize that the Bichon can be your happiest and most enthusiastic companion.',	'assets/images/category/1574775259.png');

DROP TABLE IF EXISTS `card`;
CREATE TABLE `card` (
  `card_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


SET NAMES utf8mb4;

DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) NOT NULL,
  `user_id_receiver` varchar(500) NOT NULL,
  `message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_name` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `date` varchar(50) NOT NULL,
  `media` text NOT NULL,
  `chat_type` varchar(50) NOT NULL DEFAULT '1' COMMENT '1. Text 2. Image',
  `thumb` varchar(255) NOT NULL,
  `latitude` varchar(50) NOT NULL DEFAULT '0',
  `longitude` varchar(50) NOT NULL DEFAULT '0',
  `chat_state` varchar(5) NOT NULL DEFAULT '0' COMMENT '0 sent , 1 delivered, 2 read',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `commentID` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `postID` int(11) NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `createAt` int(11) NOT NULL,
  `flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`commentID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `comments` (`commentID`, `user_id`, `postID`, `content`, `createAt`, `flag`) VALUES
(2,	3,	2,	'S the best of luck to',	1574926647,	1),
(9,	3,	3,	'Hyyyu',	1574926948,	1),
(10,	2,	3,	'Hello',	1574927053,	1),
(11,	2,	3,	'Hello Testing\r\n',	1574929792,	1),
(12,	2,	3,	'My Comment',	1574929825,	1);

DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `user_id_owner` int(11) NOT NULL,
  `description` text NOT NULL,
  `status` varchar(100) NOT NULL,
  `created` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `contact_info`;
CREATE TABLE `contact_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `mobile_no` varchar(15) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `contact_info` (`id`, `name`, `mobile_no`, `email`) VALUES
(1,	'Amit Sharma',	'7869999639',	'samyotechindore@gmail.com');

DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `iso` char(2) NOT NULL,
  `name` varchar(80) NOT NULL,
  `nicename` varchar(80) NOT NULL,
  `iso3` char(3) DEFAULT NULL,
  `numcode` smallint(6) DEFAULT NULL,
  `phonecode` int(5) NOT NULL,
  `status` int(5) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO `country` (`id`, `iso`, `name`, `nicename`, `iso3`, `numcode`, `phonecode`, `status`) VALUES
(1,	'AF',	'AFGHANISTAN',	'Afghanistan',	'AFG',	4,	93,	0),
(2,	'AL',	'ALBANIA',	'Albania',	'ALB',	8,	355,	0),
(3,	'DZ',	'ALGERIA',	'Algeria',	'DZA',	12,	213,	0),
(4,	'AS',	'AMERICAN SAMOA',	'American Samoa',	'ASM',	16,	1684,	0),
(5,	'AD',	'ANDORRA',	'Andorra',	'AND',	20,	376,	0),
(6,	'AO',	'ANGOLA',	'Angola',	'AGO',	24,	244,	0),
(7,	'AI',	'ANGUILLA',	'Anguilla',	'AIA',	660,	1264,	0),
(8,	'AQ',	'ANTARCTICA',	'Antarctica',	NULL,	NULL,	0,	0),
(9,	'AG',	'ANTIGUA AND BARBUDA',	'Antigua and Barbuda',	'ATG',	28,	1268,	0),
(10,	'AR',	'ARGENTINA',	'Argentina',	'ARG',	32,	54,	0),
(11,	'AM',	'ARMENIA',	'Armenia',	'ARM',	51,	374,	0),
(12,	'AW',	'ARUBA',	'Aruba',	'ABW',	533,	297,	0),
(13,	'AU',	'AUSTRALIA',	'Australia',	'AUS',	36,	61,	0),
(14,	'AT',	'AUSTRIA',	'Austria',	'AUT',	40,	43,	0),
(15,	'AZ',	'AZERBAIJAN',	'Azerbaijan',	'AZE',	31,	994,	0),
(16,	'BS',	'BAHAMAS',	'Bahamas',	'BHS',	44,	1242,	0),
(17,	'BH',	'BAHRAIN',	'Bahrain',	'BHR',	48,	973,	0),
(18,	'BD',	'BANGLADESH',	'Bangladesh',	'BGD',	50,	880,	0),
(19,	'BB',	'BARBADOS',	'Barbados',	'BRB',	52,	1246,	0),
(20,	'BY',	'BELARUS',	'Belarus',	'BLR',	112,	375,	0),
(21,	'BE',	'BELGIUM',	'Belgium',	'BEL',	56,	32,	0),
(22,	'BZ',	'BELIZE',	'Belize',	'BLZ',	84,	501,	0),
(23,	'BJ',	'BENIN',	'Benin',	'BEN',	204,	229,	0),
(24,	'BM',	'BERMUDA',	'Bermuda',	'BMU',	60,	1441,	0),
(25,	'BT',	'BHUTAN',	'Bhutan',	'BTN',	64,	975,	0),
(26,	'BO',	'BOLIVIA',	'Bolivia',	'BOL',	68,	591,	0),
(27,	'BA',	'BOSNIA AND HERZEGOVINA',	'Bosnia and Herzegovina',	'BIH',	70,	387,	0),
(28,	'BW',	'BOTSWANA',	'Botswana',	'BWA',	72,	267,	0),
(29,	'BV',	'BOUVET ISLAND',	'Bouvet Island',	NULL,	NULL,	0,	0),
(30,	'BR',	'BRAZIL',	'Brazil',	'BRA',	76,	55,	0),
(31,	'IO',	'BRITISH INDIAN OCEAN TERRITORY',	'British Indian Ocean Territory',	NULL,	NULL,	246,	0),
(32,	'BN',	'BRUNEI DARUSSALAM',	'Brunei Darussalam',	'BRN',	96,	673,	0),
(33,	'BG',	'BULGARIA',	'Bulgaria',	'BGR',	100,	359,	0),
(34,	'BF',	'BURKINA FASO',	'Burkina Faso',	'BFA',	854,	226,	0),
(35,	'BI',	'BURUNDI',	'Burundi',	'BDI',	108,	257,	0),
(36,	'KH',	'CAMBODIA',	'Cambodia',	'KHM',	116,	855,	0),
(37,	'CM',	'CAMEROON',	'Cameroon',	'CMR',	120,	237,	0),
(38,	'CA',	'CANADA',	'Canada',	'CAN',	124,	1,	0),
(39,	'CV',	'CAPE VERDE',	'Cape Verde',	'CPV',	132,	238,	0),
(40,	'KY',	'CAYMAN ISLANDS',	'Cayman Islands',	'CYM',	136,	1345,	0),
(41,	'CF',	'CENTRAL AFRICAN REPUBLIC',	'Central African Republic',	'CAF',	140,	236,	0),
(42,	'TD',	'CHAD',	'Chad',	'TCD',	148,	235,	0),
(43,	'CL',	'CHILE',	'Chile',	'CHL',	152,	56,	0),
(44,	'CN',	'CHINA',	'China',	'CHN',	156,	86,	0),
(45,	'CX',	'CHRISTMAS ISLAND',	'Christmas Island',	NULL,	NULL,	61,	0),
(46,	'CC',	'COCOS (KEELING) ISLANDS',	'Cocos (Keeling) Islands',	NULL,	NULL,	672,	0),
(47,	'CO',	'COLOMBIA',	'Colombia',	'COL',	170,	57,	0),
(48,	'KM',	'COMOROS',	'Comoros',	'COM',	174,	269,	0),
(49,	'CG',	'CONGO',	'Congo',	'COG',	178,	242,	0),
(50,	'CD',	'CONGO, THE DEMOCRATIC REPUBLIC OF THE',	'Congo, the Democratic Republic of the',	'COD',	180,	242,	0),
(51,	'CK',	'COOK ISLANDS',	'Cook Islands',	'COK',	184,	682,	0),
(52,	'CR',	'COSTA RICA',	'Costa Rica',	'CRI',	188,	506,	0),
(53,	'CI',	'COTE D\'IVOIRE',	'Cote D\'Ivoire',	'CIV',	384,	225,	0),
(54,	'HR',	'CROATIA',	'Croatia',	'HRV',	191,	385,	0),
(55,	'CU',	'CUBA',	'Cuba',	'CUB',	192,	53,	0),
(56,	'CY',	'CYPRUS',	'Cyprus',	'CYP',	196,	357,	0),
(57,	'CZ',	'CZECH REPUBLIC',	'Czech Republic',	'CZE',	203,	420,	0),
(58,	'DK',	'DENMARK',	'Denmark',	'DNK',	208,	45,	0),
(59,	'DJ',	'DJIBOUTI',	'Djibouti',	'DJI',	262,	253,	0),
(60,	'DM',	'DOMINICA',	'Dominica',	'DMA',	212,	1767,	0),
(61,	'DO',	'DOMINICAN REPUBLIC',	'Dominican Republic',	'DOM',	214,	1809,	0),
(62,	'EC',	'ECUADOR',	'Ecuador',	'ECU',	218,	593,	0),
(63,	'EG',	'EGYPT',	'Egypt',	'EGY',	818,	20,	0),
(64,	'SV',	'EL SALVADOR',	'El Salvador',	'SLV',	222,	503,	0),
(65,	'GQ',	'EQUATORIAL GUINEA',	'Equatorial Guinea',	'GNQ',	226,	240,	0),
(66,	'ER',	'ERITREA',	'Eritrea',	'ERI',	232,	291,	0),
(67,	'EE',	'ESTONIA',	'Estonia',	'EST',	233,	372,	0),
(68,	'ET',	'ETHIOPIA',	'Ethiopia',	'ETH',	231,	251,	0),
(69,	'FK',	'FALKLAND ISLANDS (MALVINAS)',	'Falkland Islands (Malvinas)',	'FLK',	238,	500,	0),
(70,	'FO',	'FAROE ISLANDS',	'Faroe Islands',	'FRO',	234,	298,	0),
(71,	'FJ',	'FIJI',	'Fiji',	'FJI',	242,	679,	0),
(72,	'FI',	'FINLAND',	'Finland',	'FIN',	246,	358,	0),
(73,	'FR',	'FRANCE',	'France',	'FRA',	250,	33,	0),
(74,	'GF',	'FRENCH GUIANA',	'French Guiana',	'GUF',	254,	594,	0),
(75,	'PF',	'FRENCH POLYNESIA',	'French Polynesia',	'PYF',	258,	689,	0),
(76,	'TF',	'FRENCH SOUTHERN TERRITORIES',	'French Southern Territories',	NULL,	NULL,	0,	0),
(77,	'GA',	'GABON',	'Gabon',	'GAB',	266,	241,	0),
(78,	'GM',	'GAMBIA',	'Gambia',	'GMB',	270,	220,	0),
(79,	'GE',	'GEORGIA',	'Georgia',	'GEO',	268,	995,	0),
(80,	'DE',	'GERMANY',	'Germany',	'DEU',	276,	49,	0),
(81,	'GH',	'GHANA',	'Ghana',	'GHA',	288,	233,	0),
(82,	'GI',	'GIBRALTAR',	'Gibraltar',	'GIB',	292,	350,	0),
(83,	'GR',	'GREECE',	'Greece',	'GRC',	300,	30,	0),
(84,	'GL',	'GREENLAND',	'Greenland',	'GRL',	304,	299,	0),
(85,	'GD',	'GRENADA',	'Grenada',	'GRD',	308,	1473,	0),
(86,	'GP',	'GUADELOUPE',	'Guadeloupe',	'GLP',	312,	590,	0),
(87,	'GU',	'GUAM',	'Guam',	'GUM',	316,	1671,	0),
(88,	'GT',	'GUATEMALA',	'Guatemala',	'GTM',	320,	502,	0),
(89,	'GN',	'GUINEA',	'Guinea',	'GIN',	324,	224,	0),
(90,	'GW',	'GUINEA-BISSAU',	'Guinea-Bissau',	'GNB',	624,	245,	0),
(91,	'GY',	'GUYANA',	'Guyana',	'GUY',	328,	592,	0),
(92,	'HT',	'HAITI',	'Haiti',	'HTI',	332,	509,	0),
(93,	'HM',	'HEARD ISLAND AND MCDONALD ISLANDS',	'Heard Island and Mcdonald Islands',	NULL,	NULL,	0,	0),
(94,	'VA',	'HOLY SEE (VATICAN CITY STATE)',	'Holy See (Vatican City State)',	'VAT',	336,	39,	0),
(95,	'HN',	'HONDURAS',	'Honduras',	'HND',	340,	504,	0),
(96,	'HK',	'HONG KONG',	'Hong Kong',	'HKG',	344,	852,	0),
(97,	'HU',	'HUNGARY',	'Hungary',	'HUN',	348,	36,	0),
(98,	'IS',	'ICELAND',	'Iceland',	'ISL',	352,	354,	0),
(99,	'IN',	'INDIA',	'India',	'IND',	356,	91,	1),
(100,	'ID',	'INDONESIA',	'Indonesia',	'IDN',	360,	62,	0),
(101,	'IR',	'IRAN, ISLAMIC REPUBLIC OF',	'Iran, Islamic Republic of',	'IRN',	364,	98,	0),
(102,	'IQ',	'IRAQ',	'Iraq',	'IRQ',	368,	964,	0),
(103,	'IE',	'IRELAND',	'Ireland',	'IRL',	372,	353,	0),
(104,	'IL',	'ISRAEL',	'Israel',	'ISR',	376,	972,	0),
(105,	'IT',	'ITALY',	'Italy',	'ITA',	380,	39,	0),
(106,	'JM',	'JAMAICA',	'Jamaica',	'JAM',	388,	1876,	0),
(107,	'JP',	'JAPAN',	'Japan',	'JPN',	392,	81,	0),
(108,	'JO',	'JORDAN',	'Jordan',	'JOR',	400,	962,	0),
(109,	'KZ',	'KAZAKHSTAN',	'Kazakhstan',	'KAZ',	398,	7,	0),
(110,	'KE',	'KENYA',	'Kenya',	'KEN',	404,	254,	0),
(111,	'KI',	'KIRIBATI',	'Kiribati',	'KIR',	296,	686,	0),
(112,	'KP',	'KOREA, DEMOCRATIC PEOPLE\'S REPUBLIC OF',	'Korea, Democratic People\'s Republic of',	'PRK',	408,	850,	0),
(113,	'KR',	'KOREA, REPUBLIC OF',	'Korea, Republic of',	'KOR',	410,	82,	0),
(114,	'KW',	'KUWAIT',	'Kuwait',	'KWT',	414,	965,	0),
(115,	'KG',	'KYRGYZSTAN',	'Kyrgyzstan',	'KGZ',	417,	996,	0),
(116,	'LA',	'LAO PEOPLE\'S DEMOCRATIC REPUBLIC',	'Lao People\'s Democratic Republic',	'LAO',	418,	856,	0),
(117,	'LV',	'LATVIA',	'Latvia',	'LVA',	428,	371,	0),
(118,	'LB',	'LEBANON',	'Lebanon',	'LBN',	422,	961,	0),
(119,	'LS',	'LESOTHO',	'Lesotho',	'LSO',	426,	266,	0),
(120,	'LR',	'LIBERIA',	'Liberia',	'LBR',	430,	231,	0),
(121,	'LY',	'LIBYAN ARAB JAMAHIRIYA',	'Libyan Arab Jamahiriya',	'LBY',	434,	218,	0),
(122,	'LI',	'LIECHTENSTEIN',	'Liechtenstein',	'LIE',	438,	423,	0),
(123,	'LT',	'LITHUANIA',	'Lithuania',	'LTU',	440,	370,	0),
(124,	'LU',	'LUXEMBOURG',	'Luxembourg',	'LUX',	442,	352,	0),
(125,	'MO',	'MACAO',	'Macao',	'MAC',	446,	853,	0),
(126,	'MK',	'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF',	'Macedonia, the Former Yugoslav Republic of',	'MKD',	807,	389,	0),
(127,	'MG',	'MADAGASCAR',	'Madagascar',	'MDG',	450,	261,	0),
(128,	'MW',	'MALAWI',	'Malawi',	'MWI',	454,	265,	0),
(129,	'MY',	'MALAYSIA',	'Malaysia',	'MYS',	458,	60,	0),
(130,	'MV',	'MALDIVES',	'Maldives',	'MDV',	462,	960,	0),
(131,	'ML',	'MALI',	'Mali',	'MLI',	466,	223,	0),
(132,	'MT',	'MALTA',	'Malta',	'MLT',	470,	356,	0),
(133,	'MH',	'MARSHALL ISLANDS',	'Marshall Islands',	'MHL',	584,	692,	0),
(134,	'MQ',	'MARTINIQUE',	'Martinique',	'MTQ',	474,	596,	0),
(135,	'MR',	'MAURITANIA',	'Mauritania',	'MRT',	478,	222,	0),
(136,	'MU',	'MAURITIUS',	'Mauritius',	'MUS',	480,	230,	0),
(137,	'YT',	'MAYOTTE',	'Mayotte',	NULL,	NULL,	269,	0),
(138,	'MX',	'MEXICO',	'Mexico',	'MEX',	484,	52,	0),
(139,	'FM',	'MICRONESIA, FEDERATED STATES OF',	'Micronesia, Federated States of',	'FSM',	583,	691,	0),
(140,	'MD',	'MOLDOVA, REPUBLIC OF',	'Moldova, Republic of',	'MDA',	498,	373,	0),
(141,	'MC',	'MONACO',	'Monaco',	'MCO',	492,	377,	0),
(142,	'MN',	'MONGOLIA',	'Mongolia',	'MNG',	496,	976,	0),
(143,	'MS',	'MONTSERRAT',	'Montserrat',	'MSR',	500,	1664,	0),
(144,	'MA',	'MOROCCO',	'Morocco',	'MAR',	504,	212,	0),
(145,	'MZ',	'MOZAMBIQUE',	'Mozambique',	'MOZ',	508,	258,	0),
(146,	'MM',	'MYANMAR',	'Myanmar',	'MMR',	104,	95,	0),
(147,	'NA',	'NAMIBIA',	'Namibia',	'NAM',	516,	264,	0),
(148,	'NR',	'NAURU',	'Nauru',	'NRU',	520,	674,	0),
(149,	'NP',	'NEPAL',	'Nepal',	'NPL',	524,	977,	0),
(150,	'NL',	'NETHERLANDS',	'Netherlands',	'NLD',	528,	31,	0),
(151,	'AN',	'NETHERLANDS ANTILLES',	'Netherlands Antilles',	'ANT',	530,	599,	0),
(152,	'NC',	'NEW CALEDONIA',	'New Caledonia',	'NCL',	540,	687,	0),
(153,	'NZ',	'NEW ZEALAND',	'New Zealand',	'NZL',	554,	64,	0),
(154,	'NI',	'NICARAGUA',	'Nicaragua',	'NIC',	558,	505,	0),
(155,	'NE',	'NIGER',	'Niger',	'NER',	562,	227,	0),
(156,	'NG',	'NIGERIA',	'Nigeria',	'NGA',	566,	234,	0),
(157,	'NU',	'NIUE',	'Niue',	'NIU',	570,	683,	0),
(158,	'NF',	'NORFOLK ISLAND',	'Norfolk Island',	'NFK',	574,	672,	0),
(159,	'MP',	'NORTHERN MARIANA ISLANDS',	'Northern Mariana Islands',	'MNP',	580,	1670,	0),
(160,	'NO',	'NORWAY',	'Norway',	'NOR',	578,	47,	0),
(161,	'OM',	'OMAN',	'Oman',	'OMN',	512,	968,	0),
(162,	'PK',	'PAKISTAN',	'Pakistan',	'PAK',	586,	92,	0),
(163,	'PW',	'PALAU',	'Palau',	'PLW',	585,	680,	0),
(164,	'PS',	'PALESTINIAN TERRITORY, OCCUPIED',	'Palestinian Territory, Occupied',	NULL,	NULL,	970,	0),
(165,	'PA',	'PANAMA',	'Panama',	'PAN',	591,	507,	0),
(166,	'PG',	'PAPUA NEW GUINEA',	'Papua New Guinea',	'PNG',	598,	675,	0),
(167,	'PY',	'PARAGUAY',	'Paraguay',	'PRY',	600,	595,	0),
(168,	'PE',	'PERU',	'Peru',	'PER',	604,	51,	0),
(169,	'PH',	'PHILIPPINES',	'Philippines',	'PHL',	608,	63,	0),
(170,	'PN',	'PITCAIRN',	'Pitcairn',	'PCN',	612,	0,	0),
(171,	'PL',	'POLAND',	'Poland',	'POL',	616,	48,	0),
(172,	'PT',	'PORTUGAL',	'Portugal',	'PRT',	620,	351,	0),
(173,	'PR',	'PUERTO RICO',	'Puerto Rico',	'PRI',	630,	1787,	0),
(174,	'QA',	'QATAR',	'Qatar',	'QAT',	634,	974,	0),
(175,	'RE',	'REUNION',	'Reunion',	'REU',	638,	262,	0),
(176,	'RO',	'ROMANIA',	'Romania',	'ROM',	642,	40,	0),
(177,	'RU',	'RUSSIAN FEDERATION',	'Russian Federation',	'RUS',	643,	70,	0),
(178,	'RW',	'RWANDA',	'Rwanda',	'RWA',	646,	250,	0),
(179,	'SH',	'SAINT HELENA',	'Saint Helena',	'SHN',	654,	290,	0),
(180,	'KN',	'SAINT KITTS AND NEVIS',	'Saint Kitts and Nevis',	'KNA',	659,	1869,	0),
(181,	'LC',	'SAINT LUCIA',	'Saint Lucia',	'LCA',	662,	1758,	0),
(182,	'PM',	'SAINT PIERRE AND MIQUELON',	'Saint Pierre and Miquelon',	'SPM',	666,	508,	0),
(183,	'VC',	'SAINT VINCENT AND THE GRENADINES',	'Saint Vincent and the Grenadines',	'VCT',	670,	1784,	0),
(184,	'WS',	'SAMOA',	'Samoa',	'WSM',	882,	684,	0),
(185,	'SM',	'SAN MARINO',	'San Marino',	'SMR',	674,	378,	0),
(186,	'ST',	'SAO TOME AND PRINCIPE',	'Sao Tome and Principe',	'STP',	678,	239,	0),
(187,	'SA',	'SAUDI ARABIA',	'Saudi Arabia',	'SAU',	682,	966,	0),
(188,	'SN',	'SENEGAL',	'Senegal',	'SEN',	686,	221,	0),
(189,	'CS',	'SERBIA AND MONTENEGRO',	'Serbia and Montenegro',	NULL,	NULL,	381,	0),
(190,	'SC',	'SEYCHELLES',	'Seychelles',	'SYC',	690,	248,	0),
(191,	'SL',	'SIERRA LEONE',	'Sierra Leone',	'SLE',	694,	232,	0),
(192,	'SG',	'SINGAPORE',	'Singapore',	'SGP',	702,	65,	0),
(193,	'SK',	'SLOVAKIA',	'Slovakia',	'SVK',	703,	421,	0),
(194,	'SI',	'SLOVENIA',	'Slovenia',	'SVN',	705,	386,	0),
(195,	'SB',	'SOLOMON ISLANDS',	'Solomon Islands',	'SLB',	90,	677,	0),
(196,	'SO',	'SOMALIA',	'Somalia',	'SOM',	706,	252,	0),
(197,	'ZA',	'SOUTH AFRICA',	'South Africa',	'ZAF',	710,	27,	0),
(198,	'GS',	'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS',	'South Georgia and the South Sandwich Islands',	NULL,	NULL,	0,	0),
(199,	'ES',	'SPAIN',	'Spain',	'ESP',	724,	34,	0),
(200,	'LK',	'SRI LANKA',	'Sri Lanka',	'LKA',	144,	94,	0),
(201,	'SD',	'SUDAN',	'Sudan',	'SDN',	736,	249,	0),
(202,	'SR',	'SURINAME',	'Suriname',	'SUR',	740,	597,	0),
(203,	'SJ',	'SVALBARD AND JAN MAYEN',	'Svalbard and Jan Mayen',	'SJM',	744,	47,	0),
(204,	'SZ',	'SWAZILAND',	'Swaziland',	'SWZ',	748,	268,	0),
(205,	'SE',	'SWEDEN',	'Sweden',	'SWE',	752,	46,	0),
(206,	'CH',	'SWITZERLAND',	'Switzerland',	'CHE',	756,	41,	0),
(207,	'SY',	'SYRIAN ARAB REPUBLIC',	'Syrian Arab Republic',	'SYR',	760,	963,	0),
(208,	'TW',	'TAIWAN, PROVINCE OF CHINA',	'Taiwan, Province of China',	'TWN',	158,	886,	0),
(209,	'TJ',	'TAJIKISTAN',	'Tajikistan',	'TJK',	762,	992,	0),
(210,	'TZ',	'TANZANIA, UNITED REPUBLIC OF',	'Tanzania, United Republic of',	'TZA',	834,	255,	0),
(211,	'TH',	'THAILAND',	'Thailand',	'THA',	764,	66,	0),
(212,	'TL',	'TIMOR-LESTE',	'Timor-Leste',	NULL,	NULL,	670,	0),
(213,	'TG',	'TOGO',	'Togo',	'TGO',	768,	228,	0),
(214,	'TK',	'TOKELAU',	'Tokelau',	'TKL',	772,	690,	0),
(215,	'TO',	'TONGA',	'Tonga',	'TON',	776,	676,	0),
(216,	'TT',	'TRINIDAD AND TOBAGO',	'Trinidad and Tobago',	'TTO',	780,	1868,	0),
(217,	'TN',	'TUNISIA',	'Tunisia',	'TUN',	788,	216,	0),
(218,	'TR',	'TURKEY',	'Turkey',	'TUR',	792,	90,	0),
(219,	'TM',	'TURKMENISTAN',	'Turkmenistan',	'TKM',	795,	7370,	0),
(220,	'TC',	'TURKS AND CAICOS ISLANDS',	'Turks and Caicos Islands',	'TCA',	796,	1649,	0),
(221,	'TV',	'TUVALU',	'Tuvalu',	'TUV',	798,	688,	0),
(222,	'UG',	'UGANDA',	'Uganda',	'UGA',	800,	256,	0),
(223,	'UA',	'UKRAINE',	'Ukraine',	'UKR',	804,	380,	0),
(224,	'AE',	'UNITED ARAB EMIRATES',	'United Arab Emirates',	'ARE',	784,	971,	0),
(225,	'GB',	'UNITED KINGDOM',	'United Kingdom',	'GBR',	826,	44,	0),
(226,	'US',	'UNITED STATES',	'United States',	'USA',	840,	1,	0),
(227,	'UM',	'UNITED STATES MINOR OUTLYING ISLANDS',	'United States Minor Outlying Islands',	NULL,	NULL,	1,	0),
(228,	'UY',	'URUGUAY',	'Uruguay',	'URY',	858,	598,	0),
(229,	'UZ',	'UZBEKISTAN',	'Uzbekistan',	'UZB',	860,	998,	0),
(230,	'VU',	'VANUATU',	'Vanuatu',	'VUT',	548,	678,	0),
(231,	'VE',	'VENEZUELA',	'Venezuela',	'VEN',	862,	58,	0),
(232,	'VN',	'VIET NAM',	'Viet Nam',	'VNM',	704,	84,	0),
(233,	'VG',	'VIRGIN ISLANDS, BRITISH',	'Virgin Islands, British',	'VGB',	92,	1284,	0),
(234,	'VI',	'VIRGIN ISLANDS, U.S.',	'Virgin Islands, U.s.',	'VIR',	850,	1340,	0),
(235,	'WF',	'WALLIS AND FUTUNA',	'Wallis and Futuna',	'WLF',	876,	681,	0),
(236,	'EH',	'WESTERN SAHARA',	'Western Sahara',	'ESH',	732,	212,	0),
(237,	'YE',	'YEMEN',	'Yemen',	'YEM',	887,	967,	0),
(238,	'ZM',	'ZAMBIA',	'Zambia',	'ZMB',	894,	260,	0),
(239,	'ZW',	'ZIMBABWE',	'Zimbabwe',	'ZWE',	716,	263,	0);

DROP TABLE IF EXISTS `cronJob`;
CREATE TABLE `cronJob` (
  `cronID` int(10) NOT NULL AUTO_INCREMENT,
  `ntfID` int(10) NOT NULL,
  `trueTime` varchar(250) NOT NULL,
  `manTime` varchar(250) NOT NULL,
  `timeZone` varchar(250) NOT NULL,
  `dateToday` date NOT NULL,
  `done` int(10) NOT NULL,
  PRIMARY KEY (`cronID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `cron_config`;
CREATE TABLE `cron_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `start_from` varchar(250) NOT NULL,
  `interval` int(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `currency_setting`;
CREATE TABLE `currency_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `currency_symbol` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `currency_name` varchar(150) NOT NULL,
  `code` varchar(150) NOT NULL,
  `status` varchar(150) NOT NULL DEFAULT '0' COMMENT '1. current Active 0. Default',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `currency_setting` (`id`, `currency_symbol`, `currency_name`, `code`, `status`) VALUES
(1,	'₹',	'INR',	'INR',	'1');

DROP TABLE IF EXISTS `current_body_score`;
CREATE TABLE `current_body_score` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `petid` varchar(250) NOT NULL,
  `email` varchar(250) NOT NULL,
  `score` varchar(250) NOT NULL,
  `date` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `event_name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `latitude` varchar(255) NOT NULL,
  `longitude` varchar(255) NOT NULL,
  `event_date` varchar(255) NOT NULL,
  `event_time` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `event_desc` varchar(255) NOT NULL,
  `pet_type` int(11) NOT NULL,
  `deleted` int(11) NOT NULL DEFAULT '1',
  `created_at` varchar(255) NOT NULL,
  `updated_on` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `firebase_keys`;
CREATE TABLE `firebase_keys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firebase_keys` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `firebase_keys` (`id`, `firebase_keys`) VALUES
(1,	'AAAAXThjzeU:APA91bH4yxmuIEE6GJ9HMcij023UNP7GXAboNyY1D8EYNgUFrH4Yve90IWHI2PyzlCSjwAQwblB7RWnkPHQZHle4PxAzZNtW4ZFGA038etbF6QVKK4swWNxMgCLD4am5X_FNNVVBkpc7');

DROP TABLE IF EXISTS `followers`;
CREATE TABLE `followers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pet_id` int(10) NOT NULL,
  `follower_user_id` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `food_company`;
CREATE TABLE `food_company` (
  `c_id` int(10) NOT NULL AUTO_INCREMENT,
  `c_name` varchar(250) NOT NULL,
  `c_desc` varchar(250) NOT NULL,
  `c_img_path` varchar(250) NOT NULL,
  `cat_id` int(11) NOT NULL,
  `sub_cat_id` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0.deactive,1.active',
  PRIMARY KEY (`c_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `food_company` (`c_id`, `c_name`, `c_desc`, `c_img_path`, `cat_id`, `sub_cat_id`, `status`) VALUES
(2,	'ROYAL CANIN',	'Veterinarian Jean Cathary founded ROYAL CANIN® in France in 1968.',	'assets/images/products/1564842445.jpg',	1,	1,	1),
(3,	'PEDIGREE',	'Pedigree spent the last 60 years developing a range of dog food that gives your dog the variety he wants and loves, along with just the right balance of vitamins, fibre and protein he needs – at every stage of life.',	'assets/images/products/1564916402.jpg',	1,	1,	1),
(4,	'DROOLS',	'We are a part of the IB Group , a leading ISO certified conglomerate in Central India,with an experience of 36 years in human protein.',	'assets/images/products/1564845345.jpg',	1,	1,	1),
(5,	'SMART HEART',	'Perfect Companion Group (PCG) is a worldwide leading pet food company that was established to serve the needs of the pet care industry. We are affiliated to The Charoen Pokphand (C.P.) Group – Thailand’s leading Multinational Corporation with over US',	'assets/images/products/1564845123.jpg',	1,	1,	1),
(6,	'DOG N JOY',	'INTRO REPEATED UNDER PET CARE  Betagro foods for pets include dog food under Perfecta, DOG\'njoy .',	'assets/images/products/1564845994.jpg',	1,	1,	1),
(7,	'FIDELE',	'Fidèle is French for ‘Loyal’. The brand believes that dogs should be given the same amount of commitment and loyalty that they bestow upon us, if not more. ',	'assets/images/products/1564846261.jpg',	1,	1,	1),
(8,	'ARDEN GRANGE',	'Arden Grage is renowned for producing not only super premium quality, highly digestible pet foods, but also extremely palatable ones. The super premium diets are suitable for all breeds of dog and cat.',	'assets/images/products/1564847926.jpg',	1,	1,	1),
(9,	'ACANA',	'Acana make Biologically Appropriate dog and cat foods from Fresh Regional Ingredients, and we make them from start to finish in our very own award-winning kitchen.',	'assets/images/products/1564848892.jpg',	1,	1,	1),
(10,	'ORIJEN',	'Made from Canada’s best and freshest ingredients in our award-winning kitchens here in Alberta, Canada, ORIJEN keeps your cherished dog or cat happy, healthy and strong — we guarantee it!',	'assets/images/products/1564913957.jpg',	1,	1,	1),
(11,	'HIMALAYA',	'Complete and Balanced Food for Dogs.',	'assets/images/products/1564914552.jpg',	1,	1,	1),
(12,	'ROYAL CANIN',	'Royal Canin is a global leader in pet health nutrition. ',	'assets/images/products/1564916376.jpg',	1,	2,	1),
(13,	'PEDIGREE ',	' Pedigree has spent the last 60 years developing a range of dog food that gives your dog the variety he wants and loves, along with just the right balance of vitamins, fibre and protein he needs – at every stage of life.',	'assets/images/products/1564916454.jpg',	1,	2,	1),
(14,	'DROOLS',	'We are a part of the IB Group , a leading ISO certified conglomerate in Central India,with an experience of 36 years in human protein.',	'assets/images/products/1564916514.jpg',	1,	2,	1),
(15,	'SMART HEART',	'Perfect Companion Group (PCG) is a worldwide leading pet food company that was established to serve the needs of the pet care industry.',	'assets/images/products/1564916545.jpg',	1,	2,	1),
(16,	'FIDELE',	'Fidèle is French for ‘Loyal’. The brand believes that dogs should be given the same amount of commitment and loyalty that they bestow upon us, if not more. ',	'assets/images/products/1564916574.jpg',	1,	2,	1),
(17,	'JERHIGH',	'International Pet Food Company Limited – IPF was founded in 30 January 2002 in Kang Khoi, Saraburi Province, to manufacture and distribute pet food/pet treats for dogs and cats.',	'assets/images/products/1564916926.jpg',	1,	2,	1),
(18,	'VENKYS',	'INDIAN BRAND.',	'assets/images/products/1564917121.jpg',	1,	2,	1),
(32,	'CANINE CREEK',	'MADE IN INDIA',	'assets/images/products/1564914967.jpg',	1,	1,	1),
(33,	'PUREPET',	'MADE IN INDIA',	'assets/images/products/1564915398.jpg',	1,	1,	1),
(37,	'LET\'S BITE',	'MADE IN INDIA',	'assets/images/products/1564917314.jpg',	1,	1,	1),
(38,	'ROYAL CANIN',	'FOREIGN BRAND',	'assets/images/products/1564922635.jpg',	2,	10,	1),
(39,	'ME-O',	'MADE IN THAILAND',	'assets/images/products/1564922796.jpg',	2,	10,	1),
(40,	'WHISKAS',	'FOREIGN BRAND',	'assets/images/products/1564923023.jpg',	2,	10,	1),
(41,	'LARA',	'FOREIGN FOOD',	'assets/images/products/1564923296.jpg',	2,	10,	1),
(42,	'ORIJEN',	'MADE IN CANADA',	'assets/images/products/1564923379.jpg',	2,	10,	1),
(43,	'ACANA',	'MADE IN CANADA',	'assets/images/products/1564923403.jpg',	2,	10,	1),
(44,	'DROOLS',	'MADE IN INDIA',	'assets/images/products/1564923422.jpg',	2,	10,	1),
(45,	'PUREPET',	'MADE IN INDIA',	'assets/images/products/1564923445.jpg',	2,	10,	1),
(46,	'LET\'S BITE',	'MADE IN INDIA',	'assets/images/products/1564923475.jpg',	2,	10,	1),
(47,	'ME-O',	'MADE IN THAILAND',	'assets/images/products/1564923735.jpg',	2,	11,	1),
(48,	'WHISKAS',	'MADE IN INDIA',	'assets/images/products/1564923751.jpg',	2,	11,	1),
(49,	'SHEBA',	'FOREIGN BRAND',	'assets/images/products/1564923891.jpg',	2,	11,	1),
(50,	'BELLOTTA',	'FOREIGN BRAND',	'assets/images/products/1564924044.jpg',	2,	11,	1),
(51,	'APPLAWS',	'FOREIGN FOOD',	'assets/images/products/1564924186.jpg',	2,	11,	1),
(52,	'ROYAL CANIN',	'MADE IN USA',	'assets/images/products/1564924212.jpg',	2,	11,	1),
(53,	'SOFT & CHEWY ',	'Soft treats for dogs.',	'assets/images/products/1566046021.png',	1,	3,	1),
(54,	'DENTAL ',	'Dental care for pets',	'assets/images/products/1566046323.png',	1,	3,	1),
(55,	'JERKY',	'JERKY TREATS FOR DOGS',	'assets/images/products/1566046732.png',	1,	3,	1),
(56,	'BONES & RAW HIDES',	'HARD TREATS',	'assets/images/products/1566047060.png',	1,	3,	1),
(57,	'BISCUITS',	'CRUNCHY TREATS.',	'assets/images/products/1566047299.png',	1,	3,	1),
(58,	'VEGGIE',	'VEGETARIAN TREATS',	'assets/images/products/1566049341.png',	1,	3,	1),
(59,	'VITAPOL',	'IMPORTED BRAND',	'assets/images/products/1564926376.jpg',	3,	19,	1),
(60,	'KAPIO',	'MADE IN INDIA',	'assets/images/products/1564925530.jpg',	3,	19,	1),
(61,	'SEEDS',	'MADE IN INDIA',	'assets/images/products/1564926054.jpg',	3,	19,	1),
(62,	'ZUPREEM',	'MADE IN USA',	'assets/images/products/1564926196.jpg',	3,	19,	1),
(63,	'VITAPOL',	'MADE IN USA',	'assets/images/products/1564926408.jpg',	3,	20,	1),
(64,	'VITAPOL',	'MADE IN USA',	'assets/images/products/1564937686.jpg',	6,	30,	1),
(65,	'SMART HEART',	'MADE IN THAILAND',	'assets/images/products/1564937829.jpg',	6,	30,	1),
(66,	'SMART HEART',	'MADE IN THAILAND',	'assets/images/products/1564926293.jpg',	3,	19,	1),
(67,	'CAPT ZACK',	'MADE IN INDIA',	'assets/images/products/1564918621.jpg',	1,	5,	1),
(68,	'BEAPHAR',	'IMPORTED BRAND',	'assets/images/products/1564918670.jpg',	1,	5,	1),
(69,	'HIMALAYA',	'MADE IN INDIA',	'assets/images/products/1564918713.jpg',	1,	5,	1),
(70,	'FORBIS',	'IMPORTED',	'assets/images/products/1564918946.jpg',	1,	5,	1),
(71,	'BEAPHAR',	'IMPORTED BRAND',	'assets/images/products/1564924967.jpg',	2,	14,	1),
(72,	'BEAPHAR',	'IMPORTED',	'assets/images/products/1564917673.jpg',	1,	4,	1),
(73,	'TETRA',	'IMPORTED',	'assets/images/products/1564926518.jpg',	4,	23,	1),
(74,	'OPTIMUM',	'IMPORTED ',	'assets/images/products/1564927042.jpg',	4,	23,	1),
(75,	'TAIYO',	'IMPORTED',	'assets/images/products/1564927253.jpg',	4,	23,	1),
(76,	'TAIYO',	'IMPORTED',	'assets/images/products/1564927296.jpg',	5,	27,	1),
(77,	'HIKARI',	'IMORTED',	'assets/images/products/1564927480.jpg',	5,	27,	1),
(78,	'DENTAL CARE',	'IMPORTED',	'assets/images/products/1565452054.jpg',	1,	4,	1),
(79,	'DROOL\'S',	'MADE IN INDIA',	'assets/images/products/1564917707.jpg',	1,	4,	1),
(80,	'MEDIVET',	'MADE IN INDIA',	'assets/images/products/1564919359.jpg',	1,	5,	1),
(81,	'BHARAT INTERNATIONAL',	'MADE IN INDIA',	'assets/images/products/1564919759.jpg',	1,	5,	1),
(82,	'PETS LIFESTYLE',	'MADE IN INDIA',	'assets/images/products/1564920192.jpg',	1,	5,	1),
(83,	'BIOCLEAN',	'MADE IN INDIA',	'assets/images/products/1564922530.jpg',	1,	5,	1),
(84,	'LOZALO',	'MADE IN INDIA',	'assets/images/products/1564920855.jpg',	1,	5,	1),
(85,	'CAREDOM',	'MADE IN INDIA',	'assets/images/products/1564921827.jpg',	1,	5,	1),
(86,	'INTAS',	'MADE IN INDIA',	'assets/images/products/1564921637.jpg',	1,	5,	1),
(87,	'PETS EMPIRE',	'MADE IN INDIA',	'assets/images/products/1564922032.jpg',	1,	5,	1),
(88,	'CAT\'N JOY',	'MADE IN THAILAND',	'assets/images/products/1564923695.jpg',	2,	10,	1),
(89,	'VITAPOL',	'IMPORTED',	'assets/images/products/1564938394.jpg',	6,	31,	1),
(90,	'ZUPREEM',	'IMPORTED',	'assets/images/products/1564937857.jpg',	6,	30,	1),
(91,	'CHIPSI',	'IMPORTED',	'assets/images/products/1564938419.jpg',	6,	31,	1),
(92,	'CHIPSI',	'IMPORTED',	'assets/images/products/1564938350.jpg',	6,	30,	1),
(93,	'ABK IMPORTS',	'IMPORTED',	'assets/images/products/1564924488.jpg',	2,	12,	1),
(94,	'ME-O',	'MADE IN THAILAND',	'assets/images/products/1564924514.jpg',	2,	12,	1),
(95,	'RENA',	'IMPORTED',	'assets/images/products/1566134178.png',	2,	12,	1),
(96,	'BEAPHAR',	'IMPORTED',	'assets/images/products/1566133745.jpg',	2,	13,	1),
(97,	'JINNY',	'MADE IN THAILAND',	'assets/images/products/1566133476.png',	2,	12,	1),
(98,	'SHEBA',	'IMPORTED',	'assets/images/products/1566133516.jpg',	2,	12,	1),
(99,	'CAGES & PENS',	'MADE IN INDIA',	'assets/images/products/1566129421.png',	1,	6,	1),
(100,	'CAGES',	'MADE IN INDIA',	'assets/images/products/1565116243.png',	3,	21,	1),
(101,	'LITTER TRAY',	'MADE IN INDIA',	'assets/images/products/1565116990.jpg',	2,	15,	1),
(102,	'GROOMING TOOLS',	'MADE IN INDIA',	'assets/images/products/1566130448.png',	1,	6,	1),
(103,	'BOWLS & FEEDERS',	'MADE IN INDIA',	'assets/images/products/1566136519.png',	1,	6,	1),
(104,	'LEASHES & COLLARS',	'MADE IN INDIA',	'assets/images/products/1566132977.png',	1,	6,	1),
(105,	'POTTY & CLEANING',	'MADE IN INDIA',	'assets/images/products/1566135120.png',	1,	6,	1),
(106,	'CHAINS & WIRE LEASH',	'MADE IN INDIA',	'assets/images/products/1566136830.png',	1,	6,	1),
(107,	'SOFT TOYS',	'MADE IN INDIA',	'assets/images/products/1566137658.png',	1,	7,	1),
(108,	'ROPE & TUG TOYS',	'MADE IN INDIA',	'assets/images/products/1566138665.png',	1,	7,	1),
(109,	'CHEW TOYS',	'MADE IN INDIA',	'assets/images/products/1566139505.png',	1,	7,	1),
(110,	'PREMIUM TOYS',	'MADE IN USA',	'assets/images/products/1566140470.png',	1,	7,	1),
(111,	'SANDOZ',	'MADE IN INDIA',	'assets/images/products/1566131548.png',	1,	8,	1),
(112,	'T - SHIRTS',	'MADE IN INDIA',	'assets/images/products/1566132100.png',	1,	8,	1),
(113,	'POLO T - SHIRT',	'MADE IN INDIA',	'assets/images/products/1566132222.png',	1,	8,	1),
(114,	'JACKETS & SWEATERS',	'MADE IN INDIA',	'assets/images/products/1566132891.png',	1,	8,	1),
(115,	'BUGZFREE T - SHIRT',	'MADE IN INDIA',	'assets/images/products/1566133123.png',	1,	8,	1),
(116,	'RAINCOATS',	'MADE IN INDIA',	'assets/images/products/1566133307.png',	1,	8,	1),
(117,	'SAVIC',	'MADE IN BELGIUM',	'assets/images/products/1566130370.png',	1,	9,	1),
(118,	'CANES VENATICI',	'MADE IN INDIA',	'assets/images/products/1566130860.png',	1,	9,	1),
(119,	'WEANING DIET',	'MADE IN INDIA',	'assets/images/products/1565447227.jpg',	1,	1,	1),
(120,	'VERSELE LAGA',	'IMPORTED',	'assets/images/products/1566047484.png',	1,	4,	1),
(121,	'MY BEAU',	'IMPORTED',	'assets/images/products/1566047621.png',	1,	4,	1),
(122,	'HIMALAYA',	'MADE IN INDIA',	'assets/images/products/1566047656.jpg',	1,	4,	1),
(123,	'OTHERS',	'MADE IN INDIA',	'assets/images/products/1566129579.jpg',	1,	4,	1),
(124,	'LITTER',	'MADE IN INDIA',	'assets/images/products/1565519959.jpg',	2,	15,	1),
(125,	'HIMALAYA',	'MADE IN INDIA',	'assets/images/products/1566133782.jpg',	2,	14,	1),
(126,	'BHARAT INTERNATIONAL',	'MADE IN INDIA',	'assets/images/products/1566133820.jpg',	2,	14,	1),
(127,	'LITTLE BIG PAW',	'IMPORTED',	'assets/images/products/1566047057.png',	2,	11,	1),
(128,	'LITTLE BIG PAW',	'IMPORTED',	'assets/images/products/1566046934.png',	1,	2,	1),
(129,	'BIOGROOM',	'IMPORT',	'assets/images/products/1566129884.png',	1,	5,	1),
(130,	'TROPICLEAN',	'MADE IN USA',	'assets/images/products/1566129998.png',	1,	5,	1),
(131,	'BIOGROOM',	'IMPORTED',	'assets/images/products/1566133850.png',	2,	14,	1),
(132,	'TRIXIE',	'IMPORTED',	'assets/images/products/1566130158.png',	1,	5,	1),
(134,	'TRIXIE',	'IMPORTED',	'assets/images/products/1566213853.png',	2,	14,	1),
(135,	'PETKIN',	'MADE IN USA',	'assets/images/products/1566217622.png',	1,	5,	1),
(136,	'PETKIN',	'MADE IN USA',	'assets/images/products/1566217960.png',	2,	14,	1),
(137,	'TRIXIE',	'MADE IN USA',	'assets/images/products/1566232842.png',	6,	31,	1),
(138,	'SIMPLE SOLUTION',	'MADE IN USA',	'assets/images/products/1566234411.png',	1,	5,	1),
(139,	'SIMPLE SOLUTION',	'MADE IN USA',	'assets/images/products/1566237120.png',	2,	14,	1),
(140,	'TAIYO',	'MADE IN THAILAND',	'assets/images/products/1566584784.jpg',	5,	28,	1),
(141,	'BAYER',	'MADE IN INDIA',	'assets/images/products/1566669664.jpg',	1,	5,	1),
(142,	'HIKARI',	'MADE IN JAPAN',	'assets/images/products/1566754689.jpg',	4,	23,	1),
(143,	'CESAR',	'MADE IN THAILAND',	'assets/images/products/1569756492.png',	1,	2,	1),
(144,	'HOUSE',	'MADE IN INDIA',	'assets/images/products/1572273040.jpg',	5,	29,	1),
(145,	'RID ALL',	'MADE IN INDIA',	'assets/images/products/1572274966.png',	4,	24,	1);

DROP TABLE IF EXISTS `food_product`;
CREATE TABLE `food_product` (
  `p_id` int(10) NOT NULL AUTO_INCREMENT,
  `p_code` varchar(100) NOT NULL,
  `cat_id` int(11) NOT NULL,
  `p_name` varchar(250) NOT NULL,
  `p_description` text NOT NULL,
  `p_type` varchar(250) NOT NULL COMMENT 'Food or Accessories ',
  `p_pet_type` varchar(250) NOT NULL COMMENT 'Dog,Cat,Fish etc',
  `p_cat_type` varchar(250) NOT NULL COMMENT 'Latest,Popular,Feature',
  `p_rate` float NOT NULL DEFAULT '0',
  `p_rate_sale` float NOT NULL DEFAULT '0',
  `quantity` int(11) NOT NULL DEFAULT '0',
  `gross_amt` float NOT NULL DEFAULT '0',
  `discount` float NOT NULL DEFAULT '0' COMMENT 'in percentage always',
  `net_amt` double NOT NULL DEFAULT '0' COMMENT 'by quantity and discount ',
  `shipping_cost` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `img_path` varchar(250) NOT NULL,
  `color` varchar(250) NOT NULL,
  `size` varchar(250) NOT NULL,
  `weight` varchar(255) NOT NULL,
  `deleted` int(11) NOT NULL DEFAULT '1',
  `status` int(10) NOT NULL,
  `product_rating` int(10) NOT NULL DEFAULT '1',
  `c_id` int(11) NOT NULL,
  `mandatory` int(11) NOT NULL DEFAULT '0' COMMENT '0-No,1-Yes',
  `created_at` varchar(255) NOT NULL,
  `updated_at` varchar(255) NOT NULL,
  UNIQUE KEY `p_id` (`p_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `food_product` (`p_id`, `p_code`, `cat_id`, `p_name`, `p_description`, `p_type`, `p_pet_type`, `p_cat_type`, `p_rate`, `p_rate_sale`, `quantity`, `gross_amt`, `discount`, `net_amt`, `shipping_cost`, `country`, `img_path`, `color`, `size`, `weight`, `deleted`, `status`, `product_rating`, `c_id`, `mandatory`, `created_at`, `updated_at`) VALUES
(6,	'144327540877',	0,	'PEDIGREE STARTER MOTHER & PUP',	'Pedigree Professional Range dog food fulfills the special needs of your dog. The range provides expert nutrition combining high quality ingredients with the science developed by our veterinarians and nutritionists at WALTHAM- the world’s leading auth',	'1',	'1',	'General',	500,	500,	4,	2000,	10,	1800,	'0',	'IN',	'',	'',	'',	'1.2 Kg',	1,	1,	1,	3,	1,	'1564467808',	'1571942456'),
(7,	'202283120999',	0,	'PEDIGREE STARTER MOTHER & PUP',	'Pedigree Professional Range dog food fulfills the special needs of your dog. The range provides expert nutrition combining high quality ingredients with the science developed by our veterinarians and nutritionists at WALTHAM- the world’s leading auth',	'1',	'1',	'General',	1200,	1200,	3,	3600,	10,	3240,	'0',	'IN',	'',	'',	'',	'3 Kg',	1,	1,	1,	3,	1,	'1564467808',	'1571942547'),
(8,	'826808711295',	0,	'PEDIGREE STARTER MOTHER & PUP ',	'Pedigree Professional Range dog food fulfills the special needs of your dog. The range provides expert nutrition combining high quality ingredients with the science developed by our veterinarians and nutritionists at WALTHAM- the world’s leading auth',	'1',	'1',	'General',	3400,	3400,	2,	6800,	10,	6120,	'0',	'IN',	'',	'',	'',	'10 Kg',	1,	1,	1,	3,	1,	'1564467808',	'1571942590'),
(9,	'114990995920',	0,	'PEDIGREE PUPPY CHICKEN & MILK 100 gm ',	'PEDIGREE Chicken & Milk for Puppy is a wholesome meal, packed with essential nutrients vital to the healthy growth of your pet. The natural goodness of cereals, chicken, meat, soybean, carrots, peas & milk blend into a tasty treat for your little one',	'1',	'1',	'Popular',	100,	100,	5,	500,	0,	500,	'50',	'IN',	'',	'',	'',	'100 gm (PACK OF 5)',	1,	1,	1,	3,	1,	'1564467808',	'1564513447'),
(10,	'207756585704',	0,	'PEDIGREE PUPPY CHICKEN & MILK 100 gm ',	'PEDIGREE Chicken & Milk for Puppy is a wholesome meal, packed with essential nutrients vital to the healthy growth of your pet. The natural goodness of cereals, chicken, meat, soybean, carrots, peas & milk blend into a tasty treat for your little one',	'1',	'1',	'Popular',	200,	200,	5,	1000,	0,	1000,	'50',	'IN',	'',	'',	'',	'100 gm (PACK OF 10)',	1,	1,	1,	3,	1,	'1564467808',	'1564513490'),
(11,	'216715984203',	0,	'PEDIGREE PUPPY CHICKEN & MILK',	'PEDIGREE Chicken & Milk for Puppy is a wholesome meal, packed with essential nutrients vital to the healthy growth of your pet. The natural goodness of cereals, chicken, meat, soybean, carrots, peas & milk blend into a tasty treat for your little one',	'1',	'1',	'Popular',	85,	85,	5,	425,	0,	425,	'50',	'IN',	'',	'',	'',	'400 gm',	1,	1,	1,	3,	1,	'1564467808',	'1564513534'),
(12,	'459838357040',	0,	'PEDIGREE PUPPY CHICKEN & MILK',	'PEDIGREE Chicken & Milk for Puppy is a wholesome meal, packed with essential nutrients vital to the healthy growth of your pet. The natural goodness of cereals, chicken, meat, soybean, carrots, peas & milk blend into a tasty treat for your little one',	'1',	'1',	'Popular',	260,	260,	5,	1300,	10,	1170,	'0',	'IN',	'',	'',	'',	'1.2 Kg',	1,	1,	1,	3,	1,	'1564467808',	'1564513558'),
(13,	'799900984794',	0,	'PEDIGREE PUPPY CHICKEN & MILK',	'PEDIGREE Chicken & Milk for Puppy is a wholesome meal, packed with essential nutrients vital to the healthy growth of your pet. The natural goodness of cereals, chicken, meat, soybean, carrots, peas & milk blend into a tasty treat for your little one',	'1',	'1',	'Popular',	610,	610,	5,	3050,	10,	2745,	'0',	'IN',	'',	'',	'',	'3 Kg',	1,	1,	1,	3,	1,	'1564467808',	'1564513579'),
(14,	'563048930190',	0,	'PEDIGREE PUPPY CHICKEN & MILK',	'PEDIGREE Chicken & Milk for Puppy is a wholesome meal, packed with essential nutrients vital to the healthy growth of your pet. The natural goodness of cereals, chicken, meat, soybean, carrots, peas & milk blend into a tasty treat for your little one',	'1',	'1',	'Popular',	1170,	1170,	5,	5850,	10,	5265,	'0',	'IN',	'',	'',	'',	'6 Kg',	1,	1,	1,	3,	1,	'1564467808',	'1564513605'),
(642,	'257737417826',	0,	'CATAHOLIC NEKO CAT SPIRAL SOFT CHICKEN & FISH ( PACK OF 2 )',	'NUTRITIOUS: These Cat Treats Provide the Ideal Nutritious Treats for Your Cat; Healthy, Tasty and Always Safe. DIGESTIBLE: Cataholic Chicken and Fish Cat Treats are Easily Digested and Absorbed with Zero Fillers for a Snack that is Healthy. QUALITY I',	'2',	'1',	'Popular',	398,	398,	1,	398,	10,	358.2,	'0',	'IN',	'',	'',	'',	'50 gm * 2',	1,	1,	1,	95,	1,	'1564946634',	'');

DROP TABLE IF EXISTS `food_recommended`;
CREATE TABLE `food_recommended` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `c_id` varchar(20) NOT NULL,
  `p_id` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `threshold` varchar(50) NOT NULL,
  `petid` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `join_event`;
CREATE TABLE `join_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  `updated_on` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `join_event_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes` (
  `likeID` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `postID` int(11) NOT NULL,
  `created` int(11) NOT NULL,
  PRIMARY KEY (`likeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `likes` (`likeID`, `user_id`, `postID`, `created`) VALUES
(1,	1,	1,	1574927459),
(3,	2,	3,	1574929833);

DROP TABLE IF EXISTS `manualactivity`;
CREATE TABLE `manualactivity` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pet_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `time_stamp` varchar(250) NOT NULL,
  `manualactivity` int(11) NOT NULL DEFAULT '0',
  `unit` varchar(50) NOT NULL,
  `date` varchar(50) NOT NULL,
  `target` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `media`;
CREATE TABLE `media` (
  `mediaID` int(11) NOT NULL AUTO_INCREMENT,
  `url` text NOT NULL,
  `mediaType` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `contentType` varchar(20) NOT NULL,
  `postID` int(11) NOT NULL,
  PRIMARY KEY (`mediaID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `nearby_type`;
CREATE TABLE `nearby_type` (
  `nearby_id` int(11) NOT NULL AUTO_INCREMENT,
  `nearby_type_name` varchar(255) NOT NULL,
  PRIMARY KEY (`nearby_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `nearby_type` (`nearby_id`, `nearby_type_name`) VALUES
(1,	'Veterinary'),
(2,	'Pet Spa'),
(3,	'Pet Shop'),
(4,	'Hostels'),
(5,	'Trainer');

DROP TABLE IF EXISTS `nearby_vss`;
CREATE TABLE `nearby_vss` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nearby_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `image_path` varchar(255) NOT NULL,
  `email` varchar(50) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `start_timing` varchar(255) NOT NULL DEFAULT '" "',
  `end_timing` varchar(255) NOT NULL DEFAULT '" "',
  `lati` double NOT NULL,
  `longi` double NOT NULL,
  `status` int(1) NOT NULL,
  `e_open_time` varchar(255) NOT NULL DEFAULT '" " ',
  `e_close_time` varchar(255) NOT NULL DEFAULT '" "',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `nearby_vss` (`id`, `nearby_id`, `name`, `image_path`, `email`, `mobile`, `address`, `start_timing`, `end_timing`, `lati`, `longi`, `status`, `e_open_time`, `e_close_time`) VALUES
(6,	1,	'DR. ASHISH JAIN  (M.V.Sc. & A.H.) Physician & Gynaecologist',	'assets/images/vss/1563303121.jpg',	'enterprisestithi@gmail.com',	'9827315534',	' Care N Cure Dog Clinic, 1-AF, Shivam Palace, Vijay Nagar, Indore - 452010, Near Scheme No 54, Near Golden Gate (Map)',	'11:00AM',	'01:00PM',	22.7567348,	75.8925876,	1,	'06:00PM',	'09:00PM'),
(7,	3,	'GIANTLAND PET SHOP',	'assets/images/vss/1563304198.jpg',	'giantlandpets@gmail.com',	'9425977777',	'148, Revenue Nagar, Annapurna Main Road, Indore ',	'10:00AM',	'10:00PM',	55665.9,	22.749909,	1,	'\" \" ',	'\" \"'),
(8,	4,	'GIANTLAN KENNEL& HOSTEL',	'assets/images/vss/1563305619.jpg',	'giantlandpets@gmail.com',	'9425977777',	'55, Anand Farms, Sahakar Nagar, Behind Aim Win Academy ,Near  CAT Circle, Indore- 452013',	'08:00AM',	'08:00PM',	55665.9,	22.749909,	1,	'\" \" ',	'\" \"'),
(9,	2,	'GIANTLAND PET SPA',	'assets/images/vss/1563305929.jpg',	'GIANTLANDPETS@GMAIL.COM',	'9425977777',	'148, Revenue Nagar , Annapurna Main Road , Indore',	'12:00AM',	'06:00PM',	55665.9,	22.749909,	1,	'\" \" ',	'\" \"'),
(19,	5,	'Sr. Raghav Patel ',	'assets/images/vss/1563888822.jpeg',	'patel@gmail.com',	'9542816945',	'A-46 nayta mundla by pass road near RTO office indore madhya pradesh 452010',	'\" \"',	'\" \"',	55665.9,	22.749909,	1,	'\" \" ',	'\" \"'),
(20,	1,	'Dr. Hemant Mehta ( M. V. Sc )',	'assets/images/vss/1565858106.jpg',	'NONE',	'919826287433',	'EB 137, SMJ Girls Hostel, Scheme 94, Landmark: Opp. Bombay Hospital., Indore (M.P.)',	'06:00PM',	'09:00PM',	55665.9,	22.749909,	1,	'06:00PM',	'09:00PM'),
(21,	2,	'Naveen Saloon',	'assets/images/vss/1564395330.jpg',	'Saloon@gmail.com',	'1234567980',	'Indore MP',	'09:31AM',	'10:30AM',	55665.9,	22.749909,	1,	'\" \" ',	'\" \"'),
(23,	4,	'DOG VILLA HOSTEL',	'assets/images/vss/1565882670.png',	'pet@gmail.com',	'918819817773',	'RCM 1/3, Scheme 140, near masc Hospital, pipliyahana',	'10:00AM',	'08:00PM',	55665.9,	22.749909,	1,	'\" \" ',	'\" \"'),
(24,	5,	'Dr. Ashish Mehra',	'assets/images/vss/1564395631.jpeg',	'Ashish@gmail.com',	'1234567890',	'Vijay Nagar Indore MP 452010',	'\" \"',	'\" \"',	55665.9,	22.749909,	1,	'\" \" ',	'\" \"'),
(26,	1,	'DR. BP SHUKLA',	'assets/images/vss/1565859584.jpg',	'NONE',	'9826298323',	'SHREE SAI ADVANCED PET POLYCLINIC, 750 SAI KRIPA COLONY,MR 10 RD ,INDORE ( NEAR INFRONT OF RADISSON )',	'12:00AM',	'12:00PM',	55665.9,	22.749909,	1,	'05:00PM',	'09:00PM'),
(27,	1,	'DR. SANDEEP NANAVATI ',	'assets/images/vss/1565860247.jpg',	'NONE',	'919826013667',	'304, Tilak Nagar, Tilak Nagar, Indore - 452018, Near Shwetambar Jain Mandir',	'',	'',	55665.9,	22.749909,	1,	'07:00PM',	'09:00PM'),
(28,	1,	'DR PRASHANT TIWARI ',	'assets/images/vss/1565860814.jpg',	'NONE',	'919826019476',	'408, Urvashi Cplx, Plot No 3, Tilak Nagar Indore, Indore - 452018, Opposite Tampo Stand',	'08:00AM',	'10:00AM',	55665.9,	22.749909,	1,	'05:00PM',	'06:00PM'),
(29,	1,	'DR RK JAIN ( B.D.S )',	'assets/images/vss/1565861267.jpg',	'NONE',	'919826018283',	'Interpet The Pets Clinic, No 59, Khajrana Road, Kakad Road, New Palasia, Indore - 452001, Near Anand Bazar, Shrinagar Main',	'',	'',	55665.9,	22.749909,	1,	'06:00PM',	'09:00PM'),
(30,	1,	'dr kuldeep singh',	'assets/images/vss/1565883506.jpg',	'NONE',	'9630809656',	'3070-E, Hawa Bangla Road, Main Road, Sudama Nagar, Indore - 452009, Near Dutt Temple ',	'',	'',	55665.9,	22.749909,	1,	'05:00PM',	'09:00PM'),
(31,	1,	'dr bs karada',	'assets/images/vss/1565884173.jpg',	'NONE',	'919827206486',	'Main Arcade, Annapurna Road, Indore - 452009, Opposite Gayatri Petrol Pump',	'',	'',	55665.9,	22.749909,	1,	'06:00PM',	'09:00PM'),
(32,	1,	'DR RAJESH MAHESHWARI',	'assets/images/vss/1565884554.jpg',	'NONE',	'919827048269',	'C/o Pet Aid Clinic and Pet Shop, Ug 8, Suniket, Khajrana Main Road, Sringar Colony, Indore - 452001, Shrinagar Extention,Nr Little Flower School ',	'',	'',	55665.9,	22.749909,	1,	'06:00PM',	'09:00PM'),
(45,	3,	'DEMO',	'assets/images/vss/1570820317.jpg',	'abc@gmail.com',	'9425977777',	'Annapurna main road , near MRF tyre , 148  ,  revenue nagar, indore , 452009',	'10:00AM',	'09:00PM',	0,	0,	1,	'\" \" ',	'\" \"');

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `ntfID` int(10) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `deviceToken` varchar(500) NOT NULL,
  `type` int(50) NOT NULL,
  `deviceType` varchar(50) NOT NULL,
  `isRead` int(20) NOT NULL,
  `trueDate` date NOT NULL,
  `trueTime` varchar(225) NOT NULL,
  `user_id` int(10) NOT NULL,
  `sentTime` varchar(225) NOT NULL,
  `sentTimestamp` varchar(225) NOT NULL,
  `flag` int(20) NOT NULL,
  `title` varchar(250) NOT NULL,
  `sentDate` date NOT NULL,
  PRIMARY KEY (`ntfID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `title` varchar(250) NOT NULL,
  `type` varchar(250) NOT NULL DEFAULT 'Individual',
  `msg` text NOT NULL,
  `created_at` varchar(20) NOT NULL,
  `ids` bigint(20) NOT NULL COMMENT 'Post Id, Pet Id, User Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `notifications` (`id`, `user_id`, `title`, `type`, `msg`, `created_at`, `ids`) VALUES
(1,	1,	'hello Shivani',	'Individual',	'How Are You',	'1574776224',	0),
(2,	1,	'hellooo',	'Individual',	'Shivaniii',	'1574924216',	0),
(3,	1,	'heloooo ',	'Individual',	'Testing',	'1574926159',	0),
(4,	2,	'heloooo ',	'Individual',	'Testing',	'1574926159',	0),
(5,	3,	'Like',	'Like',	'Shivangi Aday liked your post.',	'1574926590',	3),
(6,	3,	'Like',	'Like',	'Shivangi Aday liked your post.',	'1574926597',	3),
(7,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574926610',	3),
(8,	2,	'Like',	'Like',	'rahul liked your post.',	'1574926629',	2),
(9,	2,	'Comment',	'Comment',	'rahul commented on your post.',	'1574926647',	2),
(10,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574926903',	3),
(11,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574926906',	3),
(12,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574926908',	3),
(13,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574926909',	3),
(14,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574926909',	3),
(15,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574926910',	3),
(16,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574927053',	3),
(17,	2,	'Like',	'Like',	'kavita yadav liked your post.',	'1574927381',	1),
(18,	3,	'Like',	'Like',	'Shivangi Aday liked your post.',	'1574929781',	3),
(19,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574929792',	3),
(20,	3,	'Comment',	'Comment',	'Shivangi Aday commented on your post.',	'1574929825',	3),
(21,	3,	'Like',	'Like',	'Shivangi Aday liked your post.',	'1574929833',	3);

DROP TABLE IF EXISTS `offer`;
CREATE TABLE `offer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `p_type` varchar(255) NOT NULL,
  `p_pet_type` varchar(255) NOT NULL,
  `c_id` varchar(255) NOT NULL,
  `p_name` varchar(255) NOT NULL,
  `discount` float NOT NULL DEFAULT '0',
  `image` varchar(255) NOT NULL,
  `start_date` varchar(50) NOT NULL,
  `end_date` varchar(50) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `order_id` varchar(20) NOT NULL,
  `total_price` varchar(10) NOT NULL,
  `final_price` varchar(10) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '6' COMMENT '1 = pending',
  `payment_status` int(11) NOT NULL COMMENT '0=Unpaid,1Paid',
  `discount` varchar(10) NOT NULL DEFAULT '0',
  `cod_charges` varchar(10) NOT NULL,
  `order_date` varchar(50) NOT NULL,
  `place_date` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `city` varchar(255) NOT NULL,
  `address` longtext NOT NULL,
  `zip` longtext NOT NULL,
  `country` longtext NOT NULL,
  `landMark` text NOT NULL,
  `name` text NOT NULL,
  `country_code` varchar(255) NOT NULL,
  `mobile_no` varchar(255) NOT NULL,
  `created_at` varchar(20) NOT NULL,
  `updated_at` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `order_products`;
CREATE TABLE `order_products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payment_id` varchar(25) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `p_description` text NOT NULL,
  `p_rate` varchar(10) NOT NULL,
  `discount` varchar(10) NOT NULL DEFAULT '0',
  `price_discount` varchar(10) NOT NULL,
  `discount_total` varchar(10) NOT NULL,
  `img_path` varchar(200) NOT NULL,
  `p_name` varchar(200) NOT NULL,
  `quantity` varchar(5) NOT NULL,
  `total_price` varchar(10) NOT NULL,
  `order_id` varchar(20) NOT NULL,
  `color` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  `weight` varchar(255) NOT NULL,
  `created_at` varchar(20) NOT NULL,
  `updated_at` varchar(20) NOT NULL,
  `payment_status` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `order_status`;
CREATE TABLE `order_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(255) NOT NULL,
  `od_pending_date` varchar(255) NOT NULL,
  `od_pending_status` varchar(255) NOT NULL,
  `od_confirm_status` varchar(225) NOT NULL,
  `od_confirm_date` varchar(255) NOT NULL,
  `od_packed_status` varchar(255) NOT NULL,
  `od_packed_date` varchar(255) NOT NULL,
  `od_dispatched_status` varchar(255) NOT NULL,
  `od_dispatched_date` varchar(255) NOT NULL,
  `od_delivered_status` varchar(255) NOT NULL,
  `od_delivered_date` varchar(255) NOT NULL,
  `od_status` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  `od_canceled_date` varchar(255) NOT NULL,
  `od_canceled_status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `petName` varchar(250) NOT NULL,
  `breed_id` int(20) NOT NULL DEFAULT '1',
  `sex` varchar(250) NOT NULL,
  `age` int(20) NOT NULL,
  `city` varchar(250) NOT NULL,
  `state` varchar(250) NOT NULL,
  `country` varchar(250) NOT NULL,
  `postcode` varchar(10) NOT NULL,
  `lati` double NOT NULL DEFAULT '0',
  `longi` double NOT NULL DEFAULT '0',
  `created_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active_status` varchar(10) NOT NULL,
  `fathers_breed` int(10) NOT NULL,
  `mothers_breed` int(10) NOT NULL,
  `current_height` varchar(50) NOT NULL,
  `current_weight` varchar(50) NOT NULL,
  `microchip_id` varchar(50) NOT NULL,
  `pet_type` varchar(50) NOT NULL,
  `active_area` varchar(50) NOT NULL,
  `lifestyle` varchar(50) NOT NULL,
  `neutered` varchar(50) NOT NULL,
  `trained` varchar(50) NOT NULL,
  `temparement_ok_dog` varchar(250) NOT NULL,
  `temparement_ok_cat` varchar(250) NOT NULL,
  `temparement_ok_people` varchar(250) NOT NULL,
  `temparement_ok_child` varchar(250) NOT NULL,
  `ins_provider` varchar(50) NOT NULL,
  `ins_policy_no` varchar(50) NOT NULL,
  `ins_upload` varchar(50) NOT NULL,
  `swimmer` varchar(50) NOT NULL,
  `petpic` varchar(50) NOT NULL,
  `birth_date` varchar(100) NOT NULL,
  `adoption_date` varchar(100) NOT NULL,
  `allergies` varchar(250) NOT NULL,
  `medical_conditions` varchar(250) NOT NULL,
  `medicines` varchar(250) NOT NULL,
  `likes` varchar(250) NOT NULL,
  `dislikes` varchar(250) NOT NULL,
  `ins_renewal_date` varchar(250) NOT NULL,
  `incidents` varchar(250) NOT NULL,
  `spayed` varchar(250) NOT NULL,
  `pet_img_path` varchar(50) NOT NULL,
  `pet_ins_path` varchar(50) NOT NULL DEFAULT '""',
  `ins_user_email` varchar(50) NOT NULL,
  `ins_id` varchar(250) NOT NULL,
  `long_value` varchar(250) NOT NULL,
  `deleted` varchar(50) NOT NULL COMMENT '0 = active ,1 =  deleted',
  `verified` int(11) NOT NULL DEFAULT '0' COMMENT '0=not verified,1verified',
  `public_private` int(10) NOT NULL COMMENT '0= public, 1= private',
  `sel_notsel` int(10) NOT NULL COMMENT '0=yes,1=no',
  `adopt` int(10) NOT NULL COMMENT '0=yes,1=no',
  `updated_stamp` bigint(20) NOT NULL,
  `view_profile` int(10) NOT NULL DEFAULT '0',
  `rating` int(10) NOT NULL DEFAULT '0',
  `total_rating_user` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pet_market`;
CREATE TABLE `pet_market` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `type_id` int(11) NOT NULL,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `country` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `city` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `state` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sale_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0. free 1. Price',
  `mobile_no` bigint(20) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `country_code` int(11) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pet_market_comments`;
CREATE TABLE `pet_market_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pet_market_fav`;
CREATE TABLE `pet_market_fav` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pet_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pet_market_images`;
CREATE TABLE `pet_market_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_id` bigint(20) NOT NULL,
  `image` text NOT NULL,
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pet_memories`;
CREATE TABLE `pet_memories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `pet_img_path` varchar(450) NOT NULL,
  `description` text NOT NULL,
  `created` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pet_type`;
CREATE TABLE `pet_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_name` varchar(100) NOT NULL COMMENT 'list of pet like : Dog,Cat,Bird etc',
  `pet_image` varchar(100) NOT NULL,
  `status` int(11) NOT NULL COMMENT '0.diactive,1.active',
  `created_at` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `pet_type` (`id`, `pet_name`, `pet_image`, `status`, `created_at`) VALUES
(1,	'DOGS',	'assets/images/category/1562265286.jpg',	1,	'1561910230'),
(2,	'CATS',	'assets/images/category/1562257664.jpg',	1,	'1561910231'),
(3,	'BIRDS',	'assets/images/category/1562258639.jpg',	1,	'1561913325'),
(4,	'FISH ',	'assets/images/category/1562258873.jpg',	1,	'1561913363'),
(5,	'TURTLE',	'assets/images/category/1562262521.jpg',	1,	'1561913756'),
(6,	'RABBIT / HAMSTER',	'assets/images/category/1562262673.jpg',	1,	'1561913836');

DROP TABLE IF EXISTS `pet_user_mapping`;
CREATE TABLE `pet_user_mapping` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pet_id` int(50) NOT NULL,
  `user_id` int(50) NOT NULL,
  `type` int(50) NOT NULL,
  `created_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `postID` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `title` text NOT NULL,
  `postType` text NOT NULL,
  `media` text NOT NULL,
  `comunity_id` int(10) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL,
  `createAt` int(11) NOT NULL,
  `flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`postID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `post` (`postID`, `content`, `title`, `postType`, `media`, `comunity_id`, `user_id`, `createAt`, `flag`) VALUES
(1,	'ftuurt',	'errtf',	'image',	'assets/images/1574926518.jpg',	0,	2,	1574926518,	1),
(2,	'it\'s my comments section',	'hello pet wall',	'text',	'',	1,	2,	1574926570,	1),
(3,	'fgbn the best of luck to you',	'ttttt',	'image',	'assets/images/1574926584.jpg',	1,	3,	1574926584,	1);

DROP TABLE IF EXISTS `product_basket`;
CREATE TABLE `product_basket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `price` varchar(255) NOT NULL,
  `product_desc` longtext NOT NULL,
  `product_img` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `mandatory` tinyint(4) NOT NULL DEFAULT '0',
  `color` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  `size_no` varchar(255) NOT NULL,
  `weight` varchar(255) NOT NULL,
  `weight_no` varchar(255) NOT NULL,
  `shipping_cost` varchar(255) NOT NULL,
  `created_at` varchar(20) NOT NULL,
  `updated_at` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `product_invoice`;
CREATE TABLE `product_invoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0. Pending 1. Confrim',
  `amount` varchar(255) NOT NULL,
  `order_id` varchar(255) NOT NULL,
  `txn_id` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pro_attribute`;
CREATE TABLE `pro_attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `p_id` int(10) NOT NULL,
  `color` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  `weight` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `p_id` (`p_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pro_cat`;
CREATE TABLE `pro_cat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cat_title` varchar(255) NOT NULL,
  `c_img_path` varchar(255) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `created_at` varchar(255) NOT NULL,
  `updated_on` varchar(255) NOT NULL,
  `pet_type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `pro_cat` (`id`, `cat_title`, `c_img_path`, `status`, `created_at`, `updated_on`, `pet_type`) VALUES
(1,	'DRY - FOOD',	'assets/images/category/1563377721.jpg',	1,	'2019-06-30 17:03:03',	'2019-06-30 17:03:03',	1),
(2,	'WET - FOOD',	'assets/images/category/1563381849.jpg',	1,	'2019-06-30 17:06:20',	'2019-06-30 17:06:20',	1),
(3,	'TREATS',	'assets/images/category/1564165905.jpg',	1,	'2019-06-30 17:09:44',	'2019-06-30 17:09:44',	1),
(4,	'SUPPLEMENTS',	'assets/images/category/1564166934.jpg',	1,	'2019-06-30 17:28:50',	'2019-06-30 17:28:50',	1),
(5,	'GROOMING RANGE',	'assets/images/category/1563465623.jpg',	1,	'2019-06-30 18:04:37',	'2019-06-30 18:04:37',	1),
(6,	'ACCESSORIES',	'assets/images/category/1563468996.jpg',	1,	'2019-06-30 18:15:40',	'2019-06-30 18:15:40',	1),
(7,	'TOYS',	'assets/images/category/1563469009.jpg',	1,	'2019-06-30 18:25:34',	'2019-06-30 18:25:34',	1),
(8,	'CLOTHING',	'assets/images/category/1563635792.jpg',	1,	'2019-06-30 18:37:38',	'2019-06-30 18:37:38',	1),
(9,	'BEDS & MATS',	'assets/images/category/1563636423.jpg',	1,	'2019-06-30 18:40:54',	'2019-06-30 18:40:54',	1),
(10,	'DRY - FOOD',	'assets/images/category/1563707218.jpg',	1,	'2019-07-01 18:26:21',	'2019-07-01 18:26:21',	2),
(11,	'WET - FOOD',	'assets/images/category/1563708358.jpg',	1,	'2019-07-01 18:29:22',	'2019-07-01 18:29:22',	2),
(12,	'TREATS',	'assets/images/category/1564166268.jpg',	1,	'2019-07-01 18:31:51',	'2019-07-01 18:31:51',	2),
(13,	'SUPPLEMENTS',	'assets/images/category/1563711204.jpg',	1,	'2019-07-01 18:34:53',	'2019-07-01 18:34:53',	2),
(14,	'GROOMING RANGE',	'assets/images/category/1563713784.jpg',	1,	'2019-07-01 18:36:31',	'2019-07-01 18:36:31',	2),
(15,	'ACCESSORIES',	'assets/images/category/1564166826.jpg',	1,	'2019-07-01 18:40:55',	'2019-07-01 18:40:55',	2),
(16,	'TOYS',	'assets/images/category/1564224135.jpg',	1,	'2019-07-01 18:42:45',	'2019-07-01 18:42:45',	2),
(17,	'CLOTHING',	'assets/images/category/1564232119.jpg',	1,	'2019-07-01 18:44:32',	'2019-07-01 18:44:32',	2),
(18,	'BEDS',	'assets/images/category/1564233770.jpg',	1,	'2019-07-01 18:46:45',	'2019-07-01 18:46:45',	2),
(19,	'FOOD',	'assets/images/category/1564247498.jpg',	1,	'2019-07-02 17:47:14',	'2019-07-02 17:47:14',	3),
(20,	'SUPPLEMENTS',	'assets/images/category/1564242505.jpg',	1,	'2019-07-02 17:53:07',	'2019-07-02 17:53:07',	3),
(21,	'ACCESSORIES',	'assets/images/category/1564244662.jpg',	1,	'2019-07-02 18:00:59',	'2019-07-02 18:00:59',	3),
(22,	'TOYS',	'assets/images/category/1564245440.jpg',	1,	'2019-07-02 18:05:16',	'2019-07-02 18:05:16',	3),
(23,	'FOOD',	'assets/images/category/1564843667.jpg',	1,	'2019-07-02 18:14:14',	'2019-07-02 18:14:14',	4),
(24,	'SUPPLEMENTS',	'assets/images/category/1564847222.jpg',	1,	'2019-07-02 18:17:58',	'2019-07-02 18:17:58',	4),
(25,	'ACCESSORIES',	'assets/images/category/1564848258.jpg',	1,	'2019-07-02 18:20:08',	'2019-07-02 18:20:08',	4),
(26,	'FISH POTS',	'assets/images/category/1564848645.jpg',	1,	'2019-07-02 18:21:52',	'2019-07-02 18:21:52',	4),
(27,	'FOOD',	'assets/images/category/1564917517.jpg',	1,	'2019-07-02 18:30:25',	'2019-07-02 18:30:25',	5),
(28,	'SUPPLEMENTS',	'assets/images/category/1564920655.jpg',	1,	'2019-07-02 18:35:30',	'2019-07-02 18:35:30',	5),
(29,	'ACCESSORIES',	'assets/images/category/1564916879.jpg',	1,	'2019-07-02 18:42:15',	'2019-07-02 18:42:15',	5),
(30,	'FOOD',	'assets/images/category/1564919264.jpg',	1,	'2019-07-02 18:45:08',	'2019-07-02 18:45:08',	6),
(31,	'MISCELLANEOUS',	'assets/images/category/1564920174.jpg',	1,	'2019-07-02 18:50:40',	'2019-07-02 18:50:40',	6),
(33,	'PUPPY DIET',	'assets/images/category/1565443030.jpg',	0,	'2019-08-10 18:47:10',	'2019-08-10 18:47:10',	1);

DROP TABLE IF EXISTS `pro_color`;
CREATE TABLE `pro_color` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) NOT NULL,
  `Status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `pro_color` (`id`, `value`, `Status`) VALUES
(1,	'Red',	1),
(2,	'Black',	1),
(3,	'Blue',	1),
(4,	'White',	1),
(5,	'Grey',	1),
(6,	'Orange',	1),
(7,	'Pink',	1),
(8,	'Yellow',	1);

DROP TABLE IF EXISTS `pro_images`;
CREATE TABLE `pro_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `p_id` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `pro_size`;
CREATE TABLE `pro_size` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `pro_size` (`id`, `value`, `status`, `created_at`) VALUES
(1,	'XXS',	1,	''),
(2,	'XS',	1,	''),
(3,	'S',	1,	''),
(4,	'M',	1,	''),
(5,	'L',	1,	''),
(6,	'XL',	1,	''),
(7,	'XXL',	1,	''),
(8,	'XXL',	1,	''),
(9,	'6',	1,	''),
(10,	'8',	1,	''),
(11,	'10',	1,	''),
(12,	'12',	1,	''),
(13,	'14',	1,	''),
(14,	'16',	1,	''),
(15,	'18',	1,	''),
(16,	'20',	1,	''),
(17,	'22',	1,	''),
(18,	'24',	1,	''),
(19,	'26',	1,	''),
(20,	'28',	1,	''),
(21,	'30',	1,	''),
(22,	'32',	1,	'');

DROP TABLE IF EXISTS `pro_weight`;
CREATE TABLE `pro_weight` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `rating`;
CREATE TABLE `rating` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `pet_id` int(10) NOT NULL,
  `rating` text NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `review_product`;
CREATE TABLE `review_product` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `product_id` int(10) NOT NULL,
  `rating` text NOT NULL,
  `title` text NOT NULL,
  `description` text NOT NULL,
  `created_date` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sell_pets`;
CREATE TABLE `sell_pets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `pet_name` varchar(255) NOT NULL,
  `owner_name` varchar(255) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `breed_name` varchar(255) NOT NULL,
  `pet_price` int(11) NOT NULL,
  `pet_age` int(11) NOT NULL,
  `image` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `shopifyOrders`;
CREATE TABLE `shopifyOrders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `variant_id` varchar(255) NOT NULL,
  `quantity` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `price` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `zip` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sizee`;
CREATE TABLE `sizee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(250) NOT NULL,
  `last_name` varchar(250) NOT NULL,
  `email` varchar(250) NOT NULL,
  `mobile_no` varchar(15) NOT NULL,
  `password` varchar(100) NOT NULL,
  `address` text NOT NULL,
  `city` varchar(50) CHARACTER SET utf8 NOT NULL,
  `state` varchar(50) NOT NULL,
  `postcode` varchar(50) NOT NULL,
  `country` varchar(50) NOT NULL,
  `role_id` int(10) NOT NULL DEFAULT '2' COMMENT '2. user 1. admin',
  `created_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `social` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `active_status` varchar(50) NOT NULL,
  `social_id` varchar(50) NOT NULL,
  `notification` int(11) NOT NULL,
  `verification_code` varchar(50) NOT NULL,
  `door` varchar(50) NOT NULL,
  `os_type` varchar(50) NOT NULL,
  `device_id` varchar(250) NOT NULL,
  `profile_pic` varchar(250) NOT NULL,
  `last_notification` varchar(50) NOT NULL,
  `device_tokan` text NOT NULL,
  `lati` double NOT NULL,
  `longi` double NOT NULL,
  `country_code` int(10) NOT NULL,
  `status` int(10) NOT NULL DEFAULT '1' COMMENT '1. active 0. deactive',
  `comunity_id` int(10) NOT NULL DEFAULT '0',
  `verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0. Not Verified 1. Verified',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `mobile_no`, `password`, `address`, `city`, `state`, `postcode`, `country`, `role_id`, `created_stamp`, `social`, `type`, `active_status`, `social_id`, `notification`, `verification_code`, `door`, `os_type`, `device_id`, `profile_pic`, `last_notification`, `device_tokan`, `lati`, `longi`, `country_code`, `status`, `comunity_id`, `verified`) VALUES
(1,	'kavita yadav',	'',	'yadav1996kavita@gmail.com',	'',	'123456',	'',	'',	'',	'',	'',	2,	'2019-11-28 07:12:49',	'',	'',	'',	'',	0,	'',	'',	'',	'',	'',	'',	'',	0,	0,	0,	1,	0,	1),
(2,	'Shivangi Aday',	'',	'shivangiaday29@gmail.com',	'8989898989',	'123456',	'68, Vijay Nagar Square, Ratna Lok Colony, Indore, Madhya Pradesh 452010, India',	'Indore',	'Madhya Pradesh',	'452010',	'India',	2,	'2019-11-28 08:55:10',	'',	'',	'',	'',	0,	'',	'',	'',	'',	'assets/images/1574925680.jpg',	'',	'',	22.7498488,	75.8989112,	0,	1,	1,	1),
(3,	'rahul',	'',	'patidarrahul9752@gmail.com',	'9824586568',	'123456',	'68, Vijay Nagar Square, Ratna Lok Colony, Indore, Madhya Pradesh 452010, India',	'Indore',	'Madhya Pradesh',	'452010',	'India',	2,	'2019-11-28 07:35:08',	'',	'',	'',	'',	0,	'',	'',	'android',	'123456',	'',	'',	'csQYsjcnfaA:APA91bG40avN7FVNJTjOMrmKwrRod5kb9QZe74EE79PYdLazDswe2R7bD_Zx6CbeDsWOkJ3Il0GE-U-tPY_tXNyKTi5mDb0IlVaW3cJMCbIjgHSGB6SyHWO1p_-FAPIvjRn2ebJQSquw',	22.7498406,	75.8989009,	0,	1,	1,	1);

DROP TABLE IF EXISTS `views`;
CREATE TABLE `views` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `pet_id` int(10) NOT NULL,
  `view` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `weight`;
CREATE TABLE `weight` (
  `sno` int(10) NOT NULL AUTO_INCREMENT,
  `petid` varchar(10) NOT NULL,
  `usermail` varchar(100) NOT NULL,
  `long` varchar(100) NOT NULL,
  `weight` varchar(100) NOT NULL,
  `unit` varchar(100) NOT NULL,
  `date` varchar(100) NOT NULL,
  PRIMARY KEY (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `weightt`;
CREATE TABLE `weightt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- 2019-11-28 09:31:14
