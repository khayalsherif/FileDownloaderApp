package az.khayalsharifli.fileDownloader.tools

import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.gone() {
    isGone = true
}

fun View.visible() {
    isVisible = true
}

fun View.isEnable() {
    isEnabled = true
}

fun View.isNotEnable() {
    isEnabled = false
}