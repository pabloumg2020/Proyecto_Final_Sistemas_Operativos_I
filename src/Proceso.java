
public class Proceso {
    
    int Nproceso=0;
    int Ejecucion=0;
    int Memoria=0;
    String Estado="null";
    String Nombre="null";
   
    void Proceso(){
        this.Nproceso=0;
        this.Ejecucion=0;
        this.Memoria=0;
        this.Estado="null";
        this.Nombre="null";
    }
    
    public void SetValues(int _np, int _mem, int _eje, String _est, String _nombre){
        this.Nproceso=_np;
        this.Ejecucion=_eje;
        this.Memoria=_mem;
        this.Estado=_est;
        this.Nombre=_nombre;
    }
    
    public void SetEjecucion(){
        this.Ejecucion--;
    }   
}
