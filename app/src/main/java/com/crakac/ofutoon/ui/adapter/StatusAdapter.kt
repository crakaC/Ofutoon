package com.crakac.ofutoon.ui.adapter

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.databinding.library.baseAdapters.BR
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.databinding.ListItemStatusBinding
import com.crakac.ofutoon.ui.widget.ContentMovementMethod
import com.crakac.ofutoon.util.GlideApp
import com.crakac.ofutoon.util.TextUtil

class StatusAdapter : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {
    val items = ArrayList<Status>()

    fun add(newItems: Collection<Status>) {
        val oldSize = itemCount
        items.addAll(newItems)
        notifyItemRangeInserted(oldSize, newItems.size)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.setup(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(View.inflate(parent.context, R.layout.list_item_status, null))
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].reblog != null) {
            HolderType.Reblog.rawValue
        } else {
            HolderType.Status.rawValue
        }
    }

    private enum class HolderType(val rawValue: Int) {
        Status(3),
        Reblog(4)
    }

    class StatusViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val binding = DataBindingUtil.bind<ListItemStatusBinding>(root)!!
        init {
            binding.content.movementMethod = ContentMovementMethod.instance
        }

        fun setup(status: Status){
            if(status.reblog != null){
                StatusBinder.bind(status.reblog!!, status, binding)
            } else {
                StatusBinder.bind(status, null, binding)
            }
        }
    }

    class StatusBinder{
        companion object {
            fun bind(status: Status, rootStatus: Status?, binding: ListItemStatusBinding){
                generateSpannedContent(status, binding)
                generateSpannedDisplayName(status, rootStatus, binding)
                setColorFilter(status, binding)
                binding.setVariable(BR.status, status)
                binding.setVariable(BR.rootStatus, rootStatus)
                if(status.mediaAttachments.isNotEmpty()){
                    binding.preview.setMedia(status.mediaAttachments, status.isSensitive)
                }
            }

            private fun generateSpannedContent(status: Status, binding: ListItemStatusBinding){
                if (status.spannedContent == null) {
                    status.spannedContent = TextUtil.emojify(binding.content, status)
                }
            }

            private fun generateSpannedDisplayName(status: Status, rootStatus: Status?, binding: ListItemStatusBinding){
                val account = status.account
                if (account.spannedDisplayNameWithAcct == null) {
                    val sb = SpannableStringBuilder()
                    sb.append(TextUtil.emojify(binding.displayName, account.displayName, account.emojis))
                    account.displayNameWithEmoji = SpannableStringBuilder(sb)

                    val start = sb.length
                    sb.append(" @${account.unicodeAcct}")
                    val acctAppearance = TextAppearanceSpan(binding.displayName.context, R.style.TextAppearance_AppCompat_Caption)
                    sb.setSpan(acctAppearance, start, sb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    account.spannedDisplayNameWithAcct = sb
                }
                if(rootStatus != null) {
                    val rootAccount = rootStatus.account
                    val sb = SpannableStringBuilder()
                    val actionedText = binding.actionedText.context.getString(R.string.boosted_by, rootAccount.displayName)
                    sb.append(TextUtil.emojify(binding.actionedText, actionedText, rootAccount.emojis))
                    binding.setVariable(BR.boostedBy, sb)
                }
            }

            private fun setColorFilter(status: Status, binding: ListItemStatusBinding){
                if (status.isReblogged) {
                    binding.boost.setColorFilter(ContextCompat.getColor(binding.boost.context, R.color.boosted))
                } else {
                    binding.boost.clearColorFilter()
                }

                if (status.isFavourited) {
                    binding.favorite.setColorFilter(ContextCompat.getColor(binding.favorite.context, R.color.favourited))
                } else {
                    binding.favorite.clearColorFilter()
                }
            }
        }
    }

    companion object {
        @BindingAdapter("imageUrl", "radius", requireAll = false)
        @JvmStatic
        fun loadImage(imageView: ImageView, url: String?, radius: Int?) {
            if(url == null){
                GlideApp.with(imageView.context).clear(imageView)
                return
            }
            if (radius != null) {
                GlideApp.with(imageView.context).load(url).apply(RequestOptions().transform(RoundedCorners(radius)))
                    .into(imageView)
            } else {
                GlideApp.with(imageView.context).load(url).into(imageView)
            }
        }
    }
}