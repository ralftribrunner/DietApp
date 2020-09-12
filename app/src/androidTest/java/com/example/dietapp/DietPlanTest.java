package com.example.dietapp;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.dietapp.Diet.DietPlan;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DietPlanTest {

    @Rule
    public ActivityTestRule<DietPlan> activityRule =
            new ActivityTestRule<>(DietPlan.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.dietapp", appContext.getPackageName());
    }
    @Test //Button visibility test
    public void Buttontest() {
        onView(withId(R.id.dietplan)).check(matches(isClickable()));
        onView(withId(R.id.dietname)).check(matches(isDisplayed()));

    }
}
