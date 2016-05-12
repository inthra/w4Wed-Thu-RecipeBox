import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class TagTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Tag_instantiatesCorrectly_true() {
    Tag myTag = new Tag("Appetizer");
    assertEquals(true, myTag instanceof Tag);
  }

  @Test
  public void getTagName_tagInstantiatesWithTagName_String() {
    Tag myTag = new Tag("Appetizer");
    assertEquals("Appetizer", myTag.getTagName());
  }

  @Test
  public void all_emptyAtFirst_0() {
    assertEquals(0, Tag.all().size());
  }

  @Test
  public void equals_returnsTrueIfTagNamesAretheSame_true() {
    Tag firstTag = new Tag("Appetizer");
    Tag secondTag = new Tag("Appetizer");
    assertTrue(firstTag.equals(secondTag));
  }

  @Test
  public void save_savesObjectIntoDatabase_true() {
    Tag myTag = new Tag("Appetizer");
    myTag.save();
    assertTrue(Tag.all().get(0).equals(myTag));
  }

  @Test
  public void save_assignsIdToObject_int() {
    Tag myTag = new Tag("Appetizer");
    myTag.save();
    Tag savedTag = Tag.all().get(0);
    assertEquals(myTag.getTagId(), savedTag.getTagId());
  }

  @Test
  public void find_findTagInDatabase_true() {
    Tag myTag = new Tag("Appetizer");
    myTag.save();
    Tag savedTag = Tag.find(myTag.getTagId());
    assertTrue(myTag.equals(savedTag));
  }

  @Test
  public void update_updateTagInDatabase() {
    Tag myTag = new Tag("Appetizer");
    myTag.save();
    myTag.update("Fondue");
    assertEquals("Fondue", Tag.find(myTag.getTagId()).getTagName());
  }

  @Test
  public void addRecipe_addsRecipeToTag_true() {
    Tag myTag = new Tag("Breakfast");
    myTag.save();
    Recipe myRecipe = new Recipe("Fried egg");
    myRecipe.save();
    myTag.addRecipe(myRecipe);
    Recipe savedRecipe = myTag.getRecipes().get(0);
    assertTrue(myRecipe.equals(savedRecipe));
  }

  @Test
  public void getRecipes_returnsAllRecipes_List() {
    Tag myTag = new Tag("Breakfast");
    myTag.save();
    Recipe myRecipe = new Recipe("Fried egg");
    myRecipe.save();
    myTag.addRecipe(myRecipe);
    List savedRecipes = myTag.getRecipes();
    assertEquals(1, savedRecipes.size());
  }

  @Test
  public void delete_deletesAllRecipesAndTagsAssociations() {
    Tag myTag = new Tag("Dessert");
    myTag.save();
    Recipe myRecipe = new Recipe("Ice cream");
    myRecipe.save();
    myTag.addRecipe(myRecipe);
    myTag.delete();
    assertEquals(0, Tag.all().size());
    assertEquals(0, myRecipe.getTags().size());
  }
}
