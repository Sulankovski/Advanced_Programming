import java.awt.*;
import java.util.*;
import java.util.List;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable{
    void moveUp() throws ObjectCanNotBeMovedException ;
    void moveDown()throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();

    TYPE gettype();
}
class MovablePoint implements Movable{
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public TYPE gettype(){
        return TYPE.POINT;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(y+ySpeed<MovablesCollection.y_MAX){
            this.y=this.y+this.ySpeed;
        }else {
            throw new ObjectCanNotBeMovedException(x,y+ySpeed);
        }
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(y-ySpeed>0){
            this.y=this.y-this.ySpeed;
        }else {
            throw new ObjectCanNotBeMovedException(x,y-ySpeed);
        }
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(x+xSpeed<MovablesCollection.x_MAX){
            this.x=this.x+this.xSpeed;
        }else {
            throw new ObjectCanNotBeMovedException(x+xSpeed,y);
        }
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(x-xSpeed>0){
            this.x=this.x-this.xSpeed;
        }else {
            throw new ObjectCanNotBeMovedException(x-xSpeed,y);
        }
    }

    @Override
    public int getCurrentXPosition() {
        return this.x;
    }

    @Override
    public int getCurrentYPosition() {
        return this.y;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)",getCurrentXPosition(),getCurrentYPosition());
    }
}

class MovableCircle implements Movable{
    private int radius;
    private MovablePoint centar;

    public MovableCircle(int radius, MovablePoint centar) {
        this.radius = radius;
        this.centar = centar;
    }

    @Override
    public TYPE gettype(){
        return TYPE.CIRCLE;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        centar.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        centar.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        centar.moveRight();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        centar.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return centar.getCurrentXPosition();
    }

    public int getRadius(){
        return radius;
    }

    @Override
    public int getCurrentYPosition() {
        return centar.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d)",centar.getCurrentXPosition(),centar.getCurrentYPosition());
    }
}

class ObjectCanNotBeMovedException extends Exception{
    public String poraka;
    public ObjectCanNotBeMovedException(int x, int y){
        super(String.format("Point (%d,%d) is out of bounds",x,y));
        poraka=String.format("Point (%d,%d) is out of bounds",x,y);
    }
}

class MovableObjectNotFittableException extends Exception{
    public String poraka;
    public MovableObjectNotFittableException(int x, int y){
        super(String.format("Point (%d,%d) is out of bounds",x,y));
        poraka=String.format("Point (%d,%d) is out of bounds",x,y);
    }
    public MovableObjectNotFittableException(int x, int y, int r){
        super(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",x,y,r));
        poraka=String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",x,y,r);
    }
}

class MovablesCollection{
    private Movable[] movable;
    static int x_MAX=0;
    static int y_MAX=0;

    static void setxMax(int x){
        x_MAX=x;
    }
    static void setyMax(int y){
        y_MAX=y;
    }

    public MovablesCollection(int prva, int vtora){
        setxMax(prva);
        setyMax(vtora);
        this.movable=new Movable[0];
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (m.gettype().equals(TYPE.POINT)){
            MovablePoint nova=(MovablePoint)m;
            int x=nova.getCurrentXPosition();
            int y= nova.getCurrentYPosition();
            if((0>=x && x<=x_MAX) && (0>=y && y<=y_MAX)){
                dodavane(m);
            }else {
                throw new MovableObjectNotFittableException(x,y);
            }
        }else if(m.gettype().equals(TYPE.CIRCLE)){
            MovableCircle nova=(MovableCircle)m;
            int x= nova.getCurrentXPosition();
            int y=nova.getCurrentYPosition();
            int radius=nova.getRadius();
            if((0>=x-radius && x+radius<=x_MAX) && (0>=y-radius && y+radius<=y_MAX)){
                dodavane(m);
            }else {
                throw new MovableObjectNotFittableException(x,y,radius);
            }
        }
    }
    private void dodavane(Movable nova){
        List<Movable> tmp=new ArrayList<>();
        Collections.addAll(tmp, this.movable);
        tmp.add(nova);
        this.movable=new Movable[tmp.size()];
        for(int i=0; i<tmp.size(); i++){
            this.movable[i]=tmp.get(i);
        }
    }

    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for(int i=0; i<this.movable.length; i++){
            if(this.movable[i].gettype().equals(type)){
                if(direction.equals(DIRECTION.UP)){
                    movable[i].moveUp();
                }else if(direction.equals(DIRECTION.RIGHT)){
                    movable[i].moveRight();
                }else if(direction.equals(DIRECTION.DOWN)){
                    movable[i].moveDown();
                }else if(direction.equals(DIRECTION.LEFT)){
                    movable[i].moveLeft();
                }
            }
        }
    }

    @Override
    public String toString() {
        String string = String.format("Collection of movable objects with size %d:\n",movable.length);
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append(string);
        for (int i=0; i<movable.length; i++) {
            if(this.movable[i].gettype().equals(TYPE.POINT)) {
                MovablePoint novo = (MovablePoint) movable[i];
                stringBuilder.append(novo.toString());
            }
            else if (this.movable[i].gettype().equals(TYPE.CIRCLE)) {
                MovableCircle novo = (MovableCircle) movable[i];
                stringBuilder.append(novo.toString());
            }
        }
        return stringBuilder.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) throws MovableObjectNotFittableException, ObjectCanNotBeMovedException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                }catch (MovableObjectNotFittableException e){
                    System.out.println(e.poraka);
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }catch (MovableObjectNotFittableException e){
                    System.out.println(e.poraka);
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        }catch (ObjectCanNotBeMovedException e){
            System.out.println(e.poraka);
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        }catch (ObjectCanNotBeMovedException e){
            System.out.println(e.poraka);
        }
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        }catch (ObjectCanNotBeMovedException e){
            System.out.println(e.poraka);
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        }catch (ObjectCanNotBeMovedException e){
            System.out.println(e.poraka);
        }
        System.out.println(collection.toString());
    }
}
