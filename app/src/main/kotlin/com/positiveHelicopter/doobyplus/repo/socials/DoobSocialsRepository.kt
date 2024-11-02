package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.model.PostMessage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import javax.inject.Inject
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DoobSocialsRepository @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val userPreferenceDataSource: PreferenceDataSource
): SocialsRepository {
    override val data: Flow<SocialsData> = userPreferenceDataSource.userData.map {
        SocialsData(
            userPreference = it,
            tweets = listOf(
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                ),
                PostMessage(
                    text = "This is a post This is a post This is a post This is a post This is a post This is a post",
                    url = "https://t.co/RzMUobvVW7"
                )
            )
        )
    }

    override suspend fun setIsFirstTimeNotification(
        isFirstTime: Boolean
    ) = withContext(dispatcher) {
        userPreferenceDataSource.updateIsFirstTimeNotification(isFirstTime)
    }
}