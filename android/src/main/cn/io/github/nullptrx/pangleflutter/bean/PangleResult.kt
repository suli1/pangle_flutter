package io.github.nullptrx.pangleflutter.bean

import java.util.HashMap

class PangleResult {

  var code = 0
  var message: String? = null
  var verify: Boolean? = null
  var count: Int? = null
  var data: List<*>? = null

  fun toMap(): HashMap<String, Any?> {
    val map = HashMap<String, Any?>()
    map["code"] = code
    map["message"] = message
    map["verify"] = verify
    map["count"] = count
    map["data"] = data
    return map
  }
}
