<?php
$servername="localhost";
$username="root";
$password="12345";
$dbname="visio";
$conn=mysqli_connect("$servername","$username","$password","$dbname");
if(!$conn)
{
	die("Connection failure".mysqli_connect_error);
}
else
	// echo "Connected Successfully";
?>
