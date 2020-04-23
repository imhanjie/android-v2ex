package com.imhanjie.v2ex.view

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.imhanjie.v2ex.databinding.ItemReplyBinding
import com.imhanjie.v2ex.parser.model.Reply
import com.imhanjie.widget.recyclerview.base.BaseItemViewDelegate
import com.imhanjie.widget.recyclerview.base.VBViewHolder

class ReplyAdapter : BaseItemViewDelegate<Reply, ItemReplyBinding>() {

    override fun bindTo(holder: VBViewHolder<ItemReplyBinding>, position: Int, item: Reply) {
        val vb = holder.vb
        Glide.with(vb.root)
            .load(item.userAvatar)
            .transform(CircleCrop())
            .into(vb.userAvatar)
        vb.userName.text = item.userName
        vb.time.text = item.time
        vb.content.text = item.content
        vb.no.text = "#${item.no}"
    }

}