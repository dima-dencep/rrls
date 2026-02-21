/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import net.minecraft.client.gui.GuiGraphics;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.spongepowered.asm.util.asm.ASM;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GuiGraphicsGenerator extends ClassVisitor {
    protected final ClassNode output;
    protected final String ownerName;
    protected final List<String> accessWidenerEntries = new ArrayList<>();

    protected GuiGraphicsGenerator(ClassNode output, String ownerName) {
        super(ASM.API_VERSION);
        this.output = output;
        this.ownerName = ownerName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals("<init>") || name.equals("<clinit>")) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        if ((access & Opcodes.ACC_STATIC) != 0) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        if (name.contains("lambda$")) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

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

        boolean isPrivate = (access & Opcodes.ACC_PRIVATE) != 0;
        boolean isFinal = (access & Opcodes.ACC_FINAL) != 0;

        if (isPrivate) {
            this.accessWidenerEntries.add("accessible\tmethod\t" + this.ownerName + "\t" + name + "\t" + descriptor);
            this.accessWidenerEntries.add("extendable\tmethod\t" + this.ownerName + "\t" + name + "\t" + descriptor);
        } else if (isFinal) {
            this.accessWidenerEntries.add("extendable\tmethod\t" + this.ownerName + "\t" + name + "\t" + descriptor);
        }

        Rrls.LOGGER.info("Adding {}{}...", name, descriptor);
        this.output.methods.add(methodNode);

        return methodNode;
    }

    public static void main(String... args) throws IOException {
        ClassNode guiGraphicsNode = GuiGraphicsGenerator.readClassNode(GuiGraphics.class, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
        ClassNode dummyGuiGraphics = GuiGraphicsGenerator.readClassNode(DummyGuiGraphics.class, ClassReader.EXPAND_FRAMES);

        GuiGraphicsGenerator generator = new GuiGraphicsGenerator(dummyGuiGraphics, guiGraphicsNode.name);
        guiGraphicsNode.accept(generator);

        String source = generateJavaSource(dummyGuiGraphics);
        Files.writeString(Path.of("DummyGuiGraphics.java"), source);

        if (!generator.accessWidenerEntries.isEmpty()) {
            StringBuilder aw = new StringBuilder();
            aw.append("accessWidener\tv2\tnamed\n\n");
            for (String entry : generator.accessWidenerEntries) {
                aw.append(entry).append("\n");
            }
            Files.writeString(Path.of("rrls.accesswidener"), aw.toString());
        }
    }

    private static String generateJavaSource(ClassNode classNode) {
        Set<String> imports = new TreeSet<>();
        List<String> methodSources = new ArrayList<>();

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("<init>") || method.name.equals("<clinit>")) {
                continue;
            }

            if ((method.access & Opcodes.ACC_STATIC) != 0) {
                continue;
            }

            if ((method.access & (Opcodes.ACC_SYNTHETIC | Opcodes.ACC_BRIDGE)) != 0) {
                continue;
            }

            methodSources.add(generateMethod(method, imports));
        }

        imports.add("net.minecraft.client.Minecraft");
        imports.add("net.minecraft.client.gui.GuiGraphics");
        imports.add("net.minecraft.client.gui.render.state.GuiRenderState");

        imports.removeIf(imp -> imp.startsWith("java.lang.") && !imp.substring("java.lang.".length()).contains("."));

        StringBuilder sb = new StringBuilder();

        // License header
        sb.append("/*\n");
        sb.append(" * Copyright 2023 - 2026 dima_dencep.\n");
        sb.append(" *\n");
        sb.append(" * Licensed under the Open Software License, Version 3.0 (the \"License\");\n");
        sb.append(" * you may not use this file except in compliance with the License.\n");
        sb.append(" *\n");
        sb.append(" * You may obtain a copy of the License at\n");
        sb.append(" *     https://spdx.org/licenses/OSL-3.0.txt\n");
        sb.append(" */\n\n");

        // Package
        sb.append("package org.redlance.dima_dencep.mods.rrls.utils;\n\n");

        // Imports
        for (String imp : imports) {
            sb.append("import ").append(imp).append(";\n");
        }
        sb.append("\n");

        // Class declaration
        sb.append("@SuppressWarnings(\"all\")\n");
        sb.append("public class DummyGuiGraphics extends GuiGraphics {\n");
        sb.append("    public static final DummyGuiGraphics INSTANCE = new DummyGuiGraphics();\n\n");

        // Constructor
        sb.append("    private DummyGuiGraphics() {\n");
        sb.append("        super(Minecraft.getInstance(), (GuiRenderState) null, 0, 0);\n");
        sb.append("    }\n");

        // Methods
        for (String methodSource : methodSources) {
            sb.append("\n");
            sb.append(methodSource);
        }

        sb.append("}\n");

        return sb.toString();
    }

    private static String generateMethod(MethodNode method, Set<String> imports) {
        StringBuilder sb = new StringBuilder();

        boolean hasOverride = false;
        if (method.invisibleAnnotations != null) {
            for (AnnotationNode ann : method.invisibleAnnotations) {
                if (ann.desc.equals("Ljava/lang/Override;")) {
                    hasOverride = true;
                    break;
                }
            }
        }
        if (method.visibleAnnotations != null) {
            for (AnnotationNode ann : method.visibleAnnotations) {
                if (ann.desc.equals("Ljava/lang/Override;")) {
                    hasOverride = true;
                    break;
                }
            }
        }

        if (!hasOverride && (method.access & Opcodes.ACC_STATIC) == 0) {
            hasOverride = true;
        }

        if (hasOverride) {
            sb.append("    @Override\n");
        }

        sb.append("    ");
        int outputAccess = method.access;
        outputAccess &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL);
        outputAccess |= Opcodes.ACC_PUBLIC;
        sb.append(accessToString(outputAccess));

        String returnTypeStr;
        List<String> paramTypeStrs;

        if (method.signature != null) {
            MethodSignatureInfo sigInfo = parseMethodSignature(method.signature, imports);
            returnTypeStr = sigInfo.returnType;
            paramTypeStrs = sigInfo.paramTypes;
        } else {
            Type returnType = Type.getReturnType(method.desc);
            Type[] argTypes = Type.getArgumentTypes(method.desc);

            returnTypeStr = typeToJava(returnType, imports);
            paramTypeStrs = new ArrayList<>();
            for (Type t : argTypes) {
                paramTypeStrs.add(typeToJava(t, imports));
            }
        }

        sb.append(returnTypeStr).append(" ").append(method.name).append("(");

        List<String> paramNames = getParameterNames(method, paramTypeStrs.size());

        for (int i = 0; i < paramTypeStrs.size(); i++) {
            if (i > 0) sb.append(", ");

            String paramAnnotation = getParameterAnnotation(method, i, imports);
            if (paramAnnotation != null) {
                sb.append(paramAnnotation).append(" ");
            }

            sb.append(paramTypeStrs.get(i)).append(" ").append(paramNames.get(i));
        }

        sb.append(") {\n");

        Type returnType = Type.getReturnType(method.desc);
        String defaultReturn = defaultReturn(returnType);
        if (!defaultReturn.isEmpty()) {
            sb.append("        ").append(defaultReturn).append("\n");
        }

        sb.append("    }\n");

        return sb.toString();
    }

    private static String accessToString(int access) {
        StringBuilder sb = new StringBuilder();
        if ((access & Opcodes.ACC_PUBLIC) != 0) sb.append("public ");
        else if ((access & Opcodes.ACC_PROTECTED) != 0) sb.append("protected ");
        else if ((access & Opcodes.ACC_PRIVATE) != 0) sb.append("private ");

        if ((access & Opcodes.ACC_STATIC) != 0) sb.append("static ");
        if ((access & Opcodes.ACC_FINAL) != 0) sb.append("final ");
        if ((access & Opcodes.ACC_ABSTRACT) != 0) sb.append("abstract ");

        return sb.toString();
    }

    private static String defaultReturn(Type returnType) {
        return switch (returnType.getSort()) {
            case Type.VOID -> "";
            case Type.BOOLEAN -> "return false;";
            case Type.CHAR, Type.BYTE, Type.INT, Type.SHORT -> "return 0;";
            case Type.FLOAT -> "return 0.0f;";
            case Type.LONG -> "return 0L;";
            case Type.DOUBLE -> "return 0.0;";
            default -> "return null;";
        };
    }

    private static List<String> getParameterNames(MethodNode method, int paramCount) {
        List<String> names = new ArrayList<>();

        if (method.parameters != null && method.parameters.size() == paramCount) {
            for (ParameterNode p : method.parameters) {
                names.add(p.name);
            }
            return names;
        }

        if (method.localVariables != null && !method.localVariables.isEmpty()) {
            boolean isStatic = (method.access & Opcodes.ACC_STATIC) != 0;
            int startIdx = isStatic ? 0 : 1;

            var sortedLocals = method.localVariables.stream()
                    .sorted(Comparator.comparingInt(a -> a.index))
                    .toList();

            int added = 0;
            for (var local : sortedLocals) {
                if (local.index < startIdx) continue;
                if (added >= paramCount) break;

                names.add(local.name);
                added++;

                Type localType = Type.getType(local.desc);
                if (localType.getSort() == Type.LONG || localType.getSort() == Type.DOUBLE) {
                    startIdx++;
                }
            }

            if (names.size() == paramCount) {
                return names;
            }
            names.clear();
        }

        for (int i = 0; i < paramCount; i++) {
            names.add("p" + i);
        }

        return names;
    }

    private static String getParameterAnnotation(MethodNode method, int paramIndex, Set<String> imports) {
        List<AnnotationNode> annotations = null;

        if (method.invisibleParameterAnnotations != null && paramIndex < method.invisibleParameterAnnotations.length) {
            annotations = method.invisibleParameterAnnotations[paramIndex];
        }

        if (annotations == null && method.visibleParameterAnnotations != null && paramIndex < method.visibleParameterAnnotations.length) {
            annotations = method.visibleParameterAnnotations[paramIndex];
        }

        if (annotations != null) {
            for (AnnotationNode ann : annotations) {
                if (ann.desc.contains("Nullable")) {
                    String className = Type.getType(ann.desc).getClassName();
                    imports.add(className);
                    String simpleName = className.substring(className.lastIndexOf('.') + 1);
                    return "@" + simpleName;
                }
            }
        }

        return null;
    }

    private static String typeToJava(Type type, Set<String> imports) {
        return switch (type.getSort()) {
            case Type.VOID -> "void";
            case Type.BOOLEAN -> "boolean";
            case Type.CHAR -> "char";
            case Type.BYTE -> "byte";
            case Type.SHORT -> "short";
            case Type.INT -> "int";
            case Type.FLOAT -> "float";
            case Type.LONG -> "long";
            case Type.DOUBLE -> "double";
            case Type.ARRAY -> typeToJava(type.getElementType(), imports) + "[]".repeat(type.getDimensions());
            case Type.OBJECT -> {
                String className = type.getClassName();
                yield resolveClassName(className, imports);
            }
            default -> type.getClassName();
        };
    }

    private static String resolveClassName(String className, Set<String> imports) {
        if (!className.startsWith("java.lang.") || className.substring("java.lang.".length()).contains(".")) {
            // Inner classes: GuiGraphics$HoveredTextEffects
            if (className.contains("$")) {
                String outerClass = className.substring(0, className.indexOf('$'));
                imports.add(outerClass);
                String outerSimple = outerClass.substring(outerClass.lastIndexOf('.') + 1);
                String innerPart = className.substring(className.indexOf('$') + 1).replace('$', '.');
                return outerSimple + "." + innerPart;
            }
            imports.add(className);
        }
        return className.substring(className.lastIndexOf('.') + 1);
    }

    private record MethodSignatureInfo(String returnType, List<String> paramTypes) {}

    private static MethodSignatureInfo parseMethodSignature(String signature, Set<String> imports) {
        int[] pos = {0};

        if (signature.charAt(0) == '<') {
            int depth = 0;
            do {
                char c = signature.charAt(pos[0]);
                if (c == '<') depth++;
                if (c == '>') depth--;
                pos[0]++;
            } while (depth > 0);
        }

        if (signature.charAt(pos[0]) != '(') {
            throw new IllegalStateException("Expected '(' at position " + pos[0] + " in: " + signature);
        }
        pos[0]++; // skip (

        List<String> paramTypes = new ArrayList<>();
        while (signature.charAt(pos[0]) != ')') {
            paramTypes.add(parseTypeSignature(signature, pos, imports));
        }
        pos[0]++; // skip )

        String returnType = parseTypeSignature(signature, pos, imports);
        return new MethodSignatureInfo(returnType, paramTypes);
    }

    private static String parseTypeSignature(String sig, int[] pos, Set<String> imports) {
        char c = sig.charAt(pos[0]);
        pos[0]++;

        switch (c) {
            case 'B': return "byte";
            case 'C': return "char";
            case 'D': return "double";
            case 'F': return "float";
            case 'I': return "int";
            case 'J': return "long";
            case 'S': return "short";
            case 'Z': return "boolean";
            case 'V': return "void";
            case '[': return parseTypeSignature(sig, pos, imports) + "[]";
            case 'T': {
                // Type variable: T<Name>;
                int end = sig.indexOf(';', pos[0]);
                String name = sig.substring(pos[0], end);
                pos[0] = end + 1;
                return name;
            }
            case 'L': {
                // Class type: L<class>(.<inner>)*(<typeArgs>)?;
                StringBuilder result = new StringBuilder();
                int start = pos[0];

                while (sig.charAt(pos[0]) != ';' && sig.charAt(pos[0]) != '<') {
                    pos[0]++;
                }

                String rawName = sig.substring(start, pos[0]).replace('/', '.');
                result.append(resolveClassName(rawName, imports));

                // Type arguments
                if (sig.charAt(pos[0]) == '<') {
                    pos[0]++; // skip <
                    result.append('<');
                    boolean first = true;
                    while (sig.charAt(pos[0]) != '>') {
                        if (!first) result.append(", ");
                        first = false;
                        result.append(parseTypeArgument(sig, pos, imports));
                    }
                    pos[0]++; // skip >
                    result.append('>');
                }

                while (pos[0] < sig.length() && sig.charAt(pos[0]) == '.') {
                    pos[0]++; // skip .
                    result.append('.');
                    int innerStart = pos[0];
                    while (sig.charAt(pos[0]) != ';' && sig.charAt(pos[0]) != '<' && sig.charAt(pos[0]) != '.') {
                        pos[0]++;
                    }
                    result.append(sig, innerStart, pos[0]);

                    if (sig.charAt(pos[0]) == '<') {
                        pos[0]++;
                        result.append('<');
                        boolean first = true;
                        while (sig.charAt(pos[0]) != '>') {
                            if (!first) result.append(", ");
                            first = false;
                            result.append(parseTypeArgument(sig, pos, imports));
                        }
                        pos[0]++;
                        result.append('>');
                    }
                }

                pos[0]++; // skip ;
                return result.toString();
            }
            default:
                throw new IllegalStateException("Unexpected type char '" + c + "' at position " + (pos[0] - 1) + " in: " + sig);
        }
    }

    private static String parseTypeArgument(String sig, int[] pos, Set<String> imports) {
        char c = sig.charAt(pos[0]);
        if (c == '*') {
            pos[0]++;
            return "?";
        } else if (c == '+') {
            pos[0]++;
            return "? extends " + parseTypeSignature(sig, pos, imports);
        } else if (c == '-') {
            pos[0]++;
            return "? super " + parseTypeSignature(sig, pos, imports);
        } else {
            return parseTypeSignature(sig, pos, imports);
        }
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
