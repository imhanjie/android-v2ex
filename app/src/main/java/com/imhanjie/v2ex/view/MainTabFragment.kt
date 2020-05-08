package com.imhanjie.v2ex.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.imhanjie.support.e
import com.imhanjie.v2ex.BaseLazyFragment
import com.imhanjie.v2ex.databinding.FragmentTabMainBinding
import com.imhanjie.v2ex.vm.MyViewModel

class MainTabFragment : BaseLazyFragment<FragmentTabMainBinding>() {

    private lateinit var fragmentAdapter: FragmentStateAdapter
    private lateinit var vm: MyViewModel

    override fun getViewModels() = listOf(vm)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun initViews() {
        fragmentAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return vm.tabs.size
            }

            override fun createFragment(position: Int): Fragment {
                return TabFragment.newInstance(vm.tabs[position])
            }
        }
        vb.viewPager.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        vb.viewPager.adapter = fragmentAdapter
        TabLayoutMediator(vb.tabLayout, vb.viewPager) { tab, position ->
            tab.text = vm.tabs[position].title
        }.attach()

        vb.topBar.setOnRightClickListener(View.OnClickListener {
            val targetUiMode = when (configSp.getInt("ui_mode", AppCompatDelegate.MODE_NIGHT_NO)) {
                AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_NO
            }
            configSp.putInt("ui_mode", targetUiMode)
            AppCompatDelegate.setDefaultNightMode(targetUiMode)
        })
    }

    override fun onLazyLoad() {
        e("onLazyLoad: MainTabFragment()")
    }

    override fun onResumeAfterLazyLoad() {
        super.onResumeAfterLazyLoad()
        e("onResumeAfterLazyLoad: MainTabFragment()")
    }

}