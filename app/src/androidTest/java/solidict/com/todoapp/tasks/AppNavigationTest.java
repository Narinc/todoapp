package solidict.com.todoapp.tasks;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import solidict.com.todoapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests for the {@link DrawerLayout} layout component in {@link TasksActivity} which manages
 * navigation within the app.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * <p> Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests</p>
     */
    @Rule
    public ActivityTestRule<TasksActivity> activityTestRule =
            new ActivityTestRule<TasksActivity>(TasksActivity.class);

    @Test
    public void clickOnStatisticsNavigationItem_ShowStatisticsScreen() {
        openStatisticsScreen();

        // TODO: 2.01.2018 will be continue
    }

    private void openStatisticsScreen() {
        // Open Drawer to click on navigation item.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.statistics_navigation_menu_item));
    }
}
