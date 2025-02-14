<?php

header("Content-Type: application/json");

if ($mysqli = new mysqli("mysql41.mydevil.net", "m1227_kosch_user", "Doe9breEgHKE64oBf7z2", "m1227_kosch")) {
	$mysqli->query("SET NAMES 'utf8'");
}

$data = new stdClass();
$data->success = false;

$ean = isset($_REQUEST["ean"]) ? $_REQUEST["ean"] : null;
$trashId = isset($_REQUEST["trashId"]) ? ($_REQUEST["trashId"]) : 0;

if ($ean != null && is_numeric($ean) && is_numeric($trashId)) {
	if ($trashId) {
		$stmt = $mysqli->prepare("INSERT INTO kosch_products (ean_number, trash_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE trash_id = ?");
		$stmt->bind_param("sii", $ean, $trashId, $trashId);
		$stmt->execute();
		$stmt->close();
		$data->trashId = 0;
	}
	$stmt = $mysqli->prepare("SELECT trash_id FROM kosch_products WHERE ean_number = ?");
	$stmt->bind_param("s", $ean);
	$stmt->execute();
	$result = $stmt->get_result();
	if ($row = $result->fetch_assoc()) {
		$data->trashId = $row["trash_id"];
		$data->success = $data->trashId == 0 ? false : true;
		$stmt->close();
	} else {
		$trashId = 0;
		$stmt = $mysqli->prepare("INSERT INTO kosch_products (ean_number, trash_id) VALUES (?, ?)");
		$stmt->bind_param("si", $ean, $trashId);
		$stmt->execute();
		$stmt->close();
		$data->trashId = 0;
	}
}

echo json_encode($data);

?>