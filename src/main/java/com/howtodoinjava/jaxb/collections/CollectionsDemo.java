package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

/**
 * Prints every result shown in the article "JAXB: Marshal / Unmarshal a List or Set":
 * a wrapper class with a List, the error for a bare List, a generic wrapper with
 * JAXBElement, output with and without @XmlElementWrapper, Set order, Maps, and
 * empty and null collections.
 */
public class CollectionsDemo {

  public static void main(String[] args) throws Exception {

    // 1. Quick answer: marshal a List inside a wrapper root class
    Recipe recipe = new Recipe("Pancakes");
    recipe.getIngredients().add(new Ingredient("flour", 200));
    recipe.getIngredients().add(new Ingredient("milk", 300));

    JAXBContext context = JAXBContext.newInstance(Recipe.class);
    // 1. Java object to XML
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    StringWriter out = new StringWriter();
    marshaller.marshal(recipe, out);
    String xml = out.toString();
    section("Recipe to XML", xml);

    // 2. XML back to the Java object
    Recipe copy = (Recipe) context.createUnmarshaller().unmarshal(new StringReader(xml));
    print("unmarshalled", copy);
    print("ingredients", copy.getIngredients());

    // the same with a file
    File file = Files.createTempFile("recipe", ".xml").toFile();
    marshaller.marshal(recipe, file);
    Recipe fromFile = (Recipe) context.createUnmarshaller().unmarshal(file);
    print("from file", fromFile.getIngredients());
    print("list class", fromFile.getIngredients().getClass().getName());
    file.delete();

    // 2. A bare List cannot be marshalled
    List<Ingredient> list = List.of(new Ingredient("flour", 200), new Ingredient("milk", 300));
    printError("newInstance(List.class)", () -> JAXBContext.newInstance(List.class));
    printError("marshal(ArrayList) with ArrayList context", () -> {
      JAXBContext ctx = JAXBContext.newInstance(ArrayList.class);
      return toXml(ctx, new ArrayList<>(list));
    });
    printError("marshal(List) with Ingredient context", () -> {
      JAXBContext ctx = JAXBContext.newInstance(Ingredient.class);
      return toXml(ctx, new ArrayList<>(list));
    });

    // 3. Generic wrapper + JAXBElement: any list, any root name
    JAXBContext genericContext = JAXBContext.newInstance(Wrapper.class, Ingredient.class);
    JAXBElement<Wrapper> root = new JAXBElement<>(
        new QName("ingredients"), Wrapper.class, new Wrapper<>(list));
    String genericXml = toXml(genericContext, root);
    section("generic wrapper", genericXml);

    Unmarshaller genericUnmarshaller = genericContext.createUnmarshaller();
    Wrapper<?> back = genericUnmarshaller
        .unmarshal(new StreamSource(new StringReader(genericXml)), Wrapper.class)
        .getValue();
    print("generic items", back.getItems());
    print("generic item class", back.getItems().get(0).getClass().getSimpleName());

    // unknown element inside the generic wrapper stays a DOM element
    Wrapper<?> unknown = genericUnmarshaller.unmarshal(new StreamSource(new StringReader(
        "<ingredients><ingredient name=\"flour\" grams=\"200\"/><spice>salt</spice></ingredients>")),
        Wrapper.class).getValue();
    print("unknown item classes", unknown.getItems().stream()
        .map(o -> o.getClass().getName()).toList());

    // 4. With and without @XmlElementWrapper
    FlatRecipe flat = new FlatRecipe("Pancakes");
    flat.getIngredients().addAll(list);
    JAXBContext flatContext = JAXBContext.newInstance(FlatRecipe.class);
    section("without @XmlElementWrapper", toXml(flatContext, flat));
    print("flat unmarshal", flatContext.createUnmarshaller().unmarshal(new StringReader(
        "<recipe name=\"Pancakes\"><ingredient name=\"flour\" grams=\"200\"/>"
            + "<ingredient name=\"milk\" grams=\"300\"/></recipe>")));

    // wrapped XML read by the flat class, and flat XML read by the wrapped class
    UnannotatedRecipe unannotated = new UnannotatedRecipe();
    unannotated.getIngredients().addAll(list);
    section("no annotation on the List", toXml(JAXBContext.newInstance(UnannotatedRecipe.class), unannotated));

    print("wrapped XML into FlatRecipe", flatContext.createUnmarshaller()
        .unmarshal(new StringReader(xml)));
    print("flat XML into Recipe", context.createUnmarshaller().unmarshal(new StringReader(
        "<recipe name=\"Pancakes\"><ingredient name=\"flour\" grams=\"200\"/></recipe>")));

    // 5. Set order
    JAXBContext tagContext = JAXBContext.newInstance(TaggedRecipe.class);
    List<String> input = List.of("quick", "breakfast", "sweet", "vegetarian");
    for (Set<String> set : List.<Set<String>>of(new HashSet<>(input),
        new LinkedHashSet<>(input), new TreeSet<>(input))) {
      TaggedRecipe tagged = new TaggedRecipe();
      tagged.setTags(set);
      section(set.getClass().getSimpleName(), toXml(tagContext, tagged));
    }
    TaggedRecipe tagsBack = (TaggedRecipe) tagContext.createUnmarshaller().unmarshal(
        new StringReader("<recipe><tags><tag>sweet</tag><tag>quick</tag><tag>sweet</tag>"
            + "<tag>breakfast</tag></tags></recipe>"));
    print("tags back", tagsBack.getTags());
    print("set class", tagsBack.getTags().getClass().getName());

    // a Set field without an initial value: JAXB creates a HashSet
    print("HashSet created by JAXB", JAXBContext.newInstance(PlainTaggedRecipe.class)
        .createUnmarshaller().unmarshal(new StringReader(
            "<recipe><tags><tag>sweet</tag><tag>quick</tag><tag>sweet</tag>"
                + "<tag>breakfast</tag></tags></recipe>")));

    // 6. Map without and with XmlAdapter
    Pantry pantry = new Pantry();
    pantry.getStock().put("apple", 5);
    pantry.getStock().put("banana", 3);
    JAXBContext pantryContext = JAXBContext.newInstance(Pantry.class);
    String pantryXml = toXml(pantryContext, pantry);
    section("Map default", pantryXml);
    print("Map default back", pantryContext.createUnmarshaller().unmarshal(new StringReader(pantryXml)));

    AdaptedPantry adapted = new AdaptedPantry();
    adapted.getStock().put("apple", 5);
    adapted.getStock().put("banana", 3);
    JAXBContext adaptedContext = JAXBContext.newInstance(AdaptedPantry.class);
    String adaptedXml = toXml(adaptedContext, adapted);
    section("Map with XmlAdapter", adaptedXml);
    print("Map adapter back", adaptedContext.createUnmarshaller().unmarshal(new StringReader(adaptedXml)));

    // 7. Empty and null collections
    Recipe empty = new Recipe("Toast");                    // empty list
    section("wrapper, empty list", toXml(context, empty));
    Recipe nullList = new Recipe("Toast");
    nullList.setIngredients(null);
    section("wrapper, null list", toXml(context, nullList));

    FlatRecipe flatEmpty = new FlatRecipe("Toast");
    section("no wrapper, empty list", toXml(flatContext, flatEmpty));
    FlatRecipe flatNull = new FlatRecipe("Toast");
    flatNull.setIngredients(null);
    section("no wrapper, null list", toXml(flatContext, flatNull));

    JAXBContext nilContext = JAXBContext.newInstance(NillableRecipe.class);
    section("nillable, null list", toXml(nilContext, new NillableRecipe()));
    NillableRecipe nilEmpty = new NillableRecipe();
    nilEmpty.setIngredients(new ArrayList<>());
    section("nillable, empty list", toXml(nilContext, nilEmpty));

    // reading empty and missing collections back
    Unmarshaller u = context.createUnmarshaller();
    print("<ingredients/> into Recipe",
        u.unmarshal(new StringReader("<recipe name=\"Toast\"><ingredients/></recipe>")));
    print("no <ingredients> into Recipe",
        u.unmarshal(new StringReader("<recipe name=\"Toast\"/>")));
    print("xsi:nil into Recipe", u.unmarshal(new StringReader(
        "<recipe name=\"Toast\"><ingredients xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/></recipe>")));
    Unmarshaller nu = nilContext.createUnmarshaller();
    print("no <ingredients> into NillableRecipe",
        nu.unmarshal(new StringReader("<recipe/>")));
    NillableRecipe nilBack = (NillableRecipe) nu.unmarshal(new StringReader(
        "<recipe><ingredients><ingredient name=\"flour\" grams=\"200\"/></ingredients></recipe>"));
    print("List created by JAXB", nilBack.getIngredients().getClass().getName());
    print("<ingredients/> into NillableRecipe",
        nu.unmarshal(new StringReader("<recipe><ingredients/></recipe>")));
    print("xsi:nil into NillableRecipe", nu.unmarshal(new StringReader(
        "<recipe><ingredients xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/></recipe>")));
  }

  static String toXml(JAXBContext context, Object value) throws JAXBException {
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);   // no XML declaration
    StringWriter out = new StringWriter();
    marshaller.marshal(value, out);
    return out.toString();
  }

  static void section(String label, Object value) {
    System.out.println("--- " + label + " ---");
    System.out.println(value);
  }

  static void print(String label, Object value) {
    System.out.println(label + " -> " + value);
  }

  interface Action {
    Object run() throws Exception;
  }

  static void printError(String label, Action action) {
    try {
      print(label, action.run());
    } catch (Exception e) {
      print(label, e.getClass().getName() + ": " + e.getMessage());
      if (e instanceof JAXBException je && je.getLinkedException() != null) {
        Throwable linked = je.getLinkedException();
        print(label + " (linked)", linked.getClass().getName() + ": " + linked.getMessage());
      }
    }
  }
}
