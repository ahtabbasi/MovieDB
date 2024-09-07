package com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kshitijchauhan.haroldadmin.moviedb.about.AboutData
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.ItemAppBinding
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.ItemAuthorBinding
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.ItemTmdbBinding

internal abstract class AboutItemViewHolder(view: View) : ViewHolder(view) {

    abstract fun bind(item: AboutItem)

    internal class AboutAppViewHolder(
        private val binding: ItemAppBinding,
        private val callback: AboutAdapterCallback,
    ) : AboutItemViewHolder(binding.root) {

        override fun bind(item: AboutItem) {
            val versionText = "Version ${AboutData.VERSION}"
            binding.appVersion.text = versionText
            binding.appRate.setOnClickListener { callback.onRateMeClicked() }
        }
    }

    class AboutAuthorViewHolder(
        private val binding: ItemAuthorBinding,
        private val callback: AboutAdapterCallback,
    ) : AboutItemViewHolder(binding.root) {

        override fun bind(item: AboutItem) {
            binding.author.text = AboutData.AUTHOR_NAME
            binding.author.setOnClickListener { callback.onAuthorClicked() }
            binding.repository.setOnClickListener { callback.onRepoClicked() }
        }
    }

    internal class AboutTmdbViewHolder(
        private val binding: ItemTmdbBinding,
        private val callback: AboutAdapterCallback,
    ) : AboutItemViewHolder(binding.root) {

        override fun bind(item: AboutItem) {
            binding.root.setOnClickListener { callback.onTmdbClicked() }
        }
    }
}