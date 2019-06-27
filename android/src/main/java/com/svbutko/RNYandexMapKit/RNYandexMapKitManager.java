package com.svbutko.RNYandexMapKit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.LinearRing;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polygon;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolygonMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchMetadata;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static final String PROP_FOLLOW_USER_LOCATION = "followUserLocation";
    public static final String PROP_POLYGONS = "polygons";
    public static final String PROP_ON_MARKER_PRESS = "onMarkerPress";
    public static final String PROP_ON_MAP_PRESS = "onMapPress";
    public static final String PROP_ON_LOCATION_SEARCH = "onLocationSearch";
    public static final String PROP_SEARCH_LOCATION = "searchLocation";

    private UserLocationLayer userLocationLayer;
    private ThemedReactContext context = null;
    private MapView mapView = null;
    private SearchManager searchManager;
    private Session searchSession;

    private boolean shouldSearchLocation = false;
    private boolean shouldSendOnMapPress = false;

    private List<PolygonMapObject> polygonsList = new ArrayList<PolygonMapObject>();
    private List<PlacemarkMapObject> markersList = new ArrayList<PlacemarkMapObject>();
    private PlacemarkMapObject userSearchPlacemark;

    //TODO: add icon prop
    private byte[] imageDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAB4AAAAqCAYAAACk2+sZAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwwAADsMBx2+oZAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS40E0BoxAAAAkhJREFUWEftl79O40AQxv0OkMROZCAHytsc75AKIfEOQEFDAz21BQ0F0h05TgEhCoKERIEQgoIKJCr665b5otkwux4HW7FJcVj6SZv5832eOI7XgTFmKqhBn4uZmZjo9prRzsHS/HHSWbgiHpNO+wFrxJBDDWo1DR81aIHI37C+/SdqvNHa5AG16KH12BNQg2g6q9XWe80wt6EPeqFBa/UEUgEUnoSNnhSZBNZKmTsfUPC7FV3LxjJgTcfcMe1FjXPZUCasPTIfmfYb9UNZWAXsMTS3xrgN1OIK6A6NaRHTL/DSS2bS/9E2g7VV87C5MQRrxLRaDfaKC017t7Ji/r280Am7B2LIaT0ZdAM6g10l4dBfbJvXJFFN7YEcalCraUjgGRw3w1ctKYFg3gO1moYEnviq1aRlsPxz7KT+gVr0aFqST42LTGuPPFN/G6f4/4yn9qsGRabOMy0I6Fn5qCUkZf9zwTM4rde2tKRGWf/V8MRXXeiROOnTienCmLY74Y2XqAz2iqe3EWBjbPLuvILSYY+Prc8XTj2c1jfGFujCKywN1nZ3mcK8yqlH0wLfmF5dZve9holhTX1Db0EBbU0GsnESWMsxBc4HCwqLvCFmwRopU5AKWKihjOvtXFeJGgTUhOt94AnlhnvVaYEatKDxJCr+yso9maZADUogwK+ZqokP1441BWrQB0IkeC8NNLjmU1OgBjUg+KsVPUkjCedymQI1mAWE6b58loaAY7lNgRocBwyO5lp7Sad9C47i5l5RU6AGq8cE77senOxoWv4fAAAAAElFTkSuQmCC", Base64.DEFAULT);
    private Bitmap image = BitmapFactory.decodeByteArray(imageDecodedString, 0, imageDecodedString.length);

    private byte[] locationDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAYAAADFeBvrAAAAAXNSR0IArs4c6QAABVVJREFUaAXdWl1sFFUU/u7uoibSGkNiMAahUCEpSPEFo760D/JoMFh8Mr7W2JhgNUVeIFDRQkrjD1oTXxb1hQYTDCZGIt0iWFCkLEJDFQr+i4bYdtvQn90dz7kzk52Z3fndu23teZm5c+895/vmnLl/ZwRUSverDyGfb4SGekBbQ9eVdK0GRJVuRsvQ/RgEhuk6RNc0YrFeNO//SRUMUbai7pfrkRPPEfCt0LRlkfQJ8SsRPIy49hGaD6Qj6TA6RSf0/raNyKGD9DSUA6BE3xTiaMMLXd+WqPN9FJ7QwW219Db3kjeafLWX00CIHvL6DrzYdTWMmuCEenclMJh5E8i/RN/GojBGIrcVmAFib6Ouajsad2WD6AlGKPnaEoxP9ZBXGoMoVd5GiF4svrMJz79xy0+3P6F329ZCTH+mj1h+6ipYzyOjdsdTaOm47GUl5lWJ7tYnIWb655wMg+QpgLEwJg9x95D0DJPRjDnEQ8tsVgmRgbboMTdPlfYQfzMyzOYZGX5x/IIZG2MsIcWEeDSTAwDP8vNUOPwYI2N1SDEhHprnajRzgPMsMkY5jdhb2b8hOWlicNbmGTuW8CU5T6HOOvk6PMQrgFmaNMPDL+4hsRJmixQ8pK/NzlrqZv32gbvvweaadThz82d8/89vwe3H8ai59it8VPpCM7gSRS2ZxJZV69G0qh5P3F8DIQRa+o6EI6Rjb2RIuod4C5DVLijC6KumFAmzk6ZpWJbcjd8nRs1Hwa4JsYG3HrqH9P1MsI4RW3mRsKo8/ef18GRYgeQAgxBvziogQUlYTfdci7q/kxxeEeBtczb7o1VpOfdRSJj2IoebqSCRWJ2QZwDmg4jXckhYTUYON1MJnWckaN6hA43wYpLYWrsBjy9dIUen8FrsPaKHm6GHuNCgQKczAaUSJEzTHG5Hrl00ixGv2hr2kO8i9Omah9H6SIMyT5RCW3a4sVLiQksfPjfzlrVLlmLjfQ8qCSs3S2WHm1SsVRMh8xDQzRTQfu44aj9+HQd/OIXJLJ1bKBY14cagRJVjceqO9JfxEbSc/BQrDu3BvvMnkJmedG8cskZJuBk2OeToeDa43Lw9jrb+Y1hOxHae/QK3JieCd3ZpqSbcWLmW4ZAbc7Hj+fjfqdvYfe5LLE/uQeupo/gj7NrL0K4u3FihGIvpB+ee2D0rJ7LTOJDuw8pD7WhO9WB41PfozKZPZbgxF/bQkM1CxMJUPocPLvdj9Sd7saP/88Ba1IUbmxRD7KGoq8GSoHM0Qe4bOBHIU2rDjfkgHZP5mZLQoj9kUh3nv/JVoDTc2BrlmmIy2STzM772QzVIXvnOd6BQGm7MgRJnxjxEySbFwt9U50DKVavycOOEGYlOiDNnFRAeJNzmqW/+uhFtZ+qG0+CgE9LTgCm3tlGf85D+Vvpkye6Hryo9wkiZqUwj5MgmpwErIO9c/LpomaQ83CzYC4Q4pynTgGpZjdCa771Lp21KlYYbY7bkYwuEpEnKaerHqzYA5Ra6LvTZVunKwk1iJcwWsROSCVrKaSoWXtB+OHhGalUbboTVkVS2E2KTnKDlnKZi2T/Qi5lcDsrCjTEyVofEHWUgmcrj2YZjmMk+Q3X3FtVHfDBK35JGfY8OX8KVkb8jajG6cb518V2bsLm9aO9SOKx3mlhQKUkmx9nmuNhC4RdqA+h8L0rLjIUxeWTCi78hK4LmzuMyQSt/NrJWzMG9TOtTspgxeYh7yFk7/Y9+vPD2kEmK/+Coq94EEeusxDxlmim68jzDNtl2gL9IuH8wD1ktLZifl6yk+H7B/F7mJLZgfgB0EuPyPPhF8z8Rhj4Ww1Y2ZAAAAABJRU5ErkJggg==", Base64.DEFAULT);
    private Bitmap locationImage = BitmapFactory.decodeByteArray(locationDecodedString, 0, locationDecodedString.length);

    private Session.SearchListener searchListener = new Session.SearchListener() {
        @Override
        public void onSearchResponse(@NonNull Response response) {
            List<GeoObjectCollection.Item> searchResultList = response.getCollection().getChildren();

            if(searchResultList.size() > 0) {
                Point resultLocation = searchResultList.get(0).getObj().getGeometry().get(0).getPoint();
                if (resultLocation != null) {
                    MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
                    if (userSearchPlacemark != null) {
                        mapObjects.remove(userSearchPlacemark);
                    }
                    userSearchPlacemark = mapObjects.addPlacemark(resultLocation, ImageProvider.fromBitmap(locationImage));
                    WritableMap writableMap = Arguments.createMap();
                    SearchMetadata metadata = response.getMetadata();

                    writableMap.putString("location", metadata.getCorrectedRequestText());
                    writableMap.putDouble("latitude", resultLocation.getLatitude());
                    writableMap.putDouble("longitude", resultLocation.getLongitude());
                    sendNativeEvent(PROP_ON_LOCATION_SEARCH, writableMap, mapView.getId(), context);
                }
            }
        }

        @Override
        public void onSearchError(@NonNull Error error) {
            String errorMessage = "Unknown search error";
            if (error instanceof RemoteError) {
                errorMessage = "Remote server error";
            } else if (error instanceof NetworkError) {
                errorMessage = "Network error";
            }

            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("error", errorMessage);

            sendNativeEvent(PROP_ON_LOCATION_SEARCH, writableMap, mapView.getId(), context);
        }
    };

    private InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
            if(shouldSendOnMapPress) {
                WritableMap writableMap = Arguments.createMap();
                writableMap.putString("latitude", Double.toString(point.getLatitude()));
                writableMap.putString("longitude", Double.toString(point.getLongitude()));

                sendNativeEvent(PROP_ON_MAP_PRESS, writableMap, mapView.getId(), context);
            }
            if(shouldSearchLocation) {
                if (searchSession != null) {
                    searchSession.cancel();
                }
                searchSession = searchManager.submit(point, 16, new SearchOptions(), searchListener);
            }
        }

        @Override
        public void onMapLongTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {

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
        SearchFactory.initialize(context);

        this.context = context;
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

        mapView = new MapView(context);
        mapView.getMap().addInputListener(inputListener);
        mapView.getMap().getMapObjects().addCollection();

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

    public void zoomIn() {
        mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                        mapView.getMap().getCameraPosition().getZoom()+2, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1), null);
    }

    public void zoomOut() {
        mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                        mapView.getMap().getCameraPosition().getZoom()-2, 0.0f, 0.0f),
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
    public void setInitialRegion(MapView view, ReadableMap region) {
        double latitude = region.getDouble("latitude");
        double longitude = region.getDouble("longitude");
        double latitudeDelta = region.getDouble("latitudeDelta");
        double longitudeDelta = region.getDouble("longitudeDelta");

        Point point = new Point(latitude, longitude);

        view.getMap().move(
                new CameraPosition(point, 10.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null
        );
    }

    @ReactProp(name = PROP_SEARCH_LOCATION, defaultBoolean = false)
    public void setShouldSearchLocation(MapView view, boolean shouldSearch) {
        shouldSearchLocation = shouldSearch;
    }

    @ReactProp(name = PROP_POLYGONS)
    public void setPolygons(MapView view, ReadableArray polygons) {
        this.clearPolygons(view);

        if (polygons != null) {
            for (int i = 0; i < polygons.size(); i++) {
                ReadableMap polygon = polygons.getMap(i);
                ReadableArray points = polygon.getArray("points");

                ArrayList<Point> rectPoints = new ArrayList<>();

                for (int j = 0; j < points.size(); j++) {
                    ReadableMap latLng = points.getMap(j);
                    double latitude = latLng.getDouble("latitude");
                    double longitude = latLng.getDouble("longitude");

                    Point point = new Point(latitude, longitude);
                    rectPoints.add(point);
                }

                PolygonMapObject rect = view.getMap().getMapObjects().addPolygon(new Polygon(new LinearRing(rectPoints), new ArrayList<LinearRing>()));
                polygonsList.add(rect);

                rect.setStrokeColor(Color.rgb(0, 148, 113));
                rect.setStrokeWidth(1.0f);
                rect.setFillColor(Color.argb(85, 0, 148, 113));
            }
        }
    }

    public boolean onMarkerPress(MapObject mapObject, Point point) {
        if (mapObject instanceof PlacemarkMapObject) {
            WritableMap writableMap = Arguments.createMap();
            Object userData = mapObject.getUserData();

            if (userData != null) {
                try {
                    String id = userData.getClass().getField("id").toString();
                    WritableMap resultData = Arguments.createMap();
                    resultData.putString("id", id);

                    writableMap.putMap("userData", resultData);
                }
                catch (Exception e) {

                }
            }

            writableMap.putDouble("latitude", point.getLatitude());
            writableMap.putDouble("longitude", point.getLongitude());

            sendNativeEvent(PROP_ON_MARKER_PRESS, writableMap, mapView.getId(), context);
            return true;
        }
        return false;
    }

    @ReactProp(name = PROP_MARKERS)
    public void setMarkers(MapView view, ReadableArray markers) {
        this.clearMarkers(view);

        if (markers != null) {
            for (int i = 0; i < markers.size(); i++) {
                ReadableMap marker = markers.getMap(i);
                Float opacity = marker.hasKey("opacity") ? (float)marker.getDouble("opacity") : 1.0f;

                ReadableMap latLng = marker.getMap("coordinate");
                double latitude = latLng.getDouble("latitude");
                double longitude = latLng.getDouble("longitude");

                boolean draggable = marker.hasKey("draggable") && marker.getBoolean("draggable");
                Object userData = marker.hasKey("userData") ? marker.getMap("userData") : new Object();

                Point point = new Point(latitude, longitude);
                PlacemarkMapObject mark = view.getMap().getMapObjects().addPlacemark(point);
                markersList.add(mark);

                mark.setOpacity(opacity);
                mark.setIcon(ImageProvider.fromBitmap(image));
                mark.setDraggable(draggable);
                mark.setUserData(userData);
                mark.addTapListener(this::onMarkerPress);
            }
        }
    }

    public void clearMarkers(MapView view) {
        MapObjectCollection mapObjects = view.getMap().getMapObjects();

        for (PlacemarkMapObject marker : markersList) {
            mapObjects.remove(marker);
        }

        markersList.clear();
    }

    public void clearPolygons(MapView view) {
        MapObjectCollection mapObjects = view.getMap().getMapObjects();

        for (PolygonMapObject polygon : polygonsList) {
            mapObjects.remove(polygon);
        }

        polygonsList.clear();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationView.getPin().setIcon(ImageProvider.fromBitmap(locationImage));
        userLocationView.getArrow().setIcon(ImageProvider.fromBitmap(locationImage));
        userLocationView.getAccuracyCircle().setFillColor(Color.TRANSPARENT);
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
                .put(PROP_ON_MARKER_PRESS, MapBuilder.of("registrationName", PROP_ON_MARKER_PRESS))
                .put(PROP_ON_LOCATION_SEARCH, MapBuilder.of("registrationName", PROP_ON_LOCATION_SEARCH))
                .build();
    }

    private void sendNativeEvent(final String eventName, final WritableMap event, final int id, final ReactContext context) {
        context.getJSModule(RCTEventEmitter.class).receiveEvent(id, eventName, event);
    }
}
