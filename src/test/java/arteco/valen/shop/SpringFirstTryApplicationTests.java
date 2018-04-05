package arteco.valen.shop;

import arteco.valen.shop.exception.IncorrectPermissionException;
import arteco.valen.shop.exception.IncorrectQuantityException;
import arteco.valen.shop.exception.ItemNotFoundException;
import arteco.valen.shop.inmemory.InMemoryCarryImpl;
import arteco.valen.shop.inmemory.InMemoryShopEnvironment;
import arteco.valen.shop.inmemory.InMemoryShopImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringFirstTryApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testEnvironment() throws ItemNotFoundException, IncorrectPermissionException, SQLException {
        ShopEnvironment env = new InMemoryShopEnvironment();
        Shop shop = env.getShop();
        User user = new User("Solid Snake", 56, true);
        shop.addItem("Leche Sabrosona", 100.00, 10, user);
        shop.incQuantity(3, 1, user);
        Assert.assertTrue(shop.stockCount() > 0);
    }

    @Test
    public void testUsuario() {
        User Manager = new User("Miquelet Parera", 48, true);
        Assert.assertEquals("Miquelet Parera", Manager.getName());
        Assert.assertEquals(48, Manager.getYears());
        Assert.assertTrue(Manager.isManager());
    }

    @Test

    public void testAddItem() throws IncorrectPermissionException {
        User user = new User("Solid Snake", 56, true);
        InMemoryShopImpl shop = new InMemoryShopImpl();
        try {
            shop.addItem("pollina", 1.00, 1, user);
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
        try {
            shop.addItem("viladrau",0.00,67,user);
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
        try {
            shop.addItem("Hipogrifo",1.00,0,user);
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test

    public void testManager() throws IncorrectPermissionException, ItemNotFoundException, SQLException {
        ShopEnvironment env = new InMemoryShopEnvironment();
        Shop shop = env.getShop();
        User newManager = new User("Jose Penacho", 36, true);
        System.out.println("Test Manager:");
        shop.addItem("Leche Sabrosona", 0.99, 5, newManager);
        shop.viewItem(1);
        shop.modifyName(1, "Nesquick Forever", newManager);
        shop.modifyPrice(1, 250.09, newManager);
        shop.viewItem(1);
        shop.addItem("Pizza de Atun", 3.55, 8, newManager);
        shop.viewItem(2);
        shop.addItem("Pizza de Atun", 3.55, 5, newManager);
        shop.viewItem(2);
        shop.addItem("Guacamole", 2.70, 15, newManager);
        shop.viewItem(3);
        Assert.assertEquals(33, shop.stockCount());
        shop.removeItem(2, newManager);
        Assert.assertEquals(20, shop.stockCount());
        shop.viewShop();
    }

    @Test

    public void notManagerTest() throws ItemNotFoundException, SQLException {
        ShopEnvironment env = new InMemoryShopEnvironment();
        Shop shop = env.getShop();
        User notManager = new User("Jose Penacho", 36, false);
        User newManager = new User("Miguel Penacho", 36, true);
        System.out.println("Test Manager:");
        try {
            shop.addItem("Leche Sabrosona", 0.99, 5, notManager);
        } catch (ItemNotFoundException | IncorrectPermissionException|SQLException e) {
            e.printStackTrace();
        }
        try {
            shop.addItem("Leche Sabrosona", 0.99, 5, newManager);
        } catch (ItemNotFoundException | IncorrectPermissionException|SQLException e) {
            e.printStackTrace();
        }
        shop.viewItem(1);
        try {
            shop.modifyName(1, "Nesquick Forever", notManager);
        } catch (ItemNotFoundException | IncorrectPermissionException|SQLException e) {
            e.printStackTrace();
        }
        try {
            shop.modifyPrice(1, 250.09, notManager);
        } catch (ItemNotFoundException | IncorrectPermissionException|SQLException e) {
            e.printStackTrace();
        }
        try {
            shop.viewItem(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test

    public void CarritoTestadd() {
        Carry carrito = new InMemoryCarryImpl();
        ShopEnvironment env = new InMemoryShopEnvironment();
        Shop shop = env.getShop();
        User newManager = new User("Jose Penacho", 36, true);
        try {
            shop.addItem("Leche Sabrosona", 0.99, 5, newManager);
            shop.addItem("Pizza de Atun", 3.55, 8, newManager);
            shop.addItem("Guacamole", 2.70, 15, newManager);
        } catch (ItemNotFoundException | IncorrectPermissionException|SQLException e) {
            e.printStackTrace();
        }

        try {
            carrito.addCarry(2, 5, shop, newManager);
        } catch (ItemNotFoundException | IncorrectQuantityException|IncorrectPermissionException|SQLException e) {
            e.printStackTrace();
        }
        try {
            carrito.viewCarry(shop);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals((Object)shop.searchItemShop(2).getQuantity(), 3);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            carrito.addCarry(50, 5, shop, newManager);
        } catch (ItemNotFoundException | IncorrectQuantityException | IncorrectPermissionException|SQLException e) {
            e.printStackTrace();
        }

    }

    @Test

    public void carritoTestModify() throws IncorrectPermissionException {
        Carry carrito = new InMemoryCarryImpl();
        ShopEnvironment env = new InMemoryShopEnvironment();
        Shop shop = env.getShop();
        User newManager = new User("Jose Penacho", 36, true);
        try {
            shop.addItem("Leche Sabrosona", 0.99, 5, newManager);
            shop.addItem("Pizza de Atun", 3.55, 8, newManager);
            shop.addItem("Guacamole", 2.70, 15, newManager);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }

        try {
            carrito.addCarry(2, 5, shop,newManager);
        } catch (IncorrectQuantityException | ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            carrito.addCarry(1, 10, shop, newManager);
        } catch (IncorrectQuantityException | ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals((Object)shop.searchItemShop(1).getQuantity(), 5);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            carrito.addCarry(1, -10, shop, newManager);
        } catch (IncorrectQuantityException | ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals((Object)shop.searchItemShop(1).getQuantity(), 5);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            carrito.addCarry(50, 10, shop, newManager);
        } catch (IncorrectQuantityException | ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            carrito.viewCarry(shop);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            carrito.addCarry(2, 1, shop, newManager);
        } catch (IncorrectQuantityException | ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals((Object)shop.searchItemShop(2).getQuantity(), 2);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void carritoTestDelete() throws IncorrectPermissionException, IncorrectQuantityException, ItemNotFoundException, SQLException {
        Carry carrito = new InMemoryCarryImpl();
        ShopEnvironment env = new InMemoryShopEnvironment();
        Shop shop = env.getShop();
        User newManager = new User("Jose Penacho", 36, true);
        shop.addItem("Leche Sabrosona", 0.99, 5, newManager);
        shop.addItem("Pizza de Atun", 3.55, 8, newManager);
        shop.addItem("Guacamole", 2.70, 15, newManager);
        carrito.addCarry(2, 5, shop, newManager);
        carrito.addCarry(1, 1, shop, newManager);
        carrito.addCarry(3, 5, shop, newManager);
        try {
            carrito.removeItemCarryAndReturnItemToShop(3, 2, shop);
        } catch (ItemNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((Object)shop.searchItemShop(3).getQuantity(), 12);
        carrito.viewCarry(shop);

        carrito.removeAllCarryAndReturnItemsToShop(shop);

        carrito.viewCarry(shop);
    }





    @Test
    public void carritoTestBuy() throws IncorrectPermissionException, ItemNotFoundException, IncorrectQuantityException, SQLException {
        Carry carrito = new InMemoryCarryImpl();
        ShopEnvironment env = new InMemoryShopEnvironment();
        Shop shop = env.getShop();
        User newManager = new User("Jose Penacho", 36, true);
        shop.addItem("Leche Sabrosona", 0.99, 5, newManager);
        shop.addItem("Pizza de Atun", 3.55, 8, newManager);
        shop.addItem("Guacamole", 2.70, 15, newManager);
        carrito.addCarry(2, 5, shop, newManager);
        carrito.addCarry(1, 1, shop, newManager);
        carrito.addCarry(3, 5, shop, newManager);
        try {
            carrito.buyCarry(shop);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    }

