-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 25 Kwi 2023, 23:43
-- Wersja serwera: 10.4.27-MariaDB
-- Wersja PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `todo_app`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tasks`
--

CREATE TABLE `tasks` (
  `taskid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `datecreated` date NOT NULL,
  `description` varchar(200) NOT NULL,
  `taskname` varchar(50) NOT NULL,
  `isImportant` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `tasks`
--

INSERT INTO `tasks` (`taskid`, `userid`, `datecreated`, `description`, `taskname`, `isImportant`) VALUES
(1, 1, '2023-04-11', 'zupa, kotlet, ryba', 'Gotowanie', 1),
(3, 3, '2023-04-11', 'ściany, sufit, barierki', 'Malowanie', 0),
(4, 1, '2023-04-11', 'na polski, matme i geografie', 'Sprawozdania', 0),
(7, 1, '2023-04-14', 'wykład algorytmy i struktury danych', 'Wykład', 0),
(8, 1, '2023-04-18', 'opis nowego', 'nowe', 0),
(23, 2, '2023-04-20', 'opis mojego nowego zadania', 'nowe zadanie', 0),
(28, 12, '2023-04-20', 'opis www', 'www', 0),
(36, 1, '2023-04-21', 'Skończyć projekt z JAVY :)', 'Projekt', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `userid` int(11) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`userid`, `firstname`, `lastname`, `username`, `password`) VALUES
(1, 'Pawel', 'Buczek', 'pawelb123', 'pawel'),
(2, 'adam', 'nowak', 'anowak', 'haslo123'),
(3, 'Andrzej', 'Koparka', 'kopara', 'haslo123456789'),
(6, 'aaa', 'bbb', 'ccc', 'ddd'),
(11, 'a', 'a', 'a', 'a'),
(12, 'b', 'b', 'b', 'b');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`taskid`),
  ADD KEY `tasks_ibfk_1` (`userid`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userid`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `tasks`
--
ALTER TABLE `tasks`
  MODIFY `taskid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `userid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `tasks`
--
ALTER TABLE `tasks`
  ADD CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
