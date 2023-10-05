import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}
class Ime{
    private String ime;
    private int br_pojavuvana;

    public Ime() {
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getBr_pojavuvana() {
        return br_pojavuvana;
    }

    public void setBr_pojavuvana(int br_pojavuvana) {
        this.br_pojavuvana = br_pojavuvana;
    }
    public int unikatni_bukvi(){
        Set<Character> pom=new HashSet<>();
        for(int i=0; i<ime.length(); i++){
            char bukva=Character.toLowerCase(ime.charAt(i));
            pom.add(bukva);
        }
        return pom.size();
    }
    public boolean proverka(int dolzina){
        return ime.length()<dolzina;
    }
}
class Names{
    private Map<String,Ime> lista;
    private List<Ime> lista_imina;

    public Names() {
        lista=new HashMap<>();
        lista_imina=new ArrayList<>();
    }
    public void addName(String name){
        if(!lista.containsKey(name)){
            Ime novo=new Ime();
            novo.setIme(name);
            novo.setBr_pojavuvana(1);
            lista.put(name,novo);
        }else {
            Ime update=lista.get(name);
            update.setBr_pojavuvana(update.getBr_pojavuvana()+1);
        }
    }
    public Comparator<Ime> spored_ime(){
        return Comparator.comparing(Ime::getIme);
    }
    public void printN(int n){
        lista.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(spored_ime()))
                        .forEach(key->lista_imina.add(key.getValue()));

        lista.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(x->x.getValue().getBr_pojavuvana()>=n)
                .forEach(key-> System.out.println(key.getValue().getIme()+" ("+
                                                key.getValue().getBr_pojavuvana()+") "+
                                                key.getValue().unikatni_bukvi()));
    }
    public String findName(int len, int x){
        lista_imina=lista_imina.stream().filter(element->element.proverka(len)).collect(Collectors.toList());
        if(x<lista_imina.size()){
            return lista_imina.get(x).getIme();
        }else {
            while (x>=lista_imina.size()){
                x-=lista_imina.size();
            }
            return lista_imina.get(x).getIme();
        }
    }
}