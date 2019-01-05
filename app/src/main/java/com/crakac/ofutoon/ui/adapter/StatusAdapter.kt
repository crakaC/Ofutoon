package com.crakac.ofutoon.ui.adapter

import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.databinding.ListItemStatusBinding
import com.crakac.ofutoon.ui.activity.AttachmentsPreviewActivity
import com.crakac.ofutoon.ui.widget.ContentMovementMethod
import com.crakac.ofutoon.ui.widget.InlineImagePreview
import com.crakac.ofutoon.util.GlideApp
import com.crakac.ofutoon.util.TextUtil

class StatusAdapter : RefreshableAdapter<Status>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StatusViewHolder).setup(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(ListItemStatusBinding.inflate(LayoutInflater.from(parent.context)))
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

    class StatusViewHolder(private val binding: ListItemStatusBinding) :
        RecyclerView.ViewHolder(binding.root), Refreshable {
        init {
            binding.content.movementMethod = ContentMovementMethod.instance
        }

        fun setup(status: Status) {
            if (status.reblog != null) {
                StatusBinder.bind(status.reblog!!, status, binding)
            } else {
                StatusBinder.bind(status, null, binding)
            }
        }

        override fun refresh() {
            binding.createdAt.text = binding.status?.getRelativeTime()
        }
    }

    class StatusBinder {
        companion object {
            fun bind(status: Status, rootStatus: Status?, binding: ListItemStatusBinding) {
                generateSpannedContent(status, binding)
                generateSpannedDisplayName(status, rootStatus, binding)
                setColorFilter(status, binding)
                binding.setVariable(BR.status, status)
                binding.setVariable(BR.rootStatus, rootStatus)
                if (status.mediaAttachments.isNotEmpty()) {
                    binding.preview.setMedia(status.mediaAttachments, status.isSensitive)
                    binding.preview.setOnPreviewClickListener(object : InlineImagePreview.OnClickPreviewListener {
                        override fun onClick(attachmentIndex: Int) {
                            val intent = Intent(binding.preview.context, AttachmentsPreviewActivity::class.java)
                            AttachmentsPreviewActivity.setup(intent, status, attachmentIndex)
                            binding.preview.context.startActivity(intent)
                        }
                    })
                }
            }

            private fun generateSpannedContent(status: Status, binding: ListItemStatusBinding) {
                if (status.spannedContent == null) {
                    status.spannedContent = TextUtil.emojify(binding.content, status)
                }
            }

            private fun generateSpannedDisplayName(
                status: Status,
                rootStatus: Status?,
                binding: ListItemStatusBinding
            ) {
                val account = status.account
                if (account.spannedDisplayName == null) {
                    val sb = SpannableStringBuilder()
                    sb.append(TextUtil.emojify(binding.displayName, account.displayName, account.emojis))
                    val start = sb.length
                    sb.append(" @${account.unicodeAcct}")
                    val acctAppearance =
                        TextAppearanceSpan(binding.displayName.context, R.style.TextAppearance_AppCompat_Caption)
                    sb.setSpan(acctAppearance, start, sb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    account.spannedDisplayName = sb
                }
                if (rootStatus != null) {
                    val rootAccount = rootStatus.account
                    val sb = SpannableStringBuilder()
                    val actionedText =
                        binding.actionedText.context.getString(R.string.boosted_by, rootAccount.displayName)
                    sb.append(TextUtil.emojify(binding.actionedText, actionedText, rootAccount.emojis))
                    binding.setVariable(BR.boostedBy, sb)
                }
            }

            private fun setColorFilter(status: Status, binding: ListItemStatusBinding) {
                if (status.isReblogged) {
                    binding.boost.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.boosted))
                } else {
                    binding.boost.clearColorFilter()
                }

                if (status.isFavourited) {
                    binding.favorite.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.favourited))
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
            if (url == null) {
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