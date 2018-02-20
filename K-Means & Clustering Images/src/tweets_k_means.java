
import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedWriter;
import java.util.Collections;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class tweets_k_means {
    static String k;

    static String initial_seeds;
    static String tweets_File;
    static String output_File;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        k =args[0];
        initial_seeds =args[1];
        tweets_File =args[2];
        output_File =args[3];
        String outputFileName = output_File;
		File f=new File(tweets_File);
        FileReader fReader=new FileReader(f);
        BufferedReader bReader=new BufferedReader(fReader);
        String line="";
        int line_no=0;
		FileWriter fstream = new FileWriter(outputFileName);
        BufferedWriter out = new BufferedWriter(fstream);
        ArrayList<Read_tweets> tweetList=new ArrayList<>();
        while((line=bReader.readLine())!=null)
        {
            line_no++;
            String text[]=line.split("\"text\"");
            String id[]=line.split("\"id\":");
            String text1 = text[1].substring(3,text[1].indexOf("\","));
            long id1= Long.parseLong(id[1].substring(1, id[1].indexOf(',')));
            Read_tweets tweet=new Read_tweets(text1,id1);
            tweetList.add(tweet);
        }
        bReader.close();

        File seed_file=new File(initial_seeds);
        fReader=new FileReader(seed_file);
        bReader=new BufferedReader(fReader);

        String distnce[][]=new String[line_no+1][Integer.parseInt(k)+2];
        ArrayList<Read_tweets> tweet_array=new ArrayList<>();
        while((line=bReader.readLine())!=null)
        {
            String line1 = "";
            for(int i=0;i<line.length();i++)
            {
                if(line.charAt(i)!=',')
                    line1=line1+line.charAt(i);
            }
            long l=Long.parseLong(line1);
            for(Read_tweets t:tweetList)
            {
                if(t.id==l)
                {
                    Read_tweets tw=new Read_tweets(t.text,l);
                    tweet_array.add(tw);
                }
            }
        }
        distnce[0][0]="";
        int u=1;
        ArrayList<Long> fg=new ArrayList<>();
        for(Read_tweets p:tweet_array)
        {
            distnce[0][u]=""+p.id;
            u++;
            fg.add(p.id);
        }
        u=1;

        for(Read_tweets p:tweetList)
        {
            distnce[u][0]=""+p.id;

            u++;
        }
        ArrayList<Long> tmp_tweets_list=new ArrayList<>();
        ArrayList<ArrayList<Long>> arrayList = null;
        int flag=0;
        double sum=0.0;
        for(int v=0;v<25;v++)
        {
            double squaredError=0.0;
            ArrayList<Long> m=new ArrayList<>();
            m=tmp_tweets_list;
            Collections.sort(tmp_tweets_list);
            Collections.sort(fg);
            if(fg.equals(tmp_tweets_list))
            {
                out.flush();
                out.write("\n");
                break;
            }
            fg=m;
            tmp_tweets_list=new ArrayList<>();
            arrayList=new ArrayList<>();

            for(int i=1;i<=tweetList.size();i++)
            {
                double min=Double.MAX_VALUE;
                long id = 0;
                for(int j = 1; j<=Integer.parseInt(k); j++)
                {
                    String id1=distnce[i][0];
                    String id2=distnce[0][j];
                    String tweet1="";
                    String tweet2="";
                    for(Read_tweets t:tweetList)
                    {

                        long l=t.id;
                        if(Long.toString(l).equals(id1))
                        {
                            tweet1=t.text;

                        }

                    }
                    for(Read_tweets t:tweetList)
                    {
                        long l=t.id;
                        if(Long.toString(l).equals(id2))
                        {
                            tweet2=t.text;
                        }
                    }
                    List<String> a = Arrays.asList(tweet1.toLowerCase().split(" "));
                    List<String> b = Arrays.asList(tweet2.toLowerCase().split(" "));
                    Set<String> a1 = new HashSet<>(a);
                    a1.addAll(b);
                    Set<String> intersection = new HashSet<>(a);
                    intersection.retainAll(b);
                    distnce[i][j]=Double.toString(1 - (intersection.size()/ (double)a1.size()));
                    if(Double.parseDouble(distnce[i][j])<min)
                    {
                        min=Double.parseDouble(distnce[i][j]);
                        id=Long.parseLong(distnce[0][j]);
                    }

                }
                squaredError+=min*min;
                sum=sum+squaredError;
                distnce[i][Integer.parseInt(k)+1]=Long.toString(id);
            }
            int len=0;
            for(int j = 1; j<=Integer.parseInt(k); j++)
            {
                ArrayList<Long> a5=new ArrayList<>();
                for(int i=1;i<line_no+1;i++)
                {
                    if(distnce[i][Integer.parseInt(k)+1].equals(distnce[0][j]))
                    {
                        a5.add(Long.parseLong(distnce[i][0]));
                    }
                }
                len=len+a5.size();
                arrayList.add(a5);

            }

            int hp=0,t=0;
            if(v==24)
            {
                for(ArrayList<Long> a5:arrayList)
                {
                    out.write("Cluster id: "+(t+1)+" "+a5+"\r\n");
                    out.flush();
//                    out.write("\r\n");
                    t++;
                    flag=1;

                }
                out.write("Sum of Squared Error:"+sum);
                out.flush();
            }
            for(ArrayList<Long> a5:arrayList)
            {
                double minDistance=Double.POSITIVE_INFINITY;
                ArrayList<DoubleLong> er=new ArrayList<>();
                long id1=0;
                for(long l:a5)
                {
                    ArrayList<Read_tweets> ar=new ArrayList<>();

                    long long1=0;
                    Read_tweets twe = null ;
                    for(Read_tweets e:tweetList)
                    {
                        if(e.id==l)
                        {
                            String tweet1=e.text;
                            twe=new Read_tweets(tweet1,l);
                            long1=l;
                        }
                        else
                        {
                            Read_tweets c=new Read_tweets(e.text,e.id);
                            ar.add(c);
                        }
                    }
                    double JaccardDistance= getJaccardDistance(twe,ar);
                    DoubleLong dl=new DoubleLong(JaccardDistance,long1);
                    er.add(dl);
                }
                double mi=Double.MAX_VALUE;
                for(DoubleLong d1:er)
                {
                    if(d1.jaccardDistance <mi)
                    {

                        mi=d1.jaccardDistance;
                        id1=d1.id;
                    }
                }
                distnce[0][hp+1]=Long.toString(id1);
                tmp_tweets_list.add(id1);
                hp++;

            }
        }
        int d=0;
        if(flag==0)
        {
            for(ArrayList<Long> a5:arrayList)
            {
                out.write("Cluster id: "+(d+1)+" "+a5+"\r\n");
                out.flush();
//                out.write("\r\n");
                d++;

            }
            out.write("Sum of Squared Error:"+sum+"\r\n");
            out.flush();
        }

    }

    private static double getJaccardDistance(Read_tweets twe, ArrayList<Read_tweets> ar) {
        double min = 0;
        for(Read_tweets t:ar)
        {
            List<String> a = Arrays.asList(t.text.toLowerCase().split(" "));
            List<String> b = Arrays.asList(twe.text.toLowerCase().split(" "));
            Set<String> a1 = new HashSet<>(a);
            a1.addAll(b);
            Set<String> intersection = new HashSet<>(a);
            intersection.retainAll(b);
            double ans=(1 - (intersection.size() / (double)a1.size()));
            min=min+ans;
        }
        return min;
    }
   

    private static class DoubleLong {
        double jaccardDistance;
        long id;
        public DoubleLong(double a,long b) {
            jaccardDistance =a;
            id=b;
        }
    }



}

