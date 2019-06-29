
#import "RNYandexMapKit.h"
#import <YandexMapKit/YMKMapKitFactory.h>

@implementation RNYandexMapKit

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setApiKey: (nonnull NSString *) apiKey) {
    [YMKMapKit setApiKey: @apiKey];
}

@end
  