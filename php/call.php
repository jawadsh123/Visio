<?php

$api_key = "AIzaSyDM7gkaxXg3L4yFSpUBYAkFShV-27cuNKg";
$cvurl = "https://vision.googleapis.com/v1/images:annotate?key=" . $api_key;
$type = "TEXT_DETECTION";


//Did they upload a file...
if($_FILES['file']['name'])
{
	//if no errors...
	if(!$_FILES['file']['error'])
	{
		$valid_file = true;
		//can't be larger than ~4 MB
		if($_FILES['file']['size'] > (4024000))
		{
			$valid_file = false;
			die('Your file\'s size is too large.');
		}


		//if the file has passed the test
		if($valid_file)
		{
			//convert it to base64
			$fname = $_FILES['file']['tmp_name'];
			$data = file_get_contents($fname);
			$base64 = base64_encode($data);
			//Create this JSON
			$r_json ='{
				"requests": [
					{
						"image": {
							"content": '.$base64.'
						},
						"features": [
							{
								"type": '.$type.'
							}
						]
					}
				]
			}';


			$curl = curl_init();
			curl_setopt($curl, CURLOPT_URL, $cvurl);
			curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
			curl_setopt($curl, CURLOPT_HTTPHEADER,
				array("Content-type: application/json"));
			curl_setopt($curl, CURLOPT_POST, true);
			curl_setopt($curl, CURLOPT_POSTFIELDS, $r_json);
            curl_setopt($curl, CURLOPT_CONNECTTIMEOUT ,0);
            curl_setopt($curl, CURLOPT_TIMEOUT, 400);
			$json_response = curl_exec($curl);
			$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
			curl_close($curl);


			if ( $status != 200 ) {
			    die("Error: $cvurl failed status $status" );
			}

			echo "<pre>";
			echo $json_response;
			echo "</pre>";
		}
	}
	//if there is an error...
	else
	{
		//set that to be the returned message
		echo "Error";
		 die('Drror:  '.$_FILES['file']['error']);
	}
}
?>
