package com.imhanjie.v2ex.view

import android.os.Bundle
import com.imhanjie.v2ex.BaseActivity
import com.imhanjie.v2ex.R
import com.imhanjie.v2ex.databinding.ActivityFavoriteTopicsBinding
import com.imhanjie.v2ex.view.fragment.FavoriteTopicsFragment
import com.imhanjie.v2ex.vm.BaseViewModel

class FavoriteTopicsActivity : BaseActivity<ActivityFavoriteTopicsBinding>() {

    override fun initViewModels(): List<BaseViewModel> {
        return emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preFragment = supportFragmentManager.findFragmentById(R.id.list_container)
        if (preFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.list_container, FavoriteTopicsFragment())
                .commit()
        }
    }

}