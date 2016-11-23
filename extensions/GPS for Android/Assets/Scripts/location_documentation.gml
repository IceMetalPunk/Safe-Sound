///location_documentation()
// Here's some information about the functions that are part of this extension.

exit; // Documentation function! Should not be called during the game.

//The following functions are part of this extension:
device_has_gps(); //Returns if this device supports GPS. You should always call this before calling location_init().
location_init(); //Should be used before calling any other function but device_has_gps().
location_is_enabled(); //If the user has disabled GPS, this will return false. You can still start updating the location if you want, but this won't do anything until the user does enable GPS.
location_open_settings(); //Can be used if the location has been disabled to open the Android settings where the user can enable it.
location_update_start(); //Start updating the location.
location_update_stop(); //Stop updating the location.
location_is_updating(); //Check if the location is currently being updated.
location_get_status(); //Can be used while updating. If updating the location is not possible at this moment, this will return 0. Return 1 otherwise.

//A cached location will be obtained after you call location_init(). It will be changed while you update it (between calling location_update_start() and location_update_stop()).
location_is_available(); //Returns if a location is available.
//You can use the following functions to get information about the current location:
location_get_age(); //Get the age of the location, in ms.
location_get_time(); //Get the time stamp of the location, in ms since January 1, 1970 00:00:00.0 UTC.
//The latitude and longitude are always available when there's a location.
location_get_latitude();
location_get_longitude();
//If you would draw a circle with a radius equal to the accuracy around the location, there'd be a 68% chance that the real location would be in this circle.
location_has_accuracy(); //Returns true if the location has an accuracy.
location_get_accuracy();
//The altitude (in meters) is not above sea level, but above the WGS 84 reference ellipsoid. Also, it may be terribly inaccurate on some devices. Use with care.
location_has_altitude(); //Returns true if the location has an altitude.
location_get_altitude();
//The speed is in meters per second over ground.
location_has_speed(); //Returns true if the location has a speed.
location_get_speed();
//The bearing is the direction in which the device is moving. It has nothing to do with the device orientation. It often will not be available when the speed is 0.
location_has_bearing(); //Returns true if the location has a bearing.
location_get_bearing();

var latitude1, longitude1, latitude2, longitude2;
point_distance_earth(latitude1, longitude1, latitude2, longitude2); //Returns the approximate distance in meters between two locations on Earth.

var always_update;
location_update_always(always_update); //Advanced function! Set this to true to keep updating the location, even while the device has the screen off. As this drains the battery really quickly and has very little benefit, this is false by default.