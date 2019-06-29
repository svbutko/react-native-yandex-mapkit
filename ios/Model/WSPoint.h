#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface WSPoint : NSObject

@property (nonatomic) NSString* identifier;
@property (nonatomic) double latitude;
@property (nonatomic) double longitude;
@property (nonatomic) UIImage* icon;
@property (nonatomic, nullable) UIImage* selectedIcon;

-(instancetype)initWithJSON:(id) json;

@end