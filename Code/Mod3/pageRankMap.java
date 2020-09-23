import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.net.URI;

// Hadoop imports
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class pageRankMap
         extends Mapper<Object, Text, Text, IntWritable> {
             
             PriorityQueue<String> linksQueue = new PriorityQueue<>();
             float rankOfPage;
             float outputRank;
             String page = "none";
             
      @Override
      public void map(Object key, Text value, Context context
                      ) throws IOException, InterruptedException {
        //input will be line# as key and line as value

        String line = value.toString();
        String[] splitLine = line.split("\\s");
        
        for ( String elemInLine : splitLine ) {

          if (page.equals("none")) {
            // is page
            page = elemInLine;
          } else if ( elemInLine.matches("^-?\\d*\\.{0,1}\\d$")) {
            // is rank
            rankOfPage = Float.parseFloat(elemInLine);
            outputRank = rankOfPage / linksQueue.size();
          } else {
            // is link
            linksQueue.add(elemInLine);
          }
        }
        
        while (linksQueue.size() > 0) {
          context.write(page, linksQueue.peek());
          context.write(linksQueue.poll(), outputRank);
        }
        
      }
    }