<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>userInfo</title>

    <link href="../css/styles.css" rel="stylesheet" type="text/css">
    <link href="../css/backgrounds.css" rel="stylesheet" type="text/css"/>

</head>
<body>

<form class="fields" id="myForm" method="post" novalidate
      action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">>
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
    <tr>
    <td>
        <div id="endingDiv" class="form-group">
        <label for="ending-selection">How will this program end?</label>

        <input type="radio"
               name="end" id="lastSquare" <?php if ($end=="last"){echo "checked";}?>
               value="last"/> <label for="lastSquare">The last unpainted square is painted for the first time</label><br/>

        <input type="radio"
               name="end" id="secondBlob" <?php if ($end=="second"){echo "checked";}?>
               value="second"/> <label for="secondBlob">A square gets a second paint blob</label>
        </div></td>
    </tr>

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

</form>

</body>
</html>