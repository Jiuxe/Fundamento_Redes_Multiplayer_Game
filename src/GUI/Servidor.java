package GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Servidor extends JFrame{

    boolean partida_terminada = false;
    String pieza, pieza_ataque, num_casillas, direccion;
    private Tablero tablero = new Tablero(10, 10);
    private int jugador_turno = 0 ;
    private int port = 8887;
    private int ganador;
    String host = "localhost";
    ServerSocket socketServidor, socketServidor_j1, socketServidor_j2;
    Socket socketConexion, socketConexion_j1, socketConexion_j2 ;
    
    PrintWriter outPrinter;
    BufferedReader in;
    
    public Servidor(String titulo){
        
        super(titulo);
        jugador_turno = 0;
        
        tablero = new Tablero(10, 10);  
        
        this.setSize(400, 400);
        this.setResizable(false);
        
        this.add(tablero); // Cuando añado la vista, se pinta automáticamente*/
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        //this.setVisible(false);
    }
    
    public void empezarJuego() throws IOException{
        
        socketServidor = new ServerSocket(port);
        socketServidor_j1 = new ServerSocket(8888);
        socketServidor_j2 = new ServerSocket(8889);
        
        System.out.println("Escuchando en puerto: " + port);
	socketConexion = socketServidor.accept();
   
        outPrinter = new PrintWriter(socketConexion.getOutputStream(),true);
        
        /********************JUGADOR 1***************************/
        outPrinter.println(8888);
        outPrinter.flush();
        System.out.println("Escuchando en puerto: 8888");
        socketConexion_j1 = socketServidor_j1.accept();
        
        PrintWriter outPrinterJ1 = new PrintWriter(socketConexion_j1.getOutputStream(),true);
	BufferedReader inJ1 = new BufferedReader(new InputStreamReader(socketConexion_j1.getInputStream()));
        
        /********************JUGADOR 2****************************/
        System.out.println("Escuchando en puerto: " + port);
	socketConexion = socketServidor.accept();
        
        outPrinter = new PrintWriter(socketConexion.getOutputStream(),true);
        
        outPrinter.println(8889);
        outPrinter.flush();
        System.out.println("Escuchando en puerto: 8889");
        socketConexion_j2 = socketServidor_j2.accept();
 
        PrintWriter outPrinterJ2 = new PrintWriter(socketConexion_j2.getOutputStream(),true);
	BufferedReader inJ2 = new BufferedReader(new InputStreamReader(socketConexion_j2.getInputStream()));
        
        /***************************************************/
        
        while(!partida_terminada){
             
            if(jugador_turno == 0){
                in = inJ1;
                outPrinter = outPrinterJ2;
            }else{
                in = inJ2;
                outPrinter = outPrinterJ1;
            }
            
            System.out.println("<Turno del jugador>: " + jugador_turno);
            pieza = in.readLine();
            outPrinter.println(pieza);
            outPrinter.flush();
            if(!pieza.equals("SURR")){
                if(!pieza.equals("PASS")){
                    direccion = in.readLine();
                    outPrinter.println(direccion);
                    outPrinter.flush();
                    num_casillas = in.readLine();
                    outPrinter.println(num_casillas);
                    outPrinter.flush();
                    
                    tablero.moverPieza(jugador_turno, pieza, Integer.parseInt(direccion), Integer.parseInt(num_casillas));
                }else{
                     System.out.println("Movimiento Pasado por Jugador " + (jugador_turno+1));
                }
                pieza_ataque = in.readLine();
                outPrinter.println(pieza_ataque);
                outPrinter.flush();

                if(!pieza_ataque.equals("PASS")){
                     tablero.atacarConPieza(jugador_turno, pieza_ataque);
                }else{
                    System.out.println("Ataque Pasado por Jugador " + (jugador_turno+1));
                }

                ganador = tablero.getGanador();

                jugador_turno = (jugador_turno + 1) % 2; // Paso el turno al otro jugador
            }else{
                System.out.println("El Jugador " + (jugador_turno+1) + " se ha rendido");
                ganador = (jugador_turno + 1) % 2;
            }
            
            if (ganador != -1){ // La partida se ha terminado
                partida_terminada = true;
                    
                if (ganador == 0)
                    System.out.println("¡El ganador es el jugador azul!");
                else
                    System.out.println("¡El ganador es el jugador rojo!");
            }
        }
    }
    
    public static void main (String[] args) throws IOException{
         
        Servidor marco = new Servidor("Ejercicio 5 FR");
        
        // Espero a que se pinten el tablero y las piezas antes de empezar el juego
        try{
            Thread.sleep(100);
        }
        catch (InterruptedException e){}
        
        marco.empezarJuego();   
    }
}
