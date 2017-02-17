<!DOCTYPE html>


<?php session_start();
    require_once "php/connection.php";

 ?>
<html>
    <head>
        <meta charset="utf-8">
        <title>File Viewer</title>

        <!-- Linking CSS -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons|Lato" rel="stylesheet">
        <link rel="stylesheet" href="css/materialize.css">



        <link rel="stylesheet" href="css/color_scheme.css">
        <link rel="stylesheet" href="css/text_viewer.css">
        <!-- <link rel="stylesheet" href="css/main.css"> -->
    </head>

    <body>

        <nav class="nav-extended blue">
            <div class="nav-wrapper row">
                <img class="col s1 logo valign" src="navbar_icon.png"/>
                <div class="brand-logo col s4">Visio</div>
                 <div class="col s1">

                 </div>
                 <div class="input-field col s3 search blue lighten-1 ">
                   <i class="material-icons prefix">search</i>
                   <input id="icon_prefix" placeholder="Search here ..." type="text" c  lass="validate">
                 </div>

                 <ul class="right hide-on-med-and-down tooltipped logout" data-position="left" data-delay="50" data-tooltip="Hello <?php echo $_SESSION['user'] ?>">
                    <li><a id="user_prof" href="php/logout.php"><i class="material-icons">perm_identity</i></a></li>
                  </ul>

           </div>
         </nav>

         <?php

            $user = $_SESSION["user"];
            $id = $_GET["id"];

            $query = "SELECT * FROM user_data WHERE id = ". $id ."";

            $result = mysqli_query($conn, $query);

            $row = mysqli_fetch_assoc($result);
            $file_path = $row['file_loc'] . ".txt";



          ?>

         <textarea id="text" style="font-size: 2em">
             <?php

             $file = fopen($file_path, "r");

             foreach(file($file_path) as $line) {
                echo $line. "\n";
             }

             fclose($file);

              ?>
         </textarea>


         <div class="tranlation_panel row">
             <div class="col s2"></div>
             <div class="input-field col s3">
                <select>
                  <option value="" disabled selected>English</option>
                </select>
              </div>
             <div class="col s1 lang_mid center-align"><i class="material-icons medium ">translate</i></div>
             <div class="input-field col s3">
                <select class="langu">
                  <option value="" disabled selected>Choose your Language</option>
                  <option value="1" id="1">Hindi</option>
                  <option value="2" id="2">French</option>
                  <option value="3" id="3">German</option>
                </select>
              </div>
         </div>

         <div class="card-panel blue lighten-4" id="translation"></div>

         <div class="fixed-action-btn">
            <a class="btn-floating btn-large red">
              <i class="large material-icons">mode_edit</i>
            </a>
            <ul>
              <li><a class="btn-floating green tooltipped tts" data-position="left" data-delay="50" data-tooltip="Text to Speech"><i class="material-icons">hearing</i></a></li>
              <li><a class="btn-floating yellow darken-1 tooltipped translate" data-position="left" data-delay="50" data-tooltip="Translate"><i class="material-icons">format_quote</i></a></li>
              <li>
                  <div class="file-field input-field">
                      <div class="btn-floating blue tooltipped" data-position="left" data-delay="50" data-tooltip="Upload">
                      <i class="material-icons">publish</i>
                      <input type="file">
                  </div>
                  </div>
              </li>
            </ul>
         </div>

         <div class="overlay valign-wrapper row">
             <div class="btn-floating btn-large waves-effect waves-light red cancel valign scale-transition scale-out"><i class="material-icons ">power_settings_new</i></div>
         </div>




    </body>


    <script type="text/javascript" src="js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="js/materialize.js"></script>
    <script src="//cloud.tinymce.com/stable/tinymce.min.js"></script>
    <script>tinymce.init({ selector:'textarea' });</script>
    <script src="https://code.responsivevoice.org/responsivevoice.js"></script>

    <!-- <script type="text/javascript" src="js/main.js"></script> -->
    <!-- <script type="text/javascript" src="js/convert.js"></script> -->
    <script type="text/javascript" src="js/text_viewer.js"></script>


</html>
