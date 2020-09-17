import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class wordCountMap
    extends Mapper<LongWritable, Text, Text, IntWritable> {

        private IntWritable count = new IntWritable();
        private Text searchTerm = new Text();
        private final static String[] searchTermArray = new String[]{"hackathon", "Dec", "Chicago", "Java"};

        public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
                
            String line = value.toString();

            for ( String term : searchTermArray) {
                searchTerm.set(term);

                // Setting 0 or 1 values for reducer to add will allow for terms with 0 matches to appear in the output
                if ( line.toLowerCase().indexOf(term.toLowerCase()) > -1 ) {
                    count.set(1);
                } else {
                    count.set(0);
                }

                context.write(searchTerm, count);

            }
        }
    }
