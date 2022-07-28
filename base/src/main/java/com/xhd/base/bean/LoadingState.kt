package com.xhd.base.bean

/**
 * Created by maoqi on 2020/9/23.
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
