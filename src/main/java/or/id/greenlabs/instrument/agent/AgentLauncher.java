package or.id.greenlabs.instrument.agent;

/**
 * @author krissadewo
 * @date 1/15/21 3:30 PM
 */
public class AgentLauncher {

    public static void main(String[] args) throws Exception {
        AgentLoader.run(args);

        Thread.sleep(15000);
    }
}
