import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}
class Flight implements Comparable<Flight>{
    private String from;
    private String to;
    private int time_departure;
    private int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time_departure = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
    public int getfullvreme(){
        return time_departure+duration;
    }
    public String timeDepature(){
        int saat=time_departure/60;
        int minuti=time_departure-60*saat;
        return String.format("%02d:%02d",saat,minuti);
    }
    public String timeArival(){
        int vreme=time_departure+duration;
        int saat=vreme/60;
        int minuti=vreme-60*saat;
        if(!(saat<=23)){
            saat=saat-24;
        }
        return String.format("%02d:%02d",saat,minuti);
    }
    public String flightTime(){
        String format;
        int vremepoletuvane=time_departure/60;
        int vreme=time_departure+duration;
        int saat=vreme/60;
        if(!(saat<=23)){
            saat=saat-24;
        }
        int vremesletuvane=saat;
        if(vremesletuvane>=vremepoletuvane){
            format=String.format("%01dh%02dm",duration/60,duration-(duration/60)*60);
        }else {
            format=String.format("+1d %01dh%02dm",duration/60,duration-(duration/60)*60);
        }
        return format;
    }
    public void print(){
        System.out.println(String.format("%s-%s %s-%s %s",from,to,timeDepature(),timeArival(),flightTime()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return time_departure == flight.time_departure && duration == flight.duration && Objects.equals(from, flight.from) && Objects.equals(to, flight.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, time_departure, duration);
    }

    @Override
    public int compareTo(Flight o) {
        return Integer.compare(time_departure,o.time_departure);
    }
}
class Airport{
    private String name;
    private String country;
    private String code;
    private int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }
}
class Airports{
    private List<Airport> lista_aerodromi;
    private List<Flight> lista_letovi;

    public Airports() {
        lista_aerodromi=new ArrayList<>();
        lista_letovi=new ArrayList<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        Airport tmp=new Airport(name, country, code, passengers);
        lista_aerodromi.add(tmp);
    }
    public void addFlights(String from, String to, int time, int duration){
        Flight tmp=new Flight(from, to, time, duration);
        lista_letovi.add(tmp);
    }
    public void showFlightsFromAirport(String code){
        AtomicInteger redenbr= new AtomicInteger(1);
        Set<Flight> tmp=lista_letovi.stream().filter(x->x.getFrom().equals(code)).collect(Collectors.toSet());
        lista_aerodromi.forEach(x->
        {
            if(x.getCode().equals(code)){
                System.out.println(String.format("%s (%s)",x.getName(),x.getCode()));
                System.out.println(x.getCountry());
                System.out.println(x.getPassengers());
            }
        });
        TreeSet<Flight> tmp1=new TreeSet<>(Comparator.comparing(Flight::getFrom).thenComparing(Flight::getTo).thenComparing(Flight::timeDepature));
        tmp1.addAll(tmp);
        tmp1.forEach(x->{
            System.out.print(redenbr+". ");
            x.print();
            redenbr.incrementAndGet();
        });
    }
    public void showDirectFlightsFromTo(String from, String to){
        AtomicBoolean tmp=new AtomicBoolean(false);
        lista_letovi.stream()
                .filter(x->x.getFrom().equals(from) && x.getTo().equals(to))
                .forEach(x->{
                    x.print();
                    tmp.set(true);
                });
        if(!tmp.get()){
            System.out.printf("No flights from %s to %s\n",from,to);
        }
    }
    public Comparator<Flight> spordvreme(){
        return Comparator.comparing(Flight::timeDepature);
    }
    public Comparator<Flight> sporedvreme1(){
        return Comparator.comparing(Flight::getfullvreme);
    }
    public void showDirectFlightsTo(String to){
        TreeSet<Flight> tmp=new TreeSet<>(Comparator.comparing(Flight::timeDepature));
        tmp.addAll(lista_letovi);
//        tmp.stream()
//                .filter(x->x.getTo().equals(to))
//                .forEach(Flight::print);
        lista_letovi.stream()
                .sorted(spordvreme().thenComparing(sporedvreme1()))
                   .filter(x->x.getTo().equals(to))
                        .forEach(Flight::print);
    }
}