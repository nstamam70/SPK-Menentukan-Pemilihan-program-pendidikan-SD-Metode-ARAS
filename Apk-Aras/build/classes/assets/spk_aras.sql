-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 18, 2025 at 08:22 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spk_aras`
--

-- --------------------------------------------------------

--
-- Table structure for table `alternatif`
--

CREATE TABLE `alternatif` (
  `id_alternatif` int NOT NULL,
  `kode_alternatif` varchar(50) DEFAULT NULL,
  `nama_alternatif` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `alternatif`
--

INSERT INTO `alternatif` (`id_alternatif`, `kode_alternatif`, `nama_alternatif`) VALUES
(1, 'A001', 'Literasi Pagi'),
(2, 'A002', 'Sholat Berjamaah'),
(3, 'A003', 'Jumat Bersih'),
(4, 'A004', '5S'),
(5, 'A005', 'Bakti Sosial');

-- --------------------------------------------------------

--
-- Table structure for table `aras`
--

CREATE TABLE `aras` (
  `id_aras` int NOT NULL,
  `id_alternatif` int DEFAULT NULL,
  `id_penilaian` int DEFAULT NULL,
  `id_kriteria` int DEFAULT NULL,
  `nilai_normalisasi` float DEFAULT NULL,
  `nilai_si` float DEFAULT NULL,
  `id_hasil` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `aras`
--

INSERT INTO `aras` (`id_aras`, `id_alternatif`, `id_penilaian`, `id_kriteria`, `nilai_normalisasi`, `nilai_si`, `id_hasil`) VALUES
(351, 1, 1, 1, 0.193548, NULL, NULL),
(352, 1, 2, 2, 0.18018, NULL, NULL),
(353, 2, 3, 1, 0.210753, NULL, NULL),
(354, 2, 4, 2, 0.207207, NULL, NULL),
(355, 3, 5, 1, 0.172043, NULL, NULL),
(356, 3, 6, 2, 0.198198, NULL, NULL),
(357, 4, 7, 1, 0.165591, NULL, NULL),
(358, 4, 9, 2, 0.0990991, NULL, NULL),
(359, 5, 10, 1, 0.0473118, NULL, NULL),
(360, 5, 11, 2, 0.108108, NULL, NULL),
(361, 1, 15, 3, 0.0829384, NULL, NULL),
(362, 2, 16, 3, 0.0995261, NULL, NULL),
(363, 3, 17, 3, 0.0710901, NULL, NULL),
(364, 4, 18, 3, 0.248815, NULL, NULL),
(365, 5, 19, 3, 0.248815, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `hasil_akhir`
--

CREATE TABLE `hasil_akhir` (
  `id_hasil` int NOT NULL,
  `id_alternatif` int DEFAULT NULL,
  `nilai_ki` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kriteria`
--

