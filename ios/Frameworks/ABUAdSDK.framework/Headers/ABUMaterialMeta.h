//
//  ABUMaterialMeta.h
//  BUAdSDK
//
//  Copyright © 2017 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "ABUDislikeWords.h"
#import "ABUImage.h"

typedef NS_ENUM (NSInteger, ABUInteractionType) {
    ABUInteractionTypeCustorm        = 0,
    ABUInteractionTypeNO_INTERACTION = 1,  // pure ad display
    ABUInteractionTypeURL            = 2,  // open the webpage using a browser
    ABUInteractionTypePage           = 3,  // open the webpage within the app
    ABUInteractionTypeDownload       = 4,  // download the app
    ABUInteractionTypePhone          = 5,  // make a call
    ABUInteractionTypeMessage        = 6,  // send messages
    ABUInteractionTypeEmail          = 7,  // send email
    ABUInteractionTypeVideoAdDetail  = 8,   // video ad details page
    ABUInteractionTypeOthers         = 100 //其他广告sdk返回的类型
};

typedef NS_ENUM (NSInteger, ABUFeedADMode) {
    ABUFeedADModeSmallImage    = 2,
    ABUFeedADModeLargeImage    = 3,
    ABUFeedADModeGroupImage    = 4,
    ABUFeedVideoAdModeImage    = 5, // video ad || rewarded video ad horizontal screen
    ABUFeedVideoAdModePortrait = 15, // rewarded video ad vertical screen
    ABUFeedADModeImagePortrait = 16
};

@interface ABUMaterialMeta : NSObject <NSCoding>

/// interaction types supported by ads.
@property (nonatomic, assign) ABUInteractionType interactionType;

/// material pictures.If there is a picture width and height, please use the default width and height first
@property (nonatomic, strong) NSArray<ABUImage *> *imageAry;

/// icon of App. Return the ABUImage.image first, if the picture is nil, try to return the ABUImage.imageURL. AndIf there is a picture width and height, please use the default width and height first.empty if there is no value.
@property (nonatomic, strong) ABUImage *icon;

/// adLogo. Return the ABUImage.image first, if the picture is nil, try to return the ABUImage.imageURL. AndIf there is a picture width and height, please use the default width and height first.empty if there is no value.
@property (nonatomic, strong) ABUImage *adLogo;

/// ad adk Logo. Return the ABUImage.image first, if the picture is nil, try to return the ABUImage.imageURL. AndIf there is a picture width and height, please use the default width and height first.empty if there is no value.
@property (nonatomic, strong) ABUImage *sdkLogo;

/// ad headline.empty if there is no value.
@property (nonatomic, copy) NSString *AdTitle;

/// ad description.empty if there is no value.
@property (nonatomic, copy) NSString *AdDescription;

/// ad source.empty if there is no value.
@property (nonatomic, copy) NSString *source;

/// text displayed on the creative button.empty if there is no value.
@property (nonatomic, copy) NSString *buttonText;

/// display format of the in-feed ad, other ads ignores it.empty if there is no value.
@property (nonatomic, assign) ABUFeedADMode imageMode;

/// Star rating, range from 1 to 5. Return -1 if there is no value.
@property (nonatomic, assign) NSInteger score;

/// Number of comments.Return -1 if there is no value.
@property (nonatomic, assign) NSInteger commentNum;

/// ad installation package size, unit byte.Return -1 if there is no value.
@property (nonatomic, assign) NSInteger appSize;

/// video duration,empty if there is no value.
@property (nonatomic, assign) NSInteger videoDuration;

/// media configuration parameters.empty if there is no value.
@property (nonatomic, copy) NSDictionary *mediaExt;

/// String representation of the app's price,empty if there is no value.
@property (nonatomic, strong) NSString *appPrice;

/// Identifies the advertiser. For example, the advertiser’s name or visible URL.empty if there is no value.
@property (nonatomic, copy) NSString *advertiser;

/// Brand name, empty if there is no value.
/// 品牌名称，若广告返回中无品牌名称则为空
@property (copy, nonatomic) NSString *brandName;


- (instancetype)initWithDictionary:(NSDictionary *)dict error:(NSError *__autoreleasing *)error;

@end
