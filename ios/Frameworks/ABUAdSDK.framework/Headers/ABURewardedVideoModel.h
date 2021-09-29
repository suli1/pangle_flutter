//
//  ABURewardedVideoModel.h
//  ABUAdSDK
//
//  Copyright © 2018年 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
// For BUAdSDK & GDT & Admob & Sigmob & Ks.
@interface ABURewardedVideoModel : NSObject

/**
   required.
   Third-party game user_id identity.
   Mainly used in the reward issuance, it is the callback pass-through parameter from server-to-server.
   It is the unique identifier of each user.
   In the non-server callback mode, it will also be pass-through when the video is finished playing.
   Only the string can be passed in this case, not nil. For BUAdSDK & GDT & Admob & Sigmob & Ks.
 */
@property (nonatomic, copy) NSString *userId;

//optional. reward name. For BUAdSDK & Ks.
@property (nonatomic, copy) NSString *rewardName;

//optional. number of rewards. For BUAdSDK & Ks.
@property (nonatomic, assign) NSInteger rewardAmount;

//optional. custom reward string to include in the server-to-server callback. For BUAdSDK & GDT & Admob & Sigmob & Ks.Need to be a json string for dictionary, eg:""key1":"value1", "key2":"value2""
@property (nonatomic, copy) NSString *extra;

@end
