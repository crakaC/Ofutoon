package com.crakac.ofutoon.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.crakac.ofutoon.C
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.api.entity.AccessToken
import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.AppCredentials
import com.crakac.ofutoon.api.entity.MastodonCallback
import com.crakac.ofutoon.db.AppDatabase
import com.crakac.ofutoon.db.User
import com.crakac.ofutoon.util.PrefsUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call

class LoginActivity : AppCompatActivity() {
    companion object {
        const val INSTANCE_DOMAIN = "instance_domain"
        val TAG = "LoginActivity"
        val ACTION_ADD_ACCOUNT = "add_account"
        val REQUEST_AUTHORIZE = 1001 // 特に意味はない
    }

    private val instanceDomain
        get() = domainEditText.text.toString()
    private val oauthRedirectUri
        get() = "${getString(R.string.oauth_scheme)}://${getString(R.string.oauth_redirect_host)}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        savedInstanceState?.let {
            domainEditText.setText(it.getString(INSTANCE_DOMAIN))
        }
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        domainEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                inputManager.hideSoftInputFromWindow(
                    domainEditText.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        domainEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                loginButton.isEnabled = s.count() > 0
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })
        loginButton.setOnClickListener {
            if (Mastodon.hasAppCredential(instanceDomain)) {
                startAuthorize(instanceDomain)
            } else {
                registerApplication()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(INSTANCE_DOMAIN, instanceDomain)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setLoading(false)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setLoading(false)
        val data = intent?.data ?: return

        if (data.scheme != getString(R.string.oauth_scheme)) {
            return
        }
        val code = data.getQueryParameter("code")
        val error = data.getQueryParameter("error")

        if (error != null) {
            //TODO do something
            return
        }

        if (code != null) {
            val domain = PrefsUtil.getString(C.OAUTH_TARGET_DOMAIN)
            if (domain == null) {
                Log.e(TAG, "target domain is null")
                //TODO Show dialog
                return
            }
            setLoading(true)
            Mastodon.fetchAccessToken(domain, oauthRedirectUri, code).enqueue(object : MastodonCallback<AccessToken> {
                override fun onSuccess(result: AccessToken) {
                    onFetchAccessTokenSuccess(instanceDomain, result.accessToken)
                    PrefsUtil.remove(C.OAUTH_TARGET_DOMAIN)
                }
            })
        }
    }

    private fun onFetchAccessTokenSuccess(domain: String, accessToken: String) {
        //verify credentials
        Mastodon.create(domain, accessToken).getCurrentAccount().enqueue(object : MastodonCallback<Account> {
            override fun onSuccess(result: Account) {
                val user = User(
                    name = result.username,
                    domain = domain,
                    token = accessToken,
                    accountJson = Gson().toJson(result)
                )
                AppDatabase.execute {
                    val oldUser = AppDatabase.instance.userDao().select(user.name, user.domain)
                    if (oldUser != null) {
                        user.id = oldUser.id
                        AppDatabase.instance.userDao().update(user) //update token etc.
                    } else {
                        AppDatabase.instance.userDao().insert(user)
                    }
                    val newUser = AppDatabase.instance.userDao().select(user.name, user.domain)!!
                    PrefsUtil.putInt(C.CURRENT_USER_ID, newUser.id)
                    AppDatabase.uiThread {
                        Mastodon.initialize(newUser)
                        startHomeActivity()
                    }
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                setLoading(false)
                Snackbar.make(domainEditText, "Something wrong", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun registerApplication() {
        setLoading(true)
        Mastodon.registerApplication(
            instanceDomain,
            getString(R.string.app_name),
            oauthRedirectUri,
            getString(R.string.website)
        ).enqueue(object : MastodonCallback<AppCredentials> {
            override fun onSuccess(result: AppCredentials) {
                Mastodon.saveAppCredential(instanceDomain, result)
                startAuthorize(instanceDomain)
            }

            override fun onFailure(call: Call<AppCredentials>, t: Throwable) {
                Snackbar.make(domainEditText, "Something wrong", Snackbar.LENGTH_SHORT).show()
                setLoading(false)
            }
        })
    }

    private fun startAuthorize(domain: String) {
        // Save target domain since keeping
        PrefsUtil.putString(C.OAUTH_TARGET_DOMAIN, domain)
        val uri = Mastodon.createAuthenticationUri(domain, oauthRedirectUri)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivityForResult(intent, REQUEST_AUTHORIZE)
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.action = HomeActivity.ACTION_RELOAD
        startActivity(intent)
        finish()
    }

    private fun setLoading(isEnable: Boolean) {
        if (isEnable) {
            loginButton.visibility = View.GONE
            progress.visibility = View.VISIBLE
            domainEditText.isEnabled = false
        } else {
            loginButton.visibility = View.VISIBLE
            progress.visibility = View.GONE
            domainEditText.isEnabled = true
        }
    }
}
