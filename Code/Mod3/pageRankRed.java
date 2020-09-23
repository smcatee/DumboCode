import java.io.IOException;
import java.util.PriorityQueue;

// Hadoop imports
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class pageRankRed
         extends Reducer<Text,IntWritable,Text,IntWritable> {
           
           PriorityQueue<String> linksQueue = new PriorityQueue<>();
           float rankOfPage = 0;
           String outputValue = new String();
           
           public void reduce(Text key, Text values,   // values will come in as arrays
           Context context
           ) throws IOException, InterruptedException {
               //input will be pages as keys and values as a list of links and ranks
               String line = values.toString();
               String[] splitLine = line.split("\\s");

        for ( String elemInLine : splitLine ) {

          if ( elemInLine.matches("^-?\\d*\\.{0,1}\\d$")) {
            // is rank
            rankOfPage += Float.parseFloat(elemInLine);
          } else {
            // is link
            linksQueue.add(elemInLine);
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