package arteco.valen.shop.inmemory;

import arteco.valen.shop.*;
import arteco.valen.shop.exception.IncorrectPermissionException;
import arteco.valen.shop.exception.ItemNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class InMemoryShopImpl implements Stock {
    private List<Item> productList = new ArrayList<>();


//*Permite la creacion de una tienda, lo que permitira tener diferentes tiendas , cada una con su stock.

    public InMemoryShopImpl() {


    }

    public int getQuantity(int itemId) throws ItemNotFoundException {
        Item item = searchItemShop(itemId);
        return item.getQuantity();
    }

    public void setQuantity(int number, int itemId, User user) throws ItemNotFoundException, IncorrectPermissionException {
        if (isManager(user)) {
            Item item = searchItemShop(itemId);
            item.setQuantity(item.getQuantity() + number);
        }
    }

    public void addItem(String desc, double price, int quantity, User user) throws ItemNotFoundException, IncorrectPermissionException { // Otra posibilidad sería sólo pedir: (String desc, Double price) -> Item
        if (isManager(user)) {
            if (price > 0.00 && quantity > 0) {
                Item item = new Item(idAssigner(), desc, price, quantity);
                if (productList.size() == 0) {
                    productList.add(item);
                } else {
                    boolean found = false;
                    for (Item _item : productList) {
                        String itemName = _item.getName();
                        if (item.getName().equals(itemName)) { // int la comparación con == es ok. Pero con Integer --> error!. id.equals(_id);
                            found = true;
                            incQuantity(quantity, _item.getId(), user);
                        }
                    }
                    if (!found) {
                        productList.add(item);
                    }
                }
            } else {
                throw new ItemNotFoundException("Item not found. Please insert a new one");
            }

        }
    }


    @Override
    public void incQuantity(int cantidad, int itemId, User user) throws ItemNotFoundException {
        if (cantidad > 0) {
            Item item = searchItemShop(itemId);
            item.setQuantity(item.getQuantity() + cantidad);
        } else {
            throw new ItemNotFoundException("Item not found. Please insert a new one");
        }

    }

    @Override
    public int stockCount() {
        int totalQuantity = 0;
        System.out.println("Actually, our stock have:");
        for (Item producto : productList) {
            int itemQuantity = producto.getQuantity();
            String itemName = producto.getName();
            totalQuantity = totalQuantity + itemQuantity;
            System.out.println("Del producto " + itemName + " tenemos en stock " + itemQuantity + " unidades");
        }

        System.out.println("En total tenemos " + totalQuantity + " unidades en Stock");
        return totalQuantity;
    }


    public void removeItem(int itemId, User user) throws ItemNotFoundException, IncorrectPermissionException {
        if (isManager(user)) {
            Item stockItem = searchItemShop(itemId);
            productList.remove(stockItem);
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }


    public Item searchItemShop(int itemId) throws ItemNotFoundException {
        for (Item producto : productList) {
            if (producto.getId() == itemId) {
                return producto;
            }
        }
        throw new ItemNotFoundException("The item wasn't found, Please enter a new ID");
    }

    public Item searchItemShopByName(String itemName) throws ItemNotFoundException {
        for (Item producto : productList) {
            if (producto.getName().equals(itemName)) {
                return producto;
            }
        }
        throw new ItemNotFoundException("The item wasn't found, Please enter a new ID");
    }


    private boolean isManager(User user) throws IncorrectPermissionException {
        if (user.getRole() == Role.MANAGER) {
            return true;
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }

    private int idAssigner() {
        int lastId = 0;
        if (productList.size() >= 1) {
            lastId = productList.get(productList.size() - 1).getId();
            return lastId + 1;
        }

        return lastId + 1;
    }

    public void modifyName(int itemId, String newName, User user) throws ItemNotFoundException, IncorrectPermissionException {
        if (isManager(user)) {
            searchItemShop(itemId).setName(newName);
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }

    public void modifyPrice(int itemId, double cantidad, User user) throws ItemNotFoundException, IncorrectPermissionException {

        if (isManager(user) && cantidad > 0) {
            searchItemShop(itemId).setPrice(cantidad);
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }

    public void viewItem(int itemId) throws ItemNotFoundException {
        Item stockItem = searchItemShop(itemId);
        System.out.println("Our product " + stockItem.getName() + ", have the ID " + stockItem.getId() + ". It's price is " + stockItem.getPrice() + " and we have " + stockItem.getQuantity() + " units");

    }

    public void viewShop() throws ItemNotFoundException {
        for (Item aProductList : productList) {
            viewItem(aProductList.getId());

        }
    }

    public void searchAndAddCarry(User user) throws IncorrectPermissionException {
        if (user != null) {
            if (user.getCarry() != null) {
            } else {
                Carry newCarry = new InMemoryCarryImpl();
                user.setCarry(newCarry);
            }
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }
}


