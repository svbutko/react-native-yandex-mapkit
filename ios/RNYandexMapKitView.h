#import <UIKit/UIKit.h>
#import <React/RCTView.h>
#import <YandexMapKit/YMKMapKitFactory.h>
#import <YandexMapKit/YMKMapInputListener.h>
#import <YandexMapKit/YMKMapView.h>
#import <YandexMapKit/YMKMap.h>
#import <YandexMapKit/YMKPlacemarkMapObject.h>
#import <YandexMapKit/YMKUserLocationObjectListener.h>
#import <YandexMapKit/YMKUserLocationView.h>
#import <YandexMapKit/YMKMapCameraListener.h>
#import <CoreLocation/CoreLocation.h>
#import <YandexMapKitSearch/YMKSearchManager.h>
#import <YandexMapKitSearch/YMKSearchSession.h>
#import "NSStringCategory.h"
#import "WSPoint.h"

@interface RNYandexMapKitView : RCTView <YMKUserLocationObjectListener, YMKMapInputListener, CLLocationManagerDelegate, YMKMapCameraListener>

@property (nonatomic, copy) RCTBubblingEventBlock onLocationSearch;
@property (nonatomic, copy) RCTBubblingEventBlock onLocationError;
@property (nonatomic, copy) RCTBubblingEventBlock onMapPress;
@property (nonatomic, copy) RCTBubblingEventBlock onMarkerPress;

@property (strong, nonatomic) YMKSearchManager* searchManager;
@property (nonatomic) YMKSearchSession* searchSession;
@property (nonatomic) CLLocationManager* locationManager;
@property (nonatomic, copy) NSArray* markers;
@property (nonatomic, copy) NSMutableArray* mapMarkers;
@property (nonatomic, copy) NSArray* polygons;
@property (nonatomic, copy) NSMutableArray* mapPolygons;
@property (nonatomic) BOOL searchLocation;

@property (nonatomic) YMKMapView *map;
@property (nonatomic) float zoom;

@property (nonatomic) UIImage* userLocationIcon;

+ (NSString*) addressKey;
+ (NSString*) iconKey;
+ (NSString*) iconImage;
+ (NSString*) locationImage;

- (void) addMarkerWithJSON: (id)json;
- (void) setSearchLocation: (BOOL)json;

- (void) navigateToUserLocation;
- (void) zoomIn;
- (void) zoomOut;
- (YMKPoint*) getDeviceLocation;
- (void) clearMarkers;
- (void) clearPolygons;

@end
