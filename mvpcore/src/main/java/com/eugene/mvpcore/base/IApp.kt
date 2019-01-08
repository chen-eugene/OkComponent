package com.eugene.mvpcore.base

import com.eugene.mvpcore.di.component.AppComponent

interface IApp {

    fun getAppComponent(): AppComponent

}