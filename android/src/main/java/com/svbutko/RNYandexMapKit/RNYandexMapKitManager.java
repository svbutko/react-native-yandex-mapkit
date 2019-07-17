package com.svbutko.RNYandexMapKit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.LinearRing;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polygon;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolygonMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.i18n.I18nManagerFactory;
import com.yandex.runtime.i18n.LocaleUpdateListener;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class RNYandexMapKitManager extends SimpleViewManager<MapView> implements UserLocationObjectListener, ActivityCompat.OnRequestPermissionsResultCallback {
    public static final String REACT_CLASS = "RNYandexMapKit";

    public static final int NAVIGATE_TO_REGION = 1;
    public static final int ZOOM_IN = 2;
    public static final int ZOOM_OUT = 3;
    public static final int NAVIGATE_TO_USER_LOCATION = 4;
    public static final int NAVIGATE_TO_BOUNDING_BOX = 5;

    public static final String PROP_BOUNDING_BOX = "boundingBox";
    public static final String PROP_MARKERS = "markers";
    public static final String PROP_INITIAL_REGION = "initialRegion";
    public static final String PROP_POLYGONS = "polygons";
    public static final String PROP_ON_MARKER_PRESS = "onMarkerPress";
    public static final String PROP_ON_POLYGON_PRESS = "onPolygonPress";
    public static final String PROP_ON_MAP_PRESS = "onMapPress";
    public static final String PROP_ON_LOCATION_SEARCH = "onLocationSearch";
    public static final String PROP_SEARCH_LOCATION = "searchLocation";
    public static final String PROP_SEARCH_ROUTE = "searchRoute";
    public static final String PROP_SEARCH_MARKER = "searchMarker";

    private UserLocationLayer userLocationLayer;
    private ThemedReactContext context = null;
    private MapView mapView = null;

    private SearchManager searchManager;
    private Session searchSession;

    private DrivingRouter drivingRouter;

    private boolean shouldSearchLocation = false;

    private List<PolygonMapObject> polygonsList = new ArrayList<PolygonMapObject>();
    private List<PlacemarkMapObject> markersList = new ArrayList<PlacemarkMapObject>();
    private List<PolylineMapObject> polylinesList = new ArrayList<PolylineMapObject>();

    private PlacemarkMapObject userSearchPlacemark;

    //TODO: Add icon prop
    private byte[] imageDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAB4AAAAqCAYAAACk2+sZAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwwAADsMBx2+oZAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS40E0BoxAAAAkhJREFUWEftl79O40AQxv0OkMROZCAHytsc75AKIfEOQEFDAz21BQ0F0h05TgEhCoKERIEQgoIKJCr665b5otkwux4HW7FJcVj6SZv5832eOI7XgTFmKqhBn4uZmZjo9prRzsHS/HHSWbgiHpNO+wFrxJBDDWo1DR81aIHI37C+/SdqvNHa5AG16KH12BNQg2g6q9XWe80wt6EPeqFBa/UEUgEUnoSNnhSZBNZKmTsfUPC7FV3LxjJgTcfcMe1FjXPZUCasPTIfmfYb9UNZWAXsMTS3xrgN1OIK6A6NaRHTL/DSS2bS/9E2g7VV87C5MQRrxLRaDfaKC017t7Ji/r280Am7B2LIaT0ZdAM6g10l4dBfbJvXJFFN7YEcalCraUjgGRw3w1ctKYFg3gO1moYEnviq1aRlsPxz7KT+gVr0aFqST42LTGuPPFN/G6f4/4yn9qsGRabOMy0I6Fn5qCUkZf9zwTM4rde2tKRGWf/V8MRXXeiROOnTienCmLY74Y2XqAz2iqe3EWBjbPLuvILSYY+Prc8XTj2c1jfGFujCKywN1nZ3mcK8yqlH0wLfmF5dZve9holhTX1Db0EBbU0GsnESWMsxBc4HCwqLvCFmwRopU5AKWKihjOvtXFeJGgTUhOt94AnlhnvVaYEatKDxJCr+yso9maZADUogwK+ZqokP1441BWrQB0IkeC8NNLjmU1OgBjUg+KsVPUkjCedymQI1mAWE6b58loaAY7lNgRocBwyO5lp7Sad9C47i5l5RU6AGq8cE77senOxoWv4fAAAAAElFTkSuQmCC", Base64.DEFAULT);
    private Bitmap pinBitmapImage = BitmapFactory.decodeByteArray(imageDecodedString, 0, imageDecodedString.length);
    private ImageProvider pinImage = ImageProvider.fromBitmap(pinBitmapImage);

    private byte[] locationDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAYAAADFeBvrAAAAAXNSR0IArs4c6QAABVVJREFUaAXdWl1sFFUU/u7uoibSGkNiMAahUCEpSPEFo760D/JoMFh8Mr7W2JhgNUVeIFDRQkrjD1oTXxb1hQYTDCZGIt0iWFCkLEJDFQr+i4bYdtvQn90dz7kzk52Z3fndu23teZm5c+895/vmnLl/ZwRUSverDyGfb4SGekBbQ9eVdK0GRJVuRsvQ/RgEhuk6RNc0YrFeNO//SRUMUbai7pfrkRPPEfCt0LRlkfQJ8SsRPIy49hGaD6Qj6TA6RSf0/raNyKGD9DSUA6BE3xTiaMMLXd+WqPN9FJ7QwW219Db3kjeafLWX00CIHvL6DrzYdTWMmuCEenclMJh5E8i/RN/GojBGIrcVmAFib6Ouajsad2WD6AlGKPnaEoxP9ZBXGoMoVd5GiF4svrMJz79xy0+3P6F329ZCTH+mj1h+6ipYzyOjdsdTaOm47GUl5lWJ7tYnIWb655wMg+QpgLEwJg9x95D0DJPRjDnEQ8tsVgmRgbboMTdPlfYQfzMyzOYZGX5x/IIZG2MsIcWEeDSTAwDP8vNUOPwYI2N1SDEhHprnajRzgPMsMkY5jdhb2b8hOWlicNbmGTuW8CU5T6HOOvk6PMQrgFmaNMPDL+4hsRJmixQ8pK/NzlrqZv32gbvvweaadThz82d8/89vwe3H8ai59it8VPpCM7gSRS2ZxJZV69G0qh5P3F8DIQRa+o6EI6Rjb2RIuod4C5DVLijC6KumFAmzk6ZpWJbcjd8nRs1Hwa4JsYG3HrqH9P1MsI4RW3mRsKo8/ef18GRYgeQAgxBvziogQUlYTfdci7q/kxxeEeBtczb7o1VpOfdRSJj2IoebqSCRWJ2QZwDmg4jXckhYTUYON1MJnWckaN6hA43wYpLYWrsBjy9dIUen8FrsPaKHm6GHuNCgQKczAaUSJEzTHG5Hrl00ixGv2hr2kO8i9Omah9H6SIMyT5RCW3a4sVLiQksfPjfzlrVLlmLjfQ8qCSs3S2WHm1SsVRMh8xDQzRTQfu44aj9+HQd/OIXJLJ1bKBY14cagRJVjceqO9JfxEbSc/BQrDu3BvvMnkJmedG8cskZJuBk2OeToeDa43Lw9jrb+Y1hOxHae/QK3JieCd3ZpqSbcWLmW4ZAbc7Hj+fjfqdvYfe5LLE/uQeupo/gj7NrL0K4u3FihGIvpB+ee2D0rJ7LTOJDuw8pD7WhO9WB41PfozKZPZbgxF/bQkM1CxMJUPocPLvdj9Sd7saP/88Ba1IUbmxRD7KGoq8GSoHM0Qe4bOBHIU2rDjfkgHZP5mZLQoj9kUh3nv/JVoDTc2BrlmmIy2STzM772QzVIXvnOd6BQGm7MgRJnxjxEySbFwt9U50DKVavycOOEGYlOiDNnFRAeJNzmqW/+uhFtZ+qG0+CgE9LTgCm3tlGf85D+Vvpkye6Hryo9wkiZqUwj5MgmpwErIO9c/LpomaQ83CzYC4Q4pynTgGpZjdCa771Lp21KlYYbY7bkYwuEpEnKaerHqzYA5Ra6LvTZVunKwk1iJcwWsROSCVrKaSoWXtB+OHhGalUbboTVkVS2E2KTnKDlnKZi2T/Qi5lcDsrCjTEyVofEHWUgmcrj2YZjmMk+Q3X3FtVHfDBK35JGfY8OX8KVkb8jajG6cb518V2bsLm9aO9SOKx3mlhQKUkmx9nmuNhC4RdqA+h8L0rLjIUxeWTCi78hK4LmzuMyQSt/NrJWzMG9TOtTspgxeYh7yFk7/Y9+vPD2kEmK/+Coq94EEeusxDxlmim68jzDNtl2gL9IuH8wD1ktLZifl6yk+H7B/F7mJLZgfgB0EuPyPPhF8z8Rhj4Ww1Y2ZAAAAABJRU5ErkJggg==", Base64.DEFAULT);
    private Bitmap locationBitmapImage = BitmapFactory.decodeByteArray(locationDecodedString, 0, locationDecodedString.length);
    private ImageProvider locationImage = ImageProvider.fromBitmap(locationBitmapImage);

    private byte[] selectedPinDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAB4AAAAqCAYAAACk2+sZAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwwAADsMBx2+oZAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS40E0BoxAAAAuVJREFUWEfFlz9oE2EYxl8QRBFE3BTURVx06uBYJzfBTVxcVbrdl7RFp6CbOuYSIy1FEaxZFGwHi3YsSNFBRMW1FlFoBSuY1sj5vHdfkrt+T/5cc0c/+CWX533f57lr7q45CYJgV6BiN2SmdEjKhVGpepfF9wpSMSbcVg01NtMNKsaRR8UDCBmTslnA+5aUvYAS1sKeMZ1hXnGoqEi9vkd8cw18o0G90BmdhQfzVrhYu3kEe/+GmqYCHvCiGY5QGx/B3n7lRjtAveDp5CQ++N5JNK5Tg2FQT3gnstobj0sHUfxIB7NAvZHhBvtmmg5kCTISwVKePI0T4R9tjnHi4e3gztvXwfu11WBjazNEt1XTGptJ4HtNzeoE+95z2hhjYulF0Gg20c6X1sbRw2YT+OYZ2kVkyjsc7Qlpstx9txi5D7C0l3m00SzNxO3uCm2wXJyfspaDL51hXh2QiZcnvBjxef2HtRt8ffr5nXp1QGavO9TI7D1rlX7pLPOMQCY2VtxCxNXFp9Ym/dJZ5mlZ0SPeJIWQG0tz1ib90lnmGYHMXrfI3I5YM3F6f6FFkNt3rJl4eUmLllzOas3EYZdo0ZLLdayZcr9wnhZjZHrnUjRT6qW9OMvWaEOMzO7VmqWZmNHvucqbkgz930mpmEqYGb5UvbO0KQ+Q1Q6Ojtq8oo1Zgox2XntDf5Sz5kwpjDrBUTh+kNOBLDALiazEhwfmFJoa7tDQNNQ7kRX/EAq+uUUGhwOeTo4jzJT24fL6QA12gnrB08nZLoRitXgGe/mHGqVBPeBFM5ioYE/xhEjM0gAP5q1QsQX2eJYaDgJmmWcLKraQurcfJsuOaX+WdZZ5tqBiHJmeOIprcJWYc/TpEDPMKw4Vt2MfXX/RoDjaQx5JGVRkSMU7B+PuZ7rW0MNmGVTshlTNBfzZ/7rB0FBjM92gYi+kXLyEy2QjFvpbNdbbCyr2Q2qTx6VSuB7im2Ospx9UzJ9A/gNsGGyJMIipoAAAAABJRU5ErkJggg==", Base64.DEFAULT);
    private Bitmap selectedPinBitmapImage = BitmapFactory.decodeByteArray(selectedPinDecodedString, 0, selectedPinDecodedString.length);
    private ImageProvider selectedPinImage = ImageProvider.fromBitmap(selectedPinBitmapImage);

    private byte[] userLocationDecodedString = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAYAAADFeBvrAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwwAADsMBx2+oZAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS40E0BoxAAABAxJREFUaEPdWltPE0EU3gffffAvaLzHxAf/gX/F90KM4Y2ECGLwjSJeeNBAYkh4MGqiUG4BGwSC3CIUbIEil5oGERSjL+N8p1Niu9PuzpntLvFLvrTN7s75zs7MmXNm6gSKztvnnI76W068Pu7E6xJOe13GaY/l5efvAul7hq7hHtyLZ04UOuuuSXFtUmRWChYs4lm0gbYiw8PYDSlm2CXOnsPUdmiIx87KN9qrERIsYQO2aobhxlOFoRX7oxVQC8IWbMJ2oHjWcEa+sSGt0TAI29AQCNrvXJZvKq01FCZJg9Rihc76m/LtHGgN+OTpxw3i+ou2Y+K37j5fhBZoYoF6hucMRN+fHhQvU7Ni/+iH+Bfffh6KvuUZ0Tw1wHOONJn2FM0Z3jC71HNPTG6vK/nVMba5Ki50t2jbqUpo8z2nKJrxAsAV6Uwqv6vk+sPH3SzTKanRV/RDmNQ14MGLUtT0zoaSaYYR2VO84Se1VgUtmrx15sHMkJLHAwKGrt2qpHWq2uLLzAAwZNJ7OSWNh7ncJjdI9Cr1ZSjkZvqHPNg02a9k2YHVS6A297NINBGCgwDbIWgvAdJ2/Y2+2BK9Q6K09GBGtiJPhEMlEc+mOJO8OxXxHALhAwElsO4GAzZNvlOS7GDlEEjlPOp63UUDImxjxbcB0iCrxBWEL3LsxbUXDdk6nVDSeLDuHRC+yLGX0F405PnuZkphOHidXrTvHRC+qK0m/Q2GRD43sZVRMv1hNLtCL0PXnjmlL/JL3n2BTzj1dP69yB1+V5L12D7YEx2zYwE6Q8yjh7ABqLtoxas9raJrPinerM6L0bVlMb6eEiNrS+LVyhw5jLpJ95wdpS9BOITxj0ltS/t5RA7xhxwEIDFdyu+ogWSHha9b1J6FYzTkWEEBRhMby0pKsOhfX2I6haDACNswNriRUuZrA7wsY6cobBsurDDStzqrzNYWqIJ1GiqSFlbD1AeTNyxgC8yolyj1MUhO0XjyS1qZCwfPFye0WrQ8PmvyWT40fnirzISHg19H/hbf4/IB8FngYUGMAo/mxrV6SlhS4PkowTHccof7ykS4yO7nveeS6/TPY5MkqJ0dLlBE6nQplm2SAB7bWF0LSdV0NHgih7tOF7HiEWaVjcahzCfVdDRAkqvTRZorospW8OKOXYlti4nsZ5cm761goELEG5BVZZTAWZNLV0lkq4QKxymIMuXpfph0RTnfxymAxYFXKDQ68CrC4kiypmQdSRYRwKFxoIQW9qFxEdRTJ2D4kQZuz5SD5pQ7UIRG2DaeM16g6CfDJPPIkkVaZ6RN39GMA1p8eUeXRoQNz0UzSBRyv//g72XlQNpOQ9HijAnPog1XCRA1UAKjrsdmBe0m0RZZiH/RdJy/WRcqH3hhVqIAAAAASUVORK5CYII=", Base64.DEFAULT);
    private Bitmap userLocationBitmapImage = BitmapFactory.decodeByteArray(userLocationDecodedString, 0, userLocationDecodedString.length);
    private ImageProvider userLocationImage = ImageProvider.fromBitmap(userLocationBitmapImage);

    private Session.SearchListener searchListener = new Session.SearchListener() {
        @Override
        public void onSearchResponse(@NonNull Response response) {
            try {
                List<GeoObjectCollection.Item> searchResultList = response.getCollection().getChildren();

                if(searchResultList.size() > 0) {
                    GeoObject geoObject = searchResultList.get(0).getObj();
                    Point resultLocation = geoObject.getGeometry().get(0).getPoint();

                    if (resultLocation != null) {
                        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
                        if (userSearchPlacemark != null) {
                            try {
                                mapObjects.remove(userSearchPlacemark);
                            } catch (Exception e) {
                                //TODO: Solve the error
                            }
                        }
                        userSearchPlacemark = mapObjects.addPlacemark(resultLocation, userLocationImage);
                        WritableMap writableMap = Arguments.createMap();

                        String descriptionLocation = geoObject.getDescriptionText();
                        String location = geoObject.getName();

                        if (descriptionLocation != null) {
                            location = descriptionLocation + ", " + location;
                        }

                        writableMap.putString("location", location);
                        writableMap.putDouble("latitude", resultLocation.getLatitude());
                        writableMap.putDouble("longitude", resultLocation.getLongitude());
                        sendNativeEvent(PROP_ON_LOCATION_SEARCH, writableMap, mapView.getId(), context);
                    }
                }
            } catch (Exception e) {
                //TODO: Solve the error
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

    private LocaleUpdateListener localeUpdateListener = new LocaleUpdateListener() {
        @Override
        public void onLocaleUpdated() {

        }

        @Override
        public void onLocaleUpdateError(@NonNull Error error) {

        }
    };

    private MapObjectTapListener mapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            if (mapObject instanceof PlacemarkMapObject || mapObject instanceof PolygonMapObject) {
                WritableMap writableMap = Arguments.createMap();
                Object userData = mapObject.getUserData();

                if (userData != null) {
                    try {
                        MarkerUserData markerUserData = (MarkerUserData)userData;
                        writableMap.putString("id", markerUserData.getId());
                    }
                    catch (Exception e) {

                    }
                }

                writableMap.putDouble("latitude", point.getLatitude());
                writableMap.putDouble("longitude", point.getLongitude());

                String eventName = mapObject instanceof PlacemarkMapObject ? PROP_ON_MARKER_PRESS : PROP_ON_POLYGON_PRESS;

                sendNativeEvent(eventName, writableMap, mapView.getId(), context);
                return true;
            }
            return false;
        }
    };

    private InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("latitude", Double.toString(point.getLatitude()));
            writableMap.putString("longitude", Double.toString(point.getLongitude()));

            sendNativeEvent(PROP_ON_MAP_PRESS, writableMap, mapView.getId(), context);
            if(shouldSearchLocation) {
                if (searchSession != null) {
                    searchSession.cancel();
                }

                int zoom = (int)mapView.getMap().getCameraPosition().getZoom();
                searchSession = searchManager.submit(point, zoom, new SearchOptions(), searchListener);
            }
        }

        @Override
        public void onMapLongTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {

        }
    };

    private DrivingSession.DrivingRouteListener drivingRouteListener = new DrivingSession.DrivingRouteListener() {
        @Override
        public void onDrivingRoutes(@NonNull List<DrivingRoute> routes) {
            MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
            clearPolylines(mapView);

            if(!routes.isEmpty()) {
                for (int i = 0; i < routes.size(); i++) {
                    Polyline polylineGeometry = routes.get(i).getGeometry();
                    if (polylineGeometry.getPoints().size() > 0) {
                        PolylineMapObject polyline = mapObjects.addPolyline(polylineGeometry);
                        polyline.setStrokeColor(Color.argb(153,194, 19, 19));
                        polyline.setOutlineWidth(0);
                        polylinesList.add(polyline);
                        break;
                    }
                }
            }
        }

        @Override
        public void onDrivingRoutesError(@NonNull Error error) {
        }
    };

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
        RNYandexMapKitModule nativeModule = context.getNativeModule(RNYandexMapKitModule.class);

        String locale = nativeModule.getLocale();

        MapKitFactory.setApiKey(nativeModule.getApiKey());
        MapKitFactory.initialize(context);
        SearchFactory.initialize(context);
        DirectionsFactory.initialize(context);
        if (locale != null) {
            I18nManagerFactory.setLocale(locale, localeUpdateListener);
        }
        this.context = context;
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

        mapView = new MapView(context);
        mapView.getMap().addInputListener(inputListener);
        mapView.getMap().getMapObjects().addCollection();

        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.context.getCurrentActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            this.addUserLocationLayer();
        }

        MapKitFactory.getInstance().onStart();
        mapView.onStart();

        return mapView;
    }

    private void addUserLocationLayer() {
        userLocationLayer = mapView.getMap().getUserLocationLayer();
        userLocationLayer.setEnabled(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }

    @ReactProp(name = PROP_SEARCH_MARKER)
    public void setSearchMarker(MapView view, ReadableMap marker) {
        if (marker != null) {
            MapObjectCollection mapObjects = view.getMap().getMapObjects();

            if (userSearchPlacemark != null) {
                try {
                    mapObjects.remove(userSearchPlacemark);
                } catch (Exception e) {
                    //TODO: Solve the error
                }
            }
            ReadableMap latLng = marker.getMap("coordinate");
            double latitude = latLng.getDouble("latitude");
            double longitude = latLng.getDouble("longitude");

            userSearchPlacemark = mapObjects.addPlacemark(new Point(latitude, longitude), userLocationImage);
        }
    }

    @ReactProp(name = PROP_SEARCH_ROUTE)
    public void setSearchRoute(MapView view, ReadableArray markers) {
        if (markers.size() == 2) {
            ArrayList<Point> points = new ArrayList<>();

            for (int i = 0; i < markers.size(); i++) {
                ReadableMap marker = markers.getMap(i);
                ReadableMap latLng = marker.getMap("coordinate");
                double latitude = latLng.getDouble("latitude");
                double longitude = latLng.getDouble("longitude");

                points.add(new Point(latitude, longitude));
            }

            submitRouteRequest(points);
        }
    }

    private void submitRouteRequest(ArrayList<Point> points) {
        try {
            DrivingOptions options = new DrivingOptions();
            ArrayList<RequestPoint> requestPoints = new ArrayList<>();

            Point firstPoint = points.get(0);
            Point secondPoint = points.get(1);

            requestPoints.add(new RequestPoint(firstPoint, RequestPointType.WAYPOINT, null));
            requestPoints.add(new RequestPoint(secondPoint, RequestPointType.WAYPOINT, null));

            drivingRouter.requestRoutes(requestPoints, options, drivingRouteListener);
        } catch (Exception e) {
            //TODO: Solve the error
        }
    }

    public void setNavigateToCoordinates(ReadableMap region, boolean isAnimated) {
        double latitude = region.getDouble("latitude");
        double longitude = region.getDouble("longitude");

        Point point = new Point(latitude, longitude);

        this.navigateToCoordinates(point, isAnimated);
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
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.context.getCurrentActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            this.navigateToLocationAfterChecks();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.navigateToLocationAfterChecks();
        }
    }

    private void navigateToLocationAfterChecks() {
        try {
            if (userLocationLayer == null) {
                this.addUserLocationLayer();
            }

            CameraPosition cameraPosition = userLocationLayer.cameraPosition();
            if (cameraPosition != null) {
                Point point = cameraPosition.getTarget();
                if (point != null) {
                    mapView.getMap().move(
                            new CameraPosition(point, 18.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 1),
                            null);
                }
            }
        } catch (Exception e) {
            //TODO: Solve the error
        }
    }

    public void navigateToCoordinates(Point point, Boolean isAnimated) {
        CameraPosition position = new CameraPosition(point, mapView.getMap().getCameraPosition().getZoom(), 0.0f, 0.0f);

        if (isAnimated) {
            mapView.getMap().move(position, new Animation(Animation.Type.SMOOTH, 1), null);
        } else {
            mapView.getMap().move(position);
        }
    }

    @ReactProp(name = PROP_INITIAL_REGION)
    public void setInitialRegion(MapView view, ReadableMap region) {
        double latitude = region.getDouble("latitude");
        double longitude = region.getDouble("longitude");

        Point point = new Point(latitude, longitude);

        view.getMap().move(new CameraPosition(point, 10.0f, 0.0f, 0.0f));
    }

    @ReactProp(name = PROP_BOUNDING_BOX)
    public void setBoundingBox(MapView view, ReadableMap boundingBox) {
        ReadableMap northEastMap = boundingBox.getMap("northEastPoint");
        ReadableMap southWestMap = boundingBox.getMap("southWestPoint");

        this.navigateToBoundingBox(northEastMap, southWestMap, view);
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
                rect.addTapListener(mapObjectTapListener);

                if (polygon.hasKey("userData") && polygon.getString("identifier") != null) {
                    rect.setUserData(new MarkerUserData(polygon.getString("identifier")));
                }
            }
        }
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
                ImageProvider icon = marker.hasKey("icon") ? getImageById(marker.getString("icon")) : pinImage;

                Point point = new Point(latitude, longitude);
                PlacemarkMapObject mark = view.getMap().getMapObjects().addPlacemark(point);
                markersList.add(mark);

                mark.setOpacity(opacity);
                mark.setIcon(icon);
                mark.setDraggable(draggable);
                mark.addTapListener(mapObjectTapListener);

                if (marker.hasKey("userData") && marker.getString("identifier") != null) {
                    mark.setUserData(new MarkerUserData(marker.getString("identifier")));
                }
            }
        }
    }

    private ImageProvider getImageById(String id) {
        switch (id) {
            case "pin":
                return pinImage;
            case "selectedPin":
                return selectedPinImage;
            case "user":
                return userLocationImage;
            default:
                return pinImage;
        }
    }

    private void clearMarkers(MapView view) {
        MapObjectCollection mapObjects = view.getMap().getMapObjects();

        for (PlacemarkMapObject marker : markersList) {
            try {
                mapObjects.remove(marker);
            } catch (Exception e) {
                //TODO: Solve the error
            }
        }

        markersList.clear();
    }

    private void clearPolygons(MapView view) {
        MapObjectCollection mapObjects = view.getMap().getMapObjects();

        for (PolygonMapObject polygon : polygonsList) {
            try {
                mapObjects.remove(polygon);
            } catch (Exception e) {
                //TODO: Solve the error
            }
        }

        polygonsList.clear();
    }

    private void clearPolylines(MapView view) {
        MapObjectCollection mapObjects = view.getMap().getMapObjects();

        for (PolylineMapObject polyline : polylinesList) {
            try {
                mapObjects.remove(polyline);
            } catch (Exception e) {
                //TODO: Solve the error
            }
        }

        polylinesList.clear();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationView.getPin().setIcon(locationImage);
        userLocationView.getArrow().setIcon(locationImage);
        userLocationView.getAccuracyCircle().setFillColor(Color.TRANSPARENT);
    }

    @Override
    public void onObjectRemoved(UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(UserLocationView userLocationView, ObjectEvent objectEvent) {

    }

    public void navigateToBoundingBox(ReadableMap northEastRegion, ReadableMap southWestRegions, @Nullable MapView view) {
        double neLatitude = northEastRegion.getDouble("latitude");
        double neLongitude = northEastRegion.getDouble("longitude");

        Point northEastPoint = new Point(neLatitude, neLongitude);

        double swLatitude = southWestRegions.getDouble("latitude");
        double swLongitude = southWestRegions.getDouble("longitude");

        Point southWestPoint = new Point(swLatitude, swLongitude);

        MapView map = view != null ? view : mapView;
        CameraPosition cameraPosition = map.getMap().cameraPosition(new BoundingBox(southWestPoint, northEastPoint));
        map.getMap().move(cameraPosition);
    }

    @Override
    public void receiveCommand(MapView mapView, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(mapView, commandId, args);
        switch (commandId) {
            case NAVIGATE_TO_REGION:
                this.setNavigateToCoordinates(args.getMap(0), args.getBoolean(1));
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
            case NAVIGATE_TO_BOUNDING_BOX:
                this.navigateToBoundingBox(args.getMap(0), args.getMap(1), null);
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
                "navigateToRegion", NAVIGATE_TO_REGION,
                "zoomIn", ZOOM_IN,
                "zoomOut", ZOOM_OUT,
                "navigateToUserLocation", NAVIGATE_TO_USER_LOCATION,
                "navigateToBoundingBox", NAVIGATE_TO_BOUNDING_BOX
        );

        return map;
    }

    public static <K, V> Map<K, V> CreateMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);

        return map;
    }

    @Nullable
    @Override
    public java.util.Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(PROP_ON_MAP_PRESS, MapBuilder.of("registrationName", PROP_ON_MAP_PRESS))
                .put(PROP_ON_MARKER_PRESS, MapBuilder.of("registrationName", PROP_ON_MARKER_PRESS))
                .put(PROP_ON_LOCATION_SEARCH, MapBuilder.of("registrationName", PROP_ON_LOCATION_SEARCH))
                .put(PROP_ON_POLYGON_PRESS, MapBuilder.of("registrationName", PROP_ON_POLYGON_PRESS))
                .build();
    }

    private void sendNativeEvent(final String eventName, final WritableMap event, final int id, final ReactContext context) {
        context.getJSModule(RCTEventEmitter.class).receiveEvent(id, eventName, event);
    }
}

class MarkerUserData {
    private String id;

    public MarkerUserData(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "id: " + this.id;
    }
}
