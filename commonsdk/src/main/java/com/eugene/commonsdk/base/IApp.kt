package com.eugene.commonsdk.base

import com.eugene.commonsdk.di.component.AppComponent


interface IApp {

    fun getAppComponent(): AppComponent?

}