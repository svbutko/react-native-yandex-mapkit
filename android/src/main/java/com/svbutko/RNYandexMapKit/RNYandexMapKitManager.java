package com.svbutko.RNYandexMapKit;

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

public class RNYandexMapKitManager extends SimpleViewManager<MapView> {
    public static final String REACT_CLASS = "RNYandexMapKit";

    public MapView map;
    private ImageProvider markerIcon;
    private MapObjectCollection mapObjects;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
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

    public void animatedToCoordinates(Point point) {
        this.map.getMap().move(
                new CameraPosition(point, 10.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);
    }

    public void setApiKey(String apiKey) {
        MapKitFactory.setApiKey(apiKey);
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

        Point point = new Point(latitude, longitude);
        PlacemarkMapObject mark = mapObjects.addPlacemark(point);


        if (this.markerIcon == null) {
            //this.markerIcon = ImageProvider.fromAsset(this, "");
        }

        mark.setOpacity(opacity);
        //mark.setIcon(this.markerIcon);
        mark.setDraggable(dragable);
        //mark.setUserData();
        //mark.addTapListener(data -> this.addMarker(data));
    }

    public void onMarkerPress(MapObject tapListener) {

    }

    public void setMarkers(ReadableArray markers) {
        for (int i = 0; i < markers.size(); i++) {
            this.addMarker(markers.getMap(i));
        }
    }

    @Override
    protected void onStop() {
        this.map.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        this.map.onStart();
    }
}
