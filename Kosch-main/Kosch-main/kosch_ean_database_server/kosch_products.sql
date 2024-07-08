CREATE TABLE `kosch_products` (
  `ean_number` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `trash_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

ALTER TABLE `kosch_products`
  ADD PRIMARY KEY (`ean_number`(64));
COMMIT;