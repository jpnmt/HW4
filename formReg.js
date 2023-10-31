
// first color selector should only error if nothing is selected
function chkColor() {
    var colorSel = document.getElementById("color-selection");
    if (colorSel) {
        if (colorSel.value == "") {
            var clDiv = document.getElementById("colorDiv");
            if (clDiv) {
                clDiv.classList.add("has-error");
                clDiv.classList.remove("has-success");
            }
            var clErr = document.getElementById("colorErr");
            if (clErr) {
                clErr.classList.remove("hide");
                clErr.classList.add("show");
            }
            return false;
        } else {
            var clDiv = document.getElementById("colorDiv");
            if (clDiv) {
                clDiv.classList.remove("has-error");
                clDiv.classList.add("has-success");
            }
            var clErr = document.getElementById("colorErr");
            if (clErr) {
                clErr.classList.add("hide");
                clErr.classList.remove("show");
            }
        }
    }
    return false;
}

/* second color selector should error if nothing is selected OR
if color was already selected in first color selector
 */

function chkColor2() {
    var colorSel2 = document.getElementById("color-selection2");
    if (colorSel2) {
        if (colorSel2.value == "") {
            var clDiv2 = document.getElementById("colorDiv2");
            if (clDiv2) {
                clDiv2.classList.add("has-error");
                clDiv2.classList.remove("has-success");
            }
            var clErr2 = document.getElementById("colorErr2");
            if (clErr2) {
                clErr2.classList.remove("hide");
                clErr2.classList.add("show");
            }
            return false;
        } else {
            var clDiv2 = document.getElementById("colorDiv2");
            if (clDiv2) {
                clDiv2.classList.remove("has-error");
                clDiv2.classList.add("has-success");
            }
            var clErr2 = document.getElementById("colorErr2");
            if (clErr2) {
                clErr2.classList.add("hide");
                clErr2.classList.remove("show");
            }
        }
    }
    return false;
}

/* third color selector should error if nothing is selected OR
if color was already selected in first or second color selector
 */

function chkColor3() {
    var colorSel3 = document.getElementById("color-selection3");
    if (colorSel3) {
        if (colorSel3.value == "") {
            var clDiv3 = document.getElementById("colorDiv3");
            if (clDiv3) {
                clDiv3.classList.add("has-error");
                clDiv3.classList.remove("has-success");
            }
            var clErr3 = document.getElementById("colorErr3");
            if (clErr3) {
                clErr3.classList.remove("hide");
                clErr3.classList.add("show");
            }
            return false;
        } else {
            var clDiv3 = document.getElementById("colorDiv3");
            if (clDiv3) {
                clDiv3.classList.remove("has-error");
                clDiv3.classList.add("has-success");
            }
            var clErr3 = document.getElementById("colorErr");
            if (clErr3) {
                clErr3.classList.add("hide");
                clErr3.classList.remove("show");
            }
        }
    }
    return false;
}

function chkEnding() {
    var ed = document.querySelector('input[name="end"]:checked');

    if (!ed) {
        var endDiv = document.getElementById("endingDiv");
        if (endDiv) {
            endDiv.classList.add("has-error");
            endDiv.classList.remove("has-success");
        }
        var endErr = document.getElementById("endingErr");
        if (endErr) {
            endErr.classList.remove("hide");
            endErr.classList.add("show");
        }
        return false;
    } else {
        var endDiv = document.getElementById("endingDiv");
        if (endDiv) {
            endDiv.classList.remove("has-error");
            endDiv.classList.add("has-success");
        }
        var endErr = document.getElementById("endingErr");
        if (endErr) {
            endErr.classList.add("hide");
            endErr.classList.remove("show");
        }
    }

}

function registerHandlers() {
    document.getElementById("colorDiv").onsubmit = chkColor();
    document.getElementById("colorDiv2").onsubmit = chkColor2();
    document.getElementById("colorDiv3").onsubmit = chkColor3();
    document.getElementById("endingDiv").onsubmit = chkEnding();

}





