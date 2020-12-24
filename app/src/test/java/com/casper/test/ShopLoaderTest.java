package com.casper.test;

import com.casper.test.data.ShopLoader;
import com.casper.test.data.model.Shop;

import junit.framework.TestCase;

public class ShopLoaderTest extends TestCase {
    ShopLoader shopLoader;

    public void setUp() throws Exception {
        super.setUp();
        shopLoader = new ShopLoader();
    }

    public void tearDown() throws Exception {
    }

    public void testGetShops() {
        assertNotNull(shopLoader.getShops ()) ;
        assertEquals( 0, shopLoader. getShops().size());
    }

    public void testDownload() {
        String content = shopLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
        assertTrue( content.length() >= 300) ;//364or386 linux/win/server
        assertTrue(content.contains("\"longitude\": \"113.526421\","));

    }

    public void testParseJson() {
        String content = "{\n" +
                "  \"shops\": [{\n" +
                "    \"name\": \"暨南大学珠海校区\",\n" +
                "    \"latitude\": \"22.255925\",\n" +
                "    \"longitude\": \"113.541112\",\n" +
                "    \"memo\": \"暨南大学珠海校区\"\n" +
                "  },\n" +
                "    {\n" +
                "      \"name\": \"明珠商业广场\",\n" +
                "      \"latitude\": \"22.251953\",\n" +
                "      \"longitude\": \"113.526421\",\n" +
                "      \"memo\": \"珠海二城广场\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        shopLoader. parseJson(content) ;
        assertEquals(  2,shopLoader. getShops().size()) ;
        Shop shop = shopLoader. getShops().get(1);
        assertEquals( "明珠商业广场", shop.getName() );
        assertEquals( "珠海二城广场", shop. getMemo() ) ;
        assertEquals( 22.2519523,shop. getLatitude(),  1e-6);
        assertEquals(  113.526421, shop. getLongitude(),  1e-6);

    }
}