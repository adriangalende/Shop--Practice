package arteco.valen.shop;

import arteco.valen.shop.exception.IncorrectPermissionException;
import arteco.valen.shop.exception.IncorrectQuantityException;
import arteco.valen.shop.exception.ItemNotFoundException;

import java.sql.SQLException;

public interface Carry {


    /***
     * add an item to carry, if quantity is <= 0 or price is  <= 0 throws exception,
     * and if quantity is > of the quantity of that item in shop throws exception too.</=>
     * if item is on carry, it only add the new quantity
     * @param itemId int ->the id of the item
     * @param quantity int -> the quantity of the item
     * @param shop Shop -> the shop where you were buying
     * @param user  User-> the user than buys
     * @throws ItemNotFoundException an item is not found
     * @throws IncorrectQuantityException the amount isnt correct
     */
    void addCarry(int itemId, int quantity, Shop shop,User user) throws ItemNotFoundException, IncorrectQuantityException, IncorrectPermissionException, SQLException;

    /***
     * it returns a print  of all  the items that has been added to the carry.
     * if is empty do not show nothing.
     * Throws ItemnotFound exception if is added a item that is not in shop.
     * @param shop Shop -> the shop where you were buying
     * @throws ItemNotFoundException the item is not found
     */

    void viewCarry(Shop shop) throws ItemNotFoundException, SQLException;

    /***
     * remove an item  in the carry and returns the quantity to the shop.
     * thows itemNotFound exception if the item is not in shop.
     * quantity must be > 0. if not throws exception. If quantity to remove is bigger than the quantity in the carry,
     * it removes the entire item and return to shop the items inside the carry
     * @param itemId int -> the id of the item
     * @param quantity int -> the quantity that must be removed
     * @param shop Shop -> the shop
     * @throws ItemNotFoundException an item is not found
     */

    void removeItemCarryAndReturnItemToShop(int itemId, int quantity, Shop shop) throws ItemNotFoundException, SQLException;

    /**
     * it removes all the items in the carry, adding them again to shop. throws exception if the item inside the carry isn't in shop.
     * @param shop Shop ->
     * @throws ItemNotFoundException an item is not found
     */

    void removeAllCarryAndReturnItemsToShop(Shop shop) throws ItemNotFoundException, SQLException;

    /**
     * returns a list of the items buyed and a totalPrice.
     * if item is not in shop throws exception
     * @param shop Shop ->
     * @throws ItemNotFoundException an item is not found
     */

    void buyCarry(Shop shop) throws ItemNotFoundException, SQLException;


}