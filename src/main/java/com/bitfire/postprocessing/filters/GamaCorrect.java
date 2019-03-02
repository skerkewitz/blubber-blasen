/*******************************************************************************
 * Copyright 2012 bmanuel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.utils.ShaderLoader;

public final class GamaCorrect extends Filter<GamaCorrect> {

  private Texture inputTexture2 = null;

  public GamaCorrect() {
    super(ShaderLoader.fromFile("screenspace", "gama-correct"));
    rebind();
  }

  public GamaCorrect setInput(FrameBuffer buffer1) {
    this.inputTexture = buffer1.getColorBufferTexture();
    return this;
  }

  public GamaCorrect setInput(Texture texture1) {
    this.inputTexture = texture1;
    return this;
  }


  @Override
  public void rebind() {
    setParams(Param.Texture0, u_texture0);
    endParams();
  }

  @Override
  protected void onBeforeRender() {
    inputTexture.bind(u_texture0);
  }

  public enum Param implements Parameter {
    // @formatter:off
    Texture0("u_texture0", 0);
    // @formatter:on

    private final String mnemonic;
    private int elementSize;

    Param(String m, int elementSize) {
      this.mnemonic = m;
      this.elementSize = elementSize;
    }

    @Override
    public String mnemonic() {
      return this.mnemonic;
    }

    @Override
    public int arrayElementSize() {
      return this.elementSize;
    }
  }
}
