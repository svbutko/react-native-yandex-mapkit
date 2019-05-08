package com.svbutko.RNYandexMapKit;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class RNYandexMapKitManager extends SimpleViewManager<MapView> {
    public static final String REACT_CLASS = "RNYandexMapKit";

    private static final String MAPKIT_API_KEY = "216c53c2-0add-46e9-bdcf-fa4d8c960b95";
    private static final Point TARGET_LOCATION = new Point(59.945933, 30.320045);

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(context);

        MapView mapView = new MapView(context);
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);

        MapKitFactory.getInstance().onStart();
        mapView.onStart();

        return mapView;

    }
}
