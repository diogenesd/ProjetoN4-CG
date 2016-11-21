package n4;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Star {
	
	private int id;
	private boolean eHMaterial = true;
	private GL gl;
	private GLUT glut;
	private float corWhite[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private n4.BoundingBox bBox;
	public Transformacao4D matrixObject = new Transformacao4D();
	
	private static final float speed = 0.3f;
	private float moveStar;
	
	public void atribuirGLs(GL gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	
}
