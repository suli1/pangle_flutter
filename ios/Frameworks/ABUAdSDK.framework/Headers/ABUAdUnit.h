//
//  ABUAdUnit.h
//  BUAdSDK
//
//  Copyright © 2017 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreGraphics/CoreGraphics.h>

#import "ABUSize.h"

// MSDK目前实际只有1，，2，3，5，7，8
typedef NS_ENUM (NSInteger, ABUAdSlotAdType) {
    ABUAdSlotAdTypeUnknown         = 0,
    ABUAdSlotAdTypeBanner          = 1,     // banner ads
    ABUAdSlotAdTypeInterstitial    = 2,     // interstitial ads
    ABUAdSlotAdTypeSplash          = 3,     // splash ads
    ABUAdSlotAdTypeSplash_Cache    = 4,     // cache splash ads
    ABUAdSlotAdTypeFeed            = 5,     // feed ads
    ABUAdSlotAdTypePaster          = 6,     // paster ads
    ABUAdSlotAdTypeRewardVideo     = 7,     // rewarded video ads
    ABUAdSlotAdTypeFullscreenVideo = 8,     // full-screen video ads
    ABUAdSlotAdTypeDrawVideo       = 9,     // vertical (immersive) video ads
};

// 代码位类型，值与服务端下发对应
typedef NS_ENUM(NSInteger, ABUAdPriceType) {
    ABUAdPriceTypeUnknown = -1,     // 未知类型
    ABUAdPriceTypeNormal = 0,       // 普通类型
    ABUAdPriceTypeClientBid = 1,    // 客户端竞价
    ABUAdPriceTypeServerBid = 2,    // 服务端竞价
    ABUAdPriceTypePriority = 100,   // 手动优先层
};

typedef NS_ENUM(NSInteger, ABUAdSlotPosition) {
    ABUAdSlotPositionTop = 1,
    ABUAdSlotPositionBottom = 2,
    ABUAdSlotPositionFeed = 3,
    ABUAdSlotPositionMiddle = 4, // for interstitial ad only
    ABUAdSlotPositionFullscreen = 5,
};

@interface ABUAdUnit : NSObject

/// required. The unique identifier of a native ad.
@property (nonatomic, copy) NSString *ID;


/// Get a express Ad if SDK can.Default is NO.Only for native Ad.And v2700+ the property will be prioritized based on the platform configuration.
@property (nonatomic, assign, readwrite) BOOL getExpressAdIfCan;

/// required. Ad type.
@property (nonatomic, assign) ABUAdSlotAdType AdType;

/// required. Ad display location.
@property (nonatomic, assign) ABUAdSlotPosition position;

/// Image size ratio
@property (nonatomic, strong) ABUSize *imgSize;

/// Image size ratio
@property (nonatomic, strong) ABUSize *iconSize;

/// Maximum length of the title.Supported for pangle only.
@property (nonatomic, assign) NSInteger titleLengthLimit;

/// Maximum length of description.Supported for pangle only.
@property (nonatomic, assign) NSInteger descLengthLimit;

/// Whether to support deeplink.Supported for pangle only.
@property (nonatomic, assign) BOOL isSupportDeepLink;

/**
required.
size expected ad view size， acture height will match size when width
 setep when getExpressAdIfCan is YES.But if getExpressAdIfCan is NO and adn is gdt ,height should not to be zero.
*/
@property (nonatomic, assign) CGSize adSize;

@end
