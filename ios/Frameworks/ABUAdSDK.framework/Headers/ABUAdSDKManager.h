//
//  ABUAdSDKManager.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/23.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ABUAdSDKDefines.h"
#import "ABUUserInfoForSegment.h"
#import "ABUPrivacyConfig.h"

/// ABUAdSDKManager
@interface ABUAdSDKManager : NSObject

///SDKVersion
@property (nonatomic, copy, readonly, class) NSString *SDKVersion;

/**
 Register the App key that’s already been applied before requesting an ad from TikTok Audience Network.
 @param appID : the unique identifier of the App
 */
+ (void)setAppID:(NSString *)appID;

/// Set whether the app is a paid app, the default is a non-paid app.
/// Must obtain the consent of the user before incoming
+ (void)setIsPaidApp:(BOOL)isPaidApp;

/// extra device data for ABUAdSDK.Not required for every developer.Please make sure that it is called only when needed.It is recommended to call it once before SDK init.And supported recall it after SDK init but the value will be appended instead of overwritten. Now  supported fields are "device_id"(Conventional dedicated fields) and other custom keys.The incoming format is an dictionary of json strings,eg:"[{\"device_id\":\"62271333038\"}"
/// @param extraDeviceStr  eg:@"[{\"device_id\":\"62271333038\"}, {\"key\":\"value\"}]"
+ (void)setExtDeviceData:(NSString *)extraDeviceStr;

/// Configure development mode.
/// @param level default BUAdSDKLogLevelNone
/// @param language default ABUAdSDKLogLanguageCH
+ (void)setLoglevel:(ABUAdSDKLogLevel)level language:(ABUAdSDKLogLanguage)language;

/// get appID
+ (NSString *)appID;

/// get isPaidApp
+ (BOOL)isPaidApp;

/**
 *  user info for segment
 *  @param  userInfo
 *  can't be null
 *  please enter the values according to the format and rule
 *  ps: 1.create new segment to change key & value.
 *      2.value changed,reload config,be care for.
 *
 */
+ (void)setUserInfoForSegment:(nonnull ABUUserInfoForSegment *)userInfo;

/// Set theme mode for iOS. Just valid for BuadSDK ad, the default is normal.
+ (void)setThemeStatus:(ABUAdSDKThemeStatus)themeStatus;

/// current theme mode
+ (ABUAdSDKThemeStatus)themeStatus;

/// Configure whether the config request is successful
+ (BOOL)configDidLoad;

/// The method will add observer to listen if config is loaded, will callbcak ONLY once.
/// @param observer observer of config did loaded.
/// @param action  config did loaded action, param observer is the objserver object post from caller
+ (void)addConfigLoadSuccessObserver:(id _Nonnull)observer withAction:(void(^_Nonnull)(id _Nonnull observer))action;

@end
