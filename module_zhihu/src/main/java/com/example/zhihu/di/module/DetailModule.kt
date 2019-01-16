package com.example.zhihu.di.module

import android.app.Dialog
import com.eugene.commonsdk.di.scope.ActivityScope
import com.example.commonres.dialog.ProgresDialog
import com.example.zhihu.mvp.contract.DetailContract
import com.example.zhihu.mvp.model.ZhihuModel
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [DetailModule.SubModule::class])
class DetailModule {

    @ActivityScope
    @Provides
    internal fun provideDialog(view: DetailContract.View): Dialog {
        return ProgresDialog(view.getActivity())
    }

    @Module
    interface SubModule {

        @Binds
        fun bindZhihuModel(model: ZhihuModel): DetailContract.Model

    }

}