-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 18, 2023 lúc 02:40 PM
-- Phiên bản máy phục vụ: 10.4.25-MariaDB
-- Phiên bản PHP: 8.1.10

drop database quan_ly_to;
create database quan_ly_to;
use quan_ly_to;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";



CREATE TABLE `nhankhau` (
  `ID` int(11) NOT NULL,
  `HoTen` text NOT NULL,
  `BiDanh` text DEFAULT NULL,
  `NgaySinh` date NOT NULL,
  `NoiSinh` text NOT NULL,
  `GioiTinh` text NOT NULL,
  `NguyenQuan` text NOT NULL,
  `DanToc` text NOT NULL,
  `NoiThuongTru` text NOT NULL,
  `TonGiao` text DEFAULT NULL,
  `QuocTich` text DEFAULT 'Việt Nam',
  `DiaChiHienNay` text NOT NULL,
  `NgheNghiep` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sohokhau` (
  `ID` int(11) NOT NULL,
  `MaHoKhau` text NOT NULL,
  `DiaChi` text NOT NULL DEFAULT 'D9 401',
  `MaChuHo` int(12) NOT NULL,
  `NgayLap` date DEFAULT NULL,
  `NgayChuyenDi` date DEFAULT NULL,
  `LyDoChuyen` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `thanhviencuaho` (
  `idNhanKhau` int(11) NOT NULL,
  `idHoKhau` int(11) NOT NULL,
  `quanHeVoiChuHo` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `cccd` (
  `ID` int(11) NOT NULL,
  `idNhankhau` int(11) NOT NULL,
  `CCCD` varchar(15) NOT NULL,
  `NgayCap` date DEFAULT '2020-01-01',
  `NoiCap` text DEFAULT 'Hà Nội'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `khaitu` (
  `ID` int(11) NOT NULL,
  `idNguoiKhai` int(11) NOT NULL,
  `idNguoiChet` int(11) NOT NULL,
  `quanHeVoiNguoiChet` varchar(30) NOT NULL,
  `ngayKhai` date NOT NULL,
  `ngayChet` date NOT NULL,
  `lyDoChet` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `tamtru` (
  `ID` int(11) NOT NULL,
  `idNhankhau` int(11) NOT NULL,
  `diaChiTamTru` text NOT NULL,
  `tuNgay` date NOT NULL,
  `denNgay` date NOT NULL,
  `lido` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tamvang` (
  `ID` int(11) NOT NULL,
  `idNhankhau` int(11) NOT NULL,
  `noiTamTru` text NOT NULL,
  `tuNgay` date NOT NULL,
  `denNgay` date NOT NULL,
  `lydo` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `role` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `cosovatchat` (
  `MaDoDung` int(11) NOT NULL,
  `TenDoDung` varchar(30) NOT NULL,
  `TinhTrang` varchar(30) NOT NULL,
  `LoaiDoDung` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `lichhoatdong` (
  `MaHoatDong` int(11) NOT NULL,
  `TenHoatDong` text NOT NULL,
  `ThoiGianBatDau` datetime NOT NULL,
  `ThoiGianKetThuc` datetime NOT NULL,
  `DuocDuyet` varchar(15) NOT NULL,
  `ThoiGianTao` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `MaNguoiTao` int(12) NOT NULL,
  `tenPhong` varchar(255) NOT NULL,
  `thuPhi` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `phong`(
  `ID` int(11) ,
  `tenPhong` varchar(255) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
ALTER TABLE `phong`
  ADD PRIMARY KEY (`ID`);

ALTER TABLE `nhankhau`
  ADD PRIMARY KEY (`ID`);

ALTER TABLE `sohokhau`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `MaChuHo` (`MaChuHo`);

ALTER TABLE `thanhviencuaho`
  ADD PRIMARY KEY (`idNhanKhau`,`idHoKhau`),
  ADD KEY `idHoKhau` (`idHoKhau`),
  ADD KEY `idNhanKhau` (`idNhanKhau`);

ALTER TABLE `cccd`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `idNhankhau_2` (`idNhankhau`),
  ADD KEY `idNhankhau` (`idNhankhau`);

ALTER TABLE `khaitu`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `idNguoiChet` (`idNguoiChet`),
  ADD KEY `idNguoiKhai` (`idNguoiKhai`);

ALTER TABLE `tamtru`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `idNhankhau` (`idNhankhau`);

ALTER TABLE `tamvang`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `idNhankhau` (`idNhankhau`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

ALTER TABLE `cosovatchat`
  ADD PRIMARY KEY (`MaDoDung`);

ALTER TABLE `lichhoatdong`
  ADD PRIMARY KEY (`MaHoatDong`),
  ADD KEY `MaNguoiTao` (`MaNguoiTao`);


ALTER TABLE `cccd`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;


ALTER TABLE `phong`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `cosovatchat`
  MODIFY `MaDoDung` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `nhankhau`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `sohokhau`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `tamtru`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `tamvang`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `khaitu`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `lichhoatdong`
  MODIFY `MaHoatDong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;


ALTER TABLE `cccd`
  ADD CONSTRAINT `cccd_ibfk_1` FOREIGN KEY (`idNhankhau`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `hoatdong_cosovatchat`
--

-- Các ràng buộc cho bảng `khaitu`
--
ALTER TABLE `khaitu`
  ADD CONSTRAINT `khaitu_ibfk_1` FOREIGN KEY (`idNguoiKhai`) REFERENCES `nhankhau` (`ID`),
  ADD CONSTRAINT `khaitu_ibfk_2` FOREIGN KEY (`idNguoiChet`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `lichhoatdong`
--
ALTER TABLE `lichhoatdong`
  ADD CONSTRAINT `lichhoatdong_ibfk_1` FOREIGN KEY (`MaNguoiTao`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `sohokhau`
--
ALTER TABLE `sohokhau`
  ADD CONSTRAINT `sohokhau_ibfk_1` FOREIGN KEY (`MaChuHo`) REFERENCES `nhankhau` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `tamtru`
--
ALTER TABLE `tamtru`
  ADD CONSTRAINT `tamtru_ibfk_1` FOREIGN KEY (`idNhankhau`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `tamvang`
--
ALTER TABLE `tamvang`
  ADD CONSTRAINT `tamvang_ibfk_1` FOREIGN KEY (`idNhankhau`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `thanhviencuaho`
--
ALTER TABLE `thanhviencuaho`
  ADD CONSTRAINT `thanhviencuaho_ibfk_6` FOREIGN KEY (`idNhanKhau`) REFERENCES `nhankhau` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `thanhviencuaho_ibfk_7` FOREIGN KEY (`idHoKhau`) REFERENCES `sohokhau` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

INSERT INTO `nhankhau` (`ID`, `HoTen`, `BiDanh`, `NgaySinh`, `NoiSinh`, `GioiTinh`, `NguyenQuan`, `DanToc`, `NoiThuongTru`, `TonGiao`, `QuocTich`, `DiaChiHienNay`, `NgheNghiep`) VALUES
(1,'Hoàng Danh Quân', 'Spidartist', '2002-10-03', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Sinh viên'),
(2,'Hoàng Như Nghĩa', 'khangbn', '2002-11-22', 'Bắc Ninh', 'Nam', 'Bắc Ninh', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Sinh viên'),
(3,'Hoàng Văn Bắc', 'bac', '2002-10-23', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Giáo viên'),
(4,'Phùng Trung Kiên', 'kien', '2002-12-13', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Kĩ sư'),
(5,'Nguyễn Hùng Tiến', 'tienngu', '2002-01-23', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Thất nghiệp'),
(6,'Trần Tiến Ngọc', 'ngoc', '2002-11-11', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Game thủ'),
(7,'Phạm Minh Quang', 'quang', '2002-12-11', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Sinh viên'),
(8,'Lê Anh Tuấn', 'tuan', '2002-01-23', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Sinh viên'),
(9,'Vũ Anh Quân', 'quan', '2002-03-03', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Marketing'),
(10,'Trần Quốc Anh Quân', 'quazz', '2002-05-13', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Làm ruộng'),
(11,'Trần Minh Tuấn', 'tuana', '2002-10-05', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Đa cấp'),
(12,'Phạm Quang Nhật', 'nhatngu', '2002-10-07', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Game thủ'),
(13,'Phạm Đức Thắng', 'thangngu', '2002-10-09', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Cầu thủ'),
(14,'Nguyễn Tuấn Thành', 'thanhdan', '2002-13-02', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Bán hàng'),
(15,'Hoàng Anh Quân', 'dquaz', '2002-13-01', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Bất động sản'),
(16,'Văn Đăng Huy', 'huy', '2002-11-12', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Học sinh'),
(17,'Trần Nhật Hóa', 'thaydayhoa', '1999-11-23', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Sinh viên'),
(18,'Nguyễn Quang Trường', 'truong', '2002-11-13', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Game thủ'),
(19,'Trần Ái Quốc', 'quoc', '2002-12-12', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Vận động viên'),
(20,'Nguyễn An Nam', 'nam', '2002-10-10', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Học sinh'),
(21,'Hoàng Hải Nam', 'namz', '2002-10-09', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Sinh viên'),
(22,'Umezawa Ayumi', 'ayumi', '1977-01-01', 'Nhật Bản', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Nhật Bản', 'Hà Nội', 'Giáo viên'),
(23,'Lương Phương Liên', 'liensensei', '1977-10-03', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Giáo sư'),
(24,'Phạm Bích Phương', 'phuong30', '1930-10-03', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Giáo viên'),
(25,'Ngô Lan Anh', 'lananh', '2000-10-02', 'Hà Nội', 'Nam', 'Hà Nội', 'Kinh', 'Hà Nội', 'Không', 'Việt Nam', 'Hà Nội', 'Cảnh sát');


INSERT INTO `sohokhau` (`ID`, `MaHoKhau`, `DiaChi`, `MaChuHo`, `NgayLap`, `NgayChuyenDi`, `LyDoChuyen`) VALUES
(1, '001232324', 'Cầu Giấy - Hà Nội', 4, '2013-01-02', NULL, NULL),
(2, '001232325', 'Hai Bà Tưng - Hà Nội', 2, '2013-01-02', NULL, NULL);

INSERT INTO `cosovatchat` (`MaDoDung`, `LoaiDoDung`, `TenDoDung`, `TinhTrang`) VALUES
(1, 'Quạt điện','Quạt điện Vinfast', "Còn dùng được"),
(2, 'Quạt điện','Quạt điện Thống nhất', "Còn dùng được"),
(3, 'Quạt điện','Quạt điện Vinfast', "Còn dùng được"),
(4, 'Quạt điện','Quạt điện Vinfast', "Còn dùng được"),
(5, 'Quạt điện','Quạt điện Vinfast', "Còn dùng được"),
(6, 'Quạt điện','Quạt điện Vinfast', "Còn dùng được"),
(7, 'Máy chiếu','Máy chiếu Vinfast', "Còn dùng được"),
(8, 'Máy chiếu','Máy chiếu Konica', "Hỏng"),
(9, 'Máy tính','Máy tính Intel', "Còn dùng được"),
(10, 'Máy tính','Máy tính MSI', "Hỏng"),
(11, 'Bàn nhựa','Bàn nhựa Thống Nhất', "Còn dùng được"),
(12, 'Ghế nhựa','Ghế nhựa Thống Nhất', "Còn dùng được"),
(13, 'Ghế nhựa','Ghế nhựa Thống Nhất', "Còn dùng được");

INSERT INTO `phong` (`ID`, `tenPhong`) VALUES
(1, 'Hội trường'),
(2, 'Phòng chức năng 1'),
(3, 'Phòng bóng bàn'),
(4, 'Nhà thi đấu');


INSERT INTO `cccd` (`ID`, `idNhankhau`, `CCCD`, `NgayCap`, `NoiCap`) VALUES
(1, 1, '001202005585', '2020-01-01', 'Hà Nội'),
(2, 2, '001202005586', '0000-00-00', 'Hà nội'),
(3, 3, '001202005587', '2020-01-01', 'Hà Nội'),
(4, 4, '001202005588', '2020-01-01', 'Hà Nội'),
(5, 5, '001202005589', '2020-01-01', 'Hà Nội'),
(6, 6, '001202005510', '2020-01-01', 'Hà Nội'),
(7, 7, '001202005511', '2020-01-01', 'Hà Nội'),
(8, 8, '001202005512', '2020-01-01', 'Hà Nội'),
(9, 9, '001202005522', '2020-01-01', 'Hà Nội'),
(10, 10, '001202005533', '2020-01-01', 'Hà Nội');

INSERT INTO `thanhviencuaho` (`idNhanKhau`, `idHoKhau`, `quanHeVoiChuHo`) VALUES
(5, 1, 'Con trai'),
(6, 1, 'Con gái');

INSERT INTO `user` (`userId`, `username`, `password`, `role`) VALUES
(1, 'admin', '123', 'totruong'),
(2, 'admin2', '123', 'canbo'),
(3, 'cocc', '123', 'canbo');

COMMIT;

