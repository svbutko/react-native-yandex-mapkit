
package com.svbutko.RNYandexMapKit;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

public class RNYandexMapKitModule extends ReactContextBaseJavaModule {

  public static final String REACT_CLASS = "TextGradient";
  public static final String PROP_MARKERS = "markers";
  public static final String PROP_INITIAL_REGION = "initialRegion";
  public static final String PROP_API_KEY = "apiKey";
  public static final String PROP_ON_MARKER_PRESS = "onMarkerPress";

  public RNYandexMapKitModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @ReactProp(name = PROP_ON_MARKER_PRESS)
  public void setOnMarkerPress(RNYandexMapKitManager mapView, Callback onPress) {
    mapView.setOnMarkerPress(onPress);
  }

  @ReactProp(name = PROP_MARKERS)
  public void setMarkers(RNYandexMapKitManager mapView, ReadableArray markers) {
    mapView.setMarkers(markers);
  }

  @ReactProp(name = PROP_INITIAL_REGION)
  public void setInitialRegion(RNYandexMapKitManager mapView, ReadableMap region) {
    mapView.setInitialRegion(region);
  }

  @ReactProp(name = PROP_API_KEY)
  public void setApiKey(RNYandexMapKitManager mapView, String apiKey) {
    mapView.setApiKey(apiKey);
  }
}