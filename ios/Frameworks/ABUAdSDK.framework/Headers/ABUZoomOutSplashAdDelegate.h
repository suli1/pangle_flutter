//
//  ABUZoomOutSplashAdDelegate.h
//  ABUAdSDK
//
//  Created by wangchao on 2021/4/21.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUMaterialMeta.h"
#import <UIKit/UIKit.h>

@protocol ABUZoomOutSplashAdDelegate <NSObject>

/**
 This method is called when splash ad is clicked.
 */
- (void)splashZoomOutViewAdDidClick:(UIView *_Nonnull)splashZoomOutView;

/**
 This method is called when splash ad is closed.
 */
- (void)splashZoomOutViewAdDidClose:(UIView *_Nonnull)splashZoomOutView;

/**
 This method is called when another controller will open.
 */
- (void)splashZoomOutViewAdWillPresentFullScreenModal:(UIView *_Nonnull)splashZoomOutView;

/**
 This method is called when another controller has been closed.
 */
- (void)splashZoomOutViewAdDidDismissFullScreenModal:(UIView *_Nonnull)splashZoomOutView;


@end
