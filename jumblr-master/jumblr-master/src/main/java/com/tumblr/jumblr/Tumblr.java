package com.tumblr.jumblr;


import com.tumblr.jumblr.responses.ResponseWrapper;
import com.tumblr.jumblr.types.Post;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class Tumblr {
    String filepath = "E:\\film\\Tumblr\\tumblr-key.txt";
    String savepath = "E:\\film\\Tumblr\\video\\";
    String api_key = null;
    String secret_key = null;
    String api_token = null;
    String sceret_token = null;
    Vector<Info> info = new Vector<>();
    Vector<String> VideoUrl = new Vector<>();
    int Step = 66;
    int cur = 66;

    class Info{
        public String identifier = null;
        public String No = null;
        public String name = null;
    }

    public void like() {
        try {
            int offset = 0;
            int number = 0;
            Map<String, Object> params = new HashMap<String, Object>();

            JumblrClient client = new JumblrClient("rvmtYHW0ItdYBxuBTzLjPEbdRfA2WtAJ5tNcYGBZccHXVTsSgv", "iWCZToYbTfJiC0iP8edq35RzWnEKyCq5Hyiposysgx0fkvJgJI");
            while (number < 100) {
                params.clear();
                params.put("offset", offset);
                List<Post> posts = client.blogLikes("flyingmaple41.tumblr.com", params);
                for (Post p : posts) {
                    String type = p.getType().toString();
                    if (type == "VIDEO") {
                        number++;
                        System.out.println(p.getBlogName() + " " + p.getVideoUrl());
                    }
                }
                offset += 20;
            }
        } catch (Exception e) {

        }
    }


    public void readurl() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;
            line = br.readLine();
            api_key = line;
            line = br.readLine();
            sceret_token = line;
            line = br.readLine();
            api_token = line;
            line = br.readLine();
            sceret_token = line;
            while ((line = br.readLine()) != null) {
                line = br.readLine();
                String[] strs = line.split("/");
                Info ff = new Info();
                ff.identifier = strs[2];
                ff.No = strs[4];
                if (strs.length > 5)
                    ff.name = strs[5];
                else
                    ff.name = "video";
                info.add(ff);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i;
        for (i = 0; i < info.size(); i++) {
            Info tmp = info.get(i);
            System.out.println(tmp.identifier + " " + tmp.No + " ");
        }
    }

    public void test(){
        try {
            JumblrClient client = new JumblrClient("rvmtYHW0ItdYBxuBTzLjPEbdRfA2WtAJ5tNcYGBZccHXVTsSgv", "iWCZToYbTfJiC0iP8edq35RzWnEKyCq5Hyiposysgx0fkvJgJI");
            client.setToken("SEkzHj6R8AIKXvWr3cSiVtzoKgAV3HCwBdkJCQ1hIZrN8rKE0Q", "22ftzVHB1wIK6Knti7JFRWmTp4oFbbCQyfDyQhWC4qbWC8uEvb");

            int i;
            for (i = cur; i < info.size(); i++) {
                Info tmp = info.get(i);
                Map<String, Object> params = new HashMap<String, Object>();
                //params.put("blog-identifier", tmp.identifier);
                params.put("type", "video");
                params.put("id", tmp.No);

                /*
                Map<String, Object> soptions = JumblrClient.safeOptionMap(params);
                soptions.put("api_key", api_key);

                String path = "/posts";
                if (soptions.containsKey("type")) {
                    path += "/" + soptions.get("type").toString();
                    soptions.remove("type");
                }

                ResponseWrapper rp = client.requestBuilder.get(JumblrClient.blogPath(tmp.identifier, path), soptions);
                */
                System.out.println(tmp.identifier + " " + tmp.No);
                List<Post> posts = client.blogPosts(tmp.identifier, params);
                for (Post p : posts) {
                    //p.save();
                    //JsonObject jo = new JsonObject(p);
                    System.out.println(cur + " " + Step + " " + p.getVideoUrl());
                    //System.out.println(tmp.No + " " + tmp.name + " url=" + p.);
                    //VideoUrl.add(p.getShortUrl());
                    download(p.getVideoUrl());
                    Step++;
                }
            }

        } catch (Exception e) {
            try {
                System.out.println("Exception");
                e.printStackTrace();
                //Thread.currentThread().sleep(60000);
                cur++;
                test();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public void download(String urlStr) throws IOException{
        int i;

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //c.setDoOutput(true);
        con.connect();

        String path = savepath + String.valueOf(Step) + ".mp4";

        File outputFile = new File(path);


        if (!outputFile.exists()) {
            outputFile.createNewFile();
        } else {
            return;
        }

        FileOutputStream fos = new FileOutputStream(outputFile);

        int status = con.getResponseCode();

        int time = 0;

        InputStream is = con.getInputStream();

        byte[] buffer = new byte[1024];
        int len1 = 0;
        System.out.println("Begin Download");
        while ((len1 = is.read(buffer)) != -1) {
            time++;
            fos.write(buffer, 0, len1);
        }
        System.out.println("Download Finished " + time);
        fos.close();
        is.close();

        /*
        try {
            URL url = new URL(urlStr);
            Object o = url.getContent();
            //HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //InputStream inputStream = conn.getInputStream();
            InputStream inputStream = (InputStream) o;
            //byte[] getData = readInputStream(inputStream);
            String path = savepath + String.valueOf(Step) + ".mp4";
            File sf = new File(path + ".mp4");
            if (sf.exists())
                return;
            if (!sf.exists())
                sf.createNewFile();
            FileOutputStream fos = new FileOutputStream(sf);
            byte[] b = new byte[1000];
            int time = 0;
            while (inputStream.read(b) != -1) {
                time++;
                fos.write(b);
            }
            System.out.println("Download Finished " + time);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public static void main(String[] args) {
        // write your code here
        Tumblr m= new Tumblr();
        final String authUser = "smart1434";
        final String authPasswd = "smart1434";
        System.setProperty("http.proxyHost", "121.42.187.24");
        System.setProperty("http.proxyPort", "3434");
        System.setProperty("http.proxyUser", authUser);
        System.setProperty("http.proxyPassword", authPasswd);
        System.setProperty("https.proxyHost", "121.42.187.24");
        System.setProperty("https.proxyPort", "3434");
        System.setProperty("https.proxyUser", authUser);
        System.setProperty("https.proxyPassword", authPasswd);

        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(authUser, authPasswd.toCharArray());
                    }
                }
        );
        m.like();
        //m.readurl();
        //m.test();
        // m.download();
    }
}
