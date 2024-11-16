package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.database.dao.TweetDao
import com.positiveHelicopter.doobyplus.database.dao.TwitchDao
import com.positiveHelicopter.doobyplus.database.dao.YouTubeDao
import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.model.PostMessage
import com.positiveHelicopter.doobyplus.model.PreviewImage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.model.SocialsVideo
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
import com.positiveHelicopter.doobyplus.model.database.YouTubeEntity
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
    private val twitchDao: TwitchDao,
    private val youtubeDao: YouTubeDao
): SocialsRepository {
    override val data: Flow<SocialsData> = combine(
        userPreferenceDataSource.userData,
        getTweets(),
        getTwitchVODs(),
        getTwitchTopClips(),
        combine(
            getYouTubeVideos(),
            getYouTubeShorts(),
            getYouTubeLiveStreams()
        ) {
            youtubeVideos, youtubeShorts, youtubeLive ->
            youtubeVideos to youtubeShorts to youtubeLive
        },
    ) { userData, tweets, twitchVODs, twitchTopClips, youtubeVideos ->
        SocialsData(
            userPreference = userData,
            previewImage = PreviewImage(
                shouldPreviewImage = false,
                url = ""
            ),
            tweets = tweets.sortedByDescending { it.timestamp },
            twitchVODs = twitchVODs,
            twitchTopClips = twitchTopClips,
            youtubeVideos = youtubeVideos.first.first.sortedByDescending { it.date },
            youtubeShorts = youtubeVideos.first.second.sortedByDescending { it.date },
            youtubeLiveStreams = youtubeVideos.second.sortedByDescending { it.date }
        )
    }

    override suspend fun setIsFirstTimeNotification(
        isFirstTime: Boolean
    ) = withContext(dispatcher) {
        userPreferenceDataSource.updateIsFirstTimeNotification(isFirstTime)
    }

    override suspend fun setBottomNavigationExpandedState(
        isExpanded: Boolean
    ) = withContext(dispatcher) {
        userPreferenceDataSource.updateBottomNavigationExpandedState(isExpanded)
    }

    override fun getTweets(): Flow<List<PostMessage>> =
        tweetDao.getTweets().map { it.map(TweetEntity::asExternalModel) }

    override suspend fun insertTweets(tweets: List<TweetEntity>) {
        tweetDao.insertTweets(tweets)
    }

    override suspend fun updateTweetPreview(
        id: String,
        text: String,
        url: String
    ) = withContext(dispatcher) {
        try {
            val doc = Jsoup.connect(url)
                .userAgent("googlebot")
                .timeout(3000)
                .get()
            val image = doc.select("meta[property=og:image]")
            val description = doc.select("meta[property=og:description]")
            val descContent = if(description.isEmpty())
                text else description[0].attr("content")
            if (image.isEmpty()) {
                tweetDao.updateTweetPreview(
                    id = id,
                    text = descContent,
                    previewLink = "null"
                )
                return@withContext
            }
            val content = image[0].attr("content")
            if (content.contains("profile_images", ignoreCase = true)) {
                tweetDao.updateTweetPreview(
                    id = id,
                    text = descContent,
                    previewLink = "null"
                )
                return@withContext
            }
            tweetDao.updateTweetPreview(
                id = id,
                text = descContent,
                previewLink = content
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getTwitchVODs(): Flow<List<SocialsVideo>> =
        twitchDao.getTwitchVODs().map { it.map(TwitchEntity::asExternalModel) }

    override fun getTwitchTopClips(): Flow<List<SocialsVideo>> =
        twitchDao.getTwitchTopClips().map { it.map(TwitchEntity::asExternalModel) }

    override suspend fun insertTwitchVideos(videos: List<TwitchEntity>) {
        twitchDao.insertVideos(videos)
    }

    override suspend fun deleteOldTwitchVideos(videos: List<TwitchEntity>) {
        twitchDao.deleteOldVideos(videos.map { it.id })
        twitchDao.deleteOldWeeklyVideos(videos.filter { it.type == "weekly" }.map { it.id })
    }

    override fun getYouTubeVideos(): Flow<List<SocialsVideo>> =
        youtubeDao.getYouTubeVideos().map { it.map(YouTubeEntity::asExternalModel) }


    override fun getYouTubeShorts(): Flow<List<SocialsVideo>> =
        youtubeDao.getYouTubeShorts().map { it.map(YouTubeEntity::asExternalModel) }

    override fun getYouTubeLiveStreams(): Flow<List<SocialsVideo>> =
        youtubeDao.getYouTubeLiveStreams().map { it.map(YouTubeEntity::asExternalModel) }

    override suspend fun insertYouTubeVideos(videos: List<YouTubeEntity>) {
        youtubeDao.insertVideos(videos)
    }

    override suspend fun deleteOldYouTubeVideos(videos: List<YouTubeEntity>) {
        youtubeDao.deleteOldVideos(videos.map { it.id })
    }
}