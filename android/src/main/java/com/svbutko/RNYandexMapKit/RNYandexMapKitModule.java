
package com.svbutko.RNYandexMapKit;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

import com.yandex.mapkit.geometry.Point;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RNYandexMapKitModule extends ReactContextBaseJavaModule {

  public static final String REACT_CLASS = "TextGradient";
  public static final String PROP_MARKERS = "markers";
  public static final String PROP_INITIAL_REGION = "initialRegion";
  public static final String PROP_API_KEY = "apiKey";

  public static final int ANIMATE_TO_REGION = 1;

  public RNYandexMapKitModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public void receiveCommand(RNYandexMapKitManager mapView, int commandId, @Nullable ReadableMap args) {
    switch (commandId) {
      case ANIMATE_TO_REGION:
        mapView.setAnimateToCoordinated(args);
        break;
    }
  }

  @Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
    Map<String, Integer> map = this.CreateMap("animateToRegion", ANIMATE_TO_REGION);
    return map;
  }

  public static <K, V> Map<K, V> CreateMap(K k1, V v1) {
    Map map = new HashMap<K, V>();
    map.put(k1, v1);
    return map;
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