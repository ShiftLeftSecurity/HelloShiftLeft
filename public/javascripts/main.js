function loadDoc() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("output").innerHTML = this.responseText;
            var a = JSON.parse(this.responseText);
        }
    };
    var custno = "customers/"+document.getElementById("searchvalue").value;
    xhttp.open("GET", custno, true);
    console.log(custno);
    xhttp.send();
}
$("input")[0].oninput = function () {
   loadDoc();
};