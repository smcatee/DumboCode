import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * input file and search term list
 * counts the lines containing that term (not case sensitive)
 * output line for each search term containing: <search term> <count>
 */


public class wordCount {
    public static void main( String[] args )
    throws IOException {
    
    final FileInputStream inputFile = new FileInputStream(args[0]);
    final String[] searchTerms = Arrays.copyOfRange(args, 1, args.length);
    int[] searchCounts = new int[searchTerms.length];

    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile));

    String line = reader.readLine();
    while(line != null) {

        for (int i = 0; i < searchTerms.length; i++) {
            searchCounts[i] += ( line.toLowerCase().indexOf(searchTerms[i].toLowerCase()) > -1 ) ? 1 : 0;
        }
        line = reader.readLine();
    }
    reader.close();
    inputFile.close();

    for (int i = 0; i < searchTerms.length; i++) {
        System.out.println(searchTerms[i] + " " + searchCounts[i]);
        }
    }
}
