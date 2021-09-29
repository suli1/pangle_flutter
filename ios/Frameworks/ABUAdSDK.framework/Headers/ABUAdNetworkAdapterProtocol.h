//
//  ABUAdNetworkAdapterProtocol.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/24.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "ABUAdRequestConfig.h"
#import "ABUCustemSceneModel.h"
#import "ABUNativeAdContainer.h"
#import "ABUAdapterInfoModel.h"

@protocol ABUAdNetworkConnectorProtocol;
@protocol ABUAdBannerConnectorProtocol;
@protocol ABUAdNativeConnectorProtocol;

@protocol ABUAdNetworkAdapterProtocol <NSObject>
@optional

// 第三方平台名称
@property (nonatomic, strong) ABUAdapterInfoModel *_Nullable adapterInfo;

/// Adapter必须保持对所提供connector的弱引用
/// SDK会在请求广告开始加载数据的时候初始化adpater
/// The adapter must only maintain a weak reference to the provided connector.
/// Returns an initialized instance of the adapter when ad requests come in.
/// @param connector 遵循connector协议的d实例，一般为聚合内部广告类实例
/// @param adapterInfo 第三方适配器信息，eg：platformSlotID等
- (instancetype _Nonnull)initWithAdNetworkConnector:(id<ABUAdNetworkConnectorProtocol> _Nonnull)connector adapterInfo:(ABUAdapterInfoModel *_Nonnull)adapterInfo;

/// 针对Banner Adapter特定的初始化方法
/// Specific initialization method for Banner Adapter
- (instancetype _Nonnull)initWithAdBannerConnector:(id<ABUAdBannerConnectorProtocol> _Nonnull)connector adapterInfo:(ABUAdapterInfoModel *_Nonnull)adapterInfo;

/// 针对Native Adapter特定的初始化方法
/// Specific initialization method for Banner Adapter
- (instancetype _Nonnull)initWithAdNativeConnector:(id<ABUAdNativeConnectorProtocol> _Nonnull)connector adapterInfo:(ABUAdapterInfoModel *_Nonnull)adapterInfo;

/// 更新connector
- (void)updateConnector:(id<ABUAdNetworkConnectorProtocol> _Nonnull)connector;

/// Ad adapter版本
/// The version of the Ad adapter
- (NSString *_Nonnull)adapterVersion;

/// Ad networkSdk版本
/// The version of the Ad networkSdk
- (NSString *_Nonnull)networkSdkVersion;

/// 告诉adapter如何设置广告数据，SDK会在初始化成功时调用此方法
/// 成功时调用connector的adapterAdDidSetUp:
/// 失败调用connector的adapter:AdDidFailToSetUpWithError:
/// 失败会在下一次请求数据的时候重新setUp直到获取到数据
/// Tells the adapter to set up  ads. The adapter should notify the SDK whether set up has succeeded or failed using callbacks provided in the connector.
/// When set up fails, the SDK may try to set up the adapter again.
- (void)setUp;

+ (BOOL)canLoadAdInBackgroundThread;

/// 告诉adapter如何获取广告数据，SDK会在收到setUp成功的回调时调用此方法
/// 数据可用调用connector的adapterAdDidLoad:
/// 不可用调用connector的adapter:didAdFailToLoadwithError:
/// Tells the adapter to request a  ad.
/// This method is called after the adapter has been set up. The adapter should notify the SDK if the request succeeds or fails using callbacks provided in the connector.
- (void)requestAdWithConfig:(ABUAdRequestConfig *_Nonnull)adRequestCfg;

/// 告诉adapter当前广告是否有效
/// Tells the adapter whether the current Ad is valid.
/// -2未知(无可用方法) -1三方adn返回过期，0M判断过期，1未过期，
- (ABUAdValidStatus)adValidStatus;

/// 广告是否准备好，无对应接口  返回ABUAdValidStatusV2Unknown即可
- (ABUAdValidStatusV2)adReady;
/// 广告是否可用，无对应接口  返回ABUAdValidStatusV2Unknown即可
- (ABUAdValidStatusV2)adValid;
/// 广告是否过期，无对应接口  返回ABUAdValidStatusV2Unknown即可
- (ABUAdValidStatusV2)adExpried;

/// 告诉adapter如何展示一个广告，SDK会在展示广告时调用此方法
/// 展示成功返回YES，展示失败返回NO
/// /// Tells the adapter to show the  ad with the provided view controller.
/// Show success returns YES, show failure returns NO.
/// @param viewController 要显示的控制器
/// @param custemScene 自定义配置
- (BOOL)showAdFromRootViewController:(UIViewController *_Nonnull)viewController custemScene:(ABUCustemSceneModel *_Nullable)customScene;

/// 针对开屏广告的展示方法
/// @param window 展示容器
- (void)showSplashAdInWindow:(UIWindow *_Nullable)window;

/// 关闭时销毁ad
- (void)destorySplashAd;

/// 设置zoomout开屏view的vc
- (void)setupZoomoutViewController:(UIViewController *_Nonnull)viewController;

/// 为原生自渲染广告注册容器，用于原生自渲染NativeAd
/// @param container 容器结构，eg:父视图，click范围
- (void)registerNativeAdContainer:(ABUNativeAdContainer *_Nullable)container;

/// 模板广告进行渲染
- (void)renderForExpressAd:(ABUNativeAdContainer *_Nullable)container;

/// 原生信息流视频窗口frame调节 专用于百度
- (void)reSizeNativeVideoAd:(id _Nonnull)nativeAdView;

/// 信息流广告是否在播放，针对百度Ad
- (BOOL)isNativeAdVideoPlaying:(id _Nonnull)nativeAdView;

/// 改变信息流视频的播放状态，针对百度Ad
- (void)changeNativeAdVideoPlayState:(ABUPlayerPlayState)state adView:(id _Nonnull)nativeAdView;

@end
