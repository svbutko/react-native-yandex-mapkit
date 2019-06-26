package com.svbutko.RNYandexMapKit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.CalendarContract;
import android.util.Base64;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.common.MapBuilder;
import com.yandex.runtime.image.ImageProvider;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RNYandexMapKitManager extends SimpleViewManager<MapView> implements UserLocationObjectListener {
    public static final String REACT_CLASS = "RNYandexMapKit";

    public static final int ANIMATE_TO_REGION = 1;
    public static final int ZOOM_IN = 2;
    public static final int ZOOM_OUT = 3;
    public static final int NAVIGATE_TO_USER_LOCATION = 4;

    public static final String PROP_MARKERS = "markers";
    public static final String PROP_INITIAL_REGION = "initialRegion";
    public static final String PROP_ON_MARKER_PRESS = "onMarkerPress";
    public static final String PROP_ON_MAP_PRESS = "onMapPress";
    public static final String PROP_FOLLOW_USER_LOCATION = "followUserLocation";

    private MapObjectCollection mapObjects;
    private Callback onMarkerPressCallback;
    private Callback onMapPressCallback;
    private UserLocationLayer userLocationLayer;

    private ThemedReactContext context = null;
    private MapView mapView = null;


    //TODO: add icon prop
    private byte[] imageDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAABP1JREFUeNrUW1lsjUEUnmtrqFoisbWE0NTyUmIPFVsTYkkaS1KpJRIEpVQ8VBBppCFiSYMXW0gEIXiwkwoiltqqiooleKBLUEuopb6Te25IU+79z8z8d3zJlz/p/Wf+c77ZzpyZBmpqapQOXsWMieS1zuBIsC+YBCaCzcBY/v0TWAWWgg/BQvAc+DxcxQlfT2jZ30DZQ0dwGjMxzLuxzHbgUHAO/53E2AvuIa1tGFnPQp09wP3cerkROP8vdAPXcF27wa4uC9Ac3AIWg1PAgMG664PTuUds4uHjlACj2Lh5hh2vS4hFYAk4LOoCYAIkZ1eBp8C2yj/E8ySZwzaIEZCuAvgwtcYO7prRxHZwLlaDH74JwM4fBNOUG9hHq41EBOkQ2OqQ84R0cKMvcwBaPxuP2co9ZMK2BVaHAD7QB48rYEPlJqrB/hgKd4wLAOdj8LinGdj4AVoikyHCN9NDYMl/4HwoEs0y2gPQ+m3weAo2MWAgBUznuaXecOBE9ffkDZMJkT/SBgy9oMLUZijLgPMHwHXgrTDv9QOXgpM0vtUUXAiu1O4BaP04PF5yrC/BCzADvOix3DDeCcYLv/sW7IBe8El3DkjTcP462FvgPKGAy94SfrslOMHEJDhVaEARmApWanTlMp4XHgjLZ2gNAXR/GkvveBfmBR/AXuATQzM7ZZFu/pFBihS0FLbAMPgs7QGDBc4TVhh0nvAIXC0oRwHbIJ0hMETwUZowt1lY3/PB14JyKToCdBduT6stCPAF3Cko11NHAElQcshilCepO1FHgATB2ltiUYC7PMF6QbyOAF5n3UeW4/yf4GOPZeJ0BPC67a1Q9vFWsBKYS4iYTrBE+xvhKqvyWF9rHwRo5fH99zoCeA1jkyz3ggaClalSRwCv0Vwch8C20B9s7LHMEx0BJLP6ZIsCSHIEpToCFAo+OIsTEqZB54EzBOUKdQQ4K5ykllkQIEeYlzgr3g7zlvi+CiYavYD2AgM1khm1MQC8pLzfZyjBVlhrL0DYLzC4EXhUEErXhQ7gYSW7zHHARFCxm0NQieEXwC6aiRBKp7UXhs27tAVAF6L9/UmhA+Q8ZXIyPJYL8IRHE1gn4bdPsu1GwsqNGq1IExfd8bmqggnWRv94N4aX0RvcejqryYaIlPZwNHYbj2QDY5q2s5dV8CpNmfp9MEITbYpgB1oX7qD1IwrIvEwsuTwZ6YKixdFMW8i1sbM6wuPZdRSyrWYFQJeisbL8PxBgOdtqfm+Nik/jccJh54/DxjO2kwt0C+OLg86TTZnWsytQ+BkeeQ4KkMe2Kds9gLBWBW+LuIIitkn5IgCU/srRXbUDzpMNGWyT8qsHkAiUo1/pgAArYEuRtLBu/m69Cp7jRwsFbIMYAQP/MNGGg48En52n/x/ojdYv16nERAaXLjpN9Hk+qOaNVbluRaZS2NfA+T4KMI93jMoVAQh0LJ7vg/ObVfCWunJNAMJi8JhF5ynNlm2yQtMC0HX1dN7vmwYlRafyN5wVgEAXksZydGYKdPl5HNetXBeAQAeSdL2t2EBdVEeqCnPI6ZoAipeo4ZoiFHMd5baMtH2eHxJBMhzu2nbeDwFCIqRwrBApKIM81LbzfgkQmhOoNSM5X6CM0whbYz5aAoRWh/EqeNL0N9BvE2zM9i4IQPgOzlTBk97ayOHfvvtpkN8ChJDHG6hy5kQVpTTbLwEGAJozOmIuo2nJAAAAAElFTkSuQmCC", Base64.DEFAULT);
    private Bitmap image = BitmapFactory.decodeByteArray(imageDecodedString, 0, imageDecodedString.length);

    private byte[] locationDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAtxJREFUeNrsm79PFEEUx2dRQA3BGOgxyJ0URyKttiSGhoYSSztsoNKa0AAVWBn/CGIhISH8BYAmSgLckfCjRGNDEEhg/U7uEcnJrezuvHlv7/Yln+Zu5828777ZNzu7a8IwNGlIYAWwAMrgnCjTb4W4zlKP36MAAXhHAYd1OKdjgkYUYCYi8FpmGk2A4RjBXzHsQ4Ag4Tz+m9fBrbL1C3gW0/VXMHgbAVKN34MARbCd0P1TsMMpQIvht0GhtmoE6BJqq0aAn0Jt1QiwLtRWjQCVhIGsU9vMC2Dtrac28c3jSnAqxiJoqhGXwtYmwWlE4Kd0jLcT6GsleN16wBta6j6m3/bAEngP9uMKoH0lyD6Fs3ARVGt3mf13gBHwktL9F/gIVmi+X7d7YAi8Bo9oWiyDT+A4a1XAzotxcBRxsdsAn4mNiIvjEfkKslIFHoDFBPf//2ORfKsWoI3SO2RihfpQK8A0Y/BXTGtdB/SD7+AO84X1ApTAlrYyOOEheEN9TLhy5ioDrJA/qHz5MFtOu8GllgwoeQzeUF8lTbfDAwKLuAFNAvQJCPBEkwC9zS6ARAb0aRKgKCBAUYsAD6kk+bZu6ltcAIn0d9a3CwEKggIUcgHyKZALkAsgfTcYCgpg9wMC6QxYFYx/1YWCabfEOsEHw78VVovts1PTnuAL8M1D4LaP585OoONN0VZTfax9whD4CfludZrBTA9GRhkEGOWYwlzPBjcz4pPt6bB9ePHb4e32Jbhvqu8S/5MBGvYDas0O9MChv4Obgte0IXKTVZT6yqQA5WYXYLfZBajkAjAZ50tS7VQK075FFVIJPKu3ENKaAXbAhw78HNYLXvsUcJW6rO8LcwtQVuJDTIBdJT7yKZALIFAGDZWv4xRC27vADiqnJmtl0NDA11K0X4sKPgtTwNpsirZz7KPz9MHEvIm/BTbvZfwevxgZM9UPoS4igrb/2RenX/k6gX8EGABSY47G+1wwtgAAAABJRU5ErkJggg==", Base64.DEFAULT);
    private Bitmap locationImage = BitmapFactory.decodeByteArray(locationDecodedString, 0, locationDecodedString.length);

    private InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(Map map, Point point) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("latitude", Double.toString(point.getLatitude()));
            writableMap.putString("longitude", Double.toString(point.getLongitude()));

            sendNativeEvent(PROP_ON_MAP_PRESS, writableMap, mapView.getId(), context);
        }

        @Override
        public void onMapLongTap(Map map, Point point) {

        }
    };

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
        RNYandexMapKitModule nativeModule = context.getNativeModule(RNYandexMapKitModule.class);

        MapKitFactory.setApiKey(nativeModule.getApiKey());
        MapKitFactory.initialize(context);

        this.context = context;

        mapView = new MapView(context);
        mapView.getMap().addInputListener(inputListener);

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        userLocationLayer = mapView.getMap().getUserLocationLayer();
        userLocationLayer.setEnabled(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        MapKitFactory.getInstance().onStart();
        mapView.onStart();

        return mapView;
    }

    public void setAnimateToCoordinated(ReadableMap region) {
        double latitude = region.getDouble("latitude");
        double longitude = region.getDouble("longitude");
        double latitudeDelta = region.getDouble("latitudeDelta");
        double longitudeDelta = region.getDouble("longitudeDelta");

        Point point = new Point(latitude, longitude);

        this.animatedToCoordinates(point);
    }

    public void zoomOut() {
        mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                        mapView.getMap().getCameraPosition().getZoom()+1, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1), null);
    }

    public void zoomIn() {
        mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                        mapView.getMap().getCameraPosition().getZoom()-1, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1), null);
    }

    public void navigateToUserLocation() {
        if (userLocationLayer != null) {
            mapView.getMap().move(
                    new CameraPosition(userLocationLayer.cameraPosition().getTarget(), 18.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 1),
                    null);
        }
    }

    public void animatedToCoordinates(Point point) {
        mapView.getMap().move(
                new CameraPosition(point, 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);
    }

    @ReactProp(name = PROP_INITIAL_REGION)
    public void setInitialRegion(ReadableMap region) {
        double latitude = region.getDouble("latitude");
        double longitude = region.getDouble("longitude");
        double latitudeDelta = region.getDouble("latitudeDelta");
        double longitudeDelta = region.getDouble("longitudeDelta");

        Point point = new Point(latitude, longitude);

        mapView.getMap().move(
                new CameraPosition(point, 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null
        );
    }

    public void addMarker(ReadableMap marker) {
        Float opacity = marker.hasKey("opacity") ? (float)marker.getDouble("opacity") : 1.0f;

        ReadableMap latLng = marker.getMap("coordinate");
        double latitude = latLng.getDouble("latitude");
        double longitude = latLng.getDouble("longitude");

        boolean dragable = marker.hasKey("draggable") && marker.getBoolean("draggable");
        Object userData = marker.hasKey("userData") ? marker.getMap("userData") : new Object();

        Point point = new Point(latitude, longitude);
        PlacemarkMapObject mark = mapObjects.addPlacemark(point);

        mark.setOpacity(opacity);
        mark.setIcon(ImageProvider.fromBitmap(image));
        mark.setDraggable(dragable);
        mark.setUserData(userData);
        mark.addTapListener(this::onMarkerPress);
    }

    public boolean onMarkerPress(MapObject mapObject, Point point) {
        if (mapObject instanceof PlacemarkMapObject && onMarkerPressCallback != null) {
            onMarkerPressCallback.invoke(mapObject.getUserData(), point);
            return true;
        }
        return false;
    }

    @ReactProp(name = PROP_ON_MARKER_PRESS)
    public void setOnMarkerPress(Callback onPress) {
        onMarkerPressCallback = onPress;
    }

    @ReactProp(name = PROP_ON_MARKER_PRESS)
    public void setOnMapPress(Callback onPress) {
        onMapPressCallback = onPress;
    }

    @ReactProp(name = PROP_MARKERS)
    public void setMarkers(ReadableArray markers) {
        for (int i = 0; i < markers.size(); i++) {
            addMarker(markers.getMap(i));
        }
    }

    @Override
    public void onObjectRemoved(UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(UserLocationView userLocationView, ObjectEvent objectEvent) {

    }

    @Override
    public void receiveCommand(MapView mapView, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(mapView, commandId, args);
        switch (commandId) {
            case ANIMATE_TO_REGION:
                this.setAnimateToCoordinated(args.getMap(0));
                return;
            case ZOOM_IN:
                this.zoomIn();
                return;
            case ZOOM_OUT:
                this.zoomOut();
                return;
            case NAVIGATE_TO_USER_LOCATION:
                this.navigateToUserLocation();
                return;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandId,
                        getClass().getSimpleName()));
        }
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer> map = this.CreateMap(
                "animateToRegion", ANIMATE_TO_REGION,
                "zoomIn", ZOOM_IN,
                "zoomOut", ZOOM_OUT,
                "navigateToUserLocation", NAVIGATE_TO_USER_LOCATION
        );

        return map;
    }

    public static <K, V> Map<K, V> CreateMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);

        return map;
    }

    @Nullable
    @Override
    public java.util.Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(PROP_ON_MAP_PRESS, MapBuilder.of("registrationName", PROP_ON_MAP_PRESS))
                .build();
    }

    private void sendNativeEvent(final String eventName, final WritableMap event, final int id, final ReactContext context) {
        context.getJSModule(RCTEventEmitter.class).receiveEvent(id, eventName, event);
    }
}
