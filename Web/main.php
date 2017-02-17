<!DOCTYPE html>


<?php session_start();
    require_once "php/connection.php";

 ?>
<html>
    <head>
        <meta charset="utf-8">
        <title>Dashboard</title>

        <!-- Linking CSS -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="css/materialize.css">



        <link rel="stylesheet" href="css/color_scheme.css">
        <link rel="stylesheet" href="css/main.css">
    </head>

    <body>

        <nav class="nav-extended blue">
            <div class="nav-wrapper row">
                <img class="col s1 logo valign" src="navbar_icon.png"/>
                <div class="brand-logo col s4">Visio</div>
                 <div class="col s1">

                 </div>
                 <div class="input-field col s3 search blue lighten-1">
                   <i class="material-icons prefix">search</i>
                   <input id="icon_prefix" placeholder="Search here ..." type="text" class="validate">
                 </div>

                 <ul class="right hide-on-med-and-down tooltipped logout" data-position="left" data-delay="50" data-tooltip="Hello <?php echo $_SESSION['user'] ?>">
                    <li><a id="user_prof" href="php/logout.php"><i class="material-icons">perm_identity</i></a></li>
                  </ul>

           </div>
         </nav>

         <div class="row">

             <div class="file_names col s8">

                 <table class="highlight bordered">
                    <thead>
                      <tr>
                          <th data-field="name">Name</th>
                          <th data-field="owner">Owner</th>
                          <th data-field="size">Date Modified</th>
                      </tr>
                    </thead>
                   <tbody>
                    <?php
                        $query = "SELECT * FROM `user_data` WHERE `uname` = '".$_SESSION['user']."'";
                        $result = mysqli_query($conn, $query);
                        while($row = mysqli_fetch_assoc($result)){
                            echo "<tr id=\"".$row['id']."\" class=\"list\">
                              <td class=\"valign-wrapper\"> <i class=\" valign material-icons prefix\">description</i> ".$row['filename']."</td>
                              <td>".$row['uname']."</td>
                              <td>".$row['date']."</td>
                            </tr>";
                        }
                     ?>
                    </tbody>
                  </table>

             </div>

             <div class="col s3 center-align file_upload z-depth-2 right">
                 <h5 class="center-align">Upload new files</h5>
                 <form id="fileform" action="">
                     <div class="input-field">
                         <input placeholder="Enter File name" id="file_name" type="text" class="validate">
                     </div>
                     <div class="file-field input-field ">
                         <div class="btn blue darken-2">
                             <span>File</span>
                             <input type="file" name="fileField">
                         </div>
                         <div class="file-path-wrapper">
                             <input class="file-path validate" type="text">
                         </div>
                     </div>
                     <input type="submit" name="submit" value="Upload" class="waves-effect waves-light btn blue lighten-1" id="ff_btn">

                 </form>



             </div>

             <div class="fixed-action-btn">
                <a class="btn-floating btn-large red">
                  <i class="large material-icons">mode_edit</i>
                </a>
                <ul>
                  <li><a class="btn-floating green tooltipped" data-position="left" data-delay="50" data-tooltip="Text to Speech"><i class="material-icons">volume_up</i></a></li>
                  <li><a class="btn-floating yellow darken-1 tooltipped" data-position="left" data-delay="50" data-tooltip="Translate"><i class="material-icons">format_quote</i></a></li>
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

         </div>


    </body>


    <script type="text/javascript" src="js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="js/materialize.js"></script>

    <script type="text/javascript" src="js/main.js"></script>
    <script type="text/javascript" src="js/convert.js"></script>
</html>
