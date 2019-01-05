package com.crakac.ofutoon.ui.vh

import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.Notification
import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.databinding.ListItemStatusBinding
import com.crakac.ofutoon.ui.activity.AttachmentsPreviewActivity
import com.crakac.ofutoon.ui.adapter.RefreshableAdapter
import com.crakac.ofutoon.ui.widget.ContentMovementMethod
import com.crakac.ofutoon.ui.widget.InlineImagePreview
import com.crakac.ofutoon.util.TextUtil

class StatusViewHolder(private val binding: ListItemStatusBinding) :
    RecyclerView.ViewHolder(binding.root), RefreshableAdapter.Refreshable {
    val context: Context get() = itemView.context

    init {
        binding.content.movementMethod = ContentMovementMethod.instance
    }

    fun setup(status: Status) {
        if (status.reblog != null) {
            bind(status.reblog!!, status.account)
        } else {
            bind(status)
        }
    }

    fun setup(notification: Notification) {
        val type = notification.getType()
        if (type == Notification.Type.Mention) {
            bind(notification.status!!)
            clearAlpha()
        } else {
            bind(notification.status!!, notification.account)
            setAlpha()
            if(type == Notification.Type.Favourite){
                setFavoritedText(notification.account)
            } else if(type == Notification.Type.ReBlog) {
                setRebloggedText(notification.account)
            }
        }
    }

    private fun bind(status: Status, rootAccount: Account? = null) {
        generateSpannedContent(status)
        generateSpannedDisplayName(status, rootAccount)
        setColorFilter(status)
        binding.setVariable(BR.status, status)
        binding.setVariable(BR.rootAccount, rootAccount)
        if (status.mediaAttachments.isNotEmpty()) {
            binding.preview.setMedia(status.mediaAttachments, status.isSensitive)
            binding.preview.setOnPreviewClickListener(object : InlineImagePreview.OnClickPreviewListener {
                override fun onClick(attachmentIndex: Int) {
                    val intent = Intent(context, AttachmentsPreviewActivity::class.java)
                    AttachmentsPreviewActivity.setup(intent, status, attachmentIndex)
                    context.startActivity(intent)
                }
            })
        }
    }

    private fun generateSpannedContent(status: Status) {
        if (status.spannedContent == null) {
            status.spannedContent = TextUtil.emojify(binding.content, status)
        }
    }

    private fun generateSpannedDisplayName(status: Status, rootAccount: Account?) {
        val account = status.account
        if (account.spannedDisplayName == null) {
            val sb = SpannableStringBuilder()
            sb.append(TextUtil.emojify(binding.displayName, account.displayName, account.emojis))
            val start = sb.length
            sb.append(" @${account.unicodeAcct}")
            val acctAppearance =
                TextAppearanceSpan(context, R.style.TextAppearance_AppCompat_Caption)
            sb.setSpan(acctAppearance, start, sb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            account.spannedDisplayName = sb
        }
        if (rootAccount != null) {
            val sb = SpannableStringBuilder()
            val actionedText =
                context.getString(R.string.boosted_by, rootAccount.displayName)
            sb.append(TextUtil.emojify(binding.actionedText, actionedText, rootAccount.emojis))
            binding.actionedText.text = sb
        }
    }

    private fun setColorFilter(status: Status) {
        if (status.isReblogged) {
            binding.boost.setColorFilter(context.getColor(R.color.boosted))
        } else {
            binding.boost.clearColorFilter()
        }

        if (status.isFavourited) {
            binding.favorite.setColorFilter(context.getColor(R.color.favourited))
        } else {
            binding.favorite.clearColorFilter()
        }
    }

    private fun setAlpha() {
        binding.originalUserIcon.setColorFilter(context.getColor(R.color.notification_icon_mask))
        binding.actionedByIcon.setColorFilter(context.getColor(R.color.notification_icon_mask))
        for (v in arrayOf(
            binding.content,
            binding.statusActions,
            binding.createdAt,
            binding.spoilerText,
            binding.readMore,
            binding.displayName
        )) {
            v.alpha = 0.625f
        }
    }

    private fun clearAlpha() {
        binding.originalUserIcon.clearColorFilter()
        binding.actionedByIcon.clearColorFilter()
        for (v in arrayOf(
            binding.content,
            binding.statusActions,
            binding.createdAt,
            binding.spoilerText,
            binding.readMore,
            binding.displayName
        )) {
            v.alpha = 1f
        }
    }

    private fun setRebloggedText(account: Account) {
        val sb = SpannableStringBuilder()
        val actionedText =
            context.getString(R.string.boosted_by, account.displayName)
        sb.append(TextUtil.emojify(binding.actionedText, actionedText, account.emojis))
        binding.actionedText.text = sb
        binding.actionedIcon.setImageResource(R.drawable.ic_boost)
        binding.actionedIcon.setColorFilter(context.getColor(R.color.boosted))
    }

    private fun setFavoritedText(account: Account) {
        val sb = SpannableStringBuilder()
        val actionedText =
            context.getString(R.string.favourited_by, account.displayName)
        sb.append(TextUtil.emojify(binding.actionedText, actionedText, account.emojis))
        binding.actionedText.text = sb
        binding.actionedIcon.setImageResource(R.drawable.ic_star)
        binding.actionedIcon.setColorFilter(context.getColor(R.color.favourited))
    }


    override fun refresh() {
        binding.createdAt.text = binding.status?.getRelativeTime()
    }
}