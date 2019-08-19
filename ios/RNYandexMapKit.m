
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
    [YMKMapKit setApiKey: apiKey];
}

RCT_EXPORT_METHOD(setLocale: (nonnull NSString *) language) {
    [YRTI18nManagerFactory setLocaleWithLanguage:language localeUpdateDelegate:^(NSError *error) {
        if (error != nil) {
            NSLog(@"setLocale update delegate error: %@", [error localizedDescription]);
        }
    }];
}

@end

