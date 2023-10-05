import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Contact implements Comparator<String> {
    private String ime;
    private List<String> broevi=new ArrayList<>();
    public Contact(String ime, String...broj) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if(validno_ime(ime)){
            for(int i=0; i<broj.length; i++){
                if(validen_broj(broj[i])){
                    this.ime=ime;
                    if(broevi.size()<5) {
                        broevi.add(broj[i]);
                    }else {
                        throw new MaximumSizeExceddedException();
                    }
                }else {
                    throw new InvalidNumberException();
                }
            }
        }else {
            throw new InvalidNameException(ime);
        }
    }
    private boolean validen_broj(String broj) throws InvalidNumberException {
        char []proverka=broj.toCharArray();
        String []operatori={"070","071","072","075","076","077","078"};
        String operator=broj.substring(0,3);
        int f=1;
        if(proverka.length!=9){
            f=0;
        }else {
            for(int i=0; i<proverka.length; i++){
                if(!Character.isDigit(proverka[i])){
                    f=0;
                    break;
                }
            }
            if(!(operatori[0].equals(operator)) && !(operatori[1].equals(operator)) && !(operatori[2].equals(operator))
                    && !(operatori[3].equals(operator)) && !(operatori[4].equals(operator))
                    && !(operatori[5].equals(operator)) && !(operatori[6].equals(operator))){
                f=0;
            }
        }

        if(f==0){
            throw new InvalidNumberException();
        }else {
            return true;
        }
    }
    private boolean validno_ime(String ime) throws InvalidNameException {
        if (ime.length() > 4 && ime.length() <= 10 && ime.matches("\\w+"))
            return true;
        throw new InvalidNameException(ime);
    }
    public String getName(){
        return ime;
    }
    private String[] sort(List<String> niza){
        String []pom=new String[niza.size()];
        niza=niza.stream().sorted().collect(Collectors.toList());
        for(int i=0; i<niza.size(); i++){
            pom[i]=niza.get(i);
        }
        return pom;
    }
    public String[] getNumbers(){
        return sort(broevi);
    }
    public void addNumber(String phonenumber) throws MaximumSizeExceddedException, InvalidNumberException{
        if(validen_broj(phonenumber)){
            if(broevi.size()<5) {
                broevi.add(phonenumber);
            }else {
                throw new MaximumSizeExceddedException();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder tmp=new StringBuilder();
        tmp.append(ime);
        tmp.append("\n");
        tmp.append(broevi.size());
        tmp.append("\n");
        sort(broevi);
        for(int i=0; i<broevi.size(); i++){
            tmp.append(broevi.get(i));
            tmp.append("\n");
        }
        return tmp.toString();
    }
    public static Contact valueOf(String s) throws InvalidFormatException, InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        Contact nov = null;
        char []niza=s.toCharArray();
        int prv_index=0, golemina=s.length(), brojac=0, f=1, n=1;
        for(int i=0; i<niza.length; i++){
            brojac++;
            if(Character.isDigit(niza[i])){
                prv_index=i;
                break;
            }else {
                throw new InvalidFormatException();
            }
        }
        String ime=s.substring(0,prv_index);        //za ime
        if((golemina-brojac)%9==0){
            for(int i=brojac; i<niza.length; i+=9){
                String tmp=s.substring(brojac,brojac+9);
                if(f==1){
                    nov=new Contact(ime,tmp);
                    f=0;
                }
                if(n!=1){
                    nov.addNumber(tmp);
                }
                n--;
            }
        }else {
            throw new InvalidFormatException();
        }
        return nov;
    }

    @Override
    public int compare(String o1, String o2) {
        char c1, c2;
        for(int i=0; i<o1.length(); i++){
            c1=o1.charAt(i);
            c2=o2.charAt(i);
            if(c1<c2){
                return -1;
            }
        }
        return 1;
    }
}
class InvalidFormatException extends Exception{
    public InvalidFormatException(){

    }
}
class InvalidNameException extends Exception{
    public String name;
    public InvalidNameException(String ime){
        super("Invalid name exception");
        this.name=ime;
    }
}
class InvalidNumberException extends Exception{
    public InvalidNumberException(){

    }
}
class MaximumSizeExceddedException extends Exception{
    public MaximumSizeExceddedException(){

    }
}

class PhoneBook{
    private Contact[] imenik=new Contact[0];
    public PhoneBook() {
    }
    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        List<Contact> novi=new ArrayList<>();
        for(int i=0; i<imenik.length; i++){
            novi.add(imenik[i]);
        }
        if(novi.size()<250) {
            if(!novi.contains(contact)) {
                novi.add(contact);
            }else {
                throw new InvalidNameException(contact.getName());
            }
        }else {
            throw new MaximumSizeExceddedException();
        }
        imenik=new Contact[novi.size()];
        for(int i=0; i<novi.size(); i++){
            imenik[i]=novi.get(i);
        }
    }
    public Contact getContactForName(String name){
        int indeks=-1;
        for (int i=0; i<imenik.length; i++){
            if(name.equals(imenik[i].getName())){
                indeks=i;
            }
        }
        if(indeks==-1){
            return null;
        }else {
            return imenik[indeks];
        }
    }
    public int numberOfContacts(){
        return imenik.length;
    }
    public Contact[] getContacts(){
        Contact[] tmp = new Contact[imenik.length];
        System.arraycopy(imenik, 0, tmp, 0, imenik.length);
        Arrays.sort(imenik);
        Arrays.sort(tmp);
        return tmp;
    }
    public boolean removeContact(String name){
        List<Contact> tmp=new ArrayList<>();
        for(int i=0; i<imenik.length; i++){
            tmp.add(imenik[i]);
        }
        if(tmp.contains(name)) {
            imenik = new Contact[tmp.size()];
            for (int i = 0; i < imenik.length; i++) {
                imenik[i] = tmp.get(i);
            }
            return true;
        }else {
            return false;
        }
    }
    @Override
    public String toString() {
        StringBuilder tmp=new StringBuilder();
        for(int i=0; i<imenik.length; i++){
            tmp.append(imenik[i].toString());
            tmp.append("\n");
        }
        return tmp.toString();
    }
    public static boolean saveAsTextFile(PhoneBook phonebook, String path) {
        File file = new File(path);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(phonebook);
            out.close();
        } catch (FileNotFoundException e) {
            try {
                if (file.createNewFile()) {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                    out.writeObject(phonebook);
                    out.close();
                }
            } catch (IOException e1) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static PhoneBook loadFromTextFile(String path) throws IOException, InvalidFormatException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        PhoneBook pb = null;
        try {
            pb = (PhoneBook) in.readObject();
            in.close();
        } catch (ClassNotFoundException e) {
            throw new InvalidFormatException();
        }
        return pb;
    }
    public Contact[] getContactsForNumber(String number_prefix) {
        int counter = 0;
        for (int i = 0; i < getContacts().length; ++i) {
            for (int j = 0; j < getContacts()[i].getNumbers().length; ++j) {
                String prefix = getContacts()[i].getNumbers()[j].substring(0, 3);
                if (prefix.equals(number_prefix)) {
                    ++counter;
                    break;
                }
            }
        }
        Contact[] contacts = new Contact[counter];
        for (int i = 0, k = 0; i < getContacts().length; ++i) {
            for (int j = 0; j < getContacts()[i].getNumbers().length; ++j) {
                String prefix = getContacts()[i].getNumbers()[j].substring(0, 3);
                if (prefix.equals(number_prefix)) {
                    contacts[k++] = this.imenik[i];
                    break;
                }
            }
        }
        return contacts;
    }

}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
