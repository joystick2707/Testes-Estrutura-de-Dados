class Pilha {
    private Node topo;

    public Pilha() {
        this.topo = null;
    }

    public void insere(Pixel valor) {
        Node novoNo = new Node(valor);
        novoNo.proximo = topo;
        topo = novoNo;
    }

    public Pixel remover() {
        if (topo == null) {
            return null;
        }
        Pixel valorRemovido = topo.valor;
        topo = topo.proximo;
        return valorRemovido;
    }


    public boolean estaVazia() {
        return topo == null;
    }
}
