
function fetchDataRotten(){
    fetch('/api/dataRotten')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('dataRotten').innerText = JSON.stringify(data.data, null, 2);
            } else {
                document.getElementById('dataRotten').innerText = 'Errore nel recupero dei dati';
            }
        })
        .catch(err => console.error('Errore:', err));
}
function fetchDataOscarAwards(){
    fetch('/api/dataOscar')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('dataOscars').innerText = JSON.stringify(data.data, null, 2);
            } else {
                document.getElementById('dataOscars').innerText = 'Errore nel recupero dei dati';
            }
        })
        .catch(err => console.error('Errore:', err));
}