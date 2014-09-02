package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.SideConstants.*;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.techshroom.mods.tbm.block.tile.CPUConnectable;
import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class Tutils {

    @SideOnly(Side.CLIENT)
    public static final class Client {
        public static final class Draw {
            private static int[] genLengthArr(int len) {
                int[] out = new int[len];
                for (int i = 0; i < out.length; i++) {
                    out[i] = i;
                }
                return out;
            }

            private static void bindTexture(ResourceLocation res) {
                TextureManager manager =
                        TileEntityRendererDispatcher.instance.field_147553_e;
                if (manager != null) {
                    manager.bindTexture(res);
                }
            }

            public static void drawBlockAsEntity(Block b, World w, double x,
                    double y, double z) {
                RenderBlocks renderBlocks = new RenderBlocks(w);
                Tessellator tessellator = Tessellator.instance;
                bindTexture(TextureMap.locationBlocksTexture);
                RenderHelper.disableStandardItemLighting();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_CULL_FACE);

                if (Minecraft.isAmbientOcclusionEnabled()) {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                } else {
                    GL11.glShadeModel(GL11.GL_FLAT);
                }

                tessellator.startDrawingQuads();
                tessellator.setTranslation(x, y, z);
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                renderBlocks.renderBlockAllFaces(b, 0, 0, 0);

                tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                tessellator.draw();
                RenderHelper.enableStandardItemLighting();
            }

            public static double scalePixelToBlock(double pixel) {
                return pixel / 16f;
            }

            public static float scalePixelToBlock(float pixel) {
                return pixel / 16f;
            }

            public static PositionTextureVertex[] scaleToBlock(
                    PositionTextureVertex[] verts) {
                // copy it
                verts = verts.clone();
                for (int i = 0; i < verts.length; i++) {
                    Vec3 v3 = verts[i].vector3D;
                    v3.xCoord = scalePixelToBlock(v3.xCoord);
                    v3.yCoord = scalePixelToBlock(v3.yCoord);
                    v3.zCoord = scalePixelToBlock(v3.zCoord);
                }
                return verts;
            }

            /**
             * Like {@link GL11#glDrawElements(int, java.nio.IntBuffer)}, but
             * for {@link Tessellator}!
             * 
             * @param tessellator
             *            - the tessellator (note: {@link Tessellator#instance}
             *            is the only available instance, but that could
             *            change!)
             * @param verts
             *            - the vertices
             * @param index
             *            - the index array for the vert array
             */
            public static void tesselateElements(Tessellator tessellator,
                    PositionTextureVertex[] verts, int... index) {
                tesselateElementsWIthTextureSpecs(tessellator, verts, null,
                        index);
            }

            /**
             * Like {@link GL11#glDrawElements(int, java.nio.IntBuffer)}, but
             * for {@link Tessellator}! This version scales the given vertex
             * coords via {@link #scalePixelToBlock(double)}.
             * 
             * @param tessellator
             *            - the tessellator (note: {@link Tessellator#instance}
             *            is the only available instance, but that could
             *            change!)
             * @param verts
             *            - the vertices
             * @param index
             *            - the index array for the vert array
             */
            public static void tesselateElementsWithScale(
                    Tessellator tessellator, PositionTextureVertex[] verts,
                    int... index) {
                tesselateElements(tessellator, scaleToBlock(verts), index);
            }

            public static void tesselateElementsWithScaleAndTextureSpecs(
                    Tessellator tessellator, PositionTextureVertex[] verts,
                    Vector2f[] texCoords, int... index) {
                tesselateElementsWIthTextureSpecs(tessellator,
                        scaleToBlock(verts), texCoords, index);
            }

            public static void tesselateElementsWIthTextureSpecs(
                    Tessellator tessellator, PositionTextureVertex[] verts,
                    Vector2f[] texCoords, int... index) {
                if (index == null || index.length == 0) {
                    index = genLengthArr(verts.length);
                }
                if (texCoords == null || texCoords.length == 0) {
                    texCoords = new Vector2f[verts.length];
                    for (int i = 0; i < verts.length; i++) {
                        texCoords[i] =
                                new Vector2f(verts[i].texturePositionX,
                                        verts[i].texturePositionY);
                    }
                }
                if (texCoords.length < index.length
                        && texCoords.length == verts.length) {
                    Vector2f[] texNew = new Vector2f[index.length];
                    for (int i = 0; i < index.length; i++) {
                        texNew[i] = texCoords[index[i]];
                    }
                    texCoords = texNew;
                }
                if (texCoords.length != index.length) {
                    throw new IllegalArgumentException(
                            "texture array provided must match indexes length");
                }
                for (int i = 0; i < index.length; i++) {
                    PositionTextureVertex v = verts[index[i]];
                    Vec3 v3 = v.vector3D;
                    tessellator.addVertexWithUV(v3.xCoord, v3.yCoord,
                            v3.zCoord, texCoords[i].x, texCoords[i].y);
                }
            }

            private Draw() {
                throw new AssertionError("Nope.");
            }
        }

        public static boolean buttonIsPressed(int id, GuiButton check) {
            return check.enabled && check.id == id;
        }

        private Client() {
            throw new AssertionError("Nope.");
        }
    }

    public static final class IBS {
        private static class DistSort extends BlockSourceImpl implements
                Comparable<DistSort> {

            private int dSq = 0;

            public DistSort(World par1World, int par2, int par3, int par4,
                    IBlockSource from) {
                super(par1World, par2, par3, par4);
                dSq = distSq(this, from);
            }

            @Override
            public int compareTo(DistSort o) {
                if (IBS.equal(o, this)) {
                    return 0;
                }
                if (dSq > o.dSq) {
                    return 1;
                }
                return -1;
            }

        }

        public static Block block(IBlockSource ibs) {
            if (ibs == null) {
                return null;
            }
            return ibs.getWorld().getBlock(ibs.getXInt(), ibs.getYInt(),
                    ibs.getZInt());
        }

        public static String collectionAsString(
                Collection<? extends IBlockSource> c) {
            List<String> out = new ArrayList<String>(c.size());
            Iterator<? extends IBlockSource> i = c.iterator();
            while (i.hasNext()) {
                out.add(string(i.next()));
            }
            return out.toString();
        }

        @SuppressWarnings("unchecked")
        private static <T extends IBlockSource> T constr(Class<T> ibs, World w,
                int x, int y, int z) {
            try {
                Class<? extends IBlockSource> ibsClass = ibs;
                if (override != null) {
                    ibsClass = override;
                }
                Constructor<? extends IBlockSource> ibsConstr =
                        constrCache.get(ibsClass);
                if (ibsConstr == null) {
                    try {
                        ibsConstr =
                                ibsClass.getConstructor(World.class, int.class,
                                        int.class, int.class);
                        ibsConstr.setAccessible(true);
                    } catch (SecurityException e) {
                        System.err.println("[techshroom-util] "
                                + "SecurityException caught, falling back: "
                                + e.getMessage());
                    } catch (NoSuchMethodException e) {
                        System.err
                                .println("[techshroom-util] "
                                        + "Class "
                                        + ibsClass
                                        + " does not expose a constructor"
                                        + " with the parameters [World, int, int, int]!"
                                        + " Falling back to BlockSourceImpl.");
                    }
                    if (ibsConstr == null) {
                        ibsConstr = constrCache.get(BlockSourceImpl.class);
                    }
                }
                constrCache.put(ibsClass, ibsConstr);
                try {
                    return (T) ibsConstr.newInstance(w, x, y, z);
                } catch (IllegalArgumentException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            } catch (RuntimeException re) {
                throw re;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        public static boolean contains(List<? extends IBlockSource> list,
                IBlockSource check) {
            return IBS.indexOf(list, check) != -1;
        }

        public static int distSq(IBlockSource o1, IBlockSource o2) {
            int xo = o1.getXInt(), yo = o1.getYInt(), zo = o1.getZInt(), xt =
                    o2.getXInt(), yt = o2.getYInt(), zt = o2.getZInt();
            xo = xt - xo;
            yo = yt - yo;
            zo = zt - zo;
            int distSq1 = xo * xo + yo * yo + zo * zo;
            return distSq1;
        }

        public static boolean equal(IBlockSource ibs1, IBlockSource ibs2) {
            return ibs1 != null
                    && ibs2 != null
                    && (ibs1 == ibs2 || (ibs1.getWorld() == ibs2.getWorld()
                            && ibs1.getXInt() == ibs2.getXInt()
                            && ibs1.getYInt() == ibs2.getYInt() && ibs1
                            .getZInt() == ibs2.getZInt()));
        }

        public static int indexOf(List<? extends IBlockSource> list,
                IBlockSource check) {
            if (list != null) {
                int index = 0;
                for (IBlockSource c : list) {
                    if (equal(check, c)) {
                        return index;
                    }
                    index++;
                }
            }
            return -1;
        }

        public static <T extends IBlockSource> IBlockSource[] neighbors(T ibs) {
            IBlockSource[] n = new IBlockSource[6];
            Class<? extends IBlockSource> c = ibs.getClass();
            for (int i = 0; i < n.length; i++) {
                n[i] =
                        constr(c, ibs.getWorld(), ibs.getXInt()
                                + Facing.offsetsXForSide[i], ibs.getYInt()
                                + Facing.offsetsYForSide[i], ibs.getZInt()
                                + Facing.offsetsZForSide[i]);
            }
            return n;
        }

        public static void setOverride(Class<? extends IBlockSource> c) {
            override = c;
        }

        @SuppressWarnings("unchecked")
        public static <T extends IBlockSource> List<T> sortByDistFrom(
                IBlockSource loc, List<T> list) {
            List<DistSort> sort = new ArrayList<DistSort>();
            for (int i = 0; i < list.size(); i++) {
                T t = list.get(i);
                DistSort nt =
                        new DistSort(t.getWorld(), t.getXInt(), t.getYInt(),
                                t.getZInt(), loc);
                sort.add(nt);
            }
            Collections.sort(sort);
            for (int i = 0; i < sort.size(); i++) {
                T t = list.get(i);
                DistSort d = sort.get(i);
                list.set(
                        i,
                        (T) constr(t.getClass(), d.getWorld(), d.getXInt(),
                                d.getYInt(), d.getZInt()));
            }
            return list;
        }

        public static String string(IBlockSource ibs) {
            if (ibs == null) {
                return String.valueOf(null);
            }
            return ibs.getClass().getSimpleName()
                    + "["
                    + String.format("world=%s,x=%s,y=%s,z=%s", ibs.getWorld()
                            .getWorldInfo().getWorldName(), ibs.getXInt(),
                            ibs.getYInt(), ibs.getZInt()) + "]";
        }

        public static void unsetOverride() {
            override = null;
        }

        private static Map<Class<? extends IBlockSource>, Constructor<? extends IBlockSource>> constrCache =
                new HashMap<Class<? extends IBlockSource>, Constructor<? extends IBlockSource>>();

        static {
            Constructor<BlockSourceImpl> constr =
                    cast(BlockSourceImpl.class.getDeclaredConstructors()[0]);
            constrCache.put(BlockSourceImpl.class, constr);
            constrCache.put(IBlockSource.class, constr);
        }

        private static Class<? extends IBlockSource> override = null;

        private IBS() {
            throw new AssertionError("Nope.");
        }
    }

    public static class MCAddress {
        private final String id, object;

        private MCAddress(String d, String obj) {
            id = d;
            object = obj;
        }

        @Override
        public String toString() {
            return id + ":" + object;
        }
    }

    public static final class MetadataConstants {
        public static final int UPDATE = 1, SEND = 2, DONTRERENDER = 2;
        public static final int UPDATE_AND_SEND = UPDATE | SEND;

        private MetadataConstants() {
            throw new AssertionError("Nope.");
        }
    }

    public static final class SharedMethods {
        public static boolean CPUConnectable_sendUpdateRequestToCPU(
                TileEntity _this, CPUConnectable _thisAsConnectable,
                TileEntity[] dejaVu) {
            boolean thisIsSource =
                    _this.equals(_thisAsConnectable.getCPUTile());
            if (thisIsSource) {
                return mod().log.exit(true);
            }
            boolean noFailure = true;
            IBlockSource thisLoc =
                    new BlockSourceImpl(_this.getWorldObj(), _this.xCoord,
                            _this.yCoord, _this.zCoord);
            List<TileEntity> dejaList = Arrays.asList(dejaVu);
            IBlockSource[] surrounding = IBS.neighbors(thisLoc);
            for (IBlockSource around : surrounding) {
                TileEntity at = around.getBlockTileEntity();
                if (dejaList.contains(at)) {
                    // already checked
                    continue;
                }
                if (at instanceof CPUConnectable) {
                    CPUConnectable tileAsConnect = (CPUConnectable) at;
                    noFailure |=
                            tileAsConnect
                                    .sendUpdateRequestToCPU(CPUConnectable_extendByUs(
                                            _this, dejaVu));
                }
            }
            return mod().log.exit(noFailure);
        }

        public static boolean CPUConnectable_updateCPUConnections(
                TileEntity _this, CPUConnectable _thisAsConnectable,
                TileEntity[] dejaVu, TBMCPUTile backRef) {
            boolean thisIsSource =
                    _this.equals(_thisAsConnectable.getCPUTile());
            if (thisIsSource && (dejaVu.length > 0 || backRef != null)) {
                // source is doing propagation, but we are a source? Conflict,
                // return false.
                return 
                        mod().log.exit(false);
            }
            boolean noFailure = true;
            IBlockSource thisLoc =
                    new BlockSourceImpl(_this.getWorldObj(), _this.xCoord,
                            _this.yCoord, _this.zCoord);
            List<TileEntity> dejaList = Arrays.asList(dejaVu);
            IBlockSource[] surrounding = IBS.neighbors(thisLoc);
            for (IBlockSource around : surrounding) {
                TileEntity at = around.getBlockTileEntity();
                if (dejaList.contains(at)) {
                    // already checked
                    continue;
                }
                if (at instanceof CPUConnectable) {
                    CPUConnectable tileAsConnect = (CPUConnectable) at;
                    noFailure &=
                            tileAsConnect.updateCPUConnections(
                                    CPUConnectable_extendByUs(_this, dejaVu),
                                    (TBMCPUTile) (thisIsSource ? _this
                                            : backRef));
                }
            }
            if (noFailure && backRef != null) {
                // okay to set our tile, there is no conflict
                _thisAsConnectable.setCPUTile(backRef);
            } else if (noFailure) {
                // this is weird. There's no backRef, but we aren't a source.
                // Who
                // doesn't have the tile?
                return mod().log.exit(false);
            }
            return mod().log.exit(noFailure);
        }

        public static void CPUConnectable_updateEntity(TileEntity _this,
                CPUConnectable _thisAsConnectable) {
            if (_thisAsConnectable.hasCPUTile() && _this.getWorldObj() != null) {
                _this.updateContainingBlockInfo();
            }
        }

        public static void CPUConnectable_updateContainingBlockInfo(
                TileEntity _this, CPUConnectable _thisAsConnectable) {
            if (isClient(_this.getWorldObj())) {
                return;
            }
            if (_this.equals(_thisAsConnectable.getCPUTile())) {
                _thisAsConnectable
                        .updateCPUConnections(new TileEntity[0], null);
            } else {
                _thisAsConnectable.sendUpdateRequestToCPU(new TileEntity[0]);
            }
        }

        private static TileEntity[] CPUConnectable_extendByUs(TileEntity _this,
                TileEntity[] dejaVu) {
            TileEntity[] copy = Arrays.copyOf(dejaVu, dejaVu.length + 1);
            copy[dejaVu.length] = _this;
            return copy;
        }

        private SharedMethods() {
            throw new AssertionError("Nope.");
        }
    }

    public static final class SideConstants {
        public static final int TOP = 1, BOTTOM = 0, NORTH = 2, EAST = 5,
                SOUTH = 3, WEST = 4;

        private SideConstants() {
            throw new AssertionError("Nope.");
        }
    }

    public static final class Time {
        public static int minutesAsSeconds(int minutes) {
            return minutes * 60;
        }

        public static int minutesAsTicks(int minutes) {
            return secondsAsTicks(minutesAsSeconds(minutes));
        }

        public static int secondsAsTicks(int seconds) {
            return seconds * 20;
        }

        private Time() {
            throw new AssertionError("Nope.");
        }
    }

    public static MCAddress address(String id, String object) {
        return new MCAddress(id, object);
    }

    public static int clockwise(int side) {
        return nc[side];
    }

    public static int counterClockwise(int side) {
        return cc[side];
    }

    public static void drawBackground(GuiScreen gui, int xoff, int yoff, int u,
            int v, int w, int h) {
        gui.drawTexturedModalRect(xoff + u, yoff + v, u, v, w, h);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    public static int getMetadataForBlock(int x, int y, int z,
            EntityLivingBase entity) {
        if (MathHelper.abs((float) entity.posX - (float) x) < 2.0F
                && MathHelper.abs((float) entity.posZ - (float) z) < 2.0F) {
            double d0 = entity.posY + 1.82D - (double) entity.yOffset;

            if (d0 - (double) y > 2.0D) {
                return 1;
            }

            if ((double) y - d0 > 0.0D) {
                return 0;
            }
        }

        int l =
                MathHelper
                        .floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

    /**
     * Gets the side meta, but only for left/right/front/back, not up/down
     */
    public static int getMetadataForBlockOnlySided(EntityLivingBase base) {
        int l =
                MathHelper
                        .floor_double((double) (base.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (l == 0) {
            return 2;
        }

        if (l == 1) {
            return 5;
        }

        if (l == 2) {
            return 3;
        }

        if (l == 3) {
            return 4;
        }
        return 2;
    }

    @Deprecated
    public static Point[] pointsForItemsAroundArc(int objSide, int arcDegrees,
            int circRadius, int count) {
        double baseRad = Math.PI / (arcDegrees / 2);
        int halfOS = objSide / 2;
        double baseArcDegAdd = arcDegrees / count / 2;
        Point[] out = new Point[count];
        for (int i = 0; i < count; i++) {
            double angMult = baseArcDegAdd * i;
            int x = (int) (Math.sin(angMult * baseRad) * circRadius - halfOS);
            int y = (int) (Math.cos(angMult * baseRad) * circRadius - halfOS);
            out[i] = new Point(x, y);
        }
        return out;
    }

    public static void reverse(Object[] o) {
        Object[] copy = o.clone();
        for (int i = copy.length - 1; i >= 0; i--) {
            o[i] = copy[copy.length - i - 1];
        }
    }

    public static void throwing(Throwable t) {
        mod().log.throwing(t);
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new RuntimeException(t);
        }
    }

    public static boolean isClient(World w) {
        return w == null ? FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT
                : w.isRemote;
    }

    public static void placeTileEntityCopy(TileEntity original, Block block,
            World w, int x, int y, int z) {
        TileEntity copy = null;
        if (copy == null) {
            copy = block.createTileEntity(w, w.getBlockMetadata(x, y, z));
            if (original.getClass() != copy.getClass()) {
                throw new IllegalArgumentException(
                        "Transfers only valid between same class "
                                + String.format("(%s != %s)",
                                        original.getClass(), copy.getClass()));
            }
        }
        NBTTagCompound copyTag = new NBTTagCompound();
        original.writeToNBT(copyTag);
        copyTag.setInteger("x", x);
        copyTag.setInteger("y", y);
        copyTag.setInteger("z", z);
        copy.readFromNBT(copyTag);
        w.setBlock(x, y, z, block);
        w.setTileEntity(x, y, z, copy);
    }

    private static final int[] cc, nc;

    static {
        cc = new int[6];
        nc = new int[6];
        nc[TOP] = cc[TOP] = TOP;
        nc[BOTTOM] = cc[BOTTOM] = BOTTOM;
        nc[NORTH] = EAST;
        nc[EAST] = SOUTH;
        nc[SOUTH] = WEST;
        nc[WEST] = NORTH;

        cc[NORTH] = WEST;
        cc[WEST] = SOUTH;
        cc[SOUTH] = EAST;
        cc[EAST] = NORTH;
    }

    private Tutils() {
    }
}
