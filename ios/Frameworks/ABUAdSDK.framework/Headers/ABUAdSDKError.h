//
//  ABUAdSDKError.h
//  BUAdSDK
//
//  Copyright © 2017年 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

extern NSErrorDomain ABUErrorDomain;

typedef NS_ENUM (NSInteger, ABUErrorCode) {
    ABUErrorCodeLoadAllfaied                      = 10086,  // ads all load failed
    ABUErrorCodeLoadTimeout                       = 10010,  // ads all load failed
    ABUErrorCodeLoadAdNoSetting                   = 40040,  // load ad no setting
    ABUErrorCodeAdUnitIDInvalid                   = 40006,  // adUnitID invalid
    ABUErrorCodeShowFreqInvilid                   = 40041, // 聚合维度展示频次超过上限
    ABUErrorCodeShowPeriodInvilid                 = 40042, // 聚合维度展示间隔超过上限
    ABUErrorCodeLoadFreqInvilid                   = 40043,  // app维度加载频次超过上限
    ABUErrorCodeLoadErrorCodePeriodInvilid        = 40044,  // media维度错误码控制间隔未达标
    ABUErrorCodeLoadSecondNoShow                  = 40047,  // 禁止同一对象二次请求,否则,若未展示过,上报错误码40047
    ABUErrorCodeLoadSecondShowed                  = 40048,  // 禁止同一对象二次请求,否则,若展示过,上报错误码40048
    ABUErrorCodeAdnNoAdNoReason                   = 40050,  // adn no ads filled without fail reason
    ABUErrorCodeAdnSDKVersionError                = 40051,  // adn SDK version error
    ABUErrorCodeShowFailedForNoneAds              = 40052,  // ad show fail for none ads
};
