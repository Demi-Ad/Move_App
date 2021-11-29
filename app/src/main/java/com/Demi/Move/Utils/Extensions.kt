package com.Demi.Move.Utils

import com.Demi.Move.MoveApplication
import java.io.File

fun String.toShotPath(root:String):String {
    if (this.isNotEmpty())
        return this.split(root)[1]

    return ""
}

fun File.walkForPreference():FileTreeWalk =
    MoveApplication.prefs.getInt(Constant.DEPTH,0).let {
    if (it > 0)
        return@let this.walk().maxDepth(it)
    else
        return@let this.walk()
}