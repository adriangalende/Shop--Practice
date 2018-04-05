package arteco.valen.shop.inmemory;


import arteco.valen.shop.Carry;
import arteco.valen.shop.Item;
import arteco.valen.shop.Shop;
import arteco.valen.shop.User;
import arteco.valen.shop.exception.IncorrectPermissionException;
import arteco.valen.shop.exception.IncorrectQuantityException;
import arteco.valen.shop.exception.ItemNotFoundException;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryCarryImpl implements Carry {

    private Map<Integer, Integer> carryMap = new LinkedHashMap<>();

    @Override
    public void addCarry(int itemId, int quantity, Shop shop, User user) throws ItemNotFoundException, IncorrectQuantityException, IncorrectPermissionException, SQLException {
        shop.searchAndAddCarry(user);
        Item item = shop.searchItemShop(itemId);
        if (quantity > 0 && item != null) {
            if (item.getQuantity() >= quantity) {
                if (carryMap.containsKey(itemId)) {
                    int newQuantity = carryMap.get(itemId) + quantity;
                    carryMap.replace(itemId, newQuantity);
                    int finalQuantity = item.getQuantity() - quantity;
                    item.setQuantity(finalQuantity);

                } else {
                    carryMap.put(itemId, quantity);
                    int finalQuantity = item.getQuantity() - quantity;
                    item.setQuantity(finalQuantity);
                }
            } else {
                throw new IncorrectQuantityException("Incorrect Quantity, Please insert a new one");
            }
        } else {
            throw new IncorrectQuantityException("Incorrect Quantity, Please insert a new one");
        }
    }

    @Override
    public void viewCarry(Shop shop) throws ItemNotFoundException, SQLException {
        for (Map.Entry<Integer, Integer> entry : carryMap.entrySet()) {
            System.out.println("Our " + shop.searchItemShop(entry.getKey()).getName() + " with " + entry.getValue());
        }
    }

    @Override
    public void removeItemCarryAndReturnItemToShop(int itemId, int quantity, Shop shop) throws ItemNotFoundException, SQLException {
        boolean found = false;

        Integer cant = carryMap.get(itemId);
        if (cant !=null) {
            if (quantity > 0) {
                    if (carryMap.get(itemId) > quantity) {
                        int newQuantity = carryMap.get(itemId) - quantity;
                        carryMap.replace(itemId, newQuantity);
                        found = true;
                        Item item = shop.searchItemShop(itemId);
                        item.setQuantity(item.getQuantity() + quantity);
                    } else {
                        carryMap.remove(itemId);
                        found = true;
                        Item item = shop.searchItemShop(itemId);
                        item.setQuantity(item.getQuantity() + carryMap.get(itemId));
                    }
                }

        }
        if (!found) {
            throw new ItemNotFoundException("Item not found in your carry");
        }
    }

    @Override
    public void removeAllCarryAndReturnItemsToShop(Shop shop) throws ItemNotFoundException, SQLException {
        for (Map.Entry<Integer, Integer> entry : carryMap.entrySet()) {
            Item item = shop.searchItemShop(entry.getKey());
            item.setQuantity(item.getQuantity() + entry.getValue());
        }
        carryMap.clear();
    }

    @Override
    public void buyCarry(Shop shop) throws ItemNotFoundException, SQLException {
        double finalPrice = 0.00;
        NumberFormat nf = new DecimalFormat("0.00");
        System.out.println("Su compra es:");
        for (Map.Entry<Integer, Integer> entry : carryMap.entrySet()) {
            Item item = shop.searchItemShop(entry.getKey());
            String numString = nf.format(totalPrice(item.getPrice(), entry.getValue()));
            System.out.println(entry.getValue() + " unidades de " + item.getName() + " a un precio total de " + numString);
            finalPrice = finalPrice + totalPrice(item.getPrice(), entry.getValue());
        }
        String doubleFinalPrice = nf.format(finalPrice);
        System.out.println("El precio total de su compra es: " + doubleFinalPrice);
    }

    private double totalPrice(Double price, int quantity) {
        return price * quantity;
    }
}
