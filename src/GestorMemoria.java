
import static java.lang.Integer.parseInt;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GestorMemoria extends javax.swing.JFrame {
    
    Hilo_Procesos _hiloProcesos=null;
    Hilo_Memoria _hiloMemoria=null;
    public static Proceso[] procesos=new Proceso[15];
    public static RamAsign[] ramBank=new RamAsign[0];
    public static Stack<RamVirtual> ramVirtual=new Stack<RamVirtual>();
    public static Stack<PagHistorial> pagHistorial=new Stack<PagHistorial>();
    public static ControlRam RAM=new ControlRam(0,0,0,0);
    public static DefaultTableModel Tabla1;
    public static DefaultTableModel Tabla2;
    public static DefaultTableModel TablePaginasR;
    public static DefaultTableModel TableRamV;
    public static DefaultTableModel TablePaginaH;
      
    public GestorMemoria() {
        initComponents();
        if(_hiloProcesos==null){
          _hiloProcesos = new Hilo_Procesos();  
        }
        if(_hiloMemoria==null){
          _hiloMemoria = new Hilo_Memoria();  
        }
        procesos=new Proceso[15];
        ramBank=new RamAsign[0];
        RAM=new ControlRam(0,0,0,0);
        Tabla1 =(DefaultTableModel) jTable1.getModel();
        Tabla2 =(DefaultTableModel) jTable2.getModel();
        TablePaginasR =(DefaultTableModel) tpaginas.getModel();
        TablePaginaH =(DefaultTableModel) ttablapag.getModel();
        TableRamV =(DefaultTableModel) tmvirtual.getModel();
        init();
    }
    
    private void init(){
        Tabla2.setRowCount(0);
        TablePaginasR.setRowCount(0);
        for(int p=0; p<15; p++){
            procesos[p]=new Proceso();
        }
        float countRow=Integer.valueOf(tfMtotal.getText())/200000;
        ramBank=new RamAsign[(int)Math.ceil(countRow)];
        for(int p=0; p<(int)Math.ceil(countRow); p++){
            ramBank[p]=new RamAsign();
            /* tabla de memoria */
            Object[] tabla2 = new Object[5];
            tabla2[0]= ramBank[p].BankSize;
            tabla2[1]= ramBank[p].BankUsed;
            tabla2[2]= ramBank[p].BankFree;
            tabla2[3]= ramBank[p].TitleProcess; 
            tabla2[4]= ramBank[p].KeyProcess; 
            Tabla2.addRow(tabla2);
            Tabla2.fireTableDataChanged();
            /*tabla de paginacion */
            Object[] tablap = new Object[3];
            tabla2[0]= ramBank[p].KeyProcess;//proceso
            tabla2[1]= p;//pagina
            tabla2[2]= ramBank[p].BankSize;//memoria
            TablePaginasR.addRow(tabla2);
            TablePaginasR.fireTableDataChanged();
        }
        RAM=new ControlRam(Integer.valueOf(GestorMemoria.tfMtotal.getText()),Integer.valueOf(GestorMemoria.tfMtotal.getText()),0,ramBank.length);
    }
    
    void Matar_proceso(){
        int fila = jTable1.getSelectedRow();
        jTable1.setValueAt("0", fila, 2);
        jTable1.setValueAt("Terminado", fila, 3);        
    }
    
    public static boolean Verify(int _ram){
       boolean res=true;
       if( _ram>4000000 ){ 
           res=false;
       }
       if( jTable1.getRowCount() >= 15 ){ 
           res=false;
       }
       return res;
    }

    public static void main(String args[]) {
   
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestorMemoria().setVisible(true);
            }
        });
    }
    
    public void Crear(){ //Crea proceso y se agrega a la tabla
        int key = jTable1.getRowCount();
        if(Verify(RAM.MemoryGlobal)){
            procesos[key]=new Proceso();
            procesos[key].SetValues(key, Integer.valueOf(tfMemoria.getText()), 0, "Espera",tfNombre.getText());
            Object[] newRow = new Object[4];
            newRow[0]= key+1;
            newRow[1]= procesos[key].Nombre;
            newRow[2]= procesos[key].Memoria;
            newRow[3]= procesos[key].Estado;
            Tabla1.addRow(newRow); 
            //limpiar textfield's
            tfNombre.setText(null);
            tfMemoria.setText(null);
            //reenfocar en textfield nombre para agregar otro
            tfNombre.grabFocus(); 
            //refrescar listado de procesos
            Tabla1.fireTableDataChanged();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor no sobrepasar los recursos del sistema");
        }
        
        if(_hiloProcesos.getState().toString().contentEquals("NEW")){
           _hiloProcesos.start(); 
        }
        if(_hiloMemoria.getState().toString().contentEquals("NEW")){
           _hiloMemoria.start(); 
        }
    } 
    
    public static void setProcesosList(int _key){
        Tabla1.setValueAt(procesos[_key].Nombre, _key,1);
        Tabla1.setValueAt(procesos[_key].Memoria, _key,2);
        Tabla1.setValueAt(procesos[_key].Estado, _key,3); 
        Tabla1.fireTableDataChanged();
    }
    
    public static void setRamVirtualList(){
        TableRamV.setRowCount(0);
        for (RamVirtual item: ramVirtual) {
            Object[] newRow = new Object[3];
            newRow[0]= item.TitleProcess;//proceso nombre
            newRow[1]= item.KeyProcess;//id proceso marco
            newRow[2]= item.BankSize;//memoria proceso
            TableRamV.addRow(newRow); 
        }
        TableRamV.fireTableDataChanged();
    }
    
    public static void setPaginaHList(int _pro,int _pag){
        TablePaginaH.setRowCount(0);
        boolean add=true;
        for (PagHistorial item: pagHistorial) {
            if((_pro+1)==item.marco){
                add=false;
            }
            Object[] newRow = new Object[2];
            newRow[0]= item.pagina;//pagina
            newRow[1]= item.marco;//id proceso marco
            TablePaginaH.addRow(newRow); 
        }
        if(add){
            pagHistorial.push(new PagHistorial(_pag,_pro+1));
            Object[] newRow = new Object[2];
            newRow[0]= _pag;//pagina
            newRow[1]= _pro+1;//id proceso marco
            TablePaginaH.addRow(newRow);  
        }
        TablePaginaH.fireTableDataChanged();
    }
    
    public static void setBlockMemory(int _key){
        /* actualizar info de memoria en uso */
        tfMenuso.setText(String.valueOf(RAM.MemoryUsed));
        tfMdisponible.setText(String.valueOf(RAM.MemoryFree));
        /* actualizar tabla de memoria fisica */
        Tabla2.setValueAt(ramBank[_key].BankUsed, _key,1);
        Tabla2.setValueAt(ramBank[_key].BankFree, _key,2);
        Tabla2.setValueAt(ramBank[_key].TitleProcess, _key,3); 
        Tabla2.setValueAt(ramBank[_key].KeyProcess, _key,4); 
        Tabla2.fireTableDataChanged();
        /* actualizar tabla de paginacion resumen */
        TablePaginasR.setValueAt(ramBank[_key].TitleProcess, _key,0);
        TablePaginasR.fireTableDataChanged();
        /* verificar si se encuentra en la memoria virtual*/
        int keyPV=VerifyVirtual(ramBank[_key].KeyProcess);
        if(keyPV>-1){
            /* eliminarlo de la memoria virtual */
            ramVirtual.remove(keyPV);
            setRamVirtualList(); 
        }
    }
    private static int VerifyVirtual(int _ver){
        int res=-1;
        int inc=0;
        for (RamVirtual item: ramVirtual){
            if(item.KeyProcess==_ver){
                res=inc;
            }
            inc++;
        }
        return res;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bCrear = new javax.swing.JButton();
        tfNombre = new javax.swing.JTextField();
        tfMemoria = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLProcesos = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tpaginas = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        ttablapag = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLAsignMem = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tmvirtual = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tfMtotal = new javax.swing.JTextField();
        tfMenuso = new javax.swing.JTextField();
        tfMdisponible = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Gestor de Memoria");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Crear Nuevo Proceso");

        jLabel3.setText("Nombre");

        jLabel5.setText("Memoria");

        bCrear.setText("Crear ");
        bCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCrearActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Memoria KB", "Estado"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        jLProcesos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLProcesos.setText("Procesos");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Paginas");

        tpaginas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Proceso", "Pagina", "Tamaño"
            }
        ));
        jScrollPane3.setViewportView(tpaginas);

        ttablapag.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pagina", "Marco"
            }
        ));
        jScrollPane4.setViewportView(ttablapag);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tamaño marco", "En Uso KB", "Disponible KB", "Procesos", "Marco"
            }
        ));
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        jLAsignMem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLAsignMem.setText("Asignacion de Memoria");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Memoria Virtual");

        tmvirtual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Proceso", "ID", "Memoria (KB)"
            }
        ));
        jScrollPane5.setViewportView(tmvirtual);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Tabla de paginas");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("MEMORIA KB");

        jLabel7.setText("Total");

        jLabel8.setText("En uso");

        jLabel9.setText("Disponible");

        tfMtotal.setText("2097152");
        tfMtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfMtotalActionPerformed(evt);
            }
        });

        tfMenuso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfMenusoActionPerformed(evt);
            }
        });

        jButton1.setText("Reasignar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfMdisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfMenuso, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfMtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfMtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfMenuso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfMdisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLProcesos, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel5)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(tfNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                            .addComponent(tfMemoria))
                                        .addGap(27, 27, 27)
                                        .addComponent(bCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLAsignMem)
                                .addGap(120, 120, 120)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77)
                                .addComponent(jLabel11))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfMemoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(bCrear))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLProcesos)
                            .addComponent(jLAsignMem)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel11)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addGap(41, 41, 41))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 295, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(215, 215, 215)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(422, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCrearActionPerformed

        if(
            ((Integer.parseInt(tfMemoria.getText()))<=(parseInt(tfMtotal.getText())))){
            Crear();
        }else{
            JOptionPane.showMessageDialog(null, "Por favor no sobrepasar los recursos del sistema");
            tfMemoria.setText(null);
            tfMemoria.grabFocus();  
            }    
    }//GEN-LAST:event_bCrearActionPerformed

    private void tfMenusoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfMenusoActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tfMenusoActionPerformed

    private void tfMtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfMtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfMtotalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        init();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCrear;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLAsignMem;
    private javax.swing.JLabel jLProcesos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    public static javax.swing.JTextField tfMdisponible;
    private javax.swing.JTextField tfMemoria;
    public static javax.swing.JTextField tfMenuso;
    public static javax.swing.JTextField tfMtotal;
    private javax.swing.JTextField tfNombre;
    private javax.swing.JTable tmvirtual;
    private javax.swing.JTable tpaginas;
    private javax.swing.JTable ttablapag;
    // End of variables declaration//GEN-END:variables
}
