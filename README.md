HSON
====

HSON is a quick to use Android library which converts a json file from a network response to a JSON object with UTF-8 encoding as standard, ensuring that all those special characters are present in the JSON object.

HSON greatly simplifies the process as HSON handles all the networking, the conversion of the network response to a string and the conversion of the string to a UTF-8 encoded JSON object. The library uses the HttpUrlConnection class to ensure maximal compatibility.

HSON also provides a conversion class which converts any json string to a UTF-8 encoded JSON object.

The minimum API required for this library is API 3.

Screenshots
-----------
<img src="/screenshots/screenshot-01.png" height="500"> <img src="/screenshots/screenshot-02.png" height="500"> <img src="/screenshots/screenshot-03.png" height="500">

Dependencies
------------
To use the library, add the dependency to your app module's build.grade file:
```gradle
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    
    // Add this dependency.
    compile 'uk.co.hassie:hson:0.0.2'
}
```

HSON Downloader example
-----------------------
```java
new HSON.Downloader()
    .setURL("https://raw.githubusercontent.com/hassie-dash/hson/master/sample_json.json")
    .download(new Callback.DownloadCallback() {
        @Override
        public void onStart() {
            // Do any work required before the download is started ie. showing a progress dialog.
        }
        @Override
        public void onFinish(JSONObject json) {
            // JSON download successful. Do work with JSON here.
        }
        @Override
        public void onFail() {
            // JSON download failed. Handle error here if required.
        }
    });
```

Note: All callback methods must be overidden but can be left blank.

HSON Converter example
----------------------
```java
String jsonString = "some JSON string";

new HSON.Converter()
    .setString(jsonString)
    .convert(new Callback.ConversionCallback() {
        @Override
        public void onStart() {
            // Do any work required before the conversion is started ie. showing a progress dialog.
        }
        @Override
        public void onFinish(JSONObject json) {
            // JSON conversion successful. Do work with JSON here.
        }
        @Override
        public void onFail() {
            // JSON conversion failed. Handle error here if required.
        }
    });
```

Note: All callback methods must be overidden but can be left blank.

Methods
-------
* <b>HSON.Downloader</b>
  * <b>setURL(param)</b> - The URL of the JSON to download and convert to a JSON object. Can either be a string or a string resource.
  * <b>download(Callback.DownloadCallback) - Start the download. A download callback is required as the parameter.
* <b>HSON.Converter</b>
  * <b>setString(String)</b> - The string to be converted to a JSON object. Must be a string.
  * <b>convert(Callback.ConversionCallback)</b> - Start the conversion. A conversion callback is required as the parameter.

License
-------
Copyright ©2017 Hassie.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.