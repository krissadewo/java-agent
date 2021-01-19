package com.etle.instrument.agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

/**
 * @author krissadewo
 * @date 1/15/21 3:19 PM
 */
public class TaxTransformer implements ClassFileTransformer {

    private static Logger LOGGER = Logger.getLogger(TaxTransformer.class.getName());

    private static final String CALCULATE_TAX_METHOD = "countTax";

    /**
     * The internal form class name of the class to transform
     */
    private String targetClassName;
    /**
     * The class loader of the class we want to transform
     */
    private ClassLoader targetClassLoader;

    public TaxTransformer(String targetClassName, ClassLoader targetClassLoader) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/"); //replace . with /

        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (loader.equals(targetClassLoader)) {
            LOGGER.info("[Agent] Transforming class TaxService");

            try {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));

                CtClass cc = cp.get(targetClassName);
                CtMethod m = cc.getDeclaredMethod(CALCULATE_TAX_METHOD);

                m.addLocalVariable("startTime", CtClass.longType);
                m.insertBefore("startTime = System.currentTimeMillis();");

                StringBuilder endBlock = new StringBuilder();

                m.addLocalVariable("endTime", CtClass.longType);
                m.addLocalVariable("opTime", CtClass.longType);

                endBlock.append("endTime = System.currentTimeMillis();");
                endBlock.append("opTime = (endTime-startTime)/1000;");
                endBlock.append("LOGGER.info(\"[Application] Calculate tax operation completed in:\" + opTime + \" seconds!\");");

                m.insertAfter(endBlock.toString());

                byteCode = cc.toBytecode();

                cc.detach();
            } catch (CannotCompileException | IOException | NotFoundException e) {
                e.printStackTrace();

                LOGGER.info("Exception");
            }
        }

        return byteCode;
    }
}
