package com.eugene.mvpcore.base

import android.app.Application
import android.content.Context
import com.eugene.mvpcore.di.component.AppComponent

class BaseApplication : Application(),IApp{

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun getAppComponent(): AppComponent {

    }


}