$(function(){

    var text = $("textarea").val();

    $(".tts").on("click", function() {

        responsiveVoice.speak(text);

        $(".overlay").toggleClass("overlay_active");

        $(".overlay div").toggleClass("scale-out");

        console.log("hey");

    });

    $(".overlay div").on("click", function() {

        responsiveVoice.cancel();



        $(".overlay div").toggleClass("scale-out");

        setTimeout(function() {
            $(".overlay").toggleClass("overlay_active");
        }, 300)

    });


    window.showIt = function(response) {
      if(response.data)
          document.getElementById("translation").innerHTML = response.data.translations[0].translatedText;
          else
          alert("Error:"+response.error.message);
    }

    window.translate = function(lang){
        var text = escape(document.getElementById("text").innerHTML);
        var key="AIzaSyDM7gkaxXg3L4yFSpUBYAkFShV-27cuNKg";
        var source="en";
        var dest= lang;

        var url = 'https://www.googleapis.com/language/translate/v2?';
        url += 'key='+key+'&source='+source+'&target='+dest+'&callback=showIt&q='+text;
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = url;
        document.getElementsByTagName('head')[0].appendChild(script);
    }


    $(".translate").on("click",function() {

        console.log("hey");


    });

    $(".langu").on("change", function() {
        var value = $('.langu').find(":selected").text()
        switch (value) {
            case "Hindi":
                translate("hi");
                break;
            case "French":
                translate("fr");
                break;
            case "German":
                translate("de");
                break;
            default:

        }
    });

    $('select').material_select();

    console.log("hey");

});
