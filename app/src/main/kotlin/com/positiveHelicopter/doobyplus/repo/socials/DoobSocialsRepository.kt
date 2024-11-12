package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.database.dao.TweetDao
import com.positiveHelicopter.doobyplus.database.dao.TwitchDao
import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.model.PostMessage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.model.TwitchVideo
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
import com.positiveHelicopter.doobyplus.model.database.asExternalModel
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import javax.inject.Inject
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class DoobSocialsRepository @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val userPreferenceDataSource: PreferenceDataSource,
    private val tweetDao: TweetDao,
    private val twitchDao: TwitchDao
): SocialsRepository {
    override val data: Flow<SocialsData> = combine(
        userPreferenceDataSource.userData,
        getTweets(),
        getTwitchVODs(),
        getTwitchTopClips(),
    ) { userData, tweets,
        twitchVODs, twitchTopClips ->
        SocialsData(
            userPreference = userData,
            tweets = tweets.sortedByDescending { it.timestamp },
            twitchVODs = twitchVODs,
            twitchTopClips = twitchTopClips
        )
    }

    override suspend fun setIsFirstTimeNotification(
        isFirstTime: Boolean
    ) = withContext(dispatcher) {
        userPreferenceDataSource.updateIsFirstTimeNotification(isFirstTime)
    }

    override fun getTweets(): Flow<List<PostMessage>> =
        tweetDao.getTweets().map { it.map(TweetEntity::asExternalModel) }

    override suspend fun insertTweets(tweets: List<TweetEntity>) {
        tweetDao.insertTweets(tweets)
    }

    override suspend fun updateTweetPreview(
        id: String,
        url: String
    ) = withContext(dispatcher) {
        try {
            val doc = Jsoup.connect(url)
                .userAgent("googlebot")
                .timeout(3000)
                .get()
            val image = doc.select("meta[property=og:image]")
            if (image.isEmpty()) {
                tweetDao.updateTweetPreview(id, "null")
                return@withContext
            }
            val content = image[0].attr("content")
            if (content.contains("profile_images", ignoreCase = true)) {
                tweetDao.updateTweetPreview(id, "null")
                return@withContext
            }
            tweetDao.updateTweetPreview(id, content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getTwitchVODs(): Flow<List<TwitchVideo>> =
        twitchDao.getTwitchVODs().map { it.map(TwitchEntity::asExternalModel) }

    override fun getTwitchTopClips(): Flow<List<TwitchVideo>> =
        twitchDao.getTwitchTopClips().map { it.map(TwitchEntity::asExternalModel) }

    override suspend fun insertTwitchVideos(videos: List<TwitchEntity>) {
        twitchDao.insertVideos(videos)
    }

    override suspend fun deleteOldTwitchVideos(videos: List<TwitchEntity>) {
        twitchDao.deleteOldVideos(videos.map { it.id })
        twitchDao.deleteOldWeeklyVideos(videos.filter { it.type == "weekly" }.map { it.id })
    }
}