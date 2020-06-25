package GUI;

import java.io.InputStreamReader;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public class Main extends JFrame{
    private int jugador_turno; // 0 = azul, 1 = rojo
    private Tablero tablero;
    
    public Main(String titulo){
        super(titulo);
        jugador_turno = 0;
        
        tablero = new Tablero(10, 10);  
        
        this.setSize(800, 800);
        this.setResizable(false);
         
        this.add(tablero); // Cuando añado la vista, se pinta automáticamente*/
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    public static void main (String[] args) throws InterruptedException{
        Main marco = new Main("Ejercicio 5 FR");
        
        // Espero a que se pinten el tablero y las piezas antes de empezar el juego
        try{
            Thread.sleep(100);
        }
        catch (InterruptedException e){}
        
        marco.empezarJuego();
    }
    
    public void empezarJuego(){
        BufferedReader inReader = new BufferedReader(
            new InputStreamReader(System.in));
        
        String pieza;
        String direccion;
        String num_casillas;
        Set<String> piezas_vivas;
        
        boolean partida_terminada = false;
        int ganador;
        
        while(!partida_terminada){
        
            try{
                piezas_vivas = tablero.getPiezasVivas(jugador_turno); // Veo las piezas que le quedan al jugador del turno

                // Muevo una pieza
                // Esta parte se podría pasar

                System.out.println("<Turno del jugador>: " + jugador_turno);

                do{
                    System.out.print("Introduce el nombre de la pieza a mover: ");        
                    pieza = inReader.readLine();
                }while ( !piezas_vivas.contains(pieza) ); // Repito el bucle hasta que se seleccione una pieza viva

                do{
                    System.out.print("Dirección: ");
                    direccion = inReader.readLine(); // pasar 
                }while(!(direccion.equals("1") || direccion.equals("2") || direccion.equals("3")
                        || direccion.equals("4")));

                do{
                    System.out.print("Número de casillas: ");
                    num_casillas = inReader.readLine();
                }while(Integer.parseInt(num_casillas) <= 0);

                tablero.moverPieza(jugador_turno, pieza, Integer.parseInt(direccion), Integer.parseInt(num_casillas));

                // Ataco con una pieza
                // Esta parte se podría pasar

                do{
                    System.out.print("Introduce el nombre de la pieza para atacar: ");        
                    pieza = inReader.readLine();
                }while ( !piezas_vivas.contains(pieza) ); // Repito el bucle hasta que se seleccione una pieza viva

                tablero.atacarConPieza(jugador_turno, pieza);

                // Compruebo si se ha terminado la partida (algún jugador ha perdido todas sus piezas)
                ganador = tablero.getGanador();

                if (ganador != -1){ // La partida se ha terminado
                    partida_terminada = true;
                    
                    if (ganador == 0)
                        System.out.println("¡El ganador es el jugador azul!");
                    else
                        System.out.println("¡El ganador es el jugador rojo!");
                }

                jugador_turno = (jugador_turno + 1) % 2; // Paso el turno al otro jugador
            }
            catch (IOException e){}
        }
    }
}
