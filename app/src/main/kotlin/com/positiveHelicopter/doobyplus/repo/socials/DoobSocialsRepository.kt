package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.database.dao.TweetDao
import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.model.PostMessage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.asExternalModel
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import javax.inject.Inject
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DoobSocialsRepository @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val userPreferenceDataSource: PreferenceDataSource,
    private val tweetDao: TweetDao
): SocialsRepository {
    override val data: Flow<SocialsData> = combine(
        userPreferenceDataSource.userData,
        getTweets()
    ) { userData, tweets ->
        SocialsData(
            userPreference = userData,
            tweets = tweets
        )
    }

    override suspend fun setIsFirstTimeNotification(
        isFirstTime: Boolean
    ) = withContext(dispatcher) {
        userPreferenceDataSource.updateIsFirstTimeNotification(isFirstTime)
    }

    override fun getTweets(): Flow<List<PostMessage>> =
        tweetDao.getTweets().map { it.map(TweetEntity::asExternalModel) }
}