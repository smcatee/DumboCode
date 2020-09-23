import java.io.IOException;

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


public class pageRankJob {

    
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