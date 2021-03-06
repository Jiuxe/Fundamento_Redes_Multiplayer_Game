package GUI;

import static GUI.Pieza.MAX_ANCHO;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

public class Cuadrado extends Pieza{
    protected static int MAX_PASOS = 2; // Máximo número de pasos que puede recorrer una pieza en un turno
      
    public Cuadrado(int equipo, int pos_x, int pos_y, int ancho, int alto, int max_ancho, int max_alto){
        super(equipo, pos_x, pos_y, ancho, alto, max_ancho, max_alto);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int[] puntos_x = new int[4]; // 0  1
        int[] puntos_y = new int[4]; // 3  2
        
         puntos_x[0] = puntos_x[3] = pos_x*ancho;
         puntos_x[1] = puntos_x[2] = pos_x*ancho + ancho;
                
         puntos_y[0] = puntos_y[1] = pos_y*alto;
         puntos_y[2] = puntos_y[3] = pos_y*alto + alto;
     
        Color color = (equipo == 0) ? new Color(0,0,255) : new Color(255,0,0);
        g.setColor(color);
        
        g.fillPolygon(puntos_x, puntos_y, 4);
    }
    
    @Override
    public void moverPieza(int direccion, int num_pasos){
        if (num_pasos > MAX_PASOS)
            num_pasos = MAX_PASOS;
        
        switch (direccion){
            case 1:
                pos_y-=num_pasos;
                break;
            case 2:
                pos_x+=num_pasos;
                break;
            case 3:
                pos_y+=num_pasos;
                break;
            case 4:
                pos_x-=num_pasos;
                break;
        }
        
        if (pos_x > MAX_ANCHO)
            pos_x = MAX_ANCHO;
        else if (pos_x < 0)
            pos_x = 0;
        
        if (pos_y > MAX_ALTO)
            pos_y = MAX_ALTO;
        else if (pos_y < 0)
            pos_y = 0;
    }
    
    // Solo ataca a las piezas que se encuentran arriba, abajo, derecha e izquierda
    // a una distancia de tres casillas o menos
    @Override
    public HashMap<String, Boolean> atacar(HashMap<String, Pieza> piezas_enemigo){
        HashMap<String, Boolean> haMuerto = new HashMap();
        int distancia;
        
        for (HashMap.Entry<String, Pieza> pieza_actual : piezas_enemigo.entrySet()){
            haMuerto.put(pieza_actual.getKey(), Boolean.FALSE);
            
            if (pieza_actual.getValue().pos_x == this.pos_x){
                distancia = Math.abs(pieza_actual.getValue().pos_y - this.pos_y);
                
                if (distancia <= 3)
                    haMuerto.put(pieza_actual.getKey(), Boolean.TRUE);
            }
            
            else if (pieza_actual.getValue().pos_y == this.pos_y){
                distancia = Math.abs(pieza_actual.getValue().pos_x - this.pos_x);
                
                if (distancia <= 3)
                    haMuerto.put(pieza_actual.getKey(), Boolean.TRUE);
            }
        }
        
        return haMuerto;
    } 
}
