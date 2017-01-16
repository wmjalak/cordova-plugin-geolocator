module.exports = {

    getCurrentPosition: function (params, success, failure) {
        cordova.exec(success, failure, "Geolocator", "getCurrentPosition", [params]);
    }

}