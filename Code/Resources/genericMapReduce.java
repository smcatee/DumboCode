/**
 * Based on WordCount2 from apache tutorial, plus additions as encountered
 * https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Overview
 */



//~~~~~~~~~~~~~~~~~~~~~~~~ COMMON IMPORTS ~~~~~~~~~~~~~~~~~~~~~~~~
// Java imports
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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

public class WordCount2 {

    //~~~~~~~~~~~~~~~~~~~~~~~~ MAPPER ~~~~~~~~~~~~~~~~~~~~~~~~

    public static class TokenizerMapper //might not compile with 'static' modifier
         extends Mapper<Object, Text, Text, IntWritable>{ //<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
  
      static enum CountersEnum { INPUT_WORDS }
  
      private final static IntWritable one = new IntWritable(1);
      private Text word = new Text();
  
      private boolean caseSensitive;
      private Set<String> patternsToSkip = new HashSet<String>();
  
      private Configuration conf;
      private BufferedReader fis;
  
      @Override
      public void setup(Context context) throws IOException,
          InterruptedException {
        conf = context.getConfiguration();
        caseSensitive = conf.getBoolean("wordcount.case.sensitive", true);
        if (conf.getBoolean("wordcount.skip.patterns", false)) {
          URI[] patternsURIs = Job.getInstance(conf).getCacheFiles();
          for (URI patternsURI : patternsURIs) {
            Path patternsPath = new Path(patternsURI.getPath());
            String patternsFileName = patternsPath.getName().toString();
            parseSkipFile(patternsFileName);
          }
        }
      }
  
      private void parseSkipFile(String fileName) {
        try {
          fis = new BufferedReader(new FileReader(fileName));
          String pattern = null;
          while ((pattern = fis.readLine()) != null) {
            patternsToSkip.add(pattern);
          }
        } catch (IOException ioe) {
          System.err.println("Caught exception while parsing the cached file '"
              + StringUtils.stringifyException(ioe));
        }
      }
  
      @Override
      public void map(Object key, Text value, Context context
                      ) throws IOException, InterruptedException {
        String line = (caseSensitive) ?
            value.toString() : value.toString().toLowerCase();
        for (String pattern : patternsToSkip) {
          line = line.replaceAll(pattern, "");
        }
        StringTokenizer itr = new StringTokenizer(line);
        while (itr.hasMoreTokens()) {
          word.set(itr.nextToken());
          context.write(word, one);
          Counter counter = context.getCounter(CountersEnum.class.getName(),
              CountersEnum.INPUT_WORDS.toString());
          counter.increment(1);
        }
      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ REDUCER ~~~~~~~~~~~~~~~~~~~~~~~~
  
    public static class IntSumReducer //might not compile with 'static' modifier
         extends Reducer<Text,IntWritable,Text,IntWritable> { //<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
      private IntWritable result = new IntWritable();
  
      public void reduce(Text key, Iterable<IntWritable> values,
                         Context context
                         ) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
          sum += val.get();
        }
        result.set(sum);
        context.write(key, result);
      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ JOB CONFIG ~~~~~~~~~~~~~~~~~~~~~~~~
    // Need to address:
    // different input types and ways to customize (inputsplit, inputformat)
    // how to override cleanup(Context) to perform cleanup of input
    // does output have to be a writable type??
    // how to use Counter to report stats mid run
    // how to custom group each? map output with Job.setGroupingComparatorClass()
    // how to create a custom Partitioner (which sends specific keys to the same reducers)
    // how (why?) to set custom sort and group methods in the stage between reduce input gathering and reduce running
    // how to use FileOutputFormat.getWorkOutputPath() to create side files while in map
    // how to use counters Counters.incrCounter(Enum, long)
    // distributed cache?

    public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
      String[] remainingArgs = optionParser.getRemainingArgs();
      if ((remainingArgs.length != 2) && (remainingArgs.length != 4)) {
        System.err.println("Usage: wordcount <in> <out> [-skip skipPatternFile]");
        System.exit(2);
      }


      Job job = Job.getInstance(conf, "word count");

      // keep in mind, the shuffle/reduce phase could be a complex bottleneck from designated memory(buffer)/storage(disk)/segments, these can be modified

      job.setJarByClass(WordCount2.class);
      job.setMapperClass(TokenizerMapper.class);
      job.setCombinerClass(IntSumReducer.class); // takes the list of key-vals from a single map node and processes those in a set before local storage and subsuquent accession from god knows how many reduce nodes
                                                    // could be just a copy of the reduce class if the data is spread in the map phase and combined in the reduce phase, might as well make use of the spread data and perform some small combination on each map output set
      job.setReducerClass(IntSumReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
  
      List<String> otherArgs = new ArrayList<String>();
      for (int i=0; i < remainingArgs.length; ++i) {
        if ("-skip".equals(remainingArgs[i])) {
          job.addCacheFile(new Path(remainingArgs[++i]).toUri());
          job.getConfiguration().setBoolean("wordcount.skip.patterns", true);
        } else {
          otherArgs.add(remainingArgs[i]);
        }
      }
      FileInputFormat.addInputPath(job, new Path(otherArgs.get(0))); // .gz files can be submitted, but each file is processed by a single mapper
      FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));
  
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
  }