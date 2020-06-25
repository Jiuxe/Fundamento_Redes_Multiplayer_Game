package GUI;

import static GUI.Pieza.MAX_ANCHO;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

public class Circulo extends Pieza{
    protected static int MAX_PASOS = 2; // Máximo número de pasos que puede recorrer una pieza en un turno
      
    public Circulo(int equipo, int pos_x, int pos_y, int ancho, int alto, int max_ancho, int max_alto){
        super(equipo, pos_x, pos_y, ancho, alto, max_ancho, max_alto);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Color color = (equipo == 0) ? new Color(0,0,255) : new Color(255,0,0);
        g.setColor(color);
        
        g.fillOval(pos_x*ancho, pos_y*alto, ancho, alto);
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
    
    // Ataca a las piezas que se encuentran a una distancia (en línea recta (Manhattan))
    // de dos o menos
    @Override
    public HashMap<String, Boolean> atacar(HashMap<String, Pieza> piezas_enemigo){
        HashMap<String, Boolean> haMuerto = new HashMap();
        int distancia;
        
        for (HashMap.Entry<String, Pieza> pieza_actual : piezas_enemigo.entrySet()){
            haMuerto.put(pieza_actual.getKey(), Boolean.FALSE);
            distancia = Math.abs(pieza_actual.getValue().pos_y - this.pos_y) +
                    Math.abs(pieza_actual.getValue().pos_x - this.pos_x);
            
            if (distancia <= 2)
                haMuerto.put(pieza_actual.getKey(), Boolean.TRUE);
        }
        
        return haMuerto;
    } 
}
