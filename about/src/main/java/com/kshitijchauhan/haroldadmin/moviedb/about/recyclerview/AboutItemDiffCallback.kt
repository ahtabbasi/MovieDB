package com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview

import androidx.recyclerview.widget.DiffUtil.ItemCallback

internal object AboutItemDiffCallback : ItemCallback<AboutItem>() {

    override fun areItemsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem == newItem
    }
}