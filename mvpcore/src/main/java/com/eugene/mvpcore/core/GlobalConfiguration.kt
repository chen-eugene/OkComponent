/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eugene.mvpcore.core

import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.eugene.commonsdk.base.delegate.AppDelegate
import com.eugene.commonsdk.base.service.IAppLifecycle
import com.eugene.commonsdk.core.ActivityLifecycleCallbacksImpl
import com.eugene.commonsdk.core.FragmentLifecycleCallbacksImpl
import com.eugene.commonsdk.core.GlobalHttpHandlerImpl
import com.eugene.commonsdk.core.ResponseErrorListenerImpl
import com.eugene.commonsdk.di.module.AppModule
import com.eugene.commonsdk.di.module.ClientModule
import com.eugene.commonsdk.di.module.GlobalConfigModule
import com.eugene.commonsdk.integration.IConfigModule
import com.eugene.commonsdk.integration.cache.IntelligentCache
import com.eugene.commonsdk.module.imageloader.BaseImageLoaderStrategy
import com.eugene.commonsdk.module.imageloader.ImageConfig
import com.eugene.commonsdk.module.network.SSLSocketClient
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.commonsdk.utils.ManifestParser
import com.eugene.imageloader_glide.GlideImageLoaderStrategy
import com.eugene.mvpcore.BuildConfig
import com.google.gson.GsonBuilder
import com.squareup.leakcanary.RefWatcher
import io.rx_cache2.internal.RxCache
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import timber.log.Timber


/**
 * ================================================
 * 组件的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * CommonSDK 中已有 [GlobalConfiguration] 配置有所有组件都可公用的配置信息
 * 这里用来配置一些组件自身私有的配置信息
 *
 * @see AppDelegate
 *
 * @see ManifestParser
 * ================================================
 */
class GlobalConfiguration : IConfigModule {

    override fun applyOptions(context: Context?): GlobalConfigModule? {
        return GlobalConfigModule.build {
            apiUrl { "" }
            imageLoaderStrategy { GlideImageLoaderStrategy() as BaseImageLoaderStrategy<ImageConfig> }
            globalHttpHandler { GlobalHttpHandlerImpl(context) }
            responseErrorListener { ResponseErrorListenerImpl() }
            gsonConfiguration {
                object : AppModule.GsonConfiguration {
                    override fun configGson(context: Context, builder: GsonBuilder) {
                        //支持序列化null的参数
                        builder.serializeNulls()
                                .enableComplexMapKeySerialization()//支持将序列化key为object的map,默认只能序列化key为string的map
                    }
                }
            }
            okhttpConfiguration {
                object : ClientModule.OkhttpConfiguration {
                    override fun configOkhttp(context: Context, builder: OkHttpClient.Builder) {
                        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager())
                        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                        //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                        RetrofitUrlManager.getInstance().with(builder)
                    }
                }
            }
            rxCacheConfiguration {
                //这里可以自己自定义配置RxCache的参数
                object : ClientModule.RxCacheConfiguration {
                    override fun configRxCache(context: Context, builder: RxCache.Builder): RxCache? {
                        builder.useExpiredDataIfLoaderNotAvailable(true)
                        return null
                    }
                }
            }
        }
    }


    override fun injectAppLifecycle(context: Context?, lifecycles: MutableList<IAppLifecycle>) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
//        lifecycles.add(AppLifecyclesImpl())
        lifecycles.add(object :IAppLifecycle{
            override fun attachBaseContext(base: Context?) {
            }

            override fun onCreate(base: Application) {
                if (BuildConfig.LOG_DEBUG){//Timber日志打印
                    Timber.plant(Timber.DebugTree())
                    ARouter.openLog()     // 打印日志
                    ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                    RetrofitUrlManager.getInstance().setDebug(true)
                }
                ARouter.init(base) // 尽可能早,推荐在Application中初始化
            }

            override fun onTerminate(base: Application) {
            }

        })
    }

    override fun injectActivityLifecycle(context: Context?, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>) {
        lifecycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(context: Context?, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
        lifecycles.add(FragmentLifecycleCallbacksImpl())
    }


}
