/* Syntax: getEmergencyNumber(String countryCode, latitude, longitude)

Arguments:
String countryCode : The 2-letter country code of the country to get the emergency numbers from.
                        Pass <undefined> here to collect the code from the internet rather than the cache.
Real latitude, longitude : Required only if countryCode is <undefined>, in which case this will
                            be used to get the country code first.
*/
var countryCode=argument[0], lat=argument[1], long=argument[2];
if (argument_count<1) {
    show_error("getEmergencyNumber expected at least 1 argument; 0 provided.", true);
    return false;
}
if (is_undefined(countryCode)) {
    gettingEmergencyNumber=true;
    getCountry=getCountryCode(lat, long);
    return undefined;
}

var url="http://emergencynumberapi.com/api/country/"+countryCode;
return http_get(url);
