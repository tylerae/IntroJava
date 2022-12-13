package net.morgz;
 
import static spark.Spark.get;
 
import spark.Request;
import spark.Response;
import spark.Route;
 
public class Main {
    public static void main(String[] args) {
        get(new Route("/users/:id") {
            @Override
        public Object handle(Request request, Response response) {
            return  "User: username=test, email=test@test.net";
        }
            });
    }
}

// expected output  Listening on 0.0.0.0:4567


package net.morgz.core.database;
 
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
 
@DatabaseTable(tableName = "users")
public class User {
 
       @DatabaseField(generatedId = true)
    private int id;
 
       @DatabaseField
    private String username;
 
       @DatabaseField
    private String email;
 
       public User() {
        // ORMLite needs a no-arg constructor
     }
 
       public int getId() {
        return this.id;
    }
 
       public String getUsername() {
            return this.username;
    }
 
       public void setUsername(String username) {
        this.username = username;
    }
 
       public String getEmail() {
        return email;
    }
 
       public void setEmail(String email) {
        this.email = email;
    }
}

String databaseUrl = "jdbc:mysql://localhost/spark";
 
      ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
((JdbcConnectionSource)connectionSource).setUsername("spark");
((JdbcConnectionSource)connectionSource).setPassword("spark");

TableUtils.createTableIfNotExists(connectionSource, User.class);

post(new Route("/users") {
    @Override
    public Object handle(Request request, Response response) {
        String username = request.queryParams("username");
        String email = request.queryParams("email");
 
                       User user = new User();
        user.setUsername(username);
        user.setEmail(email);
 
                       userDao.create(user);
 
                                  response.status(201); // 201 Created
     }
});

get(new Route("/users/:id") {
   @Override
   public Object handle(Request request, Response response) {
       User user = userDao.queryForId(request.params(":id"));
       if (user != null) {
           return "Username: " + user.getUsername(); // or JSON? :-)
       } else {
           response.status(404); // 404 Not found
           return "User not found";
       }
   }
});


// expected http://localhost:4567/users/1