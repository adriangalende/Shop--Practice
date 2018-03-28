package arteco.valen.springFirstTry;

public interface Stock extends Shop {
    //* permite cambiar las cantidades de un producto

   int getQuantity (int itemId) throws ItemNotFoundException;
   void setQuantity( int number, int itemId,User user) throws ItemNotFoundException, IncorrectPermisionException;


}