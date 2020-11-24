import org.apache.spark.sql.SparkSession
import com.databricks.spark.xml._

val spark = SparkSession.builder.getOrCreate()
val df = spark.read
  .option("rowTag", "PubmedArticle")
  .xml("truncatedPubmed.xml")