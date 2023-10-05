import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class Vozac implements Comparable<Vozac> {
    private String ime;
    private String[] vremina;

    public Vozac(String linija) {
        vremina=new String[3];
        String[] delovi=linija.split("\\s+");
        ime=delovi[0];
        vremina[0]=delovi[1];
        vremina[1]=delovi[2];
        vremina[2]=delovi[3];
    }
    private double pretvarane(String podatok){
        String[] delovi=podatok.split(":");
        int minuti=Integer.parseInt(delovi[0]);
        int sekundi=Integer.parseInt(delovi[1]);
        int ns=Integer.parseInt(delovi[2]);
        return minuti+(sekundi/60.0)+(ns/1000.0)/60.0;
    }
    public double najdobro_vreme(){
        double min=100;
        for(int i=0; i<vremina.length; i++){
            if(min>pretvarane(vremina[i])){
                min=pretvarane(vremina[i]);
            }
        }
        return min;
    }
    public String najdobro_vreme_podatok(){
        String vreme = null;
        double min=100;
        for(int i=0; i<vremina.length; i++){
            if(min>pretvarane(vremina[i])){
                min=pretvarane(vremina[i]);
                vreme=vremina[i];
            }
        }
        return vreme;
    }

    public String getIme() {
        return ime;
    }

    @Override
    public int compareTo(Vozac o) {
        return Double.compare(this.najdobro_vreme(),o.najdobro_vreme());
    }
}
class F1Race {
    private List<Vozac> vozacList;

    public F1Race() {
        vozacList=new ArrayList<>();
    }
    void readResults(InputStream inputStream){
        BufferedReader citac=new BufferedReader(new InputStreamReader(inputStream));
        vozacList=citac.lines().map(linija-> new Vozac(linija)).collect(Collectors.toList());
    }
    void printSorted(OutputStream outputStream){
        PrintWriter izlez=new PrintWriter(outputStream);
        vozacList=vozacList.stream().sorted(Vozac::compareTo).collect(Collectors.toList());
        for(int i=0; i<vozacList.size(); i++){
           StringBuilder tmp=new StringBuilder();
           tmp.append(String.format("%d.",i+1));
           tmp.append(String.format(" %-10s", vozacList.get(i).getIme()));
           tmp.append(String.format("%10s",vozacList.get(i).najdobro_vreme_podatok()));
           izlez.println(tmp);
        }
        izlez.flush();
    }
}