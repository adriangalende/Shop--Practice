package arteco.valen.springFirstTry;

import java.util.ArrayList;
import java.util.List;

public class ShopImpl implements Stock {
    private List<Item> productList = new ArrayList<>();


//*Permite la creacion de una tienda, lo que permitira tener diferentes tiendas , cada una con su stock.

    ShopImpl() {


    }

    public int getQuantity(int itemId) throws ItemNotFoundException {
        Item item = searchItemShop(itemId);
        return item.getQuantity();
    }

    public void setQuantity(int number, int itemId, User user) throws ItemNotFoundException, IncorrectPermisionException {
        if (isManager(user)) {
            Item item = searchItemShop(itemId);
            item.incQuantity(number, item, user);
        }
    }

    public void addItem(String desc, double price, int quantity, User user) throws ItemNotFoundException, IncorrectPermisionException { // Otra posibilidad sería sólo pedir: (String desc, Double price) -> Item
        if (isManager(user)) {
            if (price > 0.00 && quantity > 0) {
                Item item = new Item(desc, price, idAssigner(), quantity);
                if (productList.size() == 0) {
                    productList.add(item);
                } else {
                    boolean found = false;
                    for (Item _item : productList) {
                        String itemName = _item.getItemName();
                        if (item.getItemName().equals(itemName)) { // int la comparación con == es ok. Pero con Integer --> error!. id.equals(_id);
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
        Item item = searchItemShop(itemId);
        item.incQuantity(cantidad, item, user);


    }

    @Override
    public int stockCount() {
        int totalQuantity = 0;
        System.out.println("Actually, our stock have:");
        for (Item producto : productList) {
            int itemQuantity = producto.getQuantity();
            String itemName = producto.getItemName();
            totalQuantity = totalQuantity + itemQuantity;
            System.out.println("Del producto " + itemName + " tenemos en stock " + itemQuantity + " unidades");
        }

        System.out.println("En total tenemos " + totalQuantity + " unidades en Stock");
        return totalQuantity;
    }


    public void removeItem(int itemId, User user) throws ItemNotFoundException, IncorrectPermisionException {
        if (isManager(user)) {
            Item stockItem = searchItemShop(itemId);
            productList.remove(stockItem);
        }else{throw new IncorrectPermisionException("The user can't do that");}
    }


    public Item searchItemShop(int itemId) throws ItemNotFoundException
    {
        for (Item producto : productList) {
            if (producto.getId() == itemId) {
                return producto;
            }
        }
        throw new ItemNotFoundException("The item wasn't found, Please enter a new ID");
    }

    public Item searchItemShopByName(String itemName) throws ItemNotFoundException
    {
        for (Item producto : productList) {
            if (producto.getItemName().equals(itemName) ) {
                return producto;
            }
        }
        throw new ItemNotFoundException("The item wasn't found, Please enter a new ID");
    }


    private boolean isManager(User user) throws IncorrectPermisionException {
        if (user.getRole() == Role.MANAGER) {
            return true;
        } else {
            throw new IncorrectPermisionException("The user can't do that");
        }
    }

    private int idAssigner() {
        int lastId = 0;
        if (productList.size() >= 1) {
            lastId = productList.get(productList.size() - 1).getId();
            return lastId + 1;
        }

        int newId = lastId + 1;
        return newId;
    }
    public void modifyName(int itemId, String newName, User user) throws ItemNotFoundException, IncorrectPermisionException {
        if(isManager(user)) {
            searchItemShop(itemId).setItemName(newName, user);
        } else{throw new IncorrectPermisionException("The user can't do that");}
    }

    public void modifyPrice(int itemId, double cantidad, User user) throws ItemNotFoundException, IncorrectPermisionException {

        if (isManager(user)){
        searchItemShop(itemId).setPrice(cantidad,user);}
        else {throw new IncorrectPermisionException("The user can't do that"); }
    }

    public void viewItem(int itemId) throws ItemNotFoundException {
        Item stockItem = searchItemShop(itemId);
        System.out.println("Our product " + stockItem.getItemName() + ", have the ID " + stockItem.getId() + ". It's price is " + stockItem.getPrice() + " and we have " + stockItem.getQuantity() + " units");

    }

    public void viewShop() throws ItemNotFoundException {
        for (int i = 0; i < productList.size(); i++) {
            viewItem(productList.get(i).getId());

        }
    }

    public Carry searchAndAddCarry(User user){
        if (user.getCarry()!= null){
            return user.getCarry();
        }
        else{
            Carry newCarry= new CarryImpl();
            user.setCarry(newCarry);
            return user.getCarry();
        }
    }
}


