package or.id.greenlabs.instrument.agent;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author krissadewo
 * @date 1/15/21 3:29 PM
 */
public class AgentLoader {

    private static final Logger LOGGER = Logger.getLogger(AgentLoader.class.getName());

    public static void run(String[] args) {
        String agentFilePath = "/home/ksadewo/Workspace/research/java-agent/target/java-agent-1.0-SNAPSHOT.jar";
        String applicationName = "Application";

        //iterate all jvms and get the first one that matches our application name
        Optional<String> jvmProcessOpt = Optional.ofNullable(VirtualMachine.list()
                .stream()
                .filter(jvm -> {
                    LOGGER.info("jvm:{}" + jvm.displayName());
                    return jvm.displayName().contains(applicationName);
                })
                .findFirst().get().id());

        if (jvmProcessOpt.isEmpty()) {
            LOGGER.fine("Target Application not found");
            return;
        }

        File agentFile = new File(agentFilePath);

        try {
            String jvmPid = jvmProcessOpt.get();

            LOGGER.info("Attaching to target JVM with PID: " + jvmPid);

            VirtualMachine jvm = VirtualMachine.attach(jvmPid);
            jvm.loadAgent(agentFile.getAbsolutePath());
            jvm.detach();

            LOGGER.info("Attached to target JVM and loaded Java agent successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
