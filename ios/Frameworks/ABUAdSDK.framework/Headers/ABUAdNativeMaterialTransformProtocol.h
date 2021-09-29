
//
//  ABUAdNativeTransform.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/3/10.
//  Copyright © 2020 bytedance. All rights reserved.
//

@class ABUMaterialMeta;

@protocol ABUAdNativeMaterialTransformProtocol <NSObject>
@optional

- (instancetype _Nullable)initWithConfig:(NSDictionary *_Nonnull)configs;

/**
 Initializes Adpter native ad with ad materialMeta.
 The third adSDK need to Initializes and return a ABUNativeAdView to AggregationAdSDK
 接收第三方广告sdk原生广告物料的载体，聚合sdk通过它将三方物料转换为内部
 @param materialMeta : ad materialMeta.
               including title,imageUrl,icon,etc.
 */
- (void)configThirdMaterialAd:(id _Nonnull)materialMeta;

/// interaction types supported by ads.
@property (nonatomic, assign, readonly) ABUInteractionType interactionType;

/// material pictures.
@property (nonatomic, strong, readonly) NSArray<ABUImage *> *_Nullable imageAry;

/// ad logo icon For app.
@property (nonatomic, strong, readonly) ABUImage *_Nullable icon;

/// ad logo icon.
@property (nonatomic, strong, readonly) ABUImage *_Nullable adLogo;

/// sdk logo icon For app.
@property (nonatomic, strong, readonly) ABUImage *_Nullable sdkLogo;

/// ad headline.
@property (nonatomic, copy, readonly) NSString *_Nullable AdTitle;

/// ad description.
@property (nonatomic, copy, readonly) NSString *_Nullable AdDescription;

/// ad source.
@property (nonatomic, copy, readonly) NSString *_Nullable source;

/// text displayed on the creative button.
@property (nonatomic, copy, readonly) NSString *_Nullable buttonText;

/// display format of the in-feed ad, other ads ignores it.
@property (nonatomic, assign, readonly) ABUFeedADMode imageMode;

/// Star rating, range from 1 to 5.
@property (nonatomic, assign, readonly) NSInteger score;

/// Number of comments.
@property (nonatomic, assign, readonly) NSInteger commentNum;

/// ad installation package size, unit byte.
@property (nonatomic, assign, readonly) NSInteger appSize;

/// video duration
@property (nonatomic, assign, readonly) NSInteger videoDuration;

/// media configuration parameters.
@property (nonatomic, copy, readonly) NSDictionary *_Nullable mediaExt;

/// String representation of the app's price
@property (nonatomic, strong, readonly) NSString *_Nullable appPrice;

/// Identifies the advertiser. For example, the advertiser’s name or visible URL.
@property (nonatomic, copy, readonly) NSString *_Nullable advertiser;

/// brandName
@property (nonatomic, copy, readonly) NSString *_Nullable brandName;

/// mediaView video
@property (nonatomic, strong, readonly) UIView *_Nullable mediaView;

/// Weak reference to your ad view's call to action asset view.
@property (nonatomic, strong, readonly) UIButton *_Nullable callToActionView;

/// Weak reference to your ad view's headline asset view.
@property (nonatomic, strong, readonly) UILabel *_Nullable titleView;

@property (nonatomic, strong, readonly) UILabel *_Nullable iconView;

@property (nonatomic, strong, readonly) UILabel *_Nullable descView;

@property (nonatomic, strong, readonly) UILabel *_Nullable priceView;

@property (nonatomic, strong, readonly) UILabel *_Nullable imageView;

@property (nonatomic, strong, readonly) UIView *_Nullable adLogoView;
@property (nonatomic, strong, readonly) UIView *_Nullable sdkLogoView;

@property (nonatomic, strong, readonly) UIView *_Nullable starRatingView;

@property (nonatomic, strong, readonly) UIView *_Nullable advertiserView;

@property (nonatomic, strong, readonly) UIButton *_Nullable dislikeBtn;

@end
