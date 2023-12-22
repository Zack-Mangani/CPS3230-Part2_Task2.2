import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EbayOperator {

    private static WebDriver driver;

    // State Variables
    boolean IsProductSearched = false;
    boolean IsProductPageViewed = false;
    boolean IsBasketPageViewed = false;
    boolean IsDailyDealsInitiated = false;
    boolean IsCategoryInitiated = false;

    // Transitions
    boolean InitiateSearch(String searchQuery) { //Implementation of searching a product
        System.setProperty("webdriver.chrome.driver", "C:/Users/zackm/webtesting/chromedriver-win64/chromedriver.exe");
        ChromeOptions co = new ChromeOptions();
        co.setBinary("C:/Users/zackm/webtesting/chrome-win64/chrome.exe");
        driver = new ChromeDriver(co);
        driver.get("https://www.ebay.co.uk/");
        WebElement searchInput = driver.findElement(By.name("_nkw"));

        searchInput.sendKeys(searchQuery);

        searchInput.submit();
        IsProductSearched = true;

        return IsProductSearched;
    }
    public boolean isProductSearched() {
        return IsProductSearched;
    }

    boolean SelectProduct() { //Implementation of selecting first product
        if (IsProductSearched || IsBasketPageViewed || IsCategoryInitiated || IsDailyDealsInitiated) {
            IsProductSearched = false;
            IsBasketPageViewed = false;
            IsCategoryInitiated = false;
            IsDailyDealsInitiated = false;
        }

        String xpathExpression = "//ul[@class='srp-results srp-list clearfix']/li[1]//div[@class='s-item__title']//span";

        WebElement productLink = driver.findElement(By.xpath(xpathExpression));

        productLink.click();

        IsProductPageViewed = true;

        return IsProductPageViewed;
    }

    boolean isProductPageViewed() {
        return IsProductPageViewed;
    }

    boolean ViewBasket() { //Implementation of viewing basket page
        if (IsProductSearched || IsProductPageViewed || IsCategoryInitiated|| IsDailyDealsInitiated) {
            IsProductSearched = false;
            IsProductPageViewed = false;
            IsCategoryInitiated = false;
            IsDailyDealsInitiated = false;
        }

        WebElement svgIcon = driver.findElement(By.className("gh-cart-icon"));

        svgIcon.click();

        IsBasketPageViewed = true;

        return IsBasketPageViewed;

    }

    boolean isBasketPageViewed() {
        return IsBasketPageViewed;
    }

    boolean SelectCategory(String categoryName) { //Implementation of selecting a category

        if (IsProductSearched || IsProductPageViewed || IsBasketPageViewed|| IsDailyDealsInitiated) {
            IsProductSearched = false;
            IsProductPageViewed = false;
            IsBasketPageViewed = false;
            IsDailyDealsInitiated = false;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement shopByCategoryButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gh-shop-a")));
        shopByCategoryButton.click();


        WebElement allCategoriesLink = driver.findElement(By.id("gh-shop-see-all-center"));
        allCategoriesLink.click();


        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[@class='cat-title']/a[text()='" + categoryName + "']")));

        categoryLink.click();

        IsCategoryInitiated = true;
        return IsCategoryInitiated;



    }

    boolean isCategoryPageInitiated() {
        return IsCategoryInitiated;
    }

    boolean ViewDailyDeals() { //Implementation of viewing Daily Deals page
        if (IsProductSearched || IsProductPageViewed || IsBasketPageViewed|| IsCategoryInitiated) {
            IsProductSearched = false;
            IsProductPageViewed = false;
            IsBasketPageViewed = false;
            IsCategoryInitiated = false;
        }

        WebElement dailyDealsLink = driver.findElement(By.linkText("Daily Deals"));
        dailyDealsLink.click();

        IsDailyDealsInitiated = true;
        return IsDailyDealsInitiated;


    }
    boolean isDailyDealsInitiated() {
        return IsDailyDealsInitiated;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:/Users/zackm/webtesting/chromedriver-win64/chromedriver.exe");
        ChromeOptions co = new ChromeOptions();
        co.setBinary("C:/Users/zackm/webtesting/chrome-win64/chrome.exe");
        driver = new ChromeDriver(co);

    }

}