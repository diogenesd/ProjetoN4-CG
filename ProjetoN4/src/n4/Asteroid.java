package n4;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

/**
 * @author kfus
 *
 */
public class Asteroid extends ObjetoGrafico{
	 
	private int id;
	private boolean eHMaterial = true;
	private GL gl;
	private GLUT glut;
	private float corYellow[] = { 1.0f, 1.0f, 0.0f, 1.0f };
	private n4.BoundingBox bBox;
	public Transformacao4D matrixObject = new Transformacao4D();
	
	private boolean ativo;
	private static final float speed = 0.05f;
	private float moveAsteroid;
	private float size = 1.5f;
	
	
	
	public Asteroid(GL gl, GLUT glut) {
		super();
		bBox = new n4.BoundingBox();
		this.gl = gl;
		this.glut = glut;
		this.ativo = true;
	}

	public void drawBbox() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);	// COR VERMELHA
		gl.glLineWidth(2.0f);
		gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			bBox.desenharOpenGLBBox(gl);
		gl.glPopMatrix();
		
	}
	
	 public void atualizarBBox(float size) {
	        n4.Ponto4D pontos[] = {new n4.Ponto4D(size * -1, size * -1, size * -1),
	            new n4.Ponto4D(size, size, size),};

	        // atualizar os pontos
	        for (n4.Ponto4D ponto : pontos) {
	            ponto.atribuirX(ponto.obterX() + matrixObject.GetElement(12));
	            ponto.atribuirY(ponto.obterY() + matrixObject.GetElement(13));
	            ponto.atribuirZ(ponto.obterZ() + matrixObject.GetElement(14));
	        }

	        // atualizar a Bbox
	        bBox.setBoundingBox(pontos);
	    }
	 
	 public void atualizarBBox() {
	        float cordenada = size  ; // 2.0f;
	        atualizarBBox(cordenada);
	 }

	public void showBbox() {
		System.out.println(this.bBox.toString());
	}
	public void atribuirIdentidade() {
		matrixObject.atribuirIdentidade();
	}
	
	
	public void drawAsteroid() {
	
		if (ativo) {
			if (eHMaterial) {
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corYellow, 0);
				gl.glEnable(GL.GL_LIGHTING);
			}
			gl.glColor3f(1.0f, 1.0f, 0.0f); //YELLOW
			gl.glPushMatrix();
				gl.glMultMatrixd(matrixObject.GetDate(), 0);
				gl.glTranslated(0.0f, 0.0f, 0.0f);
				gl.glScalef( size , size, size);
				glut.glutSolidCube(size);//glut.glutSolidSphere(1.0f, 360, 360);	
			gl.glPopMatrix();
			
			if (eHMaterial) {
				gl.glDisable(GL.GL_LIGHTING);
			}
			moveDown();
			atualizarBBox(size);
		}
	}
	
	public void randomPosition(double tx, double ty, double tz){
		this.translacaoXYZ(tx, ty, tz);
	}
	
	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(tx, ty, tz);
		// ATRIBUI ALTERAÇÕES PARA A MATRIZ DESTE OBJETO
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveRigth(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(speed, 0.0f, 0.0f);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveLeft(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(-speed, 0.0f, 0.0f);
		matrixObject = mL.transformMatrix(matrixObject);
		atualizarBBox();
	}
	public void moveUp(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(0.0f, 0.0f, -speed);
		matrixObject = mL.transformMatrix(matrixObject);
		atualizarBBox();
	}
	public void moveDown(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(0.0f, 0.0f, speed);
		matrixObject = mL.transformMatrix(matrixObject);
		atualizarBBox();
	}
	
	
	public void escalaXYZ(double Sx, double Sy) {
		Transformacao4D matrizScale = new Transformacao4D();
		matrizScale.atribuirEscala(Sx, Sy, 1.0);
		// ATRIBUI ALTERAÇÕES PARA A MATRIZ DESTE OBJETO
		matrixObject = matrizScale.transformMatrix(matrixObject);
		
	}
		
	public void showMatrix() {
		matrixObject.exibeMatriz();
	}
	public Transformacao4D getMatrixObject() {
		return this.matrixObject;
	}
	/**
	 * Retorna a maior coordenada X da BBox deste objeto
	 * @return double maior coordenada X 
	 */
	public double obterMaiorX() {
		return bBox.obterMaiorX();
	}

	/**
	 * Retorna a menor coordenada X da BBox deste objeto
	 * @return double menor coordenada X
	 */
	public double obterMenorX() {
		return bBox.obterMenorX();
	}

	/**
	 * Retorna a maior coordenada Y da BBox deste objeto
	 * @return double maior coordenada Y
	 */
	public double obterMaiorY() {
		return bBox.obterMaiorY();
	}

	/**
	 * Retorna a menor coordenada Y da BBox deste objeto
	 * @return double menor coordenada Y
	 */
	public double obterMenorY() {
		return bBox.obterMenorY();
	}
	
	public double obterMaiorZ() {
		return bBox.obterMaiorZ();
	}
	
	public double obterMenorZ() {
		return bBox.obterMenorZ();
	}

	/**
	 * Retorna a BBOX deste objeto
	 * @return BoundingBox do objeto
	 */
	public n4.BoundingBox getBbox() {
		return this.bBox;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getMoveAsteroid() {
		return moveAsteroid += speed;
	}

	public void setMoveAsteroid(float moveAsteroid) {
		this.moveAsteroid = moveAsteroid;
	}

	@Override
	public void drawn() {
		drawAsteroid();
	}

	@Override
	public boolean isAtivo() {
		return ativo;
	}

	@Override
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	
	
}
