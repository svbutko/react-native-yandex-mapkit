#import "NSStringCategory.h"

@implementation NSString (NSStringCategory)

-(UIImage *)decodeBase64ToImage {
	NSData *data = [[NSData alloc]initWithBase64EncodedString:self options:NSDataBase64DecodingIgnoreUnknownCharacters];
	return [UIImage imageWithData:data];
}

@end