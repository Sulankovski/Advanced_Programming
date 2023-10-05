import java.security.cert.CertPath;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    private String karegorija;
    private String id;
    private String name;
    private double price;
    private LocalDateTime datum_prizvodstvo;
    private int kolicina;

    public Product(String karegorija, String id, String name, double price, LocalDateTime datum_prizvodstvo) {
        this.karegorija = karegorija;
        this.id = id;
        this.name = name;
        this.price = price;
        this.datum_prizvodstvo = datum_prizvodstvo;
        kolicina=0;
    }

    public String getKaregorija() {
        return karegorija;
    }

    public double getPrice() {
        return price;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public int getKolicina() {
        return kolicina;
    }

    public LocalDateTime getDatum_prizvodstvo() {
        return datum_prizvodstvo;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name=" + name +
                ", reatedAt=" + datum_prizvodstvo +
                ", price=" + price +
                ", quantitySold=" + kolicina +
                '}';
    }
}

class OnlineShop {
    private Map<String,Product> lista;

    OnlineShop() {
        lista=new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        lista.put(id,new Product(category,id,name,price,createdAt));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        double cena=0;
        if(!lista.containsKey(id)) {
            throw new ProductNotFoundException("");
        }else {
            Product tmp=lista.get(id);
            cena=quantity*tmp.getPrice();
            tmp.setKolicina(tmp.getKolicina()+quantity);
        }
        return cena;
    }
    public Comparator<Product> sporedcena(){
        return Comparator.comparing(Product::getPrice);
    }
    public Comparator<Product> sporedkolicina(){
        return Comparator.comparing(Product::getKolicina);
    }
    public Comparator<Product> sporeedProizvodstvo(){
        return Comparator.comparing(Product::getDatum_prizvodstvo);
    }
    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product> lista_produkti=new ArrayList<>();
        lista.forEach((key, value) -> lista_produkti.add(value));
        List<List<Product>> result = new ArrayList<>();
        if(null == category){
            while (!lista_produkti.isEmpty()){
                List<Product> tmp=new ArrayList<>();
                IntStream.range(0,pageSize)
                        .forEach(x->{
                            if(lista_produkti.get(0)!=null){
                                tmp.add(lista_produkti.get(0));
                                lista_produkti.remove(0);
                            }
                        });
                result.add(tmp);
            }
        }else {
            List<Product> tmp=lista_produkti.stream().filter(x->x.getKaregorija().equals(category)).collect(Collectors.toList());
            if(comparatorType.equals(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST)){
                tmp=tmp.stream().sorted(sporedcena().reversed()).collect(Collectors.toList());
            }else if(comparatorType.equals(COMPARATOR_TYPE.LOWEST_PRICE_FIRST)){
                tmp=tmp.stream().sorted(sporedcena()).collect(Collectors.toList());
            }else if(comparatorType.equals(COMPARATOR_TYPE.LEAST_SOLD_FIRST)){
                tmp=tmp.stream().sorted(sporedkolicina()).collect(Collectors.toList());
            }else if(comparatorType.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST)) {
                tmp = tmp.stream().sorted(sporedkolicina().reversed()).collect(Collectors.toList());
            }else if(comparatorType.equals(COMPARATOR_TYPE.NEWEST_FIRST)) {
                tmp = tmp.stream().sorted(sporeedProizvodstvo()).collect(Collectors.toList());
            }else if(comparatorType.equals(COMPARATOR_TYPE.OLDEST_FIRST)) {
                tmp = tmp.stream().sorted(sporeedProizvodstvo().reversed()).collect(Collectors.toList());
            }
            while (!tmp.isEmpty()){
                List<Product> pom=new ArrayList<>();
                List<Product> finalTmp = tmp;
                IntStream.range(0,pageSize)
                        .forEach(x->{
                            if(finalTmp.get(0)!=null){
                                pom.add(finalTmp.get(0));
                                pom.remove(0);
                            }
                        });
                result.add(pom);
            }
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

