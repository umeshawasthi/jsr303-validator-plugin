jsr303-validator-plugin
=======================

JSR-303 standardizes validation constraint declaration and metadata for the Java platform. Using this API, 
you annotate domain model properties with declarative validation constraints and the runtime enforces them. 
There are a number of built-in constraints you can take advantage of. You may also define your own custom constraints. 

To illustrate, consider a simple PersonForm model with two properties:

<code>
public class PersonForm {
    private String name;
    private int age;
	
}
</code>
JSR-303 allows you to define declarative validation constraints against such properties.

<code>
public class PersonForm {

    @NotNull
    @Size(max=64)
    private String name;

    @Min(0)
    private int age;

}
</code>
When an instance of this class is validated by a JSR-303 Validator, these constraints will be enforced. 

A JSR-303 Bean Validation Plguin for Struts2.
This Plugin work as a bridge between Struts2 request flow and JSR-303 Compliant bean Validator like Hibernate Validator.

This plugin itself do not provide JSR-303 Specific validationn but will use underlying validator to perform bean validation.

For general information on JSR-303, see the Bean Validation Specification. 
For information on the specific capabilities of the default reference implementation, see the Hibernate Validator documentation.

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-12895099-2', 'github.com');
  ga('send', 'pageview');

</script>