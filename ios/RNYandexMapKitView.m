#import "RNYandexMapKitView.h"
#import "NSStringCategory.h"
#import "WSPoint.h"

@implementation RNYandexMapKitView

static NSString* addressKey = @"address";
static NSString* addressesKey = @"addresses";
static NSString* iconKey = @"icon";
static NSString* selectedIconKey = @"selectedIcon";
static NSString* iconImage = @"iVBORw0KGgoAAAANSUhEUgAAAB4AAAAqCAYAAACk2+sZAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwwAADsMBx2+oZAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS40E0BoxAAAAkhJREFUWEftl79O40AQxv0OkMROZCAHytsc75AKIfEOQEFDAz21BQ0F0h05TgEhCoKERIEQgoIKJCr665b5otkwux4HW7FJcVj6SZv5832eOI7XgTFmKqhBn4uZmZjo9prRzsHS/HHSWbgiHpNO+wFrxJBDDWo1DR81aIHI37C+/SdqvNHa5AG16KH12BNQg2g6q9XWe80wt6EPeqFBa/UEUgEUnoSNnhSZBNZKmTsfUPC7FV3LxjJgTcfcMe1FjXPZUCasPTIfmfYb9UNZWAXsMTS3xrgN1OIK6A6NaRHTL/DSS2bS/9E2g7VV87C5MQRrxLRaDfaKC017t7Ji/r280Am7B2LIaT0ZdAM6g10l4dBfbJvXJFFN7YEcalCraUjgGRw3w1ctKYFg3gO1moYEnviq1aRlsPxz7KT+gVr0aFqST42LTGuPPFN/G6f4/4yn9qsGRabOMy0I6Fn5qCUkZf9zwTM4rde2tKRGWf/V8MRXXeiROOnTienCmLY74Y2XqAz2iqe3EWBjbPLuvILSYY+Prc8XTj2c1jfGFujCKywN1nZ3mcK8yqlH0wLfmF5dZve9holhTX1Db0EBbU0GsnESWMsxBc4HCwqLvCFmwRopU5AKWKihjOvtXFeJGgTUhOt94AnlhnvVaYEatKDxJCr+yso9maZADUogwK+ZqokP1441BWrQB0IkeC8NNLjmU1OgBjUg+KsVPUkjCedymQI1mAWE6b58loaAY7lNgRocBwyO5lp7Sad9C47i5l5RU6AGq8cE77senOxoWv4fAAAAAElFTkSuQmCC";
static NSString* locationImage = @"iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAYAAADFeBvrAAAAAXNSR0IArs4c6QAABVVJREFUaAXdWl1sFFUU/u7uoibSGkNiMAahUCEpSPEFo760D/JoMFh8Mr7W2JhgNUVeIFDRQkrjD1oTXxb1hQYTDCZGIt0iWFCkLEJDFQr+i4bYdtvQn90dz7kzk52Z3fndu23teZm5c+895/vmnLl/ZwRUSverDyGfb4SGekBbQ9eVdK0GRJVuRsvQ/RgEhuk6RNc0YrFeNO//SRUMUbai7pfrkRPPEfCt0LRlkfQJ8SsRPIy49hGaD6Qj6TA6RSf0/raNyKGD9DSUA6BE3xTiaMMLXd+WqPN9FJ7QwW219Db3kjeafLWX00CIHvL6DrzYdTWMmuCEenclMJh5E8i/RN/GojBGIrcVmAFib6Ouajsad2WD6AlGKPnaEoxP9ZBXGoMoVd5GiF4svrMJz79xy0+3P6F329ZCTH+mj1h+6ipYzyOjdsdTaOm47GUl5lWJ7tYnIWb655wMg+QpgLEwJg9x95D0DJPRjDnEQ8tsVgmRgbboMTdPlfYQfzMyzOYZGX5x/IIZG2MsIcWEeDSTAwDP8vNUOPwYI2N1SDEhHprnajRzgPMsMkY5jdhb2b8hOWlicNbmGTuW8CU5T6HOOvk6PMQrgFmaNMPDL+4hsRJmixQ8pK/NzlrqZv32gbvvweaadThz82d8/89vwe3H8ai59it8VPpCM7gSRS2ZxJZV69G0qh5P3F8DIQRa+o6EI6Rjb2RIuod4C5DVLijC6KumFAmzk6ZpWJbcjd8nRs1Hwa4JsYG3HrqH9P1MsI4RW3mRsKo8/ef18GRYgeQAgxBvziogQUlYTfdci7q/kxxeEeBtczb7o1VpOfdRSJj2IoebqSCRWJ2QZwDmg4jXckhYTUYON1MJnWckaN6hA43wYpLYWrsBjy9dIUen8FrsPaKHm6GHuNCgQKczAaUSJEzTHG5Hrl00ixGv2hr2kO8i9Omah9H6SIMyT5RCW3a4sVLiQksfPjfzlrVLlmLjfQ8qCSs3S2WHm1SsVRMh8xDQzRTQfu44aj9+HQd/OIXJLJ1bKBY14cagRJVjceqO9JfxEbSc/BQrDu3BvvMnkJmedG8cskZJuBk2OeToeDa43Lw9jrb+Y1hOxHae/QK3JieCd3ZpqSbcWLmW4ZAbc7Hj+fjfqdvYfe5LLE/uQeupo/gj7NrL0K4u3FihGIvpB+ee2D0rJ7LTOJDuw8pD7WhO9WB41PfozKZPZbgxF/bQkM1CxMJUPocPLvdj9Sd7saP/88Ba1IUbmxRD7KGoq8GSoHM0Qe4bOBHIU2rDjfkgHZP5mZLQoj9kUh3nv/JVoDTc2BrlmmIy2STzM772QzVIXvnOd6BQGm7MgRJnxjxEySbFwt9U50DKVavycOOEGYlOiDNnFRAeJNzmqW/+uhFtZ+qG0+CgE9LTgCm3tlGf85D+Vvpkye6Hryo9wkiZqUwj5MgmpwErIO9c/LpomaQ83CzYC4Q4pynTgGpZjdCa771Lp21KlYYbY7bkYwuEpEnKaerHqzYA5Ra6LvTZVunKwk1iJcwWsROSCVrKaSoWXtB+OHhGalUbboTVkVS2E2KTnKDlnKZi2T/Qi5lcDsrCjTEyVofEHWUgmcrj2YZjmMk+Q3X3FtVHfDBK35JGfY8OX8KVkb8jajG6cb518V2bsLm9aO9SOKx3mlhQKUkmx9nmuNhC4RdqA+h8L0rLjIUxeWTCi78hK4LmzuMyQSt/NrJWzMG9TOtTspgxeYh7yFk7/Y9+vPD2kEmK/+Coq94EEeusxDxlmim68jzDNtl2gL9IuH8wD1ktLZifl6yk+H7B/F7mJLZgfgB0EuPyPPhF8z8Rhj4Ww1Y2ZAAAAABJRU5ErkJggg==";

