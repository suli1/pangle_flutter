//
//  ABUUserInfoForSegment.h
//  ABUAdSDK
//
//  Created by heyinyin on 2021/2/25.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM (NSInteger, ABUUserInfo_Gender) {
    ABUUserInfo_Gender_Female                   = 0,
    ABUUserInfo_Gender_Male                     = 1,
    ABUUserInfo_Gender_Unknown                  = 2,
    ABUUserInfo_Gender_UnSet                    = 3//default，can't use.
};


/// 流量分组信息
@interface ABUUserInfoForSegment : NSObject
/// user_id
@property (nonatomic, copy) NSString *user_id;
/// 渠道
@property (nonatomic, copy) NSString *channel;
@property (nonatomic, copy) NSString *sub_channel;
/// user age，type:int
@property (nonatomic, assign) NSInteger age;
/// user gender,please use ABUUserInfo_Gender to set
@property (nonatomic, assign) ABUUserInfo_Gender gender;
/// 价值分组
@property (nonatomic, copy) NSString *user_value_group;
/// custom setting
/**
 要求:
    自定义参数key&value要求均为string
    字符为数字,字母,"-","_"任意组合
    长度上限为100
 */
@property (nonatomic, copy) NSDictionary *customized_id;
@end

