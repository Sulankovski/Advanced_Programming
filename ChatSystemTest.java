import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        try {
                            m.invoke(cs,params);
                        }catch (Exception e){

                        }
                    }
                }
            }
        }
    }

}

class ChatRoom{
    private String ime;
    private Set<String> imina_korisnici;
    public ChatRoom(String name){
        ime=name;
        imina_korisnici=new TreeSet<>();
    }
    public void addUser(String username){
        imina_korisnici.add(username);
    }
    public void removeUser(String username){
        imina_korisnici.remove(username);
    }
    public String toString(){
        StringBuilder tmp=new StringBuilder();
        tmp.append(ime);
        tmp.append("\n");
        if(imina_korisnici.isEmpty()){
            tmp.append("EMPTY").append("\n");
        }else {
            imina_korisnici.forEach(x->{tmp.append(x);tmp.append("\n");});
        }
        return tmp.toString();
    }
    public boolean hasUser(String username){
        return imina_korisnici.contains(username);
    }
    public int numUsers(){
        return imina_korisnici.size();
    }

    public String getIme() {
        return ime;
    }
}
class ChatSystem{
    Set<String> site_korisnici;
    Map<String,ChatRoom> lista_sobi;

    public ChatSystem() {
        lista_sobi=new TreeMap<>();
        site_korisnici=new HashSet<>();
    }
    public void addRoom(String roomName){
        ChatRoom nova=new ChatRoom(roomName);
        lista_sobi.put(roomName,nova);
    }
    public void removeRoom(String roomName){
        lista_sobi.remove(roomName);
    }
    public ChatRoom getRoom(String roomName){
        return lista_sobi.get(roomName);
    }
    public Comparator<ChatRoom> sporedkorisnici(){
        return Comparator.comparing(ChatRoom::numUsers);
    }
    public void register(String userName){
        site_korisnici.add(userName);
        String ime=lista_sobi.values().stream()
                .min(sporedkorisnici()).get().getIme();
        lista_sobi.get(ime).addUser(userName);
    }
    public void registerAndJoin(String userName, String roomName){
        site_korisnici.add(userName);
        lista_sobi.get(roomName).addUser(userName);
    }
    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(lista_sobi.containsKey(roomName)){
            if(site_korisnici.contains(userName)) {
                lista_sobi.get(roomName).addUser(userName);
            }else {
                throw new NoSuchUserException(userName);
            }
        }else {
            throw new NoSuchRoomException(roomName);
        }
    }
    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(lista_sobi.containsKey(roomName)){
            if(site_korisnici.contains(username)) {
                lista_sobi.get(roomName).removeUser(username);
            }else {
                throw new NoSuchUserException(username);
            }
        }else {
            throw new NoSuchRoomException(roomName);
        }
    }
    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if(site_korisnici.contains(friend_username)){
            lista_sobi.values().forEach(x->
            {
                if(x.hasUser(friend_username)){
                    x.addUser(username);
                }
            });
        }else{
            throw new NoSuchUserException(friend_username);
        }
    }
}
class NoSuchRoomException extends Exception{
        NoSuchRoomException(String roomName){
            super(roomName);
        }
}
class NoSuchUserException extends Exception{
    NoSuchUserException(String ime){
        super(ime);
    }
}