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
static NSString* selectedPinImage = @"iVBORw0KGgoAAAANSUhEUgAAAB4AAAAqCAYAAACk2+sZAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwwAADsMBx2+oZAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS40E0BoxAAAAuVJREFUWEfFlz9oE2EYxl8QRBFE3BTURVx06uBYJzfBTVxcVbrdl7RFp6CbOuYSIy1FEaxZFGwHi3YsSNFBRMW1FlFoBSuY1sj5vHdfkrt+T/5cc0c/+CWX533f57lr7q45CYJgV6BiN2SmdEjKhVGpepfF9wpSMSbcVg01NtMNKsaRR8UDCBmTslnA+5aUvYAS1sKeMZ1hXnGoqEi9vkd8cw18o0G90BmdhQfzVrhYu3kEe/+GmqYCHvCiGY5QGx/B3n7lRjtAveDp5CQ++N5JNK5Tg2FQT3gnstobj0sHUfxIB7NAvZHhBvtmmg5kCTISwVKePI0T4R9tjnHi4e3gztvXwfu11WBjazNEt1XTGptJ4HtNzeoE+95z2hhjYulF0Gg20c6X1sbRw2YT+OYZ2kVkyjsc7Qlpstx9txi5D7C0l3m00SzNxO3uCm2wXJyfspaDL51hXh2QiZcnvBjxef2HtRt8ffr5nXp1QGavO9TI7D1rlX7pLPOMQCY2VtxCxNXFp9Ym/dJZ5mlZ0SPeJIWQG0tz1ib90lnmGYHMXrfI3I5YM3F6f6FFkNt3rJl4eUmLllzOas3EYZdo0ZLLdayZcr9wnhZjZHrnUjRT6qW9OMvWaEOMzO7VmqWZmNHvucqbkgz930mpmEqYGb5UvbO0KQ+Q1Q6Ojtq8oo1Zgox2XntDf5Sz5kwpjDrBUTh+kNOBLDALiazEhwfmFJoa7tDQNNQ7kRX/EAq+uUUGhwOeTo4jzJT24fL6QA12gnrB08nZLoRitXgGe/mHGqVBPeBFM5ioYE/xhEjM0gAP5q1QsQX2eJYaDgJmmWcLKraQurcfJsuOaX+WdZZ5tqBiHJmeOIprcJWYc/TpEDPMKw4Vt2MfXX/RoDjaQx5JGVRkSMU7B+PuZ7rW0MNmGVTshlTNBfzZ/7rB0FBjM92gYi+kXLyEy2QjFvpbNdbbCyr2Q2qTx6VSuB7im2Ospx9UzJ9A/gNsGGyJMIipoAAAAABJRU5ErkJggg==";
static NSString* userLocationImage = @"iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAYAAADFeBvrAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwwAADsMBx2+oZAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS40E0BoxAAABAxJREFUaEPdWltPE0EU3gffffAvaLzHxAf/gX/F90KM4Y2ECGLwjSJeeNBAYkh4MGqiUG4BGwSC3CIUbIEil5oGERSjL+N8p1Niu9PuzpntLvFLvrTN7s75zs7MmXNm6gSKztvnnI76W068Pu7E6xJOe13GaY/l5efvAul7hq7hHtyLZ04UOuuuSXFtUmRWChYs4lm0gbYiw8PYDSlm2CXOnsPUdmiIx87KN9qrERIsYQO2aobhxlOFoRX7oxVQC8IWbMJ2oHjWcEa+sSGt0TAI29AQCNrvXJZvKq01FCZJg9Rihc76m/LtHGgN+OTpxw3i+ou2Y+K37j5fhBZoYoF6hucMRN+fHhQvU7Ni/+iH+Bfffh6KvuUZ0Tw1wHOONJn2FM0Z3jC71HNPTG6vK/nVMba5Ki50t2jbqUpo8z2nKJrxAsAV6Uwqv6vk+sPH3SzTKanRV/RDmNQ14MGLUtT0zoaSaYYR2VO84Se1VgUtmrx15sHMkJLHAwKGrt2qpHWq2uLLzAAwZNJ7OSWNh7ncJjdI9Cr1ZSjkZvqHPNg02a9k2YHVS6A297NINBGCgwDbIWgvAdJ2/Y2+2BK9Q6K09GBGtiJPhEMlEc+mOJO8OxXxHALhAwElsO4GAzZNvlOS7GDlEEjlPOp63UUDImxjxbcB0iCrxBWEL3LsxbUXDdk6nVDSeLDuHRC+yLGX0F405PnuZkphOHidXrTvHRC+qK0m/Q2GRD43sZVRMv1hNLtCL0PXnjmlL/JL3n2BTzj1dP69yB1+V5L12D7YEx2zYwE6Q8yjh7ABqLtoxas9raJrPinerM6L0bVlMb6eEiNrS+LVyhw5jLpJ95wdpS9BOITxj0ltS/t5RA7xhxwEIDFdyu+ogWSHha9b1J6FYzTkWEEBRhMby0pKsOhfX2I6haDACNswNriRUuZrA7wsY6cobBsurDDStzqrzNYWqIJ1GiqSFlbD1AeTNyxgC8yolyj1MUhO0XjyS1qZCwfPFye0WrQ8PmvyWT40fnirzISHg19H/hbf4/IB8FngYUGMAo/mxrV6SlhS4PkowTHccof7ykS4yO7nveeS6/TPY5MkqJ0dLlBE6nQplm2SAB7bWF0LSdV0NHgih7tOF7HiEWaVjcahzCfVdDRAkqvTRZorospW8OKOXYlti4nsZ5cm761goELEG5BVZZTAWZNLV0lkq4QKxymIMuXpfph0RTnfxymAxYFXKDQ68CrC4kiypmQdSRYRwKFxoIQW9qFxEdRTJ2D4kQZuz5SD5pQ7UIRG2DaeM16g6CfDJPPIkkVaZ6RN39GMA1p8eUeXRoQNz0UzSBRyv//g72XlQNpOQ9HijAnPog1XCRA1UAKjrsdmBe0m0RZZiH/RdJy/WRcqH3hhVqIAAAAASUVORK5CYII=";

