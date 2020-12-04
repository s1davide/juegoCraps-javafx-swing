/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegocraps;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;


/**
 *
 * @author David E
 */
public class VentanaCraps  extends JFrame{
    private Object[]elementosMostrar;
    private Object[]dados;
    private int resultado;
    private int cantidadDados;
    private int conteoDados;
    private boolean punto;
    private int valorPunto;
    public VentanaCraps(){        
        this.setLayout(null);       
        this.setTitle("Juego Craps");     
        this.resultado = 0;
        this.cantidadDados= 2;
        this.conteoDados = 0;
        this.punto = false;
        this.valorPunto = 0;
    }
    public void iniciaGui()  {
      // crear un cubo 3D, anchura, altura y profundidad
    // crear la escena, true para activar el buffer de profindidad 
        // crear una luz puntual
        // crear una camara en perspectiva        
        final JFXPanel fxPanel = new JFXPanel();
        JLabel fondo =  new JLabel();        
        JLabel labelResultado =  new JLabel();            
        JLabel resultado =  new JLabel();     
        JLabel estadoJuego =  new JLabel();             
        Font fuenteEG = new Font("Arial", Font.BOLD, 12);
        fondo.setBackground(java.awt.Color.WHITE);
        fondo.setOpaque(true);        
        labelResultado.setText("Resultado");
        labelResultado.setFont(fuenteEG);
        resultado.setHorizontalAlignment(SwingConstants.CENTER);
        resultado.setText("0");
        resultado.setFont(fuenteEG);
        estadoJuego.setText("Esperando Lanzamiento");
        estadoJuego.setHorizontalAlignment(SwingConstants.CENTER);
        estadoJuego.setFont(new Font("Arial", Font.BOLD, 19));
        this.setBounds(0,0,500, 450);
        JButton boton = boton("Lanzar Dados");
        boton.setBounds((this.getWidth()/2)-65,350,90, 30);    
        fondo.setBounds(0, 0, this.getWidth(), this.getHeight());
        labelResultado.setBounds((this.getWidth()/2)-50, 200, 200, 30);                
        resultado.setBounds((this.getWidth()/2)-122, 230, 200, 30);        
        estadoJuego.setBounds(-22, 280, this.getWidth(), 30);                 
        this.add(boton);
        this.add(fxPanel);
        this.add(resultado);
        this.add(labelResultado);        
        this.add(estadoJuego); 
        this.add(fondo);                
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);  
        this.setResizable(false);        
        elementosMostrar =new Object[] {resultado, estadoJuego, this};
        Platform.runLater(() -> {//Runnable
            iniciaFX(fxPanel, this, boton);            
        });        
    }
    private JButton  boton(String text){
        JButton b = new JButton();        
        b.setText(text);
        ImageIcon iconNormal = new ImageIcon("src/img/btn-normal.png");        
        ImageIcon iconOver = new ImageIcon("src/img/btn-over.png");          
        b.setIcon(iconNormal);
        b.setRolloverIcon(iconOver);
        b.setBorder(null);
        b.setForeground(java.awt.Color.white);
        b.setHorizontalTextPosition(SwingConstants.CENTER);               
        return b;
    }
    private void iniciaFX(JFXPanel fXPanel, JFrame frame, JButton b){       
        Scene scene = crearEscena(b);
        fXPanel.setSize(frame.getWidth(), 200);
        fXPanel.setScene(scene);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    private Scene crearEscena(JButton b){
        PointLight light = new PointLight();
        light.setTranslateX(80);
        light.setTranslateY(-350);
        light.setTranslateZ(-170);                      
        Dado dado1 = new Dado(-10, -230, 0,100, 100, 100);
        Dado dado2 = new Dado(190, -230, 0,100, 100, 100);
        dados = new Object[]{dado1, dado2};
        dado1.setTextureModeImage(JuegoCraps.class.getResource("/img/dado.png").toExternalForm());
        dado2.setTextureModeImage(JuegoCraps.class.getResource("/img/dado.png").toExternalForm());                
        dado1.setId("1");
        dado2.setId("2");
        agregarEventoClick(dado1, b, elementosMostrar);
        agregarEventoClick(dado2, b, elementosMostrar );
        Group root = new Group();        
        root.getChildren().add(dado1);
        root.getChildren().add(dado2);
        root.getChildren().add(light);
        Scene escena = new Scene(root, 300, 250, true, SceneAntialiasing.BALANCED);
        escena.setFill(Color.WHITE);
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(escena.getWidth() / -2.0);
        camera.setTranslateY((escena.getHeight() / -2.0) - 200);
        escena.setCamera(camera);
           return escena;
       }
    
    private void agregarEventoClick (Dado dado, JButton b, Object[]elementosMostrar){  
        ActionListener animacion = (ActionEvent e) -> {  //new action listener
            dado.lanzarDado(elementosMostrar, punto);
        };
        b.addActionListener(animacion);        
    }
    public void muestraResultado(int res, Object [] labels){        
        JLabel lResultado = (JLabel) labels[0];
        JLabel lEstado = (JLabel) labels[1];
        conteoDados +=1;                
        if(conteoDados==cantidadDados){
            lResultado.setText(String.valueOf(resultado)); 
            if(resultado==2&& !punto || resultado==3 && !punto || resultado==12 && !punto){                
                lEstado.setText("Craps");                
                conteoDados=0;
                resultado=0;
                finJuego("lose");
            }
            else if(resultado==7 && !punto || resultado==11 && !punto){
                lEstado.setText("Natural");
                finJuego("win");
                conteoDados=0;
                resultado=0;
            }
            else{
                if(!punto){
                lEstado.setText("Punto: "+ resultado);
                conteoDados=0;                
                punto =true;
                valorPunto=resultado;
                resultado=0;
                }
                else if(punto && resultado ==7){
                    //Partida perdida
                    finJuego("lose");
                    punto=false;
                }else if(punto && resultado == valorPunto){                    
                    //Partida ganada
                    finJuego("win");
                    punto=false;
                }
                else{                    
                    conteoDados=0;
                    resultado=0;                                        
                }
            }            
        }
        
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }
    public void finJuego(String accionFin){
        System.err.println("fin");
        UIManager UI = new UIManager();
        UI.put("OptionPane.background",new ColorUIResource(java.awt.Color.white));
        UI.put("Panel.background",new ColorUIResource(java.awt.Color.white));
        JOptionPane optionPane = new JOptionPane();    
        
        ImageIcon icono;
        String titulo;
        if(accionFin.equals("win")){        
            optionPane.setMessage("Has ganado la partida");  
            titulo = "Victoria";
            icono = new ImageIcon("src/img/apl.jpg");
            
        }else{
            optionPane.setMessage("Has perdido la partida");        
            titulo = "Derrota";
            icono = new ImageIcon("src/img/facep.png");
        }
        Image img = icono.getImage();
        img = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        icono = new ImageIcon(img);
        optionPane.setIcon(icono);
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);                       
        JButton btnAceptar = boton("Volver a Jugar");
        JButton btnCancelar = boton("Salir");
        JDialog dialog = optionPane.createDialog(this,titulo);   
        ActionListener actionListener1 = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {        
        dialog.dispose();
        reiniciaJuego();
        }
      };
        btnAceptar.addActionListener(actionListener1);
        ActionListener actionListener2 = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {        
        dispose();
        }
      };
        btnCancelar.addActionListener(actionListener2);
        optionPane.setOptions(new Object[] { btnAceptar, btnCancelar });            
        dialog.setVisible(true);        
    }
    public void reiniciaJuego(){
        conteoDados=0;
        resultado=0;                    
        valorPunto=0;
        Dado dado1 = (Dado) dados[0];
        Dado dado2 = (Dado) dados[1];
        dado1.restablecerPosicion(elementosMostrar, true);
        dado2.restablecerPosicion(elementosMostrar, true);
        JLabel lResultado = (JLabel) elementosMostrar[0];
        JLabel lEstado = (JLabel) elementosMostrar[1];
        lResultado.setText("0");        
        lEstado.setText("Esperando Lanzamiento");
    }
    
}
