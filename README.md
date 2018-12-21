# minipos-sdk - Bancard
SDK para integrar el core del miniPOS de Bancard en aplicaciones de terceros.

El SDK permite la integración del lector de tarjetas Crédito y Débito Miura [006 - 010] por bluetooth en aplicaciones en forma fácil.

Download
--------

Gradle:
```groovy
    implementation files('libs/sdkminiposcomercios.jar')
```

Dependencias
```
dependencies {
...
    implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    implementation 'com.squareup.retrofit2:converter-jackson:2.5.0'
...
}
```

### Permisos para Bluetooth

Agrega estos permisos en tu AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
    <uses-permission android:name="android.permission.BLUETOOTH"/>
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
```

Uso
---
Para inicializar el dispositivo con los datos del usuario del portal de comercio:

``` java
      MposUi.initialize(/* Dispositivo [Miura, Sunyard o Walker] */,
                                /* email */,
                                /* clave */,
                                /* numerodelinea */,
                        /* Listener: MposUi.OnDeviceListener */);
```

actual codigo
``` java
        MposUi.initialize(DeviceFactory.DeviceMode.MIURA,
                                Config.MERCHANT_LOGIN,
                                Config.MERCHANT_KEY,
                        phoneNumber,
                        this);
                        
    @Override
    public void onDeviceDetected(String serial, String name) {
        
    }

    @Override
    public void onError() {
        
    }

    @Override
    public void onError(String message) {
       
    }

    @Override
    public void onRequestEnableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onAuthFailed() {
        
    }

    @Override
    public void onAuthDeviceFailed() {
        
    }

    @Override
    public void onSuccess() {
        
    }

    @Override
    public void onDisconnected() {
        
    }

    @Override
    public void onTransactionData(PaymentResponse response) {
        
    }

    @Override
    public void onCVVRequired() {
        
    }                        
```

Nota: Los datos del Config.MERCHANT_LOGIN, Config.MERCHANT_KEY y phoneNumber son los datos propios del comercio

Iniciamos el cobro con la siguiente línea
```java
    MposUi.getInstance().createTransaction(this, 25000);
```

Generar el apk en modo debug y buscar el archivo para copiar al dispositivo para su instalación, en

```
    myapp/app/build/outputs/apk/app-debug.apk
       
``` 

License
-------

    The MIT License (MIT)

    Copyright (c) 2018 onesTech

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
    
    Retrofit2
    Copyright 2013 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
