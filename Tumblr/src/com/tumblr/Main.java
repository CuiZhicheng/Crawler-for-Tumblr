package com.tumblr;


import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class Main {
    String filepath = "E:\\film\\Tumblr\\tumblr-key.txt";
    String savepath = "E:\\film\\Tumblr\\";
    String api_key = null;
    String secret_key = null;
    String api_token = null;
    String sceret_token = null;
    Vector<Info> info = new Vector<>();
    Vector<String> VideoUrl = new Vector<>();
    int Step = 0;

    class Info{
        public String identifier = null;
        public String No = null;
        public String name = null;
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
            User user = client.user();
            int i;
            for (i = 0; i < info.size(); i++) {
                Info tmp = info.get(i);
                Map<String, Object> params = new HashMap<String, Object>();
                //params.put("blog-identifier", tmp.identifier);
                params.put("type", "video");
                params.put("id", tmp.No);

                String path = "/posts";
                if(params.containsKey("type")) {
                    path = path + "/" + params.get("type").toString();
                    params.remove("type");
                }

                //ResponseWrapper rp = client.requestBuilder.get(blogPath(blogName, path), soptions);
                List<Post> posts = client.blogPosts(tmp.identifier, params);
                for (Post p : posts) {
                    //p.save();
                    //JsonObject jo = new JsonObject(p);
                    System.out.println(p.toString());
                    //System.out.println(tmp.No + " " + tmp.name + " url=" + p.);
                    //VideoUrl.add(p.getShortUrl());
                    //download(p.getShortUrl());
                    Step++;
                }
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public void download(String urlStr){
        int i;
        try {
            URL url = new URL(urlStr);
            Object o = url.getContent();
            //HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //InputStream inputStream = conn.getInputStream();
            InputStream inputStream = (InputStream) o;
            //byte[] getData = readInputStream(inputStream);
            String path = savepath + String.valueOf(Step);
            File sf = new File(path);
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
            System.out.println(time);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
	// write your code here
        Main m= new Main();
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
        m.readurl();
        m.test();
       // m.download();
    }
}
