//
//  ABUAdapterPersonaliseConfigProtocol.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/4/16.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ABUAdapterPersonaliseConfigProtocol <NSObject>

@required
/// 为第三方SDK设置一些个性化设置，主要是SDK客户端可设置的一些全局配置
/// Set some personalized settings for the third-party SDK, mainly some global configurations that can be set by the SDK client
+ (void)setupPersonaliseConfiguration;

@optional
/// unityAdSDK测试模式；无配置默认为NO
+ (BOOL)testUnityAd;

@end
