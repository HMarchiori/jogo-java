import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Random;

public class Jogo extends JFrame implements KeyListener {
    private JLabel statusBar;
    private Mapa mapa;
    private int tamanhoCelula = 20;
    private int width = 1600;
    private int height = 1200;
    private int numMoedas = 0; // Contador de moedas
    private int vidas = 3; // Número de vidas do jogador
    private final Color fogColor = new Color(192, 192, 192, 150); // Cor cinza claro com transparência para nevoa
    private final Color characterColor = Color.BLACK; // Cor preta para o personagem
    private int tamanhoFonte;
    private JPanel mapPanel;
    private InterfaceServidor servidor;
    private String clientId;
    private int numeroSequente = 0;

    public Jogo(String arquivoMapa) {
        setTitle("Jogo de Aventura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Cria o mapa do jogo
        mapa = new Mapa(arquivoMapa, tamanhoCelula);

        // Tamanho inicial da janela
        setSize(width, height);

        // Painel para desenhar o mapa do jogo
        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Define a fonte para garantir que o caractere caiba na celula
                tamanhoFonte = mapa.getTamanhoCelula() / 10 * 12;
                Font font = new Font("Roboto", Font.BOLD, tamanhoFonte);
                g.setFont(font);
                desenhaMapa(g);
                desenhaPersonagem(g);
            }
        };
        mapPanel.setPreferredSize(new Dimension(width, height));

