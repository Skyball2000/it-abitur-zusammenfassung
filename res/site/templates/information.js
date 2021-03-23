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