CREATE TABLE `kriteria` (
  `id_kriteria` int NOT NULL,
  `kode_kriteria` varchar(50) DEFAULT NULL,
  `nama_kriteria` varchar(100) DEFAULT NULL,
  `tipe_kriteria` varchar(20) DEFAULT NULL,
  `bobot_kriteria` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `kriteria`
--

INSERT INTO `kriteria` (`id_kriteria`, `kode_kriteria`, `nama_kriteria`, `tipe_kriteria`, `bobot_kriteria`) VALUES
(1, 'C001', 'Partisipasi Siswa', 'Benefit', 0.2),
(2, 'C002', 'Dukungan Guru', 'Benefit', 0.15),
(3, 'C003', 'Dampak Karakter', 'Cost', 0.25),
(4, 'C004', 'Kemudahan Pelaksanaan', 'Benefit', 0.15),
(5, 'C005', 'Dukungan Orang Tua', 'Benefit', 0.15),
(6, 'C006', 'Keberlanjutan Program', 'Benefit', 0.1);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_alternatif`
--

CREATE TABLE `nilai_alternatif` (
  `id_penilaian` int NOT NULL,
  `kode` varchar(50) DEFAULT NULL,
  `id_alternatif` int DEFAULT NULL,
  `id_kriteria` int DEFAULT NULL,
  `nilai` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_alternatif`
--

INSERT INTO `nilai_alternatif` (`id_penilaian`, `kode`, `id_alternatif`, `id_kriteria`, `nilai`) VALUES
(1, 'N001', 1, 1, 90),
(2, 'N002', 1, 2, 20),
(3, 'N003', 2, 1, 98),
(4, 'N004', 2, 2, 23),
(5, 'N005', 3, 1, 80),
(6, 'N006', 3, 2, 22),
(7, 'N007', 4, 1, 77),
(9, 'N008', 4, 2, 11),
(10, 'N009', 5, 1, 22),
(11, 'N010', 5, 2, 12),
(15, 'N011', 1, 3, 6),
(16, 'N012', 2, 3, 5),
(17, 'N013', 3, 3, 7),
(18, 'N014', 4, 3, 2),
(19, 'N015', 5, 3, 2);

-- --------------------------------------------------------

--
-- Table structure for table `solusi_ideal`
--

CREATE TABLE `solusi_ideal` (
  `id_kriteria` int NOT NULL,
  `nilai_a0` float DEFAULT NULL,
  `a0_ternormalisasi` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `solusi_ideal`
--

INSERT INTO `solusi_ideal` (`id_kriteria`, `nilai_a0`, `a0_ternormalisasi`) VALUES
(1, 98, 0.210752688172043),
(2, 23, 0.2072072072072072),
(3, 2, 0.24881516587677727);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_users` int NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_users`, `username`, `password`, `email`) VALUES
(1, 'admin', '21232f297a57a5a743894a0e4a801fc3', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alternatif`
--
ALTER TABLE `alternatif`
  ADD PRIMARY KEY (`id_alternatif`);

--
-- Indexes for table `aras`
--
ALTER TABLE `aras`
  ADD PRIMARY KEY (`id_aras`),
  ADD KEY `fk_aras_siswa` (`id_alternatif`),
  ADD KEY `fk_aras_penilaian` (`id_penilaian`),
  ADD KEY `fk_aras_kriteria` (`id_kriteria`),
  ADD KEY `fk_aras_hasil` (`id_hasil`);

--
-- Indexes for table `hasil_akhir`
--
ALTER TABLE `hasil_akhir`
  ADD PRIMARY KEY (`id_hasil`),
  ADD KEY `fk_hasil_akhir_alternatif` (`id_alternatif`);

--
-- Indexes for table `kriteria`
--
ALTER TABLE `kriteria`
  ADD PRIMARY KEY (`id_kriteria`);

--
-- Indexes for table `nilai_alternatif`
--
ALTER TABLE `nilai_alternatif`
  ADD PRIMARY KEY (`id_penilaian`),
  ADD KEY `fk_nilai_alternatif_alternatif` (`id_alternatif`),
  ADD KEY `fk_nilai_alternatif_kriteria` (`id_kriteria`);

--
-- Indexes for table `solusi_ideal`
--
ALTER TABLE `solusi_ideal`
  ADD PRIMARY KEY (`id_kriteria`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_users`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `alternatif`
--
ALTER TABLE `alternatif`
  MODIFY `id_alternatif` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `aras`
--
ALTER TABLE `aras`
  MODIFY `id_aras` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=366;

--
-- AUTO_INCREMENT for table `hasil_akhir`
--
ALTER TABLE `hasil_akhir`
  MODIFY `id_hasil` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kriteria`
--
ALTER TABLE `kriteria`
  MODIFY `id_kriteria` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `nilai_alternatif`
--
ALTER TABLE `nilai_alternatif`
  MODIFY `id_penilaian` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_users` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `aras`
--
ALTER TABLE `aras`
  ADD CONSTRAINT `fk_aras_hasil` FOREIGN KEY (`id_hasil`) REFERENCES `hasil_akhir` (`id_hasil`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_aras_kriteria` FOREIGN KEY (`id_kriteria`) REFERENCES `kriteria` (`id_kriteria`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_aras_penilaian` FOREIGN KEY (`id_penilaian`) REFERENCES `nilai_alternatif` (`id_penilaian`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_aras_siswa` FOREIGN KEY (`id_alternatif`) REFERENCES `alternatif` (`id_alternatif`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hasil_akhir`
--
ALTER TABLE `hasil_akhir`
  ADD CONSTRAINT `fk_hasil_akhir_alternatif` FOREIGN KEY (`id_alternatif`) REFERENCES `alternatif` (`id_alternatif`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `nilai_alternatif`
--
ALTER TABLE `nilai_alternatif`
  ADD CONSTRAINT `fk_nilai_alternatif_alternatif` FOREIGN KEY (`id_alternatif`) REFERENCES `alternatif` (`id_alternatif`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_nilai_alternatif_kriteria` FOREIGN KEY (`id_kriteria`) REFERENCES `kriteria` (`id_kriteria`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `solusi_ideal`
--
ALTER TABLE `solusi_ideal`
  ADD CONSTRAINT `fk_solusi_ideal_kriteria` FOREIGN KEY (`id_kriteria`) REFERENCES `kriteria` (`id_kriteria`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
