
package com.svbutko.RNYandexMapKit;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNYandexMapKitModule extends ReactContextBaseJavaModule {

  public RNYandexMapKitModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "RNYandexMapKit";
  }

  @ReactMethod
  public void getString(Callback successCallback) { successCallback.invoke("Test"); }
}