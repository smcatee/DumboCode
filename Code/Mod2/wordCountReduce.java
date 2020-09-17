import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class wordCountReduce 
    extends Reducer<Text,IntWritable,Text,IntWritable> {
    
        private IntWritable count = new IntWritable();
    
        public void reduce(LongWritable key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {

                int sum = 0;

                for (IntWritable val : values) {
                    sum += val.get();
                }

                count.set(sum);
                context.write(key, count);

            }

}
