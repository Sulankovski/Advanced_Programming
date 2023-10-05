import java.security.PrivateKey;
import java.util.*;
import java.util.stream.IntStream;

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
class Stadium{
    private String name;
    private Map<String,Sector> sektori;
    private Map<String,Integer> dozvolen_tip;

    Stadium(String name){
        this.name=name;
        sektori=new HashMap<>();
        dozvolen_tip=new HashMap<>();
    }
    void createSectors(String[] sectorNames, int[] sizes){
        IntStream
                .range(0,sectorNames.length)
                .forEach(x-> {
                    sektori.put(sectorNames[x], new Sector(sectorNames[x],sizes[x]));
                    dozvolen_tip.put(sectorNames[x],0);
                }
                );
    }
    void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if(!sektori.get(sectorName).sceckseet(seat)) {
            throw new SeatTakenException();
        }
        if((sektori.get(sectorName).getType() == 1 && type==2) || (sektori.get(sectorName).getType() == 2 && type==1)) {
            throw new SeatNotAllowedException();
        }
        if(type != 0) {
            sektori.get(sectorName).setType(type);
        }
        sektori.get(sectorName).changeseet(seat);
    }
    public Comparator<Sector> spored_mesta(){
        return Comparator.comparing(Sector::getEmptySeats);
    }
    public Comparator<Sector> spored_ime(){
        return Comparator.comparing(Sector::getCode);
    }
    void showSectors(){
        sektori.values().stream()
                .sorted(spored_mesta().reversed().thenComparing(spored_ime()))
                .forEach(Sector::print);
    }
}
class SeatTakenException extends Exception{
    public SeatTakenException(){
        super();
    }
}
class SeatNotAllowedException extends Exception{
    public SeatNotAllowedException(){
        super();
    }
}
class Sector{
    private String code;
    private int sitemesta;
    private int mesta;
    private Map<Integer,Boolean> sedista;
    private int type;

    public Sector(String code, int mesta) {
        this.code = code;
        this.mesta = mesta;
        sitemesta=mesta;
        sedista=new HashMap<>();
        for(int i=0; i<mesta; i++){
            sedista.put(i+1,true);
        }
        type=0;
    }
    public void print(){
        System.out.println(String.format("%s\t%d/%d\t%1.1f%%",code,getEmptySeats(),sitemesta,zafatenost()));
    }
    private double zafatenost(){
        double difference = 1.0 *getEmptySeats()/ sitemesta;
        return 100.0*(1 - difference);
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getEmptySeats() {
        return (int) sedista.values().stream().filter(bool -> bool).count();
    }
    public int getType() {
        return type;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void changeseet(int sediste){
        sedista.put(sediste,false);
    }
    public boolean sceckseet(int sediste){
        return sedista.get(sediste);
    }
    public void setMesta(int mesta) {
        this.mesta = mesta;
    }

    public String getCode() {
        return code;
    }

    public int getMesta() {
        return mesta;
    }
}