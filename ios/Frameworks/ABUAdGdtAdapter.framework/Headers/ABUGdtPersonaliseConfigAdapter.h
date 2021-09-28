//
//  ABUGdtPersonaliseCfgAdapter.h
//  ABUDemo
//
//  Created by wangchao on 2020/4/16.
//  Copyright © 2020 wangchao. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN



@interface ABUGdtPersonaliseConfigAdapter : NSObject

/// 三方sdk个性化设置，eg：部分静音设置，合规，定位权限，debug模式等。主要为一些聚合SDK并未统一封装的配置接口(如果聚合SDK已提供，请勿在此设置！！！)；不要在configAdapterWithBlock写初始化代码！！！聚合SDK已完成初始化
+ (void)configAdapterWithBlock:(void(^)(void))block;

@end

NS_ASSUME_NONNULL_END
