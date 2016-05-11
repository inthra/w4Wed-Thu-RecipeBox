import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class RecipeTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Recipe_instantiatesCorrectly_true() {
    Recipe myRecipe = new Recipe("Cheddar Crackers");
    assertEquals(true, myRecipe instanceof Recipe);
  }

  @Test
  public void getRecipeName_tagInstantiatesWithRecipeName_String() {
    Recipe myRecipe = new Recipe("Cheddar Crackers");
    assertEquals("Cheddar Crackers", myRecipe.getRecipeName());
  }

  @Test
  public void all_emptyAtFirst_0() {
    assertEquals(0, Recipe.all().size());
  }

  @Test
  public void equals_returnsTrueIfRecipeNamesAretheSame_true() {
    Recipe firstRecipe = new Recipe("Test1");
    Recipe secondRecipe = new Recipe("Test1");
    assertTrue(firstRecipe.equals(secondRecipe));
  }

  @Test
  public void save_savesObjectIntoDatabase_true() {
    Recipe myRecipe = new Recipe("Test2");
    myRecipe.save();
    assertTrue(Recipe.all().get(0).equals(myRecipe));
  }

  @Test
  public void save_assignsIdToObject_int() {
    Recipe myRecipe = new Recipe("Test3");
    myRecipe.save();
    Recipe savedRecipe = Recipe.all().get(0);
    assertEquals(myRecipe.getRecipeId(), savedRecipe.getRecipeId());
  }

  @Test
  public void find_findRecipeInDatabase_true() {
    Recipe myRecipe = new Recipe("Test4");
    myRecipe.save();
    Recipe savedRecipe = Recipe.find(myRecipe.getRecipeId());
    assertTrue(myRecipe.equals(savedRecipe));
  }

  @Test
  public void update_updateRecipeInDatabase() {
    Recipe myRecipe = new Recipe("Omelette");
    myRecipe.save();
    myRecipe.update("Sausage-Cheddar Bites", 4, "Bake 15 mins");
    assertEquals("Sausage-Cheddar Bites", Recipe.find(myRecipe.getRecipeId()).getRecipeName());
  }

  @Test
  public void update_updateRecipeNameOnlyInDatabase() {
    Recipe myRecipe = new Recipe("Pasta");
    myRecipe.save();
    myRecipe.update("Spaghetti", 3, "");
    assertEquals("Spaghetti", Recipe.find(myRecipe.getRecipeId()).getRecipeName());
  }

  @Test
  public void update_updateRecipeRatingOnlyInDatabase() {
    Recipe myRecipe = new Recipe("Cheddar Crackers");
    myRecipe.save();
    myRecipe.update("", 5, "");
    assertTrue(5 == Recipe.find(myRecipe.getRecipeId()).getRecipeRating());
  }

  @Test
  public void update_updateInstructionsOnlyInDatabase() {
    Recipe myRecipe = new Recipe("Blue Cheese Crackers");
    myRecipe.save();
    myRecipe.update("", 2, "Bake 15 mins");
    assertEquals("Bake 15 mins", Recipe.find(myRecipe.getRecipeId()).getInstructions());
  }

  @Test
  public void addTag_addsTagToRecipe() {
    Tag myTag = new Tag("Lunch");
    myTag.save();
    Recipe myRecipe = new Recipe("Burrito");
    myRecipe.save();
    myRecipe.addTag(myTag);
    Tag savedTag = myRecipe.getTags().get(0);
    assertTrue(myTag.equals(savedTag));
  }

  @Test
  public void getTags_returnsAllTags_List() {
    Tag myTag = new Tag("Lunch");
    myTag.save();
    Recipe myRecipe = new Recipe("Burrito");
    myRecipe.save();
    myRecipe.addTag(myTag);
    List savedTags = myRecipe.getTags();
    assertEquals(1, savedTags.size());
  }

}
