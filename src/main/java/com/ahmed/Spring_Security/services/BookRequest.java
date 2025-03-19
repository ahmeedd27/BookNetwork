package com.ahmed.Spring_Security.services;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(
      Integer id,
      @NotNull(message = "100")
      @NotEmpty(message = "100")
      String title ,
      @NotNull(message = "101")
      @NotEmpty(message = "101")
      String authorName,
      @NotNull(message = "102")
      @NotEmpty(message = "102")
      String isbn,
      @NotNull(message = "103")
      @NotEmpty(message = "103")
      String synopsis,
      boolean shareable
) {}


/*public class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

Why Use record Instead of class?
A normal class for storing data requires a lot of boilerplate code:

Fields
Constructor
Getters
toString()
equals() and hashCode()

public record Person(String name, int age) {}
✔️ Same functionality, but much shorter!
Key Features of record
Immutable by Default 🛑

Fields are final (cannot be changed after creation).
No setters.
Auto-Generated Methods ✨

Java automatically creates a constructor, getters, toString(), equals(), and hashCode().
Compact & Readable 📝

Less code makes it easier to read and maintain.
When to Use record?
✅ When you need data-only objects (DTOs, responses, configurations).
✅ When you want immutable objects.
✅ When you want less boilerplate code.

❌ Do NOT use record if:

You need mutable objects (fields that change).
You need custom logic inside methods (records are for simple data storage).
 */


