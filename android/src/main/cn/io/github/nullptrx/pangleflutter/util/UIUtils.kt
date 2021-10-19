package io.github.nullptrx.pangleflutter.util

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * Created by bytedance on 2019/9/5.
 */
object UIUtils {
  fun getScreenWidth(context: Context): Int {
    val dm = context.resources.displayMetrics
    return dm.widthPixels
  }

  fun removeFromParent(view: View?) {
    if (view != null) {
      val vp = view.parent
      if (vp is ViewGroup) {
        vp.removeView(view)
      }
    }
  }
}
