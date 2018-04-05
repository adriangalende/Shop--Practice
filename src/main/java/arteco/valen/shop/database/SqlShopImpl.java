package arteco.valen.shop.database;

import arteco.valen.shop.*;
import arteco.valen.shop.exception.IncorrectPermissionException;
import arteco.valen.shop.exception.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlShopImpl implements Stock {
    private final Connection connection;


//*Permite la creacion de una tienda, lo que permitira tener diferentes tiendas , cada una con su stock.

    SqlShopImpl(Connection c) {
        this.connection = c;
        this.createTable();
        // crear las estructuras necesarias.
        //c.prepareStatement("create ....")


    }

    /*private void referencias() throws Exception {
        Connection c = this.connection;
        // ejemplo de query
        PreparedStatement ps = c.prepareStatement("select * from ,,");
        ResultSet rs = ps.executeQuery();
        // en el result set tienes las filas y las columnas obtenidas
        while (rs.next()) {
            // Empiezan en 1.
            rs.getInt(1);
            rs.getInt("asdfasdf");
        }
        ps.close();
        rs.close();

        // updates / insert

        ps = c.prepareStatement("insert ... | update ... ");
        int num = ps.executeUpdate();
        ps.close();

        // El string de sql, NUNCA DEBE LLEVAR STRING CONCATENADOS, siempre lo argumentos con ?
        ps = c.prepareStatement("select * from Usuarios where alta = ?");
        ps.setString(1, "sdgsdfgds");

        // insert
        ps = c.prepareStatement("insert into Shop (col1,col2,...)values(?,?,?,?)");
        ps.setString(1, "sdgsdfgds");

        // insert
        ps = c.prepareStatement("update from Shop set col1 = ?, col2=?,... where id = ? ");
        ps.setString(1, "sdgsdfgds");
    }*/

    public int getQuantity(int itemId) throws SQLException {
        return readTableById(itemId).getQuantity();
    }

    public void setQuantity(int number, int itemId, User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException {
        if (isManager(user)) {
            Item item = searchItemShop(itemId);

            updateSetFinalQuantity(number, itemId);
            item.setQuantity(item.getQuantity() + number);
        }
    }


    public void addItem(String desc, double price, int quantity, User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException { // Otra posibilidad sería sólo pedir: (String desc, Double price) -> Item
        if (isManager(user)) {
            if (price > 0.00 && quantity > 0) {
                Item newItem = new Item(null, desc, price, quantity);
                if (readTable().size() == 0) {
                    addItemTable(newItem);
                } else {
                    Item item = readTableByName(desc);
                    if (item != null) {
                        updateQuantity(quantity, item.getId());
                    } else {
                        addItemTable(newItem);
                    }
                }
            } else {
                throw new ItemNotFoundException("Item not found. Please insert a new one");
            }

        }
    }


    @Override
    public void incQuantity(int cantidad, int itemId, User user) throws ItemNotFoundException, SQLException, IncorrectPermissionException {
        if (isManager(user)) {
            Item item = searchItemShop(itemId);
            updateQuantity(-cantidad, itemId);

            item.setQuantity(item.getQuantity() + cantidad);
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }


    }

    @Override
    public int stockCount() throws SQLException {
        int totalQuantity = 0;
        System.out.println("Actually, our stock have:");
        for (Item producto : readTable()) {
            int itemQuantity = producto.getQuantity();
            String itemName = producto.getName();
            totalQuantity = totalQuantity + itemQuantity;
            System.out.println("Del producto " + itemName + " tenemos en stock " + itemQuantity + " unidades");
        }

        System.out.println("En total tenemos " + totalQuantity + " unidades en Stock");
        return totalQuantity;
    }


    public void removeItem(int itemId, User user) throws ItemNotFoundException, IncorrectPermissionException, SQLException {
        if (isManager(user)) {
            Item stockItem = searchItemShop(itemId);
            deleteTable(itemId);
            readTable().remove(stockItem);
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }


    public Item searchItemShop(int itemId) throws ItemNotFoundException, SQLException {
        Item item = readTableById(itemId);
        if (item != null) {
            return item;
        } else {
            throw new ItemNotFoundException("The item wasn't found, Please enter a new ID");
        }
    }

    public Item searchItemShopByName(String itemName) throws ItemNotFoundException, SQLException {
        Item item = readTableByName(itemName);
        if (item != null) {
            return item;
        } else {
            throw new ItemNotFoundException("The item wasn't found, Please enter a new ID");
        }
    }


    private boolean isManager(User user) throws IncorrectPermissionException {
        if (user.getRole() == Role.MANAGER) return true;
        else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }


    public void modifyName(int itemId, String newName, User user) throws
            IncorrectPermissionException, SQLException {
        if (isManager(user)) {
            updateName(newName, itemId);
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }

    public void modifyPrice(int itemId, double cantidad, User user) throws
            IncorrectPermissionException, SQLException {

        if (isManager(user)) {

            updatePrice(cantidad, itemId);
        } else {
            throw new IncorrectPermissionException("The user can't do that");
        }
    }

    public void viewItem(int itemId) throws ItemNotFoundException, SQLException {
        Item stockItem = searchItemShop(itemId);
        System.out.println("Our product " + stockItem.getName() + ", have the ID " + stockItem.getId() + ". It's price is " + stockItem.getPrice() + " and we have " + stockItem.getQuantity() + " units");

    }

    public void viewShop() throws ItemNotFoundException, SQLException {
        for (Item aProductList : readTable()) {
            viewItem(aProductList.getId());

        }
    }

    public void searchAndAddCarry(User user) {
        if (user.getCarry() != null) {
        } else {
            Carry newCarry = new SqlCarryImpl();
            user.setCarry(newCarry);
        }
    }

    private void createTable() {
        // ejemplo de query
        try {
            Statement state = this.connection.createStatement();
            state.execute("CREATE TABLE public.shopTable (ID bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name char(50),price double, quantity int)");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Patata',0.95,50)");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Pizza Atun',2.15,25);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Guacamole',0.90,60);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Yogur Arandanos',1.00,40);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Espada Bastarda',200.00,5);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Armadura de fuego',150.50,5);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Entrada Fary',50.00,100);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Excursion Mordor',25.00,100);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Caja preservativos Cutrex',9.99,50);\n");
            state.execute("INSERT INTO public.shopTable (name,price,quantity) VALUES ('Trifuerza',1000000.00,1);");
            state.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Item readTableById(int itemId) throws SQLException {
        Item item = null;
        // ejemplo de query
        PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM shopTable WHERE id =?");
        ps.setInt(1, itemId);
        ResultSet rs = ps.executeQuery();
        // en el result set tienes las filas y las columnas obtenidas
        while (rs.next()) {
            // Empiezan en 1.
            item = retrieveItem(rs);
        }
        ps.close();
        rs.close();
        return item;
    }

    private Item retrieveItem(ResultSet rs) {
        try {
            Item item = new Item();
            item.setQuantity(rs.getInt("quantity"));
            item.setName(rs.getString("name"));
            item.setPrice(rs.getDouble("price"));
            item.setId(rs.getInt("id"));
            return item;
        } catch (SQLException sqe) {
            throw new RuntimeException(sqe);
        }
    }


    private Item readTableByName(String itemName) throws SQLException {
        Item item = null;
        // ejemplo de query
        PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM shopTable WHERE name =?");
        ps.setString(1, itemName);
        ResultSet rs = ps.executeQuery();
        // en el result set tienes las filas y las columnas obtenidas
        while (rs.next()) {
            // Empiezan en 1.
            item = retrieveItem(rs);
        }
        ps.close();
        rs.close();
        return item;
    }


    private List<Item> readTable() throws SQLException {
        ArrayList<Item> itemArray = new ArrayList<>();
        // ejemplo de query
        PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM shopTable");
        ResultSet rs = ps.executeQuery();
        // en el result set tienes las filas y las columnas obtenidas
        while (rs.next()) {
            itemArray.add(retrieveItem(rs));
        }
        ps.close();
        rs.close();
        return itemArray;
    }


    private void updateQuantity(int substractQuantity, int idItem) throws SQLException {
        // ejemplo de query
        PreparedStatement ps = this.connection.prepareStatement("UPDATE shopTable SET quantity=?+quantity WHERE id=?;");
        ps.setInt(1, substractQuantity);
        ps.setInt(2, idItem);
        ps.executeUpdate();
        ps.close();

    }

    private void updateSetFinalQuantity(int finalQuantity, int idItem) throws SQLException {
        // ejemplo de query
        PreparedStatement ps = this.connection.prepareStatement("UPDATE shopTable SET quantity=? WHERE id=?;");
        ps.setInt(1, finalQuantity);
        ps.setInt(2, idItem);
        ps.executeUpdate();
        ps.close();

    }

    private void updatePrice(double newPrice, int idItem) throws SQLException {
        // ejemplo de query
        PreparedStatement ps = this.connection.prepareStatement("UPDATE shopTable SET price=? WHERE id=?;");
        ps.setDouble(1, newPrice);
        ps.setInt(2, idItem);
        ps.executeUpdate();
        ps.close();

    }

    private void updateName(String newName, int idItem) throws SQLException {
        // ejemplo de query
        PreparedStatement ps = this.connection.prepareStatement("UPDATE shopTable SET name=? WHERE id=?;");
        ps.setString(1, newName);
        ps.setInt(2, idItem);
        ps.executeUpdate();
        ps.close();

    }


    private void deleteTable(int id) {
        {
            // ejemplo de query
            PreparedStatement ps;
            try {
                ps = this.connection.prepareStatement("DELETE from shopTable where ID=?;");
                ps.setInt(1, id);
                ps.executeUpdate();

                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }


    private void addItemTable(Item item) {
        {
            // ejemplo de query
            PreparedStatement ps = null;
            try {
                ps = this.connection.prepareStatement("INSERT INTO shopTable (id, name,price,quantity) VALUES (," + item.getName() + "," + item.getPrice() + "," + item.getQuantity() + ");");
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
}


