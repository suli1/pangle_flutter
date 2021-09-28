//
//  ABUPrivacyConfig.h
//  ABUAdSDK
//
//  Created by CHAORS on 2021/8/24.
//

#import <Foundation/Foundation.h>

/// optional key. Whether to restrict personalized ads:0-not limit, 1-limit, the default is 0.Now only valid for pangle, Ks and Baidu adn.
const static NSString *kABUPrivacyLimitPersonalAds = @"ABUPrivacyLimitPersonalAds";
/// optional key. Whether to forbid CAID:0-not forbidden, 1-forbid, the default is 0.Now only valid for pangle Baidu adn.
const static NSString *kABUPrivacyForbiddenCAID = @"ABUPrivacyForbiddenCAID";

@interface ABUPrivacyConfig : NSObject

// 根据key配置相关隐私项
+ (void)setPrivacyWithKey:(const NSString *)key andValue:(id)value;
// 获取当前配置项
+ (NSDictionary *)privacyConfig;

@end

