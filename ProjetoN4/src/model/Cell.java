package model;


public class Cell {
    //identificador
    private int id;
    //posição x e y inicial do texto
    private int x, y;
    //valores minímos e máximos permitidos para essa célula
    private float min, max;
    //valor da célula
    private float value;
    //valor de preciao utilizado para calcular o novo value em update
    private float step;
    //texto de ajuda
    private String info;
    
    //construtor
    public Cell(int id, int x, int y, float min, float max, float value, float step, String info) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.min = min;
        this.max = max;
        this.value = value;
        this.step = step;
        this.info = info;
    }
    
    /**
     * Verifica se o mouse está sobre este texto.
     * @return id se o mouse estiver sobre o texto;
     * @return 0 do contrário
     */
    public int hit(int mousex, int mousey) {
        if(mousex > x && mousex < x+60 &&
           mousey > y-30 && mousey < y+10)
                return id;
        else
                return 0;
    }
    
    
    /**
     * Atualiza o valor da célula.
     * @param update - Valor que sera usado para atualizar o valor da célula.
     * @param selection - Identificador da célula que foi selecionada.
     */
    public void update(int update, int selection) {
        //verifica se esta célula é a selecionada pelo mouse
        if(selection == id) {
            value += update * step;
            
            //verifica se não estourou os limites da célula
            if (value < min)
                value = min;
            else if (value > max) 
                value = max;
        }
    }
    
    
    /**
     * Retorna a posição x da célula
     */
    public int getX() {
        return x;
    }
        
    /**
     * Retorna a posição y da célula
     */
    public int getY() {
        return y;
    }
    
    /**
     * Retorna o id da célula
     */
    public int getId() {
        return id;
    }
    
    /**
     * Retorna a descrição da célula
     */
    public String getInfo() {
        return info;
    }
    
    /**
     * Retorna o valor da célula.
     */
    public float getValue() {
        return value;
    }
    
    /**
     * Altera o valor da célula.
     */
    public void setValue(float valor) {
        value = valor;
    }
}
