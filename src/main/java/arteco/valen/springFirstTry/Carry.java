package arteco.valen.springFirstTry;

public interface Carry {

    void addCarry(int itemId, int quantity, Shop shop,User user) throws ItemNotFoundException, IncorrectQuantityException;

    void viewCarry(Shop shop) throws ItemNotFoundException;

    void removeItemCarryAndReturnItemToShop(int itemId, int quantity, Shop shop) throws ItemNotFoundException;

    void removeAllCarryAndReturnItemsToShop(Shop shop) throws ItemNotFoundException;

    void buyCarry(Shop shop) throws ItemNotFoundException;


}