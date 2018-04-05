package arteco.valen.shop;


import arteco.valen.shop.exception.IncorrectPermissionException;
import arteco.valen.shop.exception.ItemNotFoundException;

import java.sql.SQLException;

public interface Shop {

    /**
     * Add an item to shop. The user must be Manager, if not throws exception.If item is in shop, it only increase quantity
     * if is not in shop, it asign a new id to the item.
     *
     * price and quantity must be > 0. if not it throws exception
     * @param desc String ->
     * @param price Double
     * @param quantity int
     * @param user User
     * @throws ItemNotFoundException an item is not found
     * @throws IncorrectPermissionException the user can't do that
     */

    void addItem(String desc, double price, int quantity, User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException;


    /**
     * search an item in shop by id, and if it exists, increases the quantity. User must be Manager. if item is not in shop, throws exception.
     * Quantity must be >0
     * @param cantidad int
     * @param itemId int
     * @param user User
     * @throws ItemNotFoundException an item is not found
     */
    void incQuantity(int cantidad, int itemId,User user) throws ItemNotFoundException, SQLException, IncorrectPermissionException;

    /**
     * Devuelve el total de items, teniendo en cuenta la cantidad de cada uno de ellos
     * Siempre >= 0
     */
    int stockCount() throws SQLException;


    /**
     * search if an item is in the shop using the id. if not throws exception
     * @param itemID int
     * @return Returns an Item
     * @throws ItemNotFoundException an item is not found
     */

    Item searchItemShop(int itemID) throws ItemNotFoundException, SQLException;

    /**
     * search if an item is in the shop using the name. if not throws exception
     * @param itemName String
     * @return Item
     * @throws ItemNotFoundException an item is not found
     */
    Item searchItemShopByName(String itemName) throws ItemNotFoundException, SQLException;


    /**
     * remove an item of the shop using an id. User must be Manager ( if not throws exception)
     * if item is not found throws exception too
     * @param itemId int
     * @param user User
     * @throws ItemNotFoundException an item is not found
     * @throws IncorrectPermissionException the user can't do that
     */
    void removeItem(int itemId,User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException;

    /**
     * shows an item in console using his id. If is not found throws exception
     * @param itemId int
     * @throws ItemNotFoundException an item is not found
     */
    void viewItem(int itemId) throws ItemNotFoundException, SQLException;

    /**
     * Change the name of an item using his id. User must be manager( if not throws exception)
     * if the item is not find throws exception
     * @param itemId int
     * @param newName String of the new Name
     * @param user User
     * @throws ItemNotFoundException an item is not found
     * @throws IncorrectPermissionException the user can't do that
     */

    void modifyName(int itemId,String newName,User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException;

    /**
     * Change the price of an item using his id. User must be manager( if not throws exception)
     * if the item is not find or new price is <= 0 throws exception.
     * @param itemId int
     * @param newPrice double of the new price
     * @param user User
     * @throws ItemNotFoundException an item is not found
     * @throws IncorrectPermissionException the user can't do that
     */

    void modifyPrice(int itemId,double newPrice,User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException;

    /**
     * print in console a list of all the items that is in the shop. if an item is not found throws exception
     * @throws ItemNotFoundException an item is not found
     */
    void viewShop() throws ItemNotFoundException, SQLException;

    /**
     * search if an user have an assignet carry. if it got one returns that carry, if not a new carry is asignet to him.
     * user must not be null.
     * @param user User
     * @throws IncorrectPermissionException the user can't do that
     * @return a carry
     */

    void searchAndAddCarry(User user) throws IncorrectPermissionException;

}
