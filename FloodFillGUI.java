import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FloodFillGUI extends JFrame {
    private BufferedImage image;
    private JLabel imageLabel;
    private boolean[][] visitado; // Matriz para marcar pixels já processados

    public FloodFillGUI(String imagePath, int startX, int startY) {
        setTitle("Flood Fill - Automático");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Carrega a imagem
        image = ImageLoader.loadImage(imagePath);
        if (image == null) {
            System.out.println("Erro ao carregar a imagem.");
            System.exit(1);
        }

        // Inicializa a matriz de controle
        visitado = new boolean[image.getWidth()][image.getHeight()];

        // Cria JLabel com a imagem
        imageLabel = new JLabel(new ImageIcon(image));
        add(imageLabel, BorderLayout.CENTER);

        setSize(image.getWidth(), image.getHeight());
        setLocationRelativeTo(null);
        setSize(500, 500);
        setVisible(true);

        // Inicia o Flood Fill no ponto inicial
        iniciarFloodFill(startX, startY, Color.BLUE.getRGB());

        // Após processar o ponto inicial, verificar o restante da imagem
        verificarImagem(Color.BLUE.getRGB());

        // Salva a imagem
        ImageSaver.saveImage(image, "output.png");
    }

    private void iniciarFloodFill(int x, int y, int novaCor) {
        if (dentroDosLimites(x, y) && !visitado[x][y] && ehPreto(image.getRGB(x, y))) {
            // Executa o flood fill no ponto inicial
            FloodFillPilha.floodFill(image, x, y, novaCor);
            visitado[x][y] = true; // Marca como visitado
            SwingUtilities.invokeLater(() -> imageLabel.repaint());
        }
    }

    private void verificarImagem(int novaCor) {
        new Thread(() -> {
            final int delay = 1;

            // Criando uma pilha para armazenar os pontos pretos encontrados
            Pilha pilhaPixels = new Pilha();

            // Percorre a imagem e adiciona pixels pretos à pilha
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (ehPreto(image.getRGB(x, y)) && !visitado[x][y]) {
                        pilhaPixels.insere(new Pixel(x, y));
                        visitado[x][y] = true;
                    }
                }
            }

            // Processa cada ponto da pilha
            while (!pilhaPixels.estaVazia()) {
                Pixel p = pilhaPixels.remover();
                FloodFillPilha.floodFill(image, p.x, p.y, novaCor);

                // Atualiza a interface gráfica
                SwingUtilities.invokeLater(() -> imageLabel.repaint());

                // Adiciona um pequeno delay para mostrar a pintura gradualmente
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean ehPreto(int cor) {
        int r = (cor >> 16) & 0xFF;
        int g = (cor >> 8) & 0xFF;
        int b = cor & 0xFF;
        return r < 100 && g < 100 && b < 100; // Detecta tons escuros
    }

    private boolean dentroDosLimites(int x, int y) {
        return x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight();
    }

    public static void main(String[] args) {
        int startX = 60; // Coordenada X inicial
        int startY = 60; // Coordenada Y inicial
        SwingUtilities.invokeLater(() -> new FloodFillGUI("/home/bryan/IdeaProjects/TrabalhoEstrutura/src/imagens/bolaVolei.png", startX, startY));
    }
}