+ (NSString*) addressKey { return addressKey; }
+ (NSString*) iconKey { return iconKey; }
+ (NSString*) iconImage { return iconImage; }
+ (NSString*) locationImage { return locationImage; }

- (instancetype) init
{
    self = [super init];

    _mapPolygons = [[NSMutableArray alloc]init];
    _mapMarkers = [[NSMutableArray alloc]init];
    _mapPolylines = [[NSMutableArray alloc]init];

    _map = [[YMKMapView alloc] initWithFrame:self.bounds];
    _map.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

    [_map.mapWindow.map addInputListenerWithInputListener: self];
    [_map.mapWindow.map addCameraListenerWithCameraListener: self];

    [self addSubview:_map];

    _searchManager = [YMKSearch.sharedInstance createSearchManagerWithSearchManagerType:YMKSearchSearchManagerTypeCombined];

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


- (void) willMoveToSuperview:(nullable UIView *)newSuperview {
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    if (status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse) {
        YMKUserLocationLayer* userLocationLayer = _map.mapWindow.map.userLocationLayer;
        [userLocationLayer setEnabled: true];
        [userLocationLayer setHeadingEnabled: true];
        [userLocationLayer setObjectListenerWithObjectListener: self];
    }
}


- (void) onMapTapWithMap:(YMKMap *)map point:(nonnull YMKPoint *)point
{
    [[UIApplication sharedApplication] sendAction:@selector(resignFirstResponder) to:nil from:nil forEvent:nil];

    if (_searchLocation) {
        YMKSearchOptions* options = [YMKSearchOptions new];
        options.searchTypes = YMKSearchTypeGeo;

        _searchSession = [_searchManager submitWithPoint:point
                                                    zoom:@(_zoom)
                                           searchOptions:options
                                         responseHandler:^(YMKSearchResponse *response, NSError *error) {
                                             NSArray* searchResultList = [[response collection] children];

                                             if ([searchResultList count] > 0) {
                                                 YMKGeoObject* geoObject = [searchResultList[0] obj];
                                                 YMKPoint* resultLocation = [[geoObject geometry][0] point];

                                                 if (resultLocation != nil) {
                                                     YMKMapObjectCollection* mapObjects = self.map.mapWindow.map.mapObjects;
                                                     if (self.userSearchPlacemark != nil) {
                                                         @try {
                                                             [mapObjects removeWithMapObject: self.userSearchPlacemark];
                                                         } @catch (NSException *exception) {
                                                             //TODO: Solve the error
                                                         }
                                                     }

                                                     self.userSearchPlacemark = [mapObjects addPlacemarkWithPoint:resultLocation image:[userLocationImage decodeBase64ToImage]];
                                                     NSString* location = [NSString stringWithFormat:@"%@/%@/%@", [geoObject descriptionText], @", ", [geoObject name]];

                                                     NSDictionary* addressDict = @{
                                                                                   @"latitude" : [NSString stringWithFormat:@"%f", point.latitude],
                                                                                   @"longitude" : [NSString stringWithFormat:@"%f", point.longitude],
                                                                                   @"location": location,
                                                                                   };

                                                     NSLog(@"onLocationSearch with output info: %@", addressDict);

                                                     self.onLocationSearch(addressDict);
                                                 }
                                             }
                                         }];

    } else {
        NSDictionary* addressDict = @{
                                      @"latitude" : [NSString stringWithFormat:@"%f", point.latitude],
                                      @"longitude" : [NSString stringWithFormat:@"%f", point.longitude]
                                      };

        NSLog(@"onMapPress with output info: %@", addressDict);
        self.onMapPress(addressDict);
    }
}

- (void) setSearchMarker:(NSDictionary *)searchMarker {
    YMKMapObjectCollection* mapObjects = _map.mapWindow.map.mapObjects;

    if (_userSearchPlacemark != nil) {
        @try {
            [mapObjects removeWithMapObject: self.userSearchPlacemark];
        } @catch (NSException *exception) {
            //TODO: Solve the error
        }

        NSDictionary *coordinates = [searchMarker objectForKey:@"coordinate"];

        double latitude = [[coordinates valueForKey:@"latitude"] doubleValue];
        double longitude = [[coordinates valueForKey:@"longitude"] doubleValue];

        YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];

        _userSearchPlacemark = [mapObjects addPlacemarkWithPoint:point image:[userLocationImage decodeBase64ToImage]];

    }
}

