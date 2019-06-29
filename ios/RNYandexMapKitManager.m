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

RCT_CUSTOM_VIEW_PROPERTY(searchLocation, BOOL, RNYandexMapKitView) {
    if (json == @(YES)) {
        [view setSearchLocation: true];
    } else {
        [view setSearchLocation: false];
    }
};

RCT_CUSTOM_VIEW_PROPERTY(markers, NSArray, RNYandexMapKitView)
{
    double latitude = [[json valueForKey:@"latitude"] doubleValue];
    double longitude = [[json valueForKey:@"longitude"] doubleValue];
    BOOL draggable =  [[json valueForKey:@"draggable"] boolValue];

    YMKPoint* point = [YMKPoint pointWithLatitude:latitude longitude:longitude];

    if (latitude != 0 && longitude != 0) {
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
