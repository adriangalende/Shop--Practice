package arteco.valen.shop;

import arteco.valen.shop.exception.IncorrectPermissionException;
import arteco.valen.shop.exception.ItemNotFoundException;

import java.sql.SQLException;

public interface Stock extends Shop {
    //* permite cambiar las cantidades de un producto

   int getQuantity (int itemId) throws ItemNotFoundException, SQLException;
   void setQuantity( int number, int itemId,User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException;


}