//
//  ABUBaseAggregationAd.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/25.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ABUAdNetworkAdapterProtocol.h"
#import "ABUAdNetworkConnectorProtocol.h"
#import "ABUAdUnit.h"


/// 聚合广告基类
@interface ABUBaseAggregationAd : NSObject

/// 广告类型
@property (nonatomic, assign) ABUAdSlotAdType adType;

/// 广告类型,与adslot不同，是区分由那个类创建的
@property (nonatomic, assign) ABUAdClassType adClsType;

/// 一次waterfall中各adn代码位加载广告失败原因，建议调用时机：展示广告时/超时时/全部返回报错时；返回nil表示一次加载无代码位加载失败或其加载无响应
- (NSArray<NSDictionary *> *)waterfallFillFailMessages;

@end
