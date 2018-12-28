package com.crakac.ofutoon.api

import com.crakac.ofutoon.BuildConfig
import com.crakac.ofutoon.api.service.*
import com.crakac.ofutoon.db.UserAccount
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    mastodonService: MastodonService,
    mediaAttachmentService: MediaAttachmentService,
    muteService: MuteService,
    notificationService: NotificationService,
    reportService: ReportService,
    searchService: SearchService,
    statusService: StatusService,
    timelineService: TimelineService,
    val userAccount: UserAccount?
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
    MastodonService by mastodonService,
    MediaAttachmentService by mediaAttachmentService,
    MuteService by muteService,
    NotificationService by notificationService,
    ReportService by reportService,
    SearchService by searchService,
    StatusService by statusService,
    TimelineService by timelineService {
    companion object {
        val TAG: String = "Mastodon"
        lateinit var api: Mastodon
            private set

        fun initialize(domain: String, accessToken: String? = null): Mastodon =
            Mastodon.Builder().setHost(domain).setAccessToken(accessToken).build()

        fun initialize(user: UserAccount) {
            api = Mastodon.Builder(user).build()
        }
    }

    class Builder() {
        companion object {
            val dispatcher: Dispatcher = Dispatcher()
        }

        private var userAccount: UserAccount? = null
        private var host: String = ""
        private var token: String? = null

        constructor(user: UserAccount) : this() {
            userAccount = user
            host = user.domain
        }

        fun setHost(host: String): Builder {
            this.host = host
            return this
        }

        fun setAccessToken(accessToken: String?): Builder {
            this.token = accessToken
            return this
        }

        fun build(): Mastodon {
            val okHttpClient = createMastodonHttpClient(token ?: userAccount?.token)
            val retrofit = Retrofit.Builder()
                .baseUrl(if (host == "localhost") "http://localhost" else "https://$host")
                .client(okHttpClient)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create()
                    )
                )
                .build()
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
                retrofit.create(MastodonService::class.java),
                retrofit.create(MediaAttachmentService::class.java),
                retrofit.create(MuteService::class.java),
                retrofit.create(NotificationService::class.java),
                retrofit.create(ReportService::class.java),
                retrofit.create(SearchService::class.java),
                retrofit.create(StatusService::class.java),
                retrofit.create(TimelineService::class.java),
                userAccount
            )
        }

        private fun createMastodonHttpClient(bearerToken: String?): OkHttpClient {
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
    }
}