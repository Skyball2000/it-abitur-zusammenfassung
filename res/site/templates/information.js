function copyToClipboard(containerid, copySymbolId, languageId) {
    var langText = document.getElementById(languageId).innerHTML;
    document.getElementById(languageId).innerHTML = "";
    document.getElementById(containerid).innerHTML = document.getElementById(containerid).innerHTML.trim();
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
}

async function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function getClicks() {
  await sleep(300);
  var namespaceKey = document.getElementById('counterPage').innerHTML;
  var clicksJSON = await(await fetch('https://api.countapi.xyz/hit/' + namespaceKey)).json();
  document.getElementById('counterInsert').innerHTML = clicksJSON.value;
}

getClicks();