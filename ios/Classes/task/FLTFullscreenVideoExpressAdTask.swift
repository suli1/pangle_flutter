//
//  FLTFullscreenVideoExpressAdTask.swift
//  pangle_flutter
//
//  Created by nullptrX on 2020/8/25.
//

import ABUAdSDK

internal final class FLTFullscreenVideoExpressAdTask: FLTTaskProtocol {
    private var manager: ABUFullscreenVideoAd
    private var delegate: ABUFullscreenVideoAdDelegate?
    private var slotId: String = ""

    internal init(_ manager: ABUFullscreenVideoAd) {
        self.manager = manager
    }

    init(_ args: [String: Any?]) {
        slotId = args["slotId"] as! String
        let manager = ABUFullscreenVideoAd(adUnitID: slotId)
        self.manager = manager
    }

    func execute(_ loadingType: LoadingType) -> (@escaping (FLTTaskProtocol, Any) -> Void) -> Void {
        { result in
            let delegate = FLTFullscreenVideoExpressAd(self.slotId, loadingType: loadingType, success: { [weak self] () in
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
