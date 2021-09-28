//
//  FLTRewardedVideoExpressAdTask.swift
//  pangle_flutter
//
//  Created by nullptrX on 2020/8/16.
//

import ABUAdSDK

internal final class FLTRewardedVideoExpressAdTask: FLTTaskProtocol {
    var rewardedVideoAd: ABURewardedVideoAd?
    private var slotId: String = ""
    private var delegate: ABURewardedVideoAdDelegate?
    
    internal init(_ manager: ABURewardedVideoAd) {
            self.rewardedVideoAd = manager
        }

    init(_ args: [String: Any?]) {
        slotId = args["slotId"] as! String
        let userId: String = args["userId"] as? String ?? ""
        let rewardName: String? = args["rewardName"] as? String
        let rewardAmount: Int? = args["rewardAmount"] as? Int
        let extra: String? = args["extra"] as? String
        
        let model = ABURewardedVideoModel()
        model.userId = userId
        if rewardName != nil {
            model.rewardName = rewardName
        }
        if rewardAmount != nil {
            model.rewardAmount = rewardAmount!
        }
        if extra != nil {
            model.extra = extra
        }
        self.rewardedVideoAd = ABURewardedVideoAd(adUnitID: slotId, rewardedVideoModel: model)
        self.rewardedVideoAd!.mutedIfCan = false
    }

    func execute(_ loadingType: LoadingType) -> (@escaping (FLTTaskProtocol, Any) -> Void) -> Void {
        { [unowned self]result in
            let delegate = FLTRewardedVideoExpressAd(slotId, loadingType, success: { [weak self] verify in
                guard let self = self else {
                    return
                }
                result(self, ["code": 0, "verify": verify])
            }, fail: { [weak self] error in
                guard let self = self else {
                    return
                }
                let e = error as NSError?
                result(self, ["code": e?.code ?? -1, "message": error?.localizedDescription ?? ""])
            })
            self.rewardedVideoAd!.delegate = delegate;
            self.delegate = delegate;
            //当前配置拉取成功直接加载广告
            if ABUAdSDKManager.configDidLoad() {
                self.rewardedVideoAd?.loadData()
            } else {
                // 配置没有拉取成功，配置到达回调里加载广告
                ABUAdSDKManager.addConfigLoadSuccessObserver(self) { (Any) in
                    self.rewardedVideoAd?.loadData()
                }
            }
        }
    }
}
