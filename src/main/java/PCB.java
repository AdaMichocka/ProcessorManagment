
public class PCB {

    public enum processState {
        WAITING, RUNNING, TERMINATED;
    }

    public static processState getValue(String x) {
        if ("w".equals(x)) {
            return processState.WAITING;
        } else if ("r".equals(x)) {
            return processState.RUNNING;
        } else if ("t".equals(x)) {
            return processState.TERMINATED;
        } else throw new IllegalArgumentException();
    }
}

