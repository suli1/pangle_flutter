//
//  FLTInterstitialExpressAdTask.swift
//  pangle_flutter
//
//  Created by nullptrX on 2020/8/16.
//

import ABUAdSDK

internal final class FLTInterstitialExpressAdTask: FLTTaskProtocol {
    private var manager: ABUInterstitialAd
    private var delegate: ABUInterstitialAdDelegate?
    
    internal init(_ manager: ABUInterstitialAd) {
        self.manager = manager
    }
    
    convenience init(_ args: [String: Any?]) {
        let slotId: String = args["slotId"] as! String
        let expressArgs = args["expressSize"] as! [String: Double]
        let width = expressArgs["width"]!
        let height = expressArgs["height"]!
        let adSize = CGSize(width: width, height: height)
        let manager = ABUInterstitialAd(adUnitID: slotId, size: adSize)
        self.init(manager)
    }
    
    func execute() -> (@escaping (FLTTaskProtocol, Any) -> Void) -> Void {
        { result in
            let delegate = FLTInterstitialExpressAd(success: { [weak self] () in
                guard let self = self else {
                    return
                }
                result(self, ["code": 0])
            }, fail: { [weak self] error in
                guard let self = self else {
                    return
                }
                let e = error as NSError?
                result(self, ["code": e?.code ?? -1, "message": error?.localizedDescription ?? ""])
            })

            self.manager.delegate = delegate
            self.delegate = delegate

            self.manager.loadData()
        }
    }
}
