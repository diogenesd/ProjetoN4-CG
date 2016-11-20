package n4;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Bullet {

	private boolean eHMaterial = true;
	private float corBlue[] = { 0.0f, 0.0f, 1.0f, 1.0f };
	public Transformacao4D matrixObject = new Transformacao4D();
	private GL gl;
	private GLUT glut;
	private int id;
	private static final float speed = 1.0f;
	private float moveBullet;
	
	public Bullet() {
		super();
	}
	
	public void atribuirGLs(GL gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	public void drawBullet() {

		if (eHMaterial) {
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corBlue, 0);
			gl.glEnable(GL.GL_LIGHTING);
		}
		gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			gl.glColor3f(0.0f, 0.0f, 1.0f); 	//AZUL
			gl.glScalef(2.0f, 2.0f, 2.0f);
			glut.glutSolidSphere(0.3f, 360, 360);

		gl.glPopMatrix();

		if (eHMaterial) {
			gl.glDisable(GL.GL_LIGHTING);
		}
	}
	
	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(tx, ty, tz);
		// ATRIBUI ALTERAÇÕES PARA A MATRIZ DESTE OBJETO
		matrixObject = mL.transformMatrix(matrixObject);
		
	}
	
	public void setMatrix(Transformacao4D matrixL){
		this.matrixObject = matrixL;
	}
	public void showMatrix() {
		matrixObject.exibeMatriz();
	}
	public Transformacao4D getMatrixObject() {
		return this.matrixObject;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getMoveBullet() {
		return moveBullet += speed;
	}

	public void setMoveBullet(float move) {
		this.moveBullet = move;
	}
	
	
}
