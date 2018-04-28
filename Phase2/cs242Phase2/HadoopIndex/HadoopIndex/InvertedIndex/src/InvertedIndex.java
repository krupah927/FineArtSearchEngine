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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class InvertedIndex {




    public static class JsonTokenizer extends Mapper<LongWritable, Text, Text, IntWritable>{


        JSONParser parser;


        public static boolean isStopWord(String s) throws FileNotFoundException {

            Scanner t = new Scanner(new File("/home/rutuja/IdeaProjects/InvertedIndex/stopwords.txt"));
            List<String> arrayList = new ArrayList<String>();
            while(t.hasNextLine()){
                arrayList.add(t.nextLine());
            }

            String[] stopwords = arrayList.toArray(new String[0]);
            for(int i=0; i<stopwords.length; i++){
                if (s.equalsIgnoreCase(stopwords[i]))
                    return true;
            }

            return false;
        }

        public static String removeSpecialChar(String s) {

            s = s.replaceAll("[^a-zA-Z0-9]","");

            return s;
        }


        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String artist;
            String desc;
            String medium;
            String title;
            ArrayList<String> tags = new ArrayList<String>();
            String _id;
            String color1;
            String color2;

            String line  = value.toString();
            parser = new JSONParser();
            JSONArray docs = null;
            try {
                docs = (JSONArray)parser.parse(line);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (Object o: docs) {
                JSONObject object = (JSONObject) o;
                //String c = object.toString();
                //context.write(new Text(c),new Text("1"));

                //ID
                _id = (String) object.get("_id");

                //color
                JSONArray colorArray = (JSONArray) object.get("color");
                color1 = colorArray.get(0).toString().substring(1);
                color2 = colorArray.get(1).toString().substring(1);
                if ((!(color1.equalsIgnoreCase("null"))) && (!(color2.equalsIgnoreCase("null")))){
                    context.write(new Text(color1+" color "+_id), new IntWritable(1));
                    context.write(new Text(color2+" color "+_id), new IntWritable(1));
                }

                //tags
                JSONArray tagsArray = (JSONArray) object.get("tags");
                for (int j=0; j<tagsArray.size(); j++)
                    context.write(new Text(tagsArray.get(j).toString()+" tags "+_id), new IntWritable(1));


                //artist
                artist = (String) object.get("artist");
                context.write(new Text(artist+" artist "+_id), new IntWritable(1));

                //Title
                title = (String) object.get("title");
                String[] t = title.split(" ");

                for (int i = 0; i < t.length; i++)
                    t[i] = removeSpecialChar(t[i]);

                for (int i=0; i<t.length; i++){
                    if((!(isStopWord(t[i]))) && (!(t[i].isEmpty())))
                        context.write(new Text(t[i]+" title "+_id), new IntWritable(1));
                }

                //Description
                desc = (String) object.get("description");
                if (desc != null) {
                    String[] d = desc.split(" ");

                    for (int i = 0; i < d.length; i++)
                        d[i] = removeSpecialChar(d[i]);


                    for (int i = 0; i < d.length; i++) {
                        if ((!(isStopWord(d[i]))) && (!(d[i].isEmpty())))
                            context.write(new Text(d[i]+" description "+_id), new IntWritable(1));
                    }
                }

                //medium
                medium = (String) object.get("medium");
                if (medium != null) {
                    String[] m = medium.split(" ");

                    for (int i = 0; i < m.length; i++)
                        m[i] = removeSpecialChar(m[i]);

                    for (int i = 0; i < m.length; i++) {
                        if ((!(isStopWord(m[i]))) && (!(m[i].isEmpty())))
                            context.write(new Text(m[i]+" medium "+_id), new IntWritable(1));
                    }
                }

            }

        }

    }


    public static class IndexReducer extends Reducer<Text, IntWritable, Text, IntWritable>{


        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            int count = 0;
            for (IntWritable val: values){
                count += val.get();
            }
            context.write(key,new IntWritable(count));

        }
    }



    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {

        long startTime = System.nanoTime();

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"invertedIndex");
        job.setJarByClass(InvertedIndex.class);
        job.setMapperClass(JsonTokenizer.class);
        job.setCombinerClass(IndexReducer.class);
        job.setReducerClass(IndexReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);


        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean status = job.waitForCompletion(true);

        long endTime = System.nanoTime();
        System.out.println("Time elapsed in seconds: "+(endTime-startTime)/1000000000);


        System.exit(job.waitForCompletion(true)?0:1);
    }



}
