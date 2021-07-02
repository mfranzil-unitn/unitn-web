var x = document.getElementById("demo");
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        x.innerHTML = "GEO-location is not supported by this browser.";
        var lat = localStorage.lat;
        var lon = localStorage.lon;
        showMap(lat, lon);
    }
}

function showPosition(position) {
    var lat = position.coords.latitude;
    var lon = position.coords.longitude;
    x.innerHTML = "Latitude: " + lat + "<br>Longitude: " + lon;

    localStorage.setItem("lat", lat);
    localStorage.setItem("lon", lon);

    showMap(lat, lon);
}

function showError(error) {
    switch (error.code) {
        case error.PERMISSION_DENIED:
            x.innerHTML = "User denied the request Geolocation.";
            break;
        case error.POSITION_UNAVAILABLE:
            x.innerHTML = "Location information is unavailable.";
            break;
        case error.TIMEOUT:
            x.innerHTML = "The request to get user location timed out.";
            break;
        case error.UNKNOWN_ERROR:
            x.innerHTML = "An unknown error occurred.";
            break;
    }
    showMap(localStorage.lat, localStorage.lon);
}

function showMap(lat, lon) {
    // Initialize the platform object:
    var platform = new H.service.Platform({
        'app_id': 'zILU1lxRnAjEDjGMt3M9',
        'app_code': 'h8HbVK-vvg6qikxNAVDwfA'
    });

    // Obtain the default map types from the platform object
    var maptypes = platform.createDefaultLayers();

    // Instantiate (and display) a map object:
    var map = new H.Map(
            document.getElementById('mapContainer'),
            maptypes.normal.map,
            {
                zoom: 15,
                center: {lng: lon, lat: lat}
            });
}

$().ready(function () {
    getLocation();
});