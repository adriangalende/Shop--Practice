package arteco.valen.shop.database;

import arteco.valen.shop.Shop;
import arteco.valen.shop.ShopEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlShopEnvironment implements ShopEnvironment {

    private Shop shop;

    public SqlShopEnvironment() {
        try {
            Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            shop = new SqlShopImpl(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Shop getShop() {
        return shop;
    }

}
