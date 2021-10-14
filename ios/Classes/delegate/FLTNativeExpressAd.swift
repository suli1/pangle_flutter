//
//  FLTNativeExpressAdViewDelegate.swift
//  pangle_flutter
//
//  Created by nullptrX on 2020/8/16.
//

import ABUAdSDK

internal final class FLTNativeExpressAdViewDelegate: NSObject, ABUNativeAdsManagerDelegate, ABUNativeAdViewDelegate  {
    typealias Success = ([String]) -> Void
    typealias Fail = (Error?) -> Void

    let success: Success?
    let fail: Fail?

    init(success: Success?, fail: Fail?) {
        self.success = success
        self.fail = fail
    }
    
    func nativeAdsManagerSuccess(toLoad adsManager: ABUNativeAdsManager, nativeAds nativeAdViewArray: [ABUNativeAdView]?) {
        if nativeAdViewArray != nil {
            /// 设置view的关联对象，让delegate随着view的销毁一起销毁
            nativeAdViewArray!.forEach {
                $0.extraDelegate = self
                $0.extraManager = adsManager
                $0.delegate = self
                $0.rootViewController = AppUtil.getCurrentVC()
                if($0.hasExpressAdGot) {
                    $0.render()
                }
            }
            /// 存入缓存
            PangleAdManager.shared.setExpressAd(nativeAdViewArray)
            success?(nativeAdViewArray!.map {
                String($0.hash)
            })
        }
    }
    
    func nativeAdsManager(_ adsManager: ABUNativeAdsManager, didFailWithError error: Error?) {
        if error != nil {
            fail?(error)
        }
    }
    
    func nativeAdExpressViewRenderFail(_ nativeExpressAdView: ABUNativeAdView, error: Error?) {
        postMessage(nativeExpressAdView, "onRenderFail")
    }

    func nativeAdExpressViewRenderSuccess(_ nativeExpressAdView: ABUNativeAdView) {
        postMessage(nativeExpressAdView, "onRenderSuccess", arguments: ["width" : nativeExpressAdView.frame.size.width, "height" : nativeExpressAdView.frame.size.height])
    }
    
    func nativeAdExpressView(_ nativeAdView: ABUNativeAdView, stateDidChanged playerState: ABUPlayerPlayState) {
    }

    func nativeAdDidClick(_ nativeAdView: ABUNativeAdView, with view: UIView?) {
        postMessage(nativeAdView, "onClick")
    }
    
    func nativeAdDidBecomeVisible(_ nativeAdView: ABUNativeAdView) {
        postMessage(nativeAdView, "onShow")
    }

    private func postMessage(_ nativeExpressAdView: ABUNativeAdView, _ method: String, arguments: [String: Any?] = [:]) {
        let channel = nativeExpressAdView.extraChannel
        channel?.invokeMethod(method, arguments: arguments)
    }
}

private var managerKey = "nullptrx.github.io/manager"
private var delegateKey = "nullptrx.github.io/delegate"
private var channelKey = "nullptrx.github.io/channel"

extension ABUNativeAdView {
    var extraManager: ABUNativeAdsManager? {
        get {
            objc_getAssociatedObject(self, &managerKey) as? ABUNativeAdsManager
        }
        set {
            objc_setAssociatedObject(self, &managerKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }

    var extraDelegate: ABUNativeAdsManagerDelegate? {
        get {
            objc_getAssociatedObject(self, &delegateKey) as? ABUNativeAdsManagerDelegate
        }
        set {
            objc_setAssociatedObject(self, &delegateKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }

    weak var extraChannel: FlutterMethodChannel? {
        get {
            objc_getAssociatedObject(self, &channelKey) as? FlutterMethodChannel
        }
        set {
            objc_setAssociatedObject(self, &channelKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
}
