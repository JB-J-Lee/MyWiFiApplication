# Android Specific WiFi Application Sample
===================================

This sample demonstrates how to connect specific wifi.

Introduction
------------

This sample demonstrates how to connect specific wifi on Android.

```xml
public SpecificWiFi(Context context, OnConnectionResultListener listener, @NonNull String ssid, @NonNull String password) {
        this.context = context;
        this.listener = listener;
        this.ssid = ssid;
        this.password = password;
    }
```

In some cases, you may want to customize this specific ssid. When doing so, change the `ssid` and `password`. 
For an example, see this sample's `SpecificWiFi` class.


Pre-requisites
--------------

- Android SDK 27
- Android Build Tools v27.1.1
- Android Support Repository

License
-------

Copyright JB.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
