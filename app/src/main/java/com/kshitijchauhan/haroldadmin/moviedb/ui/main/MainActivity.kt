package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.kshitijchauhan.haroldadmin.moviedb.BuildConfig
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BackPressListener
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.getMainHandler
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.postDelayed
import com.kshitijchauhan.haroldadmin.moviedb.databinding.ActivityMainBinding
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private var backPressListener: BackPressListener? = null
    private lateinit var navController: NavController
    private var enableCrashlytics: Boolean = true
    private var enableAnalytics: Boolean = true
    private lateinit var binding: ActivityMainBinding

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        when (key) {
            com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_CRASHLYTICS -> {
                toggleCrashlytics(prefs)
                Toast.makeText(this, "Restarting MovieDB", Toast.LENGTH_LONG).show()
                getMainHandler().postDelayed(1000) {
                    recreate()
                }
            }
            com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_ANALYTICS -> {
                toggleAnalytics(prefs)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            binding = this
            setContentView(root)
        }

        toggleAnalytics(get<SharedPreferences>())
        toggleCrashlytics(get<SharedPreferences>())

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(prefsListener)

        navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController

        with(mainViewModel) {
            snackbar.observe(this@MainActivity, Observer { snackbarAction ->
                val snackbar = Snackbar.make(binding.homeRootView, snackbarAction.message, snackbarAction.length)
                if (snackbarAction.actionText != null && snackbarAction.action != null) {
                    snackbar.setAction(snackbarAction.actionText, snackbarAction.action)
                }
                snackbar.show()
            })
            toolbarTitle.observe(this@MainActivity, Observer { newTitle ->
                supportActionBar?.apply {
                    title = newTitle
                }
            })
            backPressListener.observe(this@MainActivity, Observer { listener ->
                this@MainActivity.backPressListener = listener
            })
        }

        binding.mainNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.aboutFragment -> {
                    binding.mainNavView.animateHideDown()
                    window.navigationBarColor = ContextCompat.getColor(this, R.color.colorSurfaceDark)
                }
                R.id.movieDetailsFragment, R.id.actorDetailsFragment -> {
                    binding.mainNavView.animateHideDown()
                    binding.mainToolbar.animatedHideUp()
                    window.navigationBarColor = ContextCompat.getColor(this, R.color.colorSurfaceDark)
                }
                else -> {
                    binding.mainNavView.animateShowUp()
                    binding.mainToolbar.animateShowDown()
                    window.navigationBarColor = ContextCompat.getColor(this, R.color.colorSurface)
                }
            }
        }

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }

    }

    override fun onBackPressed() {
        if (backPressListener == null || backPressListener?.onBackPressed() == true) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuPrivacyPolicy -> {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(com.kshitijchauhan.haroldadmin.moviedb.core.Constants.PRIVACY_POLICY_URL)
                }.also {
                    startActivity(it)
                }
                true
            }
            R.id.menuTandC -> {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(com.kshitijchauhan.haroldadmin.moviedb.core.Constants.TERMS_AND_CONDITIONS_URL)
                }.also {
                    startActivity(it)
                }
                true
            }
            else -> {
                item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
            }
        }
    }

    private fun toggleCrashlytics(prefs: SharedPreferences) {
        enableCrashlytics = prefs.getBoolean(com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_CRASHLYTICS, true)
        if (enableCrashlytics && !BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }

    private fun toggleAnalytics(prefs: SharedPreferences) {
        enableAnalytics = prefs.getBoolean(com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_ANALYTICS, true)
        if (enableAnalytics && !BuildConfig.DEBUG) {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(prefsListener)
    }

    private fun View.animateHideDown() {
        if (this.visibility == View.VISIBLE) {
            this.animate()
                .translationY(-1f)
                .alpha(0f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction { this.visibility = View.GONE }
        }
    }

    private fun View.animatedHideUp() {
        if (this.visibility == View.VISIBLE) {
            this.animate()
                .translationY(2f)
                .alpha(0f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction { this.visibility = View.GONE }
        }
    }

    private fun View.animateShowUp() {
        if (this.visibility != View.VISIBLE) {
            this.animate()
                .translationY(1f)
                .alpha(1f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withStartAction { this.visibility = View.VISIBLE }
        }
    }

    private fun View.animateShowDown() {
        if (this.visibility != View.VISIBLE) {
            this.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withStartAction { this.visibility = View.VISIBLE }
        }
    }
}
