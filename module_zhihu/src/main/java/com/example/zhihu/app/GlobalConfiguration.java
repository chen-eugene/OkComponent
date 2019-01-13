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
package com.example.zhihu.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.eugene.commonsdk.base.proxy.AppDelegate;
import com.eugene.commonsdk.base.service.IAppLifecycle;
import com.eugene.commonsdk.di.module.GlobalConfigModule;
import com.eugene.commonsdk.integration.IConfigModule;
import com.eugene.commonsdk.integration.cache.IntelligentCache;
import com.eugene.commonsdk.utils.ArmsUtils;
import com.eugene.commonsdk.utils.ManifestParser;
import com.example.zhihu.BuildConfig;
import com.squareup.leakcanary.RefWatcher;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * ================================================
 * 组件的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * CommonSDK 中已有 {@link GlobalConfiguration} 配置有所有组件都可公用的配置信息
 * 这里用来配置一些组件自身私有的配置信息
 *
 * @see AppDelegate
 * @see ManifestParser
 * ================================================
 */
public final class GlobalConfiguration implements IConfigModule {

    @NotNull
    @Override
    public GlobalConfigModule applyOptions(@Nullable Context context) {
        return null;
    }


    @Override
    public void injectAppLifecycle(@Nullable Context context, @NotNull List<IAppLifecycle> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(@Nullable Context context, @NotNull List<Application.ActivityLifecycleCallbacks> lifecycles) {
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        //当所有模块集成到宿主 App 时, 在 App 中已经执行了以下代码, 所以不需要再执行
        if (!BuildConfig.IS_APP) {
            lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    ((RefWatcher) ArmsUtils
                            .obtainAppComponentFromContext(f.getActivity())
                            .extras()
                            .get(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())))
                            .watch(f);
                }
            });
        }
    }


}
