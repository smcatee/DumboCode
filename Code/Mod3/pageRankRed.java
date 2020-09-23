import java.io.IOException;
import java.util.*;
import java.net.URI;

// Hadoop imports
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class pageRankRed
         extends Reducer<Text,IntWritable,Text,IntWritable> {
           
           Queue<String> linksQueue = new PriorityQueue<>();
           String line = values.toString();
           String[] splitLine = line.split("\\s");
           float rankOfPage = 0;
           String outputValue = new String();
  
      public void reduce(Text key, Text values,   // values will come in as arrays
                         Context context
                         ) throws IOException, InterruptedException {
        //input will be pages as keys and values as a list of links and ranks

        for ( String elemInLine : splitLine ) {

          if ( elemInLine.matches("^-?\\d*\\.{0,1}\\d$")) {
            // is rank
            rankOfPage += Float.parseFloat(elemInLine);
          } else {
            // is link
            linksQueue.offer(elemInLine);
          }
        }
        while (linksQueue.size() > 0) {
          // create the string that will be output as value
          outputValue = outputValue + " " + linksQueue.poll();
        }
        // append on rank to output thing
        outputValue =  outputValue + " " + rankOfPage;
        context.write(key, outputValue);
      }
    }