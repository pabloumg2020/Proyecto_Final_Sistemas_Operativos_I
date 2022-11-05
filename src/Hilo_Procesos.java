public class Hilo_Procesos extends Thread{    
    
    public Hilo_Procesos (){
        
    }
    
    private void Dormir(){
        try{
            Thread.sleep(2); 
        }catch(InterruptedException ex){
            
        }
    }
    
    public boolean VerifyP(Proceso _ver){
        boolean res=true;
        /** verificar proceso no este terminado */
        if(_ver.Estado.contentEquals("Terminado") || _ver.Estado.contentEquals("Ejecutando")){
            res=false;
        }
        /** verificar la disponibilidad de la memoria */
        if(_ver.Memoria>GestorMemoria.RAM.MemoryFree || GestorMemoria.RAM.BankMemory<=0){
            res=false;
        }
        return res;
    }
    
    
    public void Borrar(int b){ //Elimina los datos de la tabla de procesos
        GestorMemoria.jTable1.setValueAt("0",b,2);
        //GestorProcesos.jTable1.setValueAt("0",b,3);
    }
    
    @Override
    public void run(){
        int process = 1; 
        int i = 0; 
        while(process!=0){
            // mientras que el valor del proceso sea diferente de 0
            while(i<GestorMemoria.procesos.length){ //recorrer las filas mientras que el contador < cantidad de procesos
                if(GestorMemoria.procesos[i].Memoria>0){
                    if(VerifyP(GestorMemoria.procesos[i])){
                        
                        GestorMemoria.procesos[i].Estado="Ejecutando";
                        int cs=(int)Math.ceil((double)GestorMemoria.procesos[i].Memoria/200000);
                        int bmc=0;
                        for(int inc=0; inc<GestorMemoria.ramBank.length; inc++){
                            if(GestorMemoria.ramBank[inc].BankFree==200000 && cs>0){
                                if((GestorMemoria.procesos[i].Memoria-((bmc)*200000))>=200000){
                                    GestorMemoria.ramBank[inc].BankFree=0;
                                    GestorMemoria.ramBank[inc].BankUsed=200000;
                                    GestorMemoria.RAM.MemoryUsed+=200000;
                                    GestorMemoria.RAM.MemoryFree-=200000;
                                } else {
                                    GestorMemoria.ramBank[inc].BankFree=200000-(GestorMemoria.procesos[i].Memoria-(bmc*200000));
                                    GestorMemoria.ramBank[inc].BankUsed=GestorMemoria.procesos[i].Memoria-(bmc*200000); 
                                    GestorMemoria.RAM.MemoryUsed+=GestorMemoria.procesos[i].Memoria-(bmc*200000);
                                    GestorMemoria.RAM.MemoryFree-=GestorMemoria.procesos[i].Memoria-(bmc*200000);
                                }
                                GestorMemoria.ramBank[inc].TitleProcess=GestorMemoria.procesos[i].Nombre;
                                GestorMemoria.ramBank[inc].KeyProcess=i;
                                GestorMemoria.RAM.BankMemory-=1;
                                cs--;
                                bmc++;
                            }

                            GestorMemoria.setBlockMemory(inc);
                        }
                    } else {
                        /* memoria virtual */
                        if(GestorMemoria.procesos[i].Estado.contentEquals("Espera")){
                            GestorMemoria.procesos[i].Estado="Virtual";
                            GestorMemoria.ramVirtual.push(new RamVirtual(GestorMemoria.procesos[i].Memoria,GestorMemoria.procesos[i].Nproceso,GestorMemoria.procesos[i].Nombre));
                            GestorMemoria.setRamVirtualList();  
                        }
                    }
                    GestorMemoria.setProcesosList(i);
                } 
                i++;
            }
            i=0;           
        }           
    }
}

