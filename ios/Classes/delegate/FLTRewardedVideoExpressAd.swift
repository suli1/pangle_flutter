//
//  FLTRewardedVideoExpressAd.swift
//  ttad
//
//  Created by Jerry on 2020/7/20.
//

import Foundation
import ABUAdSDK

internal final class FLTRewardedVideoExpressAd: NSObject, ABURewardedVideoAdDelegate {
    typealias Success = (Bool) -> Void
    typealias Fail = (Error?) -> Void

    private var verify = false
    private var loadingType: LoadingType
    private var slotId: String

    let success: Success?
    let fail: Fail?

    init(_ slotId: String, _ loadingType: LoadingType, success: Success?, fail: Fail?) {
        self.slotId = slotId
        self.loadingType = loadingType
        self.success = success
        self.fail = fail
    }

    func rewardedVideoAdDidLoad(_ rewardedVideoAd: ABURewardedVideoAd) {
        let preload = loadingType == .preload || loadingType == .preload_only
        if preload {
            rewardedVideoAd.extraDelegate = self;
            /// 存入缓存
            PangleAdManager.shared.setRewardedVideoAd(slotId, rewardedVideoAd)
            /// 必须回调，否则task不能销毁，导致内存泄漏
            self.success?(false)
        } else {
            let vc = AppUtil.getVC()
            rewardedVideoAd.show(fromRootViewController: vc)
        }
    }
    
    func rewardedVideoAdDidClose(_ rewardedVideoAd: ABURewardedVideoAd) {
        if rewardedVideoAd.didReceiveSuccess != nil {
            rewardedVideoAd.didReceiveSuccess?(self.verify)
        } else {
            self.success?(self.verify)
        }
    }
    
    func rewardedVideoAd(_ rewardedVideoAd: ABURewardedVideoAd, didFailWithError error: Error?) {
        if rewardedVideoAd.didReceiveFail != nil {
            rewardedVideoAd.didReceiveFail?(error)
        } else {
            self.fail?(error)
        }
    }
    
    func rewardedVideoAdServerRewardDidSucceed(_ rewardedVideoAd: ABURewardedVideoAd, rewardInfo: ABUAdapterRewardAdInfo?, verify: Bool) {
        self.verify = verify
    }

    func rewardedVideoAdDidShowFailed(_ rewardedVideoAd: ABURewardedVideoAd, error: Error) {
        if rewardedVideoAd.didReceiveFail != nil {
            rewardedVideoAd.didReceiveFail?(error)
        } else {
            self.fail?(error)
        }
    }
}

private var delegateKey = "nullptrx.github.io/delegate"
private var successKey = "nullptrx.github.io/delegate_success"
private var failKey = "nullptrx.github.io/delegate_fail"

extension ABURewardedVideoAd {
    var extraDelegate: ABURewardedVideoAdDelegate? {
        get {
            return objc_getAssociatedObject(self, &delegateKey) as? ABURewardedVideoAdDelegate
        }
        set {
            objc_setAssociatedObject(self, &delegateKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }

    var didReceiveSuccess: ((Bool) -> Void)? {
        get {
            objc_getAssociatedObject(self, &successKey) as? ((Bool) -> Void)
        }
        set {
            objc_setAssociatedObject(self, &successKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }

    var didReceiveFail: ((Error?) -> Void)? {
        get {
            objc_getAssociatedObject(self, &failKey) as? ((Error?) -> Void)
        }
        set {
            objc_setAssociatedObject(self, &failKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
}
