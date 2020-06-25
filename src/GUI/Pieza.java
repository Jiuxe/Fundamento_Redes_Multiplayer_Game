package GUI;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComponent;

// Las piezas son sprites (se pintan a sí mismas)
public abstract class Pieza extends JComponent{
    protected int ancho, alto; // Ancho y alto de los sprites, en píxeles
    protected int pos_x, pos_y; // Posición como cuadrícula, no píxeles. El 0,0 corresponde a la esquina superior izquierda
    protected int equipo; // 0 = azul, 1 = rojo
    
    protected static int MAX_ANCHO; // Última columna 
    protected static int MAX_ALTO; // Última fila (se usa para que las piezas no se salgan del tablero)
    
    public Pieza(int equipo, int pos_x, int pos_y, int ancho, int alto, int max_ancho, int max_alto){
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.ancho = ancho;
        this.alto = alto;
        this.equipo = equipo;
        
        MAX_ANCHO = max_ancho;
        MAX_ALTO = max_alto;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
    
    @Override
    public int getX(){
        return pos_x;
    }
    
    @Override
    public int getY(){
        return pos_y;
    }
    
    // Dirección: 1 -> norte, 2 -> este, 3 -> sur, 4 -> oeste
    public abstract void moverPieza(int direccion, int num_pasos);
    
    // Se le pasan todas las piezas que quedan del enemigo y devuelve en el map
    // para cada pieza si estaba en el efecto del ataque o no (false->no, true->sí (ha muerto))
    public abstract HashMap<String, Boolean> atacar(HashMap<String, Pieza> piezas_enemigo);
}
