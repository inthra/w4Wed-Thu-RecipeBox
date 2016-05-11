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

}
