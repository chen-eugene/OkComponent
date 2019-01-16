package com.eugene.okcomponent.mvp.ui

import android.os.Bundle
import com.eugene.commonsdk.di.component.AppComponent
import com.eugene.mvpcore.base.BaseActivity
import com.eugene.okcomponent.R
import com.example.commonservice.core.RouterHub
import com.example.commonservice.utils.RouterUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity<Nothing>() {


    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_splash
    }

    override fun initData(savedInstanceState: Bundle?) {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    RouterUtil.navigation(this@SplashActivity, RouterHub.APP_MAINACTIVITY)
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
    }

}