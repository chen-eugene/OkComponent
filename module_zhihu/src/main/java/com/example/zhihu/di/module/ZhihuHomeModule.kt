package com.example.zhihu.di.module

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.eugene.commonsdk.di.scope.ActivityScope
import com.example.commonservice.core.RouterHub
import com.example.zhihu.app.ZhihuConstants
import com.example.zhihu.mvp.contract.ZhihuHomeContract
import com.example.zhihu.mvp.model.ZhihuModel
import com.example.zhihu.mvp.model.entity.DailyListBean
import com.example.zhihu.mvp.ui.adapter.ZhihuHomeAdapter
import com.example.zhihu.mvp.ui.holder.ZhihuHomeHolder
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [ZhihuHomeModule.SubModule::class])
class ZhihuHomeModule {

    @ActivityScope
    @Provides
    fun provideLayoutManager(view: ZhihuHomeContract.View): RecyclerView.LayoutManager {
        return LinearLayoutManager(view.getActivity())
    }

    @ActivityScope
    @Provides
    fun provideList(): MutableList<DailyListBean.StoriesBean> {
        return mutableListOf()
    }

    @ActivityScope
    @Provides
    fun provideZhihuHomeAdapter(view: ZhihuHomeContract.View, data: MutableList<DailyListBean.StoriesBean>)
            : RecyclerView.Adapter<*> {

        val adapter = ZhihuHomeAdapter(data)
        adapter.setOnItemClickListener { parent, viewType, storiesBean, pos ->

            ARouter.getInstance()
                    .build(RouterHub.ZHIHU_DETAILACTIVITY)
                    .withInt(ZhihuConstants.DETAIL_ID, storiesBean.id)
                    .withString(ZhihuConstants.DETAIL_TITLE, storiesBean.title)
                    .navigation(view.getActivity())
        }
        return adapter
    }

    @Module
    interface SubModule {

        @Binds
        fun bindZhihuModel(model: ZhihuModel): ZhihuHomeContract.Model

    }

}