import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("tags", Tag.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tags/tag_form", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Tag tag = new Tag(request.queryParams("tag_input"));
      if ((tag.getTagName()).trim().length() != 0) {
        tag.save();
      }
      Recipe recipe = new Recipe(request.queryParams("recipe_input"));
      if ((recipe.getRecipeName()).trim().length() != 0) {
        recipe.save();
      }
      Ingredient ingredient = new Ingredient(request.queryParams("ingredient_input"));
      if ((ingredient.getIngredientName()).trim().length() != 0) {
        ingredient.save();
      }
      model.put("tags", Tag.all());
      response.redirect("/");
      return null;
    });

    get("/tags/:tag_id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Tag tag = Tag.find(Integer.parseInt(request.params(":tag_id")));
      model.put("tag", tag);
      model.put("template", "templates/tag.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tags/:tag_id/recipe_form", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Tag tag = Tag.find(Integer.parseInt(request.params(":tag_id")));
      Recipe newRecipe = new Recipe(request.queryParams("recipe_input"));
      if ((newRecipe.getRecipeName()).trim().length() != 0) {
        newRecipe.save();
      }
      tag.addRecipe(newRecipe);
      model.put("tag", tag);
      response.redirect("/tags/" + tag.getTagId());
      return null;
    });

    post("/tags/:tag_id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Tag tag = Tag.find(Integer.parseInt(request.params(":tag_id")));
      String updateTagName = request.queryParams("update_tag");
      tag.update(updateTagName);
      model.put("tag", tag);
      response.redirect("/tags/" + tag.getTagId());
      return null;
    });

    get("/tag/:tag_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Tag tag = Tag.find(Integer.parseInt(request.params(":tag_id")));
      tag.delete();
      response.redirect("/");
      return null;
    });

    get("/tags/:tag_id/recipes/:recipe_id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.find(Integer.parseInt(request.params(":recipe_id")));
      model.put("recipe", recipe);
      model.put("template", "templates/recipe.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/recipes/:recipe_id/ingredient_form", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      // Tag tag = Tag.find(Integer.parseInt(request.params(":tag_id")));
      Recipe recipe = Recipe.find(Integer.parseInt(request.params(":recipe_id")));
      Ingredient newIngredient = new Ingredient(request.queryParams("ingredient_input"));
      if ((newIngredient.getIngredientName()).trim().length() != 0) {
        newIngredient.save();
      }
      recipe.addIngredient(newIngredient);
      model.put("recipe", recipe);
      String url = String.format("/tags/%d/recipes/%d", tag.getTagId(), recipe.getRecipeId());
      response.redirect(url);
      return null;
    });
  }
}
