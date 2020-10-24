import java.io.*;
import java.util.*;
import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        PostSet boardSet = new PostSet();

        File file = new File("female-names.txt");

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String name = sc.nextLine();
            boardSet.saveUser(name);
        }

        Scanner sc2 = new Scanner(System.in);

        System.out.println("/*---------- a) ----------*/");

        while(true) {
            System.out.print("Search for ('Enter') for quit): ");
            String input_name = sc2.nextLine();

            if(input_name.length() == 0)
                break;

            Set<String> answer = boardSet.getUser(input_name);
            System.out.println(input_name);
            for (String s : answer)
                System.out.println(s);
        }
        System.out.println("/*---------- b) ----------*/");

        PostSetCsv csv= new PostSetCsv();

        Scanner sc3 = new Scanner(System.in);

        BufferedReader csvReader = new BufferedReader(new FileReader(new File("nomes-registados-2020.csv")));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            csv.saveUser(data[0], Integer.parseInt(data[2]));
        }
        csvReader.close();

        while(true) {
            System.out.print("Search for ('Enter') for quit): ");
            String name = sc3.nextLine();

            if(name.length() == 0)
                break;

            Set<String> answer2 = csv.getUser();
            for (String s : answer2)
                if (s.toLowerCase().matches(name + "(.*)"))
                    System.out.println(s);
        }
        sc.close();

    }
}

class PostSet {
    private Jedis jedis;
    public static String USERS = "femaleNames";

    // Key set for users' name
    public PostSet() { this.jedis = new Jedis("localhost"); }

    public void saveUser(String username) {
        jedis.zadd(USERS, 0 , username);
    }

    public Set<String> getUser(String search) {
        return jedis.zrangeByLex(USERS, "[" + search + "*", "[" + search + (char)0xFF);
    }

    public Set<String> getAllKeys() { return jedis.keys("*"); }
}

class PostSetCsv{
    private Jedis jedis;
    public static String USERS = "femaleNames2"; // Key set for users' name

    public PostSetCsv() {
        this.jedis = new Jedis("localhost");
    }
    public void saveUser(String username, int score) {
        jedis.zadd(USERS, score , username);
    }
    public Set<String> getUser() {
        return jedis.zrevrange(USERS,0,-1);
    }
    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
    public void flushAll(){
        jedis.flushAll();
    }
}
