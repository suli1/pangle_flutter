//
//  ABUSplashUserData.h
//  ABUAdSDK
//
//  Created by XuQingJia on 2020/11/19.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ABUAdSDKDefines.h"

NS_ASSUME_NONNULL_BEGIN

@interface ABUSplashUserData : NSObject

@property (nonatomic, assign) ABUAdnType adnType;   // adn类型
@property (nonatomic, copy) NSString *_Nonnull rit;     // adn对应代码位
@property (nonatomic, copy) NSString *_Nonnull appID;   // adn对应appID
@property (nonatomic, copy) NSString *_Nullable appKey;     // adn对应appKey, 没有时可不传

@end

NS_ASSUME_NONNULL_END
