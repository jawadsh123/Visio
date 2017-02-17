<?php
if (isset($_POST['submitted']))
{
	include 'connection.php';
	$uname = $_POST['uname'];
	$fname = $_POST['fname'];
	$email = $_POST['email'];
	$pwd   = $_POST['pwd'];
	$sqlinsert = "INSERT INTO user(uname,fname,email,pwd) VALUES ('$uname','$fname','$email','$pwd')";
	if(!mysqli_query($conn,$sqlinsert))
	{
		die("Data cannot be inserted");
	}

	header("Location: ../index.html");
}
?>
