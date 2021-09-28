//
//  ABUAdApterConfigProtocol.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/24.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ABUSdkInitConfig.h"
#import "ABUAdapterInfoModel.h"
#import "ABUAdSdkConfig.h"

/// Ad adapter必须遵循这个协议才能初始化第三方SDK
/// Ad adapter must conform to this protocol to initialize other SDK.
@protocol ABUAdApterConfigProtocol <NSObject>
@required

/// 告诉adapter如何初始化第三方SDK，聚合SDK会尽可能早的调用此方法初始化第三方SDK
/// Tells the adapter to initialize the third-party SDK. We will call this method as early as possible to initialize the third-party SDK.
+ (void)initializeAdapterWithConfiguration:(ABUSdkInitConfig *_Nullable)initConfig;

/// config adn SDK
+ (void)configAdSDK:(ABUAdSdkConfig *_Nullable)initConfig;

/// config adn Privacy.The key for privacyConfig see ABUPrivacyConfig.h
+ (void)configAdPrivacyConfig:(NSDictionary *_Nonnull)privacyConfig;

/// adn can be use
+ (BOOL)isAdnCanUse;

/// Ad adapter版本
/// The version of the Ad adapter
+ (NSString *_Nonnull)adapterVersion;

/// Ad networkSdk版本
/// The version of the Ad networkSdk
+ (NSString *_Nonnull)networkSdkVersion;

@optional

/// 三方服务端竞价的Token，不支持可不实现或返回为nil
/// The Three-party server bidding token
/// error:返回的错误信息
+ (NSString *_Nullable)biddingToken:(ABUAdapterInfoModel *_Nonnull)model error:(NSError *_Nullable*_Nullable)error;


@end
