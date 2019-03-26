import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.catalyst.expressions.IntegerLiteral;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/7/27
 * \* Description:
 * \
 */
public class JavaSparkDemo1 {
    public static void main(String[] args){
        SparkConf conf=new SparkConf().setAppName("WorldCountLocal").setMaster("local");
        JavaSparkContext sc=new JavaSparkContext(conf);
        JavaRDD<String> lines=sc.textFile("D:\\test-data\\as.csv");

        //flatMap
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            public Iterator<String> call(String s) throws Exception{
                return Arrays.asList(s.split(",")).iterator();
            }
        });

        //map -> (k, 1)
        JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String word) throws Exception{
                return new Tuple2<String, Integer>(word, 1);
            }
        });

        //reduceByKey
        JavaPairRDD<String, Integer> wordCounts = pairs.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    public Integer call(Integer v1, Integer v2) throws Exception{
                        return v1 + v2;
                    }
        });

        //foreach
        wordCounts.foreach(
                new VoidFunction<Tuple2<String, Integer>>() {
                    @Override
                    public void call(Tuple2<String, Integer> wordcount) throws Exception {
                        System.out.println(wordcount._1 + " appeared " + wordcount._2 + " times ");
                    }
                }
        );

    }
}