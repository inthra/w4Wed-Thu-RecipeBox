import org.sql2o.*;
import org.junit.*;
import org.fluentlenium.adapter.FluentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Recipe Box!");
    assertThat(pageSource()).contains("Add a New Recipe Category");
  }

  @Test
  public void tagIsCreatedAndDisplayedTest() {
    goTo("http://localhost:4567/");
    fill("#tag_input").with("Breakfast");
    submit(".btn");
    assertThat(pageSource()).contains("Breakfast");
  }

  @Test
  public void recipeIsCreatedAndSavedTest() {
    goTo("http://localhost:4567/");
    fill("#recipe_input").with("Scrambled Eggs");
    submit(".btn");
    assertEquals(1, Recipe.all().size());
  }

  @Test
  public void ingredientIsCreatedAndSavedTest() {
    goTo("http://localhost:4567/");
    fill("#ingredient_input").with("Carrot");
    submit(".btn");
    assertEquals(1, Ingredient.all().size());
  }

  @Test
  public void tagAndRecipeFormIsDisplayed() {
    Tag testTag = new Tag("Lunch");
    testTag.save();
    String url = String.format("http://localhost:4567/tags/%d", testTag.getTagId());
    goTo(url);
    assertThat(pageSource()).contains("Lunch");
  }

  @Test
  public void recipeIsAddedToTagTest() {
    Tag testTag = new Tag("Sandwich");
    testTag.save();
    String url = String.format("http://localhost:4567/tags/%d", testTag.getTagId());
    goTo(url);
    fill("#recipe_input").with("Chicken Bacon");
    submit("#add_recipe");
    goTo(url);
    assertThat(pageSource()).contains("Chicken Bacon");
  }

  @Test
  public void tagNameIsUpdatedTest() {
    Tag testTag = new Tag("Dinner");
    testTag.save();
    String url = String.format("http://localhost:4567/tags/%d", testTag.getTagId());
    goTo(url);
    fill("#update_tag").with("Supper");
    submit("#update_tag_button");
    goTo(url);
    assertThat(pageSource()).contains("Supper");
  }

  @Test
  public void tagNameIsDeletedTest() {
    Tag testTag = new Tag("Dinner");
    testTag.save();
    String url = String.format("http://localhost:4567/tags/%d", testTag.getTagId());
    goTo(url);
    click("a", withText("Warning: This will delete this category!!!"));
    assertThat(pageSource()).doesNotContain("Dinner");
  }

}
