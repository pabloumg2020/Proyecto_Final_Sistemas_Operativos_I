public class RamVirtual {
    int BankSize;
    int KeyProcess;
    String TitleProcess;

    public RamVirtual(int _bankSize, int _keyProcess, String _title) {
        this.BankSize = _bankSize;
        this.KeyProcess = _keyProcess;
        this.TitleProcess = _title;
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

    public void setTitleProcess(String TitleProcess) {
        this.TitleProcess = TitleProcess;
    }

    public int getBankSize() {
        return BankSize;
    }

    public String getTitleProcess() {
        return TitleProcess;
    }   
}
