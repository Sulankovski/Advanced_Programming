import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

interface Item{
    int getPrice();
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
    }
}
class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
    }
}

class ExtraItem implements Item{
    private String tip;
    private static final String[] validni = {"Coke", "Ketchup"};
    private static final int[] ceni = {5, 3};
    public ExtraItem(String tip) throws InvalidExtraTypeException{
        if(proverka(tip)){
            this.tip=tip;
        }else {
            throw new InvalidExtraTypeException();
        }
    }
    private boolean proverka(String proveri){
        return proveri.equals(validni[0]) || proveri.equals(validni[1]);
    }

    @Override
    public int getPrice() {
        int cena=0;
        if(tip.equals(validni[0])){
            cena=ceni[0];
        }else if(tip.equals(validni[1])){
            cena=ceni[1];
        }
        return cena;
    }

    @Override
    public String toString() {
        return tip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return Objects.equals(tip, extraItem.tip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tip);
    }
}
class PizzaItem implements Item{
    private String tip;
    private static final String[] validni = {"Standard", "Pepperoni", "Vegetarian"};
    private static final int[] ceni = {10, 12, 8};

    public PizzaItem(String tip) throws InvalidPizzaTypeException{
        if(proverka(tip)){
            this.tip=tip;
        }else {
            throw new InvalidPizzaTypeException();
        }
    }
    private boolean proverka(String proveri){
        return proveri.equals(validni[0]) || proveri.equals(validni[1]) || proveri.equals(validni[2]);
    }

    @Override
    public int getPrice() {
        int cena=0;
        if(tip.equals(validni[0])){
            cena=ceni[0];
        }else if(tip.equals(validni[1])){
            cena=ceni[1];
        }else if(tip.equals(validni[2])){
            cena=ceni[2];
        }
        return cena;
    }

    @Override
    public String toString() {
        return tip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return Objects.equals(tip, pizzaItem.tip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tip);
    }
}

class Order{
    private List<Item> trakatanci;
    private List<Integer> kolicina;
    private boolean  zakluceno;

    public Order(){
        trakatanci=new ArrayList<>();
        kolicina=new ArrayList<>();
        zakluceno=false;
    }

    public int proverka(Item za_proveruvane){
        int pom=-1;
        for(int i=0; i<trakatanci.size(); i++){
            if(trakatanci.get(i).equals(za_proveruvane)){
                pom=i;
                break;
            }
        }
        return pom;
    }

    public void addItem(Item item, int count) throws OrderLockedException, ItemOutOfStockException {
        if(!zakluceno){
            if(count>10){
                throw new ItemOutOfStockException(item);
            }
            if(proverka(item)!=-1){
                int nov_indeks=proverka(item);
                trakatanci.set(nov_indeks, item);
                kolicina.set(nov_indeks,count);
            }else {
                trakatanci.add(item);
                kolicina.add(count);
            }
        }else {
            throw new OrderLockedException();
        }
    }
    public int getPrice(){
        int vkupna_cena=0;
        for(int i=0; i<trakatanci.size(); i++){
            vkupna_cena+=(trakatanci.get(i).getPrice()*kolicina.get(i));
        }
        return vkupna_cena;
    }
    public void removeItem(int idx) throws ArrayIndexOutOfBоundsException, OrderLockedException {
        if(!zakluceno) {
            if (idx >= 0 && idx <= trakatanci.size()) {
                trakatanci.remove(idx);
                kolicina.remove(idx);
            } else {
                throw new ArrayIndexOutOfBоundsException(idx);
            }
        }else {
            throw new OrderLockedException();
        }
    }
    public void lock() throws EmptyOrder {
        if(kolicina.size()!=0){
            zakluceno=true;
        }else {
            throw new EmptyOrder();
        }
    }
    public void displayOrder(){
        StringBuilder tmp=new StringBuilder();
        for(int i=0; i<trakatanci.size(); i++){
            tmp.append(String.format("%3s",i+1));
            tmp.append(".");
            tmp.append(String.format("%-15s",trakatanci.get(i)));
            tmp.append("x");
            tmp.append(String.format("%2s",kolicina.get(i)));
            tmp.append(String.format("%5s",trakatanci.get(i).getPrice()*kolicina.get(i)));
            tmp.append("$");
            tmp.append("\n");
        }
        tmp.append(String.format("%-22s","Total:"));
        tmp.append(String.format("%5s",getPrice()));
        tmp.append("$");
        System.out.println(tmp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return zakluceno == order.zakluceno && Objects.equals(trakatanci, order.trakatanci) && Objects.equals(kolicina, order.kolicina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trakatanci, kolicina, zakluceno);
    }
}
class ArrayIndexOutOfBоundsException extends Exception{
    private int indeks;
    public ArrayIndexOutOfBоundsException(int idx){
        indeks=idx;
    }
}
class ItemOutOfStockException extends Exception{
    private Item item;
    public ItemOutOfStockException(Item item){
        this.item=item;
    }
}
class OrderLockedException extends Exception{
    public OrderLockedException(){

    }
}
class EmptyOrder extends Exception{
    public EmptyOrder(){

    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}