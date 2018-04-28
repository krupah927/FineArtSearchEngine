import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class InvIndex2 {

    public static class JsonTokenizer extends Mapper<LongWritable, Text, Text, Text>{

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] tokens = line.split(" ");
            String word = line.split(" ")[0];
            //String fieldIdCount = line.split(" ")[1]+" "+line.split(" ")[2]+" "+line.split(" ")[3];
            //String fieldIdCount = line.substring(line.length()-1);
            //String fieldIdCount = line.split(" ")[1];

            //String match[] = {"color","artist","tags","title","description","medium"};

            int position;
            if (isArtist(line))
                position = line.indexOf("artist");
            else if (isColor(line))
                position = line.indexOf("color");
            else if (isTags(line))
                position = line.indexOf("tags");
            else if (isTitle(line))
                position = line.indexOf("title");
            else if (isDesc(line))
                position = line.indexOf("description");
            else if (isMedium(line))
                position = line.indexOf("medium");
            else
                position = 0;

            String fieldIdCount;
            if (position != 0)
                fieldIdCount = line.substring(position);
            else fieldIdCount = "null null 0";

            context.write(new Text(word), new Text(fieldIdCount));

        }

        private boolean isColor(String line) {
            return line.contains("color");
        }

        private boolean isTags(String line){
            return line.contains("tags");
        }

        private boolean isTitle(String line){
            return line.contains("title");
        }

        private boolean isDesc(String line){
            return line.contains("description");
        }

        private boolean isMedium(String line){
            return line.contains("medium");
        }

        private boolean isArtist(String line) {
            return line.contains("artist");
        }


    }

    public static class IndexReducer extends Reducer<Text, Text, Text, Text>{

        //private Text result = new Text();

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            String myVal = "";
            for (Text val: values){
                //String stringVal = val.toString();
                String h = val.toString().replaceAll("\t"," ");
                myVal = myVal+","+h;
            }

            //String result = myVal.toString();

            context.write(key,new Text(myVal));

        }

    }


    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {

        long start = System.nanoTime();

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"invertedIndex");
        job.setJarByClass(InvIndex2.class);
        job.setMapperClass(JsonTokenizer.class);
        job.setCombinerClass(IndexReducer.class);
        job.setReducerClass(IndexReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        long end = System.nanoTime();
        System.out.println("hundred time elapsed secs: "+(end-start)/1000000000);

        System.exit(job.waitForCompletion(true)?0:1);
    }



}
