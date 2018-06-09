package com.exp.game;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.text.TextUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
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
public class ScoreActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            ScoreActivity.class);

    @Before
    public void before() throws Exception {
        assertNotNull("ScoreActivity is Null", mActivityRule.getActivity());
    }

    @Test
    public void views() throws Exception {
        onView(withText("Name")).check(matches(isDisplayed()));
        onView(withText("Score")).check(matches(isDisplayed()));
        onView(withText("Time")).check(matches(isDisplayed()));
    }

    @Test
    public void listview() throws Exception {
        onData(allOf(is(instanceOf(DBScore.class)), searchItemWithName("Test")))
                .inAdapterView(withId(R.id.list));
    }

    /**
     * 查找指定关键字的搜索条件
     * @param name 需要搜索的关键字
     */
    public static Matcher<Object> searchItemWithName(final String name) {
        return new BoundedMatcher<Object, DBScore>(DBScore.class) {
            @Override
            protected boolean matchesSafely(DBScore item) {
                return item != null
                        && !TextUtils.isEmpty(item.getName())
                        && item.getName().equals(name);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("DBScore has Name: " + name);
            }
        };
    }

}