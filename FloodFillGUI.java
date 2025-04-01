import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class FloodFillGUI extends JFrame {
    private BufferedImage image;
    private JLabel imageLabel;

    public FloodFillGUI(String imagePath, int startX, int startY) {
        setTitle("Flood Fill - Coordenado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        image = ImageLoader.loadImage(imagePath);
        if (image == null) {
            System.out.println("Erro ao carregar a imagem.");
            System.exit(1);
        }

        imageLabel = new JLabel(new ImageIcon(image));
        add(imageLabel, BorderLayout.CENTER);

        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        iniciarFloodFill(startX, startY, Color.BLUE.getRGB());
        ImageSaver.saveImage(image, "output.png");
    }

    private void iniciarFloodFill(int x, int y, int novaCor) {
        new Thread(() -> {
            if (x >= 0 && y >= 0 && x < image.getWidth() && y < image.getHeight()) {
                floodFill(image, x, y, novaCor, 50);  // Passe apenas 'image', que é um BufferedImage
                SwingUtilities.invokeLater(() -> imageLabel.repaint());  // Força o repintar após a modificação
            } else {
                System.out.println("Coordenadas fora dos limites da imagem.");
            }
        }).start();
    }

    public static void floodFill(BufferedImage image, int x, int y, int novaCor, int tolerancia) {
        int corOriginal = image.getRGB(x, y);

        // Não impede o preenchimento se a cor for a mesma, pois a ideia é explorar vizinhos
        if (corOriginal == novaCor) return; // Evita preenchimento desnecessário de áreas que já estão com a nova cor

        int largura = image.getWidth();
        int altura = image.getHeight();

        Pilha pilha = new Pilha();
        pilha.insere(new Pixel(x, y));

        Set<Pixel> visitados = new HashSet<>(); // Conjunto de pixels já visitados

        while (!pilha.estaVazia()) {
            Pixel p = pilha.remover();
            int px = p.x, py = p.y;

            // Verifica se o pixel está dentro dos limites da imagem
            if (px < 0 || py < 0 || px >= largura || py >= altura) continue;

            // Verifica se o pixel foi visitado
            if (visitados.contains(p)) continue;

            // Marca o pixel como visitado
            visitados.add(p);

            // Verifica se a cor do pixel está dentro da tolerância da cor original
            if (!corDentroDaTolerancia(image.getRGB(px, py), corOriginal, tolerancia)) continue;

            // Preenche o pixel com a nova cor
            image.setRGB(px, py, novaCor);

            // Adiciona os vizinhos para serem processados
            pilha.insere(new Pixel(px + 1, py));  // Direita
            pilha.insere(new Pixel(px - 1, py));  // Esquerda
            pilha.insere(new Pixel(px, py + 1));  // Abaixo
            pilha.insere(new Pixel(px, py - 1));  // Acima
        }
    }

    // Método para verificar se o pixel é transparente
    private static boolean isTransparente(int cor) {
        return ((cor >> 24) & 0xFF) == 0; // Verifica se o canal alfa (transparência) é 0
    }

    private static boolean corDentroDaTolerancia(int cor, int corOriginal, int tolerancia) {
        int r1 = (cor >> 16) & 0xFF;
        int g1 = (cor >> 8) & 0xFF;
        int b1 = cor & 0xFF;

        int r2 = (corOriginal >> 16) & 0xFF;
        int g2 = (corOriginal >> 8) & 0xFF;
        int b2 = corOriginal & 0xFF;

        // Verifique se a diferença entre as cores está dentro da tolerância
        return Math.abs(r1 - r2) <= tolerancia && Math.abs(g1 - g2) <= tolerancia && Math.abs(b1 - b2) <= tolerancia;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a coordenada X para iniciar o Flood Fill: ");
        int startX = scanner.nextInt();
        System.out.print("Digite a coordenada Y para iniciar o Flood Fill: ");
        int startY = scanner.nextInt();

        SwingUtilities.invokeLater(() -> new FloodFillGUI("C:/Users/Usuario/Downloads/Trabalho-Estrutura-de-Dados-main/Trabalho-Estrutura-de-Dados-main/FloodFill Pilha/images/cara.png", startX, startY));
    }
}
