#import "RNYandexMapKitManager.h"
#import "RNYandexMapKitView.h"
#import <YandexMapKit/YMKMap.h>

@implementation RNYandexMapKitManager {
    RNYandexMapKitView *mapKitView;
};

RCT_EXPORT_MODULE();

RCT_EXPORT_VIEW_PROPERTY(onLocationSearch, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onMapPress, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onLocationError, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onMarkerPress, RCTBubblingEventBlock)

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

        double latitude = [[coordinates valueForKey:@"latitude"] doubleValue];
        double longitude = [[coordinates valueForKey:@"longitude"] doubleValue];

        YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];

        NSDictionary* addMarkerJSON =
        @{
          RNYandexMapKitView.addressKey:
              @{
                  @"id": @"initialAddress",
                  @"latitude": [NSString stringWithFormat:@"%f", point.latitude],
                  @"longitude": [NSString stringWithFormat:@"%f", point.longitude],
                  },
          RNYandexMapKitView.iconKey: RNYandexMapKitView.iconImage
          };

        [view addMarkerWithJSON: addMarkerJSON];
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

        [view addPolygon:rectPoints];
    }
}


RCT_EXPORT_METHOD(animateToRegion)
{
    [mapKitView navigateToUserLocation];
}

RCT_EXPORT_METHOD(navigateToUserLocation)
{
    [mapKitView navigateToUserLocation];
}

RCT_EXPORT_METHOD(zoomIn)
{
    [mapKitView zoomIn];
}

RCT_EXPORT_METHOD(zoomOut)
{
    [mapKitView zoomOut];
}

- (instancetype) init
{
    self = [super init];
    mapKitView = RNYandexMapKitView.new;
    return self;
}


- (UIView *)view
{
    return mapKitView;
}

@end
