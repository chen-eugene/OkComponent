package com.example.zhihu.di.component

import com.eugene.commonsdk.di.component.AppComponent
import com.eugene.commonsdk.di.scope.ActivityScope
import com.example.zhihu.di.module.ZhihuHomeModule
import com.example.zhihu.mvp.contract.ZhihuHomeContract
import com.example.zhihu.mvp.ui.activity.ZhihuHomeActivity
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(modules = [ZhihuHomeModule::class], dependencies = [AppComponent::class])
interface ZhihuHomeComponent {

    fun inject(activity: ZhihuHomeActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun view(view: ZhihuHomeContract.View): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): ZhihuHomeComponent
    }


}