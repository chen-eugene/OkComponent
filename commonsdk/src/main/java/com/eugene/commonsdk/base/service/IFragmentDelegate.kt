package com.eugene.commonsdk.base.service

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.eugene.commonsdk.integration.EventBusManager
import com.eugene.commonsdk.utils.ArmsUtils

const val FRAGMENT_DELEGATE = "FRAGMENT_DELEGATE"

/**
 * {@link Fragment} 代理类,用于框架内部在每个 {@link Fragment} 的对应生命周期中插入需要的逻辑
 */
interface IFragmentDelegate {


    fun onAttach(context: Context)

    fun onCreate(savedInstanceState: Bundle?)

    fun onCreateView(view: View?, savedInstanceState: Bundle?)

    fun onActivityCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroyView()

    fun onDestroy()

    fun onDetach()

    /**
     * Return true if the fragment is currently added to its activity.
     */
    fun isAdded(): Boolean

}


class FragmentDelegateImpl(fragmentManager: FragmentManager, fragment: Fragment) : IFragmentDelegate {

    private var mFragmentManager: FragmentManager? = null
    private var mFragment: Fragment? = null
    private var iFragment: IFragment? = null

    init {
        this.mFragmentManager = fragmentManager
        this.mFragment = fragment
        this.iFragment = fragment as IFragment
    }

    override fun onAttach(context: Context) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (iFragment?.useEventBus() == true)
        //如果要使用eventbus请将此方法返回true
            EventBusManager.getInstance().register(mFragment)//注册到事件主线
        iFragment?.setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(mFragment?.activity))
    }

    override fun onCreateView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        iFragment?.initData(savedInstanceState)
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroyView() {

    }

    override fun onDestroy() {
        if (iFragment != null && iFragment?.useEventBus() == true)
        //如果要使用eventbus请将此方法返回true
            EventBusManager.getInstance().unregister(mFragment)//注册到事件主线
        this.mFragmentManager = null
        this.mFragment = null
        this.iFragment = null
    }

    override fun onDetach() {

    }

    override fun isAdded(): Boolean {
        return mFragment != null && mFragment?.isAdded == true
    }

}
