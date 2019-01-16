package com.example.zhihu.mvp.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.eugene.commonsdk.base.BaseHolder
import com.eugene.commonsdk.di.component.AppComponent
import com.eugene.commonsdk.module.imageloader.ImageLoader
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.imageloader_glide.config.GlideImageConfigImpl
import com.example.zhihu.R
import com.example.zhihu.mvp.model.entity.DailyListBean

class ZhihuHomeHolder(itemView: View) : BaseHolder<DailyListBean.StoriesBean>(itemView) {

    private val mAppComponent: AppComponent
    private val mImageLoader: ImageLoader?

    init {
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.context)
        mImageLoader = mAppComponent.imageLoader()
    }

    override fun onBindViewData(data: DailyListBean.StoriesBean, pos: Int) {
        findView<TextView>(R.id.tv_name).text = data.title

        val ivAvatar = findView<ImageView>(R.id.iv_avatar)
        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader?.loadImage(itemView.context,
                GlideImageConfigImpl
                        .builder()
                        .url(data.images[0])
                        .imageView(ivAvatar)
                        .build())


    }

    override fun onRelease() {
        mImageLoader?.clear(mAppComponent.application(),
                GlideImageConfigImpl
                        .builder()
                        .imageView(findView(R.id.iv_avatar))
                        .build())
    }

}