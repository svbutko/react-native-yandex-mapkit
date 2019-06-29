#import "WSPoint.h"
#import "NSStringCategory.h"

@implementation WSPoint

-(instancetype)initWithJSON:(id) json {
	self = [WSPoint new];

	//JSON required
	if ([json isKindOfClass:[NSDictionary class]] == false) {
		NSLog(@"Error with WSPoint init. Required json: %@", json);
		return nil;
	}
	NSDictionary* dictionary = (NSDictionary*) json;

	self.identifier = dictionary[@"id"];
	self.latitude = [self convertToDouble:dictionary[@"latitude"]];
	self.longitude = [self convertToDouble:dictionary[@"longitude"]];

	return self;
}

-(double)convertToDouble:(id) value {
	if ([value isKindOfClass:[NSNumber class]]) {
		return ((NSNumber*)value).doubleValue;
	}
	else if ([value isKindOfClass:[NSString class]]) {
		return ((NSString*)value).doubleValue;
	}
	return 0;
}

@end