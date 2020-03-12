package com.example.preprofexama;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    private static int grade = 0;
    private static int total = 0;
    private static int passTests = 0;

    private Random random = new Random();

    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void interfaceTest() {
        //Check view existing
        total++;
        onView(withId(R.id.caption)).check(matches(isDisplayed()));
        onView(withId(R.id.getSolution)).check(matches(isDisplayed()));
        onView(withId(R.id.solution)).check(matches(isDisplayed()));
        onView(withId(R.id.coeffALabel)).check(matches(isDisplayed()));
        onView(withId(R.id.coeffA)).check(matches(isDisplayed()));
        onView(withId(R.id.coeffBLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.coeffB)).check(matches(isDisplayed()));
        grade += 3;
        passTests++;
    }

    @Test
    public void appName() {
        //Check Application name
        total++;
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals(appContext.getString(R.string.app_name), "Решение уравнений");
        grade += 2;
        passTests++;
    }

    @Test
    public void stringRes() {
        //Check string resource
        total++;
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull(appContext.getString(R.string.caption_text));
        assertNotNull(appContext.getString(R.string.coeff_a_text));
        assertNotNull(appContext.getString(R.string.coeff_b_text));
        grade += 2;
        passTests++;
    }

    @Test
    public void emptyEditText() {
        //Check empty text on startup
        total++;
        onView(withId(R.id.coeffA)).check(matches(withText("")));
        onView(withId(R.id.coeffB)).check(matches(withText("")));
        onView(withId(R.id.solution)).check(matches(withText("")));
        grade += 2;
        passTests++;
    }

    @Test
    public void errorInput1() {
        //Check error input with empty text
        total++;
        onView(withId(R.id.coeffA)).perform(typeText("5"));
        onView(withId(R.id.coeffB)).perform(typeText(""));
        onView(withId(R.id.getSolution)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.solution)).check(matches(withText("Ошибка ввода")));
        grade += 2;
        passTests++;
    }

    @Test
    public void errorInput2() {
        //Check error input with real number
        total++;
        onView(withId(R.id.coeffA)).perform(typeText("5"));
        onView(withId(R.id.coeffB)).perform(typeText("7.2"));
        onView(withId(R.id.getSolution)).perform(click());
        onView(withId(R.id.solution)).check(matches(withText("Ошибка ввода")));
        grade += 2;
        passTests++;
    }

    @Test
    public void errorInput3() {
        //Check error input with wrong text
        total++;
        onView(withId(R.id.coeffA)).perform(typeText("hello"));
        onView(withId(R.id.coeffB)).perform(typeText("2"));
        onView(withId(R.id.getSolution)).perform(click());
        onView(withId(R.id.solution)).check(matches(withText("Ошибка ввода")));
        grade += 2;
        passTests++;
    }

    @Test
    public void changeConfigTest() {
        //Rotate device
        int a, b;
        total++;
        ActivityScenario<MainActivity> scenarioRule = activityScenarioRule.getScenario();
        a = 5;
        b = -10;
        onView(withId(R.id.coeffA)).perform(clearText(), typeText(Integer.toString(a)));
        onView(withId(R.id.coeffB)).perform(clearText(), typeText(Integer.toString(b)));
        onView(withId(R.id.getSolution)).perform(click());
        scenarioRule.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
        onView(withId(R.id.coeffA)).check(matches(withText(Integer.toString(a))));
        onView(withId(R.id.coeffB)).check(matches(withText(Integer.toString(b))));
        onView(withId(R.id.solution)).check(matches(withText("2")));
        grade += 5;
        passTests++;
    }

    @Test
    public void checkNoSolution1() {
        //Check when no solution
        total++;
        int a, b, ans;
        for (int i = 0; i < 10; i++) {
            a = randomInt(0, 1) == 1 ? randomInt(2, 10) : -randomInt(2, 10);
            ans = randomInt(0, 1) == 1 ? randomInt(1, 10) : -randomInt(1, 10);
            b = -a * ans + randomInt(1, Math.abs(a));
            if (b % a == 0) b++;
            onView(withId(R.id.coeffA)).perform(clearText(), typeText(Integer.toString(a)));
            onView(withId(R.id.coeffB)).perform(clearText(), typeText(Integer.toString(b)));
            onView(withId(R.id.getSolution)).perform(click());
            onView(withId(R.id.solution)).check(matches(withText("Нет решений")));
        }
        grade += 5;
        passTests++;
    }

    @Test
    public void checkNoSolution2() {
        //Check when no solution
        total++;
        int a, b;
        for (int i = 0; i < 3; i++) {
            a = 0;
            b = randomInt(1, 101);
            onView(withId(R.id.coeffA)).perform(clearText(), typeText(Integer.toString(a)));
            onView(withId(R.id.coeffB)).perform(clearText(), typeText(Integer.toString(b)));
            onView(withId(R.id.getSolution)).perform(click());
            onView(withId(R.id.solution)).check(matches(withText("Нет решений")));
        }
        grade += 5;
        passTests++;
    }

    @Test
    public void checkManySolution() {
        //Check when many solution
        total++;
        onView(withId(R.id.coeffA)).perform(clearText(), typeText("0"));
        onView(withId(R.id.coeffB)).perform(clearText(), typeText("0"));
        onView(withId(R.id.getSolution)).perform(click());
        onView(withId(R.id.solution)).check(matches(withText("Бесконечное множество решений")));
        grade += 5;
        passTests++;
    }

    @Test
    public void checkSolution1() {
        //Check when no solution
        total++;
        int a, b, ans;
        for (int i = 0; i < 10; i++) {
            a = randomInt(0, 1) == 1 ? randomInt(2, 10) : -randomInt(2, 10);
            ans = randomInt(0, 1) == 1 ? randomInt(1, 10) : -randomInt(1, 10);
            b = -a * ans;
            onView(withId(R.id.coeffA)).perform(clearText(), typeText(Integer.toString(a)));
            onView(withId(R.id.coeffB)).perform(clearText(), typeText(Integer.toString(b)));
            onView(withId(R.id.getSolution)).perform(click());
            onView(withId(R.id.solution)).check(matches(withText(Integer.toString(ans))));
        }
        grade += 10;
        passTests++;
    }

    @Test
    public void checkSolution2() {
        //Check when no solution
        total++;
        int a, b, ans;
        for (int i = 0; i < 3; i++) {
            a = randomInt(0, 1) == 1 ? randomInt(2, 10) : -randomInt(2, 10);
            b = 0;
            onView(withId(R.id.coeffA)).perform(clearText(), typeText(Integer.toString(a)));
            onView(withId(R.id.coeffB)).perform(clearText(), typeText(Integer.toString(b)));
            onView(withId(R.id.getSolution)).perform(click());
            onView(withId(R.id.solution)).check(matches(withText(Integer.toString(0))));
        }
        grade += 5;
        passTests++;
    }

    @AfterClass
    public static void printResult() {
        Log.d("Tests", passTests + " из " + total + " тестов пройдено.");
        Log.d("Tests", grade + " из 50 баллов получено.");
    }

    private int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
