import org.apache.spark.sql.SparkSession
import com.databricks.spark.xml._


object XML_Parser {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("XML Parse").getOrCreate()
    val df = spark.read // sc.read   or   spark.read.format("com.databricks.spark.xml")
      .option("rowTag", "PubmedArticle")
      .xml("tmp/pubmed20n1000.xml") // .load("")
  }
}