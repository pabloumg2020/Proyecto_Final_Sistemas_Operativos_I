public class ControlRam {
    public int MemoryGlobal;
    public int MemoryFree;
    public int MemoryUsed;
    public int BankMemory;

    public ControlRam(int MemoryGlobal, int MemoryFree, int MemoryUsed, int BankMemory) {
        this.MemoryGlobal = MemoryGlobal;
        this.MemoryFree = MemoryFree;
        this.MemoryUsed = MemoryUsed;
        this.BankMemory = BankMemory;
    }

    public void setMemoryGlobal(int MemoryGlobal) {
        this.MemoryGlobal = MemoryGlobal;
    }

    public void setMemoryFree(int MemoryFree) {
        this.MemoryFree = MemoryFree;
    }

    public void setMemoryUsed(int MemoryUsed) {
        this.MemoryUsed = MemoryUsed;
    }

    public int getMemoryGlobal() {
        return MemoryGlobal;
    }

    public int getMemoryFree() {
        return MemoryFree;
    }

    public int getMemoryUsed() {
        return MemoryUsed;
    }
}
