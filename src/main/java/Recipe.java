import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Recipe {
  private int id;
  private String recipe_name;
  private Integer recipe_rating = 3; //Setting average rating from a scale of 1 - 5.
  private String instructions;

  public Recipe(String name) {
  this.recipe_name = name;
  }

  public String getRecipeName() {
    return recipe_name;
  }

  public int getRecipeId() {
    return id;
  }

  public Integer getRecipeRating() {
    return recipe_rating;
  }

  public String getInstructions() {
    return instructions;
  }

  public static List<Recipe> all() {
    String sql = "SELECT * FROM recipes;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Recipe.class);
    }
  }

  @Override
  public boolean equals(Object otherRecipe) {
    if (!(otherRecipe instanceof Recipe)) {
      return false;
    } else {
      Recipe newRecipe = (Recipe) otherRecipe;
      return this.getRecipeName().equals(newRecipe.getRecipeName()) && this.getRecipeId() == newRecipe.getRecipeId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (recipe_name, recipe_rating, instructions) VALUES (:recipe_name, :recipe_rating, :instructions);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("recipe_name", this.recipe_name)
        .addParameter("recipe_rating", this.recipe_rating)
        .addParameter("instructions", this.instructions)
        .executeUpdate()
        .getKey();
    }
  }

  public static Recipe find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes WHERE id = :id;";
      Recipe recipe = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Recipe.class);
      return recipe;
    }
  }

  public void update(String recipe_name, Integer recipe_rating, String instructions) {
    if (recipe_name.trim().length() != 0) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE recipes SET recipe_name = :recipe_name WHERE id = :id;";
        con.createQuery(sql)
          .addParameter("recipe_name", recipe_name)
          .addParameter("id", this.getRecipeId())
          .executeUpdate();
      }
    }
    if (recipe_rating != 3) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE recipes SET recipe_rating = :recipe_rating WHERE id = :id;";
        con.createQuery(sql)
          .addParameter("recipe_rating", recipe_rating)
          .addParameter("id", this.getRecipeId())
          .executeUpdate();
      }
    }
    if (instructions.trim().length() != 0) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE recipes SET instructions = :instructions WHERE id = :id;";
        con.createQuery(sql)
          .addParameter("instructions", instructions)
          .addParameter("id", this.getRecipeId())
          .executeUpdate();
      }
    }
  }

}
