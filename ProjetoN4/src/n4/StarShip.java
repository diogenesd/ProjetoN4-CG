package n4;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class StarShip {
	 
	private boolean eHMaterial = true;
	private GL gl;
	private GLUT glut;
	private float corRed[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	private n4.BoundingBox bBox;
	public Transformacao4D matrixObject = new Transformacao4D();
	
	private static Transformacao4D matrizTmpTranslacao = new Transformacao4D();
	private static Transformacao4D matrizTmpTranslacaoInversa = new Transformacao4D();
	private static Transformacao4D matrizTmpEscala = new Transformacao4D();
	private static Transformacao4D matrizTmpRotacao = new Transformacao4D();
	private static Transformacao4D matrizGlobal = new Transformacao4D();
	
	
	public StarShip() {
		super();
		bBox = new n4.BoundingBox();
		
	}

	public void atribuirGLs(GL gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	public void drawBbox() {
		gl.glColor3f(1.0f, 1.0f, 0f);	// COR AMARELA
		gl.glLineWidth(3.0f);
		gl.glPushMatrix();
		gl.glMultMatrixd(matrixObject.GetDate(), 0);
		//DESENHA A BBOX
		bBox.desenharOpenGLBBox(gl);
		gl.glPopMatrix();
		
	}

	public void showBbox() {
		System.out.println(this.bBox.toString());
	}
	public void atribuirIdentidade() {
		matrixObject.atribuirIdentidade();
	}
	
	public void drawStarShip() {
		
		if (eHMaterial) {
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corRed, 0);
			gl.glEnable(GL.GL_LIGHTING);
		}
		gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			gl.glScalef(1.0f,1.0f,1.0f);
			gl.glTranslated(0.0f, 0.0f, 0.0f);
			glut.glutSolidCube(2.0f);
		gl.glPopMatrix();
		
		if (eHMaterial) {
			gl.glDisable(GL.GL_LIGHTING);
		}
	}
	
	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(tx, ty, tz);
		// ATRIBUI ALTERA��ES PARA A MATRIZ DESTE OBJETO
		matrixObject = mL.transformMatrix(matrixObject);
		
	}
	public void escalaXYZ(double Sx, double Sy) {
		Transformacao4D matrizScale = new Transformacao4D();
		matrizScale.atribuirEscala(Sx, Sy, 1.0);
		// ATRIBUI ALTERA��ES PARA A MATRIZ DESTE OBJETO
		matrixObject = matrizScale.transformMatrix(matrixObject);
		
	}
	public void escalaXYZPtoFixo(double escala, Ponto4D ptoFixo) {
		// LIMPA MATRIZ GLOBAL
		matrizGlobal.atribuirIdentidade(); 
		// INVERTE COORDENADAS x,y,z,w PARA TRANSLADAR ATE SRU=[0,0]
		ptoFixo.inverterSinal(ptoFixo); 
		// TRANSLADA OBJETO PARA SRU=[0,0], CONSIDERA A DIFEREN�A DO SCREEN (*2)
		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.obterX() * 2, ptoFixo.obterY() * 2, ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);
		// REALIZA A ALTERA��OD DA ESCALA DO OBJETO
		matrizTmpEscala.atribuirEscala(escala, escala, 1.0);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);
		// INVERTE COORDENADAS x,y,z,w PARA TRANSLADAR ATE PONTO INICIAL]
		ptoFixo.inverterSinal(ptoFixo);
		// TRANSLADA OBJETO PARA PONTO INICAL, CONSIDERA A DIFEREN�A DO SCREEN (*2)
		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.obterX() * 2, ptoFixo.obterY() * 2, ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);
		// ATRIBUI ALTERA��ES PARA A MATRIZ DESTE OBJETO
		matrixObject = matrixObject.transformMatrix(matrizGlobal);
		
	}
	
	public void rotacaoZPtoFixo(double angulo, Ponto4D ptoFixo) {
		// LIMPA MATRIZ GLOBAL
		matrizGlobal.atribuirIdentidade();
		// INVERTE COORDENADAS x,y,z,w PARA TRANSLADAR ATE SRU=[0,0]
		ptoFixo.inverterSinal(ptoFixo);
		// TRANSLADA OBJETO PARA SRU=[0,0], CONSIDERA A DIFEREN�A DO SCREEN (*2)
		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.obterX() * 2, ptoFixo.obterY() * 2, ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);
		// REALIZA A ALTERA��O DA ROTA��O DO OBJETO EM RADIANOS
		matrizTmpRotacao.atribuirRotacaoZ(Transformacao4D.DEG_TO_RAD * angulo);
		matrizGlobal = matrizTmpRotacao.transformMatrix(matrizGlobal);
		// INVERTE COORDENADAS x,y,z,w PARA TRANSLADAR ATE PONTO INICIAL]
		ptoFixo.inverterSinal(ptoFixo);
		// TRANSLADA OBJETO PARA PONTO INICAL, CONSIDERA A DIFEREN�A DO SCREEN (*2)
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.obterX() * 2, ptoFixo.obterY() * 2, ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);
		// ATRIBUI ALTERA��ES PARA A MATRIZ DESTE OBJETO
		matrixObject = matrixObject.transformMatrix(matrizGlobal);
		
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

	/**
	 * Retorna a BBOX deste objeto
	 * @return BoundingBox do objeto
	 */
	public n4.BoundingBox getBbox() {
		return this.bBox;
	}
	
	
}
