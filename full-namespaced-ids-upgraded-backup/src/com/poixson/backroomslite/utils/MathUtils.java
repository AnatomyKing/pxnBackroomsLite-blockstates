package com.poixson.backroomslite.utils;

import java.awt.*;

public final class MathUtils
{
    private MathUtils() {
    }
    
    public static double RotateX(final double x, final double y, final double angle) {
        final double ang = 3.141592653589793 * angle;
        return Math.sin(ang) * y - Math.cos(ang) * x;
    }
    
    public static double RotateY(final double x, final double y, final double angle) {
        final double ang = 3.141592653589793 * angle;
        return Math.sin(ang) * x + Math.cos(ang) * y;
    }
    
    public static float RotateX(final float x, final float y, final float angle) {
        final float ang = (float)(3.141592653589793 * angle);
        return (float)(Math.sin(ang) * y - Math.cos(ang) * x);
    }
    
    public static float RotateY(final float x, final float y, final float angle) {
        final float ang = (float)(3.141592653589793 * angle);
        return (float)(Math.sin(ang) * x + Math.cos(ang) * y);
    }
    
    public static int Remap(final int lowA, final int highA, final int lowB, final int highB, final int value) {
        final double lA = lowA;
        final double hA = highA;
        final double lB = lowB;
        final double hB = highB;
        double result = (hB - lB) / (hA - lA);
        result *= value - lA;
        result += lB;
        return (int)result;
    }
    
    public static int Remap(final int low, final int high, final double percent) {
        final double result = (high - low) * percent;
        return (int)result + low;
    }
    
    public static Color Remap(final Color colorA, final Color colorB, final double percent) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* colorA */
        //     5: invokevirtual   java/awt/Color.getRed:()I
        //     8: aload_1         /* colorB */
        //     9: invokevirtual   java/awt/Color.getRed:()I
        //    12: dload_2         /* percent */
        //    13: invokestatic    com/poixson/utils/MathUtils.Remap:(IID)I
        //    16: iconst_0       
        //    17: sipush          255
        //    20: invokestatic    invokestatic   !!! ERROR
        //    23: aload_0         /* colorA */
        //    24: invokevirtual   java/awt/Color.getGreen:()I
        //    27: aload_1         /* colorB */
        //    28: invokevirtual   java/awt/Color.getGreen:()I
        //    31: dload_2         /* percent */
        //    32: invokestatic    com/poixson/utils/MathUtils.Remap:(IID)I
        //    35: iconst_0       
        //    36: sipush          255
        //    39: invokestatic    invokestatic   !!! ERROR
        //    42: aload_0         /* colorA */
        //    43: invokevirtual   java/awt/Color.getBlue:()I
        //    46: aload_1         /* colorB */
        //    47: invokevirtual   java/awt/Color.getBlue:()I
        //    50: dload_2         /* percent */
        //    51: invokestatic    com/poixson/utils/MathUtils.Remap:(IID)I
        //    54: iconst_0       
        //    55: sipush          255
        //    58: invokestatic    invokestatic   !!! ERROR
        //    61: invokespecial   java/awt/Color.<init>:(III)V
        //    64: areturn        
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Invalid BootstrapMethods attribute entry: 2 additional arguments required for method java/lang/invoke/StringConcatFactory.makeConcatWithConstants, but only 1 specified.
        //     at com.strobel.assembler.ir.Error.invalidBootstrapMethodEntry(Error.java:244)
        //     at com.strobel.assembler.ir.MetadataReader.readAttributeCore(MetadataReader.java:280)
        //     at com.strobel.assembler.metadata.ClassFileReader.readAttributeCore(ClassFileReader.java:261)
        //     at com.strobel.assembler.ir.MetadataReader.inflateAttributes(MetadataReader.java:439)
        //     at com.strobel.assembler.metadata.ClassFileReader.visitAttributes(ClassFileReader.java:1134)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:439)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Color Remap8BitColor(final int value) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: bipush          7
        //     3: iand           
        //     4: istore_1        /* r */
        //     5: iload_0         /* value */
        //     6: bipush          56
        //     8: iand           
        //     9: iconst_3       
        //    10: ishr           
        //    11: istore_2        /* g */
        //    12: iload_0         /* value */
        //    13: sipush          192
        //    16: iand           
        //    17: bipush          6
        //    19: ishr           
        //    20: istore_3        /* b */
        //    21: new             Ljava/awt/Color;
        //    24: dup            
        //    25: iconst_0       
        //    26: bipush          7
        //    28: iconst_0       
        //    29: sipush          255
        //    32: iload_1         /* r */
        //    33: invokestatic    com/poixson/utils/MathUtils.Remap:(IIIII)I
        //    36: iconst_0       
        //    37: sipush          255
        //    40: invokestatic    invokestatic   !!! ERROR
        //    43: iconst_0       
        //    44: bipush          7
        //    46: iconst_0       
        //    47: sipush          255
        //    50: iload_2         /* g */
        //    51: invokestatic    com/poixson/utils/MathUtils.Remap:(IIIII)I
        //    54: iconst_0       
        //    55: sipush          255
        //    58: invokestatic    invokestatic   !!! ERROR
        //    61: iconst_0       
        //    62: iconst_3       
        //    63: iconst_0       
        //    64: sipush          255
        //    67: iload_3         /* b */
        //    68: invokestatic    com/poixson/utils/MathUtils.Remap:(IIIII)I
        //    71: iconst_0       
        //    72: sipush          255
        //    75: invokestatic    invokestatic   !!! ERROR
        //    78: invokespecial   java/awt/Color.<init>:(III)V
        //    81: areturn        
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Invalid BootstrapMethods attribute entry: 2 additional arguments required for method java/lang/invoke/StringConcatFactory.makeConcatWithConstants, but only 1 specified.
        //     at com.strobel.assembler.ir.Error.invalidBootstrapMethodEntry(Error.java:244)
        //     at com.strobel.assembler.ir.MetadataReader.readAttributeCore(MetadataReader.java:280)
        //     at com.strobel.assembler.metadata.ClassFileReader.readAttributeCore(ClassFileReader.java:261)
        //     at com.strobel.assembler.ir.MetadataReader.inflateAttributes(MetadataReader.java:439)
        //     at com.strobel.assembler.metadata.ClassFileReader.visitAttributes(ClassFileReader.java:1134)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:439)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static double CubicInterpolate(final double p, final double a, final double b, final double c, final double d) {
        final double e = d - c - a + b;
        final double f = a - b - e;
        final double g = c - a;
        return e * p * p * p + f * p * p + g * p + b;
    }

}
