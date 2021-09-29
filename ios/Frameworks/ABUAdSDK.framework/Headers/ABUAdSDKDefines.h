//
//  ABUAdSDKDefines.h
//  BUAdSDK
//
//  Copyright © 2017年 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ABUAdSDKError.h"

FOUNDATION_EXPORT void abu_safe_dispatch_sync_main_queue(void (^block)(void));
FOUNDATION_EXPORT void abu_safe_dispatch_async_main_queue(void (^block)(void));

#define ABUCheckValidString(__string)          (__string && [__string isKindOfClass:[NSString class]] && [__string length])


// 展示额外参数的key：ABUShowExtroInfoKey
static NSString * ABUShowExtroInfoKeySceneType          = @"ABUShowExtroInfoKeySceneType"; // type of scene, only used for pangle SDK now.And the value of key see ABURitSceneType ABURitSceneType
static NSString * ABUShowExtroInfoKeySceneDescription   = @"ABUShowExtroInfoKeySceneDescription"; // scene description, the description defined by the developer, which needs to be assigned when ABUShowExtroInfoKeySceneType = 0
// 扩展


typedef NS_ENUM (NSInteger, ABUOfflineType) {
    ABUOfflineTypeNone,  // Do not set offline
    ABUOfflineTypeProtocol, // Offline dependence NSURLProtcol
    ABUOfflineTypeWebview, // Offline dependence WKWebview
};

typedef NS_ENUM (NSInteger, ABUAdSDKLogLevel) {
    ABUAdSDKLogLevelNone,
    ABUAdSDKLogLevelError,
    ABUAdSDKLogLevelDebug
};

typedef NS_ENUM (NSInteger, ABUAdSDKLogLanguage) {
    ABUAdSDKLogLanguageCH,
    ABUAdSDKLogLanguageEN
};

// Theme mode for iOS.
typedef NS_ENUM(NSInteger, ABUAdSDKThemeStatus) {
    ABUAdSDKThemeStatus_None = -1,
    ABUAdSDKThemeStatus_Normal = 0,
    ABUAdSDKThemeStatus_Night  = 1,
};

typedef NS_ENUM (NSInteger, ABURitSceneType) {
    ABURitSceneType_custom                  = 0,//custom
    ABURitSceneType_home_open_bonus         = 1,// “home_open_bonus”, Login/open rewards (login, sign-in, offline rewards doubling, etc.)
    ABURitSceneType_home_svip_bonus         = 2,// "home_svip_bonus", Special privileges (VIP privileges, daily rewards, etc.)
    ABURitSceneType_home_get_props          = 3,// "home_get_props", Watch rewarded video ad to gain skin, props, levels, skills, etc
    ABURitSceneType_home_try_props          = 4,// "home_try_props", Watch rewarded video ad to try out skins, props, levels, skills, etc
    ABURitSceneType_home_get_bonus          = 5,// "home_get_bonus", Watch rewarded video ad to get gold COINS, diamonds, etc
    ABURitSceneType_home_gift_bonus         = 6,// "home_gift_bonus", Sweepstakes, turntables, gift boxes, etc
    ABURitSceneType_game_start_bonus        = 7,// "game_start_bonus", Before the opening to obtain physical strength, opening to strengthen, opening buff, task props
    ABURitSceneType_game_reduce_waiting     = 8,// "geme_reduce_waiting", Reduce wait and cooldown on skill CD, building CD, quest CD, etc
    ABURitSceneType_game_more_opportunities = 9,// "game_more_opportunities", More chances (resurrect death, extra game time, decrypt tips, etc.)
    ABURitSceneType_game_finish_rewards     = 10,// "game_finish_rewards", Settlement multiple times/extra bonus (completion of chapter, victory over boss, first place, etc.)
    ABURitSceneType_game_gift_bonus         = 11// "game_gift_bonus", The game dropped treasure box, treasures and so on
};

// 三方Adn枚举
typedef NS_ENUM (NSInteger, ABUAdnType) {
    ABUAdnNoPermission = -3,    // 无权限访问
    ABUAdnNoData = -2,  // 暂时无真实数据，未获取到最佳广告，一般在未展示之前提前调用
    ABUAdnNone = 0,     // 未知adn
    ABUAdnPangle = 1,   // 穿山甲adn
    ABUAdnAdmob = 2,    // 谷歌Admob
    ABUAdnGDT = 3,      // 腾讯广点通adn
    ABUAdnMTG = 4,      // Mintegral adn
    ABUAdnUnity = 5,    // unity adn
    ABUAdnBaidu = 6,    // 百度adn
    ABUAdnKs = 7,       // 快手Adn
    ABUAdnSigmob = 8,   // Sigmob adn
    ABUAdnKlevin = 9,   // Klevin游可赢
};

// 广告类类型
typedef NS_ENUM (NSInteger, ABUAdClassType) {
    ABUAdClassTypeNative = 1,
    ABUAdClassTypeRewardedVideo,
    ABUAdClassTypeFullscreenVideo,
    ABUAdClassTypeInterstitial,
    ABUAdClassTypeSplash,
    ABUAdClassTypeBanner,
};

// 开屏点击区域类型
typedef NS_ENUM(NSInteger, ABUSplashButtonType) {
  ABUSplashButtonTypeFullScreen = 1, // The whole area of splash view will respond to click event
  ABUSplashButtonTypeDownloadBar = 2  // The area of download bar in splash view will respond to click event
};


@protocol ABUToDictionary <NSObject>
- (NSDictionary *)dictionaryValue;
@end

#if defined(__has_attribute)
#if __has_attribute(deprecated)
#define ABU_DEPRECATED_MSG_ATTRIBUTE(s) __attribute__((deprecated(s)))
#define ABU_DEPRECATED_ATTRIBUTE __attribute__((deprecated))
#else
#define ABU_DEPRECATED_MSG_ATTRIBUTE(s)
#define ABU_DEPRECATED_ATTRIBUTE
#endif
#else
#define ABU_DEPRECATED_MSG_ATTRIBUTE(s)
#define ABU_DEPRECATED_ATTRIBUTE
#endif


// 对枚举值进行日志字符串转换， 例如对于一个枚举值   1表示激励视频广告的意思， 将返回：   激励视频广告(value:1)
FOUNDATION_EXPORT NSString *NSStringLogFromABUAdEnumItem(NSInteger enumItem, NSDictionary *dic, NSString *defaultValue);
// 对枚举值进行字符串转换   例如对于一个枚举值   1表示rewarded_ad的字符串， 将返回：  rewarded_ad
FOUNDATION_EXPORT NSString *NSStringFromABUAdEnumItem(NSInteger enumItem, NSDictionary *dic, NSString *defaultValue);
