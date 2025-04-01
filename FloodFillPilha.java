import java.awt.image.BufferedImage;

public class FloodFillPilha {
    public static void floodFill(BufferedImage image, int x, int y, int novaCor) {
        int corOriginal = image.getRGB(x, y);
        if (corOriginal == novaCor) return; // Evita preenchimento desnecessário

        int largura = image.getWidth();
        int altura = image.getHeight();
        boolean[][] visitado = new boolean[largura][altura]; // Novo controle de pixels visitados

        Pilha pilha = new Pilha();
        pilha.insere(new Pixel(x, y));
        visitado[x][y] = true; // Marca o pixel inicial como visitado

        while (!pilha.estaVazia()) {
            Pixel p = pilha.remover();
            if (p == null) continue;
            int px = p.x, py = p.y;

            if (px < 0 || py < 0 || px >= largura || py >= altura) continue;
            if (image.getRGB(px, py) != corOriginal) continue;

            image.setRGB(px, py, novaCor);

            // Adiciona apenas pixels não visitados
            if (px + 1 < largura && !visitado[px + 1][py]) {
                pilha.insere(new Pixel(px + 1, py));
                visitado[px + 1][py] = true;
            }
            if (px - 1 >= 0 && !visitado[px - 1][py]) {
                pilha.insere(new Pixel(px - 1, py));
                visitado[px - 1][py] = true;
            }
            if (py + 1 < altura && !visitado[px][py + 1]) {
                pilha.insere(new Pixel(px, py + 1));
                visitado[px][py + 1] = true;
            }
            if (py - 1 >= 0 && !visitado[px][py - 1]) {
                pilha.insere(new Pixel(px, py - 1));
                visitado[px][py - 1] = true;
            }

            // Debug para verificar o comportamento
            System.out.println("Processando pixel: (" + px + ", " + py + ")");
        }
    }
}
