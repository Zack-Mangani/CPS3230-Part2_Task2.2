import enums.EbayStates;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.util.Random;

import static enums.EbayStates.*;
import static junit.framework.TestCase.*;

public class EbayTester implements FsmModel {
    EbayOperator sut = new EbayOperator();

    EbayStates state = Home;

    boolean IsProductSearched = false;
    boolean IsProductPageViewed = false;
    boolean IsBasketPageViewed = false;
    boolean IsCheckoutInitiated = false;
    boolean IsCategoryInitiated = false;
    boolean IsDailyDealsInitiated = false;


    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void reset(boolean testing) {
        state = Home;

        IsProductSearched = false;
        IsProductPageViewed = false;
        IsBasketPageViewed = false;
        IsCheckoutInitiated = false;

        if(testing) {
            sut = new EbayOperator();
        }
        }

    public boolean InitiateSearchGuard() {
        return state == EbayStates.Home && !sut.isProductSearched();
    }

    public @Action void InitiateSearch() {
        String searchQuery = "book";
        assertTrue(InitiateSearchGuard());

        sut.InitiateSearch(searchQuery);

        state = EbayStates.SearchResults;
        IsProductSearched = true;

        assertTrue(sut.isProductSearched());
        assertEquals(EbayStates.SearchResults, state);
    }



    public boolean SelectProductGuard() {
        return state == EbayStates.SearchResults;
    }

    public @Action void SelectProduct() {
        assertTrue(SelectProductGuard());

        sut.SelectProduct();

        state = EbayStates.ProductDetail;
        IsProductPageViewed = true;

        if (IsProductSearched) {
            IsProductSearched = false;
            assertFalse(sut.isProductSearched());
        } else if (IsBasketPageViewed) {
            IsBasketPageViewed = false;
            assertFalse(sut.isBasketPageViewed());
        } else if (IsCategoryInitiated) {
            IsCategoryInitiated = false;
            assertFalse(sut.isCategoryPageInitiated());
        } else if (IsDailyDealsInitiated) {
            IsDailyDealsInitiated = false;
            assertFalse(sut.isDailyDealsInitiated());
        }

        assertFalse(sut.isProductSearched());
        assertEquals(IsProductPageViewed, sut.isProductPageViewed());
    }

    public boolean ViewBasketGuard(){ return state != DailyDealsPage; }
    public @Action void ViewBasket() {
        sut.ViewBasket();

        state = EbayStates.Basket;
        IsBasketPageViewed = true;


        if (IsProductSearched) {
            IsProductSearched = false;
            assertFalse(sut.isProductSearched());
        } else if (IsProductPageViewed) {
            IsProductPageViewed = false;
            assertFalse(sut.isProductPageViewed());
        } else if (IsCategoryInitiated) {
            IsCategoryInitiated = false;
            assertFalse(sut.isCategoryPageInitiated());
        } else if (IsDailyDealsInitiated) {
            IsDailyDealsInitiated = false;
            assertFalse(sut.isDailyDealsInitiated());
        }


        assertFalse(sut.isProductPageViewed());
        assertEquals(IsBasketPageViewed, sut.isBasketPageViewed());
    }

    public boolean InitiateCategorySelectGuard(){ return state == Basket || state == Home ||state == SearchResults; }
    public @Action void InitiateCategorySelect() {
        sut.SelectCategory("Make-Up Products");

        state = EbayStates.CategoryPage;
        IsCategoryInitiated = true;

        if (IsProductSearched) {
            IsProductSearched = false;
            assertFalse(sut.isProductSearched());
        } else if (IsProductPageViewed) {
            IsProductPageViewed = false;
            assertFalse(sut.isProductPageViewed());
        } else if (IsBasketPageViewed) {
            IsBasketPageViewed = false;
            assertFalse(sut.isBasketPageViewed());
        } else if (IsDailyDealsInitiated) {
            IsDailyDealsInitiated = false;
            assertFalse(sut.isDailyDealsInitiated());
        }


        assertEquals(IsCategoryInitiated, sut.isCategoryPageInitiated());
    }

   // public boolean InitiateCheckoutGuard(){ return state != Checkout; }
    public @Action void InitiateDailyDeals() {
        sut.ViewDailyDeals();

        state = EbayStates.DailyDealsPage;
        IsDailyDealsInitiated = true;


        if (IsProductSearched) {
            IsProductSearched = false;
            assertFalse(sut.isProductSearched());
        } else if (IsProductPageViewed) {
            IsProductPageViewed = false;
            assertFalse(sut.isProductPageViewed());
        } else if (IsBasketPageViewed) {
            IsBasketPageViewed = false;
            assertFalse(sut.isBasketPageViewed());
        } else if (IsCategoryInitiated) {
            IsCategoryInitiated = false;
            assertFalse(sut.isCategoryPageInitiated());
        }


        assertEquals(IsDailyDealsInitiated, sut.isDailyDealsInitiated());
    }

    @Test
    public void EbayOperatorModelRunner() {
        //final AllRoundTester tester = new AllRoundTester(new EbayTester());
        //final GreedyTester tester = new GreedyTester(new EbayTester());
        //final LookaheadTester tester = new LookaheadTester(new EbayTester());
        final RandomTester tester = new RandomTester(new EbayTester());

        tester.setRandom(new Random());
        tester.buildGraph();
        tester.addListener(new StopOnFailureListener());
        tester.addListener("verbose");
        tester.addCoverageMetric(new TransitionPairCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());

        tester.generate(100);
        tester.printCoverage();

    }

    }

