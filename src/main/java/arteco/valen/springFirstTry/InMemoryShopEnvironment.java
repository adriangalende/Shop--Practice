package arteco.valen.springFirstTry;

public class InMemoryShopEnvironment implements ShopEnvironment {

    private final Shop shop;

    InMemoryShopEnvironment() {
        shop = new ShopImpl();
    }

    public Shop getShop() {
        return shop;
    }

}
