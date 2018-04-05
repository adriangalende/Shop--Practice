package arteco.valen.shop;

public class User implements Manager, Customer {
    private String name;
    private int years;
    private boolean manager;
    private Carry carry;


    //* Clase que permite crear usuarios diferenciados para realizar diferentes pedidos.


    User(String name, int years, boolean manager) {
        this.name = name;
        this.years = years;
        this.manager = manager;
    }

    public String getName() {
        return this.name;
    }

    public int getYears() {
        return this.years;
    }

    @Override
    public Role getRole() {
        if (this.isManager()) {
            return Role.MANAGER;
        } else {
            return Role.USER;
        }
    }

    boolean isManager() {
        return this.manager;
    }

    public Carry getCarry() {
        return carry;
    }

    public void setCarry(Carry newCarry) {
        carry = newCarry;
    }
}