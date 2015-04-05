#Jackson Patch

This library provides convenience methods to perform JSON patches in accordance with [RFC6902](http://tools.ietf.org/html/rfc6902)
using [Jackson](http://wiki.fasterxml.com/JacksonHome).

```xml
<dependency>
    <groupId>io.progix.jackson</groupId>
    <artifactId>jackson-patch</artifactId>
    <version>0.1.0</version>
</dependency>
```

##Getting started

All patch operations can be performed using the `JsonPatch` class's static methods. All of these methods use Jackson 
objects to perform a patch operation (notably `JsonPointer` for paths and `JsonNode` for target documents and values).

To create a `JsonPointer` from a string path, use `JsonPointer#compile(String)`.

To create `JsonNode` from objects, use Jackson's `ObjectMapper#convertValue(Object, Class)` where the class is 
`JsonNode.class`.

##Example
```java
ObjectMapper mapper = new ObjectMapper();
JsonNode person = mapper.convertValue(new Person(), JsonNode.class);

JsonPointer path = JsonPointer.compile("/pets");
JsonNode dog = mapper.convertValue(new Pet(), JsonNode.class);

JsonPatch.add(path, dog, person);

```

All individual operations are supported in `JsonPatch`, including a batch patch operation with multiple `JsonPatchOperation`, 
a representation of the RFC6902 patch operation. 

##Errors
Errors will thrown if a patch operation fails as defined in the RFC.