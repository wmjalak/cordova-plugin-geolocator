# cordova-plugin-geolocator

Goal is to get the most reliable position in given time by using the GPS_PROVIDER.
If the given time finishes before accurate GPS-position, NETWORK-based position is returned.

## Installation

This requires cordova 5.0+

```xml		
	cordova plugin add cordova-plugin-geolocator
```

## Supported Platforms

  - Android

## Usage

```javascript
geolocator.getCurrentPosition({
        timeout:6000
    },
    function(position) {  // Success callback

        console.log(position.coords.latitude, position.coords.longitude);
        /*
            position.coords.altitude
            position.timestamp
            position.provider (NETWORK_PROVIDER, GPS_PROVIDER)
        */
        
    },
    function(error) {  // Error callback
        console.log(error);
    }
);
```
