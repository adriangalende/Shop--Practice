package arteco.valen.shop.inmemory;

import arteco.valen.shop.Shop;
import arteco.valen.shop.ShopEnvironment;

public class InMemoryShopEnvironment implements ShopEnvironment {

    private final Shop shop;

    public InMemoryShopEnvironment() {
        shop = new InMemoryShopImpl();
    }

    public Shop getShop() {
        return shop;
    }

}
