import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја", "" };
        TermFrequency tf = new TermFrequency(System.in,stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
        //tf.print();
    }
}
class TermFrequency{
    private Map<String,Integer> lista_zborovi;
    private String[] nepotrebni;

    public TermFrequency(InputStream inputStream, String[] nepotrebni) {
        lista_zborovi=new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        BufferedReader citac=new BufferedReader(new InputStreamReader(inputStream));
        this.nepotrebni = nepotrebni;
        citac.lines().map(line->line.split("\\s+")).forEach(this::add_new_word);
        sreduvane();
    }
    public void print(){
        System.out.println(lista_zborovi);
    }
    public void add_new_word(String[] line){
        if(line[0].equals("")){
            return;
        }
        for(int i=0; i<line.length; i++){
            String zbor=get_zbor(line[i]);
            if (lista_zborovi.containsKey(zbor)) {
                int tmp = lista_zborovi.get(zbor);
                lista_zborovi.put(zbor.toLowerCase(), tmp + 1);
            } else {
                lista_zborovi.put(zbor.toLowerCase(), 1);
            }
        }
    }
    public void sreduvane(){
        for(String zbor: nepotrebni){
            lista_zborovi.remove(zbor);
        }
    }
    public String get_zbor(String zbor){   //ni vraka gotov zbor za stavane vo tablata
        if (zbor.length() == 0) {
            return zbor;
        }
        List<Character> znaci=new ArrayList<>();
        znaci.add('.');
        znaci.add(',');
        znaci.add('-');
        StringBuilder tmp=new StringBuilder();
        for(int i=0; i<zbor.length(); i++){
            if(!znaci.contains(zbor.charAt(i))){
                tmp.append(zbor.charAt(i));
            }
        }
        return tmp.toString();
    }

    public int countTotal(){
        return lista_zborovi.values().stream().reduce(0, Integer::sum);
    }
    public  int countDistinct(){
        return lista_zborovi.size();
    }
    public Comparator<Map.Entry<String,Integer>> spred_vrednost() {
        return Map.Entry.comparingByValue();
    }
    public Comparator<Map.Entry<String,Integer>> spored_key(){
        return Map.Entry.comparingByKey();
    }
    public List<String> mostOften(int k){
        lista_zborovi.entrySet().stream().sorted(spred_vrednost().reversed()).collect(Collectors.toList()).subList(0,k)
                .forEach(x-> System.out.printf("%d ",x.getValue()));
        return lista_zborovi.entrySet()
                .stream()
                .sorted(spred_vrednost().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .subList(0,k);

    }

}