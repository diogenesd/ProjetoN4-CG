package n4;

//author Diogenes ademir Domingos,  Eduardo Ferrari Ott
//date 12/10/2016
//version $Revision: 1.0 $
//Obs.: variaveis globais foram usadas por questoes didaticas mas nao sao recomendas para aplicacoes reais.


public class Camera {

	private double xEye, yEye, zEye;
	private double xCenter, yCenter, zCenter;
	
	public Camera() {
		super();
		this.xEye = 0.0f; 			this.yEye = 20.0f; 			this.zEye = 20.0f;
		this.xCenter = 0.0f;		this.yCenter = 0.0f;		this.zCenter = 0.0f;
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

	@Override
	public String toString() {
		return "Camera [xEye=" + xEye + ", yEye=" + yEye + ", zEye=" + zEye + ", xCenter=" + xCenter + ", yCenter="
				+ yCenter + ", zCenter=" + zCenter + "]";
	}
	
}
