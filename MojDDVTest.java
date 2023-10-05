import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class AmountNotAllowedException extends Exception{
    String poraka;
    public AmountNotAllowedException(int vrednost){
        super(String.format("Receipt with amount %d is not allowed to be scanned",vrednost));
        poraka=String.format("Receipt with amount %d is not allowed to be scanned",vrednost);
    }
}
class Smetka{
    private String ime;
    private List<String> podatoci;

    public Smetka(String linija) throws AmountNotAllowedException {
        podatoci=new ArrayList<>();
        String[] delovi=linija.split("\\s+");
        ime=delovi[0];
        int suma=0;
        for(int i=1; i<delovi.length; i++){
            if(Character.isDigit(delovi[i].charAt(0))){
                suma+=Integer.parseInt(delovi[i]);
            }
        }
        if(suma<=30000) {
            IntStream.range(1, delovi.length).forEach(x -> podatoci.add(delovi[x]));
        }else {
            throw new AmountNotAllowedException(suma);
        }
    }
    public int sum(){
        int suma=0;
        for(int i=0; i<podatoci.size(); i++){
            if(Character.isDigit(podatoci.get(i).charAt(0))){
                suma+=Integer.parseInt(podatoci.get(i));
            }
        }
        return suma;
    }
    public double danok(){
        double suma=0.00;
        for(int i=0; i<podatoci.size(); i+=2){
            if(podatoci.get(i+1).equals("A")){
                suma+=Double.parseDouble(podatoci.get(i))*0.18*0.15;
            }
            if(podatoci.get(i+1).equals("B")){
                suma+=Double.parseDouble(podatoci.get(i))*0.05*0.15;
            }
            if(podatoci.get(i+1).equals("V")){
                suma+=Double.parseDouble(podatoci.get(i))*0;
            }
        }
        return suma;
    }

    public String getIme() {
        return ime;
    }
}
class MojDDV{
    private List<Smetka> smetki;

    public MojDDV(){
        smetki=new ArrayList<>();
    }
    private static Smetka nanesi(String line){
        try {
            return new Smetka(line);
        }catch (AmountNotAllowedException e){
            System.out.println(e.poraka);
            return null;
        }
    }
    void readRecords (InputStream inputStream){
        BufferedReader vlez=new BufferedReader(new InputStreamReader(inputStream));
        smetki=vlez.lines().map(linija->nanesi(linija)).filter(i->i!=null && i.sum()<=30000).collect(Collectors.toList());
    }
    void printTaxReturns (OutputStream outputStream){
        PrintWriter izlez=new PrintWriter(outputStream);
        for(int i=0; i<smetki.size(); i++){
            izlez.println(String.format("%s %d %.2f", smetki.get(i).getIme(), smetki.get(i).sum(),
                    smetki.get(i).danok()));
        }
        izlez.flush();
    }
}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}