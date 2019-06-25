package com.svbutko.RNYandexMapKit;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.map.PlacemarkMapObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.yandex.runtime.image.ImageProvider;

public class RNYandexMapKitManager extends SimpleViewManager<MapView> {
    public static final String REACT_CLASS = "RNYandexMapKit";

    private static final String MAPKIT_API_KEY = "216c53c2-0add-46e9-bdcf-fa4d8c960b95";
    private static final Point TARGET_LOCATION = new Point(59.945933, 30.320045);

    public MapView map;
    private ImageProvider markerIcon;
    private MapObjectCollection mapObjects;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(context);

        this.map = new MapView(context);
        this.map.getMap().move(new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f));
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        MapKitFactory.getInstance().onStart();
        this.map.onStart();

        return this.map;
    }

    public void animatedZoomToCoordinates(Point point) {
        this.map.getMap().move(
                new CameraPosition(point, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);
    }

    public void addMarker() {
        Point point = new Point();
        PlacemarkMapObject mark = mapObjects.addPlacemark(point);

        if (this.markerIcon == null) {
            this.markerIcon = ImageProvider.fromFile(//FILE NAME);
        }

        mark.setOpacity(1.0f);
        mark.setIcon(this.markerIcon);
        mark.setDraggable(false);
        mark.addTapListener(data -> this.onMarkerPress(data));
        mark.setUserData(//SET USER DATA);
    }

    public void onMarkerPress(MapObjectTapListener tapListener) {

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
