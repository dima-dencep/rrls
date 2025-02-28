package org.redlance.dima_dencep.mods.rrls.utils;

import net.minecraft.client.gui.GuiGraphics;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.spongepowered.asm.util.asm.ASM;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class GuiGraphicsGenerator extends ClassVisitor {
    protected final ClassNode output;

    protected GuiGraphicsGenerator(ClassNode output) {
        super(ASM.API_VERSION);
        this.output = output;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        for (MethodNode node : this.output.methods) {
            if (node.name.equals(name) && node.desc.equals(descriptor)) {
                Rrls.LOGGER.info("Skipping {}{}...", name, descriptor);

                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }

        MethodNode methodNode = new MethodNode(access, name, descriptor, signature, exceptions);
        if (methodNode.invisibleAnnotations == null) {
            methodNode.invisibleAnnotations = new ArrayList<>();
        }
        methodNode.invisibleAnnotations.add(new AnnotationNode("Ljava/lang/Override;"));

        Rrls.LOGGER.info("Adding {}{}...", name, descriptor);
        this.output.methods.add(methodNode);

        return methodNode;
    }

    public static void main(String... args) throws IOException {
        ClassNode guiGraphicsNode = GuiGraphicsGenerator.readClassNode(GuiGraphics.class, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
        ClassNode dummyGuiGraphics = GuiGraphicsGenerator.readClassNode(DummyGuiGraphics.class, ClassReader.EXPAND_FRAMES);

        guiGraphicsNode.accept(new GuiGraphicsGenerator(dummyGuiGraphics));

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        dummyGuiGraphics.accept(writer);

        Files.write(Path.of("DummyGuiGraphics.class"), writer.toByteArray());
    }

    private static ClassNode readClassNode(Class<?> clazz, int parsingOptions) {
        ClassNode classNode = new ClassNode();

        try (InputStream is = clazz.getClassLoader().getResourceAsStream(
                clazz.getName().replace(".", "/") + ".class")
        ) {
            ClassReader cr = new ClassReader(Objects.requireNonNull(is));
            cr.accept(classNode, parsingOptions);
        } catch (IOException e) {
            Rrls.LOGGER.warn("Failed to read '{}'!", clazz.getName());
        }

        return classNode;
    }
}
