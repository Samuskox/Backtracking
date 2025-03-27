package template;

import java.util.ArrayList;
import java.util.List;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.core.utils.CoreUtils;
import br.com.davidbuzatto.jsge.core.utils.DrawingUtils;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;

/**
 * Modelo de projeto básico da JSGE.
 * 
 * JSGE basic project template.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {

    private int[][] labirinto;
    private List<int[][]> listaLabirinto;

    private double tempoParaMudar;
    private double contadorTempo;
    private int pos;

    int posicaoXini;
    int posicaoYini;

    
   
    
    public Main() {
        
        super (
            800,                 // largura                      / width
            450,                 // algura                       / height
            "Window Title",      // título                       / title
            60,                  // quadros por segundo desejado / target FPS
            true,                // suavização                   / antialiasing
            true,               // redimensionável              / resizable
            false,               // tela cheia                   / full screen
            false,               // sem decoração                / undecorated
            false                // sempre no topo               / always on top
        );
        
    }
    
   
    @Override
    public void create() {
        listaLabirinto = new ArrayList<>();

        labirinto = new int[][]{
            {2, 1, 3, 0, 0, 0, 0, 0, 0, 1, 1, 1}, 
            {0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1},
            {0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1},
            {0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
        };

                                    tempoParaMudar = 0.01;
        contadorTempo = 0;
        pos = 0;

        acharPosicao();

        salvarEstadoLabirinto();

        resolverLabirinto(labirinto, 1, 0);

        
    }

    /**
     * Lê a entrada do usuário e atualiza o mundo do jogo.
     * Os métodos de entrada devem ser usados aqui.
     * Atenção: Você NÃO DEVE usar nenhum dos métodos de desenho da engine aqui.
     * 
     * 
     * Reads user input and update game world.
     * Input methods should be used here.
     * Warning: You MUST NOT use any of the engine drawing methods here.
     * 
     * @param delta O tempo passado, em segundos, de um quadro para o outro.
     * Time passed, in seconds, between frames.
     */
    @Override
    public void update( double delta ) {

        
        contadorTempo += delta;

        if (contadorTempo > tempoParaMudar) {
            contadorTempo = 0;
            if (pos < listaLabirinto.size() - 1) {
                //System.out.println("aksd ajsd");
                pos++;
            }
        }
        System.out.println(listaLabirinto.size());


        
    }
    
   
    @Override
    public void draw() {
        
        clearBackground( WHITE );

        desenharlabirinto(listaLabirinto.get(pos), 50, 50, 50);
       

        drawFPS( 20, 20 );
    
    }

    private void desenharlabirinto(int[][] labirinto, int largura, int altura, int espacamento){
        for(int i = 0; i<labirinto.length; i++){
            for(int j = 0; j < labirinto[i].length; j++){

                if(labirinto[i][j] == 0){
                    fillRectangle(300 + i + i*  espacamento,
                    50 + j + j*espacamento,
                     largura,
                      altura,
                       GRAY); // vazio
                } else if(labirinto[i][j] == 1){
                    fillRectangle(300 + i + i*  espacamento,
                    50 + j + j*espacamento,
                     largura,
                      altura,
                       DARKGREEN); //parede
                } else if(labirinto[i][j] == 2){
                    fillRectangle(300 + i + i*  espacamento,
                    50 + j + j*espacamento,
                     largura,
                      altura,
                       GREEN); // entrada
                } else if(labirinto[i][j] == 3){
                    fillRectangle(300 + i + i*  espacamento,
                    50 + j + j*espacamento,
                     largura,
                      altura,
                       BLACK); // saida
                } else if (labirinto[i][j] == 4) {
                    fillRectangle(300 + i * espacamento, 50 + j * espacamento, largura, altura, RED);
                }
            }
        }
    }

    

    public boolean resolverLabirinto(int[][] labirinto, int linha, int coluna) {
        salvarEstadoLabirinto();
        if (!posicaoValida(labirinto, linha, coluna)) {
            //System.out.println(1);
            
            return false;
        }

        //System.out.println(54);

        if (labirinto[linha][coluna] == 3) {
            //System.out.println(2);
            salvarEstadoLabirinto();
            return true; // Achou a saída
        }

        labirinto[linha][coluna] = 4; // Marca o caminho

        if (resolverLabirinto(labirinto, linha + 1, coluna) || // Baixo
            resolverLabirinto(labirinto, linha, coluna + 1) || // Direita
            resolverLabirinto(labirinto, linha - 1, coluna) || // Cima
            resolverLabirinto(labirinto, linha, coluna - 1)) { // Esquerda
                salvarEstadoLabirinto();
                //System.out.println(3);
            return true;
        }

        labirinto[linha][coluna] = 0; // Backtrack
        salvarEstadoLabirinto();
        return false;
    }

    private boolean posicaoValida(int[][] labirinto, int linha, int coluna) {
        return linha >= 0 && linha < labirinto.length && coluna >= 0 && coluna < labirinto[0].length && (labirinto[linha][coluna] == 0 || labirinto[linha][coluna] == 3);
    }

    private void salvarEstadoLabirinto() {
        int[][] novoLabirinto = new int[labirinto.length][labirinto[0].length];
        for (int i = 0; i < labirinto.length; i++) {
            System.arraycopy(labirinto[i], 0, novoLabirinto[i], 0, labirinto[i].length);
        }
        listaLabirinto.add(novoLabirinto);
    }


    public void acharPosicao(){
        for(int i = 0; i<labirinto.length;i++){
            for(int j = 0; j<labirinto[i].length; j++){
                if(labirinto[i][j] == 2){
                    posicaoXini = i;
                    posicaoYini = j;
                }
            }
        }
    }
    


    
    /**
     * Instancia a engine e a inicia.
     * 
     * Instantiates the engine and starts it.
     */
    public static void main( String[] args ) {
        new Main();
    }
    
}
