package com.kshitijchauhan.haroldadmin.moviedb.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.FragmentAboutBinding
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutAdapter
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutAdapterCallback

internal class AboutFragment : Fragment(), AboutAdapterCallback {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.rvAbout.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAbout.adapter = AboutAdapter(this)

        return binding.root
    }

    private fun createIntent(uri: Uri) = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    }

    override fun onRateMeClicked() {
        val uri = Uri.parse("market://details?id=${requireContext().packageName}")
        startActivity(createIntent(uri))
    }

    override fun onAuthorClicked() {
        val uri = Uri.parse(AboutData.AUTHOR_URL)
        startActivity(createIntent(uri))
    }

    override fun onRepoClicked() {
        val uri = Uri.parse(AboutData.REPOSITORY_URL)
        startActivity(createIntent(uri))
    }

    override fun onTmdbClicked() {
        val uri = Uri.parse(AboutData.TMDB_URL)
        startActivity(createIntent(uri))
    }
}
