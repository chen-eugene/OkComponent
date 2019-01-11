package com.eugene.commonsdk.base

import android.app.Application
import android.content.Context
import com.eugene.commonsdk.base.proxy.AppDelegate
import com.eugene.commonsdk.base.service.IAppLifecycle
import com.eugene.commonsdk.di.component.AppComponent
import com.eugene.commonsdk.utils.Preconditions

class BaseApplication : Application(), IApp {

    private var mAppProxy: IAppLifecycle? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (mAppProxy == null)
            mAppProxy = AppDelegate(base)

        this.mAppProxy?.attachBaseContext(base)
    }

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     */
    override fun onCreate() {
        super.onCreate()
        this.mAppProxy?.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        this.mAppProxy?.onTerminate(this)
    }

    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     */
    override fun getAppComponent(): AppComponent? {
        Preconditions.checkNotNull(mAppProxy, "%s cannot be null", AppDelegate::class.java.name)
        Preconditions.checkState(mAppProxy is IApp, "%s must be implements %s", AppDelegate::class.java.name, IApp::class.java.name)
        
        if (mAppProxy is IApp){
            return (mAppProxy as IApp).getAppComponent()
        }
        
        return null
    }


}