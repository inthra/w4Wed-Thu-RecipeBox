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
  public void tagAndRecipeFormIsDisplayed() {
    Tag testTag = new Tag("Lunch");
    testTag.save();
    String tagPath = String.format("http://localhost:4567/tags/%d", testTag.getTagId());
    goTo(tagPath);
    assertThat(pageSource()).contains("Lunch");
  }

}
