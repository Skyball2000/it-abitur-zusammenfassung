function copyToClipboard(containerid, copySymbolId, languageId) {
    var langText = document.getElementById(languageId).innerHTML;
    document.getElementById(languageId).innerHTML = "";
    document.getElementById(containerid).innerHTML = document.getElementById(containerid).innerHTML.trim();

    var noCopys = document.getElementById(containerid).getElementsByTagName('span');
    var spaces = '';
    for(var i = 0; i < noCopys.length; i++) {
        spaces = noCopys[i].innerHTML;
        noCopys[i].innerHTML = '';
    }

    if (document.selection) {
        var range = document.body.createTextRange();
        range.moveToElementText(document.getElementById(containerid));
        range.select().createTextRange();
        document.execCommand("copy");
    } else if (window.getSelection) {
        var range = document.createRange();
        range.selectNode(document.getElementById(containerid));
        window.getSelection().addRange(range);
        document.execCommand("copy");
    }

    window.getSelection().removeAllRanges();
    document.getElementById(copySymbolId).innerHTML = "✅";
    document.getElementById(languageId).innerHTML = langText;
    for(var i = 0; i < noCopys.length; i++) {
        noCopys[i].innerHTML = spaces;
    }
}


function initializeDropDowns() {
    /* Loop through all dropdown buttons to toggle between hiding and showing its dropdown content - This allows the user to have multiple dropdowns without any conflict */
    var dropdown = document.getElementsByClassName("dropdown-btn");
    var i;

    for (i = 0; i < dropdown.length; i++) {
        dropdown[i].addEventListener("click", function() {
        this.classList.toggle("active");
        var dropdownContent = this.nextElementSibling;
        if (dropdownContent.style.display === "block") {
            dropdownContent.style.display = "none";
        } else {
            dropdownContent.style.display = "block";
        }
        });
    }
}

function toggleDropDownVisibility() {
    var sidebar = document.getElementsByClassName("sidenav");
    if (sidebar[0].style.display === "block") {
        sidebar[0].style.display = "none";
    } else {
        sidebar[0].style.display = "block";
    }
}

initializeDropDowns();
toggleDropDownVisibility();
toggleDropDownVisibility();