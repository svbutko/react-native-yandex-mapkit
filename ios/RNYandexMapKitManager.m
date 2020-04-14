#import "RNYandexMapKitManager.h"
#import "RNYandexMapKitView.h"
#import <YandexMapKit/YMKMap.h>
#import <React/RCTConvert.h>;

@implementation RNYandexMapKitManager {
    RNYandexMapKitView *mapKitView;
};

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

RCT_EXPORT_MODULE();

RCT_EXPORT_VIEW_PROPERTY(onLocationSearch, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onMapPress, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onLocationError, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onMarkerPress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onPolygonPress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onSuggestionsFetch, RCTBubblingEventBlock)

RCT_CUSTOM_VIEW_PROPERTY(searchLocation, BOOL, RNYandexMapKitView) {
    if ([json  isEqual: @(YES)]) {
        [view setSearchLocation: true];
    } else {
        [view setSearchLocation: false];
    }
};

RCT_CUSTOM_VIEW_PROPERTY(markers, NSArray, RNYandexMapKitView)
{
    [view clearMarkers];

    for (NSDictionary *jsMarker in json) {
        NSDictionary *coordinates = [jsMarker objectForKey:@"coordinate"];
        NSString *icon = [jsMarker objectForKey:@"icon"];

        double latitude = [[coordinates valueForKey:@"latitude"] doubleValue];
        double longitude = [[coordinates valueForKey:@"longitude"] doubleValue];

        NSNumber *latitudeNumber = [[NSNumber alloc] initWithDouble:latitude];
        NSNumber *longitudeNumber = [[NSNumber alloc] initWithDouble:longitude];

        NSMutableDictionary* marker = [[NSMutableDictionary alloc]init];
        [marker setValue:latitudeNumber forKey:@"latitude"];
        [marker setValue:longitudeNumber forKey:@"longitude"];

        NSDictionary* userData = [jsMarker objectForKey:@"userData"];
        if (userData != nil) {
            [marker setValue:userData forKey:@"userData"];
        }

        if (icon != nil) {
            [marker setValue:icon forKey:@"icon"];
        } else {
            [marker setValue:@"pin" forKey:@"icon"];
        }

        [view addMarkerWithJSON: marker];
    }
}

RCT_CUSTOM_VIEW_PROPERTY(searchMarker, NSDictionary, RNYandexMapKitView)
{
    if (json != nil) {
        [view setSearchMarker:json];
    }
}

RCT_CUSTOM_VIEW_PROPERTY(searchRoute, NSArray, RNYandexMapKitView)
{
    if ([json count] == 2) {
        [view setSearchRoute:json];
    }
}


RCT_CUSTOM_VIEW_PROPERTY(polygons, NSArray, RNYandexMapKitView)
{
    [view clearPolygons];

    for (NSDictionary *jsPolygon in json) {
        NSMutableArray *points = [jsPolygon mutableArrayValueForKey:@"points"];

        NSMutableArray *rectPoints = [[NSMutableArray alloc]init];

        for (NSDictionary *point in points) {
            double latitude = [[point valueForKey:@"latitude"] doubleValue];
            double longitude = [[point valueForKey:@"longitude"] doubleValue];

            YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];
            [rectPoints addObject:point];
        }

        UIColor *backgroundColor = [UIColor colorWithRed:0.0f/255.0f
                                                  green:148.0f/255.0f
                                                   blue:113.0f/255.0f
                                                  alpha:0.3f];
        NSString *backgroundColorString = [jsPolygon objectForKey:@"backgroundColor"];
        if (backgroundColorString != nil) {
            backgroundColor = [RCTConvert UIColor:backgroundColorString];
        }

        UIColor *borderColor = [UIColor colorWithRed:0.0f/255.0f
                                              green:148.0f/255.0f
                                               blue:113.0f/255.0f
                                              alpha:1.0f];
        NSString *borderColorString = [jsPolygon objectForKey:@"borderColor"];
        if (borderColorString != nil) {
            borderColor = [RCTConvert UIColor:borderColorString];
        }

        [view addPolygon:rectPoints backgroundColor: backgroundColor borderColor: borderColor dict: jsPolygon];
    }
}

RCT_CUSTOM_VIEW_PROPERTY(initialRegion, NSDictionary, RNYandexMapKitView)
{
    double latitude = [[json valueForKey:@"latitude"] doubleValue];
    double longitude = [[json valueForKey:@"longitude"] doubleValue];

    YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];

    YMKCameraPosition* cameraPos = [YMKCameraPosition cameraPositionWithTarget:point zoom:10 azimuth:0 tilt:0];

    [view.map.mapWindow.map moveWithCameraPosition:cameraPos];
}

RCT_CUSTOM_VIEW_PROPERTY(boundingBox, NSDictionary, RNYandexMapKitView)
{
    [view navigateToBoundingBox:[json valueForKey:@"northEastPoint"] southWestRegions:[json valueForKey:@"southWestPoint"]];
}


RCT_EXPORT_METHOD(navigateToRegion: (nonnull NSNumber *)reactTag region: (nonnull NSDictionary *)params isAnimated: (BOOL)isAnimated)
{
    [mapKitView navigateToRegion:params isAnimated:isAnimated];
}

RCT_EXPORT_METHOD(navigateToBoundingBox: (nonnull NSNumber *)reactTag northEastRegion: (nonnull NSDictionary *)northEastRegion southWestRegions: (nonnull NSDictionary *)southWestRegions)
{
    [mapKitView navigateToBoundingBox:northEastRegion southWestRegions:southWestRegions];
}

RCT_EXPORT_METHOD(navigateToUserLocation: (nonnull NSNumber *)reactTag)
{
    [mapKitView navigateToUserLocation];
}

RCT_EXPORT_METHOD(zoomIn: (nonnull NSNumber *)reactTag)
{
    [mapKitView zoomIn];
}

RCT_EXPORT_METHOD(zoomOut: (nonnull NSNumber *)reactTag)
{
    [mapKitView zoomOut];
}

RCT_EXPORT_METHOD(fetchSuggestions: (nonnull NSNumber *)reactTag query: (nonnull NSString *)query)
{
    [mapKitView fetchSuggestions:query];
}

- (instancetype) init
{
    return [super init];
}


- (UIView *)view
{
    mapKitView = [[RNYandexMapKitView alloc]init];
    return mapKitView;
}

@end
