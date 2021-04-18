package processor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class NumericMatrixProcessor {

    public static double[][] a;
    public static double[][] b;
    public static double c;
    public static int option;
    public static double determinant;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            option = scanner.nextInt();

            switch (option) {
                case 1:
                case 3:
                    scanTwo();
                    printRes();
                    break;
                case 2:
                    scanOne();
                    printRes();
                    break;
                case 4:
                    printTransposeMenu();
                    printRes();
                    break;
                case 5:
                    scanOne();
                    determine(a, a.length);
                    printRes();
                    break;
                case 6:
                    scanOne();
                    inverse();
                    printRes();
                    break;
                case 0:
                    return;
                default:
                    break;
            }
        }
    }


    public static void printMenu() {
        System.out.println("1. Add matrices");
        System.out.println("2. Multiply matrix by a constant");
        System.out.println("3. Multiply matrices");
        System.out.println("4. Transpose matrices");
        System.out.println("5. Calculate a determinant");
        System.out.println("6. Inverse matrix");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    public static void printTransposeMenu() {
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.print("Your choice: ");
        transpose(-1);
    }

    public static void scanTwo() {
        System.out.print("Enter size of first matrix: ");
        a = new double[scanner.nextInt()][scanner.nextInt()];
        System.out.println("Enter first matrix: ");
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = scanner.nextDouble();
            }
        }
        System.out.print("Enter size of second matrix: ");
        b = new double[scanner.nextInt()][scanner.nextInt()];
        System.out.println("Enter second matrix: ");
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                b[i][j] = scanner.nextDouble();
            }
        }
    }

    public static void scanOne() {
        System.out.print("Enter size of matrix: ");
        a = new double[scanner.nextInt()][scanner.nextInt()];
        System.out.println("Enter matrix: ");
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = scanner.nextDouble();
            }
        }
        if (option == 2) {
            System.out.print("Enter constant: ");
            c = scanner.nextDouble();
        }
    }

    public static void transpose(int opt) {
        if(opt == -1) {
            opt = scanner.nextInt();
            scanOne();
        }
        double[][] old = a.clone();

        if (opt == 1) {
            a = new double[a[0].length][a.length];
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    a[i][j] = old[j][i];
                }
            }
        } else if (opt == 2) {
            a = new double[a[0].length][a.length];

            int oldJ = old[old.length - 1].length - 1;
            for (int i = 0; i < a.length; i++) {
                int oldI = old.length - 1;
                for (int j = 0; j < a[i].length; j++) {
                    a[i][j] = old[oldI][oldJ];
                    oldI--;
                }
                oldJ--;
            }
        } else if (opt == 3) {
            a = new double[a.length][a[0].length];
            int split = a[0].length / 2;
            for (int i = 0; i < a.length; i++) {
                int oldJ = a[i].length - 1;
                for (int j = 0; j < split; j++) {
                    a[i][j] = old[i][oldJ];
                    oldJ--;
                }
                oldJ = 0;
                for (int j = a[0].length - 1; j > split - 1; j--) {
                    a[i][j] = old[i][oldJ];
                    oldJ++;
                }
            }
        } else if (opt == 4) {
            a = new double[a.length][a[0].length];
            int split = a.length / 2;
            for (int i = 0; i < a.length; i++) {
                int oldI = a.length - 1;
                for (int j = 0; j < split; j++) {
                    a[j][i] = old[oldI][i];
                    oldI--;
                }
                oldI = 0;
                for (int j = a.length - 1; j > split - 1; j--) {
                    a[j][i] = old[oldI][i];
                    oldI++;
                }
            }
        }
    }

    public static double determine(double[][] matrix, int n) {
        determinant = 0;
        if (matrix[0].length == 2 && matrix.length == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
        }
        int sign = 1;
        for (int i = 0; i < n; i++) {
            determinant += sign * matrix[i][0] * determine(getMinor(matrix, i, 0), n-1);
            sign = -sign;
        }
        return determinant;
    }

    public static double[][] getMinor(double[][] matrix, int i, int j) {
        double[][] minor = new double[matrix.length - 1][matrix[0].length - 1];
        int minorI = 0;
        for (int k = 0; k < matrix.length; k++) {
            int minorJ = 0;
            for (int l = 0; l < matrix[k].length; l++) {
                if (k != i && l != j) {
                    minor[minorI][minorJ] = matrix[k][l];
                    minorJ++;
                    if(minorJ == matrix[k].length-1) {
                        minorJ = 0;
                        minorI++;
                    }
                }
            }
        }
        return minor;
    }

    public static void inverse() {
        double detA = determine(a, a[0].length);
        transpose(1);
        double[][] temp = a.clone();
        a = new double[temp.length][temp[0].length];
        cofactor(temp);
        option = (detA != 0) ? 2 : -1;

        c = 1.0 / detA;
    }

    public static void cofactor(double[][] matrix) {
        int sign = 1;
        int signOld = -1;
        for (int i = 0; i < matrix.length; i++) {
            sign = -signOld;
            signOld = sign;
            for (int j = 0; j < matrix[i].length; j++) {
                double[][] min;
                if (matrix.length > 2) {
                    min = getMinor(matrix, i, j);
                } else {
                    min = matrix;
                }
                a[i][j] = sign * determine(min, matrix.length - 1);
                sign = -sign;
            }
        }
    }

    public static void printRes() {
        System.out.println("The result is:");
        if (option == 3) {
            if (a[0].length == b.length) {
                for (double[] doubles : a) {
                    for (int j = 0; j < b[0].length; j++) {
                        double sum = 0;
                        for (int k = 0; k < a[0].length; k++) {
                            sum += doubles[k] * b[k][j];
                        }
                        if (sum - (int) sum == 0) {
                            int val = (int) sum;
                            System.out.printf("%d ", val);
                        } else {
                            System.out.printf("%.2f ", sum);
                        }
                    }
                    System.out.println();
                }
            } else {
                System.out.println("The operation cannot be performed");
            }
        } else if (option == 1) {
            if (a.length == b.length && a[0].length == b[0].length) {
                for (int i = 0; i < b.length; i++) {
                    for (int j = 0; j < b[i].length; j++) {
                        double sum = a[i][j] + b[i][j];
                        if (sum - (int) sum == 0) {
                            int val = (int) sum;
                            System.out.printf("%d ", val);
                        } else {
                            System.out.printf("%.2f ", sum);
                        }
                    }
                    System.out.println();
                }
            } else {
                System.out.println("The operation cannot be performed");
            }

        } else if (option == 2) {
            for (double[] m : a) {
                for (double n : m) {
                    double prod = n * c;
                    if (prod - (int) prod == 0) {
                        int val = (int) prod;
                        System.out.printf("%d ", val);
                    } else {
                        DecimalFormat df = new DecimalFormat("#.##");
                        df.setRoundingMode(RoundingMode.DOWN);
                        System.out.printf("%s ", df.format(prod));
                    }
                }
                System.out.println();
            }
        } else if (option == 5) {
            if (determinant - (int) determinant == 0) {
                int val = (int) determinant;
                System.out.printf("%d\n", val);
            } else {
                System.out.printf("%.2f\n", determinant);
            }
        } else if (option == -1) {
            System.out.println("This matrix doesn't have an inverse");
        } else {
            for (double[] m : a) {
                for (double n : m) {
                    if (n - (int) n == 0) {
                        int val = (int) n;
                        System.out.printf("%d ", val);
                    } else {
                        System.out.printf("%.2f ", n);
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }
}
