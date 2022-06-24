import javax.swing.*;  // javax.swing: proporciona una serie de clases e interfaces que amplían la funcionalidad del anterior.//
import java.awt.*;  //java.awt: proporciona una serie de clases e interfaces para realizar programas que se ejecuten en un entorno gráfico (AWT viene del inglés AbstractWindow Toolkit).//
import java.awt.event.*;  //java.awt.event: Nos permite interactuar con los botones de la pantalla grafica.
import java.text.*;  //Proporciona clases e interfaces para manejar texto, fechas, números y mensajes de forma independiente a los lenguajes naturales.

public class memorama extends JFrame{  //JFrame crea una ventana gráfica
  private final int FILAS = 4;
  private final int COLUMNAS = 4;
  private JLabel lblJugador;  // JLabel: son textos que podemos colocar en un Frame.//
  private JLabel[][] casillas;
  private JLabel lblCronometro;
  private JLabel lblIntentos;
  private JLabel lblCarta1;
  private JLabel lblCarta2;
  private String[] imagenes = {"1.jpg","2.jpg","3.jpg","4.jpg","5.jpg","6.jpg","7.jpg","8.jpg"};  //String: Inicializa un Stringobjeto recién creado para que represente una secuencia de caracteres vacía.
  private int hora, minuto, segundo;
  private Timer tiempo;  //Timer:  inicializa un reloj tanto el retardo inicial como el retardo entre eventos en delaymilisegundos.
  private boolean esJuegoActivo;  //boolean: almacena unicamente dos valores: verdadero o falso.//
  private int aciertos=0;
  private boolean otroJuego=false;
    public memorama(){
      this.setLayout (new BorderLayout());  //BorderLayout: Construye un nuevo diseño de borde sin espacios entre componentes.//
      ((JPanel) getContentPane()).setBorder (BorderFactory.createEmptyBorder(20,20,20,20)); //createEmptyBorder: Crea un borde vacío que no ocupa espacio. setBorder: Se utiliza para poner un borde alrededor de a JComponent.//
      ((JPanel) getContentPane()).setBackground(Color.WHITE); //getContentPane: Sirve Para añadir componentes a un JFrame.  setBackground: Sirve para alterar el fondo de un elemento.//   
      JPanel paneImagenes = new JPanel (new GridLayout (FILAS, COLUMNAS,10,10)); //GridLayout: Sirve para colocar componentes en forma de matriz, haciendo que todos ocupen el mismo tamaño.//
      paneImagenes.setOpaque(false); //setOpaque(false): Se utiliza para establecer un fondo transparente.//
      casillas = new JLabel[FILAS][COLUMNAS];
      for (int fila = 0; fila < FILAS; fila++)
        for (int columna = 0; columna < COLUMNAS; columna++){
          if (!otroJuego)
          casillas[fila][columna] = new JLabel(new ImageIcon ("imagenes/0.jpg"));  //ImageIcon: implementa el icono , serializable , accesible.//
          casillas[fila][columna].setToolTipText ("<html><b>Casilla ["+fila+"]["+columna+"]</b>" + "<br><i>Doble Click para voltear la casilla</i></html>"); //setToolTipText: Se utiliza para crear una información sobre herramientas.//  
          casillas[fila][columna].setCursor (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //setCursor : Es una clase para encapsular la representación de mapa de bits del cursor del mouse. //getPredefinedCursor: Devuelve un objeto de cursor con el tipo predefinido especificado. //Cursor.HAND_CURSOR: El tipo de cursor de mano.                                                                     
            casillas[fila][columna].addMouseListener(new MouseAdapter(){  //MouseListener: La interfaz de escucha para recibir eventos de mouse "interesantes" (presionar, soltar, hacer clic, ingresar y salir) en un componente.  //MouseAdapter:  Una clase de adaptador abstracta para recibir eventos de mouse.                                                     
                public void mouseClicked (MouseEvent e){   //mouseClicked: Se invoca cuando se hace clic en el botón del mouse (se presiona y se suelta) en un componente.
                 if (e.getClickCount()==2)  //ClickCount: Es un evento que indica que se produjo una acción del mouse en un componente.
                    if (esJuegoActivo){
                      JLabel casilla = (JLabel) e.getSource(); //getSource: devuelve la fuente del evento.
                      casilla.setIcon (new ImageIcon("imagenes/"+casilla.getName())); //getName: Es útil para obtener el nombre de los métodos, como una cadena.  //setIcon: método para especificar cuántos píxeles deben aparecer en la imagen.                                             
                      if (lblCarta1==null)
                      lblCarta1 = (JLabel) e.getSource();
                        else if (lblCarta2==null){
                        lblCarta2 = (JLabel) e.getSource();
                          comparar();
                        }
                    }
                }
            });
          paneImagenes.add(casillas[fila][columna]); //add: Es un método que nos permite añadir elementos a un Vector.//
        }
      JPanel paneNorte = new JPanel(); //paneNorte: La restricción de diseño norte (parte superior del contenedor).// JPanel: es un contenedor ligero genérico.//
      paneNorte.setLayout (new BoxLayout (paneNorte,BoxLayout.X_AXIS));  //BoxLayout.X_AXIS: permite poner los componentes de forma horizontal.//
      paneNorte.setOpaque(false); //setOpaque(false): Se utiliza para establecer un fondo transparente.//
      lblIntentos = new JLabel ("0"); //JLabel: son textos que podemos colocar en un Frame.//
      lblCronometro = new JLabel ("00:00:00");
      lblJugador = new JLabel (" SIN JUGADOR ");
      tiempo = new Timer (1000,null);
        tiempo.addActionListener (new ActionListener(){ //ActionListener: Es la interfaz de escucha para recibir eventos de acción.
            public void actionPerformed (ActionEvent e){ //actionPerformed: Se invoca cuando se produce una acción.
              segundo++;
                if (segundo==60){
                  segundo=0;
                  minuto++;
                }
                if (minuto==60){
                  segundo=0;
                  minuto=0;
                  hora++;
                }
              lblCronometro.setText(String.format("%02d",hora) + ":" //setText: Copia una cadena de texto al DataObject utilizando un formato especificado.
              + String.format("%02d",minuto) + ":" + String.format("%02d",segundo)); //String.format: Convierte el valor de los objetos en cadenas según los formatos especificados y los inserta en otra cadena.             
            }
        });
        this.addKeyListener (new KeyAdapter(){ //KeyListener: La interfaz de escucha para recibir eventos de teclado (pulsaciones de teclas).  //KeyAdapter: Es una clase de adaptador abstracto para recibir eventos de teclado.                                           
            public void keyPressed (KeyEvent e){ //keyPressed: Se invoca cuando se pulsa una tecla.
              if (e.getKeyCode() == KeyEvent.VK_F5) //getKeyCode: devuelven un código de tecla virtual.  //KeyEvent.VK_F5: Realiza un evento al oprimir la tecla F5.                    
                    if (!esJuegoActivo){
                      int r = JOptionPane.showConfirmDialog (null," DESEAS INICIAR UNA PARTIDA ?"," MEMORAMA ",JOptionPane.YES_NO_OPTION);  //showConfirmDialog: Hace una pregunta de confirmación, como sí / no / cancelar.
                        if (r==JOptionPane.YES_OPTION){
                          String nombre = JOptionPane.showInputDialog (null," INGRESA TU NOMBRE "); // showInputDialog: Solicitar alguna entrada.
                          lblJugador.setText(" JUGADOR : " + nombre);
                          esJuegoActivo = true;
                          hora = minuto = segundo =0 ;
                          tiempo.start(); //Inicia el Timer, lo que hace que comience a enviar eventos de acción a sus oyentes.
                          repartir();
                          repartir();
                        }
                    }
            }
        });
      paneNorte.add (new JLabel ("No. DE INTENTOS : "));
      paneNorte.add (lblIntentos);
      paneNorte.add (Box.createHorizontalGlue()); //Box.createHorizontalGlue: Se encarga de crear sus componentes uno encima del otro o los coloca en una fila.//
      paneNorte.add (lblJugador);
      paneNorte.add (Box.createHorizontalGlue());
      paneNorte.add (new JLabel (" TIEMPO : "));
      paneNorte.add (lblCronometro);
      this.add (paneNorte,BorderLayout.NORTH);  //BorderLayout.NORTH: cambia el tamaño de sus componentes para que quepan en cinco regiones: norte, sur, este, oeste y centro.//
      this.add (paneImagenes, BorderLayout.CENTER);  // BorderLayout.CENTER: cambia el tamaño de sus componentes para que quepan en cinco regiones: norte, sur, este, oeste y centro.//
      this.setTitle (" MEMORAMA ITSAL"); //setTitle: Establece el título que se utilizará para el estado especificado.//
      this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); //setDefaultCloseOperation: se utiliza para especificar una de varias opciones para el botón de cierre. JFrame.EXIT_ON_CLOSE: Terminar los subprocesos.//
      this.setResizable (false); //setResizable: Esta funcion evita que el usuario cambie o no el tamaño del marco.
      pack(); // pack: Se encarga de ajustar el tamaño del marco para que todo su contenido sea igual o superior a sus tamaños preferidos. 
      this.setLocationRelativeTo (null); //setLocationRelativeTo: coloca la ventana en una posición relativa a un componente que le pasemos como parámetro.//
      this.setVisible(true); //setVisible: Se encarga de darle visibilidad de una clase. Si desde un objeto B quieres hacer visible o invisible otro objeto A, y A ha creado a B, B debe tener una referencia a A para llamar al método .setVisible(true).//
    }
    private void repartir (){
      int fila=0, columna =0;
        for (int i=0; i<imagenes.length; i++){
          fila = (int) (Math.random() * FILAS); //Math.random: Método que nos devuelve un valor aleatorio entre 0.0 y 1.0
          columna = (int) (Math.random() * COLUMNAS);
          if (casillas[fila][columna].getName()==null)
          casillas[fila][columna].setName(imagenes[i]);
          else
          i--;
        }
    }
    private void comparar(){
        if (!lblCarta1.getName().equals(lblCarta2.getName())){ // equals: Compara esta cadena con el objeto especificado.
          Timer pausa = new Timer (1000,null);
            pausa.addActionListener (new ActionListener(){
                public void actionPerformed(ActionEvent e){
                  lblCarta1.setIcon(new ImageIcon ("imagenes/0.jpg"));
                  lblCarta2.setIcon(new ImageIcon ("imagenes/0.jpg"));
                  int intentos = Integer.parseInt(lblIntentos.getText())+1;
                  lblIntentos.setText(String.valueOf(intentos)); //valueOf: Devuelve la representación de cadena del booleanargumento.
                  lblCarta1=null;
                  lblCarta2=null;
                }
            });
          pausa.setRepeats(false); // setRepeats: Si desea que el temporizador se dispare solo la primera vez y luego se detenga
          pausa.start();
        }else{ //Si las cartas son iguales
          aciertos++;
          lblCarta1=null;
          lblCarta2=null;
            if (aciertos==imagenes.length){  //length: Este método devuelve la longitud de cualquier cadena que sea igual al número de caracteres Unicode de 16 bits en la cadena.
              JOptionPane.showMessageDialog (null," FELICIDADES, HAS GANADO !!");
              for (int fila = 0; fila < FILAS; fila++)
              for (int columna = 0; columna < COLUMNAS; columna++)
              casillas[fila][columna].setIcon(new ImageIcon ("imagenes/0.jpg"));
              lblIntentos.setText("0");
              lblJugador.setText(" SIN JUGADOR ");
              lblCronometro.setText("00:00:00");
              tiempo.stop(); // Detiene el Timer, lo que hace que deje de enviar eventos de acción a sus oyentes.
              esJuegoActivo=false;
              otroJuego=true;
            }
        }
    }
    public static void main (String args[]){
      new memorama();
    }
}