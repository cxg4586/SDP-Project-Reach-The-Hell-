package com.exp.game;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Administrator on 2018/6/5/005.
 */
public class HomeActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    @Before
    public void before() throws Exception {
        assertNotNull("HomeActivity is Null", mActivityRule.getActivity());
    }


    @Test
    public void button_start_game() throws Exception {
        onView(withId(R.id.start_game)).check(matches(isDisplayed()))
                .check(matches(withText("Start Game"))).perform(click());
    }

    @Test
    public void button_score_board() throws Exception {
        onView(withId(R.id.score_board)).check(matches(isDisplayed()))
                .check(matches(withText("ScoreBoard"))).perform(click());
    }

    @Test
    public void button_exit() throws Exception {
        onView(withId(R.id.exit)).check(matches(isDisplayed()))
                .check(matches(withText("Exit game"))).perform(click());
    }
}