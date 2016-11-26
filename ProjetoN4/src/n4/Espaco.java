package n4;

import javax.media.opengl.GL;

public class Espaco extends ObjetoGrafico {

	private GL gl;
	private BoundingBox bBox;
	public Transformacao4D matrixObject = new Transformacao4D();
	private float size = 200.0f;
	private double speed = 0.3f;
	private Texture texture;
	private boolean  ativo = true;

	public Espaco(GL gl) {
		super();
		bBox = new BoundingBox();
		texture = new Texture(gl);
		this.gl = gl;
	}

	public void drawBbox() {
		gl.glColor3f(1.0f, 0.0f, 0.0f); // COR VERMELHA
		gl.glLineWidth(2.0f);
		gl.glPushMatrix();
		gl.glMultMatrixd(matrixObject.GetDate(), 0);
		bBox.desenharOpenGLBBox(gl);
		gl.glPopMatrix();

	}

	public void atualizarBBox(float size) {
		n4.Ponto4D pontos[] = { new n4.Ponto4D(size * -1, size * -1, size * -1), new n4.Ponto4D(size, size, size), };

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
		float cordenada = size; // 2.0f;
		atualizarBBox(cordenada);
	}

	public void showBbox() {
		System.out.println(this.bBox.toString());
	}

	public void atribuirIdentidade() {
		matrixObject.atribuirIdentidade();
	}

	public void drawSpace() {

		gl.glPushMatrix();
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			gl.glEnable(GL.GL_TEXTURE_2D); // Primeiro habilita uso de textura
			gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getIdTexture()[0]);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, texture.getBuffer()[0]);
			
			drawFaces();
			
			gl.glDisable(GL.GL_TEXTURE_2D); // Desabilita uso de textura
		gl.glPopMatrix();
		float size = 1;
		atualizarBBox(size);
	}
	
	private void drawFaces() {
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		gl.glScalef(size, size, size);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin (GL.GL_QUADS );
		
		// Especifica a coordenada de textura para cada vertice
			
		// Face Posterior
		gl.glNormal3f(0.0f,0.0f,-1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	
		
		// Face Frontal
		gl.glNormal3f(0.0f,0.0f,1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
		
		// Face Inferior
		gl.glNormal3f(0.0f,1.0f,0.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
		
		// Face Superior
		gl.glNormal3f(0.0f,-1.0f,0.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, 1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, 1.0f,  1.0f);
		
		// Face Esquerda
		gl.glNormal3f(1.0f,0.0f,0.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( -1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( -1.0f,  1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( -1.0f,  1.0f,  1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( -1.0f, -1.0f,  1.0f);
					
		// Face Direita
		gl.glNormal3f(-1.0f,0.0f,0.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
		
		
		gl.glEnd();
	gl.glPopMatrix();
		
	}
	
	
	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(tx, ty, tz);
		// ATRIBUI ALTERAÇÕES PARA A MATRIZ DESTE OBJETO
		matrixObject = mL.transformMatrix(matrixObject);
		
	}
	public void moveRigth(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(speed , 0.0f, 0.0f);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveLeft(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(-speed, 0.0f, 0.0f);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveUp(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(0.0f, 0.0f, -speed);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveDown(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(0.0f, 0.0f, speed);
		matrixObject = mL.transformMatrix(matrixObject);
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

	@Override
	public void drawn() {
		drawSpace();
	}

	@Override
	public boolean isAtivo() {
		return  ativo ;
	}

	@Override
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

}