- (void) onMapLongTapWithMap:(YMKMap *)map point:(nonnull YMKPoint *)point
{
    [self onMapTapWithMap:map point:point];
}

- (void) setSearchRoute:(NSArray *)searchRoute {
    NSMutableArray* points = [[NSMutableArray alloc]init];

    for (NSDictionary *marker in searchRoute) {
        NSDictionary *coordinates = [marker objectForKey:@"coordinate"];

        double latitude = [[coordinates valueForKey:@"latitude"] doubleValue];
        double longitude = [[coordinates valueForKey:@"longitude"] doubleValue];

        YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];
        [points addObject:point];
    }

    [self submitRouteRequest:points];
}

- (void) submitRouteRequest:(NSMutableArray *)points {
    @try {
        if ([points count] == 2) {
            NSMutableArray* requestPoints = [[NSMutableArray alloc]init];
            YMKDrivingDrivingOptions *options = [[YMKDrivingDrivingOptions alloc]init];

            YMKRequestPoint *firstPoint = [YMKRequestPoint requestPointWithPoint:[points objectAtIndex:0] type:YMKRequestPointTypeWaypoint pointContext:nil];
            YMKRequestPoint *secondPoint = [YMKRequestPoint requestPointWithPoint:[points objectAtIndex:1] type:YMKRequestPointTypeWaypoint pointContext:nil];
            [requestPoints addObject:firstPoint];
            [requestPoints addObject:secondPoint];

            [_drivingRouter requestRoutesWithPoints:requestPoints
                                         drivingOptions:options
                                           routeHandler:^(NSArray<YMKDrivingRoute *> *routes, NSError *error) {
                                               [self clearPolylines];
                                               YMKMapObjectCollection* mapObjects = self.map.mapWindow.map.mapObjects;

                                               if ([routes count] > 0) {
                                                   YMKPolylineMapObject *polyline = [mapObjects addPolylineWithPolyline:[[routes objectAtIndex:0] geometry]];
                                                   [polyline setStrokeColor:[UIColor colorWithRed:194.0f/255.0f
                                                                                           green:19.0f/255.0f
                                                                                            blue:19.0f/255.0f
                                                                                           alpha:1.0f]];
                                                   [self.mapPolylines addObject:polyline];
                                               }
                                           }];
        }
    } @catch (NSException *exception) {
        //TODO: Solve the error
    }
}


