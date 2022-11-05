public class Hilo_Memoria extends Thread{
   
    int Quantum = RandomNumber.generateRandom();
    
    public Hilo_Memoria (){
        
    }
    
    private void Dormir(){
        try{
            Thread.sleep(2); //controla la velocidad de ejecucion
        }catch(InterruptedException ex){
            
        }
    }
    
    public void CalcRam(int _ram, String _est){
        
    }
    
    public void Borrar(int b){ 
        
    }
    
    @Override
    public void run(){      
        while(true){
            for(int p=0; p<GestorMemoria.ramBank.length; p++){
                if(GestorMemoria.ramBank[p].BankUsed>0){
                    for(int inc=0; inc<Quantum; inc++){
                        Dormir();
                        GestorMemoria.ramBank[p].BankUsed-=1;
                        GestorMemoria.ramBank[p].BankFree+=1;
                        if(GestorMemoria.ramBank[p].BankUsed==-1){
                            GestorMemoria.ramBank[p].BankUsed=0;
                            GestorMemoria.ramBank[p].BankFree=200000;
                            GestorMemoria.ramBank[p].TitleProcess="----";
                            GestorMemoria.setBlockMemory(p);
                            GestorMemoria.RAM.BankMemory+=1;
                            GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Ejecucion+=200000;
                            
                            if(GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Ejecucion>GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Memoria){
                                GestorMemoria.RAM.MemoryUsed-=GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Memoria-(GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Ejecucion-200000);
                                GestorMemoria.RAM.MemoryFree+=GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Memoria-(GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Ejecucion-200000);
                                GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Estado="Terminado";
                                GestorMemoria.procesos[GestorMemoria.ramBank[p].KeyProcess].Memoria=0;
                                GestorMemoria.setProcesosList(GestorMemoria.ramBank[p].KeyProcess); 
                                GestorMemoria.setPaginaHList(GestorMemoria.ramBank[p].KeyProcess,p); 
                            } else {
                                GestorMemoria.RAM.MemoryUsed-=200000;
                                GestorMemoria.RAM.MemoryFree+=200000;
                            }
                            break;
                        }
                        GestorMemoria.setBlockMemory(p);
                    }  
                }               
            }        
        }           
    }
}

