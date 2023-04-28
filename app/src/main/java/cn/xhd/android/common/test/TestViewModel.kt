package cn.xhd.android.common.test

import androidx.lifecycle.Transformations
import cn.xhd.android.common.api.UserRepository
import com.xhd.android.base.BaseViewModel

/**
 * @date created on 2023/4/17
 */
class TestViewModel : BaseViewModel() {
    val versionLiveData = Transformations.switchMap(mRefreshLiveData) {
        UserRepository.getVersion()
    }
}
