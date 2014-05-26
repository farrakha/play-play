play-play
=========
Side projects with Play Framework

Food Trucks San Francisco
--------------------------
A web application for looking up nearby food trucks in the San Francisco area. The app uses the user’s current location to display nearby food trucks and their menus. The user can also lookup food trucks near a specific address.

Implementation
--------------

The server uses the San Francisco government data service to query for licensed food trucks, their menus and their location. The data is persisted in a local mongo database and indexed for geospatial lookups for faster query times. The local database is updated on application startup. The server supports two main queries, /trucks and /liveTrucks. Both queries expect as input two sets of coordinates specifying a bounding box within which the requested trucks should be located. /trucks queries the local database and /liveTrucks makes a query directly to the San Francisco government data service. Both queries return a list of trucks that exist within the specified bounds in JSON format.

Client
------

The client is mainly composed of a google map and an address search box. When the page is loaded, the client uses html5 geolocation to determine the user’s current location. The map is then centred around that location and an asynchronous request is made to the server with the map’s bounding coordinates to load the truck information for the current view port. Markers are then added to the map for each of the locations returned. As the user pans around the map, requests are made to the server to fill in markers for the changing view port. If a user enters an address, the address is geocoded, the map is centred around that address and truck information is loaded for the new location. If a truck marker is clicked, an information window is displayed containing the name of the vendor and the menu items.
