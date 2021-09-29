//
//  ABUNativeAdContainer.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/27.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "ABUNativeAdView.h"

@interface ABUNativeAdContainer : NSObject

@property (nonatomic, strong) ABUNativeAdView *_Nullable adapterView;
@property (nonatomic, strong) NSObject *_Nullable hostObject;
@property (nonatomic, strong) UIView *_Nullable containerView;
@property (nonatomic, strong) NSArray<__kindof UIView *> *_Nullable clickableViews;

@end
