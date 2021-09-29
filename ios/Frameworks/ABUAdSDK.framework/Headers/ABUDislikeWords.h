//
//  ABUDislikeWords.h
//  BUAdSDK
//
//  Copyright © 2017年 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ABUDislikeWords : NSObject <NSCoding>
@property (nonatomic, copy) NSString *dislikeID;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, assign) BOOL isSelected;
@property (nonatomic, copy) NSArray<ABUDislikeWords *> *options;

- (instancetype)initWithDictionary:(NSDictionary *)dict error:(NSError **)error;
@end