+ (NSString*) addressKey { return addressKey; }
+ (NSString*) iconKey { return iconKey; }
+ (NSString*) iconImage { return iconImage; }
+ (NSString*) locationImage { return locationImage; }


- (instancetype) init
{
    self = [super init];

    _map = [[YMKMapView alloc] initWithFrame:self.bounds];
    _map.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

    [_map.mapWindow.map addInputListenerWithInputListener: self];
    [_map.mapWindow.map addCameraListenerWithCameraListener: self];

    [self addSubview:_map];

    _searchManager = [YMKMapKit.sharedInstance createSearchManagerWithSearchManagerType:YMKSearchSearchManagerTypeCombined];

    self.locationManager = [CLLocationManager new];
    self.locationManager.delegate = self;

    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    if (status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse) {
        YMKUserLocationLayer* userLocationLayer = _map.mapWindow.map.userLocationLayer;
        [userLocationLayer setEnabled: true];
        [userLocationLayer setHeadingEnabled: true];
        [userLocationLayer setObjectListenerWithObjectListener: self];
    }
    _userLocationIcon = [locationImage decodeBase64ToImage];

    _zoom = 0;

    return self;
}


- (void)willMoveToSuperview:(nullable UIView *)newSuperview {
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    if (status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse) {
        YMKUserLocationLayer* userLocationLayer = _map.mapWindow.map.userLocationLayer;
        [userLocationLayer setEnabled: true];
        [userLocationLayer setHeadingEnabled: true];
        [userLocationLayer setObjectListenerWithObjectListener: self];
    }
}


- (void) onMapTapWithMap:(nullable YMKMap *)map point:(nonnull YMKPoint *)point
{
    self.onMapTapped(@{});

    [[UIApplication sharedApplication] sendAction:@selector(resignFirstResponder) to:nil from:nil forEvent:nil];

    YMKSearchOptions* options = [YMKSearchOptions new];
    options.searchTypes = YMKSearchTypeGeo;

    _searchSession = [_searchManager submitWithPoint:point
                                                zoom:@(_zoom)
                                       searchOptions:options
                                     responseHandler:^(YMKSearchResponse *response, NSError *error) {

                                         NSMutableArray* addressArray = [NSMutableArray new];

                                         for (YMKGeoObjectCollectionItem* item in response.collection.children) {
                                             [addressArray addObject:item.obj.name];
                                         }

                                         id errorInfo = [NSNull null];
                                         if (error != nil) { errorInfo = error.localizedDescription; }

                                         NSDictionary* addressDict = @{
                                                                       @"latitude" : [NSString stringWithFormat:@"%f", point.latitude],
                                                                       @"longitude" : [NSString stringWithFormat:@"%f", point.longitude],
                                                                       addressKey: response.collection.children.firstObject.obj.name,
                                                                       addressesKey: addressArray,
                                                                       @"error": errorInfo
                                                                       };

                                         NSLog(@"onMapTapped with output info: %@", addressDict);

                                         self.onPointChanged(addressDict);
                                     }];


}


