package com.eugene.commonsdk.di.module

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.eugene.commonsdk.di.qualifier.ALifecycle
import com.eugene.commonsdk.di.qualifier.RxLifecycle
import com.eugene.commonsdk.integration.*
import com.eugene.commonsdk.integration.cache.Cache
import com.eugene.commonsdk.integration.cache.CacheType
import com.eugene.commonsdk.integration.lifecycle.ActivityLifecycleForRxLifecycle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.http.Field
import java.util.*
import javax.inject.Named

/**
 *  提供一些框架必须的实例的 {@link Module}
 */

@Module(includes = [AppModule.SubModule::class])
object AppModule {

    @Provides
    @JvmStatic
    internal fun provideGson(application: Application, configuration: GsonConfiguration?): Gson {
        val builder = GsonBuilder()
        configuration?.configGson(application, builder)
        return builder.create()
    }

    /**
     * 之前 [AppManager] 使用 Dagger 保证单例, 只能使用 [AppComponent.appManager] 访问
     * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 [AppManager.getAppManager] 访问, 更加方便
     * 但为了不影响之前使用 [AppComponent.appManager] 获取 [AppManager] 的项目, 所以暂时保留这种访问方式
     *
     * @param application
     * @return
     */
    @Provides
    @JvmStatic
    internal fun provideAppManager(application: Application): AppManager {
        return AppManager.getAppManager().init(application)
    }

    @Provides
    @JvmStatic
    internal fun provideExtras(cacheFactory: Cache.Factory?): Cache<String, Any>? {
        val cache = cacheFactory?.build(CacheType.EXTRAS)

        return if (cache != null)
            cache as Cache<String, Any>
        else null
    }

    @Provides
    @JvmStatic
    internal fun provideFragmentLifecycles(): List<FragmentManager.FragmentLifecycleCallbacks> {
        return ArrayList()
    }

    @Module
    interface SubModule {

        @Binds
        fun bindRepositoryManager(repositoryManager: RepositoryManager): IRepositoryManager

//        @ALifecycle
        @Binds
        @Named("ActivityLifecycle")
        fun bindActivityLifecycle(activityLifecycle: ActivityLifecycle): Application.ActivityLifecycleCallbacks

        @Binds
        @Named("ActivityLifecycleForRxLifecycle")
//        @RxLifecycle
        fun bindActivityLifecycleForRxLifecycle(activityLifecycleForRxLifecycle: ActivityLifecycleForRxLifecycle): Application.ActivityLifecycleCallbacks

        @Binds
        fun bindFragmentLifecycle(fragmentLifecycle: FragmentLifecycle): FragmentManager.FragmentLifecycleCallbacks
    }


    interface GsonConfiguration {
        fun configGson(context: Context, builder: GsonBuilder)
    }


}