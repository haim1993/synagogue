package com.example.shlez.synagogue;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;



/**
 * Created by Computer-PC on 1/15/2018.
 */
@RunWith(AndroidJUnit4.class)
public class EspressoTest
{
    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<CreateEmail> mActivityRule = new ActivityTestRule<>(CreateEmail.class);

    @Before
    public void initValidString()
    {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

    @Test
    public void changeText_sameActivity()
    {
        // Type text and then press the button.
        onView(withId(R.id.txt_create_email)).perform(typeText(mStringToBetyped), closeSoftKeyboard());

        onView(withId(R.id.btn_create_email)).perform(click());

        // Check that the text was changed.

        onView(withText("Email is not valid")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }
}