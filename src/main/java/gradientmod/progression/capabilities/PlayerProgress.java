package gradientmod.progression.capabilities;

import gradientmod.progression.Age;

public class PlayerProgress {
  private Age age = Age.AGE1;

  public Age getAge() {
    return this.age;
  }

  public void setAge(final Age age) {
    this.age = age;
  }

  public boolean meetsAgeRequirement(final Age age) {
    return this.age.ordinal() >= age.ordinal();
  }
}
