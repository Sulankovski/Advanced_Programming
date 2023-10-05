import javax.swing.text.html.HTML;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

class InsufficientElementsException extends Exception{
    public InsufficientElementsException(String poraka){
        super(poraka);
    }
}
class InvalidRowNumberException extends Exception{
    public InvalidRowNumberException(String poraka){
        super(poraka);
    }
}
class InvalidColumnNumberException extends Exception{
    public InvalidColumnNumberException(String poraka){
        super(poraka);
    }
}
final class DoubleMatrix{
    private final double [][] matrica;
    private final int m;
    private final int n;

    public DoubleMatrix(double[] matrica, int m, int n) throws InsufficientElementsException{
        this.matrica=new double[m][n];
        this.m = m;
        this.n = n;
        if(m*n>matrica.length){
            throw new InsufficientElementsException("Insufficient number of elements");
        }
        else if(m*n<=matrica.length){
            int brojac= matrica.length-1;
            for(int i=m-1; i>=0; i--){
                for(int j=n-1; j>=0; j--){
                    this.matrica[i][j]=matrica[brojac];
                    brojac--;
                }
            }
        }
    }
    String getDimensions(){
        return String.format("[%d x %d]",m,n);
    }
    int rows(){
        return m;
    }
    int columns(){
        return n;
    }
    double maxElementAtRow(int row) throws InvalidRowNumberException {   //  ||
        if((row>0 && row<m) || row==m){
            double max = matrica[row-1][0];
            for (int j = 1; j < n; j++) {
                if (max < matrica[row-1][j]) {
                    max = matrica[row-1][j];
                }
            }
            return max;
        }else {
            throw new InvalidRowNumberException("Invalid row number");
        }
    }
    double maxElementAtColumn(int column) throws InvalidColumnNumberException{
        if((column>0 && column<n) || column==n) {
            double max = matrica[0][column-1];
            for (int i = 1; i < m; i++) {
                if (max < matrica[i][column-1]) {
                    max = matrica[i][column-1];
                }
            }
            return max;
        }else{
            throw new InvalidColumnNumberException("Invalid column number");
        }
    }

    double sum(){
        double suma=0;
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                suma+= matrica[i][j];
            }
        }
        return suma;
    }
    double[] toSortedArray(){
        double[] niza=new double[m*n];
        int brojac=0;
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                niza[brojac]=matrica[i][j];
                brojac++;
            }
        }
        double tmp=0;
        for(int i=0; i<brojac; i++){
            for(int j=i+1; j<brojac; j++){
                if(niza[i]<niza[j]){
                    tmp=niza[i];
                    niza[i]=niza[j];
                    niza[j]=tmp;
                }
            }
        }
        return niza;
    }

    @Override
    public String toString() {
        //return Arrays.deepToString(matrica);  //treba so sting bilder
        StringBuilder tmp=new StringBuilder();
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                if(j!=n-1){
                    tmp.append(String.format("%.2f\t",matrica[i][j]));
                }
                else{
                    tmp.append(String.format("%.2f",matrica[i][j]));
                }
            }
            if(i!=m-1) {
                tmp.append(System.lineSeparator());
            }
        }
        return tmp.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return m == that.m && n == that.n && Arrays.deepEquals(matrica, that.matrica);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(m, n);
        result = 31 * result + Arrays.deepHashCode(matrica);
        return result;
    }
}

class MatrixReader {
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner scanner = new Scanner(input);
        int m, n;
        m=scanner.nextInt();
        n=scanner.nextInt();
        double []niza=new double[n*m];
        for(int i=0; i<n*m; i++){
            niza[i]=scanner.nextDouble();
        }
        return new DoubleMatrix(niza, m, n);
    }
}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
