package arteco.valen.springFirstTry;


import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.*;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private Options options = new Options();
    private String itemName;
    private double itemPrice;
    private int itemQuantity;
    private int itemId;
    User user = null;
    private Shop shop = new ShopImpl();


    Cli() {

        options.addOption("c", "Customer", false, "Initialize de customer shop");
        options.addOption("m", "Manager", false, "Initialize the Manager shop");
        options.addOption("h", "help", false, "show help.");
        options.addOption("vs", "viewShop", false, "Here you can see the shop");
        options.addOption("a", "addItem", false, "Here you can add an Item");
        options.addOption("b","addCarry",false,"Here you can add item to carry");
        options.addOption("r","removeItem",false,"Remove item for a carry");
        options.addOption("d","deleteItem",false,"Delete Item from Shop");
        options.addOption("buy","buyCarry",false,"Buy all the carry");
        options.addOption("vc","viewCarry",false,"View all the carry");
        options.addOption("mod","modifyItem",false,"Modify an Item");
    }

    public void parse(String[] args) {
        CommandLineParser parser = new DefaultParser();

        Scanner scanner = new Scanner(System.in);
        CommandLine cmd = null;


        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("u")) {
                System.out.println("Welcome Customer!");
                user = new User("Juanito", 33, false);

            }
            if (cmd.hasOption("m")) {
                System.out.println("Welcome Manager!, What are we going to do today?");
                user = new User("Valen", 28, true);

            }
            if (cmd.hasOption("h"))
                help();
            if (cmd.hasOption("a")) {
                System.out.println("You choose insert an object to shop. Please insert item Name:");
                itemName = scanner.nextLine();
                System.out.println("Insert price:");
                String itemPriceString = scanner.nextLine();
                itemPrice = Double.parseDouble(itemPriceString.replace(",", "."));
                System.out.println("Insert item quantity:");
                String itemQuantityString = scanner.nextLine();
                itemQuantity = Integer.parseInt(itemQuantityString);
                addItemShop(itemName, itemPrice, itemQuantity, user);
            }
            if (cmd.hasOption("vs")) {

                try {
                    shop.viewShop();
                } catch (ItemNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if(cmd.hasOption("b")) {
                System.out.println("Wich product want to insert:");
                itemName = scanner.nextLine();
                try {
                    Item item = shop.searchItemShopByName(itemName);
                    itemId = item.getId();
                } catch (ItemNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("How many " + itemName + " do you want?");
                String itemQuantityString = scanner.nextLine();
                itemQuantity = Integer.parseInt(itemQuantityString);
                try {
                    user.getCarry().addCarry(itemId, itemQuantity, shop,user);
                } catch (ItemNotFoundException | IncorrectQuantityException e) {
                    e.printStackTrace();
                }
                try {
                    user.getCarry().viewCarry(shop);
                } catch (ItemNotFoundException e) {
                    e.printStackTrace();
                }
                if (cmd.hasOption("r")) {
                    System.out.println("wich Item wants to remove from carry? (Answer all for remove all)");
                    String itemName = scanner.nextLine();
                    if (itemName.equals("all")) {
                        try {
                            user.getCarry().removeAllCarryAndReturnItemsToShop(shop);
                        } catch (ItemNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("How many " + itemName + " do you want to remove?");
                        String itemQuantityRemoveString = scanner.nextLine();
                        itemQuantity = Integer.parseInt(itemQuantityRemoveString);
                        try {
                            itemId = shop.searchItemShopByName(itemName).getId();
                            user.getCarry().removeItemCarryAndReturnItemToShop(itemId, itemQuantity, shop);
                        } catch (ItemNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
                if(cmd.hasOption("d")){
                    System.out.println("Wich item do you want to delete from shop?");
                    String itemName=scanner.nextLine();
                    try {
                        itemId= shop.searchItemShopByName(itemName).getId();
                        try {
                            shop.removeItem(itemId,user);
                        } catch (IncorrectPermisionException e) {
                            e.printStackTrace();
                        }
                    } catch (ItemNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                if(cmd.hasOption("buy")){
                    try {
                        user.getCarry().buyCarry(shop);
                    } catch (ItemNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if(cmd.hasOption("vc")){
                    try {
                        user.getCarry().viewCarry(shop);
                    } catch (ItemNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if(cmd.hasOption("mod")){
                    System.out.println("witch item want to modify (enter id)");
                    String idString= scanner.nextLine();
                    itemId=Integer.parseInt(idString);
                    try {
                        shop.searchItemShop(itemId);
                        System.out.println("what do you want to change? (name,price or quantity)");
                        String option=scanner.nextLine();
                        if(option.equals("name")){
                            System.out.println("Write the new name");
                            String newName=scanner.nextLine();
                            try {
                                shop.modifyName(itemId,newName,user);
                            } catch (IncorrectPermisionException e) {
                                e.printStackTrace();
                            }
                        }
                        if(option.equals("price")){
                            System.out.println("Write the new price");
                            String newPriceString=scanner.nextLine();
                            double newPrice=Double.parseDouble(newPriceString.replace(",", "."));
                            try {
                                shop.modifyPrice(itemId,newPrice,user);
                            } catch (IncorrectPermisionException e) {
                                e.printStackTrace();
                            }
                        }
                        if(option.equals("quantity")){
                            System.out.println("Write the new quantity");
                            String newQuantityString=scanner.nextLine();
                            int newQuantity= Integer.parseInt(newQuantityString);
                            shop.searchItemShop(itemId).setQuantity(newQuantity);
                        }

                        else{
                            System.out.println("Incorrect word");
                        }
                    } catch (ItemNotFoundException e) {
                        e.printStackTrace();
                    }
                }


        } catch (ParseException e) {

            log.log(Level.SEVERE, "Failed to parse comand line properties", e);

            help();
        }
    }


    private void help() {
        // This prints out some help

        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Main", options);

    }

    private void addItemShop(String itemName, Double itemPrice, int itemQuantity, User user) {
        try {
            shop.addItem(itemName, itemPrice, itemQuantity, user);
        } catch (ItemNotFoundException | IncorrectPermisionException e) {
            e.printStackTrace();
        }
        try {
            shop.viewShop();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }

}



