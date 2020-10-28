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

public class Causal_Relation_Extractor {

    //~~~~~~~~~~~~~~~~~~~~~~~~ MAPPER ~~~~~~~~~~~~~~~~~~~~~~~~

    public static class Causal_RelationMapper extends Mapper<LongWritable, Text, Text, Text> {
      @Override
      public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // variables

        // access text abstract
        // get easily available attributes, comment out so they are accessible later
        //    keywords

        // run NLP causal relation extractor
        //    use article key words to train causality extractor
        // save syntactic structure map as a side output file, maybe skip to this during iterations of NLP programs

        // for each causal term found
            //context.write(key=causalTerm, value=[searchTerm, confidence, UID, <other attributes>]);
        
      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ REDUCER ~~~~~~~~~~~~~~~~~~~~~~~~
  
    public static class Causal_RelationReducer extends Reducer<Text,Text,Text,Text> {
      public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        
        //context.write(key, value);
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

      job.setJarByClass(Causal_Relation_Extractor.class);
      job.setMapperClass(Causal_RelationMapper.class);
      //job.setCombinerClass(Causal_RelationReducer.class);
      job.setReducerClass(Causal_RelationReducer.class);

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