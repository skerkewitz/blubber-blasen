package de.skerkewitz.enora2d.core.gfx;

import java.util.Objects;

public final class NamedResource {

  public final String name;

  public NamedResource(String name) {
    this.name = name;
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
