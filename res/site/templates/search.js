
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
	    hideSearch();
	}
}

function hideSearch() {
    let x = document.getElementsByClassName('searchElement');
    for (i = 0; i < x.length; i++) {
        x[i].style.display="none";
    }
}

hideSearch();