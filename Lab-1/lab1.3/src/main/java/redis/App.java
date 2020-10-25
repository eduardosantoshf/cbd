package redis;

import java.util.*;

import redis.clients.jedis.Jedis;


public class App 
{
    public static void main( String[] args )
    {
        /*--------------- Using set ---------------*/
        SimplePostSet boardSet = new SimplePostSet();

        // set some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };

        for (String user: users) boardSet.saveUser(user);

        boardSet.getAllKeys().stream().forEach(System.out::println);
        boardSet.getUserSet().stream().forEach(System.out::println);

        /*--------------- Using list ---------------*/
        SimplePostList boardList = new SimplePostList();

        List<String> l = new ArrayList<String>();
        for (String s: users) l.add(s);

        for (String user: l) boardList.saveUser(user);

        boardList.getAllKeys().stream().forEach(System.out::println);
        boardList.getUserSet().stream().forEach(System.out::println);

        /*--------------- Using hash ---------------*/

        SimplePostHash boardHash = new SimplePostHash();

        Map<String, String> hashmap = new HashMap<>();
        hashmap.put("User1", "Ana");
        hashmap.put("User2", "Pedro");
        hashmap.put("User3", "Maria");
        hashmap.put("User4", "Luis");

        boardHash.saveUser(hashmap);

        Map<String, String> m = boardHash.getUserSet();
        for (String key: m.keySet()) {
            System.out.println(m.get(key));
        }
    }
}

class SimplePostSet {
    private Jedis jedis;
    public static String USERS = "users";

    public SimplePostSet() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.sadd(USERS, username);
    }

    public Set<String> getUserSet() {
        return jedis.smembers(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
}

class SimplePostList {
    private Jedis jedis;
    public static String USERS = "users2";

    public SimplePostList() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) { jedis.lpush(USERS, username); }

    public List<String> getUserSet() { return jedis.lrange(USERS, 0, -1); }

    public Set<String> getAllKeys() { return jedis.keys("*"); }
}

class SimplePostHash {
    private Jedis jedis;
    public static String USERS = "users3";

    public SimplePostHash() { this.jedis = new Jedis("localhost"); }

    public void saveUser(Map<String, String> m) { jedis.hmset(USERS, m); }

    public Map<String, String> getUserSet() { return jedis.hgetAll(USERS); }

    public Set<String> getAllKeys() { return jedis.keys("*"); }
}