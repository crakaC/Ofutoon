package com.crakac.ofutoon.api

import android.net.Uri
import android.support.annotation.VisibleForTesting
import com.crakac.ofutoon.BuildConfig
import com.crakac.ofutoon.C
import com.crakac.ofutoon.api.entity.AccessToken
import com.crakac.ofutoon.api.entity.AppCredentials
import com.crakac.ofutoon.api.service.*
import com.crakac.ofutoon.db.AppDatabase
import com.crakac.ofutoon.db.User
import com.crakac.ofutoon.util.PrefsUtil
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Mastodon(
    accountService: AccountService,
    appService: AppService,
    blockService: BlockService,
    customEmojiService: CustomEmojiService,
    domainBlockService: DomainBlockService,
    endorsementService: EndorsementService,
    favouriteService: FavouriteService,
    filterService: FilterService,
    followRequestService: FollowRequestService,
    followSuggestionService: FollowSuggestionService,
    instanceService: InstanceService,
    listService: ListService,
    mediaAttachmentService: MediaAttachmentService,
    muteService: MuteService,
    notificationService: NotificationService,
    reportService: ReportService,
    searchService: SearchService,
    statusService: StatusService,
    timelineService: TimelineService,
    val user: User?
) : AccountService by accountService,
    AppService by appService,
    BlockService by blockService,
    CustomEmojiService by customEmojiService,
    DomainBlockService by domainBlockService,
    EndorsementService by endorsementService,
    FavouriteService by favouriteService,
    FilterService by filterService,
    FollowRequestService by followRequestService,
    FollowSuggestionService by followSuggestionService,
    InstanceService by instanceService,
    ListService by listService,
    MediaAttachmentService by mediaAttachmentService,
    MuteService by muteService,
    NotificationService by notificationService,
    ReportService by reportService,
    SearchService by searchService,
    StatusService by statusService,
    TimelineService by timelineService {
    companion object {
        private const val ENDPOINT_AUTHORIZE: String = "/oauth/authorize"
        private const val OAUTH_SCOPES: String = "read write follow push"
        private const val AUTHORIZATION_CODE: String = "authorization_code"
        private const val CLIENT_ID: String = "client_id"
        private const val CLIENT_SECRET: String = "client_secret"
        private const val CREDENTIAL_ID: String = "credential_id"
        private const val REDIRECT_URI: String = "redirect_uri"
        private const val RESPONSE_TYPE: String = "response_type"
        private const val SCOPE: String = "scope"

        private val dispatcher: Dispatcher = Dispatcher()

        private var instance: Mastodon? = null
        val api: Mastodon get() = instance!!

        fun create(domain: String, accessToken: String): Mastodon =
            Mastodon.Builder(domain, accessToken).build()

        fun initialize(user: User) {
            instance = Mastodon.Builder(user).build()
        }

        private fun createHttpClient(bearerToken: String? = null): OkHttpClient {
            val logger = HttpLoggingInterceptor()
            logger.level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

            val httpClientBuilder = OkHttpClient.Builder().addInterceptor(logger).dispatcher(dispatcher)
            if (bearerToken != null) {
                httpClientBuilder.addInterceptor {
                    val org = it.request()
                    val builder = org.newBuilder()
                    builder.addHeader("Authorization", "Bearer $bearerToken")
                    val newRequest = builder.build()
                    it.proceed(newRequest)
                }
            }
            return httpClientBuilder.build()
        }

        private fun createRetrofit(domain: String, httpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(if (domain == "localhost") "http://localhost" else "https://$domain")
                .client(httpClient)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create()
                    )
                )
                .build()
        }

        fun createAuthenticationUri(domain: String, redirectUri: String): Uri {
            return Uri.Builder()
                .scheme("https")
                .authority(domain)
                .path(ENDPOINT_AUTHORIZE)
                .appendQueryParameter(CLIENT_ID, getClientId(domain))
                .appendQueryParameter(REDIRECT_URI, redirectUri)
                .appendQueryParameter(RESPONSE_TYPE, "code")
                .appendQueryParameter(SCOPE, OAUTH_SCOPES)
                .build()
        }

        fun saveAppCredential(domain: String, credentials: AppCredentials) {
            PrefsUtil.putString("$domain.$CLIENT_ID", credentials.clientId)
            PrefsUtil.putString("$domain.$CLIENT_SECRET", credentials.clientSecret)
            PrefsUtil.putLong("$domain.$CREDENTIAL_ID", credentials.id)
        }

        fun hasAppCredential(domain: String): Boolean {
            val clientId = getClientId(domain)
            val clientSecret = PrefsUtil.getString("$domain.$CLIENT_SECRET")
            return clientId != null && clientSecret != null
        }

        fun currentAccount(callback: (account: User?) -> Unit) {
            AppDatabase.execute {
                val user = AppDatabase.instance.userDao().getUser(PrefsUtil.getInt(C.CURRENT_USER_ID, 0))
                AppDatabase.uiThread {
                    callback(user)
                }
            }
        }

        private fun getClientId(domain: String): String? {
            return PrefsUtil.getString("$domain.$CLIENT_ID")
        }

        private fun getClientSecret(domain: String): String? {
            return PrefsUtil.getString("$domain.$CLIENT_SECRET")
        }

        fun registerApplication(
            domain: String,
            clientName: String,
            redirectUris: String,
            website: String
        ): Call<AppCredentials> {
            return RegisterService.create(domain).registerApplication(
                clientName,
                redirectUris,
                OAUTH_SCOPES,
                website
            )
        }

        fun fetchAccessToken(domain: String, redirectUri: String, code: String): Call<AccessToken> {
            val id = getClientId(domain)!!
            val secret = getClientSecret(domain)!!
            return RegisterService.create(domain).fetchAccessToken(id, secret, redirectUri, code, AUTHORIZATION_CODE)
        }
    }

    class Builder() {
        private var user: User? = null
        private var host: String = ""
        private var token: String? = null

        constructor(user: User) : this() {
            this.user = user
            host = user.domain
            token = user.token
        }

        constructor(host: String, token: String? = null) : this() {
            this.host = host
            this.token = token
        }

        fun build(): Mastodon {
            val httpClient = createHttpClient(token)
            val retrofit = createRetrofit(host, httpClient)
            return Mastodon(
                retrofit.create(AccountService::class.java),
                retrofit.create(AppService::class.java),
                retrofit.create(BlockService::class.java),
                retrofit.create(CustomEmojiService::class.java),
                retrofit.create(DomainBlockService::class.java),
                retrofit.create(EndorsementService::class.java),
                retrofit.create(FavouriteService::class.java),
                retrofit.create(FilterService::class.java),
                retrofit.create(FollowRequestService::class.java),
                retrofit.create(FollowSuggestionService::class.java),
                retrofit.create(InstanceService::class.java),
                retrofit.create(ListService::class.java),
                retrofit.create(MediaAttachmentService::class.java),
                retrofit.create(MuteService::class.java),
                retrofit.create(NotificationService::class.java),
                retrofit.create(ReportService::class.java),
                retrofit.create(SearchService::class.java),
                retrofit.create(StatusService::class.java),
                retrofit.create(TimelineService::class.java),
                user
            )
        }
    }

    @VisibleForTesting
    class RegisterService(
        appService: AppService,
        oauthService: OauthService
    ) : AppService by appService,
        OauthService by oauthService {
        companion object {
            fun create(domain: String): RegisterService {
                val retrofit = createRetrofit(domain, createHttpClient())
                return RegisterService(
                    retrofit.create(AppService::class.java),
                    retrofit.create(OauthService::class.java)
                )
            }
        }
    }
}