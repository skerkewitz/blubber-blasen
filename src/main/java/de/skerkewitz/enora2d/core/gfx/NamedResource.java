package de.skerkewitz.enora2d.core.gfx;

import java.util.Objects;

public final class NamedResource {

  public final String name;
  public final SpriteSheet sheet;
  public final boolean directColor;

  public NamedResource(String name, SpriteSheet sheet, boolean directColor) {
    this.name = name;
    this.sheet = sheet;
    this.directColor = directColor;
  }

  @Override
  public String toString() {
    return "NamedResource{" +
            "name='" + name + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NamedResource)) return false;
    NamedResource that = (NamedResource) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
