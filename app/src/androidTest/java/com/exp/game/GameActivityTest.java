package com.exp.game;

import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.exp.game.view.SurfaceViewLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/6/5/005.
 */
public class GameActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            GameActivity.class);

    @Before
    public void before() throws Exception {
        assertNotNull("GameActivity is Null", mActivityRule.getActivity());
    }

    @Test
    public void button_left() throws Exception {
        onView(withId(R.id.left)).check(matches(isDisplayed()))
                .check(matches(withText("← "))).perform(click());
    }

    @Test
    public void button_right() throws Exception {
        onView(withId(R.id.right)).check(matches(isDisplayed()))
                .check(matches(withText("→ "))).perform(click());
    }

    /*@Test
    public void after_game_over() throws Exception {
        onView(withId(R.id.game)).check(matches(isDisplayed()));
        GameActivity mGameActivity = (GameActivity)(mActivityRule.getActivity());
        mGameActivity.over();
        EditText namefield = mGameActivity.dialog.findViewById(R.id.namefield);
        namefield.setText("Test");
        mGameActivity.dialog.findViewById(R.id.post_scores).performClick();
    }*/

}