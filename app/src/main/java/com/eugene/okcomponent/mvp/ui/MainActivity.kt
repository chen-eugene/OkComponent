package com.eugene.okcomponent.mvp.ui

import android.os.Bundle
import android.view.View
import com.airbnb.lottie.utils.Utils
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.eugene.commonsdk.di.component.AppComponent
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.mvpcore.base.BaseActivity
import com.eugene.okcomponent.R
import com.example.commonservice.core.RouterHub
import com.example.commonservice.utils.RouterUtil
import com.example.commonservice.zhuhu.service.ZhihuInfoService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<Nothing>(), View.OnClickListener {

    @Autowired(name = RouterHub.ZHIHU_SERVICE_ZHIHUINFOSERVICE)
    internal var mZhihuInfoService: ZhihuInfoService? = null

    private var mPressedTime: Long = 0

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)

        loadZhihuInfo()

    }

    private fun loadZhihuInfo() {
        //当非集成调试阶段, 宿主 App 由于没有依赖其他组件, 所以使用不了对应组件提供的服务
        if (mZhihuInfoService == null) {
            bt_zhihu?.isEnabled = false
            return
        }
        bt_zhihu?.text = mZhihuInfoService?.info?.name
    }

    override fun onBackPressed() {
        //获取第一次按键时间
        val mNowTime = System.currentTimeMillis()
        //比较两次按键时间差
        if (mNowTime - mPressedTime > 2000) {
            ArmsUtils.makeText(applicationContext,
                    "再按一次退出" + ArmsUtils.getString(applicationContext, R.string.public_app_name))
            mPressedTime = mNowTime
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bt_zhihu -> {
                RouterUtil.navigation(this@MainActivity, RouterHub.ZHIHU_HOMEACTIVITY)
            }
        }
    }
}
