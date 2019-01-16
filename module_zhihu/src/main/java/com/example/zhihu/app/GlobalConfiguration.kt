package com.example.zhihu.app

import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.eugene.commonsdk.base.service.IAppLifecycle
import com.eugene.commonsdk.di.module.GlobalConfigModule
import com.eugene.commonsdk.integration.IConfigModule
import com.eugene.commonsdk.integration.cache.IntelligentCache
import com.eugene.commonsdk.utils.ArmsUtils
import com.example.zhihu.BuildConfig
import com.squareup.leakcanary.RefWatcher

class GlobalConfiguration : IConfigModule {
    override fun applyOptions(context: Context?): GlobalConfigModule? {
        return null
    }

    override fun injectAppLifecycle(context: Context?, lifecycles: MutableList<IAppLifecycle>) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(AppLifecyclesImpl())
    }

    override fun injectActivityLifecycle(context: Context?, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>) {
    }

    override fun injectFragmentLifecycle(context: Context?, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
        //当所有模块集成到宿主 App 时, 在 App 中已经执行了以下代码, 所以不需要再执行
        if (BuildConfig.IS_APP) {
            lifecycles.add(object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                    val refWatcher = ArmsUtils.obtainAppComponentFromContext(f.activity)
                            .extras()
                            ?.get(IntelligentCache.getKeyOfKeep(RefWatcher::class.java.name))

                    if (refWatcher is RefWatcher){
                        refWatcher.watch(f)
                    }
                }
            })
        }
    }
}