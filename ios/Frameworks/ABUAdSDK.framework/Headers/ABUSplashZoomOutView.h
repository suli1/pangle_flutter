//
//  ABUSplashZoomOutView.h
//  ABUAdSDK
//
//  Created by CHAORS on 2021/4/22.
//

#import <UIKit/UIKit.h>

#import "ABUZoomOutSplashAdDelegate.h"


// 开屏点睛处理时机
typedef NS_ENUM (NSInteger, ABUAddOccasionType) {
    ABUAddOccasionTypeWhenLoad = 0,       // 需要在load回调里处理开屏点睛视图ABUSplashZoomOutView，eg:gdt
    ABUAddOccasionTypeWhenClose,          // 需要在close回调里处理开屏点睛视图ABUSplashZoomOutView，eg:pangle
};


@interface ABUSplashZoomOutView : UIView

/**
 The delegate for receiving state change messages When it is a zoom out advertisement.
 */
@property (nonatomic, weak, nullable) id<ABUZoomOutSplashAdDelegate> delegate;

/*
Root view controller for handling ad actions.
*/
@property (nonatomic, strong, nullable) UIViewController *rootViewController;

/**
Suggested size for show.
*/
@property (nonatomic, assign, readonly) CGSize suggestedSize;


/**
 Whether the splashZoomOutView has its own animation.If not, it is recommended that users implement animation operations by themselves.
*/
@property (nonatomic, assign, readonly) BOOL hasAnimation;

/**
 Indicates that the developer needs to add the view to the super view and config it at the right time.
*/
@property (nonatomic, assign, readonly) ABUAddOccasionType addOccasionType;


@end

