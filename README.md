[![Build Status](https://travis-ci.org/umeshawasthi/jsr303-validator-plugin.png?branch=master)](https://travis-ci.org/umeshawasthi/jsr303-validator-plugin)

jsr303-validator-plugin
=======================

JSR-303 standardizes validation constraint declaration and metadata for the Java platform. Using this API, 
you annotate domain model properties with declarative validation constraints and the runtime enforces them. 
There are a number of built-in constraints you can take advantage of. You may also define your own custom constraints. 

To illustrate, consider a simple PersonForm model with two properties:

```xml
public class PersonForm {
    private String name;
    private int age;
	
}
```
JSR-303 allows you to define declarative validation constraints against such properties.

```xml
public class PersonForm {

    @NotNull
    @Size(max=64)
    private String name;

    @Min(0)
    private int age;

}
```
When an instance of this class is validated by a JSR-303 Validator, these constraints will be enforced. 

A JSR-303 Bean Validation Plguin for Struts2.
This Plugin work as a bridge between Struts2 request flow and JSR-303 Compliant bean Validator like Hibernate Validator.

This plugin itself do not provide JSR-303 Specific validationn but will use underlying validator to perform bean validation.

For general information on JSR-303, see the Bean Validation Specification. 
For information on the specific capabilities of the default reference implementation, see the Hibernate Validator documentation.

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/umeshawasthi/jsr303-validator-plugin/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-12895099-2', 'github.com');
  ga('send', 'pageview');

</script>

# Introduction

# Getting Started #
This tutorial will give you introduction as how to use Struts2-jsr303 validation plugin.I am assuming that person has basic understanding about bean Validation API and its specifications.

## Step: Install

We need to install `Struts2 JSR303 bean validation Plugin`.You can either download plugin jar manually from [download page](https://github.com/umeshawasthi/jsr303-validator-plugin) and include reference implementation
and `validation-api` version 1.0.0.GA from [download validation-api 1.0.0](http://mvnrepository.com/artifact/javax.validation/validation-api/1.0.0.GA)
you can also use maven to take care of adding plugin to your `Struts2` application.Add following to you `pom.xml`

### Using Maven
```xml
<dependency>
  <groupId>com.github.umeshawasthi</groupId>
  <artifactId>struts2-jsr303-validation-plugin</artifactId>
  <version>1.0</version>
</dependency>
```

Plugin as been tested with following reference implementations
* Hibernate Validator 
* Apache BVal

However plugin will work with any reference implementations in accordance with bean validation 1.0 specifications.

## How to use with Struts2
In order to use Plugin with struts2, all you need to extends your package with `jsr303` and you are all set to use underling features of bean validation API like
```xml
 <package name="default" extends="jsr303">
    <default-interceptor-ref name="jsr303ValidationStack"/>
    // action mapping
 </package>
```
_Make sure to add Hibernate validator or Apache Bval in your Struts2 application_.Plugin does not provide any validation itself but work as a bridge between Struts2 and Bean Validation API

## Adding Bean validation API in your Application

  ###  Hibernate validator
```xml
 <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>4.3.1.Final</version>
        </dependency>
```
  Apache Bval validator
```xml
   <dependency>
	<groupId>org.apache.geronimo.specs</groupId>
	<artifactId>geronimo-validation_1.0_spec</artifactId>
	<version>1.1</version>
    </dependency>
    <dependency>
		<groupId>org.apache.bval</groupId>
		<artifactId>org.apache.bval.bundle</artifactId>
		<version>0.5</version>
    </dependency>
```
Plugin has been tested with 1.0 Specifications and not with 1.1, as Apache Bval 1.1 implementation is still under development.

## Validation Error Messages
Plugin utilize Struts2 Message resolving algorithm. You can create action specific files or can create single file for entire application.
You can even pass messages directly in the validation annotation.

`@Size(min=10, max=10, message="mobile.number")
 private String mobile;`

in above example `mobile.number` will work as key and plugin will search messages with given key in underlying resource bundles with help of Struts2.
You can directly provide messages in validation constraints itself

`@Size(min=10, max=10, message="Please provide a valid mobile number")
 private String mobile;`

In case of no message, plugin will fall back to default messages provided by Validation API.


