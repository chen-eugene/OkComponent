package com.example.zhihu.mvp.ui.adapter

import android.view.View
import com.eugene.commonsdk.base.BaseHolder
import com.eugene.commonsdk.base.DefAdapter
import com.example.zhihu.R
import com.example.zhihu.mvp.model.entity.DailyListBean
import com.example.zhihu.mvp.ui.holder.ZhihuHomeHolder

class ZhihuHomeAdapter(datas: MutableList<DailyListBean.StoriesBean>)
    : DefAdapter<DailyListBean.StoriesBean>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.zhihu_recycle_list
    }

    override fun getHolder(v: View, viewType: Int): BaseHolder<DailyListBean.StoriesBean> {
        return ZhihuHomeHolder(v)
    }
}