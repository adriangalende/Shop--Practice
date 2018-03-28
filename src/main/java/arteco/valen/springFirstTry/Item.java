package arteco.valen.springFirstTry;

public class Item {
     private String itemName;
     private double price;
     private int quantity;


    private int id;



    //* Clase Item, porque debo crear diferentes Item, para cada funcion. Implementa Stock, que le permite cambiar las cantidades
    Item(String itenName, double price, int id, int quantity){
         this.itemName=itenName;
         this.price=price;
         this.id=id;
         this.quantity=quantity;


     }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName, User user) {
        if(isManager(user)){
        this.itemName = itemName;}
    }

    public double getPrice() {
        return  price;
    }

    public void setPrice(double price, User user) {
         if(isManager(user)) {
             this.price = price;
         }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return this.quantity;
    }
    public void incQuantity(int number, Item item,User user){
         if(isManager(user)){
         item.quantity=item.quantity + number;}
    }

    public String toString(){
        return "Our product " + getItemName() + ", have the ID " + getId() + ". It's price is " + getPrice() + " and we have " + getQuantity() + " units";
    }

    private boolean isManager(User user) {
        if (user.getRole() == Role.MANAGER) {
            return true;
        } else {
            System.out.println("The user can't do that");
            return false;
        }
    }
}