- (void) addMarkerWithJSON: (NSMutableDictionary*)dict {
    UIImage* icon = [self getImageByID:[dict valueForKey:@"icon"]];
    double longitude = [[dict objectForKey:@"longitude"] doubleValue];
    double latitude = [[dict objectForKey:@"latitude"] doubleValue];

    YMKMapObjectCollection* mapObjects = _map.mapWindow.map.mapObjects;
    YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];
    YMKPlacemarkMapObject* placemark = [mapObjects addPlacemarkWithPoint:point];

    [_mapMarkers addObject:placemark];

    [placemark setIconWithImage: icon];
    [placemark setOpacity: 1];
    [placemark setDraggable: false];
    if (dict[@"id"] != nil) {
        NSMutableDictionary * userData = [[NSMutableDictionary alloc]init];
        [userData setValue:dict[@"id"] forKey:@"id"];
        [placemark setUserData: userData];
    }
    [placemark addTapListenerWithTapListener: self];
}

- (UIImage*) getImageByID:(NSString *)imageId {
    if ([imageId isEqualToString:@"pin"]) {
        return [iconImage decodeBase64ToImage];
    } else if ([imageId isEqualToString:@"selectedPin"]) {
        return [selectedPinImage decodeBase64ToImage];
    } else if ([imageId isEqualToString:@"user"]) {
        return [userLocationImage decodeBase64ToImage];
    } else {
        return [iconImage decodeBase64ToImage];
    }
}

- (void) addPolygon: (NSMutableArray*)rectPoints {
    YMKPolygon *jsPolygon = [YMKPolygon polygonWithOuterRing:[YMKLinearRing linearRingWithPoints:rectPoints] innerRings:[[NSMutableArray alloc]init]];

    YMKMapObjectCollection* mapObjects = _map.mapWindow.map.mapObjects;
    YMKPolygonMapObject* polygon = [mapObjects addPolygonWithPolygon:jsPolygon];

    [_mapPolygons addObject:polygon];

    [polygon setStrokeColor:[UIColor colorWithRed:0.0f/255.0f
                                             green:148.0f/255.0f
                                              blue:113.0f/255.0f
                                             alpha:1.0f]];

    [polygon setStrokeWidth:1.0f];
    [polygon setFillColor:[UIColor colorWithRed:0.0f/255.0f
                                          green:148.0f/255.0f
                                           blue:113.0f/255.0f
                                          alpha:0.3f]];
}

- (void) clearMarkers {
    YMKMapObjectCollection* mapObjects = _map.mapWindow.map.mapObjects;

    for (YMKPlacemarkMapObject* marker in _mapMarkers) {
        @try {
            [mapObjects removeWithMapObject:marker];
        } @catch (NSException *exception) {
            //TODO: Solve the error
        }
    }

    [_mapMarkers removeAllObjects];
}

