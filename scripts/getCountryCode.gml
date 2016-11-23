/* Syntax: getCountryCode(latitude, longitude)

Returns:
String country_code : Example, US, GB, AU, etc.
*/

var url="http://api.geonames.org/countryCode";
url+="?lat="+string(argument0)+"&lng="+string(argument1)+"&username=icemetalpunk&type=json";

return http_get(url);
