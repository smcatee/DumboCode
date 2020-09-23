import java.io.*;
import java.util.*;
import java.net.URI;

// Hadoop imports
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;




//~~~~~~~~~~~~~~~~~~~~~~~~ CLASS FOR MAPPER, REDUCER, JOB CONFIG ~~~~~~~~~~~~~~~~~~~~~~~~

public class pageRank {

    //~~~~~~~~~~~~~~~~~~~~~~~~ MAPPER ~~~~~~~~~~~~~~~~~~~~~~~~

    public static class pageRankMapper
         extends Mapper<Object, Text, Text, IntWritable> {
  
      private Configuration conf;
  
      @Override
      public void map(Object key, Text value, Context context
                      ) throws IOException, InterruptedException {
        //input will be line# as key and line as value

        String line = value.toString();
        String[] splitLine = line.split("\\s");
        Queue<Character> linksQueue = new PriorityQueue<>();
        float rankOfPage;
        Text page = new Text("none");
        
        for ( String elemInLine : splitLine ) {

          if (page.equals("none")) {
            // is page
            page = elemInLine;
          } else if ( elemInLine.matches("^-?\d*\.{0,1}\d$")) {
            // is rank
            rankOfPage = parseFloat(elemInLine);
            outputRank = rankOfPage / linksQueue.size();
          } else {
            // is link
            linksQueue.offer(elemInLine);
          }
        }
        
        while (linksQueue.size() > 0) {
          context.write(page, linksQueue.peek());
          context.write(linksQueue.poll(), outputRank);
        }
        
      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ REDUCER ~~~~~~~~~~~~~~~~~~~~~~~~
  
    public static class pageRankReducer
         extends Reducer<Text,IntWritable,Text,IntWritable> {
      private IntWritable result = new IntWritable();
  
      public void reduce(Text key, Text values,   // values will come in as arrays
                         Context context
                         ) throws IOException, InterruptedException {
        //input will be pages as keys and values as a list of links and ranks
        Queue<Character> linksQueue = new PriorityQueue<>();
        String line = value.toString();
        String[] splitLine = line.split("\\s");
        Queue<Character> linksQueue = new PriorityQueue<>();
        float rankOfPage = 0;
        String outputValue;

        for ( String elemInLine : splitLine ) {

          if ( elemInLine.matches("^-?\d*\.{0,1}\d$")) {
            // is rank
            rankOfPage += parseFloat(elemInLine);
          } else {
            // is link
            linksQueue.offer(elemInLine);
          }
        }
        while (linksQueue.size() > 0) {
          // create the string that will be output as value
          ahhhhhhoutputthing = ahhhhhhoutputthing + " " + linksQueue.poll();
        }
        // append on rank to output thing
        ahhhhhhoutputthing =  ahhhhhhoutputthing + " " + rankOfPage;
        context.write(key, outputTHING);
      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ JOB CONFIG ~~~~~~~~~~~~~~~~~~~~~~~~

    public static void main(String[] args) throws Exception {
      if (args.length != 2) {
        System.err.println("Usage: <in> <out>");
        System.exit(2);
      }

      Job job = new Job();
      job.setJobName("page rank");
      // keep in mind, the shuffle/reduce phase could be a complex bottleneck from designated memory(buffer)/storage(disk)/segments, these can be modified

      job.setJarByClass(pageRank.class);
      job.setMapperClass(pageRankMapper.class);
      job.setCombinerClass(pageRankReducer.class);
      job.setReducerClass(pageRankReducer.class);

      job.setNumReduceTasks(1);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);
  
      FileInputFormat.addInputPath(job, new Path(args[0])); // .gz files can be submitted, but each file is processed by a single mapper
      FileOutputFormat.setOutputPath(job, new Path(args[1]));
  
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
  }