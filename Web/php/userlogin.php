<?php
if(isset($_POST['submitted']))
{
	session_start();
	include 'connection.php';
	$uname = $_POST['uname'];
	$upwd  = $_POST['pwd'];
	$sqlquery = "SELECT * FROM user WHERE uname = '$uname' ";
	$result = mysqli_query($conn, $sqlquery);
	$count  = mysqli_num_rows($result);
	if($count==1)
	{
		$query2 = "SELECT pwd FROM user WHERE uname = '$uname' ";
		$result2 = mysqli_query($conn, $query2);
		$row = mysqli_fetch_assoc($result2);
		if($row["pwd"] == $upwd)
		{
			$_SESSION['user'] = $uname;
			echo "done";
		}
		else
			{
				echo "Invalid Password!!";
				header("Location: ../index.html");
				return FALSE;
			}
	}
	else
	{
		echo "Invalid Username!!";
		header("Location: ../index.html");
		return FALSE;
	}

	header("Location: ../main.php");
}
?>
