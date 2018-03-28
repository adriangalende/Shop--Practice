package arteco.valen.springFirstTry;


public interface Shop {


    void addItem(String desc, double price, int quantity, User user) throws ItemNotFoundException, IncorrectPermisionException;


    void incQuantity(int cantidad, int itemId,User user) throws ItemNotFoundException;

    /**
     * Devuelve el total de items, teniendo en cuenta la cantidad de cada uno de ellos
     * Siempre >= 0
     */
    int stockCount();



    Item searchItemShop(int itemID) throws ItemNotFoundException;
    Item searchItemShopByName(String itemName) throws ItemNotFoundException;


    void removeItem(int itemId,User user) throws ItemNotFoundException, IncorrectPermisionException;

    void viewItem(int itemId) throws ItemNotFoundException;

    void modifyName(int itemId,String newName,User user) throws ItemNotFoundException, IncorrectPermisionException;

    void modifyPrice(int itemId,double newPrice,User user) throws ItemNotFoundException, IncorrectPermisionException;

    void viewShop() throws ItemNotFoundException;

    Carry searchAndAddCarry(User user);

}
