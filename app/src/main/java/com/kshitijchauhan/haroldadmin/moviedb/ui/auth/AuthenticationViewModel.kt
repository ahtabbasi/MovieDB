package com.kshitijchauhan.haroldadmin.moviedb.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AccountDetailsResponse
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.CreateSessionRequest
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationViewModel(application: Application): AndroidViewModel(application) {

    private val _requestToken = MutableLiveData<String>()
    private val _authSuccess = SingleLiveEvent<Boolean>()
    private val _accountDetails = MutableLiveData<AccountDetailsResponse>()
    private val compositeDisposable = CompositeDisposable()
    private var sessionId by SharedPreferencesDelegate(application, Constants.KEY_SESSION_ID, "")
    private var isAuthenticated by SharedPreferencesDelegate(application, Constants.KEY_IS_AUTHENTICATED, false)

    val requestToken: LiveData<String>
        get() = _requestToken

    val authSuccess: LiveData<Boolean>
        get() = _authSuccess

    val accountDetails: LiveData<AccountDetailsResponse>
        get() = _accountDetails

    @Inject
    lateinit var apiManager: ApiManager

    init {
        getApplication<MovieDBApplication>()
            .appComponent
            .inject(this)
    }

    fun getRequestToken() {
        apiManager
            .getRequestToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { response ->
                if (response.success) {
                    _requestToken.value = response.requestToken
                }
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun createSession(request: CreateSessionRequest) {
        apiManager
            .createSession(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { response ->
                sessionId = response.sessionId
                isAuthenticated = true
                _authSuccess.value = true
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getAccountDetails() {
        apiManager
            .getAccountDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess { response ->
                _accountDetails.postValue(response)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}