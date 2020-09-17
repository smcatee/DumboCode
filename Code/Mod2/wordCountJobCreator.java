import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class wordCountJobCreator {

    public static void main(String[] args) throws Exception {

        Job job = new Job();
        job.setNumReduceTask(1);
        job.setJarByClass(wordCount.class);
        job.setJobName("Word Count");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path("output_" + args[0]));

        job.setMapperClass(wordCountMap.class);
        job.setReducerClass(wordCountReduce.class);
        //combinder class?
        //reducer class?

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}