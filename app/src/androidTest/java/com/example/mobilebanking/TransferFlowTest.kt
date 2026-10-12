package com.example.mobilebanking

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TransferFlowTest {

    // DashboardFragment asks for POST_NOTIFICATIONS on API 33+. Granting it up front
    // stops the system dialog from taking focus away from the activity under test.
    @get:Rule(order = 0)
    val permissionRule: GrantPermissionRule =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            GrantPermissionRule.grant()
        }

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun validTransferShowsConfirmationScreen() {
        // Dashboard is drawn by Compose now, so its button has no view id for Espresso to match.
        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.action_transfer))
            .performClick()

        // TransferFragment pre-fills account and name from the last confirmed transfer,
        // so clear them first or typeText() appends to the saved values.
        onView(withId(R.id.etRecipientAccount))
            .perform(clearText(), typeText("8001234567"), closeSoftKeyboard())
        onView(withId(R.id.etRecipientName))
            .perform(clearText(), typeText("Kasun Silva"), closeSoftKeyboard())
        onView(withId(R.id.etAmount))
            .perform(typeText("2500"), closeSoftKeyboard())

        onView(withId(R.id.btnSubmit)).perform(click())

        onView(withId(R.id.tvConfirmRecipient)).check(matches(isDisplayed()))
        onView(withId(R.id.tvConfirmAmount)).check(matches(isDisplayed()))
    }
}