- (void) onMapLongTapWithMap:(nullable YMKMap *)map point:(nonnull YMKPoint *)point
{
    [self onMapTapWithMap:map point:point];
}


- (void) addMarkerWithJSON: (id)json {
    NSDictionary* dict = nil;
    if ([json isKindOfClass:[NSDictionary class]]) {
        dict = (NSDictionary*)json;
    }
    WSPoint* customPoint = [[WSPoint alloc] initWithJSON:dict[addressKey]];
    customPoint.icon =  [dict[iconKey] decodeBase64ToImage];
    if (customPoint == nil) { return; }

    YMKMapObjectCollection* mapObjects = _map.mapWindow.map.mapObjects;
    [mapObjects clear];
    YMKPoint* point = [YMKPoint pointWithLatitude:customPoint.latitude longitude:customPoint.longitude];
    YMKPlacemarkMapObject* placemark = [mapObjects addPlacemarkWithPoint:point];

    [placemark setIconWithImage: customPoint.icon];
}


- (void) navigateToUserLocation {
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];

    if (status == kCLAuthorizationStatusNotDetermined) {
        [self.locationManager requestWhenInUseAuthorization];
    }

    if (status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse) {
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{
            YMKPoint* point = self.getDeviceLocation;
            YMKCameraPosition* cameraPos = [YMKCameraPosition cameraPositionWithTarget:point zoom:18 azimuth:0 tilt:0];
            YMKAnimation* animation = [YMKAnimation animationWithType:YMKAnimationTypeSmooth duration:1];

            [self.map.mapWindow.map moveWithCameraPosition:cameraPos animationType:animation cameraCallback:nil];
        }];
    }
}

- (void) zoomIn {
    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
        YMKPoint* point = self.map.mapWindow.map.cameraPosition.target;
        float* zoom = self.map.mapWindow.cameraPosition.zoom + 2;

        YMKCameraPosition* cameraPos = [YMKCameraPosition cameraPositionWithTarget:point zoom:zoom azimuth:0 tilt:0];
        YMKAnimation* animation = [YMKAnimation animationWithType:YMKAnimationTypeSmooth duration:1];

        [self.map.mapWindow.map moveWithCameraPosition:cameraPos animationType:animation cameraCallback:nil];
    }];
}

- (void) zoomOut {
    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
        YMKPoint* point = self.map.mapWindow.map.cameraPosition.target;
        float* zoom = self.map.mapWindow.cameraPosition.zoom - 2;

        YMKCameraPosition* cameraPos = [YMKCameraPosition cameraPositionWithTarget:point zoom:zoom azimuth:0 tilt:0];
        YMKAnimation* animation = [YMKAnimation animationWithType:YMKAnimationTypeSmooth duration:1];

        [self.map.mapWindow.map moveWithCameraPosition:cameraPos animationType:animation cameraCallback:nil];
    }];
}


- (YMKPoint*) getDeviceLocation
{
    YMKUserLocationLayer* userLocationLayer = _map.mapWindow.map.userLocationLayer;
    _userLocationIcon = [locationImage decodeBase64ToImage];
    [userLocationLayer setEnabled: true];
    [userLocationLayer setHeadingEnabled: true];
    [userLocationLayer setObjectListenerWithObjectListener: self];

    YMKPoint* lastUserLocation = _map.mapWindow.map.userLocationLayer.cameraPosition.target;
    YMKPoint* point = [YMKPoint pointWithLatitude:lastUserLocation.latitude longitude:lastUserLocation.longitude];
    return point;
}


- (void)onObjectAddedWithView:(nullable YMKUserLocationView *)view
{
    [view.pin setIconWithImage: _userLocationIcon];
    [view.arrow setIconWithImage: _userLocationIcon];
    view.accuracyCircle.fillColor = UIColor.clearColor;
}


- (void)onObjectRemovedWithView:(nullable YMKUserLocationView *)view { }


- (void)onObjectUpdatedWithView:(nullable YMKUserLocationView *)view event:(nullable YMKObjectEvent *)event { }


- (void)onCameraPositionChangedWithMap:(nullable YMKMap *)map cameraPosition:(nonnull YMKCameraPosition *)cameraPosition cameraUpdateSource:(YMKCameraUpdateSource)cameraUpdateSource finished:(BOOL)finished
{
    _zoom = cameraPosition.zoom;

    [[UIApplication sharedApplication] sendAction:@selector(resignFirstResponder) to:nil from:nil forEvent:nil];
}


- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSLog(@"locationManager didFailWithError: %@", error.localizedDescription);

    self.onLocationError(@{@"error": error.localizedDescription});
}

@end
