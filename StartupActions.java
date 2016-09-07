import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class StartupActions {

    private static List<String> dismissables = asList("Allow", "OK");

    @Autowired
    AppLifecycleManager appLifecycleManager;

    @Autowired
    Utility utility;

    @Autowired
    GenericDialog genericDialog;

    @Autowired
    DiscoverPage discoverPage;

    public void resetApp() throws Throwable
    {
        appLifecycleManager.getDriver().resetApp();
        dismissStartupPopups(appLifecycleManager.getDriver());
    }

    public void closeAndLaunchApp()
    {
        utility.pause(2);
        //appLifecycleManager.getDriver().getSettings().addProperty("stopAppOnReset", "false");
        appLifecycleManager.getDriver().runAppInBackground(5);
        utility.pause(10);
        //dismissStartupPopups(appLifecycleManager.getDriver());
    }

    public void dismissStartupPopups(AppiumDriver driver) throws Exception
    {
        System.out.println("Attempting to dismiss initial app's popups");
        int count = 0;
        while (!discoverPage.isDisplayed() && count < 30) {
            for (String dismisable : dismissables) {
                System.out.println("Enter for-loop and go through the key words for dismissing the popup. Current key word is : " + dismisable);
                if (driver.findElements(By.name(dismisable)).size() > 0) {
                    System.out.println("Found button, " + dismisable + ", clicking it to dismiss.");
                    driver.findElement(By.name(dismisable)).click();
                    break;
                }else
                {
                    System.out.println("Button with key word, " + dismisable + ", is not found.");
                }
            }
            count++;
        }

        if(genericDialog.isDisplayed(1))
        {
            System.out.println("Another popup is displayed, attempt to click OK button.");
            genericDialog.clickButton("OK");
        }
        //reaching this line means the App is not on the Discover Page at initial startup, there must be an issue.
        if(!discoverPage.isDisplayed()) {
            throw new Exception("Problem with starting up the app.");
        }
    }



}
