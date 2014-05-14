angular.module('truckApp', [])
.controller('TruckCtrl', function($scope, $http) {

    //default options to load if geolocation fails
    var defaultMapOptions = {
        center: new google.maps.LatLng(37.7860099, -122.4025387), //;-)
        zoom: 18,
        panControl: false
    };

    var infowindow = new google.maps.InfoWindow();
    var geocoder;
    $scope.markers = [];
    $scope.map = new google.maps.Map(document.getElementById("map"), defaultMapOptions);

    // initialize the map and grab the user's current location.
    function initialize() {
        geocoder = new google.maps.Geocoder();
        google.maps.event.addListener($scope.map, 'idle', function() {
            //if the map's viewport changes, request data for the new bounds.
            var bounds = $scope.map.getBounds();
            var boundsRequest= {bounds:{}};
            //have to mix and match coordinates because google gives opposite corners
            //to what the data service requires
            boundsRequest.bounds.topLeftLatitude = bounds.getNorthEast().lat();
            boundsRequest.bounds.topLeftLongitude = bounds.getSouthWest().lng();
            boundsRequest.bounds.bottomRightLatitude = bounds.getSouthWest().lat();
            boundsRequest.bounds.bottomRightLongitude = bounds.getNorthEast().lng();
            loadTrucks(boundsRequest);
        });
        if (!navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                $scope.map.setCenter(pos);
                $scope.map.setZoom(18);
            },
                handleNoGeolocation);
        } else {
            // Browser doesn't support Geolocation
            handleNoGeolocation();
        }
    }

    function handleNoGeolocation() {
        $scope.map.setCenter(defaultMapOptions.center);
    }

    // makes a request to the server to load truck information for a set of bounds.
    function loadTrucks(boundsRequest) {
        $http({
            url: '/bounds',
            method: "POST",
            data: boundsRequest,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(data, status, headers, config) {
            var trucks = data;
            angular.forEach(trucks, function(truck) {
                addMarkerForTruck(truck);
            });
        }).error(function(data, status, headers, config) {
            console.log(data);
        });

    }

    // Add a marker to the map for the truck
    var addMarkerForTruck = function(truck){
        if (truck.applicant != 'null') {
            var latlng = new google.maps.LatLng(truck.latitude, truck.longitude);
            var marker = new google.maps.Marker({
                position: latlng,
                map: $scope.map,
                title: truck.applicant
            });
            google.maps.event.addListener(marker, 'click', function() {
                infowindow.setContent(marker.title)
                infowindow.open($scope.map, marker);
            });
            $scope.markers.push(marker);
        }

    }

    /* Geocodes an address and loads into the map
      This will trigger a server request to get the data for the new location.
     */
    $scope.loadAddress = function() {
        if($scope.address.length > 0){
            geocoder.geocode( { 'address': $scope.address}, function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                  $scope.map.setCenter(results[0].geometry.location);
                } else {
                  alert('Address not found');//todo:localize
                }
            });
        }
    };

    initialize();
});
