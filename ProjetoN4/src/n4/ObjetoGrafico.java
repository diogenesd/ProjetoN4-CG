package n4;

public abstract class ObjetoGrafico {

	public abstract void drawn();

	public abstract void translacaoXYZ(double tx, double ty, double tz);
	
	public abstract void atualizarBBox();
	
	public abstract void showBbox();
	
	public abstract void moveRigth();
	
	public abstract void moveLeft();
	
	public abstract void moveUp();
	
	public abstract void moveDown();
	
	public abstract boolean isAtivo();
	
	public abstract void setAtivo(boolean ativo);
	
}
