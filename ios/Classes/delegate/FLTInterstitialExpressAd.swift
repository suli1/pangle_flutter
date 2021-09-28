//
//  FLTInterstitialAd.swift
//  pangle_flutter
//
//  Created by Jerry on 2020/8/12.
//

import Flutter
import ABUAdSDK

internal final class FLTInterstitialExpressAd: NSObject, ABUInterstitialAdDelegate {
    typealias Success = () -> Void
    typealias Fail = (Error?) -> Void
    
    let success: Success?
    let fail: Fail?
    
    init(success: Success?, fail: Fail?) {
        self.success = success
        self.fail = fail
    }
    
    func interstitialAdDidLoad(_ interstitialAd: ABUInterstitialAd) {
        let vc = AppUtil.getVC()
        interstitialAd.show(fromRootViewController: vc)
    }
    
    func interstitialAdDidClose(_ interstitialAd: ABUInterstitialAd) {
        self.success?()
    }
    
    func interstitialAdViewRenderFail(_ interstitialAd: ABUInterstitialAd, error: Error?) {
        self.fail?(error)
    }
    
    func interstitialAdDidShowFailed(_ interstitialAd: ABUInterstitialAd, error: Error) {
        self.fail?(error)
    }
}
