//
//  ABUAdapterInfoModel.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/3/2.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "ABUAdUnit.h"
#import "ABUAggregationBaseProtocol.h"
#import "ABURewardedVideoModel.h"
#import "ABUExtraInfo2Third.h"
#import "ABUExtraInfo2Aggregation.h"
#import "ABUAdSDKDefines.h"


// unitiID广告位级别请求类型
typedef NS_ENUM (NSInteger, ABUUnitIDReqType) {
    ABUUnitIDReqTypeDefault,    // 正常请求
    ABUUnitIDReqTypePreloadV1,  // 早期版本预缓存，带策略preloadsort控制
    ABUUnitIDReqTypePreloadV2,   // 预缓存2.0，不带策略preloadsort控制
    ABUUnitIDReqTypeUseCache,    // 使用预缓存链路
    ABUUnitIDReqTypeReLoad,     // 预缓存未命中重新请求
};

// 广告模型有效性判断
typedef NS_ENUM(NSInteger, ABUAdValidStatus) {
    ABUAdValidStatusUnknow = -1,
    ABUAdValidStatusNotForAdn,     // 三方Adn返回不可用,该值不可变！！！
    ABUAdValidStatusNomal,             // 正常可用
    ABUAdValidStatusNotForReady,       // 三方IsReady不可用
    ABUAdValidStatusNotForExpired,     // 聚合有效时间过期
    ABUAdValidStatusNotForFailed,      // 三方Adn填充失败
    ABUAdValidStatusNotForNoResp,      // 三方Adn未返回
    ABUAdValidStatusConditionChanged   // 复用条件改变
};

// 广告模型有效性判断V2
typedef NS_ENUM(NSInteger, ABUAdValidStatusV2) {
    ABUAdValidStatusV2Unknown = 0,     // 三方Adn无对应接口
    ABUAdValidStatusV2YES,             // 可用，eg: 三方adValid接口返回yes，表示可用M为ABUAdValidStatusV2YES
    ABUAdValidStatusV2NO               // 不可用，eg:过期接口返回yes，M转换为ABUAdValidStatusV2NO
};

// 下发广告类型
typedef NS_ENUM(NSUInteger, ABUAdnOriginType) {
    ABUAdnOriginTypeDefault = 0, // 默认值，服务端不下发就是0，根据用户自定义的走
    ABUAdnOriginTypeExpress = 1, // 模板渲染
    ABUAdnOriginTypeNative = 2,  // 自渲染
    ABUAdnOriginTypeExpress2_0 = 3 // 广点通express2.0专用
};

// 测试模式，来源于服务端"if_test"字段
typedef NS_ENUM(NSUInteger, ABUAdTestMode) {
    ABUAdTestModeNone = 0,  // 默认值，服务端不下发就是0，正式数据
    ABUAdTestModeV1= 1,     // 测试工具1.0：平台测试工具
    ABUAdTestModeV2 = 2     // 测试工具2.0：可视化测试工具
};


/// 广告适配器信息
@interface ABUAdapterInfoModel : NSObject <ABUAggregationBaseProtocol, NSCopying>

@property (nonatomic, assign) ABUAdSlotAdType adType;   // 广告类型
@property (nonatomic, copy) NSString *platformName; // 广告平台名称(服务器下发)
@property (nonatomic, copy) NSString *adapterName;  // 适配器名称
@property (nonatomic, copy) NSString *configAdapterName;  // 配置器名称
@property (nonatomic, copy) NSString *adUnitID;   // 聚合广告位
@property (nonatomic, copy) NSString *platformSlotID;   // adn代码位
@property (nonatomic, copy) NSString *ritPrice;   // 广告位底价或历史cpm
@property (nonatomic, copy) NSString *priceRate;   // 价格汇率，实际价格ritPrice * priceRate
@property (nonatomic, copy) NSString *slotPrice;   // 竞价广告位cpm
@property (nonatomic, copy) NSString *nativeAdTransformName;    // 原生自渲染广告物料转换器名称
@property (nonatomic, assign) ABUAdPriceType priceType;   // 广告位类型：0为普通广告位，1为竞价广告位, 2服务端竞价， 100P层
@property (nonatomic, assign) NSInteger loadSort;   // 加载顺序：优先加载sort=1的所有Ads，其次sort++
@property (nonatomic, assign) NSInteger showSort;   // 加载顺序：优先展示sort=1的Ad，其次sort++
/// 从整体config继承的信息
@property (nonatomic, copy) NSString *appID;    // 所属SDKappID
@property (nonatomic, copy) NSString *appKey;    // 所属SDKappKey

/// 从waterfall配置(ABUAdapterAdSlotListModel)继承的信息
@property (nonatomic, copy) NSString *waterfallID;      // 某个waterfall的唯一id、
@property (nonatomic, assign) ABUAdTestMode inTestMode;      // 当前waterfall配置是否属于测试模式，用于开发者自测:1平台测试工具，2可视化测试工具；0或无该字段默认正式数据
@property (nonatomic, copy) NSString *waterfallVersion;      // 某个waterfall的配置版本(标识waterfall配置修改) add in 2200 by wangchao
@property (nonatomic, copy) NSString *segmentId;      // 某个waterfall的唯一id
@property (nonatomic, copy) NSString *segmentVersion;      // 某个waterfall的唯一id
@property (nonatomic, strong) ABUExtraInfo2Third *extraInfo2Third;  // 从聚合携带到第三方adn的额外信息
@property (nonatomic, strong) ABUExtraInfo2Aggregation *extraInfo2Aggregation;  // 三方---->聚合的携带信息

@property (nonatomic, assign)  ABUUnitIDReqType preLoadType;    // 预请求开关配置只有0，1，2值
@property (nonatomic, assign)  ABUUnitIDReqType reqType;    // 当次请求行为,类型与实际行为一直
@property (nonatomic, assign) NSInteger adExpiredTime;  // 缓存广告过期时间，adExpiredTime内有效 仅在三方adn无有效接口下生效
@property (nonatomic, assign) NSTimeInterval fillTime;  // 广告填充时间，
@property (nonatomic, assign) ABUAdValidStatus adValidStatus;   // 广告是否过期

@property (nonatomic, assign) ABUAdnType adnType; 

@property (nonatomic, assign) NSInteger isVideoCacheSuccess; //当前广告是否缓存成功
@property (nonatomic, assign) BOOL preLoadFromShow; // 预缓存是否来自show

// 2700 模板&自渲染混出
@property (nonatomic, assign) ABUAdnOriginType originType;
// show时机单个adn预请求 if_pre_request
@property (nonatomic, assign) BOOL ifPreRequestWhenShow;
// 是否使用if_is_ready控制
@property (nonatomic, assign) NSInteger ifUseIsReady;

@end
