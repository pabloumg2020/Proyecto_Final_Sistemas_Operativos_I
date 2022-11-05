public class RamAsign {
    int BankSize;
    int BankFree;
    int BankUsed;
    int KeyProcess;
    String TitleProcess;

    public RamAsign() {
        this.BankSize = 200000;
        this.BankFree = 200000;
        this.BankUsed = 0;
        this.KeyProcess = 0;
        this.TitleProcess = "-----";
    }

    public int getKeyProcess() {
        return KeyProcess;
    }

    public void setKeyProcess(int KeyProcess) {
        this.KeyProcess = KeyProcess;
    }

    public void setBankSize(int BankSize) {
        this.BankSize = BankSize;
    }

    public void setBankFree(int BankFree) {
        this.BankFree = BankFree;
    }

    public void setBankUsed(int BankUsed) {
        this.BankUsed = BankUsed;
    }

    public void setTitleProcess(String TitleProcess) {
        this.TitleProcess = TitleProcess;
    }

    public int getBankSize() {
        return BankSize;
    }

    public int getBankFree() {
        return BankFree;
    }

    public int getBankUsed() {
        return BankUsed;
    }

    public String getTitleProcess() {
        return TitleProcess;
    }   
}