        // Adiciona um listener para redimensionamento da janela
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustaTamanhoCelula();
            }
        });

        // Barra de status
        statusBar = new JLabel(getStatusBarText());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);

        // Painel para botões e barra de status
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(statusBar);

        // Adiciona os paineis ao JFrame
        add(mapPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // Ajusta o tamanho do JFrame para acomodar todos os componentes
        pack();

        // Adiciona o listener para eventos de teclado
        addKeyListener(this);

        // Registra os elementos do mapa
        // Parede
        mapa.registraElemento('#', new Parede('▣', Mapa.brickColor));
        // Vegetação
        mapa.registraElemento('V', new Vegetacao('♣', Mapa.vegetationColor));
        // Inimigo
        mapa.registraElemento('I', new Inimigo('☠', Color.RED, this));
        // Moeda
        mapa.registraElemento('M', new Moeda('♦', Mapa.goldColor));

        ajustaTamanhoCelula();

        // Distribui moedas no mapa de forma aleatória de acordo com a semente
        distibuiMoedas(100, 1L);
    }

    private String getStatusBarText() {
        return "Posição: (" + mapa.getPosX() + "," + mapa.getPosY() + ") | Moedas: " + numMoedas + " | Vidas: " + vidas;
    }

    private void ajustaTamanhoCelula() {
        int mapPanelWidth = mapPanel.getWidth();
        int numColunas = mapa.getNumColunas();
        int numLinhas = mapa.getNumLinhas();

        // Calcula o novo tamanho da célula baseado na largura disponível
        tamanhoCelula = mapPanelWidth / numColunas;
        mapa.setTamanhoCelula(tamanhoCelula);

        // Calcula a altura baseada na largura e no aspecto
        int desiredHeight = (mapPanelWidth * numLinhas) / numColunas;

        // Ajusta a altura do painel
        mapPanel.setPreferredSize(new Dimension(mapPanelWidth, desiredHeight));

        // Recalcula o tamanho da fonte
        tamanhoFonte = (tamanhoCelula * 12) / 10;
        mapPanel.setFont(new Font("Roboto", Font.BOLD, tamanhoFonte));

        // Revalida o painel e redimensiona a janela para acomodar todos os componentes
        mapPanel.revalidate();
        pack();

        // Redesenha o mapa
        mapPanel.repaint();

        System.out.println("Largura: " + mapPanelWidth + " Altura: " + desiredHeight + " Colunas: " + numColunas + " Linhas: " + numLinhas + " Tamanho celula: " + tamanhoCelula + " Tamanho fonte: " + tamanhoFonte);
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void distibuiMoedas(int numMoedas, long seed) {
        Random random = new Random(seed);
        for (int i = 0; i < numMoedas; i++) {
            int x = random.nextInt(mapa.getNumColunas());
            int y = random.nextInt(mapa.getNumLinhas());
            if (x >= 0 && x < mapa.getNumColunas() && y >= 0 && y < mapa.getNumLinhas() && mapa.getElemento(x, y) == null) {
                mapa.setElemento('M', x, y);
            }
        }
    }

    public void movimentaInimigo() {
        Inimigo inimigo = (Inimigo) mapa.getElemento('I');
        if (inimigo != null) {
            inimigo.run();
        }
    }

    public void move(Direcao direcao) {
        if (mapa == null)
            return;

        // Modifica posição do personagem no mapa
        if (!mapa.move(direcao))
            return;

        // Verifica se o jogador pegou uma moeda
        int mapX = mapa.getPosX() / mapa.getTamanhoCelula();
        int mapY = mapa.getPosY() / mapa.getTamanhoCelula() - 1;
        ElementoMapa elemento = mapa.getElemento(mapX, mapY);
        if (elemento instanceof Moeda) {
            numMoedas++;
            mapa.apagaElemento(mapX, mapY);
        }

        // Atualiza a barra de status
        if (statusBar != null)
            statusBar.setText(getStatusBarText());

        // Incrementa o número sequente e envia comando ao servidor
        numeroSequente++;
        try {
            servidor.enviarComando(clientId, numeroSequente, mapX, mapY);
        } catch (RemoteException e) {
            System.err.println("Erro ao enviar comando: " + e.getMessage());
            e.printStackTrace();
        }

        // Redesenha o painel
        repaint();
    }

    public void interage() {
        if (mapa == null)
            return;

        // Cria um diálogo para exibir a mensagem de interação
        String mensagem = mapa.interage();
        if (mensagem != null) {
            JOptionPane.showMessageDialog(this, mensagem);
        }
    }

    public void ataca() {
        if (mapa == null)
            return;

        String status = mapa.ataca();

        // Atualiza a barra de status
        if (statusBar != null)
            statusBar.setText(status);
    }

    public void verificaProximidade(Inimigo inimigo) {
        int inimigoX = inimigo.getX() * mapa.getTamanhoCelula();
        int inimigoY = inimigo.getY() * mapa.getTamanhoCelula();
        int personagemX = mapa.getPosX();
        int personagemY = mapa.getPosY();

        int distanciaX = Math.abs(inimigoX - personagemX);
        int distanciaY = Math.abs(inimigoY - personagemY);

        if (distanciaX <= mapa.getTamanhoCelula() && distanciaY <= mapa.getTamanhoCelula()) {
            vidas--;
            statusBar.setText(getStatusBarText());
            if (vidas <= 0) {
                JOptionPane.showMessageDialog(this, "Game Over!");
                vidas = 1;
                return;
            }
        }
    }

    private void desenhaMapa(Graphics g) {
        int tamanhoCelula = mapa.getTamanhoCelula();
        for (int i = 0; i < mapa.getNumLinhas(); i++) {
            for (int j = 0; j < mapa.getNumColunas(); j++) {
                int posX = j * tamanhoCelula;
                int posY = (i + 1) * tamanhoCelula;

                if (mapa.estaRevelado(j, i)) {
                    ElementoMapa elemento = mapa.getElemento(j, i);
                    if (elemento != null) {
                        g.setColor(elemento.getCor());
                        g.drawString(elemento.getSimbolo().toString(), posX, posY);
                    }
                } else {
                    // Pinta a área não revelada
                    g.setColor(fogColor);
                    g.fillRect(j * tamanhoCelula, i * tamanhoCelula, tamanhoCelula, tamanhoCelula);
                }
            }
        }
    }

    private void desenhaPersonagem(Graphics g) {
        g.setColor(characterColor);
        g.drawString("☺", mapa.getPosX(), mapa.getPosY());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não necessário
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W: // Tecla 'W' para cima
                move(Direcao.CIMA);
                break;
            case KeyEvent.VK_S: // Tecla 'S' para baixo
                move(Direcao.BAIXO);
                break;
            case KeyEvent.VK_A: // Tecla 'A' para esquerda
                move(Direcao.ESQUERDA);
                break;
            case KeyEvent.VK_D: // Tecla 'D' para direita
                move(Direcao.DIREITA);
                break;
            case KeyEvent.VK_E: // Tecla 'E' para interagir
                interage();
                break;
            case KeyEvent.VK_J: // Tecla 'J' para ação secundária
                ataca();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Não necessário
    }
}