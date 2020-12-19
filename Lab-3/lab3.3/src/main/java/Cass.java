import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser;

import java.util.Scanner;

public class Cass {
    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().withKeyspace("videosharingsystem").build()) {
            System.out.println("Choose an option:" +
                    "\n 0 - Insert user" +
                    "\n 1 - Edit user" +
                    "\n 2 - Search user" +
                    "\n 3 - Search all videos from an author" +
                    "\n 4 - Search comments by user" +
                    "\n 5 - Search comments by video" +
                    "\n 6 - List of tags of a specific video");

            Scanner scn1 = new Scanner(System.in);

            int input = scn1.nextInt();

            scn1.close();

            switch (input) {

                //-------------- a) --------------//

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

                //-------------- b) --------------//

                case 3:
                    searchVideosByAuthor(session, "eduardosantos");

                    break;

                case 4:
                    searchCommentsByUser(session, "bastitos");

                    break;

                case 5:
                    searchCommentsByVideo(session, 1);

                    break;

                case 6:
                    videoTagsList(session, 1);
            }
        }
    }

    //-------------- a) --------------//

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

    //-------------- b) --------------//

    private static void searchVideosByAuthor(CqlSession session, String author) {
        try {
            ResultSet result = session.execute(
                    SimpleStatement.builder("select * from video where author=? allow filtering")
                            .addPositionalValue(author)
                            .build()
            );

            for (Row r : result) {
                System.out.format("id: %d | timestamp: %s | author: %s | description: %s | name: %s | tags: %s \n",
                        r.getInt("id"),
                        r.getObject("upload_timestamp"),
                        r.getString("author"),
                        r.getString("description"),
                        r.getString("name"),
                        r.getList("tags", String.class)
                );
            }

        } catch (Exception QueryExecutionException) {
            System.out.println("ERROR: User doesn't exists!");
        }
    }

    private static void searchCommentsByUser(CqlSession session, String user) {
        try {
            ResultSet result = session.execute(
                    SimpleStatement.builder("select * from comment_by_author where author=? allow filtering")
                            .addPositionalValue(user)
                            .build()
            );

            for (Row r : result) {
                System.out.format("author: %s | timestamp: %s | comment: %s | video_id: %d \n",
                        r.getString("author"),
                        r.getObject("comment_timestamp"),
                        r.getString("comment"),
                        r.getInt("video_id")
                );
            }

        } catch (Exception QueryExecutionException) {
            System.out.println("ERROR: User doesn't exists!");
        }
    }

    private static void searchCommentsByVideo(CqlSession session, int video_id) {
        try {
            ResultSet result = session.execute(
                    SimpleStatement.builder("select * from comment_by_video where video_id=? allow filtering")
                            .addPositionalValue(video_id)
                            .build()
            );

            for (Row r : result) {
                System.out.format("author: %s | timestamp: %s | comment: %s | video_id: %d \n",
                        r.getString("author"),
                        r.getObject("comment_timestamp"),
                        r.getString("comment"),
                        r.getInt("video_id")
                );
            }

        } catch (Exception QueryExecutionException) {
            System.out.println("ERROR: Video doesn't exists!");
        }
    }

    private static void videoTagsList(CqlSession session, int video_id) {
        try {
            ResultSet result = session.execute(
                    SimpleStatement.builder("select tags from video where id=?")
                            .addPositionalValue(video_id)
                            .build()
            );

            Row r = result.one();
            System.out.println("Tags: " + r.getList("tags", String.class));

        } catch (Exception QueryExecutionException) {
            System.out.println("ERROR: Video doesn't exists!");
        }
    }
}
