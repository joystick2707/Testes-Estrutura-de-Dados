import java.awt.image.BufferedImage;

public class FloodFillPilha {
    public static void floodFill(BufferedImage image, int x, int y, int novaCor) {
        int corOriginal = image.getRGB(x, y);
        if (corOriginal == novaCor) return; // Evita preenchimento desnecessário

        int largura = image.getWidth();
        int altura = image.getHeight();

        Pilha pilha = new Pilha();
        pilha.insere(new Pixel(x, y));

        while (!pilha.estaVazia()) {
            Pixel p = pilha.remover();
            int px = p.x, py = p.y;

            // Verifica os limites
            if (px < 0 || py < 0 || px >= largura || py >= altura) continue;
            if (image.getRGB(px, py) != corOriginal) continue;

            // Pinta o pixel
            image.setRGB(px, py, novaCor);

            // Debug: Mostra o pixel que está sendo processado
            System.out.println("Processando pixel: (" + px + ", " + py + ")");

            // Adiciona pixels vizinhos apenas se ainda forem da cor original
            if (px + 1 < largura && image.getRGB(px + 1, py) == corOriginal) {
                pilha.insere(new Pixel(px + 1, py));  // Direita
                System.out.println("Adicionando vizinho à direita: (" + (px + 1) + ", " + py + ")");
            }
            if (px - 1 >= 0 && image.getRGB(px - 1, py) == corOriginal) {
                pilha.insere(new Pixel(px - 1, py));  // Esquerda
                System.out.println("Adicionando vizinho à esquerda: (" + (px - 1) + ", " + py + ")");
            }
            if (py + 1 < altura && image.getRGB(px, py + 1) == corOriginal) {
                pilha.insere(new Pixel(px, py + 1));  // Abaixo
                System.out.println("Adicionando vizinho abaixo: (" + px + ", " + (py + 1) + ")");
            }
            if (py - 1 >= 0 && image.getRGB(px, py - 1) == corOriginal) {
                pilha.insere(new Pixel(px, py - 1));  // Acima
                System.out.println("Adicionando vizinho acima: (" + px + ", " + (py - 1) + ")");
            }
        }
    }
}
