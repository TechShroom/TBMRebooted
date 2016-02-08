// What the fuck is this for.
// package com.techshroom.mods.tbm.render;
//
// import java.nio.ByteBuffer;
// import java.nio.ByteOrder;
// import java.nio.FloatBuffer;
// import java.nio.IntBuffer;
// import java.nio.ShortBuffer;
// import java.util.Arrays;
//
// import org.lwjgl.opengl.GL11;
//
// import net.minecraft.client.renderer.GLAllocation;
// import net.minecraft.client.renderer.OpenGlHelper;
// import net.minecraft.client.renderer.WorldRenderer;
// import net.minecraft.client.renderer.vertex.VertexFormat;
//
// public class WorldRenderDirect extends WorldRenderer {
//
// public WorldRenderDirect() {
// super(0);
// }
//
// private static int nativeBufferSize = 0x200000;
// public static boolean renderingWorldRenderer = false;
// public boolean defaultTexture = false;
// private int rawBufferSize = 0;
// public int textureID = 0;
//
// /** The byte buffer used for GL allocation. */
// private static ByteBuffer byteBuffer =
// GLAllocation.createDirectByteBuffer(nativeBufferSize * 4);
// /** The same memory as byteBuffer, but referenced as an integer buffer. */
// private static IntBuffer intBuffer = byteBuffer.asIntBuffer();
// /** The same memory as byteBuffer, but referenced as an float buffer. */
// private static FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
// /** The same memory as byteBuffer, but referenced as an short buffer. */
// private static ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
// /** Raw integer array. */
// private int[] rawBuffer;
// /**
// * The number of vertices to be drawn in the next draw call. Reset to 0
// * between draw calls.
// */
// private int vertexCount;
// /** The first coordinate to be used for the texture. */
// private double textureU;
// /** The second coordinate to be used for the texture. */
// private double textureV;
// private int brightness;
// /** The color (RGBA) value to be used for the following draw call. */
// private int color;
// /**
// * Whether the current draw object for this tessellator has color values.
// */
// private boolean hasColor;
// /**
// * Whether the current draw object for this tessellator has texture
// * coordinates.
// */
// private boolean hasTexture;
// private boolean hasBrightness;
// /**
// * Whether the current draw object for this tessellator has normal values.
// */
// private boolean hasNormals;
// /** The index into the raw buffer to be used for the next data. */
// private int rawBufferIndex;
// /** Disables all color information for the following draw call. */
// private boolean isColorDisabled;
// /** The draw mode currently being used by the tessellator. */
// private int drawMode;
// /**
// * An offset to be applied along the x-axis for all vertices in this draw
// * call.
// */
// private double xOffset;
// /**
// * An offset to be applied along the y-axis for all vertices in this draw
// * call.
// */
// private double yOffset;
// /**
// * An offset to be applied along the z-axis for all vertices in this draw
// * call.
// */
// private double zOffset;
// /** The normal to be applied to the face being drawn. */
// private int normal;
// /** Whether this tessellator is currently in draw mode. */
// private boolean isDrawing;
// /** The size of the buffers used (in integers). */
//
// /**
// * Draws the data set up in this tessellator and resets the state to prepare
// * for new drawing.
// */
// public int draw() {
// if (!this.isDrawing) {
// throw new IllegalStateException("Not tesselating!");
// } else {
// this.isDrawing = false;
//
// int offs = 0;
// while (offs < this.vertexCount) {
// int vtc = Math.min(this.vertexCount - offs,
// nativeBufferSize >> 5);
// WorldRenderDirect.intBuffer.clear();
// WorldRenderDirect.intBuffer.put(this.rawBuffer, offs * 8,
// vtc * 8);
// WorldRenderDirect.byteBuffer.position(0);
// WorldRenderDirect.byteBuffer.limit(vtc * 32);
// offs += vtc;
//
// if (this.hasTexture) {
// WorldRenderDirect.floatBuffer.position(3);
// GL11.glTexCoordPointer(2, 32,
// WorldRenderDirect.floatBuffer);
// GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
// }
//
// if (this.hasBrightness) {
// OpenGlHelper.setClientActiveTexture(
// OpenGlHelper.lightmapTexUnit);
// WorldRenderDirect.shortBuffer.position(14);
// GL11.glTexCoordPointer(2, 32,
// WorldRenderDirect.shortBuffer);
// GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
// OpenGlHelper.setClientActiveTexture(
// OpenGlHelper.defaultTexUnit);
// }
//
// if (this.hasColor) {
// WorldRenderDirect.byteBuffer.position(20);
// GL11.glColorPointer(4, true, 32,
// WorldRenderDirect.byteBuffer);
// GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
// }
//
// if (this.hasNormals) {
// WorldRenderDirect.byteBuffer.position(24);
// GL11.glNormalPointer(32, WorldRenderDirect.byteBuffer);
// GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
// }
//
// WorldRenderDirect.floatBuffer.position(0);
// GL11.glVertexPointer(3, 32, WorldRenderDirect.floatBuffer);
// GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
// GL11.glDrawArrays(this.drawMode, 0, vtc);
// GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
//
// if (this.hasTexture) {
// GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
// }
//
// if (this.hasBrightness) {
// OpenGlHelper.setClientActiveTexture(
// OpenGlHelper.lightmapTexUnit);
// GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
// OpenGlHelper.setClientActiveTexture(
// OpenGlHelper.defaultTexUnit);
// }
//
// if (this.hasColor) {
// GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
// }
//
// if (this.hasNormals) {
// GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
// }
// }
//
// if (this.rawBufferSize > 0x20000
// && this.rawBufferIndex < (this.rawBufferSize << 3)) {
// this.rawBufferSize = 0x10000;
// this.rawBuffer = new int[this.rawBufferSize];
// }
//
// int i = this.rawBufferIndex * 4;
// this.reset();
// return i;
// }
// }
//
// /*
// * @Override public TesselatorVertexState getVertexState(float p_147564_1_,
// * float p_147564_2_, float p_147564_3_) { int[] aint = new
// * int[this.rawBufferIndex]; PriorityQueue priorityqueue = new
// * PriorityQueue(this.rawBufferIndex,
// *
// * new QuadComparator(this.rawBuffer, p_147564_1_ + (float) this.xOffset,
// * p_147564_2_ + (float) this.yOffset, p_147564_3_ + (float) this.zOffset));
// * byte b0 = 32; int i;
// *
// * for (i = 0; i < this.rawBufferIndex; i += b0) {
// * priorityqueue.add(Integer.valueOf(i)); }
// *
// * for (i = 0; !priorityqueue.isEmpty(); i += b0) { int j = ((Integer)
// * priorityqueue.remove()).intValue();
// *
// * for (int k = 0; k < b0; ++k) { aint[i + k] = this.rawBuffer[j + k]; } }
// *
// * System.arraycopy(aint, 0, this.rawBuffer, 0, aint.length); return new
// * TesselatorVertexState(aint, this.rawBufferIndex, this.vertexCount,
// * this.hasTexture, this.hasBrightness, this.hasNormals, this.hasColor); }
// *
// * public void setVertexState(TesselatorVertexState p_147565_1_) { while
// * (p_147565_1_.getRawBuffer().length > this.rawBufferSize &&
// * this.rawBufferSize > 0) { this.rawBufferSize <<= 1; } if
// * (this.rawBufferSize > this.rawBuffer.length) { this.rawBuffer = new
// * int[this.rawBufferSize]; } System.arraycopy(p_147565_1_.getRawBuffer(),
// * 0, this.rawBuffer, 0, p_147565_1_.getRawBuffer().length);
// * this.rawBufferIndex = p_147565_1_.getRawBufferIndex(); this.vertexCount =
// * p_147565_1_.getVertexCount(); this.hasTexture =
// * p_147565_1_.getHasTexture(); this.hasBrightness =
// * p_147565_1_.getHasBrightness(); this.hasColor =
// * p_147565_1_.getHasColor(); this.hasNormals = p_147565_1_.getHasNormals();
// * }
// */
//
// /**
// * Clears the tessellator state in preparation for new drawing.
// */
// @Override
// public void reset() {
// this.vertexCount = 0;
// WorldRenderDirect.byteBuffer.clear();
// this.rawBufferIndex = 0;
// }
//
// /**
// * Sets draw mode in the tessellator to draw quads.
// */
// @Override
// public void startDrawingQuads() {
// this.startDrawing(7);
// }
//
// /**
// * Resets tessellator state and prepares for drawing (with the specified
// * draw mode).
// */
// @Override
// public void startDrawing(int p_78371_1_) {
// if (this.isDrawing) {
// throw new IllegalStateException("Already tesselating!");
// } else {
// this.isDrawing = true;
// this.reset();
// this.drawMode = p_78371_1_;
// this.hasNormals = false;
// this.hasColor = false;
// this.hasTexture = false;
// this.hasBrightness = false;
// this.isColorDisabled = false;
// }
// }
//
// /**
// * Sets the texture coordinates.
// */
// @Override
// public void setTextureUV(double p_78385_1_, double p_78385_3_) {
// this.hasTexture = true;
// this.textureU = p_78385_1_;
// this.textureV = p_78385_3_;
// }
//
// @Override
// public void setBrightness(int p_78380_1_) {
// this.hasBrightness = true;
// this.brightness = p_78380_1_;
// }
//
// /**
// * Sets the RGB values as specified, converting from floats between 0 and 1
// * to integers from 0-255.
// */
// @Override
// public void setColorOpaque_F(float p_78386_1_, float p_78386_2_,
// float p_78386_3_) {
// this.setColorOpaque((int) (p_78386_1_ * 255.0F),
// (int) (p_78386_2_ * 255.0F), (int) (p_78386_3_ * 255.0F));
// }
//
// /**
// * Sets the RGBA values for the color, converting from floats between 0 and
// * 1 to integers from 0-255.
// */
// @Override
// public void setColorRGBA_F(float p_78369_1_, float p_78369_2_,
// float p_78369_3_, float p_78369_4_) {
// this.setColorRGBA((int) (p_78369_1_ * 255.0F),
// (int) (p_78369_2_ * 255.0F), (int) (p_78369_3_ * 255.0F),
// (int) (p_78369_4_ * 255.0F));
// }
//
// /**
// * Sets the RGB values as specified, and sets alpha to opaque.
// */
// @Override
// public void setColorOpaque(int p_78376_1_, int p_78376_2_, int p_78376_3_) {
// this.setColorRGBA(p_78376_1_, p_78376_2_, p_78376_3_, 255);
// }
//
// /**
// * Sets the RGBA values for the color. Also clamps them to 0-255.
// */
// @Override
// public void setColorRGBA(int p_78370_1_, int p_78370_2_, int p_78370_3_,
// int p_78370_4_) {
// if (!this.isColorDisabled) {
// if (p_78370_1_ > 255) {
// p_78370_1_ = 255;
// }
//
// if (p_78370_2_ > 255) {
// p_78370_2_ = 255;
// }
//
// if (p_78370_3_ > 255) {
// p_78370_3_ = 255;
// }
//
// if (p_78370_4_ > 255) {
// p_78370_4_ = 255;
// }
//
// if (p_78370_1_ < 0) {
// p_78370_1_ = 0;
// }
//
// if (p_78370_2_ < 0) {
// p_78370_2_ = 0;
// }
//
// if (p_78370_3_ < 0) {
// p_78370_3_ = 0;
// }
//
// if (p_78370_4_ < 0) {
// p_78370_4_ = 0;
// }
//
// this.hasColor = true;
//
// if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
// this.color = p_78370_4_ << 24 | p_78370_3_ << 16
// | p_78370_2_ << 8 | p_78370_1_;
// } else {
// this.color = p_78370_1_ << 24 | p_78370_2_ << 16
// | p_78370_3_ << 8 | p_78370_4_;
// }
// }
// }
//
// public void func_154352_a(byte p_154352_1_, byte p_154352_2_,
// byte p_154352_3_) {
// this.setColorOpaque(p_154352_1_ & 255, p_154352_2_ & 255,
// p_154352_3_ & 255);
// }
//
// /**
// * Adds a vertex specifying both x,y,z and the texture u,v for it.
// */
// @Override
// public void addVertexWithUV(double p_78374_1_, double p_78374_3_,
// double p_78374_5_, double p_78374_7_, double p_78374_9_) {
// this.setTextureUV(p_78374_7_, p_78374_9_);
// this.addVertex(p_78374_1_, p_78374_3_, p_78374_5_);
// }
//
// /**
// * Adds a vertex with the specified x,y,z to the current draw call. It will
// * trigger a draw() if the buffer gets full.
// */
// @Override
// public void addVertex(double p_78377_1_, double p_78377_3_,
// double p_78377_5_) {
// if (this.rawBufferIndex >= this.rawBufferSize - 32) {
// if (this.rawBufferSize == 0) {
// this.rawBufferSize = 0x10000;
// this.rawBuffer = new int[this.rawBufferSize];
// } else {
// this.rawBufferSize *= 2;
// this.rawBuffer =
// Arrays.copyOf(this.rawBuffer, this.rawBufferSize);
// }
// }
//
// if (this.hasTexture) {
// this.rawBuffer[this.rawBufferIndex + 3] =
// Float.floatToRawIntBits((float) this.textureU);
// this.rawBuffer[this.rawBufferIndex + 4] =
// Float.floatToRawIntBits((float) this.textureV);
// }
//
// if (this.hasBrightness) {
// this.rawBuffer[this.rawBufferIndex + 7] = this.brightness;
// }
//
// if (this.hasColor) {
// this.rawBuffer[this.rawBufferIndex + 5] = this.color;
// }
//
// if (this.hasNormals) {
// this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
// }
//
// this.rawBuffer[this.rawBufferIndex + 0] =
// Float.floatToRawIntBits((float) (p_78377_1_ + this.xOffset));
// this.rawBuffer[this.rawBufferIndex + 1] =
// Float.floatToRawIntBits((float) (p_78377_3_ + this.yOffset));
// this.rawBuffer[this.rawBufferIndex + 2] =
// Float.floatToRawIntBits((float) (p_78377_5_ + this.zOffset));
// this.rawBufferIndex += 8;
// ++this.vertexCount;
// }
//
// /**
// * Sets the color to the given opaque value (stored as byte values packed in
// * an integer).
// */
// @Override
// public void setColorOpaque_I(int p_78378_1_) {
// int j = p_78378_1_ >> 16 & 255;
// int k = p_78378_1_ >> 8 & 255;
// int l = p_78378_1_ & 255;
// this.setColorOpaque(j, k, l);
// }
//
// /**
// * Sets the color to the given color (packed as bytes in integer) and alpha
// * values.
// */
// @Override
// public void setColorRGBA_I(int p_78384_1_, int p_78384_2_) {
// int k = p_78384_1_ >> 16 & 255;
// int l = p_78384_1_ >> 8 & 255;
// int i1 = p_78384_1_ & 255;
// this.setColorRGBA(k, l, i1, p_78384_2_);
// }
//
// /**
// * Disables colors for the current draw call.
// */
// public void disableColor() {
// this.isColorDisabled = true;
// }
//
// /**
// * Sets the normal for the current draw call.
// */
// @Override
// public void setNormal(float p_78375_1_, float p_78375_2_,
// float p_78375_3_) {
// this.hasNormals = true;
// byte b0 = (byte) ((int) (p_78375_1_ * 127.0F));
// byte b1 = (byte) ((int) (p_78375_2_ * 127.0F));
// byte b2 = (byte) ((int) (p_78375_3_ * 127.0F));
// this.normal = b0 & 255 | (b1 & 255) << 8 | (b2 & 255) << 16;
// }
//
// /**
// * Sets the translation for all vertices in the current draw call.
// */
// @Override
// public void setTranslation(double p_78373_1_, double p_78373_3_,
// double p_78373_5_) {
// this.xOffset = p_78373_1_;
// this.yOffset = p_78373_3_;
// this.zOffset = p_78373_5_;
// }
//
// /**
// * Offsets the translation for all vertices in the current draw call.
// */
// public void addTranslation(float p_78372_1_, float p_78372_2_,
// float p_78372_3_) {
// this.xOffset += (double) p_78372_1_;
// this.yOffset += (double) p_78372_2_;
// this.zOffset += (double) p_78372_3_;
// }
//
// @Override
// public State getVertexState(float p_178971_1_, float p_178971_2_,
// float p_178971_3_) {
// throw new UnsupportedOperationException();
// }
//
// @Override
// public void setVertexState(State p_178993_1_) {
// throw new UnsupportedOperationException();
// }
//
// @Override
// public void putBrightness4(int p_178962_1_, int p_178962_2_,
// int p_178962_3_, int p_178962_4_) {
// // TODO Auto-generated method stub
// super.putBrightness4(p_178962_1_, p_178962_2_, p_178962_3_,
// p_178962_4_);
// }
//
// @Override
// public void putPosition(double p_178987_1_, double p_178987_3_,
// double p_178987_5_) {
// // TODO Auto-generated method stub
// super.putPosition(p_178987_1_, p_178987_3_, p_178987_5_);
// }
//
// @Override
// public int getColorIndex(int p_78909_1_) {
// // TODO Auto-generated method stub
// return super.getColorIndex(p_78909_1_);
// }
//
// @Override
// public void putColorMultiplier(float p_178978_1_, float p_178978_2_,
// float p_178978_3_, int p_178978_4_) {
// // TODO Auto-generated method stub
// super.putColorMultiplier(p_178978_1_, p_178978_2_, p_178978_3_,
// p_178978_4_);
// }
//
// @Override
// public void putColorRGB_F(float p_178994_1_, float p_178994_2_,
// float p_178994_3_, int p_178994_4_) {
// // TODO Auto-generated method stub
// super.putColorRGB_F(p_178994_1_, p_178994_2_, p_178994_3_, p_178994_4_);
// }
//
// @Override
// public void putColorRGBA(int p_178972_1_, int p_178972_2_, int p_178972_3_,
// int p_178972_4_, int p_178972_5_) {
// // TODO Auto-generated method stub
// super.putColorRGBA(p_178972_1_, p_178972_2_, p_178972_3_, p_178972_4_,
// p_178972_5_);
// }
//
// @Override
// public void setColorOpaque_B(byte p_178982_1_, byte p_178982_2_,
// byte p_178982_3_) {
// // TODO Auto-generated method stub
// super.setColorOpaque_B(p_178982_1_, p_178982_2_, p_178982_3_);
// }
//
// @Override
// public void setVertexFormat(VertexFormat newFormat) {
// // TODO Auto-generated method stub
// super.setVertexFormat(newFormat);
// }
//
// @Override
// public void addVertexData(int[] vertexData) {
// // TODO Auto-generated method stub
// super.addVertexData(vertexData);
// }
//
// @Override
// public void markDirty() {
// // TODO Auto-generated method stub
// super.markDirty();
// }
//
// @Override
// public void putNormal(float p_178975_1_, float p_178975_2_,
// float p_178975_3_) {
// super.putNormal(p_178975_1_, p_178975_2_, p_178975_3_);
// }
//
// @Override
// public void finishDrawing() {
// super.finishDrawing();
// }
//
// @Override
// public int getByteIndex() {
// // TODO Auto-generated method stub
// return super.getByteIndex();
// }
//
// @Override
// public ByteBuffer getByteBuffer() {
// // TODO Auto-generated method stub
// return super.getByteBuffer();
// }
//
// @Override
// public VertexFormat getVertexFormat() {
// // TODO Auto-generated method stub
// return super.getVertexFormat();
// }
//
// @Override
// public int getVertexCount() {
// // TODO Auto-generated method stub
// return super.getVertexCount();
// }
//
// @Override
// public int getDrawMode() {
// // TODO Auto-generated method stub
// return super.getDrawMode();
// }
//
// @Override
// public void putColor4(int p_178968_1_) {
// // TODO Auto-generated method stub
// super.putColor4(p_178968_1_);
// }
//
// @Override
// public void putColorRGB_F4(float p_178990_1_, float p_178990_2_,
// float p_178990_3_) {
// // TODO Auto-generated method stub
// super.putColorRGB_F4(p_178990_1_, p_178990_2_, p_178990_3_);
// }
//
// }
