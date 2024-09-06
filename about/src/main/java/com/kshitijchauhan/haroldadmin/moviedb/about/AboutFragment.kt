package com.kshitijchauhan.haroldadmin.moviedb.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.FragmentAboutBinding
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class AboutFragment : Fragment() {

    private val epoxyHandler by inject<Handler>(named("epoxy-handler"))
    private val state = AboutState()
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.rvAbout.apply {
            setController(epoxyController)
            epoxyController.setData(state)
        }
        return view
    }

    private val epoxyController by lazy { AboutEpoxyController(epoxyHandler, callbacks) }

    private val callbacks by lazy {
        object : AboutEpoxyController.AboutEpoxyControllerCallbacks {
            override val onRateClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse("market://details?id=${requireContext().packageName}")
                startActivity(createIntent(uri))
            }
            override val onAuthorClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse(state.authorUrl)
                startActivity(createIntent(uri))
            }

            override val onRepoClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse(state.repositoryUrl)
                startActivity(createIntent(uri))
            }

            override val onTmdbClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse(state.tmdbUrl)
                startActivity(createIntent(uri))
            }
        }
    }

    private fun createIntent(uri: Uri): Intent {
        return Intent(Intent.ACTION_VIEW, uri)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            }
    }
}
