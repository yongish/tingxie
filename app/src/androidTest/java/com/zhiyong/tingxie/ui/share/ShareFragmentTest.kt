package com.zhiyong.tingxie.ui.share

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.zhiyong.tingxie.FakeAndroidTestRepository
import com.zhiyong.tingxie.Repository
import com.zhiyong.tingxie.ServiceLocator
import com.zhiyong.tingxie.network.NetworkAddQuizUser
import com.zhiyong.tingxie.network.NetworkGroupMember
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class ShareFragmentTest {
  private lateinit var repository: Repository

  @Before
  fun initRepository() {
    repository = FakeAndroidTestRepository()
    ServiceLocator.quizRepository = repository
  }

  @After
  fun cleanupDb() {
    ServiceLocator.resetRepository()
  }

  @Test
  fun shareDetails_DisplayedInUi() = runBlocking {
    // GIVEN - Add new share to the DB
    repository.addQuizMemberOrReturnNoUser(0L, NetworkAddQuizUser("creatorName0", "creatorEmail0", "email0", EnumQuizRole.MEMBER.name))

    // WHEN - ShareFragment launched to display share
    // https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-test-doubles#8
    // tutorial uses Jetpack navigation, which this app does not.
  }
}
