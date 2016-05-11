import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Tag {
  private int id;
  private String tag_name;

  public Tag(String name) {
  this.tag_name = name;
  }

  public String getTagName() {
    return tag_name;
  }

  public int getTagId() {
    return id;
  }

  public static List<Tag> all() {
    String sql = "SELECT * FROM tags;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Tag.class);
    }
  }

  @Override
  public boolean equals(Object otherTag) {
    if (!(otherTag instanceof Tag)) {
      return false;
    } else {
      Tag newTag = (Tag) otherTag;
      return this.getTagName().equals(newTag.getTagName()) && this.getTagId() == newTag.getTagId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tags(tag_name) VALUES (:tag_name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("tag_name", this.tag_name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Tag find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tags WHERE id = :id;";
      Tag tag = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Tag.class);
      return tag;
    }
  }

  public void update(String tag_name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tags SET tag_name = :tag_name WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("tag_name", tag_name)
        .addParameter("id", this.getTagId())
        .executeUpdate();
    }
  }

  public void addRecipe(Recipe recipe) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tags_recipes (tag_id, recipe_id) VALUES (:tag_id, :recipe_id)";
      con.createQuery(sql)
        .addParameter("tag_id", this.getTagId())
        .addParameter("recipe_id", recipe.getRecipeId())
        .executeUpdate();
    }
  }

  public List<Recipe> getRecipes() {
  try(Connection con = DB.sql2o.open()){
    String joinQuery = "SELECT recipe_id FROM tags_recipes WHERE tag_id = :tag_id";
    List<Integer> recipe_ids = con.createQuery(joinQuery)
      .addParameter("tag_id", this.getTagId())
      .executeAndFetch(Integer.class);

    List<Recipe> recipes = new ArrayList<Recipe>();

    for (Integer recipe_id : recipe_ids) {
      String recipeQuery = "SELECT * FROM recipes WHERE id = :recipe_id";
      Recipe recipe = con.createQuery(recipeQuery)
        .addParameter("recipe_id", recipe_id)
        .executeAndFetchFirst(Recipe.class);
      recipes.add(recipe);
    }
    return recipes;
  }
}

}
