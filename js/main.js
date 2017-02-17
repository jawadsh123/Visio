$(function() {

    $(".list").on("dblclick", function() {


        var id = $(this).attr("id");

        window.location.href = "text_viewer.php?id=" + id;

    });

    console.log("hey");



});