- (void) clearPolygons {
    YMKMapObjectCollection* mapObjects = _map.mapWindow.map.mapObjects;

    for (YMKPlacemarkMapObject* polygon in _mapPolygons) {
        @try {
            [mapObjects removeWithMapObject:polygon];
        } @catch (NSException *exception) {
            //TODO: Solve the error
        }
    }

    [_mapPolygons removeAllObjects];
}

- (void) clearPolylines {
    YMKMapObjectCollection* mapObjects = _map.mapWindow.map.mapObjects;

    for (YMKPlacemarkMapObject* polyline in _mapPolylines) {
        @try {
            [mapObjects removeWithMapObject:polyline];
        } @catch (NSException *exception) {
            //TODO: Solve the error
        }
    }

    [_mapPolylines removeAllObjects];
}

- (void) setSearchLocation: (BOOL)json {
    _searchLocation = json;
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
        float zoom = self.map.mapWindow.map.cameraPosition.zoom + 2;

        YMKCameraPosition* cameraPos = [YMKCameraPosition cameraPositionWithTarget:point zoom:zoom azimuth:0 tilt:0];
        YMKAnimation* animation = [YMKAnimation animationWithType:YMKAnimationTypeSmooth duration:1];

        [self.map.mapWindow.map moveWithCameraPosition:cameraPos animationType:animation cameraCallback:nil];
    }];
}

- (void) zoomOut {
    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
        YMKPoint* point = self.map.mapWindow.map.cameraPosition.target;
        float zoom = self.map.mapWindow.map.cameraPosition.zoom - 2;

        YMKCameraPosition* cameraPos = [YMKCameraPosition cameraPositionWithTarget:point zoom:zoom azimuth:0 tilt:0];
        YMKAnimation* animation = [YMKAnimation animationWithType:YMKAnimationTypeSmooth duration:1];

        [self.map.mapWindow.map moveWithCameraPosition:cameraPos animationType:animation cameraCallback:nil];
    }];
}

- (void) animateToRegion:(NSDictionary *)region {
    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
        double latitude = [[region objectForKey:@"latitude"] doubleValue];
        double longitude = [[region objectForKey:@"longitude"] doubleValue];

        YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];

        float zoom = self.map.mapWindow.map.cameraPosition.zoom;
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


- (void)onObjectAddedWithView:(YMKUserLocationView *)view
{
    [view.pin setIconWithImage: _userLocationIcon];
    [view.arrow setIconWithImage: _userLocationIcon];
    view.accuracyCircle.fillColor = UIColor.clearColor;
}


- (BOOL)onMapObjectTapWithMapObject:(YMKMapObject *)mapObject point:(YMKPoint *)point {
    if ([mapObject isKindOfClass:[YMKPlacemarkMapObject class]]) {
        NSMutableDictionary* resultObject = [[NSMutableDictionary alloc]init];
        id userData = [mapObject userData];

        if (userData != nil) {
            @try {
                NSString *markerId = (NSString *)[userData objectForKey:@"id"];

                [resultObject setValue:markerId forKey:@"id"];
            } @catch (NSException *exception) {
                //TODO: Solve the error
            }
        }

        [resultObject setValue:[NSNumber numberWithDouble:[point latitude]] forKey:@"latitude"];
        [resultObject setValue:[NSNumber numberWithDouble:[point longitude]] forKey:@"longitude"];

        self.onMarkerPress(resultObject);

        return true;
    }
    return false;
}

- (void)onObjectRemovedWithView:(YMKUserLocationView *)view { }


- (void)onObjectUpdatedWithView:(YMKUserLocationView *)view event:(YMKObjectEvent *)event { }


- (void)onCameraPositionChangedWithMap:(YMKMap *)map cameraPosition:(nonnull YMKCameraPosition *)cameraPosition cameraUpdateSource:(YMKCameraUpdateSource)cameraUpdateSource finished:(BOOL)finished
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
