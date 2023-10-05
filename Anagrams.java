import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Anagrams {
    public static String leksikografski(String zbor){
//        List<Character> bukvi=new ArrayList<>();
//        IntStream.range(0,zbor.length()).forEach(x->bukvi.add(zbor.charAt(x)));
//        List<Character> sredeni=new ArrayList<>(bukvi);                               //provereno
//        StringBuilder tmp=new StringBuilder();
//        sredeni.stream().sorted().forEach(tmp::append);
//        return tmp.toString();
        char[] charArray = zbor.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }
    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Anagrami lista=new Anagrami();
        BufferedReader input=new BufferedReader(new InputStreamReader(inputStream));
        input.lines().forEach(line->lista.addNew(leksikografski(line),line));
        lista.print();
    }
}
class Anagrami{
    private Map<String,List<String>> lista_anagrami;

    public Anagrami() {
        lista_anagrami=new LinkedHashMap<>();
    }
    public void addNew(String ID, String zbor){
        String key=Anagrams.leksikografski(ID);
        if(lista_anagrami.containsKey(key)){
            List<String> update=lista_anagrami.get(key);
            update.add(zbor);
            update=update.stream().sorted().collect(Collectors.toList());
            lista_anagrami.put(key,update);
        }else {
            List<String> New=new ArrayList<>();
            New.add(zbor);
            lista_anagrami.put(key,New);
        }
    }
    public void print(){
        lista_anagrami.forEach((key1, value) -> {
            List<String> pom = new ArrayList<>(value);
            StringBuilder tmp = new StringBuilder();
            pom.forEach(x -> {
                if (!x.equals(pom.get(pom.size() - 1))) {
                    tmp.append(x);
                    tmp.append(" ");
                } else {
                    tmp.append(x);
                }
            });
            System.out.println(tmp);
        });
    }
}