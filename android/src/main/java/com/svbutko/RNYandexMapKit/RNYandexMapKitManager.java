package com.svbutko.RNYandexMapKit;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.mapkit.user_location.UserLocationLayer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RNYandexMapKitManager extends SimpleViewManager<MapView> {
    public static final String REACT_CLASS = "RNYandexMapKit";

    public static final int ANIMATE_TO_REGION = 1;

    public MapView map;
    private ImageProvider markerIcon;
    private MapObjectCollection mapObjects;
    private Callback onMarkerPressCallback;
    private Callback onMapPressCallback;
    private UserLocationLayer userLocationLayer;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
        RNYandexMapKitModule nativeModule = context.getNativeModule(RNYandexMapKitModule.class);

        MapKitFactory.setApiKey(nativeModule.getApiKey());
        MapKitFactory.initialize(context);

        this.map = new MapView(context);
        mapObjects = this.map.getMap().getMapObjects().addCollection();

        MapKitFactory.getInstance().onStart();
        this.map.onStart();

        return this.map;
    }

    public void setAnimateToCoordinated(ReadableMap region) {
        double latitude = region.getDouble("latitude");
        double longitude = region.getDouble("longitude");
        double latitudeDelta = region.getDouble("latitudeDelta");
        double longitudeDelta = region.getDouble("longitudeDelta");

        Point point = new Point(latitude, longitude);

        this.animatedToCoordinates(point);
    }

    public void followUserLocation(boolean isFollowing) {
        if (isFollowing) {
            this.userLocationLayer = this.map.getMap().getUserLocationLayer();
            this.userLocationLayer.setEnabled(true);
            this.userLocationLayer.setHeadingEnabled(true);
        }
    }

    public void zoomOut() {
        this.map.getMap().move(new CameraPosition(this.map.getMap().getCameraPosition().getTarget(),
                        this.map.getMap().getCameraPosition().getZoom()+1, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1), null);
    }

    public void zoomIn() {
        this.map.getMap().move(new CameraPosition(this.map.getMap().getCameraPosition().getTarget(),
                        this.map.getMap().getCameraPosition().getZoom()-1, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1), null);
    }

    public void navigateToUserLocation() {
        if (this.userLocationLayer != null) {
            this.map.getMap().move(
                    new CameraPosition(this.userLocationLayer.cameraPosition().getTarget(), 15.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 1),
                    null);
        }
    }

    public void animatedToCoordinates(Point point) {
        this.map.getMap().move(
                new CameraPosition(point, 10.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);
    }

    public void setInitialRegion(ReadableMap region) {
        double latitude = region.getDouble("latitude");
        double longitude = region.getDouble("longitude");
        double latitudeDelta = region.getDouble("latitudeDelta");
        double longitudeDelta = region.getDouble("longitudeDelta");

        Point point = new Point(latitude, longitude);

        this.map.getMap().move(
                new CameraPosition(point, 10.0f, 0.0f, 0.0f),
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


        if (this.markerIcon == null) {
            //this.markerIcon = ImageProvider.fromAsset(this, "");
        }

        mark.setOpacity(opacity);
        //mark.setIcon(this.markerIcon);
        mark.setDraggable(dragable);
        mark.setUserData(userData);
        mark.addTapListener(this::onMarkerPress);
    }

    public boolean onMarkerPress(MapObject mapObject, Point point) {
        if (mapObject instanceof PlacemarkMapObject && this.onMarkerPressCallback != null) {
            this.onMarkerPressCallback.invoke(mapObject.getUserData(), point);
            return true;
        }
        return false;
    }

    public void setOnMarkerPress(Callback onPress) {
        this.onMarkerPressCallback = onPress;
    }

    public void setOnMapPress(Callback onPress) {
        this.onMapPressCallback = onPress;
    }

    public void setMarkers(ReadableArray markers) {
        for (int i = 0; i < markers.size(); i++) {
            this.addMarker(markers.getMap(i));
        }
    }

//    @Override
//    protected void onStop() {
//        this.map.onStop();
//        MapKitFactory.getInstance().onStop();
//        super.onStop();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        MapKitFactory.getInstance().onStart();
//        this.map.onStart();
//    }

    @Override
    public void receiveCommand(MapView mapView, int commandId, @Nullable ReadableArray  args) {
        super.receiveCommand(mapView, commandId, args);
        switch (commandId) {
            case ANIMATE_TO_REGION:
                this.setAnimateToCoordinated(args.getMap(0));
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
}
