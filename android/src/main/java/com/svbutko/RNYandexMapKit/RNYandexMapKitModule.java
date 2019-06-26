
package com.svbutko.RNYandexMapKit;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = RNYandexMapKitModule.REACT_CLASS)
public class RNYandexMapKitModule extends ReactContextBaseJavaModule {

  private String apiKey;

  public static final String REACT_CLASS = "RNYandexMapKit";

  public RNYandexMapKitModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @ReactMethod
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getApiKey() {
    return apiKey;
  }
}