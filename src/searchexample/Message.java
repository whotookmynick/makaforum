//package searchexample;
//
//import org.compass.annotations.Searchable;
//import org.compass.annotations.SearchableId;
//import org.compass.annotations.SearchableProperty;
//
///**
// * http://www.compass-project.org/docs/2.2.0/reference/html/core-osem.html
// * @author Tomer Heber
// */
//@Searchable
//public class Message {
//
//   @SearchableId
//   private Long m_id; // identifier, each searchable object must have a unique ID.
//
//   @SearchableProperty (name = "name")   
//   private String m_name; // The name of the user who created this message
//
//   @SearchableProperty (name = "contents")
//   private String m_contents;
//
//   public void setId(long id) {
//       m_id = id;
//   }
//
//   public long getId() {
//       return m_id;
//   }
//
//   public void setName(String name) {
//       m_name = name;
//   }
//
//   public String getName() {
//       return m_name;
//   }
//
//   public void setContents(String contents) {
//       m_contents = contents;
//   }
//
//   public String getContents() {
//       return m_contents;
//   }
//
//}
