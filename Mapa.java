import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapa {
    private List<String> mapa;
    private Map<Character, ElementoMapa> elementos;
    private int posX = 50; // Posição inicial X do personagem
    private int posY = 50; // Posição inicial Y do personagem
    private final int TAMANHO_CELULA = 10; // Tamanho de cada célula do mapa
    private boolean[][] areaRevelada; // Rastreia quais partes do mapa foram reveladas
    private final Color brickColor = new Color(153, 76, 0); // Cor marrom para tijolos
    private final Color vegetationColor = new Color(34, 139, 34); // Cor verde para vegetação
    private final Color goldColor = new Color(181, 148, 16); // Cor dourada para moedas
    private final int RAIO_VISAO = 5; // Raio de visão do personagem

    public Mapa(String arquivoMapa) {
        mapa = new ArrayList<>();
        elementos = new HashMap<>();
        registraElementos();
        carregaMapa(arquivoMapa);
        areaRevelada = new boolean[mapa.size()+1000][mapa.get(0).length()+1000];
        atualizaCelulasReveladas();
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getTamanhoCelula() {
        return TAMANHO_CELULA;
    }

    public int getNumLinhas() {
        return mapa.size();
    }

    public int getNumColunas() {
        return mapa.get(0).length();
    }

    public ElementoMapa getElemento(Character id) {
        return elementos.get(id);
    }

    public ElementoMapa getElemento(int x, int y) {
        Character id = mapa.get(y).charAt(x);
        return elementos.get(id);
    }


    public boolean setElemento(Character id, int x, int y) {
        if (x < 0 || x >= mapa.get(0).length() || y < 0 || y >= mapa.size()) {
            System.out.println("Fora do mapa");
            return false;
        }

        StringBuilder sb = new StringBuilder(mapa.get(y));
        System.out.println("Antes: " + mapa.get(y));
        sb.setCharAt(x, id);
        mapa.set(y, sb.toString());
        System.out.println("Depois: " + mapa.get(y));
        return true;
    }

    public boolean setElemento(ElementoMapa elemento, int x, int y) {
        // Econtra chave do elemento no set
        for (Map.Entry<Character, ElementoMapa> entry : elementos.entrySet()) {
            if (entry.getValue().equals(elemento)) {
                return setElemento(entry.getKey(), x, y);
            }
        }
        System.out.println("Elemento não encontrado");
        return false;
    }

    public boolean apagaElemento(int x, int y) {
        return setElemento(' ', x, y);
    }

    public boolean moveElemento(int xOrigem, int yOrigem, int xDestino, int yDestino) {
        if (xOrigem < 0 || xOrigem >= mapa.get(0).length() || yOrigem < 0 || yOrigem >= mapa.size() ||
            xDestino < 0 || xDestino >= mapa.get(0).length() || yDestino < 0 || yDestino >= mapa.size()) {
            System.out.println("Fora do mapa");
            return false;
        }

        Character id = mapa.get(yOrigem).charAt(xOrigem);
        if (id == ' ') {
            System.out.println("Não há elemento na origem");
            return false;
        }

        if (getElemento(xDestino, yDestino) != null) {
            System.out.println("Já existe um elemento no destino");
            return false;
        }

        if (!setElemento(id, xDestino, yDestino)) {
            System.out.println("Não foi possível mover o elemento");
            return false;
        }

        if (!apagaElemento(xOrigem, yOrigem)) {
            System.out.println("Não foi possível apagar o elemento");
            return false;
        }

        return true;
    }

    public boolean estaRevelado(int x, int y) {
        return areaRevelada[y][x];
    }

    // Move conforme enum Direcao
    public boolean move(Direcao direcao) {
        int dx = 0, dy = 0;

        switch (direcao) {
            case CIMA:
                dy = -TAMANHO_CELULA;
                break;
            case BAIXO:
                dy = TAMANHO_CELULA;
                break;
            case ESQUERDA:
                dx = -TAMANHO_CELULA;
                break;
            case DIREITA:
                dx = TAMANHO_CELULA;
                break;
            default:
                return false;
        }

        if (!podeMover(posX + dx, posY + dy)) {
            System.out.println("Não pode mover");
            return false;
        }

        posX += dx;
        posY += dy;

        // Atualiza as células reveladas
        atualizaCelulasReveladas();
        return true;
    }

    // Verifica se o personagem pode se mover para a próxima posição
    private boolean podeMover(int nextX, int nextY) {
        int mapX = nextX / TAMANHO_CELULA;
        int mapY = nextY / TAMANHO_CELULA - 1;

        if (mapa == null)
            return false;

        if (mapX >= 0 && mapX < mapa.get(0).length() && mapY >= 1 && mapY <= mapa.size()) {
            char id;

            try {
               id = mapa.get(mapY).charAt(mapX);
            } catch (StringIndexOutOfBoundsException e) {
                return false;
            }

            if (id == ' ')
                return true;

            ElementoMapa elemento = elementos.get(id);
            if (elemento != null) {
                //System.out.println("Elemento: " + elemento.getSimbolo() + " " + elemento.getCor());
                return elemento.podeSerAtravessado();
            }
        }

        return false;
    }

    public String interage() {
        //TODO: Implementar
        return "Interage";
    }

    public String ataca() {
        //TODO: Implementar
        return "Ataca";
    }

    private void carregaMapa(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                mapa.add(line);
                // Se character 'P' está contido na linha atual, então define a posição inicial do personagem
                if (line.contains("P")) {
                    posX = line.indexOf('P') * TAMANHO_CELULA;
                    posY = mapa.size() * TAMANHO_CELULA;
                    // Remove o personagem da linha para evitar que seja desenhado
                    mapa.set(mapa.size() - 1, line.replace('P', ' '));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar as células reveladas
    private void atualizaCelulasReveladas() {
        if (mapa == null)
            return;
        for (int i = Math.max(0, posY / TAMANHO_CELULA - RAIO_VISAO); i < Math.min(mapa.size(), posY / TAMANHO_CELULA + RAIO_VISAO + 1); i++) {
            for (int j = Math.max(0, posX / TAMANHO_CELULA - RAIO_VISAO); j < Math.min(mapa.get(i).length(), posX / TAMANHO_CELULA + RAIO_VISAO + 1); j++) {
                areaRevelada[i][j] = true;
            }
        }
    }

    // Registra os elementos do mapa
    private void registraElementos() {
        // Parede
        elementos.put('#', new Parede('▣', brickColor));
        // Vegetação
        elementos.put('V', new Vegetacao('♣', vegetationColor));
        // Inimigo
        elementos.put('I', new Inimigo('☠', Color.RED));
        // Moeda
        elementos.put('M', new Moeda('♦', goldColor));
    }
}
