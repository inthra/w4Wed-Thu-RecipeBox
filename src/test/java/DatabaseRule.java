import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/recipe_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTagsQuery = "DELETE FROM tags *;";
      String deleteRecipesQuery = "DELETE FROM recipes *;";
      String deleteIngredientsQuery = "DELETE FROM ingredients *;";
      String deleteTagsRecipesQuery = "DELETE FROM tags_recipes *;";
      String deleteRecipesIngredientsQuery = "DELETE FROM recipes_ingredients *;";
      con.createQuery(deleteTagsQuery).executeUpdate();
      con.createQuery(deleteRecipesQuery).executeUpdate();
      con.createQuery(deleteIngredientsQuery).executeUpdate();
      con.createQuery(deleteTagsRecipesQuery).executeUpdate();
      con.createQuery(deleteRecipesIngredientsQuery).executeUpdate();
    }
  }

}
