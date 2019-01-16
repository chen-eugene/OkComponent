package com.example.zhihu.di.component

import com.eugene.commonsdk.di.component.AppComponent
import com.eugene.commonsdk.di.scope.ActivityScope
import com.example.zhihu.di.module.DetailModule
import com.example.zhihu.mvp.contract.DetailContract
import com.example.zhihu.mvp.ui.activity.DetailActivity
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(modules = [DetailModule::class], dependencies = [AppComponent::class])
interface DetailComponent {

    fun inject(activity: DetailActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun view(view: DetailContract.View): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): DetailComponent

    }


}