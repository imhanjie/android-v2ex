package com.imhanjie.v2ex.vm

import androidx.lifecycle.MutableLiveData
import com.imhanjie.v2ex.model.TopicTab
import com.imhanjie.v2ex.parser.model.TopicItem
import com.imhanjie.v2ex.repository.provideAppRepository

class TabViewModel : BaseViewModel() {

    lateinit var tab: TopicTab
    val loadingState = MutableLiveData<Boolean>()
    val swipeLoadingState = MutableLiveData<Boolean>()
    val topicData: MutableLiveData<List<TopicItem>> = MutableLiveData()

    fun loadTopics(fromSwipe: Boolean) {
        request {
            if (fromSwipe) swipeLoadingState.value = true else loadingState.value = true
            val topics = provideAppRepository().loadLatestTopics(tab.value, 1)
            if (fromSwipe) swipeLoadingState.value = false else loadingState.value = false
            topicData.value = topics
        }
    }

}