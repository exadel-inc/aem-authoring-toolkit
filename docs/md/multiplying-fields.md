[Main page](../../README.md)

## Multiplying fields

### MultiField

* [@MultiField](https://javadoc.io/doc/com.exadel.etoolbox/etoolbox-authoring-kit-core/latest/com/exadel/aem/toolkit/api/annotations/widgets/MultiField.html)
* Resource type: /libs/granite/ui/components/coral/foundation/form/multifield
* See spec: [Multifield](https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/reference-materials/granite-ui/api/jcr_root/libs/granite/ui/components/coral/foundation/form/multifield/index.html)

Multifields are used to facilitate multiple (repeating) instances of same fields or groups of fields. The logic of this component relies on the presence of a nested class encapsulating one or more fields to be repeated.

Reference to that class is passed to `@MultiField`'s *value* property. Just as for `@FieldSet`, if you omit this value, it is guessed from the underlying field type, be it a *SomePlainType* or a *Collection\<WithTypeParameter>*.

Multifields allow you to specify the `deleteHint` (true/false) or `typeHint` values that will produce HTTP request parameters in line with Apache Sling specification for [@Delete](https://sling.apache.org/documentation/bundles/manipulating-content-the-slingpostservlet-servlets-post.html#delete) and [@TypeHint](https://sling.apache.org/documentation/bundles/manipulating-content-the-slingpostservlet-servlets-post.html#typehint).

See below how it works for a single field repetition and for a subset of fields multiplied.

#### Simple multifield

```java
public class SimpleMultiFieldDialog {
    @DialogField(label = "Multi")
    @MultiField(
        deleteHint = true, // Optional. Use this if you want the corresponding value(-s) deleted
        typeHint = "String[]" // Optional. If specified, it will become the Sling @TypeHint parameter
    )
    List<MultiFieldContainer> containers;

    static class MultiFieldContainer {
        @DialogField
        @TextField
        String dialogItem;
    }
}
```

#### Composite multi field

```java
public class CompositeMultiFieldDialog {
    @DialogField
    @MultiField(MultiCompositeField.class)
    String multiComposite;

    private static class MultiCompositeField {
        @DialogField
        @TextField
        String multiText;

        @DialogField
        @Checkbox(text = "Multi CheckBox")
        String checkboxMulti;
    }
}
```
Note that the inheritance of class(-es) encapsulating multifield items works here the same way as for the `@FieldSet`.

### Multiple

The easiest way to create a *MultiField* is with the `@Multiple` annotation. Just add it to the Java class field where a widget annotation is already present. A *simple multifield* containing this particular widget will be created on the fly.

On the other hand, if you add `@Multiple` to a field marked with `@FieldSet`, a *composite multifield* will be created (much like the one you could have adding `@Multifield` annotation itself). Moreover, you can add `@Multiple` to a mere `@Multifield`-marked field and enjoy a sort of "multifield of multifields."

Please note, however, that `@Multiple` is primarily designed for easy, "quick give me a multifield out of my single widget" cases. For more complicated cases, it lacks tweaking capabilities that `@MultiField` itself allows.

***
#### See also
- [Grouping fields with FieldSet](configuring-fieldset.md)
