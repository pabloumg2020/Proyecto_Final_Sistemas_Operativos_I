public class PagHistorial {
    int pagina;
    int marco;

    public PagHistorial(int _pagina, int _marco) {
        this.pagina = _pagina;
        this.marco = _marco;
    }

    public int getPagina() {
        return pagina;
    }

    public int getMarco() {
        return marco;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public void setMarco(int marco) {
        this.marco = marco;
    }  
}
