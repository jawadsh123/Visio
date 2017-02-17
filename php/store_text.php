<?php

    require_once "connection.php";
    session_start();


    $conv_str = $_POST["text"];
    $file_name = $_POST["name"];


    // $file_path = "../user_files/" . $_SESSION['user'] ."/". $file_name ;
    $going_back = "../";
    $file_path = "user_files/".$_SESSION['user']."/".$file_name."" ;
    echo $file_path;
    $query = "INSERT into user_data (filename, file_loc, uname) VALUES ('$file_name', '$file_path', '$_SESSION[user]')";
    echo $file_path;

    $result = mysqli_query($conn, $query);

    $file_path = $going_back . $file_path;

    $file = fopen($file_path . ".txt", "w");


    // while (!feof($conv_str)) {
    //    $line = fgets($conv_str);
    //    echo $line;
    //    echo "hey";
    // }

    $data = explode("\n", $conv_str);

    foreach($data as $value){
        fwrite($file, $value . PHP_EOL);
    }

    fclose($file);

    // echo $conv_str;

 ?>
