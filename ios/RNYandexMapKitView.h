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
#import <YandexMapKitDirections/YMKDrivingSession.h>
#import <YandexMapKitDirections/YMKDrivingDrivingOptions.h>
#import <YandexMapKitDirections/YMKDrivingDrivingRouter.h>
#import <YandexMapKitDirections/YMKDrivingRoute.h>

@interface RNYandexMapKitView : RCTView <YMKUserLocationObjectListener, YMKMapInputListener, CLLocationManagerDelegate, YMKMapCameraListener, YMKMapObjectTapListener>

@property (nonatomic) RCTBubblingEventBlock onLocationSearch;
@property (nonatomic) RCTBubblingEventBlock onLocationError;
@property (nonatomic) RCTBubblingEventBlock onMapPress;
@property (nonatomic) RCTBubblingEventBlock onMarkerPress;
@property (nonatomic) RCTBubblingEventBlock onPolygonPress;

@property (strong, nonatomic) YMKSearchManager* searchManager;
@property (nonatomic) CLLocationManager* locationManager;
@property (nonatomic) YMKSearchSession* searchSession;
@property (nonatomic) YMKDrivingRouter* drivingRouter;

@property (nonatomic) NSMutableArray* mapMarkers;
@property (nonatomic) NSMutableArray* mapPolygons;
@property (nonatomic) NSMutableArray* mapPolylines;
@property (nonatomic) YMKPlacemarkMapObject* userSearchPlacemark;

@property (nonatomic, copy) NSArray* markers;
@property (nonatomic, copy) NSArray* polygons;
@property (nonatomic, copy) NSDictionary* initialRegion;
@property (nonatomic, copy) NSDictionary* boundingBox;
@property (nonatomic) BOOL searchLocation;
@property (nonatomic, copy) NSArray* searchRoute;
@property (nonatomic, copy) NSDictionary* searchMarker;

@property (nonatomic) YMKMapView *map;
@property (nonatomic) float zoom;

@property (nonatomic) UIImage* userLocationIcon;

+ (NSString*) iconImage;
+ (NSString*) locationImage;
+ (NSString*) selectedPinImage;
+ (NSString*) userLocationImage;
+ (NSString*) disabledImage;

- (void) addMarkerWithJSON: (NSMutableDictionary *)json;
- (void) addPolygon: (NSMutableArray*)rectPoints identifier: (NSString*)identifier;
- (void) setSearchLocation: (BOOL)json;
- (void) setSearchMarker:(NSDictionary *)searchMarker;

- (void) navigateToUserLocation;
- (void) zoomIn;
- (void) zoomOut;
- (void) navigateToRegion: (NSDictionary*)region isAnimated: (BOOL)isAnimated;
- (void) navigateToBoundingBox: (NSDictionary*)northEastRegion southWestRegions: (NSDictionary*)southWestRegions;
- (YMKPoint*) getDeviceLocation;
- (void) clearMarkers;
- (void) clearPolygons;
- (void) clearPolylines;
- (void) setSearchRoute:(NSArray *)searchRoute;
- (void) submitRouteRequest:(NSMutableArray *)points;
- (UIImage*) getImageByID: (NSString *)imageId;
@end
