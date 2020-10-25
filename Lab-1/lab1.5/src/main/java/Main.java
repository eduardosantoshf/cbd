import java.io.*;
import java.util.*;
import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) {
        PostSet postS = new PostSet();
        PostList postL = new PostList();
        PostHash postH = new PostHash();

        Map<String, String> users_hashmap = new HashMap<String, String>();
        List<String> followers_list = new ArrayList<String>();
        Map<String, String> user_subscriptions = new HashMap<String, String>();

        int user_count = 1;
        int messages_user_count = 1;

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Welcome! \n" +
                    "1 - Create User \n" +
                    "2 - Send Message \n" +
                    "3 - Subscribe User \n" +
                    "4 - Check User Messages");

            int input = sc.nextInt();

            switch (input) {
                case 1:
                    Scanner sc_user = new Scanner(System.in);
                    System.out.println("Username?");
                    String username = sc_user.nextLine();

                    if (!users_hashmap.containsKey(username)) {
                        users_hashmap.put("user" + String.valueOf(user_count), username);
                        postH.saveUser(users_hashmap);
                        user_count++;
                    }
                    System.out.println(postH.getUserSet());
                    break;

                case 2:
                    Scanner sc_which_user = new Scanner(System.in);
                    Scanner sc_user_message = new Scanner(System.in);

                    System.out.println(postH.getUserSet());

                    System.out.println("User?");
                    String user = sc_which_user.nextLine();

                    System.out.println("Message?");
                    String message = sc_user_message.nextLine();

                    postL.saveMessage(user, message);
                    System.out.println(postL.getMessageSet(user));
                    break;
                case 3:

                    Scanner user_to_subscribe = new Scanner(System.in);
                    Scanner user_inline = new Scanner(System.in);

                    System.out.println(postH.getUserSet());

                    System.out.printf("Which user are you? ");
                    String current_user = user_inline.nextLine();

                    System.out.println(postH.getUserSet());

                    System.out.println("User to subscribe? ");
                    String user2subscribe = user_inline.nextLine();

                    postS.saveFollower(current_user, user2subscribe);
                    break;

                case 4:
                    Scanner c_user = new Scanner(System.in);

                    System.out.printf("Which user are you? ");
                    String current_user2 = c_user.nextLine();

                    Set<String> following = postS.getFollowerSet(current_user2);
                    if (following.size() == 0)
                        System.out.println("You don't follow anyone");
                    else {
                        for (String s_u: following) {
                            System.out.println("Message from " + s_u + ":");
                            List<String> msgs = postL.getMessageSet(s_u);
                            for(String m: msgs)
                                System.out.println(m);
                        }

                    }
                    break;
            }
        } while (sc.nextInt() != 5);
    }
}

class PostSet {
    private Jedis jedis;
    public static String FOLLOWERS = "followers";

    public PostSet() {
        this.jedis = new Jedis("localhost");
    }

    public void saveFollower(String username, String following_user) {
        jedis.sadd("following:" + username, following_user);
    }

    public Set<String> getFollowerSet(String username) {
        return jedis.smembers("following:" + username);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
}

class PostList {
    private Jedis jedis;

    public PostList() {
        this.jedis = new Jedis("localhost");
    }

    public void saveMessage(String user, String username) { jedis.lpush(user, username); }

    public List<String> getMessageSet(String user) { return jedis.lrange(user, 0, -1); }

    public Set<String> getAllKeys() { return jedis.keys("*"); }
}

class PostHash {
    private Jedis jedis;
    public static String USERS = "users";

    public PostHash() { this.jedis = new Jedis("localhost"); }

    public void saveUser(Map<String, String> m) { jedis.hmset(USERS, m); }

    public Map<String, String> getUserSet() { return jedis.hgetAll(USERS); }

    public Set<String> getAllKeys() { return jedis.keys("*"); }
}
