//
//  ABUExtraInfo2Aggregation.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/6/3.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ABUSplashZoomOutView.h"


NS_ASSUME_NONNULL_BEGIN

// 从三方adn携带回聚合的额外信息
@interface ABUExtraInfo2Aggregation : NSObject <NSCopying>

@property (nonatomic, assign) BOOL isExpressAdGot;    // 结果是否返回模板广告
@property (nonatomic, strong) NSMutableDictionary<NSString *, NSString *> *slotCpms;   // 三方返回的cpm  <adapterSaveKey-cpm> 对nativeAd是<ad对象地址-cpm>(因为一次nativeAd可能是多加载)
@property (nonatomic, copy) NSString *creativeID;   // 广告创意ID(穿山甲专用)
@property (nonatomic, copy) NSString *adID;   // 广告ID(穿山甲专用)
@property (nonatomic, copy) NSString *requestID;   // 广告请求ID(穿山甲专用)

@property (nonatomic, assign) BOOL videoCached;      // 视频是否缓存完毕，可能是M定义的cache
@property (nonatomic, assign) BOOL hasDownloadCallback;   // 广告是否有真实的视频下载回调，仅用于激励/全屏视频广告
@property (nonatomic, assign) BOOL materialMetaIsFromPreload;   // 当次加载是否来自预请求，当前pangle可获取到状态且状态在download之后唯一准确

@property (nonatomic, strong) UIView *splashZoomoutView;   // 开屏点睛View
@property (nonatomic, assign) CGSize splashZoomoutSize;   // 开屏点睛View建议大小
@property (nonatomic, assign) BOOL splashZoomoutHasAnimation;  // 开屏点睛View是否自带动画
@property (nonatomic, assign) ABUAddOccasionType addOccasionType;

/// Whether to enter the "look again" process which triggered by clicking the "look again" button on the landing page or close button . This attribute is mainly used to determine whether the delegate<ABURewardedVideoAdDelegate> is from first time or "look again".Now only valid for pangle SDK.
@property (nonatomic, assign) BOOL hasInRewardWatchAgain;

@end

NS_ASSUME_NONNULL_END
