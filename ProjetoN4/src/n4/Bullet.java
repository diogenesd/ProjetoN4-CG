package n4;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Bullet {

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
	
	public Bullet() {
		super();
		bBox = new n4.BoundingBox();
		
	}
	public void atribuirGLs(GL gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	public void drawBullet(double tx, double ty, double tz) {

		if (eHMaterial) {
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corRed, 0);
			gl.glEnable(GL.GL_LIGHTING);
		}
		gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			gl.glColor3f(1.0f, 1.0f, 0.0f); 
			gl.glScalef(2.0f, 2.0f, 2.0f);
			gl.glTranslated(tx, ty, tz);
			glut.glutSolidSphere(0.1f, 360, 360);

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
	
	
}
