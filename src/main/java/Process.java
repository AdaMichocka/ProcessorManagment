public class Process {

    private int processId;
    private int priority;
    private PCB.processState state;
    private int startBurstTime = 2;
    private int startCounter;

    public Process(int processId, int priority, PCB.processState state) {
        this.processId = processId;
        this.priority = priority;
        this.state = state;
    }


    public Process() {
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public PCB.processState setState(PCB.processState state) {
        this.state = state;
        return state;
    }

    public void setStartBurstTime(int startBurstTime) {
        this.startBurstTime = startBurstTime;
    }

    public void setStartCounter(int startCounter) {
        this.startCounter = startCounter;
    }

    public int getProcessId() {
        return processId;
    }

    public int getThisProcessId(int i) {
        i = this.getProcessId();
        return i;
    }

    public int getPriority() {
        return priority;
    }

    public int getThisPriority(int i) {
        i = this.getPriority();
        return i;
    }

    public PCB.processState getState() {
        return state;
    }

    public static PCB.processState getEnumSTate(PCB.processState p) {
        return p;
    }

    public PCB.processState getThisState(String i) {
        PCB.processState stateEnum = PCB.getValue(i);
        return stateEnum;
    }

    public int getStartBurstTime() {
        return startBurstTime;
    }

    public int getStartCounter() {
        return startCounter;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
