package com.imhanjie.v2ex.view.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.imhanjie.support.ext.dp
import com.imhanjie.v2ex.BaseLazyFragemnt
import com.imhanjie.v2ex.databinding.FragmentTabNotificationsBinding
import com.imhanjie.v2ex.parser.model.Notifications
import com.imhanjie.v2ex.view.adapter.NotificationAdapter
import com.imhanjie.v2ex.vm.BaseViewModel
import com.imhanjie.v2ex.vm.NotificationsViewModel
import com.imhanjie.widget.LineDividerItemDecoration
import com.imhanjie.widget.LoadingWrapLayout
import com.imhanjie.widget.recyclerview.loadmore.LoadMoreDelegate

class NotificationsTabFragment : BaseLazyFragemnt<FragmentTabNotificationsBinding>() {

    private lateinit var vm: NotificationsViewModel

    override fun getViewModels(): List<BaseViewModel> {
        vm = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        return listOf(vm)
    }

    override fun initViews() {
        vb.loadingLayout.update(LoadingWrapLayout.Status.LOADING)
        vb.swipeRefreshLayout.setOnRefreshListener { vm.loadNotifications(false) }

        vb.rv.addItemDecoration(LineDividerItemDecoration(requireContext(), height = 4f.dp().toInt()))
        val delegate = LoadMoreDelegate(vb.rv) {
            vm.loadNotifications(append = true)
        }
        val notificationAdapter = NotificationAdapter()
        delegate.adapter.apply {
            register(Notifications.Item::class.java, notificationAdapter)
        }
        vm.notificationLiveData.observe(this) {
            vb.loadingLayout.update(LoadingWrapLayout.Status.DONE)
            vb.swipeRefreshLayout.isRefreshing = false

            val (notifications, append, hasMore) = it
            delegate.apply {
                val isFirst = items.isEmpty()
                if (isFirst || !append) {
                    items.clear()
                    items.addAll(notifications.items)
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.notifyItemChanged(items.itemSize - 1)   // fix 最后一项 divider 不刷新的问题
                    val originSize = items.itemSize
                    items.addAll(notifications.items)
                    adapter.notifyItemRangeInserted(originSize, notifications.items.size)
                }
                notifyLoadSuccess(hasMore)
            }
        }
    }

    override fun onFirstResume() {
        vm.loadNotifications(append = false)
    }

}