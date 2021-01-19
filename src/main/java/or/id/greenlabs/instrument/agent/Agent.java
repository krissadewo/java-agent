package or.id.greenlabs.instrument.agent;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

/**
 * @author krissadewo
 * @date 1/15/21 2:17 PM
 */
public class Agent {

    static Logger logger = Logger.getLogger(Agent.class.getName());

    public static void premain(String agentArgs, Instrumentation inst) {
        logger.info("[Agent] In premain method");

        String className = "com.etle.instrument.app.TaxService";

        transformClass(className, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        logger.info("[Agent] In agentmain method");

        String className = "com.etle.instrument.app.TaxService";

        transformClass(className, inst);
    }

    private static void transformClass(String className, Instrumentation instrumentation) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();

            transform(targetCls, targetClassLoader, instrumentation);

            return;
        } catch (Exception ex) {
            logger.fine("Class [{}] not found with Class.forName");
        }

        // otherwise iterate all loaded classes and find what we want
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            if (clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();

                transform(targetCls, targetClassLoader, instrumentation);

                return;
            }
        }

        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
        TaxTransformer dt = new TaxTransformer(clazz.getName(), classLoader);

        instrumentation.addTransformer(dt, true);

        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Transform failed for: [" + clazz.getName() + "]", ex);
        }
    }
}
