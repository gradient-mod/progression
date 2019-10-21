package gradientmod.progression;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum Age {
  AGE1, AGE2, AGE3, AGE4;

  private ResourceLocation icon;

  Age() {
    this.icon = Progression.loc("textures/icons/age" + this.value() + ".png");
  }

  public int value() {
    return this.ordinal() + 1;
  }

  public String translationKey() {
    return "age." + this.value();
  }

  public ITextComponent getDisplayName() {
    return new TranslationTextComponent(this.translationKey());
  }

  public ResourceLocation getIcon() {
    return this.icon;
  }

  public static Age get(final int age) {
    if(age < 1 || age > Age.values().length) {
      throw new IndexOutOfBoundsException("Age must be between 1 and " + Age.values().length + " inclusive");
    }

    return Age.values()[age - 1];
  }

  public static Age highest() {
    return values()[values().length - 1];
  }
}
