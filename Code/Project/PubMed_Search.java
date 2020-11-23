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

public class PubMed_Search {

    //~~~~~~~~~~~~~~~~~~~~~~~~ MAPPER ~~~~~~~~~~~~~~~~~~~~~~~~

    public static class PubMed_SearchMapper extends Mapper<LongWritable, Text, Text, Text> {
      @Override
      public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // persistent variables
        // parse search term list (expect each term in list to be on new line)
        //  

        //  enter search term (value) with Entrez PubMed to return DOI
        //    https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=asthma&retmax=5000
        //      include "impact OR correlate OR correlated OR associated" in search
        //      alternatively " "Risk Factors"[MeSH Terms] OR Epidemiology[MeSH] "
        //        [All Fields] [Author] [Organism] [MeSH Terms] [Properties] 2015/3/1:2016/4/30[Publication Date]
        //        "diabet*"
        //      there is a way to store history and have entrez combine search results
        //  use UID or returned information
        //      esummary.fcgi?db=pubmed&id=19393038,30242208,29453458
        //      eutils PMID search returns
        //        https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=17284678,9997&retmode=text&rettype=abstract
        //      pubtator PMID search returns with disease identified
        //        https://www.ncbi.nlm.nih.gov/research/pubtator-api/publications/export/pubtator?pmids=30229314&concepts=disease
        //        need to use POST API to get more than 200
        //      get all other easily available attributes, comment out so they are accessible later
        //        e.g. keywords, title, authors, date, journal(s), linked db info, ...
        //  context.write(key=DOI, value=SearchTerm);
        

      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ REDUCER ~~~~~~~~~~~~~~~~~~~~~~~~
  
    public static class PubMed_SearchReducer extends Reducer<Text,Text,Text,Text> {
      public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //variables
        
        
      }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ JOB CONFIG ~~~~~~~~~~~~~~~~~~~~~~~~
    
    public static void main(String[] args) throws Exception {
      if (args.length != 2) {
        System.err.println("Usage: <in> <out>");
        System.exit(2);
      }
      
      Job job = new Job();
      job.setJobName("PubMed_Search");
      // keep in mind, the shuffle/reduce phase could be a complex bottleneck from designated memory(buffer)/storage(disk)/segments, these can be modified

      job.setJarByClass(PubMed_Search.class);
      job.setMapperClass(PubMed_SearchMapper.class);
      //job.setCombinerClass(PubMed_SearchReducer.class);
      job.setReducerClass(PubMed_SearchReducer.class);

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