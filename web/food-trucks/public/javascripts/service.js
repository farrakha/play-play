angular.module('truckApp', [])
    .controller('TruckCtrl', function($scope, $http, $compile) {

    var defaultZoomLevel = 18;
    //default options to load if geolocation fails
    var defaultMapOptions = {
        center: new google.maps.LatLng(37.7860099, -122.4025387), //;-)
        zoom: defaultZoomLevel,
        panControl: false
    };

    var infowindow;
    var geocoder;
    $scope.markers = [];
    $scope.map = new google.maps.Map(document.getElementById("map"), defaultMapOptions);

    // initialize the map and load the user's current location.
    function initialize() {
        infowindow = new google.maps.InfoWindow();
        geocoder = new google.maps.Geocoder();
        google.maps.event.addListener($scope.map, 'idle', function() {
            //if the map's viewport changes, request data for the new bounds.
            var bounds = $scope.map.getBounds();

            //have to mix and match coordinates because google gives opposite corners
            //to what the data service requires
            var boundsRequest = {
                bounds: {
                    topLeftLatitude: bounds.getNorthEast().lat(),
                    topLeftLongitude: bounds.getSouthWest().lng(),
                    bottomRightLatitude: bounds.getSouthWest().lat(),
                    bottomRightLongitude: bounds.getNorthEast().lng()
                }
            };

            loadTrucks(boundsRequest);
        });
        if (navigator.geolocation) {//is geolocation supported
            navigator.geolocation.getCurrentPosition(
                function(position) {
                    var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    $scope.map.setCenter(pos);
                    $scope.map.setZoom(defaultZoomLevel);
                },
                handleNoGeolocation
            );
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
            url: '/trucks',
            method: "POST",
            data: boundsRequest,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(data, status, headers, config) {
            var requestBounds = data.bounds;
            var mapBounds = $scope.map.getBounds();
            // if the map has moved from these bounds, ignore this response
            if(equalBounds(requestBounds, mapBounds)){
                var trucks = angular.fromJson(data.trucks);
                angular.forEach(trucks, function(truck) {
                    addMarkerForTruck(truck);
                });
            }
        }).error(function(data, status, headers, config) {
            console.log(data);
        });

    }

    /*
     * compare our request bounds object to google map bounds object
     */
    function equalBounds(requestBounds, mapBounds){
        return requestBounds.topLeftLatitude ==  mapBounds.getNorthEast().lat() &&
               requestBounds.topLeftLongitude == mapBounds.getSouthWest().lng() &&
               requestBounds.bottomRightLatitude ==  mapBounds.getSouthWest().lat() &&
               requestBounds.bottomRightLongitude ==  mapBounds.getNorthEast().lng()
    }

    // Add a marker to the map for the truck
    var addMarkerForTruck = function(truck) {
        if (truck.applicant != null && truck.location != null && !$scope.markers[truck.objectid]) {
            var latlng = new google.maps.LatLng(truck.location.latitude, truck.location.longitude);
            var marker = new google.maps.Marker({
                position: latlng,
                map: $scope.map,
                title: truck.applicant
            });
            marker.fooditems = truck.fooditems;
            marker.locationdescription = truck.locationdescription;
            var content = '<div id="infowindow_content" ng-include src="\'/assets/infowindow.scala.html\'"></div>';
            var compiled = $compile(content)($scope);
            google.maps.event.addListener(
                marker
                , 'click'
                , (function(marker, scope, compiled){
                    return function() {
                        //add the truck details to the scope for the info window and open it
                        scope.title = marker.title;
                        scope.fooditems = marker.fooditems;
                        scope.locationdescription = marker.locationdescription;
                        scope.$apply();
                        infowindow.setContent(compiled[0]);
                        infowindow.open(scope.map, marker);
                    };
                 })(marker, $scope, compiled)
            );
            $scope.markers[truck.objectid] = true;
        }
    }

    /* Geocodes an address and loads into the map
      This will trigger a server request to get the data for the new location.
     */
    $scope.loadAddress = function() {
        if ($scope.address.length > 0) {
            geocoder.geocode({
                'address': $scope.address
            }, function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    $scope.map.setCenter(results[0].geometry.location);
                } else {
                    alert('Address not found'); //todo:localize
                }
            });
        }
    };

    initialize();
});