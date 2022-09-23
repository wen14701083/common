package com.xhd.android.bean

/**
 * loading状态
 */
sealed class LoadingState {
    object LoadingStart : LoadingState()
    object LoadingFinish : LoadingState()
}

object State {
    const val LOADING_START = 1
    const val LOADING_END = 2
    const val LOADING_FAILED = 3
}
