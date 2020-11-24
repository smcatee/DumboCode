import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class XML_Parser {

    public static void main(String[] args) {
        XML_Parser app = new XML_Parser();
        app.start();
    }

    private void start() {
        SparkSession spark = SparkSession
        .builder()
        .appName("Pubmed Article Set")
        .getOrCreate();

        DataFrame df = spark.read()
            .format("xml")
            .option("rowTag", "PubmedArticle")
            .load("truncatedPubmed.xml");

        df.show(5);
        df.printSchema();
    }
}