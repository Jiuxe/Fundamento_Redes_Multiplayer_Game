package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap.*;
import java.util.Set;
import javax.swing.JPanel;

public class Tablero extends JPanel{
    private int num_fil, num_col; // Ancho y alto de las casillas, en píxeles
    private int ancho, alto;
    private HashMap<String, Pieza> piezas_azules;
    private HashMap<String, Pieza> piezas_rojas;
    
    private boolean inicializado = false;
    
    public Tablero(int num_fil, int num_col){
        this.num_fil = num_fil;
        this.num_col = num_col;
            
        piezas_azules = new HashMap();
        piezas_rojas = new HashMap();
        
    }
    
    private void pintaTablero(Graphics g, int num_fil, int num_col){
        int[] puntos_x = new int[4]; // 0  1
        int[] puntos_y = new int[4]; // 3  2
        
        ancho = this.getSize().width / num_col;
        alto = this.getSize().height / num_fil;
        
        Color color_1 = new Color(70,30,0);
        Color color_2 = new Color(170,120,70);
        Color color_actual;
        
        for (int fil = 0; fil < num_fil; fil++)
            for (int col = 0; col < num_col; col++){
                color_actual = (fil + col) % 2 == 0 ? color_1 : color_2;
                g.setColor(color_actual);
                
                puntos_x[0] = puntos_x[3] = ancho*col;
                puntos_x[1] = puntos_x[2] = ancho*(col+1);
                
                puntos_y[0] = puntos_y[1] = alto*fil;
                puntos_y[2] = puntos_y[3] = alto*(fil+1);
                
                g.fillPolygon(puntos_x, puntos_y, 4);
            }
    }
    
    
    // Equipo 0 = azul, 1 = rojo
    private void pintaPiezas(Graphics g){   
        // Cada jugador tiene 4 piezas, una de cada tipo
        
        if (!inicializado){
            Triangulo triangulo = new Triangulo(0,num_col / 2,num_fil-1,ancho,alto,num_col-1,num_fil-1);
            //this.add(triangulo); 
            piezas_azules.put("Triangulo", triangulo);

            Cuadrado cuadrado = new Cuadrado(0,num_col / 2 + 2,num_fil-1,ancho,alto,num_col-1,num_fil-1);
            //this.add(cuadrado);
            piezas_azules.put("Cuadrado", cuadrado);

            Rombo rombo = new Rombo(0,num_col-1,num_fil / 2 + 2,ancho,alto,num_col-1,num_fil-1);
            //this.add(rombo);
            piezas_azules.put("Rombo", rombo);

            Circulo circulo = new Circulo(0,num_col-1,num_fil / 2,ancho,alto,num_col-1,num_fil-1);
            //this.add(circulo);
            piezas_azules.put("Circulo", circulo);
            
            triangulo = new Triangulo(1,num_col / 2 - 1,0,ancho,alto,num_col-1,num_fil-1);
            //this.add(triangulo); 
            piezas_rojas.put("Triangulo", triangulo);

            cuadrado = new Cuadrado(1,num_col / 2 - 3,0,ancho,alto,num_col-1,num_fil-1);
            //this.add(cuadrado);
            piezas_rojas.put("Cuadrado", cuadrado);

            rombo = new Rombo(1,0,num_fil / 2 - 3,ancho,alto,num_col-1,num_fil-1);
            //this.add(rombo);
            piezas_rojas.put("Rombo", rombo);

            circulo = new Circulo(1,0,num_fil / 2 - 1,ancho,alto,num_col-1,num_fil-1);
            //this.add(circulo);
            piezas_rojas.put("Circulo", circulo);
        }
        
        inicializado = true;
        
        for (HashMap.Entry<String, Pieza> pieza_actual : piezas_azules.entrySet()){
            pieza_actual.getValue().paintComponent(g);
        }
        
        for (HashMap.Entry<String, Pieza> pieza_actual : piezas_rojas.entrySet()){
            pieza_actual.getValue().paintComponent(g);
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        pintaTablero(g, num_fil, num_col);
        pintaPiezas(g);
    }
    
    // Equipo -> 0 = azul, 1 = rojo
    public Set<String> getPiezasVivas(int equipo){
        if (equipo == 0) 
            return piezas_azules.keySet();
        else
            return piezas_rojas.keySet();
    }
    
    // Equipo -> 0 = azul, 1 = rojo
    // Pieza -> "Triangulo", "Circulo", "Cuadrado", "Rombo"
    public void moverPieza(int equipo, String pieza, int dir, int num_pasos){
        if (equipo == 0)
            piezas_azules.get(pieza).moverPieza(dir, num_pasos);
        else
            piezas_rojas.get(pieza).moverPieza(dir, num_pasos);
        
        this.repaint(); // Para que se vea la pieza en su nueva posición, hago repaint del tablero
    }
   
    // Ataca con la pieza especificada. Se eliminan del juego las piezas enemigas
    // dentro del radio de ataque
    public void atacarConPieza(int equipo, String pieza){
        HashMap<String, Boolean> haMuerto;
        
        // Equipo azul
        if (equipo == 0){        
            haMuerto = piezas_azules.get(pieza).atacar(piezas_rojas); // Le paso las piezas del enemigo
            
            for (HashMap.Entry<String, Boolean> pieza_actual : haMuerto.entrySet()){
                if (pieza_actual.getValue() == Boolean.TRUE) // Esa pieza ha sido comida
                    piezas_rojas.remove(pieza_actual.getKey()); 
            }
        }
        else{ // Equipo rojo
            haMuerto = piezas_rojas.get(pieza).atacar(piezas_azules); // Le paso las piezas del enemigo
            
            for (HashMap.Entry<String, Boolean> pieza_actual : haMuerto.entrySet()){
                if (pieza_actual.getValue() == Boolean.TRUE) // Esa pieza ha sido comida
                    piezas_azules.remove(pieza_actual.getKey());
            }
        }
        
        this.repaint();
        // Comprobar si ha ganado alguien
    }
    
    // Este método devuelve si el juego ha terminado y quién es el ganador en ese caso
    // Si todavía no ha terminado la partida devuelve -1
    // Si ha terminado y ha ganado el azul, devuelve 0
    // Si ha terminado y ha ganado el rojo, devuelve 1
    public int getGanador(){
        int salida;
        
        if (piezas_azules.isEmpty())
            salida = 1;
        else if (piezas_rojas.isEmpty())
            salida = 0;
        else
            salida = -1;
        
        return salida;
    }

    
}
