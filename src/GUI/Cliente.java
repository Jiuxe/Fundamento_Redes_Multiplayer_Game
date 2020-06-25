package GUI;

import java.io.InputStreamReader;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente extends JFrame {

    private Tablero tablero;
    private int jugador_turno; // 0 = azul, 1 = rojo
    String pieza, pieza_ataque;
    String direccion;
    String num_casillas;
    Set<String> piezas_vivas;
    int port = 8887;
    String host = "localhost";
    Socket socketServicio = new Socket (host, port);
    BufferedReader aux = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));

    Cliente(Tablero tablero ) throws IOException{
        
        super("Cliente");
        
        this.tablero = tablero;
        System.out.println("Esperando puerto");
        this.port = Integer.parseInt(aux.readLine());
        System.out.println("Puerto obtenido: " + port);
        socketServicio = new Socket(host, port);  
        this.jugador_turno = port - 8888;
        //aux = null;
        
        this.setSize(400, 400);
        this.setResizable(false);
        
        this.add(tablero); // Cuando añado la vista, se pinta automáticamente*/
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void empezarJuego() throws IOException{
        
        PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(),true);
	BufferedReader in = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
        
        BufferedReader inReader = new BufferedReader( new InputStreamReader(System.in));
  
        boolean partida_terminada = false;
        
        if(jugador_turno == 1){
            System.out.println("Esperando pieza otro jugador");
            pieza = in.readLine();
            if(!pieza.equals("SURR")){
                if(!pieza.equals("PASS")){
                    System.out.println("Esperando direccion de otro jugador");
                    direccion = in.readLine();
                    System.out.println("Esperando numero casilla otro jugador");
                    num_casillas = in.readLine();
                    
                    tablero.moverPieza((jugador_turno+1)%2, pieza, Integer.parseInt(direccion), Integer.parseInt(num_casillas));
                }
                System.out.println("Esperando pieza ataque otro jugador");
                pieza_ataque = in.readLine();

                
                if(!pieza_ataque.equals("PASS")){
                    tablero.atacarConPieza((jugador_turno+1)%2, pieza_ataque);
                }
            }else{
                partida_terminada = true;
                System.out.println("HAS GANADO!!");
            }
        }
        
        while(!partida_terminada){
                    
            try{
                
                piezas_vivas = tablero.getPiezasVivas(jugador_turno); // Veo las piezas que le quedan al jugador del turno

                // Muevo una pieza
                // Esta parte se podría pasar

                System.out.println("<Turno del jugador>: " + jugador_turno);

                do{
                    System.out.print("Introduce el nombre de la pieza a mover: ");        
                    pieza = inReader.readLine();
                }while ( !piezas_vivas.contains(pieza) && !pieza.equals("SURR")
                        && !pieza.equals("PASS")); // Repito el bucle hasta que se seleccione una pieza viva o se elija no mover ninguna pieza

                outPrinter.println(pieza);
                outPrinter.flush();

                if(!pieza.equals("SURR")){
                    
                    if (!pieza.equals("PASS")){ // Si no muevo ninguna pieza, no envío la dirección ni el número de casillas al servidor                       
                        do{
                            System.out.print("Dirección: ");
                            direccion = inReader.readLine(); // pasar 
                        }while(!(direccion.equals("1") || direccion.equals("2") || direccion.equals("3") || direccion.equals("4")));
                      
                        outPrinter.println(direccion);
                        outPrinter.flush();

                        if (pieza.equals("Triangulo")){ // El triángulo se puede mover hasta 4 casillas
                            do{
                                System.out.print("Número de casillas: ");
                                num_casillas = inReader.readLine();
                            }while(Integer.parseInt(num_casillas) <= 0 || Integer.parseInt(num_casillas) > 4);
                        }
                        else{ // El resto de piezas solo se pueden mover hasta dos casillas
                            do{
                                System.out.print("Número de casillas: ");
                                num_casillas = inReader.readLine();
                            }while(Integer.parseInt(num_casillas) <= 0 || Integer.parseInt(num_casillas) > 2);
                        }
                        
                        outPrinter.println(num_casillas);
                        outPrinter.flush();

                        tablero.moverPieza(jugador_turno, pieza, Integer.parseInt(direccion), Integer.parseInt(num_casillas));
                    }
              
                    // Ataco con una pieza
                    // Esta parte se podría pasar

                    do{
                        System.out.print("Introduce el nombre de la pieza para atacar: ");        
                        pieza_ataque = inReader.readLine();
                    }while ( !piezas_vivas.contains(pieza_ataque) && !pieza_ataque.equals("PASS") ); // Repito el bucle hasta que se seleccione una pieza viva

                    outPrinter.println(pieza_ataque);
                    outPrinter.flush();

                    if(!pieza_ataque.equals("PASS")){
                        tablero.atacarConPieza(jugador_turno, pieza_ataque);
                    }


                   if(tablero.getGanador() == -1){

                        System.out.println("Esperando pieza otro jugador");
                        pieza = in.readLine();
                        if(!pieza.equals("SURR")){
                            if(!pieza.equals("PASS")){
                                System.out.println("Esperando direccion de otro jugador");
                                direccion = in.readLine();
                                System.out.println("Esperando numero casilla otro jugador");
                                num_casillas = in.readLine();
                                
                                tablero.moverPieza((jugador_turno+1)%2, pieza, Integer.parseInt(direccion), Integer.parseInt(num_casillas));
                            }
                            System.out.println("Esperando pieza ataque otro jugador");
                            pieza_ataque = in.readLine();

                            if(!pieza_ataque.equals("PASS")){
                                tablero.atacarConPieza((jugador_turno+1)%2, pieza_ataque);
                            }

                            if(tablero.getGanador() != -1){
                                partida_terminada = true;
                                System.out.println("HAS PERDIDO...");
                            }
                        }else{
                            partida_terminada = true;
                            System.out.println("HAS GANADO!!");
                        }
                   }else{
                    partida_terminada = true;
                    System.out.println("HAS GANADO!!");
                   }
                }else{
                    partida_terminada = true;
                    System.out.println("HAS PERDIDO...");
                }
            }
            catch (IOException e){}
        }
        
         socketServicio.close();
    }
    
    public static void main (String[] args) throws IOException{

        System.out.println("Esperando mensaje servidor");
        Cliente jugador = new Cliente(new Tablero(10, 10));
        System.out.println("Empezando juego");
        jugador.empezarJuego();
       
    }
}    
