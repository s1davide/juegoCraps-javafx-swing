/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegocraps;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.util.Duration;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 *
 * @author David E
 */
public class Dado extends CuboidMesh{
    private boolean permiteEjecucion;
    private int direcionResultadoX;
    private int direcionResultadoY;
    private int gradoRotacion;
    private String ejeRotacion;
    private int resultadoDado;    
    private boolean dadoRotado;
    private Hashtable<Integer, String> ubicacionCaras;
    public Dado(int translX,int translY,int translZ, int width, int height, int depth  ) {   
        this.permiteEjecucion =true;
        this.setWidth(width);
        this.setHeight(height);
        this.setDepth(depth);        
        this.setTranslateX(translX);
        this.setTranslateY(translY);
        this.setTranslateZ(translZ);
        this.resultadoDado = 0;
        this.dadoRotado = false;
        this.ubicacionCaras = new Hashtable<>();
        this.ubicacionCaras.put(1, "X_AXIS, 360");
        this.ubicacionCaras.put(2, "X_AXIS, 630");
        this.ubicacionCaras.put(3, "Y_AXIS, 450");
        this.ubicacionCaras.put(4, "Y_AXIS, 630");
        this.ubicacionCaras.put(5, "X_AXIS, 450");
        this.ubicacionCaras.put(6, "X_AXIS, 540");
    }

    public boolean getPermiteEjecucion() {
        return permiteEjecucion;
    }

    public void setPermiteEjecucion(boolean permiteEjecucion) {
        this.permiteEjecucion = permiteEjecucion;
    }

    public void setDirecionResultadoX(int direcionResultado) {
        this.direcionResultadoX = direcionResultado;
    }

    public int getDirecionResultadoX() {
        return direcionResultadoX;
    }
    public void setDirecionResultadoY(int direcionResultado) {
        this.direcionResultadoY = direcionResultado;
    }

    public int getDirecionResultadoY() {
        return direcionResultadoY;
    }

    public int getGradoRotacion() {
        return gradoRotacion;
    }

    public void setGradoRotacion(int gradoRotacion) {
        this.gradoRotacion = gradoRotacion;
    }

    public int getResultadoDado() {
        return resultadoDado;
    }

    public void setResultadoDado(int resultadoDado) {
        this.resultadoDado = resultadoDado;
    }

    public String getEjeRotacion() {
        return ejeRotacion;
    }

    public void setEjeRotacion(String ejeRotacion) {
        this.ejeRotacion = ejeRotacion;
    }

   public void lanzarDado(Object[] labels, boolean punto){
       JLabel lEstado = (JLabel) labels[1];
       JLabel lResultado = (JLabel) labels[0];
       VentanaCraps vc = (VentanaCraps) labels[2];
       if(!punto){
           lEstado.setText("Esperando Resultado");       
       };
       
       
       lResultado.setText("0");       
       if(getPermiteEjecucion()==true){
       restablecerPosicion(labels, false);
       }
   }

   public void restablecerPosicion(Object[] labels, boolean reiniciar){
       setPermiteEjecucion(false);    
       RotateTransition rt =  new RotateTransition(Duration.millis(500),this);
       if (dadoRotado && !reiniciar){
            if(getEjeRotacion().equals("X_AXIS")){                      
               rt.setAxis(new Point3D(getDirecionResultadoX(), 0, 0));
            }                            
            else{
               rt.setAxis(new Point3D(0, getDirecionResultadoY(), 0));
            }      
            rt.setByAngle(-getGradoRotacion());
            rt.setCycleCount(1);       
            rt.setOnFinished((javafx.event.ActionEvent t) -> {
                    iniciaLanzamiento( labels);                    
            });
            rt.play();
       }else if(reiniciar){
           if(getEjeRotacion().equals("X_AXIS")){                      
               rt.setAxis(new Point3D(getDirecionResultadoX(), 0, 0));
            }                            
            else{
               rt.setAxis(new Point3D(0, getDirecionResultadoY(), 0));
            }      
            rt.setByAngle(-getGradoRotacion());
            rt.setCycleCount(1);       
            rt.setOnFinished((javafx.event.ActionEvent t) -> {
                   dadoRotado =false;
                   setPermiteEjecucion(true);
            });
            rt.play();
       }
       else{
            iniciaLanzamiento(labels);            
       }
       
   }
   
   public void iniciaLanzamiento(Object[] labels){       
        int direccionAnimacionX=0;
        int direccionAnimacionY=0;
        if(getId().equals("1")){
               direccionAnimacionX=-360;
               direccionAnimacionY=-280;
        }else{
                  direccionAnimacionX=360;
                  direccionAnimacionY=280;
        }  
        
            generaResultado();
            RotateTransition rt =  new RotateTransition(Duration.millis(3000),this);
            rt.setAxis(new Point3D(direccionAnimacionX, direccionAnimacionY, 0));
            rt.setByAngle(2520);
            rt.setCycleCount(1);                    
            rt.setOnFinished((javafx.event.ActionEvent t) -> {
                obtieneResultado(labels);
            });
            rt.play();
   }
   public void generaResultado(){
       
    Timer timer;
    timer = new Timer();

    TimerTask task = new TimerTask() {
        int tic=0;

        @Override
        public void run()
        {
             try {                          
                setResultadoDado((int) Math.round(Math.random()*6+1));                        
                int resDado = getResultadoDado();
                setEjeRotacion(ubicacionCaras.get(resDado).split(",")[0]);
                setGradoRotacion(Integer.parseInt(ubicacionCaras.get(resDado).split(",")[1].replace(" ", ""))); 
                timer.cancel();
                timer.purge();                
            } catch (Exception except) {                                   
            }
        }
        };        
    timer.schedule(task, 0, 100);
    
        
   }
   
   public void obtieneResultado(Object[] labels){
        if(getEjeRotacion().equals("X_AXIS")){
           setDirecionResultadoX(90); 
           setDirecionResultadoY(0);
        }                            
        else{
           setDirecionResultadoX(0); 
           setDirecionResultadoY(90);
        }        
         RotateTransition rt =  new RotateTransition(Duration.millis(500),this);
            rt.setAxis(new Point3D(getDirecionResultadoX(),getDirecionResultadoY(), 0));            
            rt.setByAngle(getGradoRotacion());            
            rt.play();
            rt.setOnFinished(
            (javafx.event.ActionEvent t) -> {                   
                    setPermiteEjecucion(true);                    
                    VentanaCraps vc = (VentanaCraps) labels[2];
                    vc.setResultado(getResultadoDado()+ vc.getResultado());
                    vc.muestraResultado(getResultadoDado(), labels);
                    dadoRotado=true;
            });
    }
}
