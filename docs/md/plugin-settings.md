[Main page](../../README.md)
## Plugin settings
### "terminateOn" setting

Specifies the list of exceptions, comma-separated, that would cause this plugin to terminate
the build process.

Each item may present:
- a particular exception, by its fully qualified name like `java.io.IOException`. When a singular exception is specified, all subclasses of the provided class also count;
- or a package where multiple exceptions reside, like `com.exadel.aem.plugin.exceptions.*`.

Apart from this, you may specify the values `all` (alias `*`) and `none`.
If an exception or a group of exceptions must be explicitly neglected, `!` should be prepended to the item.

Exception patterns are considered in order. Earlier patterns are applied before later patterns. For example, if `java.*, !java.lang.RuntimeException` provided, and a `NullPointerException` is thrown, the second ("negated") pattern will have no effect since any exception originating from the `java` package has already been accounted by the first pattern. That is why, if you need to define a scope of exceptions that would cause termination but need to explicitly exclude some items from that scope, put "negated" patterns in the first place.

It is also considered a good practice to end the enumeration with a default "fallback" pattern, typically `*`, if there are exclusions on the list. So, `<terminateOn>!java.lang.NullPointerException, !com.exadel.aem.plugin.exceptions.*,java.lang.RuntimeException</terminateOn>`, or `<terminateOn>!java.lang.RuntimeException, !iava.io.IOException, *</terminateOn>` would be some good samples.