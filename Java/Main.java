package readability;


import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Main {

    private static double characters = 0;
    private static double words = 0;
    private static double sentences = 0;
    private static double syllables = 0;
    private static double polysyllables = 0;
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static Scanner scanner;
    private static double avAge;

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        scanner = new Scanner(file);
        df.setRoundingMode(RoundingMode.DOWN);

        printAllLines(file.getAbsolutePath());

        while (scanner.hasNextLine()) {
            StringBuilder in = new StringBuilder(scanner.nextLine());
            while (!(in.toString().endsWith(".") || in.toString().endsWith("?") || in.toString().endsWith("!"))) {
                if (scanner.hasNextLine()) {
                    in.append(" ").append(scanner.next());
                } else {
                    break;
                }
            }
            if (in.indexOf(" ") == 0) {
                in.delete(0, 1);
            }
            //Number of Sentences
            String[] sentencesArr = in.toString().split("[!\\?\\.]+ ?");
            sentences += sentencesArr.length;

            //Number of Words
            String[][] wordArr = new String[sentencesArr.length][];
            for (int i = 0; i < wordArr.length; i++) {
                wordArr[i] = (sentencesArr[i].split("[ \t\n]"));
            }
            for (String[] wordInSentence : wordArr) {
                words += wordInSentence.length;
                for (String word : wordInSentence) {
                    //Number of vowels and syllables
                    int vowels = 0;
                    for (int i = 0; i < word.length(); i++) {
                        //if(word.charAt(word.length()-1) != 'e') {
                        String s = word.substring(i);
                        //TODO
                        if (String.valueOf(s.charAt(0)).matches("[aeiouyAEIUOY]")) {
                            if (i < word.length()-1) {
                                vowels++;
                                if (String.valueOf(s.charAt(1)).matches("[aeiouy]")) {
                                    vowels--;
                                }
                            } else {
                                if (!String.valueOf(s.charAt(0)).matches("e")) {
                                    vowels++;
                                }
                            }
                        }
                    }
                    syllables += vowels != 0 ? vowels : 1;
                    if (vowels > 2) {
                       polysyllables++;
                    }
                }
            }

            for (String letters : in.toString().split("[ \t\n]")) {
                characters += letters.length();
            }


        }
        printStats();

    }

    public static String printReaders(double score) {

        //TODO number
        double tableScore = 14;
        int age = 24;
        while(tableScore > score) {

            if(tableScore == 13) {
                age -= 5;
            } else if (tableScore == 3) {
                age -= 2;
            } else {
                age--;
            }
            tableScore--;
        }
        avAge += (double)age;
        return "about " + age + "-year-olds";

    }

    public static List<String> printAllLines(String fileName) throws IOException {
        System.out.println("The text is:");
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        for (String line; (line = br.readLine()) != null; ) {
                System.out.println(line);
        }
        br.close();
        return null;
    }

    public static void printStats() {
        System.out.println();
        System.out.println("Words: " + (int)words);
        System.out.println("Sentences: " + (int)sentences);
        System.out.println("Characters: " + (int)characters);
        System.out.println("Syllables: " + (int)syllables);
        System.out.println("Polysyllables: " + (int)polysyllables);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String input = new Scanner(System.in).next();
        System.out.println();
        switch(input) {
            case "ARI":
                ARI();
                break;
            case "FK":
                FK();
                break;
            case "SMOG":
                SMOG();
                break;
            case "CL":
                CL();
                break;
            case "all":
                ARI();
                FK();
                SMOG();
                CL();
                System.out.printf("\nThis text should be understood in average by %.2f-year-olds.\n", avAge/4.0);
        }
        scanner.close();
    }

    public static void ARI() {
        String score = df.format(4.71 * (characters / words) + 0.5 * (words / sentences) - 21.43);
        System.out.printf("Automated Readability Index: %s (%s).\n", score, printReaders(Math.round(Double.parseDouble(score))));
    }

    public static void FK() {
        String score = df.format(0.39 * (words / sentences) + 11.8 * (syllables / words) - 15.59);
        System.out.printf("Flesch–Kincaid readability tests: %s (%s).\n", score, printReaders(Math.round(Double.parseDouble(score))));
    }

    public static void SMOG() {
        String score = df.format(1.043 * Math.sqrt(polysyllables * (30.0 / sentences)) + 3.1291);
        System.out.printf("Simple Measure of Gobbledygook: %s (%s).\n", score, printReaders(Math.round(Double.parseDouble(score))));
    }

    public static void CL() {
       double L = characters / (words / 100.0);
       double S = sentences / (words / 100.0);
       String score = df.format(0.0588 * L - 0.296 * S - 15.8);
       System.out.printf("Coleman–Liau index: %s (%s).\n", score, printReaders(Math.round(Double.parseDouble(score))));
    }
}
