
package com.svbutko.RNYandexMapKit;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.annotations.ReactProp;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.mapview.MapView;

public class RNYandexMapKitModule extends ReactContextBaseJavaModule {

  public static final String REACT_CLASS = "TextGradient";
  public static final String PROP_MARKERS = "markers";
  public static final String PROP_INITIAL_REGION = "initialRegion";

  public RNYandexMapKitModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @ReactProp(name = PROP_MARKERS)
  public void setMarkers(MapView mapView, ReadableArray markers) {
    mapView.setMarkers(markers);
  }

  @ReactProp(name = PROP_INITIAL_REGION)
  public void setInitialRegion(MapView mapView, ReadableArray region) {
    mapView.setInitialRegion(region);
  }
}