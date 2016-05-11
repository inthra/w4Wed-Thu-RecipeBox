import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;


public class Ingredient {
  private int id;
  private String ingredient_name;

  public Ingredient(String name) {
  this.ingredient_name = name;
  }

  public String getIngredientName() {
    return ingredient_name;
  }

  public int getIngredientId() {
    return id;
  }

  public static List<Ingredient> all() {
    String sql = "SELECT * FROM ingredients;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Ingredient.class);
    }
  }

  @Override
  public boolean equals(Object otherIngredient) {
    if (!(otherIngredient instanceof Ingredient)) {
      return false;
    } else {
      Ingredient newIngredient = (Ingredient) otherIngredient;
      return this.getIngredientName().equals(newIngredient.getIngredientName()) && this.getIngredientId() == newIngredient.getIngredientId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO ingredients(ingredient_name) VALUES (:ingredient_name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("ingredient_name", this.ingredient_name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Ingredient find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients WHERE id = :id;";
      Ingredient ingredient = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Ingredient.class);
      return ingredient;
    }
  }

  public void update(String ingredient_name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE ingredients SET ingredient_name = :ingredient_name WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("ingredient_name", ingredient_name)
        .addParameter("id", this.getIngredientId())
        .executeUpdate();
    }
  }

}
