
package com.svbutko.RNYandexMapKit;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

public class RNYandexMapKitModule extends ReactContextBaseJavaModule {

  private String apiKey;

  public static final String REACT_CLASS = "RNYandexMapKit";
  public static final String PROP_MARKERS = "markers";
  public static final String PROP_INITIAL_REGION = "initialRegion";
  public static final String PROP_ON_MARKER_PRESS = "onMarkerPress";
  public static final String PROP_ON_MAP_PRESS = "onMapPress";

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

  @ReactProp(name = PROP_ON_MAP_PRESS)
  public void setOnMapPress(RNYandexMapKitManager mapView, Callback onPress) {
    mapView.setOnMapPress(onPress);
  }

  @ReactProp(name = PROP_MARKERS)
  public void setMarkers(RNYandexMapKitManager mapView, ReadableArray markers) {
    mapView.setMarkers(markers);
  }

  @ReactProp(name = PROP_INITIAL_REGION)
  public void setInitialRegion(RNYandexMapKitManager mapView, ReadableMap region) {
    mapView.setInitialRegion(region);
  }

  @ReactMethod
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getApiKey() {
    return apiKey;
  }
}