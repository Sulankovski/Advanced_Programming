import java.util.LinkedList;
import java.text.DecimalFormat;
import java.util.*;

abstract class Contact{
    private String date;
    public Contact(String date) {  // YYYY-MM-DD
        this.date = date;
    }

    public boolean isNewerThan(Contact c) {
        // convert Contact this to Date object
        int thisYear = Integer.parseInt(date.substring(0, 4));
        int thisMmonth = Integer.parseInt(date.substring(5, 7));
        int thisDay = Integer.parseInt(date.substring(8, 10));
        // convert Contact c to Date object
        int cYear = Integer.parseInt(c.date.substring(0, 4));
        int cMonth = Integer.parseInt(c.date.substring(5, 7));
        int cDay = Integer.parseInt(c.date.substring(8, 10));

        Date thisDate = new Date(thisYear, thisMmonth, thisDay);
        Date cDate = new Date(cYear, cMonth, cDay);

        return thisDate.compareTo(cDate) > 0;
    }
    abstract String getType();
}
class EmailContact extends Contact{
    private String email;

    public EmailContact(String datum, String email) {
        super(datum);
        this.email = email;
    }
    String getEmail(){
        return email;
    }
    @Override
    String getType(){
     return "Email";
    }

    @Override
    public String toString() {
        return email ;
    }
}
enum Operator{VIP, ONE, TMOBILE}
class PhoneContact extends Contact{
    private String phone;

    public PhoneContact(String datum, String phone) {
        super(datum);
        this.phone = phone;
    }
    String getPhone(){
        return phone;
    }
    Operator getOperator(){
        String[] pom= phone.split("/");
        char brojka = phone.charAt(2);
        int operator=Integer.parseInt(pom[0]);  //  ||
        if(operator%10==0 || operator%10==1 || operator%10==2 ){
            return Operator.TMOBILE;
        }else if(operator%10==5 || operator%10==6){
            return Operator.ONE;
        }else{
            return Operator.VIP;
        }
    }
    String getType(){
        return "Phone";
    }

    @Override
    public String toString() {
        return phone;
    }
}

class Student {
    private String firstName, lastName, city;
    private int age;
    private long index;
    private Contact[] kontakti;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        kontakti = new Contact[0];
    }

    public Contact[] getKontakti() {
        return kontakti;
    }

    void addEmailContact(String date, String email) {
        EmailContact nov = new EmailContact(date, email);
        Contact[] tmp = new Contact[kontakti.length + 1];
        for (int i = 0; i < kontakti.length; i++) {
            tmp[i] = kontakti[i];
        }
        tmp[kontakti.length] = nov;
        kontakti = tmp;
    }

    void addPhoneContact(String date, String phone) {
        PhoneContact nov = new PhoneContact(date, phone);
//        kontakti.add(nov);
        Contact[] tmp = new Contact[kontakti.length + 1];
        for (int i = 0; i < kontakti.length; i++) {
            tmp[i] = kontakti[i];
        }
        tmp[kontakti.length] = nov;
        kontakti = tmp;
    }

    Contact[] getEmailContacts() {
        int brojac_email_kontakti = 0;
        for (int i = 0; i < kontakti.length; i++) {
            if (kontakti[i].getType().equals("Email")) {
                brojac_email_kontakti++;
            }
        }
        Contact[] email_kontakti = new Contact[brojac_email_kontakti];
        int brojac = 0;
        for (int i = 0; i < kontakti.length; i++) {
            if (kontakti[i].getType().equals("Email")) {
                email_kontakti[brojac] = kontakti[i];
                brojac++;
            }
        }
        return email_kontakti;
    }

    Contact[] getPhoneContacts() {
        int brojac_phone_kontakti = 0;
        for (int i = 0; i < kontakti.length; i++) {
            if (kontakti[i].getType().equals("Phone")) {
                brojac_phone_kontakti++;
            }
        }
        Contact[] phone_kontakti = new Contact[brojac_phone_kontakti];
        int brojac = 0;
        for (int i = 0; i < kontakti.length; i++) {
            if (kontakti[i].getType().equals("Phone")) {
                phone_kontakti[brojac] = kontakti[i];
                brojac++;
            }
        }
        return phone_kontakti;
    }

    String getCity() {
        return city;
    }

    String getFullName() {
        return firstName + " " + lastName;
    }

    long getIndex() {
        return index;
    }

    Contact getLatestContact() {
        Contact najnov = kontakti[0];
        for (int i = 1; i < kontakti.length; i++) {
            if (kontakti[i].isNewerThan(najnov)) {
                najnov = kontakti[i];
            }
        }
        return najnov;
    }


    @Override
    public String toString() {
        return "{" +
                "ime:" + firstName +
                ", prezime:" + lastName +
                ", vozrast:" + age +
                ", grad:" + city +
                ", indeks:" + index +
                ", telefonskiKontakti:" + Arrays.toString(kontakti) +
                '}';
    }
}

class Faculty{
    Student[] students;
    String name;

    public Faculty(String name, Student[] students) {
        this.students = students;
        this.name = name;
    }
    int countStudentsFromCity(String cityName){
        int brojac=0;
        for(int i=0; i<students.length; i++){
            if(students[i].getCity().equals(cityName)){
                brojac++;
            }
        }
        return brojac;
    }
    Student getStudent(long index){
        int indeks=0;
        for(int i=0; i<students.length; i++){
            if(students[i].getIndex()==index){
                indeks=i;
            }
        }
        return students[indeks];
    }
    double getAverageNumberOfContacts(){
        double prosek=0;
        for(int i=0; i<students.length; i++){
            prosek+=students[i].getKontakti().length;
        }
        return prosek/(double) students.length;
    }
    Student getStudentWithMostContacts(){
        int najmnogu_kontakti=students[0].getKontakti().length;
        int najmnogu_indeks=0;
        for(int i=0; i<students.length; i++){
            if(najmnogu_kontakti<students[i].getKontakti().length){
                najmnogu_kontakti=students[i].getKontakti().length;
                najmnogu_indeks=i;
            }else if(najmnogu_kontakti==students[i].getKontakti().length){
                if(students[najmnogu_indeks].getIndex()<students[i].getIndex()){
                    najmnogu_kontakti=students[i].getKontakti().length;
                    najmnogu_indeks=i;
                }
            }
        }
        return students[najmnogu_indeks];
    }

    @Override
    public String toString() {
        return "fakultet:" + name + " studenti:" + Arrays.toString(students);
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city, age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
