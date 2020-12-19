import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import java.util.Scanner;

public class Cass {
    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().withKeyspace("videosharingsystem").build()) {
            System.out.println("Choose an option:" +
                    "\n 0 - Insert user" +
                    "\n 1 - Edit user" +
                    "\n 2 - Search user");

            Scanner scn1 = new Scanner(System.in);

            int input = scn1.nextInt();

            scn1.close();

            switch (input) {
                case 0:
                    /*
                    Scanner scn2 = new Scanner(System.in);

                    System.out.println("Name: ");
                    String name = scn2.nextLine();

                    System.out.println("Username: ");
                    String username = scn2.nextLine();

                    System.out.println("email: ");
                    String email = scn2.nextLine();

                    scn2.close();
                    */

                    insertUser(session, "teste", "teste@email.com", "Teste");

                    break;

                case 1:
                    /*
                    Scanner scn3 = new Scanner(System.in);
                    System.out.println("Insert user's email: ");
                    String email2 = scn3.nextLine();

                    System.out.println("Insert new username: ");
                    String newUsername = scn3.nextLine();

                    scn3.close();
                    */

                    updateUser(session, "zezito", "bommail@ua.pt", "Zezito");

                    break;

                case 2:
                    /*
                    Scanner scn4 = new Scanner(System.in);

                    System.out.println("Insert username: ");
                    String username2 = scn4.nextLine();

                    scn4.close();
                    */

                    searchUser(session, "eduardosantos");

                    break;
            }
        }
    }

    private static void insertUser(CqlSession session, String username, String email, String name) {
        session.execute(
                SimpleStatement.builder("insert into user (username, email, name) values (?, ?, ?)")
                .addPositionalValues(username, email, name)
                .build()
        );
    }

    private static void updateUser(CqlSession session, String username, String email, String name) {
        session.execute(
                SimpleStatement.builder("update user set name=? where username=? and email=?")
                .addPositionalValues(name, username, email)
                .build()
        );
    }

    private static void searchUser(CqlSession session, String username) {
        try {
            ResultSet result = session.execute(
                    SimpleStatement.builder("select * from user where username=?")
                    .addPositionalValue(username)
                    .build()
            );

            Row r = result.one();
            System.out.format("Username: " + r.getString("username"));
            System.out.format("\nEmail: " + r.getString("email"));
            System.out.format("\nName: " + r.getString("name"));

        } catch (Exception QueryExecutionException) {
            System.out.println("ERROR: User doesn't exists!");
        }
    }
}
