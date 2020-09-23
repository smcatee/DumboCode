import java.io.IOException;
import java.util.PriorityQueue;

// Hadoop imports
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;




//~~~~~~~~~~~~~~~~~~~~~~~~ CLASS FOR MAPPER, REDUCER, JOB CONFIG ~~~~~~~~~~~~~~~~~~~~~~~~

public class pageRank {

    //~~~~~~~~~~~~~~~~~~~~~~~~ MAPPER ~~~~~~~~~~~~~~~~~~~~~~~~

    public static class pageRankMapper extends Mapper<LongWritable, Text, Text, Text> {
      @Override
      public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] splitLine = line.split("\\s");
        PriorityQueue<String> linksQueue = new PriorityQueue<>(); //maybe problem is with this not having correct import. or just use a list
        Double outputRank;
        String page = "none";

        // the key is ignored
        // in value the first element is the page, the next elements are links, the final element is a rank
        // save the first element as page
        // save the links in a queue
        // save the rank as outputRank
        for ( String elemInLine : splitLine ) {
          if (page.equals("none")) {
            // is page
            page = elemInLine;
            
          } else if ( elemInLine.matches("\\d*\\.{0,1}\\d")) {
            // is rank
            outputRank = Double.parseDouble(elemInLine);
          } else {
            // is link
            linksQueue.add(elemInLine);
          }
        }
        
        // the proportional rank to give to each link
        outputRank = outputRank / linksQueue.size();

        // for each link output key=page and value=link
        // for each link output key=link and value=rank
        while (linksQueue.size() > 0) {
          context.write(new Text(page), new Text(linksQueue.peek().trim()));
          context.write(new Text(linksQueue.poll().trim()), new Text(outputRank.toString().trim()));
        }
        
      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ REDUCER ~~~~~~~~~~~~~~~~~~~~~~~~
  
    public static class pageRankReducer extends Reducer<Text,Text,Text,Text> {
      public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        PriorityQueue<String> linksQueue = new PriorityQueue<>();
        Double rankOfPage = Double.valueOf(0.0);
        String outputValue = "";
        
        // the same key will be output
        // the same values will be output, but if they are numeric they will be added together
        for ( Text elemInLine : values ) {
          if ( elemInLine.toString().matches("^\\d*\\.{0,1}\\d$")) {
            // is rank
            rankOfPage += Double.parseDouble(elemInLine);
          } else {
            // is link
            linksQueue.add(elemInLine.toString());
          }
        }

        outputValue = linksQueue.toString() + " " + rankOfPage.toString();
        context.write(key, new Text(outputValue));
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
      //job.setCombinerClass(pageRankReducer.class);
      job.setReducerClass(pageRankReducer.class);

      //job.setMapOutputKeyClass(Text.class);
      //job.setMapOutputValueClass(Text.class);

      job.setNumReduceTasks(1);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);
  
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));
  
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
  }