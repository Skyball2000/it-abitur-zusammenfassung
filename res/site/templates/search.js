
async function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function searchPage() {
	let input = document.getElementById('searchbar').value.toLowerCase();
	let x = document.getElementsByClassName('searchElement');

    if (input.length > 1) {
        for (i = 0; i < x.length; i++) {
            if (!x[i].innerHTML.toLowerCase().includes(input)) {
                x[i].style.display="none";
            } else {
                x[i].style.display="list-item";
            }
        }
	} else {
	    randomSearchTerm();
	    hideSearch();
	}
}

function hideSearch() {
    let x = document.getElementsByClassName('searchElement');
    for (i = 0; i < x.length; i++) {
        x[i].style.display="none";
    }
}

function randomSearchTerm() {
    var searchTerm = Array(
BUILDER-PLACE-SEARCH-RANDOM-SITE
    );

    var term = searchTerm[Math.floor(Math.random() * searchTerm.length)];
    document.getElementById('searchbar').placeholder = term;
}

async function rollRandomSearchTerm() {
	var i;
	for (i = 3; i < 15; i++) {
		await sleep(i * 20);
		randomSearchTerm();
	}
}

hideSearch();
randomSearchTerm();
setInterval(rollRandomSearchTerm, 10000);
