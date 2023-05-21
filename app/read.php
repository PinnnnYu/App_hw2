<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型為 json，編碼 utf-8

	if(@$_GET["title"]!="")
	{
		$title=$_GET['title'];
	}
	else
	{
		$title="null";
	}
	if(@$_GET["limit"] != "")
	{
		$limit=$_GET["limit"];	
		//echo $limit;
	}
	else
	{
		$limit="5";
	}

	//------------讀取資料庫--------------//	
	$servername = "localhost";
	$username = "root";
	$password = "";
	$dbname = "note";
	$conn = new mysqli($servername, $username, $password, $dbname);

	//------------------------------------//
	// 建立資料庫連線
	$conn = new mysqli($servername, $username, $password, $dbname);
	// 確認資料是否正常連線
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	}

	$sql = "SELECT * FROM `note_test` order by no desc limit ".$limit;
	$result = $conn->query($sql);
	$messageArr = array();
	$dataarray= array();	
	//data
	
	if ($result->num_rows > 0) {
		// output data of each row
		while($row = $result->fetch_assoc()) {
		
			$content=$row["content"];
			$timestamp=$row["timestamp"];			
			$dataarray[] = $row;//將資料一筆一筆丟進dataarray
			
		}
	} 

	$conn->close();
	//------------------------------------------------
	//輸出結果	
	$messageArr["data"] = $dataarray;	
	//------------------------------------------------------
	//送入時間格式
    $messageArr["status"] = array();
	date_default_timezone_set('America/La_Paz');
	$today = date('Y-m-d\TH:i:sP');//RFC3339格式
	$datetime= array(
	"code" => "0",
	"message" => "Success",
	"datetime" => $today
	);	
	$messageArr["status"] = $datetime;	


if(!empty($_GET['title']))
{
	http_response_code(200);
    echo json_encode($messageArr);	
}
else
{		
	http_response_code(404);	
	$messageArr["data"] =[];//因為沒有帳號，我們就預設讓它為空陣列
	$messageArr["status"] = array();
	date_default_timezone_set('America/La_Paz');
	$today = date('Y-m-d\TH:i:sP');//RFC3339 format
	$datetime= array(
	"code" => "404",
	"message" => "Error Title is null",
	"datetime" => $today
	);	
	$messageArr["status"] = $datetime;
	echo json_encode($messageArr);	
}

?>