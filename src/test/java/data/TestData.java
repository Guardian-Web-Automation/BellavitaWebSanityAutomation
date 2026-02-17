package data;

import org.testng.annotations.DataProvider;

public class TestData {
    @DataProvider(name = "CategoryData")
    public Object[][] getCategoryData() {
        return new Object[][] {
                {"Crazy Deals", "crazydeals"}
//                {"Shop All", "shopall"},
//                {"Bestsellers", "bestseller"},
//                {"Perfumes", "perfumes"},
//                {"Bath & Body", "bathandbody"},
//                {"Cosmetics", "cosmetics"},
//                {"New Arrivals", "newarrivals"},
//                {"Skincare", "skincare"},
//                {"Gifting", "gifting"}
        };
    }

    @DataProvider(name = "crazyDealName")
    public Object[][] crazyDealName() {
        return new Object[][] {
                {"Ultimate Perfume Box"},
                {"Daily Care Kit"},
                {"Gift Set Party"}

        };
    }

    @DataProvider(name = "PerfumesSubCategoryData")
    public Object[][] getPerfumeSubCategoryData() {
        return new Object[][] {
                {"Perfumes", "All Perfumes", "All Perfumes"}
//                {"Perfumes", "Men", "Perfumes for Him"},
//                {"Perfumes", "Women", "Perfumes for Her"},
//                {"Perfumes", "Unisex", "Unisex perfumes"},
//                {"Perfumes", "Oud Collection", "Experience our OUD Collection"},
//                {"Perfumes", "Attars", "Attars"},
//                {"Perfumes", "Little Luxuries", "Little Luxury"}
        };
    }
    @DataProvider(name = "BathAndBodySubCategoryData")
    public Object[][] getBathAndBodySubCategoryData() {
        return new Object[][] {
                {"Bath & Body", "Shower Gel", "Shower Gel"}
//                {"Bath & Body", "Body Mist", "Body Mist"},
//                {"Bath & Body", "Body Parfum", "Body Parfum"},
//                {"Bath & Body", "Body Lotion", "Body Lotion"},
//                {"Bath & Body", "Travel Kit", "Travel Kit"},
//                {"Bath & Body", "Bathing Bar (Soap)", "Bathing Bar (Soap)"},
//                {"Bath & Body", "Combos", "Combos"}
        };
    }

    @DataProvider(name = "SkincareSubCategoryData")
    public Object[][] SkincareSubCategoryData() {
        return new Object[][]{
                {"Skincare", "All Skincare", "Skincare Products"}
//                {"Skincare", "Face Wash", "Face Wash"},
//                {"Skincare", "Lip Care", "Lip Care"},
//                {"Skincare", "Skin Essential Combos", "Skin Essential Combos"},
//                {"Skincare", "K-Beauty Secrets", "K-Beauty Secrets"},
//                {"Skincare", "Dry Skin", "Dry Skin"},
//                {"Skincare", "Sun-Kissed Summer", "Sun-Kissed Summer"},
//                {"Skincare", "Sunscreen", "Sunscreen"},
//                {"Skincare", "Underarm Roll On ", "Underarm Roll On"},
//                {"Skincare", "Wooden Comb", "Wooden Comb"}

        };
    }

    @DataProvider
    public Object[][] CosmeticsSubCategoryData() {
        return new Object[][]{
                {"Cosmetics", "Flawless Base Range", "Flawless Base Range"}
//                {"Cosmetics", "Mood Range", "Mood Matching Lip Gloss"},
//                {"Cosmetics", "Pick Any 2", "Pick Any 2"},
//                {"Cosmetics", "Pick any 3", "Pick Any 3"},
//                {"Cosmetics", "Lipsticks", "Lipsticks"},
//                {"Cosmetics", "Kajal", "Kajal"},
//                {"Cosmetics", "Nail Paint", "Nail Paint"}
        };
    }

    @DataProvider
    public Object[][] ProductAndQuantityAddToCart() {
        return new Object[][]{
                {"CEO", 5}
        };
    }

    @DataProvider(name = "CategoryAndProduct")
    public Object[][] getCategoryAndProduct() {
        return new Object[][] {
                {"BESTSELLERS", "CEO Man Perfume - 100ml"},
                {"NEW ARRIVALS", "Final Touch Makeup Fixer"}
        };
    }

}
