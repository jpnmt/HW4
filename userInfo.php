<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>userInfo</title>

    <link href="../../css/styles.css" rel="stylesheet" type="text/css">
    <link href="../../css/backgrounds.css" rel="stylesheet" type="text/css"/>

</head>
<body>

<div class = "reg">

<form class="fields" id="myForm" method="post" novalidate
    <form action=http://localhost:63342/Main2.java" onsubmit="submitForm(); return false;">
    <!-- all the fields that make up the form -->
    <table>
    <tr>
        <td><div id="colorDiv" class="form-group">
                <label for="color-selection"> Please choose a 1st color</label>
                <select name="color-selection" id="color-selection" value="<?php echo $colorSel; ?>">

                    <option value="NA"></option>
                    <option value="RD">Red</option>
                    <option value="OG">Orange</option>
                    <option value="YW">Yellow</option>
                    <option value="GN">Green</option>
                    <option value="BE">Blue</option>
                    <option value="IN">Indigo</option>
                    <option value="VI">Violet</option>
                    <option value="BL">Black</option>
                </select>
            </div></td>

        <td><div id="colorDiv2" class="form-group">
            <label for="color-selection2"> Please choose a 2nd color</label>
            <select name="color-selection2" id="color-selection2" value="<?php echo $colorSel2; ?>">

                <option value="NA"></option>
                <option value="RD">Red</option>
                <option value="OG">Orange</option>
                <option value="YW">Yellow</option>
                <option value="GN">Green</option>
                <option value="BE">Blue</option>
                <option value="IN">Indigo</option>
                <option value="VI">Violet</option>
                <option value="BL">Black</option>
            </select>
        </div></td>

        <td><div id="colorDiv3" class="form-group">
            <label for="color-selection3"> Please choose a 3rd color</label>
            <select name="color-selection3" id="color-selection3" value="<?php echo $colorSel3; ?>">

                <option value="NA"></option>
                <option value="RD">Red</option>
                <option value="OG">Orange</option>
                <option value="YW">Yellow</option>
                <option value="GN">Green</option>
                <option value="BE">Blue</option>
                <option value="IN">Indigo</option>
                <option value="VI">Violet</option>
                <option value="BL">Black</option>
            </select>
        </div></td>
    </tr>
        <td>
            <tr>
        <div id="endingDiv" class="form-group">

    <p> How will the experiment end?</p>
        <input type="radio"
               name="end" id="lastSquare"
               value="last"/> <label for="lastSquare">The last unpainted square is painted for the first time</label><br/>

        <input type="radio"
               name="end" id="secondBlob"
               value="second"/> <label for="secondBlob">A square gets a second paint blob</label>
            </tr>
        </div></td>


    <tr>
        <td>

            <input type="submit" value="Submit Data" id="submitButton"
                   class="btn btn-primary"/>

        </td>
        <td>
            <input type="reset" value="Reset Data" class="btn btn-primary" />
        </td>
    </tr>

    </table>

</div>
</form>



<a href="submitted.html">
    <button>Continue</button>
</a>
<script>

function submitForm() {

var colorSel = document.getElementById("color-selection").value;
var colorSel2 = document.getElementById("color-selection2").value;
var colorSel3 = document.getElementById("color-selection3").value;
var end = document.querySelector('input[name="end"]:checked').value;


    var formData = "color-selection=" + colorSel +
        "&color-selection2=" + colorSel2 +
        "&color-selection3=" + colorSel3 +
        "&end=" + end;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:63342/Main2.java", true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
            // Handle the response from the backend if needed
            console.log(xhr.responseText);

            window.location.href = 'submitted.html';
        }
    };

    xhr.send(formData);
}

</script>
</body>

<footer>
    <a href="instructions.html">
        <button>Back</button>
    </a>
</footer>
</html>
</html>