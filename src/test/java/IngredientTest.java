import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class IngredientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Ingredient_instantiatesCorrectly_true() {
    Ingredient myIngredient = new Ingredient("cheese");
    assertEquals(true, myIngredient instanceof Ingredient);
  }

  @Test
  public void getIngredientName_ingredientInstantiatesWithIngredientName_String() {
    Ingredient myIngredient = new Ingredient("cheese");
    assertEquals("cheese", myIngredient.getIngredientName());
  }

  @Test
  public void all_emptyAtFirst_0() {
    assertEquals(0, Ingredient.all().size());
  }

  @Test
  public void equals_returnsTrueIfIngredientNamesAretheSame_true() {
    Ingredient firstIngredient = new Ingredient("cheese");
    Ingredient secondIngredient = new Ingredient("cheese");
    assertTrue(firstIngredient.equals(secondIngredient));
  }

  @Test
  public void save_savesObjectIntoDatabase_true() {
    Ingredient myIngredient = new Ingredient("cheese");
    myIngredient.save();
    assertTrue(Ingredient.all().get(0).equals(myIngredient));
  }

  @Test
  public void save_assignsIdToObject_int() {
    Ingredient myIngredient = new Ingredient("cheese");
    myIngredient.save();
    Ingredient savedIngredient = Ingredient.all().get(0);
    assertEquals(myIngredient.getIngredientId(), savedIngredient.getIngredientId());
  }

  @Test
  public void find_findIngredientInDatabase_true() {
    Ingredient myIngredient = new Ingredient("cheese");
    myIngredient.save();
    Ingredient savedIngredient = Ingredient.find(myIngredient.getIngredientId());
    assertTrue(myIngredient.equals(savedIngredient));
  }

  @Test
  public void update_updateIngredientInDatabase() {
    Ingredient myIngredient = new Ingredient("cheese");
    myIngredient.save();
    myIngredient.update("cheddar");
    assertEquals("cheddar", Ingredient.find(myIngredient.getIngredientId()).getIngredientName());
  }

}
