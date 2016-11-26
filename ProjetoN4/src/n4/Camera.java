package n4;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

//author Diogenes ademir Domingos,  Eduardo Ferrari Ott
//date 12/10/2016
//version $Revision: 1.0 $
//Obs.: variaveis globais foram usadas por questoes didaticas mas nao sao recomendas para aplicacoes reais.


public class Camera extends ObjetoGrafico{

	private double xEye, yEye, zEye;
	private double xCenter, yCenter, zCenter;
	private Ponto4D camera = new Ponto4D();
	private Transformacao4D matrizObjeto = new Transformacao4D();
	private GL gl;
	private  GLUT glut;
	private boolean ativo = true;
	
	public Camera(GL gl,  GLUT glut) {
		super();
		this.xEye = 0.0f; 			this.yEye = 0.0f; 			this.zEye = 0.0f;
		this.xCenter = 0.0f;		this.yCenter = 0.0f;		this.zCenter = 0.0f;
		this.gl = gl;
		this.glut = glut;
	}
	
	public void translacaoXYZ(double tx, double ty, double tz) {	
		this.xEye += tx; 			this.yEye += ty; 			this.zEye +=tz;
	}

	public double getxEye() {
		return xEye;
	}

	public void setxEye(double xEye) {
		this.xEye = xEye;
	}

	public double getyEye() {
		return yEye;
	}

	public void setyEye(double yEye) {
		this.yEye = yEye;
	}

	public double getzEye() {
		return zEye;
	}

	public void setzEye(double zEye) {
		this.zEye = zEye;
	}

	public double getxCenter() {
		return xCenter;
	}

	public void setxCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public double getyCenter() {
		return yCenter;
	}

	public void setyCenter(double yCenter) {
		this.yCenter = yCenter;
	}

	public double getzCenter() {
		return zCenter;
	}

	public void setzCenter(double zCenter) {
		this.zCenter = zCenter;
	}

	public Ponto4D getCamera() {
		return camera;
	}

	public Transformacao4D getMatrizObjeto() {
		return matrizObjeto;
	}

	public void setMatrizObjeto(Transformacao4D matrizObjeto) {
		this.matrizObjeto = matrizObjeto;
	}

	@Override
	public String toString() {
		return "Camera [xEye=" + xEye + ", yEye=" + yEye + ", zEye=" + zEye + ", xCenter=" + xCenter + ", yCenter="
				+ yCenter + ", zCenter=" + zCenter + "]";
	}

	@Override
	public void drawn() {
		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glLineWidth(2.0f);
		gl.glPointSize(2.0f);
		gl.glPushMatrix();
			gl.glMultMatrixd(matrizObjeto.GetDate(), 0);
			gl.glTranslated(0,10,30);
			gl.glColor3f(0.0f, 1.0f, 1.0f);
			glut.glutSolidSphere(1, 30, 30);

		gl.glPopMatrix();
	}
	
	public void Animacao() {
		giraY(1);
	}
	
	public void giraY(double angulo) {
		Transformacao4D matrizRotacaoY = new Transformacao4D();		
		matrizRotacaoY.atribuirRotacaoY(Transformacao4D.DEG_TO_RAD * angulo);
		matrizObjeto = matrizRotacaoY.transformMatrix(matrizObjeto);
	}

	@Override
	public void atualizarBBox() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showBbox() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRigth() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
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
