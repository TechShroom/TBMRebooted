package com.techshroom.mods.tbm.autogen;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.techshroom.mods.tbm.autogen.data.__datalock__;
import com.techshroom.mods.tbm.autogen.interfaces.AutoGenElement;
import com.techshroom.mods.tbm.autogen.interfaces.Face;
import com.techshroom.mods.tbm.autogen.interfaces.Orientation;

public class JSONWriterBase {

    private static final String PACKAGE;

    static {
        Package p = __datalock__.class.getPackage();
        PACKAGE = p.getName();
    }

    private static final String readString(String file) throws IOException {
        byte[] bytes = Files.readAllBytes(
                Paths.get("src/main/java", PACKAGE.replace('.', '/'), file));
        return new String(bytes, StandardCharsets.UTF_8).intern();
    }

    private static final Map<Orientation, String> BLOCK_STATE_FMTS =
            new EnumMap<>(Orientation.class);
    private static final Map<Orientation, String> BLOCK_MODEL_FMTS =
            new EnumMap<>(Orientation.class);
    private static final String ITEM_MODEL_FMT;

    static {
        try {
            // States
            BLOCK_STATE_FMTS.put(Orientation.ALL,
                    readString("blockstate_orient_all.json"));
            BLOCK_STATE_FMTS.put(Orientation.HORIZONTAL,
                    readString("blockstate_orient_horiz.json"));
            BLOCK_STATE_FMTS.put(Orientation.NONE,
                    readString("blockstate_cube.json"));
            // Models
            BLOCK_MODEL_FMTS.put(Orientation.ALL,
                    readString("blockmodel_orient_vertical.json"));
            BLOCK_MODEL_FMTS.put(Orientation.HORIZONTAL,
                    readString("blockmodel_orient.json"));
            BLOCK_MODEL_FMTS.put(Orientation.NONE,
                    readString("blockmodel_cube.json"));
            ITEM_MODEL_FMT = readString("itemmodel.json");
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // Load our package
        ClassPath path = ClassPath.from(__datalock__.class.getClassLoader());
        path.getTopLevelClasses(PACKAGE).stream().map(ClassInfo::load)
                .filter(cls -> cls.getAnnotation(AutoGenElement.class) != null)
                .forEach(JSONWriterBase::generate);
    }

    private static void generate(Class<?> base) {
        AutoGenElement data = base.getAnnotation(AutoGenElement.class);
        checkState(data != null, "data must be present");
        String name = data.name();
        String textureName = Optional
                .ofNullable(Strings.emptyToNull(data.texture())).orElse(name);
        Set<Face> faces = ImmutableSet.copyOf(data.faces());
        // right now, only support a single Face.FRONT or none
        checkState(
                faces.size() == 0 || (faces.size() == 1
                        && faces.iterator().next() == Face.FRONT),
                "no support for %s", faces);
        Orientation orient = data.orientation();
        System.err.println("Generating " + name + " w/ faces " + faces);
        generateBlockStateJson(name, orient);
        generateBlockModelJson(name, textureName, faces, orient);
        generateItemModelJson(name);
    }

    private static void generateBlockStateJson(String name,
            Orientation orient) {
        String formatted = String.format(BLOCK_STATE_FMTS.get(orient), name);
        Path target = Paths.get("src/main/resources/assets/tbmmod/blockstates",
                name + ".json");
        doWrite(formatted, target);
    }

    private static void generateBlockModelJson(String name, String textureName,
            Set<Face> faces, Orientation orient) {
        String formatted = "";
        Path target = Paths.get("src/main/resources/assets/tbmmod/models/block",
                name + ".json");
        if (faces.size() == 0) {
            formatted = String.format(BLOCK_MODEL_FMTS.get(orient), name);
        } else if (faces.size() == 1) {
            formatted = String
                    .format(BLOCK_MODEL_FMTS.get(Orientation.HORIZONTAL), name);
            if (orient == Orientation.ALL) {
                String formattedVert =
                        String.format(BLOCK_MODEL_FMTS.get(orient), name);
                Path targetVert = Paths.get(
                        "src/main/resources/assets/tbmmod/models/block",
                        name + "_vertical.json");
                doWrite(formattedVert, targetVert);
            }
        }
        if (formatted.isEmpty()) {
            return;
        }
        doWrite(formatted, target);
    }

    private static void generateItemModelJson(String name) {
        String formatted = String.format(ITEM_MODEL_FMT, name);
        Path target = Paths.get("src/main/resources/assets/tbmmod/models/item",
                "tile." + name + ".json");
        doWrite(formatted, target);
    }

    private static void doWrite(String stuff, Path to) {
        if (Files.exists(to)) {
            System.err.println("Not writing " + to.toAbsolutePath());
            System.err.println("Would have written:");
            System.err.println("===================");
            System.err.println(stuff);
            System.err.println("===================");
            return;
        }
        try {
            Files.write(to, stuff.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
