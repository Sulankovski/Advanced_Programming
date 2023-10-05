import java.util.*;
import java.util.stream.Collectors;

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}

class Car{
    private String proizvoditel;
    private String model;
    private int cena;
    private float moknost;

    public Car(String proizvoditel, String model, int cena, float moknost) {
        this.proizvoditel = proizvoditel;
        this.model = model;
        this.cena = cena;
        this.moknost = moknost;
    }

    public String getProizvoditel() {
        return proizvoditel;
    }

    public int getCena() {
        return cena;
    }

    public String getModel() {
        return model;
    }

    public float getMoknost() {
        return moknost;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d",proizvoditel, model, moknost, cena);
    }
}
class CarCollection{
    private List<Car> list;

    public CarCollection() {
        list=new ArrayList<>();
    }
    public void addCar(Car car){
        list.add(car);
    }
    public Comparator<Car> spored_cena(){
        return Comparator.comparing(Car::getCena);
    }
    public Comparator<Car> spored_model(){
        return Comparator.comparing(Car::getModel);
    }
    public Comparator<Car> spored_moknost(){
        return Comparator.comparing(Car::getMoknost);
    }
    public void sortByPrice(boolean ascending){
        if(ascending){
            list=list.stream().sorted(spored_cena().thenComparing(spored_moknost())).collect(Collectors.toList());
        }else{
            list=list.stream().sorted(spored_cena().thenComparing(spored_moknost()).reversed()).collect(Collectors.toList());
        }
    }
    public List<Car> filterByManufacturer(String manufacturer){
        List<Car> tmp;
        tmp=list.stream().filter(x->x.getProizvoditel().equalsIgnoreCase(manufacturer))
                .sorted(spored_model())
                .collect(Collectors.toList());
        return tmp;
    }
    public List<Car> getList(){
        return list;
    }
}